package cj.netos.contractbank.stub;

import java.util.ArrayList;
import java.util.List;

import cj.netos.contractbank.args.BState;
import cj.netos.contractbank.args.CBankInfo;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubCircuitStatusMatches;
import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubReturn;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/manager.service", usage = "市场管理")
public interface ICBankManagerStub {
	@CjStubMethod(usage = "注册合约银行，该方法仅是创建合约银行主体信息，稍后要调用其它方法以完善资料")
	@CjStubReturn(usage = "返回市场号")
	@CjStubCircuitStatusMatches(status = {"405 error becuse exists bank`s name.","404 not found"})
	String registerBank(@CjStubInParameter(key = "bankName", usage = "合约银行名") String bankName,
			@CjStubInParameter(key = "president", usage = "行长") String president,
			@CjStubInParameter(key = "company", usage = "公司") String company,
			@CjStubInParameter(key = "expiredDate", usage = "到期日期 格式：yyyy-MM-dd HH:mm，例：2019-10-30 09:45") String expiredDate)
			throws CircuitException;

	@CjStubMethod(usage = "更新合约银行名")
	void updateBankName(@CjStubInParameter(key = "bank", usage = "合约银行标识编码") String bank,
			@CjStubInParameter(key = "name", usage = "合约银行名") String name) throws CircuitException;

	@CjStubMethod(usage = "更新合约银行行长")
	void updateBankPresident(@CjStubInParameter(key = "bank", usage = "合约银行标识编码") String bank,
			@CjStubInParameter(key = "president", usage = "行长") String president) throws CircuitException;

	@CjStubMethod(usage = "更新合约银行归属企业")
	void updateBankCompany(@CjStubInParameter(key = "bank", usage = "合约银行标识编码") String bank,
			@CjStubInParameter(key = "company", usage = "归属企业") String company) throws CircuitException;

	@CjStubMethod(usage = "吊销指定的合约银行，吊销并不是删除，只是改变状态为吊销")
	void revokeBank(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank) throws CircuitException;

	@CjStubMethod(usage = "冻结指定的合约银行")
	void freezeBank(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank);

	@CjStubMethod(usage = "暂停运行指定的合约银行")
	void closedBank(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank);

	@CjStubMethod(usage = "恢复运行指定的合约银行")
	void resumeBank(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank);

	@CjStubMethod(usage = "获取指定的合约银行信息")
	CBankInfo getBankInfo(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank);

	@CjStubMethod(usage = "分页查询合约银行列表")
	@CjStubReturn(elementType = ArrayList.class, type = CBankInfo.class, usage = "合约银行列表")
	List<CBankInfo> pageBankInfo(@CjStubInParameter(key = "currPage", usage = "当前页码") int currPage,
			@CjStubInParameter(key = "pageSize", usage = "页大小") int pageSize);

	@CjStubMethod(usage = "获取合约银行当前状态")
	BState getBankState(@CjStubInParameter(key = "bank", usage = "合约银行代码") String bank);
}
