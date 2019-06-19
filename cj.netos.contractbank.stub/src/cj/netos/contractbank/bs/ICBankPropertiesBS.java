package cj.netos.contractbank.bs;

public interface ICBankPropertiesBS {
	static String TABLE_KEY="properties";
	void put(String bank, String key, String value,String desc);

	void remove(String bank, String key);

	boolean containsKey(String bank, String key);

	String get(String bank, String key);

	String[] enumKey(String bank);

	String[] pageKeys(String bank, int currPage, int pageSize);

	long count(String bank);

	String desc(String bank, String key);

}
