package cj.netos.contractbank.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.contractbank.args.BuyOrder;
import cj.netos.contractbank.args.Position;
import cj.studio.ecm.net.CircuitException;

public interface ICBankBuyOrderBS {
	static String TABEL_Buy="orders.buies";
	
	void buy(String key, String buyer, Position position, BigDecimal buyingPrice, long thingsQuantities, String informAddress);

	void complateOrder(String bankno, String orderno, Map<String, Object> response) throws CircuitException;

	BuyOrder getBuyOrder(String bank, String orderno);

}
