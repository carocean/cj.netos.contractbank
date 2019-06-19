package cj.netos.contractbank.bs;

import cj.netos.contractbank.args.CBankState;

public interface ICBankStateBS {
	static String TABLE_CBank_STATE="bstates";
	
	void save(CBankState state);

	CBankState getState(String bank);
	void revokeCBank(String bank);

	void freezeCBank(String bank);

	void closedCBank(String bank);

	void resumeCBank(String bank);
}
