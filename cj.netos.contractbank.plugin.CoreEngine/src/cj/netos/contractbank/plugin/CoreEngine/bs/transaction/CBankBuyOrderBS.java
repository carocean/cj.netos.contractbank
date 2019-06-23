package cj.netos.contractbank.plugin.CoreEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.BuyOrder;
import cj.netos.contractbank.args.Position;
import cj.netos.contractbank.bs.ICBankBuyOrderBS;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.plugin.CoreEngine.db.ICBankStore;
import cj.netos.contractbank.util.BigDecimalConstants;
import cj.netos.fsbank.stub.IFSBankTransactionStub;
import cj.netos.inform.Informer;
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
@CjService(name = "transaction#cbankBuyOrderBS")
public class CBankBuyOrderBS implements ICBankBuyOrderBS,BigDecimalConstants {
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
	@CjServiceRef
	ICBankInfoBS cbankInfoBS;
	@Override
	public void buy(String bank, String buyer, Position position, BigDecimal buyingPrice, long thingsQuantities,
			String informAddress) {
		BuyOrder order=new BuyOrder();
		order.setBuyer(buyer);
		order.setPosition(position);
		order.setBuyingPrice(buyingPrice);
		order.setInformAddress(informAddress);
		order.setThingsQuantities(thingsQuantities);
		order.setCtime(System.currentTimeMillis());
		
		BigDecimal buyCashDepositRate = defaultBuyCashDepositRate(cbankPropertiesBS, bank);
		order.setCashDepositRate(buyCashDepositRate);
		BigDecimal buyCashDepositAmount = buyingPrice.multiply(new BigDecimal(thingsQuantities + ""))
				.multiply(buyCashDepositRate);
		order.setCashDepositAmount(buyCashDepositAmount);
		
		String code = cbankStore.bank(bank).saveDoc(TABEL_Buy, new TupleDocument<>(order));
		order.setCode(code);

		String fsbankInformAddress = String.format("%s?bankno=%s%sorderno=%s",
				site.getProperty("transaction_buyOrder_informAddress"), bank, "%26", code);// 由当前项目接收
		try {
			// 合约银行关联的金证银行
			String fsbankno = this.cbankInfoBS.getCBankInfo(bank).getFsbank();
			fSBankTransactionStub.deposit(fsbankno, buyer, buyCashDepositAmount, fsbankInformAddress);
		} catch (Exception e) {
			cbankStore.bank(bank).deleteDoc(TABEL_Buy, code);
			throw e;
		}
	}
	@Override
	public void complateOrder(String bank, String orderno, Map<String, Object> response) throws CircuitException {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document.parse(String.format("{$set:{'tuple.bondPrice':%s,'tuple.bondQuantities':%s}}",
				response.get("dealBondPrice"), response.get("bondQuantities")));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Buy, filter, update);
		BuyOrder order = getBuyOrder(bank, orderno);
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
	@Override
	public BuyOrder getBuyOrder(String bank, String orderno) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}", TABEL_Buy,
				BuyOrder.class.getName(), orderno);
		IQuery<BuyOrder> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<BuyOrder> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}
}
