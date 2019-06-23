package cj.netos.contractbank.args;

import java.math.BigDecimal;

/**
 * 委托投放单
 * 
 * @author caroceanjofers
 *
 */
public class PutonOrder {
	String code;
	String putter;// 投放者
	String what;// 投放的是什么？采购单号？供货单号？
	BigDecimal unitPrice;// 位价
	long thingsQuantities;
	BigDecimal cashDepositRate;// 保证金率
	BigDecimal cashDepositAmount;// 实交保证金
	BigDecimal bondPrice;// 购买金证债券价格
	BigDecimal bondQuantities;// 购买的金证债券数量
	long ctime;// 投单时间
	String informAddress;// 回调地址

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInformAddress() {
		return informAddress;
	}

	public void setInformAddress(String informAddress) {
		this.informAddress = informAddress;
	}

	public String getPutter() {
		return putter;
	}

	public void setPutter(String putter) {
		this.putter = putter;
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public long getThingsQuantities() {
		return thingsQuantities;
	}

	public void setThingsQuantities(long thingsQuantities) {
		this.thingsQuantities = thingsQuantities;
	}

	public BigDecimal getCashDepositRate() {
		return cashDepositRate;
	}

	public void setCashDepositRate(BigDecimal cashDepositRate) {
		this.cashDepositRate = cashDepositRate;
	}

	public BigDecimal getCashDepositAmount() {
		return cashDepositAmount;
	}

	public void setCashDepositAmount(BigDecimal cashDepositAmount) {
		this.cashDepositAmount = cashDepositAmount;
	}

	public BigDecimal getBondPrice() {
		return bondPrice;
	}

	public void setBondPrice(BigDecimal bondPrice) {
		this.bondPrice = bondPrice;
	}

	public BigDecimal getBondQuantities() {
		return bondQuantities;
	}

	public void setBondQuantities(BigDecimal bondQuantities) {
		this.bondQuantities = bondQuantities;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

}
