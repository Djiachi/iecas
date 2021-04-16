package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5588余额单元
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:31
 */
public class X5528BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 5035068883811420241L;
	/** 余额单元代码 : 余额单元代码 [19,0] Not NULL */
	private String balanceUnitCode;
	/** 余额类型，P-本金 I-利息 F-费用 : 余额类型，P-本金 I-利息 F-费用 [1,0] */
	private String balanceType;
	/** 账户代码 : 账户代码 [30,0] Not NULL */
	private String accountNo;
	/** 币种 : 币种 [3,0] Not NULL */
	private String currencyCode;
	/** 周期号 : 周期号 [10,0] */
	private Integer cycleNo;
	/** 余额 : 余额 [18,0] */
	private BigDecimal balance;
	/** 计息年利率 : 计息年利率 [8,7] */
	private BigDecimal annualInterestRate;
	/** 累计利息 : 累计利息 [18,0] */
	private BigDecimal accumulatedInterest;
	/** 当期最低还款 : 当期最低还款 [18,0] */
	private BigDecimal currentMinRepayment;
	/** 余额对象 : 余额对象 [9,0] */
	private String balanceObject;
	/** 下一结息日期 yyyy-MM-dd : 下一结息日期 [10,0] */
	private String nextInterestDate;
	/** 上一结息处理日 yyyy-MM-dd : 上一结息处理日 [10,0] */
	private String prevInterestProcesseDate;
	/** 资产属性 : 资产属性 [2,0] */
	private String assetProperties;
	/** 余额建立日期 yyyy-MM-dd : 余额建立日期 [10,0] */
	private String createDate;
	/** 余额结束日期 yyyy-MM-dd : 余额结束日期 [10,0] */
	private String endDate;
	/** 第1次最低比例, 没有小数，程序自行除以100 : 第1次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio1;
	/** 第1次最低日期 yyyy-MM-dd : 第1次最低日期 [10,0] */
	private String lowestRatioDate1;
	/** 第2次最低比例, 没有小数，程序自行除以100 : 第2次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio2;
	/** 第2次最低日期 yyyy-MM-dd : 第2次最低日期 [10,0] */
	private String lowestRatioDate2;
	/** 第3次最低比例, 没有小数，程序自行除以100 : 第3次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio3;
	/** 第3次最低日期 yyyy-MM-dd : 第3次最低日期 [10,0] */
	private String lowestRatioDate3;
	/** 第4次最低比例, 没有小数，程序自行除以100 : 第4次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio4;
	/** 第4次最低日期 yyyy-MM-dd : 第4次最低日期 [10,0] */
	private String lowestRatioDate4;
	/** 第5次最低比例, 没有小数，程序自行除以100 : 第5次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio5;
	/** 第5次最低日期 yyyy-MM-dd : 第5次最低日期 [10,0] */
	private String lowestRatioDate5;
	/** 第6次最低比例, 没有小数，程序自行除以100 : 第6次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio6;
	/** 第6次最低日期 yyyy-MM-dd : 第6次最低日期 [10,0] */
	private String lowestRatioDate6;
	/** 第7次最低比例, 没有小数，程序自行除以100 : 第7次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio7;
	/** 第7次最低日期 yyyy-MM-dd : 第7次最低日期 [10,0] */
	private String lowestRatioDate7;
	/** 第8次最低比例, 没有小数，程序自行除以100 : 第8次最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio8;
	/** 第8次最低日期 yyyy-MM-dd : 第8次最低日期 [10,0] */
	private String lowestRatioDate8;
	/** 第九次及以上最低比例, 没有小数，程序自行除以100 : 第九次及以上最低比例, 没有小数，程序自行除以100 [18,0] */
	private BigDecimal lowestRatio9;
	/** 第九次及以上最低日期 yyyy-MM-dd : 第九次及以上最低日期 [10,0] */
	private String lowestRatioDate9;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	private String gmtCreate;
	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	private Date timestamp;

	public String getBalanceUnitCode() {
		return balanceUnitCode;
	}

	public void setBalanceUnitCode(String balanceUnitCode) {
		this.balanceUnitCode = balanceUnitCode;
	}

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Integer getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Integer cycleNo) {
		this.cycleNo = cycleNo;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setAnnualInterestRate(BigDecimal annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public BigDecimal getAccumulatedInterest() {
		return accumulatedInterest;
	}

	public void setAccumulatedInterest(BigDecimal accumulatedInterest) {
		this.accumulatedInterest = accumulatedInterest;
	}

	public BigDecimal getCurrentMinRepayment() {
		return currentMinRepayment;
	}

	public void setCurrentMinRepayment(BigDecimal currentMinRepayment) {
		this.currentMinRepayment = currentMinRepayment;
	}

	public String getBalanceObject() {
		return balanceObject;
	}

	public void setBalanceObject(String balanceObject) {
		this.balanceObject = balanceObject;
	}

	public String getNextInterestDate() {
		return nextInterestDate;
	}

	public void setNextInterestDate(String nextInterestDate) {
		this.nextInterestDate = nextInterestDate;
	}

	public String getPrevInterestProcesseDate() {
		return prevInterestProcesseDate;
	}

	public void setPrevInterestProcesseDate(String prevInterestProcesseDate) {
		this.prevInterestProcesseDate = prevInterestProcesseDate;
	}

	public String getAssetProperties() {
		return assetProperties;
	}

	public void setAssetProperties(String assetProperties) {
		this.assetProperties = assetProperties;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getLowestRatio1() {
		return lowestRatio1;
	}

	public void setLowestRatio1(BigDecimal lowestRatio1) {
		this.lowestRatio1 = lowestRatio1;
	}

	public String getLowestRatioDate1() {
		return lowestRatioDate1;
	}

	public void setLowestRatioDate1(String lowestRatioDate1) {
		this.lowestRatioDate1 = lowestRatioDate1;
	}

	public BigDecimal getLowestRatio2() {
		return lowestRatio2;
	}

	public void setLowestRatio2(BigDecimal lowestRatio2) {
		this.lowestRatio2 = lowestRatio2;
	}

	public String getLowestRatioDate2() {
		return lowestRatioDate2;
	}

	public void setLowestRatioDate2(String lowestRatioDate2) {
		this.lowestRatioDate2 = lowestRatioDate2;
	}

	public BigDecimal getLowestRatio3() {
		return lowestRatio3;
	}

	public void setLowestRatio3(BigDecimal lowestRatio3) {
		this.lowestRatio3 = lowestRatio3;
	}

	public String getLowestRatioDate3() {
		return lowestRatioDate3;
	}

	public void setLowestRatioDate3(String lowestRatioDate3) {
		this.lowestRatioDate3 = lowestRatioDate3;
	}

	public BigDecimal getLowestRatio4() {
		return lowestRatio4;
	}

	public void setLowestRatio4(BigDecimal lowestRatio4) {
		this.lowestRatio4 = lowestRatio4;
	}

	public String getLowestRatioDate4() {
		return lowestRatioDate4;
	}

	public void setLowestRatioDate4(String lowestRatioDate4) {
		this.lowestRatioDate4 = lowestRatioDate4;
	}

	public BigDecimal getLowestRatio5() {
		return lowestRatio5;
	}

	public void setLowestRatio5(BigDecimal lowestRatio5) {
		this.lowestRatio5 = lowestRatio5;
	}

	public String getLowestRatioDate5() {
		return lowestRatioDate5;
	}

	public void setLowestRatioDate5(String lowestRatioDate5) {
		this.lowestRatioDate5 = lowestRatioDate5;
	}

	public BigDecimal getLowestRatio6() {
		return lowestRatio6;
	}

	public void setLowestRatio6(BigDecimal lowestRatio6) {
		this.lowestRatio6 = lowestRatio6;
	}

	public String getLowestRatioDate6() {
		return lowestRatioDate6;
	}

	public void setLowestRatioDate6(String lowestRatioDate6) {
		this.lowestRatioDate6 = lowestRatioDate6;
	}

	public BigDecimal getLowestRatio7() {
		return lowestRatio7;
	}

	public void setLowestRatio7(BigDecimal lowestRatio7) {
		this.lowestRatio7 = lowestRatio7;
	}

	public String getLowestRatioDate7() {
		return lowestRatioDate7;
	}

	public void setLowestRatioDate7(String lowestRatioDate7) {
		this.lowestRatioDate7 = lowestRatioDate7;
	}

	public BigDecimal getLowestRatio8() {
		return lowestRatio8;
	}

	public void setLowestRatio8(BigDecimal lowestRatio8) {
		this.lowestRatio8 = lowestRatio8;
	}

	public String getLowestRatioDate8() {
		return lowestRatioDate8;
	}

	public void setLowestRatioDate8(String lowestRatioDate8) {
		this.lowestRatioDate8 = lowestRatioDate8;
	}

	public BigDecimal getLowestRatio9() {
		return lowestRatio9;
	}

	public void setLowestRatio9(BigDecimal lowestRatio9) {
		this.lowestRatio9 = lowestRatio9;
	}

	public String getLowestRatioDate9() {
		return lowestRatioDate9;
	}

	public void setLowestRatioDate9(String lowestRatioDate9) {
		this.lowestRatioDate9 = lowestRatioDate9;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "X5587BO [balanceUnitCode=" + balanceUnitCode + ", balanceType=" + balanceType + ", accountNo="
				+ accountNo + ", currencyCode=" + currencyCode + ", cycleNo=" + cycleNo + ", balance=" + balance
				+ ", annualInterestRate=" + annualInterestRate + ", accumulatedInterest=" + accumulatedInterest
				+ ", currentMinRepayment=" + currentMinRepayment + ", balanceObject=" + balanceObject
				+ ", nextInterestDate=" + nextInterestDate + ", prevInterestProcesseDate=" + prevInterestProcesseDate
				+ ", assetProperties=" + assetProperties + ", createDate=" + createDate + ", endDate=" + endDate
				+ ", lowestRatio1=" + lowestRatio1 + ", lowestRatioDate1=" + lowestRatioDate1 + ", lowestRatio2="
				+ lowestRatio2 + ", lowestRatioDate2=" + lowestRatioDate2 + ", lowestRatio3=" + lowestRatio3
				+ ", lowestRatioDate3=" + lowestRatioDate3 + ", lowestRatio4=" + lowestRatio4 + ", lowestRatioDate4="
				+ lowestRatioDate4 + ", lowestRatio5=" + lowestRatio5 + ", lowestRatioDate5=" + lowestRatioDate5
				+ ", lowestRatio6=" + lowestRatio6 + ", lowestRatioDate6=" + lowestRatioDate6 + ", lowestRatio7="
				+ lowestRatio7 + ", lowestRatioDate7=" + lowestRatioDate7 + ", lowestRatio8=" + lowestRatio8
				+ ", lowestRatioDate8=" + lowestRatioDate8 + ", lowestRatio9=" + lowestRatio9 + ", lowestRatioDate9="
				+ lowestRatioDate9 + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + "]";
	}

}
