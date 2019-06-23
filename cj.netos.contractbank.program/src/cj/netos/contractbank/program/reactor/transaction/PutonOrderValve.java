package cj.netos.contractbank.program.reactor.transaction;

import java.math.BigDecimal;

import cj.netos.contractbank.bs.ICBankPutonOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;

@CjService(name = "transaction.putonOrder")
public class PutonOrderValve implements IValve {

	@CjServiceRef(refByName = "CoreEngine.transaction#cbankPutonOrderBS")
	ICBankPutonOrderBS cbankPutonOrderBS;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String putter = (String) e.getParameters().get("putter");
		String informAddress = (String) e.getParameters().get("informAddress");
		String what = (String) e.getParameters().get("what");
		BigDecimal unitPrice = (BigDecimal) e.getParameters().get("unitPrice");
		long thingsQuantities = (long) e.getParameters().get("thingsQuantities");
		cbankPutonOrderBS.puton(e.getKey(), putter, what, unitPrice, thingsQuantities, informAddress);

	}

}
