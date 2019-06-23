package cj.netos.contractbank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cj.netos.contractbank.args.CBankProperty;
import cj.netos.contractbank.bs.ICBankPropertiesBS;
import cj.ultimate.util.StringUtil;

public interface BigDecimalConstants {
	static int scale = 16;// 小数位数为16
	static RoundingMode roundingMode = RoundingMode.FLOOR;
	static String default_putonCashDepositRate = "0.16";// 默认投放保证金率
	
	/**
	 * 向委托投单收取的保证金率
	 * @param marketPropertiesBS
	 * @param bank
	 * @return
	 */
	default BigDecimal defaultPutonCashDepositRate(ICBankPropertiesBS cbankPropertiesBS, String bank) {
		String strdefault_putonCashDepositRate = cbankPropertiesBS.get(bank,
				CBankProperty.CONSTANS_KEY_default_putonCashDepositRate);
		if (StringUtil.isEmpty(strdefault_putonCashDepositRate)) {
			strdefault_putonCashDepositRate = default_putonCashDepositRate + "";
		}
		return new BigDecimal(strdefault_putonCashDepositRate).setScale(scale, roundingMode);
	}
}
