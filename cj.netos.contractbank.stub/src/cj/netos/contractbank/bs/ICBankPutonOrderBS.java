package cj.netos.contractbank.bs;

import java.math.BigDecimal;
import java.util.Map;

import cj.netos.contractbank.args.PutonOrder;
import cj.studio.ecm.net.CircuitException;

public interface ICBankPutonOrderBS {
	static String TABEL_Puton = "orders.putons";

	void puton(String key, String putter, String what, String units, BigDecimal unitPrice,
			long thingsQuantities, String informAddress);

	void complateOrder(String bankno, String orderno, Map<String, Object> response) throws CircuitException;

	PutonOrder getPutonOrder(String bank, String orderno);

}
