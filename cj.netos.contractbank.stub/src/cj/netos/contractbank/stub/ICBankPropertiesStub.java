package cj.netos.contractbank.stub;

import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/properties.service", usage = "帑银合约银行属性")
public interface ICBankPropertiesStub {
	@CjStubMethod(usage = "添加属性")
	void put(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key,
			@CjStubInParameter(key = "value", usage = "value") String value,
			@CjStubInParameter(key = "desc", usage = "属性描述，可为空") String desc);

	@CjStubMethod(usage = "获取属性")
	String get(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "获取属性描述")
	String desc(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "移除属性")
	void remove(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank,
			@CjStubInParameter(key = "key", usage = "key") String key);

	@CjStubMethod(usage = "获取属性")
	String[] enumKey(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank);

	@CjStubMethod(usage = "获取一页属性")
	String[] pageKeys(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank,
			@CjStubInParameter(key = "currPage", usage = "当前页") int currPage,
			@CjStubInParameter(key = "pageSize", usage = "页大小") int pageSize);

	@CjStubMethod(usage = "属性个数")
	long count(@CjStubInParameter(key="bank", usage = "合约银行编号") String bank);
}
