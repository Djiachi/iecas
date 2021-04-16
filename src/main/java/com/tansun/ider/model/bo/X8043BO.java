package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 授权历史交易日志
 * @Author kangxuw
 * @Date 2018年5月23日下午3:03:34
 */
public class X8043BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 6424190912422541691L;

	private String id;
	private String transProcessTm;
	private Integer transProcessNumbr;
	private String ecommB002PanNbr;
	private String ecommB007GmtDateTime;
	private String ecommB032AcqId;
	private String ecommB037RetrvlRefNbr;
	private String ecommB011SysAudtTrace;
	private String oriTransProcessTm;
	private Integer oriTransProcessNumbr;
	private String productObjectCode;
	private String logFileFlag;
	private String logTyp;
	private String recordTyp;
	private String recordStatus;
	private String cutoffFlag;
	private String ecommInputSource;
	private Integer ecommBinNbr;
	private String mandatoryAuthFlag;
	private String receivingTransId;
	private String receivingProgId;
	private String tcpipQueueId;
	private String ecommProcessingDate;
	private String ecommProcessingTime;
	private Integer transCurrencyPoint;
	private Integer accountCurrencyPoint;
	private String issueExcptFlag;
	private String fraudFlag;
	private String specContrlFlag;
	private String specContrlReason;
	private String transDowngradeFlag;
	private String transChipFlag;
	private String ecommAuthCde;
	private String ecommAuthResponseCde;
	private Integer declineRsn;
	private String declineInfo;
	private String ecommOperMode;
	private String ecommOperCurrency;
	private String ecommAuthSceneCde;
	private String ecommTxnCde;
	private String ecommContrlSceneCde;
	private String ecommDifferentCde;
	private String ecommListProperties;
	private String ecommListCode;
	private String ecommTransLimitLevel;
	private String ecommTransLimitCde;
	private String ecommChBillCurr;
	private String ecommCustomerNo;
	private String overpayAccountCde;
	private String overpayAccountCurrency;
	private String authRequestTyp;
	private String ecommReverseTyp;
	private String userId;
	private BigDecimal ecommCreditLimit;
	private BigDecimal ecommAvailLimit;
	private String cashFlag;
	private String ecommVerifnRsltCvv;
	private String ecommVerifnRsltIcvv;
	private String ecommVerifnRsltCvv2;
	private String ecommVerifnRsltCavv;
	private String ecommVerifnRsltPin;
	private String ecommVerifnRsltArqc;
	private String ecommVerifnRsltExpDte;
	private String ecommVerifnRsltPltNbrs;
	private String ecommVerifnRsltExcp;
	private String ecommVerifnRsltActv;
	private String ecommVerifnRsltBwlist;
	private String ecommVerifnRsltVelo;
	private String ecommVerifnRsltLimit;
	private String ecommMsgTypeId;
	private String transAccountCde;
	private String ecommB003ProcCode;
	private BigDecimal ecommB004TxnAmt;
	private BigDecimal accountAmount;
	private BigDecimal ecommB010ChargeRate;
	private String ecommB014ExpirDate;
	private String ecommB018MerchType;
	private String ecommB019CntryCode;
	private String ecommB022Field;
	private Integer ecommB023CardSeqNbr;
	private Integer ecommB025PosCondCode;
	private BigDecimal transFee;
	private String ecommB033FwdId;
	private String ecommB038AuthCode;
	private String ecommB041CardAccpt;
	private String ecommB042CardAccptId;
	private String ecommB043ArdAccptDesc;
	private String transExtraInfo;
	private String ecommB049CurrCode;
	private String ecommB051ChCurrCode;
	private String ecommB055ChipData;
	private String gmtCreate;
	private Date timestamp;
	private Integer version;

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public Integer getAccountCurrencyPoint() {
		return accountCurrencyPoint;
	}

	public String getAuthRequestTyp() {
		return authRequestTyp;
	}

	public String getCashFlag() {
		return cashFlag;
	}

	public String getCutoffFlag() {
		return cutoffFlag;
	}

	public String getDeclineInfo() {
		return declineInfo;
	}

	public Integer getDeclineRsn() {
		return declineRsn;
	}

	public String getEcommAuthCde() {
		return ecommAuthCde;
	}

	public String getEcommAuthResponseCde() {
		return ecommAuthResponseCde;
	}

	public String getEcommAuthSceneCde() {
		return ecommAuthSceneCde;
	}

	public BigDecimal getEcommAvailLimit() {
		return ecommAvailLimit;
	}

	public String getEcommB002PanNbr() {
		return ecommB002PanNbr;
	}

	public String getEcommB003ProcCode() {
		return ecommB003ProcCode;
	}

	public BigDecimal getEcommB004TxnAmt() {
		return ecommB004TxnAmt;
	}

	public String getEcommB007GmtDateTime() {
		return ecommB007GmtDateTime;
	}

	public BigDecimal getEcommB010ChargeRate() {
		return ecommB010ChargeRate;
	}

	public String getEcommB011SysAudtTrace() {
		return ecommB011SysAudtTrace;
	}

	public String getEcommB014ExpirDate() {
		return ecommB014ExpirDate;
	}

	public String getEcommB018MerchType() {
		return ecommB018MerchType;
	}

	public String getEcommB019CntryCode() {
		return ecommB019CntryCode;
	}

	public String getEcommB022Field() {
		return ecommB022Field;
	}

	public Integer getEcommB023CardSeqNbr() {
		return ecommB023CardSeqNbr;
	}

	public Integer getEcommB025PosCondCode() {
		return ecommB025PosCondCode;
	}

	public String getEcommB032AcqId() {
		return ecommB032AcqId;
	}

	public String getEcommB033FwdId() {
		return ecommB033FwdId;
	}

	public String getEcommB037RetrvlRefNbr() {
		return ecommB037RetrvlRefNbr;
	}

	public String getEcommB038AuthCode() {
		return ecommB038AuthCode;
	}

	public String getEcommB041CardAccpt() {
		return ecommB041CardAccpt;
	}

	public String getEcommB042CardAccptId() {
		return ecommB042CardAccptId;
	}

	public String getEcommB043ArdAccptDesc() {
		return ecommB043ArdAccptDesc;
	}

	public String getEcommB049CurrCode() {
		return ecommB049CurrCode;
	}

	public String getEcommB051ChCurrCode() {
		return ecommB051ChCurrCode;
	}

	public String getEcommB055ChipData() {
		return ecommB055ChipData;
	}

	public Integer getEcommBinNbr() {
		return ecommBinNbr;
	}

	public String getEcommChBillCurr() {
		return ecommChBillCurr;
	}

	public String getEcommContrlSceneCde() {
		return ecommContrlSceneCde;
	}

	public BigDecimal getEcommCreditLimit() {
		return ecommCreditLimit;
	}

	public String getEcommCustomerNo() {
		return ecommCustomerNo;
	}

	public String getEcommDifferentCde() {
		return ecommDifferentCde;
	}

	public String getEcommInputSource() {
		return ecommInputSource;
	}

	public String getEcommListCode() {
		return ecommListCode;
	}

	public String getEcommListProperties() {
		return ecommListProperties;
	}

	public String getEcommMsgTypeId() {
		return ecommMsgTypeId;
	}

	public String getEcommOperCurrency() {
		return ecommOperCurrency;
	}

	public String getEcommOperMode() {
		return ecommOperMode;
	}

	public String getEcommProcessingDate() {
		return ecommProcessingDate;
	}

	public String getEcommProcessingTime() {
		return ecommProcessingTime;
	}

	public String getEcommReverseTyp() {
		return ecommReverseTyp;
	}

	public String getEcommTransLimitCde() {
		return ecommTransLimitCde;
	}

	public String getEcommTransLimitLevel() {
		return ecommTransLimitLevel;
	}

	public String getEcommTxnCde() {
		return ecommTxnCde;
	}

	public String getEcommVerifnRsltActv() {
		return ecommVerifnRsltActv;
	}

	public String getEcommVerifnRsltArqc() {
		return ecommVerifnRsltArqc;
	}

	public String getEcommVerifnRsltBwlist() {
		return ecommVerifnRsltBwlist;
	}

	public String getEcommVerifnRsltCavv() {
		return ecommVerifnRsltCavv;
	}

	public String getEcommVerifnRsltCvv() {
		return ecommVerifnRsltCvv;
	}

	public String getEcommVerifnRsltCvv2() {
		return ecommVerifnRsltCvv2;
	}

	public String getEcommVerifnRsltExcp() {
		return ecommVerifnRsltExcp;
	}

	public String getEcommVerifnRsltExpDte() {
		return ecommVerifnRsltExpDte;
	}

	public String getEcommVerifnRsltIcvv() {
		return ecommVerifnRsltIcvv;
	}

	public String getEcommVerifnRsltLimit() {
		return ecommVerifnRsltLimit;
	}

	public String getEcommVerifnRsltPin() {
		return ecommVerifnRsltPin;
	}

	public String getEcommVerifnRsltPltNbrs() {
		return ecommVerifnRsltPltNbrs;
	}

	public String getEcommVerifnRsltVelo() {
		return ecommVerifnRsltVelo;
	}

	public String getFraudFlag() {
		return fraudFlag;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public String getId() {
		return id;
	}

	public String getIssueExcptFlag() {
		return issueExcptFlag;
	}

	public String getLogFileFlag() {
		return logFileFlag;
	}

	public String getLogTyp() {
		return logTyp;
	}

	public String getMandatoryAuthFlag() {
		return mandatoryAuthFlag;
	}

	public Integer getOriTransProcessNumbr() {
		return oriTransProcessNumbr;
	}

	public String getOriTransProcessTm() {
		return oriTransProcessTm;
	}

	public String getOverpayAccountCde() {
		return overpayAccountCde;
	}

	public String getOverpayAccountCurrency() {
		return overpayAccountCurrency;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public String getReceivingProgId() {
		return receivingProgId;
	}

	public String getReceivingTransId() {
		return receivingTransId;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public String getRecordTyp() {
		return recordTyp;
	}

	public String getSpecContrlFlag() {
		return specContrlFlag;
	}

	public String getSpecContrlReason() {
		return specContrlReason;
	}

	public String getTcpipQueueId() {
		return tcpipQueueId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTransAccountCde() {
		return transAccountCde;
	}

	public String getTransChipFlag() {
		return transChipFlag;
	}

	public Integer getTransCurrencyPoint() {
		return transCurrencyPoint;
	}

	public String getTransDowngradeFlag() {
		return transDowngradeFlag;
	}

	public String getTransExtraInfo() {
		return transExtraInfo;
	}

	public BigDecimal getTransFee() {
		return transFee;
	}

	public Integer getTransProcessNumbr() {
		return transProcessNumbr;
	}

	public String getTransProcessTm() {
		return transProcessTm;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

	public void setAccountCurrencyPoint(Integer accountCurrencyPoint) {
		this.accountCurrencyPoint = accountCurrencyPoint;
	}

	public void setAuthRequestTyp(String authRequestTyp) {
		this.authRequestTyp = authRequestTyp;
	}

	public void setCashFlag(String cashFlag) {
		this.cashFlag = cashFlag;
	}

	public void setCutoffFlag(String cutoffFlag) {
		this.cutoffFlag = cutoffFlag;
	}

	public void setDeclineInfo(String declineInfo) {
		this.declineInfo = declineInfo;
	}

	public void setDeclineRsn(Integer declineRsn) {
		this.declineRsn = declineRsn;
	}

	public void setEcommAuthCde(String ecommAuthCde) {
		this.ecommAuthCde = ecommAuthCde;
	}

	public void setEcommAuthResponseCde(String ecommAuthResponseCde) {
		this.ecommAuthResponseCde = ecommAuthResponseCde;
	}

	public void setEcommAuthSceneCde(String ecommAuthSceneCde) {
		this.ecommAuthSceneCde = ecommAuthSceneCde;
	}

	public void setEcommAvailLimit(BigDecimal ecommAvailLimit) {
		this.ecommAvailLimit = ecommAvailLimit;
	}

	public void setEcommB002PanNbr(String ecommB002PanNbr) {
		this.ecommB002PanNbr = ecommB002PanNbr;
	}

	public void setEcommB003ProcCode(String ecommB003ProcCode) {
		this.ecommB003ProcCode = ecommB003ProcCode;
	}

	public void setEcommB004TxnAmt(BigDecimal ecommB004TxnAmt) {
		this.ecommB004TxnAmt = ecommB004TxnAmt;
	}

	public void setEcommB007GmtDateTime(String ecommB007GmtDateTime) {
		this.ecommB007GmtDateTime = ecommB007GmtDateTime;
	}

	public void setEcommB010ChargeRate(BigDecimal ecommB010ChargeRate) {
		this.ecommB010ChargeRate = ecommB010ChargeRate;
	}

	public void setEcommB011SysAudtTrace(String ecommB011SysAudtTrace) {
		this.ecommB011SysAudtTrace = ecommB011SysAudtTrace;
	}

	public void setEcommB014ExpirDate(String ecommB014ExpirDate) {
		this.ecommB014ExpirDate = ecommB014ExpirDate;
	}

	public void setEcommB018MerchType(String ecommB018MerchType) {
		this.ecommB018MerchType = ecommB018MerchType;
	}

	public void setEcommB019CntryCode(String ecommB019CntryCode) {
		this.ecommB019CntryCode = ecommB019CntryCode;
	}

	public void setEcommB022Field(String ecommB022Field) {
		this.ecommB022Field = ecommB022Field;
	}

	public void setEcommB023CardSeqNbr(Integer ecommB023CardSeqNbr) {
		this.ecommB023CardSeqNbr = ecommB023CardSeqNbr;
	}

	public void setEcommB025PosCondCode(Integer ecommB025PosCondCode) {
		this.ecommB025PosCondCode = ecommB025PosCondCode;
	}

	public void setEcommB032AcqId(String ecommB032AcqId) {
		this.ecommB032AcqId = ecommB032AcqId;
	}

	public void setEcommB033FwdId(String ecommB033FwdId) {
		this.ecommB033FwdId = ecommB033FwdId;
	}

	public void setEcommB037RetrvlRefNbr(String ecommB037RetrvlRefNbr) {
		this.ecommB037RetrvlRefNbr = ecommB037RetrvlRefNbr;
	}

	public void setEcommB038AuthCode(String ecommB038AuthCode) {
		this.ecommB038AuthCode = ecommB038AuthCode;
	}

	public void setEcommB041CardAccpt(String ecommB041CardAccpt) {
		this.ecommB041CardAccpt = ecommB041CardAccpt;
	}

	public void setEcommB042CardAccptId(String ecommB042CardAccptId) {
		this.ecommB042CardAccptId = ecommB042CardAccptId;
	}

	public void setEcommB043ArdAccptDesc(String ecommB043ArdAccptDesc) {
		this.ecommB043ArdAccptDesc = ecommB043ArdAccptDesc;
	}

	public void setEcommB049CurrCode(String ecommB049CurrCode) {
		this.ecommB049CurrCode = ecommB049CurrCode;
	}

	public void setEcommB051ChCurrCode(String ecommB051ChCurrCode) {
		this.ecommB051ChCurrCode = ecommB051ChCurrCode;
	}

	public void setEcommB055ChipData(String ecommB055ChipData) {
		this.ecommB055ChipData = ecommB055ChipData;
	}

	public void setEcommBinNbr(Integer ecommBinNbr) {
		this.ecommBinNbr = ecommBinNbr;
	}

	public void setEcommChBillCurr(String ecommChBillCurr) {
		this.ecommChBillCurr = ecommChBillCurr;
	}

	public void setEcommContrlSceneCde(String ecommContrlSceneCde) {
		this.ecommContrlSceneCde = ecommContrlSceneCde;
	}

	public void setEcommCreditLimit(BigDecimal ecommCreditLimit) {
		this.ecommCreditLimit = ecommCreditLimit;
	}

	public void setEcommCustomerNo(String ecommCustomerNo) {
		this.ecommCustomerNo = ecommCustomerNo;
	}

	public void setEcommDifferentCde(String ecommDifferentCde) {
		this.ecommDifferentCde = ecommDifferentCde;
	}

	public void setEcommInputSource(String ecommInputSource) {
		this.ecommInputSource = ecommInputSource;
	}

	public void setEcommListCode(String ecommListCode) {
		this.ecommListCode = ecommListCode;
	}

	public void setEcommListProperties(String ecommListProperties) {
		this.ecommListProperties = ecommListProperties;
	}

	public void setEcommMsgTypeId(String ecommMsgTypeId) {
		this.ecommMsgTypeId = ecommMsgTypeId;
	}

	public void setEcommOperCurrency(String ecommOperCurrency) {
		this.ecommOperCurrency = ecommOperCurrency;
	}

	public void setEcommOperMode(String ecommOperMode) {
		this.ecommOperMode = ecommOperMode;
	}

	public void setEcommProcessingDate(String ecommProcessingDate) {
		this.ecommProcessingDate = ecommProcessingDate;
	}

	public void setEcommProcessingTime(String ecommProcessingTime) {
		this.ecommProcessingTime = ecommProcessingTime;
	}

	public void setEcommReverseTyp(String ecommReverseTyp) {
		this.ecommReverseTyp = ecommReverseTyp;
	}

	public void setEcommTransLimitCde(String ecommTransLimitCde) {
		this.ecommTransLimitCde = ecommTransLimitCde;
	}

	public void setEcommTransLimitLevel(String ecommTransLimitLevel) {
		this.ecommTransLimitLevel = ecommTransLimitLevel;
	}

	public void setEcommTxnCde(String ecommTxnCde) {
		this.ecommTxnCde = ecommTxnCde;
	}

	public void setEcommVerifnRsltActv(String ecommVerifnRsltActv) {
		this.ecommVerifnRsltActv = ecommVerifnRsltActv;
	}

	public void setEcommVerifnRsltArqc(String ecommVerifnRsltArqc) {
		this.ecommVerifnRsltArqc = ecommVerifnRsltArqc;
	}

	public void setEcommVerifnRsltBwlist(String ecommVerifnRsltBwlist) {
		this.ecommVerifnRsltBwlist = ecommVerifnRsltBwlist;
	}

	public void setEcommVerifnRsltCavv(String ecommVerifnRsltCavv) {
		this.ecommVerifnRsltCavv = ecommVerifnRsltCavv;
	}

	public void setEcommVerifnRsltCvv(String ecommVerifnRsltCvv) {
		this.ecommVerifnRsltCvv = ecommVerifnRsltCvv;
	}

	public void setEcommVerifnRsltCvv2(String ecommVerifnRsltCvv2) {
		this.ecommVerifnRsltCvv2 = ecommVerifnRsltCvv2;
	}

	public void setEcommVerifnRsltExcp(String ecommVerifnRsltExcp) {
		this.ecommVerifnRsltExcp = ecommVerifnRsltExcp;
	}

	public void setEcommVerifnRsltExpDte(String ecommVerifnRsltExpDte) {
		this.ecommVerifnRsltExpDte = ecommVerifnRsltExpDte;
	}

	public void setEcommVerifnRsltIcvv(String ecommVerifnRsltIcvv) {
		this.ecommVerifnRsltIcvv = ecommVerifnRsltIcvv;
	}

	public void setEcommVerifnRsltLimit(String ecommVerifnRsltLimit) {
		this.ecommVerifnRsltLimit = ecommVerifnRsltLimit;
	}

	public void setEcommVerifnRsltPin(String ecommVerifnRsltPin) {
		this.ecommVerifnRsltPin = ecommVerifnRsltPin;
	}

	public void setEcommVerifnRsltPltNbrs(String ecommVerifnRsltPltNbrs) {
		this.ecommVerifnRsltPltNbrs = ecommVerifnRsltPltNbrs;
	}

	public void setEcommVerifnRsltVelo(String ecommVerifnRsltVelo) {
		this.ecommVerifnRsltVelo = ecommVerifnRsltVelo;
	}

	public void setFraudFlag(String fraudFlag) {
		this.fraudFlag = fraudFlag;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIssueExcptFlag(String issueExcptFlag) {
		this.issueExcptFlag = issueExcptFlag;
	}

	public void setLogFileFlag(String logFileFlag) {
		this.logFileFlag = logFileFlag;
	}

	public void setLogTyp(String logTyp) {
		this.logTyp = logTyp;
	}

	public void setMandatoryAuthFlag(String mandatoryAuthFlag) {
		this.mandatoryAuthFlag = mandatoryAuthFlag;
	}

	public void setOriTransProcessNumbr(Integer oriTransProcessNumbr) {
		this.oriTransProcessNumbr = oriTransProcessNumbr;
	}

	public void setOriTransProcessTm(String oriTransProcessTm) {
		this.oriTransProcessTm = oriTransProcessTm;
	}

	public void setOverpayAccountCde(String overpayAccountCde) {
		this.overpayAccountCde = overpayAccountCde;
	}

	public void setOverpayAccountCurrency(String overpayAccountCurrency) {
		this.overpayAccountCurrency = overpayAccountCurrency;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public void setReceivingProgId(String receivingProgId) {
		this.receivingProgId = receivingProgId;
	}

	public void setReceivingTransId(String receivingTransId) {
		this.receivingTransId = receivingTransId;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public void setRecordTyp(String recordTyp) {
		this.recordTyp = recordTyp;
	}

	public void setSpecContrlFlag(String specContrlFlag) {
		this.specContrlFlag = specContrlFlag;
	}

	public void setSpecContrlReason(String specContrlReason) {
		this.specContrlReason = specContrlReason;
	}

	public void setTcpipQueueId(String tcpipQueueId) {
		this.tcpipQueueId = tcpipQueueId;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTransAccountCde(String transAccountCde) {
		this.transAccountCde = transAccountCde;
	}

	public void setTransChipFlag(String transChipFlag) {
		this.transChipFlag = transChipFlag;
	}

	public void setTransCurrencyPoint(Integer transCurrencyPoint) {
		this.transCurrencyPoint = transCurrencyPoint;
	}

	public void setTransDowngradeFlag(String transDowngradeFlag) {
		this.transDowngradeFlag = transDowngradeFlag;
	}

	public void setTransExtraInfo(String transExtraInfo) {
		this.transExtraInfo = transExtraInfo;
	}

	public void setTransFee(BigDecimal transFee) {
		this.transFee = transFee;
	}

	public void setTransProcessNumbr(Integer transProcessNumbr) {
		this.transProcessNumbr = transProcessNumbr;
	}

	public void setTransProcessTm(String transProcessTm) {
		this.transProcessTm = transProcessTm;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8043BO [id=" + id + ", transProcessTm=" + transProcessTm + ", transProcessNumbr=" + transProcessNumbr
				+ ", ecommB002PanNbr=" + ecommB002PanNbr + ", ecommB007GmtDateTime=" + ecommB007GmtDateTime
				+ ", ecommB032AcqId=" + ecommB032AcqId + ", ecommB037RetrvlRefNbr=" + ecommB037RetrvlRefNbr
				+ ", ecommB011SysAudtTrace=" + ecommB011SysAudtTrace + ", oriTransProcessTm=" + oriTransProcessTm
				+ ", oriTransProcessNumbr=" + oriTransProcessNumbr + ", productObjectCode=" + productObjectCode
				+ ", logFileFlag=" + logFileFlag + ", logTyp=" + logTyp + ", recordTyp=" + recordTyp + ", recordStatus="
				+ recordStatus + ", cutoffFlag=" + cutoffFlag + ", ecommInputSource=" + ecommInputSource
				+ ", ecommBinNbr=" + ecommBinNbr + ", mandatoryAuthFlag=" + mandatoryAuthFlag + ", receivingTransId="
				+ receivingTransId + ", receivingProgId=" + receivingProgId + ", tcpipQueueId=" + tcpipQueueId
				+ ", ecommProcessingDate=" + ecommProcessingDate + ", ecommProcessingTime=" + ecommProcessingTime
				+ ", transCurrencyPoint=" + transCurrencyPoint + ", accountCurrencyPoint=" + accountCurrencyPoint
				+ ", issueExcptFlag=" + issueExcptFlag + ", fraudFlag=" + fraudFlag + ", specContrlFlag="
				+ specContrlFlag + ", specContrlReason=" + specContrlReason + ", transDowngradeFlag="
				+ transDowngradeFlag + ", transChipFlag=" + transChipFlag + ", ecommAuthCde=" + ecommAuthCde
				+ ", ecommAuthResponseCde=" + ecommAuthResponseCde + ", declineRsn=" + declineRsn + ", declineInfo="
				+ declineInfo + ", ecommOperMode=" + ecommOperMode + ", ecommOperCurrency=" + ecommOperCurrency
				+ ", ecommAuthSceneCde=" + ecommAuthSceneCde + ", ecommTxnCde=" + ecommTxnCde + ", ecommContrlSceneCde="
				+ ecommContrlSceneCde + ", ecommDifferentCde=" + ecommDifferentCde + ", ecommListProperties="
				+ ecommListProperties + ", ecommListCode=" + ecommListCode + ", ecommTransLimitLevel="
				+ ecommTransLimitLevel + ", ecommTransLimitCde=" + ecommTransLimitCde + ", ecommChBillCurr="
				+ ecommChBillCurr + ", ecommCustomerNo=" + ecommCustomerNo + ", overpayAccountCde=" + overpayAccountCde
				+ ", overpayAccountCurrency=" + overpayAccountCurrency + ", authRequestTyp=" + authRequestTyp
				+ ", ecommReverseTyp=" + ecommReverseTyp + ", userId=" + userId + ", ecommCreditLimit="
				+ ecommCreditLimit + ", ecommAvailLimit=" + ecommAvailLimit + ", cashFlag=" + cashFlag
				+ ", ecommVerifnRsltCvv=" + ecommVerifnRsltCvv + ", ecommVerifnRsltIcvv=" + ecommVerifnRsltIcvv
				+ ", ecommVerifnRsltCvv2=" + ecommVerifnRsltCvv2 + ", ecommVerifnRsltCavv=" + ecommVerifnRsltCavv
				+ ", ecommVerifnRsltPin=" + ecommVerifnRsltPin + ", ecommVerifnRsltArqc=" + ecommVerifnRsltArqc
				+ ", ecommVerifnRsltExpDte=" + ecommVerifnRsltExpDte + ", ecommVerifnRsltPltNbrs="
				+ ecommVerifnRsltPltNbrs + ", ecommVerifnRsltExcp=" + ecommVerifnRsltExcp + ", ecommVerifnRsltActv="
				+ ecommVerifnRsltActv + ", ecommVerifnRsltBwlist=" + ecommVerifnRsltBwlist + ", ecommVerifnRsltVelo="
				+ ecommVerifnRsltVelo + ", ecommVerifnRsltLimit=" + ecommVerifnRsltLimit + ", ecommMsgTypeId="
				+ ecommMsgTypeId + ", transAccountCde=" + transAccountCde + ", ecommB003ProcCode=" + ecommB003ProcCode
				+ ", ecommB004TxnAmt=" + ecommB004TxnAmt + ", accountAmount=" + accountAmount + ", ecommB010ChargeRate="
				+ ecommB010ChargeRate + ", ecommB014ExpirDate=" + ecommB014ExpirDate + ", ecommB018MerchType="
				+ ecommB018MerchType + ", ecommB019CntryCode=" + ecommB019CntryCode + ", ecommB022Field="
				+ ecommB022Field + ", ecommB023CardSeqNbr=" + ecommB023CardSeqNbr + ", ecommB025PosCondCode="
				+ ecommB025PosCondCode + ", transFee=" + transFee + ", ecommB033FwdId=" + ecommB033FwdId
				+ ", ecommB038AuthCode=" + ecommB038AuthCode + ", ecommB041CardAccpt=" + ecommB041CardAccpt
				+ ", ecommB042CardAccptId=" + ecommB042CardAccptId + ", ecommB043ArdAccptDesc=" + ecommB043ArdAccptDesc
				+ ", transExtraInfo=" + transExtraInfo + ", ecommB049CurrCode=" + ecommB049CurrCode
				+ ", ecommB051ChCurrCode=" + ecommB051ChCurrCode + ", ecommB055ChipData=" + ecommB055ChipData
				+ ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version=" + version + "]";
	}
}
