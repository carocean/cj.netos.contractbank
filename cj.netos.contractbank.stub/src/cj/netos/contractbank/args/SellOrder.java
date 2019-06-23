package cj.netos.contractbank.args;

import java.math.BigDecimal;

public class SellOrder {
	String code;
	String seller;
	Position position;
	long thingsQuantities;
	BigDecimal sellingPrice;
	BigDecimal cashDepositRate;// 保证金率
	BigDecimal cashDepositAmount;// 实交保证金
	BigDecimal bondPrice;// 购买金证债券价格
	BigDecimal bondQuantities;// 购买的金证债券数量
	String informAddress;
	long ctime;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public long getThingsQuantities() {
		return thingsQuantities;
	}
	public void setThingsQuantities(long thingsQuantities) {
		this.thingsQuantities = thingsQuantities;
	}
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
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
	public String getInformAddress() {
		return informAddress;
	}
	public void setInformAddress(String informAddress) {
		this.informAddress = informAddress;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	
}
