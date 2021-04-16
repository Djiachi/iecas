package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * Bean Validation 中内置的 constraint
 * 
 * @Null 被注释的元素必须为 null
 * @NotNull 被注释的元素必须不为 null
 * @AssertTrue 被注释的元素必须为 true
 * @AssertFalse 被注释的元素必须为 false
 * @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 @Size(max=, min=)
 *                    被注释的元素的大小必须在指定的范围内
 * @Digits (integer, fraction) 被注释的元素必须是一个数字，其值必须在可接受的范围内
 * @Past 被注释的元素必须是一个过去的日期
 * @Future 被注释的元素必须是一个将来的日期 @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 * 
 *         Hibernate Validator 附加的 constraint
 * @NotBlank(message =) 验证字符串非null，且长度必须大于0
 * @Email 被注释的元素必须是电子邮箱地址 @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 * @NotEmpty 被注释的字符串的必须非空 @Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 * 
 * @Desc:
 * @Author chenyinliao
 * @Date 2018年5月25日 上午9:46:34
 */

public class X5215BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
	/**主证件类型 */
	private String idType;
	/** 主证件号码 */
	private String idNumber;
    /** 账户代码 */
    private String accountId;
    /** 余额单元代码 */
    private String balanceUnitCode;
    /** 客户代码 */
    private String customerNo;
    /** 外部识别号 */
    private String externalIdentificationNo;
    /** 交易日期 */
    private String transDate;
    /** 起始日期 */
    private String startDate;
    /** 截止日期 */
    private String endDate;
    /** 账单日期 */
    protected String billDate;
    /** 上一账单日 */
    protected String lastBillDate;
    /** 活动编号 */
    private String actNo;
    /** 证件号 */
    private String credentialNumber;
    /** 币种 */
    private String currency;
    /** 是金融交易查询 ，还是账单交易查询*/
    private String type;
    
    private String eventNo;
    //全局流水号
    private String globalSerialNumbr;
    
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBalanceUnitCode() {
        return balanceUnitCode;
    }

    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getLastBillDate() {
        return lastBillDate;
    }

    public void setLastBillDate(String lastBillDate) {
        this.lastBillDate = lastBillDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "X5215BO [accountId=" + accountId + ", balanceUnitCode=" + balanceUnitCode + ", customerNo=" + customerNo
                + ", externalIdentificationNo=" + externalIdentificationNo + ", transDate=" + transDate + ", startDate="
                + startDate + ", endDate=" + endDate + ", billDate=" + billDate + ", lastBillDate=" + lastBillDate
                + ", actNo=" + actNo + ", credentialNumber=" + credentialNumber + ", currency=" + currency + ", type="
                + type + "]";
    }

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getGlobalSerialNumbr() {
		return globalSerialNumbr;
	}

	public void setGlobalSerialNumbr(String globalSerialNumbr) {
		this.globalSerialNumbr = globalSerialNumbr;
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

	
	
}
