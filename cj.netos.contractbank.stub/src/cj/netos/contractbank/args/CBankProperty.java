package cj.netos.contractbank.args;

public class CBankProperty {
	public static transient final String CONSTANS_KEY_policy_putonCashDepositRate = "policy.putonCashDepositRate";
	public static transient final String CONSTANS_KEY_policy_bidCashDepositRate = "policy.bidCashDepositRate";
	public static transient final String CONSTANS_KEY_policy_exchangeFeeRate = "policy.exchangeFeeRate";
	public static transient final String CONSTANS_KEY_policy_contract_dealType = "policy.contract.dealType";
	public static transient final String CONSTANS_KEY_policy_contract_expiredTimeWin = "policy.contract.expiredWin";
	public static transient final String CONSTANS_KEY_policy_contract_breakRate =  "policy.contract.breakRate";
	String key;
	String value;
	String desc;
	String bank;

	public CBankProperty() {
		// TODO Auto-generated constructor stub
	}

	public CBankProperty(String bank, String key, String value,String desc) {
		this.bank = bank;
		this.key = key;
		this.value = value;
		this.desc=desc;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
}
