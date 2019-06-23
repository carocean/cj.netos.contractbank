package cj.netos.contractbank.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.contractbank.args.Position;
import cj.netos.contractbank.args.SellOrder;
import cj.studio.ecm.net.CircuitException;

public interface ICBankSellOrderBS {
	static String TABEL_Sell = "orders.sells";
	void sell(String bank, String seller, Position position, BigDecimal sellingPrice, long thingsQuantities,
			String informAddress);
	void complateOrder(String bankno, String orderno, Map<String, Object> response) throws CircuitException;
	SellOrder getSellOrder(String bank, String orderno);

}
