package cj.netos.contractbank.program.reactor.transaction;

import java.math.BigDecimal;

import cj.netos.contractbank.bs.ICBankExchangeOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;

@CjService(name = "transaction.exchangeOrder")
public class ExchangeOrderValve implements IValve {

	@CjServiceRef(refByName = "CoreEngine.transaction#cbankExchangeOrderBS")
	ICBankExchangeOrderBS cbankExchangeOrderBS;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String exchanger = (String) e.getParameters().get("exchanger");
		String informAddress = (String) e.getParameters().get("informAddress");
		BigDecimal bondQuantities = (BigDecimal) e.getParameters().get("bondQuantities");
		cbankExchangeOrderBS.exchange(e.getKey(), exchanger, bondQuantities, informAddress);
	}

}
