package cj.netos.contractbank.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.contractbank.args.ExchangeOrder;
import cj.studio.ecm.net.CircuitException;

public interface ICBankExchangeOrderBS {
	static String TABEL_Exchange = "orders.exchanges";

	void exchange(String bank, String exchanger, BigDecimal bondQuantities, String informAddress);

	void complateOrder(String bankno, String orderno, Map<String, Object> response) throws CircuitException;

	ExchangeOrder getExchangeOrder(String bank, String orderno);

}
