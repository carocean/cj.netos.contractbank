package cj.netos.contractbank.bs;

import java.util.List;

import cj.netos.contractbank.args.CBankInfo;
import cj.studio.ecm.net.CircuitException;

public interface ICBankInfoBS {
	static String TABLE_CBank_INFO = "banks";

	void saveCBank(CBankInfo info) throws CircuitException;

	void updateCBankName(String bank, String name);

	void updateCBankPresident(String bank, String president);

	void updateCBankCompany(String bank, String company);

	CBankInfo getCBankInfo(String bank);

	List<CBankInfo> pageCBankInfo(int currPage, int pageSize);

	boolean existsCBankName(String name);

	boolean existsCBankCode(String bank);

	boolean isExpired(String bank) throws CircuitException;

}
