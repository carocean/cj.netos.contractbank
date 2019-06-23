package cj.netos.contractbank.program.reactor.transaction;

import java.math.BigDecimal;

import cj.netos.contractbank.args.Position;
import cj.netos.contractbank.bs.ICBankBuyOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;

@CjService(name = "transaction.buyOrder")
public class BuyOrderValve implements IValve {

	@CjServiceRef(refByName = "CoreEngine.transaction#cbankBuyOrderBS")
	ICBankBuyOrderBS cbankBuyOrderBS;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String buyer = (String) e.getParameters().get("buyer");
		String informAddress = (String) e.getParameters().get("informAddress");
		Position position = Position.valueOf( e.getParameters().get("position")+"");
		BigDecimal buyingPrice = (BigDecimal) e.getParameters().get("buyingPrice");
		long thingsQuantities = (long) e.getParameters().get("thingsQuantities");
		cbankBuyOrderBS.buy(e.getKey(), buyer, position, buyingPrice, thingsQuantities, informAddress);
	}

}
