package cj.netos.contractbank.bs;

public interface ICBankPropertiesBS {
	static String TABLE_KEY="properties";
	void put(String market, String key, String value,String desc);

	void remove(String market, String key);

	boolean containsKey(String market, String key);

	String get(String market, String key);

	String[] enumKey(String market);

	String[] pageKeys(String market, int currPage, int pageSize);

	long count(String market);

	String desc(String market, String key);

}
