package cj.netos.contractbank.program.stub;

import java.math.BigDecimal;

import cj.netos.contractbank.stub.ICBankTransactionStub;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceSite;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IReactor;

@CjService(name = "/transaction.service")
public class CBankTransactionStub extends GatewayAppSiteRestStub implements ICBankTransactionStub {
	@CjServiceSite
	IServiceSite site;
	IReactor reactor;

	protected IReactor getReactor() {
		if (reactor == null) {
			reactor = (IReactor) site.getService("$.reactor");
		}
		return reactor;
	}

	@Override
	public void putonOrder(String bank, String putter, String what, BigDecimal unitPrice, long thingsQuantities,
			String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(bank, "transaction.putonOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("putter", putter);
		e.getParameters().put("what", what);
		e.getParameters().put("unitPrice", unitPrice);
		e.getParameters().put("thingsQuantities", thingsQuantities);
		reactor.input(e);
	}

	@Override
	public void bidOrder(String bank, String bidder,  long thingsQuantities, BigDecimal biddingPrice,
			String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(bank, "transaction.bidOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("bidder", bidder);
		e.getParameters().put("thingsQuantities", thingsQuantities);
		e.getParameters().put("biddingPrice", biddingPrice);
		reactor.input(e);
	}

	@Override
	public void exchangeOrder(String bank, String exchanger, BigDecimal bondQuantities, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(bank, "transaction.exchangeOrder");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("exchanger", exchanger);
		e.getParameters().put("bondQuantities", bondQuantities);
		reactor.input(e);
	}

	@Override
	public void cashout(String bank, String cashouter, BigDecimal amount, String informAddress) {
		IReactor reactor = getReactor();
		Event e = new Event(bank, "transaction.cashout");
		e.getParameters().put("informAddress", informAddress);
		e.getParameters().put("cashouter", cashouter);
		e.getParameters().put("amount", amount);
		reactor.input(e);
	}

}
