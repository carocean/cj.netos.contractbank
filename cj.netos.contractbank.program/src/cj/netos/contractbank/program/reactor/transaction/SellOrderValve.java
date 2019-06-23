package cj.netos.contractbank.program.reactor.transaction;

import java.math.BigDecimal;

import cj.netos.contractbank.args.Position;
import cj.netos.contractbank.bs.ICBankSellOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;

@CjService(name = "transaction.sellOrder")
public class SellOrderValve implements IValve {

	@CjServiceRef(refByName = "CoreEngine.transaction#cbankSellOrderBS")
	ICBankSellOrderBS cbankSellOrderBS;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String seller = (String) e.getParameters().get("seller");
		String informAddress = (String) e.getParameters().get("informAddress");
		Position position = Position.valueOf( e.getParameters().get("position")+"");
		BigDecimal sellingPrice = (BigDecimal) e.getParameters().get("sellingPrice");
		long thingsQuantities = (long) e.getParameters().get("thingsQuantities");
		cbankSellOrderBS.sell(e.getKey(), seller, position, sellingPrice, thingsQuantities, informAddress);
	}

}
