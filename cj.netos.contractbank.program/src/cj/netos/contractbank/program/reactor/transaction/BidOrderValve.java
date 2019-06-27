package cj.netos.contractbank.program.reactor.transaction;

import java.math.BigDecimal;

import cj.netos.contractbank.bs.ICBankBidOrderBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.util.reactor.Event;
import cj.studio.util.reactor.IPipeline;
import cj.studio.util.reactor.IValve;

@CjService(name = "transaction.bidOrder")
public class BidOrderValve implements IValve {

	@CjServiceRef(refByName = "CoreEngine.transaction#cbankBidOrderBS")
	ICBankBidOrderBS cbankBidOrderBS;

	@Override
	public void flow(Event e, IPipeline pipeline) throws CircuitException {
		String bidder = (String) e.getParameters().get("bidder");
		String informAddress = (String) e.getParameters().get("informAddress");
		BigDecimal biddingPrice = (BigDecimal) e.getParameters().get("biddingPrice");
		long thingsQuantities = (long) e.getParameters().get("thingsQuantities");
		cbankBidOrderBS.bid(e.getKey(), bidder,  biddingPrice, thingsQuantities, informAddress);
	}

}
