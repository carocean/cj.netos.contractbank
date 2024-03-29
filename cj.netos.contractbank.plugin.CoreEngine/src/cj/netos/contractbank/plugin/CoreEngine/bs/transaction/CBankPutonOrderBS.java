package cj.netos.contractbank.plugin.CoreEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.PutonOrder;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.bs.ICBankPutonOrderBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.netos.contractbank.util.BigDecimalConstants;
import cj.netos.fsbank.stub.IFSBankTransactionStub;
import cj.netos.inform.Informer;
import cj.netos.x.dealmaking.args.DealType;
import cj.netos.x.dealmaking.args.PutonOrderStock;
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
@CjService(name = "transaction#cbankPutonOrderBS")
public class CBankPutonOrderBS implements ICBankPutonOrderBS, BigDecimalConstants {
	@CjServiceRef
	ICBankStore cbankStore;
	@CjServiceRef
	ICBankPropertiesBS cbankPropertiesBS;
	@CjStubRef(remote = "rest://remote/fsbank/", stub = IFSBankTransactionStub.class)
	IFSBankTransactionStub fSBankTransactionStub;
	@CjStubRef(remote = "rest://remote/xdm/", stub = IDeliveryQueueStub.class)
	IDeliveryQueueStub deliveryQueueStub;
	@CjServiceRef
	ICBankInfoBS cbankInfoBS;
	@CjServiceSite
	IServiceSite site;
	@CjServiceRef(refByName = "$.netos.informer")
	Informer informer;

	@Override
	public void puton(String bank, String putter, String what, BigDecimal unitPrice, long thingsQuantities,
			String informAddress) {
		PutonOrder order = new PutonOrder();
		order.setCtime(System.currentTimeMillis());
		order.setPutter(putter);
		order.setWhat(what);
		order.setUnitPrice(unitPrice);
		order.setThingsQuantities(thingsQuantities);
		order.setInformAddress(informAddress);
		// 保证金率
		BigDecimal putonCashDepositRate = putonCashDepositRate(cbankPropertiesBS, bank);
		order.setCashDepositRate(putonCashDepositRate);
		BigDecimal putonCashDepositAmount = unitPrice.multiply(new BigDecimal(thingsQuantities + ""))
				.multiply(putonCashDepositRate);
		order.setCashDepositAmount(putonCashDepositAmount);

		String code = cbankStore.bank(bank).saveDoc(TABEL_Puton, new TupleDocument<>(order));
		order.setCode(code);

		String fsbankInformAddress = String.format("%s?bankno=%s%sorderno=%s",
				site.getProperty("transaction_putonOrder_informAddress"), bank, "%26", code);// 由当前项目接收
		try {
			// 合约银行关联的金证银行
			String fsbankno = this.cbankInfoBS.getCBankInfo(bank).getFsbank();
			BigDecimal rebateRate=rebateRate(cbankPropertiesBS, bank);
			fSBankTransactionStub.deposit(fsbankno, putter, putonCashDepositAmount,rebateRate, fsbankInformAddress);
		} catch (Exception e) {
			cbankStore.bank(bank).deleteDoc(TABEL_Puton, code);
			throw e;
		}

	}

//{"freeRate":0.2000000000000000,"amount":16.000000000000000000,"bondAmount":11.2000000000000000,"reserveRate":0.1000000000000000,"freeAmount":3.2000000000000000,"dealBondPrice":0.0016115286242273,"dtime":1561288197476,"bondQuantities":6949.9230926600547189,"reserveAmount":1.6000000000000000,"source":"5d0f5e051020b095a244ffd1","balance.individualBondQuantities":6893858.6596645680547189,"newBondPrice":0.0016117288546751}
	@Override
	public void complateOrder(String bank, String orderno, Map<String, Object> response) throws CircuitException {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document.parse(String.format("{$set:{'tuple.bondPrice':%s,'tuple.bondQuantities':%s}}",
				response.get("dealBondPrice"), response.get("bondQuantities")));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Puton, filter, update);
		PutonOrder order = getPutonOrder(bank, orderno);

		PutonOrderStock stock = new PutonOrderStock();
		stock.setBondQuantities(order.getBondQuantities());
		stock.setOrderno(order.getCode());
		stock.setPutter(order.getPutter());
		stock.setPuttingPrice(order.getUnitPrice());
		stock.setPuttingQuantities(order.getThingsQuantities());
		stock.setOtime(System.currentTimeMillis());
		String dealtype =contract_dealType(cbankPropertiesBS, bank);
		stock.setDealType(DealType.valueOf(dealtype));
		stock.setCashDepositRate(order.getCashDepositRate());
		try {
			deliveryQueueStub.putonQueue(bank, stock);
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
		this.cbankStore.bank(bank).updateDocOne(TABEL_Puton, filter, update);
	}

	@Override
	public PutonOrder getPutonOrder(String bank, String orderno) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}", TABEL_Puton,
				PutonOrder.class.getName(), orderno);
		IQuery<PutonOrder> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<PutonOrder> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}
}
