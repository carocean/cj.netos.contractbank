package cj.netos.contractbank.args;

import java.math.BigDecimal;

public class ExchangeOrder {
	String code;
	String exchanger;
	BigDecimal bondQuantities;
	BigDecimal deservedAmount;//得金
	BigDecimal bondPrice;
	String informAddress;
	BigDecimal feeRate;//委托承兑，银行收取一定费率
	BigDecimal feeAmount;//实际收取的费用
	long ctime;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getBondPrice() {
		return bondPrice;
	}
	public void setBondPrice(BigDecimal bondPrice) {
		this.bondPrice = bondPrice;
	}
	public BigDecimal getDeservedAmount() {
		return deservedAmount;
	}
	public void setDeservedAmount(BigDecimal deservedAmount) {
		this.deservedAmount = deservedAmount;
	}
	public String getExchanger() {
		return exchanger;
	}
	public void setExchanger(String exchanger) {
		this.exchanger = exchanger;
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
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	
}
