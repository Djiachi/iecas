package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 
 * @author xuhaopeng
 *
 */

public class X7410BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -161123581321345589L;
	
	private String id;
	/** 运营模式 */
	private String operationMode;
	/** 外部识别号*/
    private String externalIdentificationNo;
    /** 证件类型*/
    private String idType;
    /** 证件号码*/
    private String idNumber;
	/** 客户号 [12,0] Not NULL */
    private String customerNo;
    /** 额度树 [9,0] Not NULL */
    private String creditTreeId;
    /** 额度节点 [3,0] Not NULL */
    private String creditNodeNo;
    /** 节点类型 */
    private String creditNodeTyp;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 产品对象 [9,0] Not NULL */
    private String productObjectCode;
    /** 产品描述*/
    private String productDesc;
    /** 当前生效额度 [18,0] */
    private BigDecimal currEffectvLimit;
    /** 容差金额*/
    private BigDecimal toleranceAmt;
    /**可用额度*/
    private BigDecimal usableAmt;
    /** 永久额度 [18,0] */
    private BigDecimal permLimit;
    /** 临时额度 [18,0] */
    private BigDecimal tempLimit;
    /** 临时额度生效日期 [10,0] */
    private String tempLimitEffectvDate;
    /** 临时额度失效日期 [10,0] */
    private String tempLimitExpireDate;
    /** 未对消授权金额(固额) */
    private BigDecimal outstandingAmtFixed;
    /** 未对消授权金额(临额) */
    private BigDecimal outstandingAmtTemp;
    /** 未对消授权金额(容差) */
    private BigDecimal outstandingAmtTolerance;
    /** 已入账金额(固额)*/
    private BigDecimal balanceFixed;
    /** 已入账金额(临额)*/
    private BigDecimal balanceTemp;
    /** 已入账金额(容差)*/
    private BigDecimal balanceTolerance; 
    /** 未对消授权金额 */
    private BigDecimal outstandingAmt;
    /** 还款未入账金额*/
    private BigDecimal paymentOutstandingAmt;
    /** 已入账金额*/
    private BigDecimal balance;
    /**节点描述*/
    private String creditDesc;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    private Date timestamp;
    /** 版本号 [10,0] Not NULL */
    private Integer version;
    /** 调额标记  Y：调额 */
    private String adjustFlag;
    /** 全局事件编号*/ 
	private String globalEventNo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCreditTreeId() {
		return creditTreeId;
	}
	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}
	public String getCreditNodeNo() {
		return creditNodeNo;
	}
	public void setCreditNodeNo(String creditNodeNo) {
		this.creditNodeNo = creditNodeNo;
	}
	public String getCreditNodeTyp() {
		return creditNodeTyp;
	}
	public void setCreditNodeTyp(String creditNodeTyp) {
		this.creditNodeTyp = creditNodeTyp;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public BigDecimal getCurrEffectvLimit() {
		return currEffectvLimit;
	}
	public void setCurrEffectvLimit(BigDecimal currEffectvLimit) {
		this.currEffectvLimit = currEffectvLimit;
	}
	public BigDecimal getToleranceAmt() {
		return toleranceAmt;
	}
	public void setToleranceAmt(BigDecimal toleranceAmt) {
		this.toleranceAmt = toleranceAmt;
	}
	public BigDecimal getUsableAmt() {
		return usableAmt;
	}
	public void setUsableAmt(BigDecimal usableAmt) {
		this.usableAmt = usableAmt;
	}
	public BigDecimal getPermLimit() {
		return permLimit;
	}
	public void setPermLimit(BigDecimal permLimit) {
		this.permLimit = permLimit;
	}
	public BigDecimal getTempLimit() {
		return tempLimit;
	}
	public void setTempLimit(BigDecimal tempLimit) {
		this.tempLimit = tempLimit;
	}
	public String getTempLimitEffectvDate() {
		return tempLimitEffectvDate;
	}
	public void setTempLimitEffectvDate(String tempLimitEffectvDate) {
		this.tempLimitEffectvDate = tempLimitEffectvDate;
	}
	public String getTempLimitExpireDate() {
		return tempLimitExpireDate;
	}
	public void setTempLimitExpireDate(String tempLimitExpireDate) {
		this.tempLimitExpireDate = tempLimitExpireDate;
	}
	public BigDecimal getOutstandingAmtFixed() {
		return outstandingAmtFixed;
	}
	public void setOutstandingAmtFixed(BigDecimal outstandingAmtFixed) {
		this.outstandingAmtFixed = outstandingAmtFixed;
	}
	public BigDecimal getOutstandingAmtTemp() {
		return outstandingAmtTemp;
	}
	public void setOutstandingAmtTemp(BigDecimal outstandingAmtTemp) {
		this.outstandingAmtTemp = outstandingAmtTemp;
	}
	public BigDecimal getOutstandingAmtTolerance() {
		return outstandingAmtTolerance;
	}
	public void setOutstandingAmtTolerance(BigDecimal outstandingAmtTolerance) {
		this.outstandingAmtTolerance = outstandingAmtTolerance;
	}
	public BigDecimal getBalanceFixed() {
		return balanceFixed;
	}
	public void setBalanceFixed(BigDecimal balanceFixed) {
		this.balanceFixed = balanceFixed;
	}
	public BigDecimal getBalanceTemp() {
		return balanceTemp;
	}
	public void setBalanceTemp(BigDecimal balanceTemp) {
		this.balanceTemp = balanceTemp;
	}
	public BigDecimal getBalanceTolerance() {
		return balanceTolerance;
	}
	public void setBalanceTolerance(BigDecimal balanceTolerance) {
		this.balanceTolerance = balanceTolerance;
	}
	public BigDecimal getOutstandingAmt() {
		return outstandingAmt;
	}
	public void setOutstandingAmt(BigDecimal outstandingAmt) {
		this.outstandingAmt = outstandingAmt;
	}
	public BigDecimal getPaymentOutstandingAmt() {
		return paymentOutstandingAmt;
	}
	public void setPaymentOutstandingAmt(BigDecimal paymentOutstandingAmt) {
		this.paymentOutstandingAmt = paymentOutstandingAmt;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCreditDesc() {
		return creditDesc;
	}
	public void setCreditDesc(String creditDesc) {
		this.creditDesc = creditDesc;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getAdjustFlag() {
		return adjustFlag;
	}
	public void setAdjustFlag(String adjustFlag) {
		this.adjustFlag = adjustFlag;
	}
	public String getGlobalEventNo() {
		return globalEventNo;
	}
	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}
	@Override
	public String toString() {
		return "X7410BO [id=" + id + ", operationMode=" + operationMode + ", externalIdentificationNo="
				+ externalIdentificationNo + ", idType=" + idType + ", idNumber=" + idNumber + ", customerNo="
				+ customerNo + ", creditTreeId=" + creditTreeId + ", creditNodeNo=" + creditNodeNo + ", creditNodeTyp="
				+ creditNodeTyp + ", currencyCode=" + currencyCode + ", productObjectCode=" + productObjectCode
				+ ", productDesc=" + productDesc + ", currEffectvLimit=" + currEffectvLimit + ", toleranceAmt="
				+ toleranceAmt + ", usableAmt=" + usableAmt + ", permLimit=" + permLimit + ", tempLimit=" + tempLimit
				+ ", tempLimitEffectvDate=" + tempLimitEffectvDate + ", tempLimitExpireDate=" + tempLimitExpireDate
				+ ", outstandingAmtFixed=" + outstandingAmtFixed + ", outstandingAmtTemp=" + outstandingAmtTemp
				+ ", outstandingAmtTolerance=" + outstandingAmtTolerance + ", balanceFixed=" + balanceFixed
				+ ", balanceTemp=" + balanceTemp + ", balanceTolerance=" + balanceTolerance + ", outstandingAmt="
				+ outstandingAmt + ", paymentOutstandingAmt=" + paymentOutstandingAmt + ", balance=" + balance
				+ ", creditDesc=" + creditDesc + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version="
				+ version + ", adjustFlag=" + adjustFlag + ", globalEventNo=" + globalEventNo + "]";
	}

}
