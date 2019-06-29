package cj.netos.contractbank.plugin.CoreEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.BidOrder;
import cj.netos.contractbank.bs.ICBankBidOrderBS;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.netos.contractbank.util.BigDecimalConstants;
import cj.netos.fsbank.stub.IFSBankTransactionStub;
import cj.netos.inform.Informer;
import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.stub.IDeliveryQueueStub;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.ecm.net.io.MemoryOutputChannel;
import cj.studio.gateway.stub.annotation.CjStubRef;
import cj.ultimate.util.StringUtil;

@CjBridge(aspects = "@rest")
@CjService(name = "transaction#cbankBidOrderBS")
public class CBankBidOrderBS implements ICBankBidOrderBS, BigDecimalConstants {
	@CjServiceRef
	ICBankStore cbankStore;
	@CjServiceRef
	ICBankPropertiesBS cbankPropertiesBS;
	@CjServiceSite
	IServiceSite site;
	@CjServiceRef(refByName = "$.netos.informer")
	Informer informer;
	@CjStubRef(remote = "rest://remote/fsbank/", stub = IFSBankTransactionStub.class)
	IFSBankTransactionStub fSBankTransactionStub;
	@CjStubRef(remote = "rest://remote/xdm/", stub = IDeliveryQueueStub.class)
	IDeliveryQueueStub deliveryQueueStub;
	@CjServiceRef
	ICBankInfoBS cbankInfoBS;

	@Override
	public void bid(String bank, String bidder, BigDecimal biddingPrice, long thingsQuantities, String informAddress) {
		BidOrder order = new BidOrder();
		order.setBidder(bidder);
		order.setBiddingPrice(biddingPrice);
		order.setInformAddress(informAddress);
		order.setThingsQuantities(thingsQuantities);
		order.setCtime(System.currentTimeMillis());

		BigDecimal bidCashDepositRate = bidCashDepositRate(cbankPropertiesBS, bank);
		order.setCashDepositRate(bidCashDepositRate);
		BigDecimal bidCashDepositAmount = biddingPrice.multiply(new BigDecimal(thingsQuantities + ""))
				.multiply(bidCashDepositRate);
		order.setCashDepositAmount(bidCashDepositAmount);

		String code = cbankStore.bank(bank).saveDoc(TABEL_Bid, new TupleDocument<>(order));
		order.setCode(code);

		String fsbankInformAddress = String.format("%s?bankno=%s%sorderno=%s",
				site.getProperty("transaction_bidOrder_informAddress"), bank, "%26", code);// 由当前项目接收
		try {
			// 合约银行关联的金证银行
			String fsbankno = this.cbankInfoBS.getCBankInfo(bank).getFsbank();
			BigDecimal rebateRate=rebateRate(cbankPropertiesBS, bank);
			fSBankTransactionStub.deposit(fsbankno, bidder, bidCashDepositAmount,rebateRate, fsbankInformAddress);
		} catch (Exception e) {
			cbankStore.bank(bank).deleteDoc(TABEL_Bid, code);
			throw e;
		}
	}

	@Override
	public void complateOrder(String bank, String orderno, Map<String, Object> response) throws CircuitException {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document.parse(String.format("{$set:{'tuple.bondPrice':%s,'tuple.bondQuantities':%s}}",
				response.get("dealBondPrice"), response.get("bondQuantities")));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Bid, filter, update);
		BidOrder order = getBidOrder(bank, orderno);

		BidOrderStock stock = new BidOrderStock();
		stock.setBondQuantities(order.getBondQuantities());
		stock.setOrderno(order.getCode());
		stock.setBidder(order.getBidder());
		stock.setBiddingPrice(order.getBiddingPrice());
		stock.setBiddingQuantities(order.getThingsQuantities());
		stock.setOtime(System.currentTimeMillis());
		stock.setCashDepositRate(order.getCashDepositRate());
		try {
			deliveryQueueStub.biddingQueue(bank, stock);
		} catch (Exception e) {
			CircuitException ce = CircuitException.search(e);
			String status = "";
			String message = String.format("提交到撮合交易引擎出错，原因：%s", e);
			if (ce != null) {
				status = ce.getStatus();
			} else {
				status = "500";
			}
			updateStatus(bank, order.getCode(), status, message);
			order.setStatus(status);
			order.setMessage(message);
			CJSystem.logging().error(getClass(), message);
		}
		String informAddress = order.getInformAddress();
		if (!StringUtil.isEmpty(informAddress)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order", order);
			Frame f = informer.createFrame(informAddress, map);
			MemoryOutputChannel oc = new MemoryOutputChannel();
			Circuit c = new Circuit(oc, "http/1.1 200 ok");
			informer.inform(f, c);
		}
	}

	private void updateStatus(String bank, String orderno, String status, String message) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document
				.parse(String.format("{'$set':{'tuple.message':'%s','tuple.status':'%s'}}", message, status));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Bid, filter, update);
	}

	@Override
	public BidOrder getBidOrder(String bank, String orderno) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}", TABEL_Bid,
				BidOrder.class.getName(), orderno);
		IQuery<BidOrder> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<BidOrder> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}
}
