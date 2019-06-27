package cj.netos.contractbank.args;

import java.math.BigDecimal;

public class BidOrder {
	String code;
	String bidder;
	long thingsQuantities;//委托买入数量
	BigDecimal biddingPrice;
	BigDecimal cashDepositRate;// 保证金率
	BigDecimal cashDepositAmount;// 实交保证金
	BigDecimal bondPrice;// 购买金证债券价格
	BigDecimal bondQuantities;// 购买的金证债券数量
	String informAddress;
	long ctime;
	String status;//向撮合引擎提交状态,如果不是200则表示未提交成功，则需提交
	String message;
	public BidOrder() {
		this.status="200";
		this.message="ok";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getThingsQuantities() {
		return thingsQuantities;
	}
	public void setThingsQuantities(long thingsQuantities) {
		this.thingsQuantities = thingsQuantities;
	}
	
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public BigDecimal getBiddingPrice() {
		return biddingPrice;
	}
	public void setBiddingPrice(BigDecimal biddingPrice) {
		this.biddingPrice = biddingPrice;
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
