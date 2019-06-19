package cj.netos.contractbank.program.stub;

import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.netos.contractbank.stub.ICBankPropertiesStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;
@CjService(name="/properties.service")
public class CBankPropertiesStub extends GatewayAppSiteRestStub implements ICBankPropertiesStub {
	@CjServiceRef(refByName = "MarketEngine.cbankPropertiesBS")
	ICBankPropertiesBS cbankPropertiesBS;
	@Override
	public void put(String bank, String key, String value,String desc) {
		cbankPropertiesBS.put(bank,key,value,desc);
	}

	@Override
	public String get(String bank, String key) {
		return cbankPropertiesBS.get(bank,key);
	}

	@Override
	public String[] enumKey(String bank) {
		return cbankPropertiesBS.enumKey(bank);
	}
	@Override
	public String desc(String bank, String key) {
		return cbankPropertiesBS.desc(bank,key);
	}
	@Override
	public String[] pageKeys(String bank, int currPage, int pageSize) {
		return cbankPropertiesBS.pageKeys(bank,currPage,pageSize);
	}

	@Override
	public long count(String bank) {
		return cbankPropertiesBS.count(bank);
	}
	@Override
	public void remove(String bank, String key) {
		cbankPropertiesBS.remove(bank, key);;
	}

}
