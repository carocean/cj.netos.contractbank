package cj.netos.contractbank.args;

public class CBankProperty {
	public static transient final String CONSTANS_KEY_default_putonCashDepositRate = "defaults.putonCashDepositRate";
	public static transient final String CONSTANS_KEY_default_sellCashDepositRate = "defaults.sellCashDepositRate";
	public static transient final String CONSTANS_KEY_default_buyCashDepositRate = "defaults.buyCashDepositRate";
	public static transient final String CONSTANS_KEY_default_exchangeFeeRate = "defaults.exchangeFeeRate";
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
