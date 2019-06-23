package cj.netos.contractbank.plugin.CoreEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.Position;
import cj.netos.contractbank.args.SellOrder;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.bs.ICBankSellOrderBS;
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
@CjService(name = "transaction#cbankSellOrderBS")
public class CBankSellOrderBS implements ICBankSellOrderBS, BigDecimalConstants {
	@CjServiceRef
	ICBankStore cbankStore;
	@CjStubRef(remote = "rest://remote/fsbank/", stub = IFSBankTransactionStub.class)
	IFSBankTransactionStub fSBankTransactionStub;
	@CjServiceRef
	ICBankInfoBS cbankInfoBS;
	@CjServiceRef
	ICBankPropertiesBS cbankPropertiesBS;
	@CjServiceSite
	IServiceSite site;
	@CjServiceRef(refByName = "$.netos.informer")
	Informer informer;

	@Override
	public void sell(String bank, String seller, Position position, BigDecimal sellingPrice, long thingsQuantities,
			String informAddress) {
		SellOrder order = new SellOrder();
		order.setCtime(System.currentTimeMillis());
		order.setInformAddress(informAddress);
		order.setPosition(position);
		order.setSeller(seller);
		order.setSellingPrice(sellingPrice);
		order.setThingsQuantities(thingsQuantities);

		BigDecimal sellCashDepositRate = defaultSellCashDepositRate(cbankPropertiesBS, bank);
		order.setCashDepositRate(sellCashDepositRate);
		BigDecimal sellCashDepositAmount = sellingPrice.multiply(new BigDecimal(thingsQuantities + ""))
				.multiply(sellCashDepositRate);
		order.setCashDepositAmount(sellCashDepositAmount);

		String code = cbankStore.bank(bank).saveDoc(TABEL_Sell, new TupleDocument<>(order));
		order.setCode(code);

		String fsbankInformAddress = String.format("%s?bankno=%s%sorderno=%s",
				site.getProperty("transaction_sellOrder_informAddress"), bank, "%26", code);// 由当前项目接收
		try {
			// 合约银行关联的金证银行
			String fsbankno = this.cbankInfoBS.getCBankInfo(bank).getFsbank();
			fSBankTransactionStub.deposit(fsbankno, seller, sellCashDepositAmount, fsbankInformAddress);
		} catch (Exception e) {
			cbankStore.bank(bank).deleteDoc(TABEL_Sell, code);
			throw e;
		}
	}

	@Override
	public void complateOrder(String bank, String orderno, Map<String, Object> response) throws CircuitException {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document.parse(String.format("{$set:{'tuple.bondPrice':%s,'tuple.bondQuantities':%s}}",
				response.get("dealBondPrice"), response.get("bondQuantities")));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Sell, filter, update);
		SellOrder order = getSellOrder(bank, orderno);
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
	public SellOrder getSellOrder(String bank, String orderno) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}", TABEL_Sell,
				SellOrder.class.getName(), orderno);
		IQuery<SellOrder> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<SellOrder> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}
}
