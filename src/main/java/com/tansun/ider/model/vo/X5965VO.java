package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 交易入账
 * @author qianyp
 */
public class X5965VO extends BeanVO implements Serializable{
    
    private static final long serialVersionUID = -973083846745094553L;
   
    /** id [64,0] Not NULL */
    private String id;
    /** 客户号 [12,0] Not NULL */
    private String customerNo;
    /** 交易处理日期 [10,0] */
    private String processingDate;
    /** 交易处理时间 [12,0] */
    private String processingTime;
    /** 实时余额账户代码 [23,0] Not NULL */
    private String accountId;
    /** 实时余额货币 [3,0] Not NULL */
    private String currencyCode;
    /** 事件编号 [14,0] Not NULL */
    private String eventNo;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 交易日期 [10,0] Not NULL */
    private String transDate;
    /** 交易时间 [12,0] Not NULL */
    private String transTime;
    /** 交易货币小数位 [10,0] */
    private Integer transCurrDecimal;
    /** 交易币种 [3,0] Not NULL */
    private String transCurrCode;
    /** 交易金额 [18,0] */
    private BigDecimal transAmount;
    /** 交易描述 [50,0] */
    private String transDesc;
    /** 业务类型代码 [9,0] */
    private String businessTypeCode;
    /** 运营模式 [3,0] Not NULL */
    private String operationMode;
    /** 实时余额货币小数位 [10,0] */
    private Integer currDeciaml;
    /** 实时余额当期本金入账金额 [18,0] */
    private BigDecimal currPrincipalBalance;
    /** 实时余额账单本金入账金额 [18,0] */
    private BigDecimal billPrincipalBalance;
    /** 实时余额当期利息入账金额 [18,0] */
    private BigDecimal currInterestBalance;
    /** 实时余额账单利息入账金额  [18,0] */
    private BigDecimal billInterestBalance;
    /** 实时余额当期费用入账金额 [18,0] */
    private BigDecimal currCostBalance;
    /** 实时余额账单费用入账金额 [18,0] */
    private BigDecimal billCostBalance;
    /** 入账币种小数位 [10,0] */
    private Integer postingCurrDecimal;
    /** 入账币种 [3,0] */
    private String postingCurrCode;
    /** 入账金额 [18,0] */
    private BigDecimal postingAmt;
    /** 入账转换汇率 [14,9] */
    private BigDecimal postingExchangeRate;
    /** 清算币种小数位 [10,0] */
    private Integer clearCurrDecimal;
    /** 清算币种 [3,0] */
    private String clearCurrCode;
    /** 清算金额 [18,0] */
    private BigDecimal clearAmount;
    /** 授权码 [6,0] */
    private String authCode;
    /** MCC 商户类别码,由收单机构为签约商户设置  [4,0] */
    private String mcc;
    /** 交易国家代码 [5,0] */
    private String transCountryCde;
    /** 交易城市代码 [11,0] */
    private String transCityCde;
    /** 来源码 : 来源码 [4,0] */
    private String sourceCde;
    /** 收单参考号 [23,0] */
    private String acquireReferenceNumbr;
    /** 渠道代码 [8,0] */
    private String channelCde;
    /** 商户代码 [15,0] */
    private String merchantCde;
    /** 全局交易流水号 [32,0] Not NULL */
    private String globalTransSerialNo;
    /** 原交易全局流水号 [32,0] */
    private String oriGlobalTransSerialNo;
    /** 交易入账状态 N-未入账 F-入账失败 R-重发入账 Y-入账成功 [1,0] */
    private String transBillingState;
    /** 入账处理日期 [10,0] */
    private String postingDate;
    /** 入账处理时间 [12,0] */
    private String postingTime;
    /** 重发入账次数 [10,0] */
    private Integer postingRetryNum;
    /** 重发入账间隔 [10,0] */
    private Integer postingRetryInterval;
    /** 清算分配金额 [18,0] */
    private BigDecimal settleDistriAmount;
    /** 清算分配币种 [3,0] */
    private String settleDistriCurrency;
    /** 清算分配币种小数位 [10,0] */
    private Integer settleDistriCurrPoint;
    /** 溢缴款冻结金额 [18,0] */
    private BigDecimal overpayFrzAmount;
    /** 溢缴款冻结币种 [3,0] */
    private String overpayFrzCurrCode;
    /** 溢缴款冻结小数位 [10,0] */
    private Integer overpayFrzCurrPoint;
    /** 溢缴款金额 [18,0] */
    private BigDecimal overpayAmount;
    /** 溢缴款币种 [3,0] */
    private String overpayCurrCode;
    /** 产品对象 [9,0] */
    private String productObjectCode;
    /** 分期期数 [10,0] */
    private Integer installmentNum;
    /** 业务项目代码 [9,0] */
    private String businessProgramNo;
    /** 交易处理标识 还款的总交易信息赋值为Y，其他默认空 [1,0] */
    private String transProcessFlag;
    /** 原交易入账账户 [23,0] */
    private String oriTransPostingAccountId;
    /** 原交易入账币种 [3,0] */
    private String oriTransPostingCurrCode;
    /** 前序来源 [2,0] */
    private String preorderSource;
    /** 产品形式代码 [18,0] */
    private String productForm;
    /** 原交易日期 [10,0] */
    private String oriTransDate;
    /** 借贷记标识  C:贷记 D：借记 [1,0] */
    private String debitCreditFlag;
    /** 交易识别代码 [4,0] */
    private String transIdentifiNo;
    /** 余额类型，P-本金 I-利息 F-费用 [1,0] */
    private String balanceType;
    /** installment_period : 分期期次 [10,0] */
    private Integer installmentPeriod;
    /** 信贷参数 [4000,0] */
    private String loanPara;
    /** 罚息利率 [18,6] */
    private BigDecimal penaltyInterestRate;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    private Date timeStamp;
    /** 版本号 [10,0] */
    private Integer version;
    /** 交易来源，发卡系统使用 [4,0] */
    private String transSource;
    /** 余额分配前的入账金额 [18,0] */
    private BigDecimal oriPostingAmt;
    /** 费用收取方式，0-一次性收取，1-分期收取 [1,0] */
    private String feeCollectType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getProcessingDate() {
        return processingDate;
    }
    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }
    public String getProcessingTime() {
        return processingTime;
    }
    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getEventNo() {
        return eventNo;
    }
    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }
    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }
    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }
    public String getTransDate() {
        return transDate;
    }
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }
    public String getTransTime() {
        return transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
    public Integer getTransCurrDecimal() {
        return transCurrDecimal;
    }
    public void setTransCurrDecimal(Integer transCurrDecimal) {
        this.transCurrDecimal = transCurrDecimal;
    }
    public String getTransCurrCode() {
        return transCurrCode;
    }
    public void setTransCurrCode(String transCurrCode) {
        this.transCurrCode = transCurrCode;
    }
    public BigDecimal getTransAmount() {
        return transAmount;
    }
    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }
    public String getTransDesc() {
        return transDesc;
    }
    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }
    public String getBusinessTypeCode() {
        return businessTypeCode;
    }
    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }
    public String getOperationMode() {
        return operationMode;
    }
    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }
    public Integer getCurrDeciaml() {
        return currDeciaml;
    }
    public void setCurrDeciaml(Integer currDeciaml) {
        this.currDeciaml = currDeciaml;
    }
    public BigDecimal getCurrPrincipalBalance() {
        return currPrincipalBalance;
    }
    public void setCurrPrincipalBalance(BigDecimal currPrincipalBalance) {
        this.currPrincipalBalance = currPrincipalBalance;
    }
    public BigDecimal getBillPrincipalBalance() {
        return billPrincipalBalance;
    }
    public void setBillPrincipalBalance(BigDecimal billPrincipalBalance) {
        this.billPrincipalBalance = billPrincipalBalance;
    }
    public BigDecimal getCurrInterestBalance() {
        return currInterestBalance;
    }
    public void setCurrInterestBalance(BigDecimal currInterestBalance) {
        this.currInterestBalance = currInterestBalance;
    }
    public BigDecimal getBillInterestBalance() {
        return billInterestBalance;
    }
    public void setBillInterestBalance(BigDecimal billInterestBalance) {
        this.billInterestBalance = billInterestBalance;
    }
    public BigDecimal getCurrCostBalance() {
        return currCostBalance;
    }
    public void setCurrCostBalance(BigDecimal currCostBalance) {
        this.currCostBalance = currCostBalance;
    }
    public BigDecimal getBillCostBalance() {
        return billCostBalance;
    }
    public void setBillCostBalance(BigDecimal billCostBalance) {
        this.billCostBalance = billCostBalance;
    }
    public Integer getPostingCurrDecimal() {
        return postingCurrDecimal;
    }
    public void setPostingCurrDecimal(Integer postingCurrDecimal) {
        this.postingCurrDecimal = postingCurrDecimal;
    }
    public String getPostingCurrCode() {
        return postingCurrCode;
    }
    public void setPostingCurrCode(String postingCurrCode) {
        this.postingCurrCode = postingCurrCode;
    }
    public BigDecimal getPostingAmt() {
        return postingAmt;
    }
    public void setPostingAmt(BigDecimal postingAmt) {
        this.postingAmt = postingAmt;
    }
    public BigDecimal getPostingExchangeRate() {
        return postingExchangeRate;
    }
    public void setPostingExchangeRate(BigDecimal postingExchangeRate) {
        this.postingExchangeRate = postingExchangeRate;
    }
    public Integer getClearCurrDecimal() {
        return clearCurrDecimal;
    }
    public void setClearCurrDecimal(Integer clearCurrDecimal) {
        this.clearCurrDecimal = clearCurrDecimal;
    }
    public String getClearCurrCode() {
        return clearCurrCode;
    }
    public void setClearCurrCode(String clearCurrCode) {
        this.clearCurrCode = clearCurrCode;
    }
    public BigDecimal getClearAmount() {
        return clearAmount;
    }
    public void setClearAmount(BigDecimal clearAmount) {
        this.clearAmount = clearAmount;
    }
    public String getAuthCode() {
        return authCode;
    }
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
    public String getMcc() {
        return mcc;
    }
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }
    public String getTransCountryCde() {
        return transCountryCde;
    }
    public void setTransCountryCde(String transCountryCde) {
        this.transCountryCde = transCountryCde;
    }
    public String getTransCityCde() {
        return transCityCde;
    }
    public void setTransCityCde(String transCityCde) {
        this.transCityCde = transCityCde;
    }
    public String getSourceCde() {
        return sourceCde;
    }
    public void setSourceCde(String sourceCde) {
        this.sourceCde = sourceCde;
    }
    public String getAcquireReferenceNumbr() {
        return acquireReferenceNumbr;
    }
    public void setAcquireReferenceNumbr(String acquireReferenceNumbr) {
        this.acquireReferenceNumbr = acquireReferenceNumbr;
    }
    public String getChannelCde() {
        return channelCde;
    }
    public void setChannelCde(String channelCde) {
        this.channelCde = channelCde;
    }
    public String getMerchantCde() {
        return merchantCde;
    }
    public void setMerchantCde(String merchantCde) {
        this.merchantCde = merchantCde;
    }
    public String getGlobalTransSerialNo() {
        return globalTransSerialNo;
    }
    public void setGlobalTransSerialNo(String globalTransSerialNo) {
        this.globalTransSerialNo = globalTransSerialNo;
    }
    public String getOriGlobalTransSerialNo() {
        return oriGlobalTransSerialNo;
    }
    public void setOriGlobalTransSerialNo(String oriGlobalTransSerialNo) {
        this.oriGlobalTransSerialNo = oriGlobalTransSerialNo;
    }
    public String getTransBillingState() {
        return transBillingState;
    }
    public void setTransBillingState(String transBillingState) {
        this.transBillingState = transBillingState;
    }
    public String getPostingDate() {
        return postingDate;
    }
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }
    public String getPostingTime() {
        return postingTime;
    }
    public void setPostingTime(String postingTime) {
        this.postingTime = postingTime;
    }
    public Integer getPostingRetryNum() {
        return postingRetryNum;
    }
    public void setPostingRetryNum(Integer postingRetryNum) {
        this.postingRetryNum = postingRetryNum;
    }
    public Integer getPostingRetryInterval() {
        return postingRetryInterval;
    }
    public void setPostingRetryInterval(Integer postingRetryInterval) {
        this.postingRetryInterval = postingRetryInterval;
    }
    public BigDecimal getSettleDistriAmount() {
        return settleDistriAmount;
    }
    public void setSettleDistriAmount(BigDecimal settleDistriAmount) {
        this.settleDistriAmount = settleDistriAmount;
    }
    public String getSettleDistriCurrency() {
        return settleDistriCurrency;
    }
    public void setSettleDistriCurrency(String settleDistriCurrency) {
        this.settleDistriCurrency = settleDistriCurrency;
    }
    public Integer getSettleDistriCurrPoint() {
        return settleDistriCurrPoint;
    }
    public void setSettleDistriCurrPoint(Integer settleDistriCurrPoint) {
        this.settleDistriCurrPoint = settleDistriCurrPoint;
    }
    public BigDecimal getOverpayFrzAmount() {
        return overpayFrzAmount;
    }
    public void setOverpayFrzAmount(BigDecimal overpayFrzAmount) {
        this.overpayFrzAmount = overpayFrzAmount;
    }
    public String getOverpayFrzCurrCode() {
        return overpayFrzCurrCode;
    }
    public void setOverpayFrzCurrCode(String overpayFrzCurrCode) {
        this.overpayFrzCurrCode = overpayFrzCurrCode;
    }
    public Integer getOverpayFrzCurrPoint() {
        return overpayFrzCurrPoint;
    }
    public void setOverpayFrzCurrPoint(Integer overpayFrzCurrPoint) {
        this.overpayFrzCurrPoint = overpayFrzCurrPoint;
    }
    public BigDecimal getOverpayAmount() {
        return overpayAmount;
    }
    public void setOverpayAmount(BigDecimal overpayAmount) {
        this.overpayAmount = overpayAmount;
    }
    public String getOverpayCurrCode() {
        return overpayCurrCode;
    }
    public void setOverpayCurrCode(String overpayCurrCode) {
        this.overpayCurrCode = overpayCurrCode;
    }
    public String getProductObjectCode() {
        return productObjectCode;
    }
    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }
    public Integer getInstallmentNum() {
        return installmentNum;
    }
    public void setInstallmentNum(Integer installmentNum) {
        this.installmentNum = installmentNum;
    }
    public String getBusinessProgramNo() {
        return businessProgramNo;
    }
    public void setBusinessProgramNo(String businessProgramNo) {
        this.businessProgramNo = businessProgramNo;
    }
    public String getTransProcessFlag() {
        return transProcessFlag;
    }
    public void setTransProcessFlag(String transProcessFlag) {
        this.transProcessFlag = transProcessFlag;
    }
    public String getOriTransPostingAccountId() {
        return oriTransPostingAccountId;
    }
    public void setOriTransPostingAccountId(String oriTransPostingAccountId) {
        this.oriTransPostingAccountId = oriTransPostingAccountId;
    }
    public String getOriTransPostingCurrCode() {
        return oriTransPostingCurrCode;
    }
    public void setOriTransPostingCurrCode(String oriTransPostingCurrCode) {
        this.oriTransPostingCurrCode = oriTransPostingCurrCode;
    }
    public String getPreorderSource() {
        return preorderSource;
    }
    public void setPreorderSource(String preorderSource) {
        this.preorderSource = preorderSource;
    }
    public String getProductForm() {
        return productForm;
    }
    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }
    public String getOriTransDate() {
        return oriTransDate;
    }
    public void setOriTransDate(String oriTransDate) {
        this.oriTransDate = oriTransDate;
    }
    public String getDebitCreditFlag() {
        return debitCreditFlag;
    }
    public void setDebitCreditFlag(String debitCreditFlag) {
        this.debitCreditFlag = debitCreditFlag;
    }
    public String getTransIdentifiNo() {
        return transIdentifiNo;
    }
    public void setTransIdentifiNo(String transIdentifiNo) {
        this.transIdentifiNo = transIdentifiNo;
    }
    public String getBalanceType() {
        return balanceType;
    }
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
    public Integer getInstallmentPeriod() {
        return installmentPeriod;
    }
    public void setInstallmentPeriod(Integer installmentPeriod) {
        this.installmentPeriod = installmentPeriod;
    }
    public String getLoanPara() {
        return loanPara;
    }
    public void setLoanPara(String loanPara) {
        this.loanPara = loanPara;
    }
    public BigDecimal getPenaltyInterestRate() {
        return penaltyInterestRate;
    }
    public void setPenaltyInterestRate(BigDecimal penaltyInterestRate) {
        this.penaltyInterestRate = penaltyInterestRate;
    }
    public String getGmtCreate() {
        return gmtCreate;
    }
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
    public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    public String getTransSource() {
        return transSource;
    }
    public void setTransSource(String transSource) {
        this.transSource = transSource;
    }
    public BigDecimal getOriPostingAmt() {
        return oriPostingAmt;
    }
    public void setOriPostingAmt(BigDecimal oriPostingAmt) {
        this.oriPostingAmt = oriPostingAmt;
    }
    public String getFeeCollectType() {
        return feeCollectType;
    }
    public void setFeeCollectType(String feeCollectType) {
        this.feeCollectType = feeCollectType;
    }
	@Override
	public String toString() {
		return "X5965VO [id=" + id + ", customerNo=" + customerNo + ", processingDate=" + processingDate
				+ ", processingTime=" + processingTime + ", accountId=" + accountId + ", currencyCode=" + currencyCode
				+ ", eventNo=" + eventNo + ", externalIdentificationNo=" + externalIdentificationNo + ", transDate="
				+ transDate + ", transTime=" + transTime + ", transCurrDecimal=" + transCurrDecimal + ", transCurrCode="
				+ transCurrCode + ", transAmount=" + transAmount + ", transDesc=" + transDesc + ", businessTypeCode="
				+ businessTypeCode + ", operationMode=" + operationMode + ", currDeciaml=" + currDeciaml
				+ ", currPrincipalBalance=" + currPrincipalBalance + ", billPrincipalBalance=" + billPrincipalBalance
				+ ", currInterestBalance=" + currInterestBalance + ", billInterestBalance=" + billInterestBalance
				+ ", currCostBalance=" + currCostBalance + ", billCostBalance=" + billCostBalance
				+ ", postingCurrDecimal=" + postingCurrDecimal + ", postingCurrCode=" + postingCurrCode
				+ ", postingAmt=" + postingAmt + ", postingExchangeRate=" + postingExchangeRate + ", clearCurrDecimal="
				+ clearCurrDecimal + ", clearCurrCode=" + clearCurrCode + ", clearAmount=" + clearAmount + ", authCode="
				+ authCode + ", mcc=" + mcc + ", transCountryCde=" + transCountryCde + ", transCityCde=" + transCityCde
				+ ", sourceCde=" + sourceCde + ", acquireReferenceNumbr=" + acquireReferenceNumbr + ", channelCde="
				+ channelCde + ", merchantCde=" + merchantCde + ", globalTransSerialNo=" + globalTransSerialNo
				+ ", oriGlobalTransSerialNo=" + oriGlobalTransSerialNo + ", transBillingState=" + transBillingState
				+ ", postingDate=" + postingDate + ", postingTime=" + postingTime + ", postingRetryNum="
				+ postingRetryNum + ", postingRetryInterval=" + postingRetryInterval + ", settleDistriAmount="
				+ settleDistriAmount + ", settleDistriCurrency=" + settleDistriCurrency + ", settleDistriCurrPoint="
				+ settleDistriCurrPoint + ", overpayFrzAmount=" + overpayFrzAmount + ", overpayFrzCurrCode="
				+ overpayFrzCurrCode + ", overpayFrzCurrPoint=" + overpayFrzCurrPoint + ", overpayAmount="
				+ overpayAmount + ", overpayCurrCode=" + overpayCurrCode + ", productObjectCode=" + productObjectCode
				+ ", installmentNum=" + installmentNum + ", businessProgramNo=" + businessProgramNo
				+ ", transProcessFlag=" + transProcessFlag + ", oriTransPostingAccountId=" + oriTransPostingAccountId
				+ ", oriTransPostingCurrCode=" + oriTransPostingCurrCode + ", preorderSource=" + preorderSource
				+ ", productForm=" + productForm + ", oriTransDate=" + oriTransDate + ", debitCreditFlag="
				+ debitCreditFlag + ", transIdentifiNo=" + transIdentifiNo + ", balanceType=" + balanceType
				+ ", installmentPeriod=" + installmentPeriod + ", loanPara=" + loanPara + ", penaltyInterestRate="
				+ penaltyInterestRate + ", gmtCreate=" + gmtCreate + ", timeStamp=" + timeStamp + ", version=" + version
				+ ", transSource=" + transSource + ", oriPostingAmt=" + oriPostingAmt + ", feeCollectType="
				+ feeCollectType + "]";
	}
}
