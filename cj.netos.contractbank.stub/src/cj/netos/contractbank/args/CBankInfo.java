package cj.netos.contractbank.args;

public class CBankInfo {
	String code;
	String name;
	String president;//行长
	String company;//归属的公司
	BState bstate;//市场运行状态：暂停，运行中
	long ctime;
	long expiredTime;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPresident() {
		return president;
	}
	public void setPresident(String president) {
		this.president = president;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public BState getBstate() {
		return bstate;
	}
	public void setBstate(BState bstate) {
		this.bstate = bstate;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	
}
