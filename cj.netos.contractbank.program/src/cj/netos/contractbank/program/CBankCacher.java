package cj.netos.contractbank.program;

import java.util.HashMap;
import java.util.Map;

import cj.netos.contractbank.args.CBankInfo;
import cj.netos.contractbank.bs.ICBankInfoBS;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "marketCacher")
public class CBankCacher implements ICBankCacher {
	@CjServiceRef(refByName = "FSBAEngine.cbankInfoBS")
	ICBankInfoBS cbankInfoBS;
	Map<String, CBankInfo> bankInfos;

	public CBankCacher() {
		bankInfos = new HashMap<>();
	}


	@Override
	public CBankInfo getBankInfo(String bank) {
		CBankInfo r = bankInfos.get(bank);
		if (r != null) {
			return r;
		}
		r = cbankInfoBS.getCBankInfo(bank);
		bankInfos.put(bank, r);
		return r;
	}

}
