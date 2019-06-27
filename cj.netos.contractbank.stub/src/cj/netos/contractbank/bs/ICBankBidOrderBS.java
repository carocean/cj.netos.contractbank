package cj.netos.contractbank.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.contractbank.args.BidOrder;
import cj.studio.ecm.net.CircuitException;

public interface ICBankBidOrderBS {
	static String TABEL_Bid="orders.bids";
	
	void bid(String key, String bidder, BigDecimal biddingPrice, long thingsQuantities, String informAddress);

	void complateOrder(String bankno, String orderno, Map<String, Object> response) throws CircuitException;

	BidOrder getBidOrder(String bank, String orderno);

}
