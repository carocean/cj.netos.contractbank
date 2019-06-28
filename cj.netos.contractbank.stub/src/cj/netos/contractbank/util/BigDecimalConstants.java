package cj.netos.contractbank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cj.netos.contractbank.args.CBankProperty;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.ultimate.util.StringUtil;

public interface BigDecimalConstants {
	static int scale = 16;// 小数位数为16
	static RoundingMode roundingMode = RoundingMode.FLOOR;
	static String default_putonCashDepositRate = "0.20";// 默认投放保证金率
	static String default_bidCashDepositRate = "0.16";// 默认委托买单保证金率
	static String default_exchangeFeeRate = "0.05";// 默认委托承兑收取费率
	static String default_contract_breakRate = "0.05";// 交割期间违约的处罚比率
	static String default_contract_expiredTimeWin = "43200000";// 交割时间窗12小时
	static String default_contract_dealType = "goods";
	public default long contract_ExpiredTimeWin(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String contract_ExpireTimeWin = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_contract_expiredTimeWin);
		if (StringUtil.isEmpty(contract_ExpireTimeWin)) {
			contract_ExpireTimeWin = default_contract_expiredTimeWin + "";
		}
		return Long.valueOf(contract_ExpireTimeWin);
	} 
	public default BigDecimal contract_contract_breakRate(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String contract_ExpireTimeWin = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_contract_breakRate);
		if (StringUtil.isEmpty(contract_ExpireTimeWin)) {
			contract_ExpireTimeWin = default_contract_breakRate + "";
		}
		return new BigDecimal(contract_ExpireTimeWin);
	} 
	public default String contract_dealType(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String contract_actor = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_contract_dealType);
		if (StringUtil.isEmpty(contract_actor)) {
			contract_actor = default_contract_dealType + "";
		}
		return contract_actor;
	} 
	/**
	 * 向委托投单收取的保证金率
	 * @param marketPropertiesBS
	 * @param bank
	 * @return
	 */
	public default BigDecimal putonCashDepositRate(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String strdefault_putonCashDepositRate = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_putonCashDepositRate);
		if (StringUtil.isEmpty(strdefault_putonCashDepositRate)) {
			strdefault_putonCashDepositRate = default_putonCashDepositRate + "";
		}
		return new BigDecimal(strdefault_putonCashDepositRate).setScale(scale, roundingMode);
	}
	
	/**
	 * 委托买单保证金率
	 * @param marketPropertiesBS
	 * @param bank
	 * @return
	 */
	public default BigDecimal bidCashDepositRate(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String strdefault_putonCashDepositRate = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_bidCashDepositRate);
		if (StringUtil.isEmpty(strdefault_putonCashDepositRate)) {
			strdefault_putonCashDepositRate = default_bidCashDepositRate + "";
		}
		return new BigDecimal(strdefault_putonCashDepositRate).setScale(scale, roundingMode);
	}
	/**
	 * 向委托承况收取的费率
	 * @param marketPropertiesBS
	 * @param bank
	 * @return
	 */
	public default BigDecimal exchangeFeeRate(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String strdefault_putonCashDepositRate = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_policy_exchangeFeeRate);
		if (StringUtil.isEmpty(strdefault_putonCashDepositRate)) {
			strdefault_putonCashDepositRate = default_exchangeFeeRate + "";
		}
		return new BigDecimal(strdefault_putonCashDepositRate).setScale(scale, roundingMode);
	}
}
