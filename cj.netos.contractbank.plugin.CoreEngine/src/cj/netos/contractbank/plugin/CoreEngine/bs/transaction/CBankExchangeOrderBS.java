package cj.netos.contractbank.plugin.CoreEngine.bs.transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.contractbank.args.ExchangeOrder;
import cj.netos.contractbank.bs.ICBankExchangeOrderBS;
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
@CjService(name = "transaction#cbankExchangeOrderBS")
public class CBankExchangeOrderBS implements ICBankExchangeOrderBS, BigDecimalConstants {
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
	public void exchange(String bank, String exchanger, BigDecimal bondQuantities, String informAddress) {
		ExchangeOrder order = new ExchangeOrder();
		order.setBondQuantities(bondQuantities);
		order.setExchanger(exchanger);
		order.setCtime(System.currentTimeMillis());
		order.setInformAddress(informAddress);
		BigDecimal feeRate = exchangeFeeRate(cbankPropertiesBS, bank);
		order.setFeeRate(feeRate);

		String code = cbankStore.bank(bank).saveDoc(TABEL_Exchange, new TupleDocument<>(order));
		order.setCode(code);

		String fsbankInformAddress = String.format("%s?bankno=%s%sorderno=%s",
				site.getProperty("transaction_exchangeOrder_informAddress"), bank, "%26", code);// 由当前项目接收
		try {
			// 合约银行关联的金证银行
			String fsbankno = this.cbankInfoBS.getCBankInfo(bank).getFsbank();
			fSBankTransactionStub.exchange(fsbankno, exchanger, bondQuantities, fsbankInformAddress);
		} catch (Exception e) {
			cbankStore.bank(bank).deleteDoc(TABEL_Exchange, code);
			throw e;
		}
	}

//{dealtime=1.561307823935E12, dealBondPrice=0.001613324462196, dealBondQuantities=100.0, source=5d0faaaf1020b095a244ffda, deservedAmount=0.16, newBondPrice=0.001613324462196}
	@Override
	public void complateOrder(String bank, String orderno, Map<String, Object> response) throws CircuitException {
		// 委托承兑银行要收取一定费率
		ExchangeOrder order = getExchangeOrder(bank, orderno);
		BigDecimal deservedAmount = new BigDecimal(response.get("deservedAmount") + "");
		BigDecimal cashAmount = deservedAmount.multiply(new BigDecimal("1.0").subtract(order.getFeeRate()));
		cashAmount=cashAmount.setScale(2, roundingMode);
		BigDecimal feeAmount = deservedAmount.subtract(cashAmount);
		
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", orderno));
		Bson update = Document
				.parse(String.format("{$set:{'tuple.bondPrice':%s,'tuple.deservedAmount':%s,'tuple.feeAmount':%s}}",
						response.get("dealBondPrice"), cashAmount, feeAmount));
		this.cbankStore.bank(bank).updateDocOne(TABEL_Exchange, filter, update);

		String informAddress = order.getInformAddress();
		if (!StringUtil.isEmpty(informAddress)) {
			Map<String, Object> map = new HashMap<String, Object>();
			order = getExchangeOrder(bank, orderno);// 再取一遍
			map.put("order", order);
			Frame f = informer.createFrame(informAddress, map);
			MemoryOutputChannel oc = new MemoryOutputChannel();
			Circuit c = new Circuit(oc, "http/1.1 200 ok");
			informer.inform(f, c);
		}
	}

	@Override
	public ExchangeOrder getExchangeOrder(String bank, String orderno) {
		String cjql = String.format("select {'tuple':'*'} from tuple %s %s where {'_id':ObjectId('%s')}",
				TABEL_Exchange, ExchangeOrder.class.getName(), orderno);
		IQuery<ExchangeOrder> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<ExchangeOrder> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setCode(doc.docid());
		return doc.tuple();
	}
}
