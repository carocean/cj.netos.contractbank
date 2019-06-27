package cj.netos.contractbank.stub;

import java.math.BigDecimal;

import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

//交易通过切换Engine来实现不同交易策略，Engine以插件形式实现，运行前放入什么插件提供什么样的交易策略，有：MinEngine支持小额交易（小卖场）；MaxEngine支持大额交易（大卖场）；ProcurementEngine支持采购交易（服务厅）
@CjStubService(bindService = "/transaction.service", usage = "合约银行交易服务")
public interface ICBankTransactionStub {
	// 委托投单：供方（供应商售货单）或需方（采购方采购单）
	@CjStubMethod(usage = "委托投单")
	void putonOrder(@CjStubInParameter(key = "bank", usage = "合约银行行号") String bank,
			@CjStubInParameter(key = "putter", usage = "委托投单者编号") String putter,
			@CjStubInParameter(key = "what", usage = "投放的东西。可能是商品编号也可能是需求编号") String what,
			@CjStubInParameter(key = "unitPrice", usage = "单价") BigDecimal unitPrice,
			@CjStubInParameter(key = "thingsQuantities", usage = "委托投放的东西数量") long thingsQuantities,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	// 委托买单
	@CjStubMethod(usage = "委托买单")
	void bidOrder(@CjStubInParameter(key = "bank", usage = "合约银行行号") String bank,
			@CjStubInParameter(key = "bidder", usage = "委托买入者编号") String bidder,
			@CjStubInParameter(key = "thingsQuantities", usage = "委托申购数量") long thingsQuantities,
			@CjStubInParameter(key = "biddingPrice", usage = "委托申购价格") BigDecimal biddingPrice,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	// 委托合约银行向金证银行承兑
	@CjStubMethod(usage = "委托合约银行向金证银行承兑")
	void exchangeOrder(@CjStubInParameter(key = "bank", usage = "合约银行行号") String bank,
			@CjStubInParameter(key = "exchanger", usage = "委托承兑者编号") String exchanger,
			@CjStubInParameter(key = "bondQuantities", usage = "委托承兑的债券数量。债券是平仓或交割出来的在个人账户中的债券余额") BigDecimal bondQuantities,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);

	// 提现
	@CjStubMethod(usage = "提现")
	void cashout(@CjStubInParameter(key = "bank", usage = "合约银行行号") String bank,
			@CjStubInParameter(key = "cashouter", usage = "提现者编号") String cashouter,
			@CjStubInParameter(key = "amount", usage = "申请金额") BigDecimal amount,
			@CjStubInParameter(key = "informAddress", usage = "回调通知地址") String informAddress);
}
