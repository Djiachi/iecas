package com.tansun.ider.model.bo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


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
 * @Date 2018年5月31日 上午9:46:34
 */

public class X9904BO {

	/** 运营模式 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	protected String operationMode;
	
	/** 透支费用代码 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String overdraftCostCode;
	
    /** 顺序号 */
    protected Integer orderIndex;
	
    /** 业务类型代码 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String businessTypeCode;
	
    /** 币种 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String currency;
	
    /** 描述 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String transDesc;
	
    /** 交易费用标识  */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String transCostSingCash;
	
    /** 提现交易来源 */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String transChannelCash;
	
    /** 提现手续费 */
	@NotNull
    protected BigDecimal cashFixedCharge;
	
    /** 最小费用 */
	@NotNull
    protected BigDecimal cashMinCharge;
	
    /** 最大费用 */
	@NotNull
    protected BigDecimal cashMaxCharge;
	
    /** 比例1 */
	@NotNull
    protected Integer cashScale1;
	
    /** 限额1 */
	@NotNull
    protected BigDecimal cashQuota1;
	
    /** 比例2 */
	@NotNull
    protected Integer cashScale2;
	
    /** 限额2 */
	@NotNull
    protected BigDecimal cashQuota2;
	
    /** 比例3 */
	@NotNull
    protected Integer cashScale3;
	
    /** 限额3 */
	@NotNull
    protected BigDecimal cashQuota3;
	
    /** 转账交易费用标识 */
	@NotNull
    protected String transCostSingTransfer;
	
    /** 转账交易来源*/
	@NotNull
    protected String transChannelTransfer;
	
    /** 转账手续费 */
	@NotNull
    protected BigDecimal transferFixedCharge;
	
    /** 最小费用 */
	@NotNull
    protected BigDecimal transferMinCharge;
	
    /** 最大费用 */
	@NotNull
    protected BigDecimal transferMaxCharge;
	
    /** 比例1 */
	@NotNull
    protected Integer transferScale1;
	
    /** 限额1 */
	@NotNull
    protected BigDecimal transferQuota1;
	
    /** 比例2*/
	@NotNull
    protected Integer transferScale2;
	
    /** 限额2 */
	@NotNull
    protected BigDecimal transferQuota2;
	
    /** 比例3 */
	@NotNull
    protected Integer transferScale3;
	
    /** 限额3 */
	@NotNull
    protected BigDecimal transferQuota3;
	
	/** 产品代码*/
    protected String productObjectCode;

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
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

	public String getOverdraftCostCode() {
		return overdraftCostCode;
	}

	public void setOverdraftCostCode(String overdraftCostCode) {
		this.overdraftCostCode = overdraftCostCode;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransDesc() {
		return transDesc;
	}

	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}

	public String getTransCostSingCash() {
		return transCostSingCash;
	}

	public void setTransCostSingCash(String transCostSingCash) {
		this.transCostSingCash = transCostSingCash;
	}

	public String getTransChannelCash() {
		return transChannelCash;
	}

	public void setTransChannelCash(String transChannelCash) {
		this.transChannelCash = transChannelCash;
	}

	public BigDecimal getCashFixedCharge() {
		return cashFixedCharge;
	}

	public void setCashFixedCharge(BigDecimal cashFixedCharge) {
		this.cashFixedCharge = cashFixedCharge;
	}

	public BigDecimal getCashMinCharge() {
		return cashMinCharge;
	}

	public void setCashMinCharge(BigDecimal cashMinCharge) {
		this.cashMinCharge = cashMinCharge;
	}

	public BigDecimal getCashMaxCharge() {
		return cashMaxCharge;
	}

	public void setCashMaxCharge(BigDecimal cashMaxCharge) {
		this.cashMaxCharge = cashMaxCharge;
	}

	public Integer getCashScale1() {
		return cashScale1;
	}

	public void setCashScale1(Integer cashScale1) {
		this.cashScale1 = cashScale1;
	}

	public BigDecimal getCashQuota1() {
		return cashQuota1;
	}

	public void setCashQuota1(BigDecimal cashQuota1) {
		this.cashQuota1 = cashQuota1;
	}

	public Integer getCashScale2() {
		return cashScale2;
	}

	public void setCashScale2(Integer cashScale2) {
		this.cashScale2 = cashScale2;
	}

	public BigDecimal getCashQuota2() {
		return cashQuota2;
	}

	public void setCashQuota2(BigDecimal cashQuota2) {
		this.cashQuota2 = cashQuota2;
	}

	public Integer getCashScale3() {
		return cashScale3;
	}

	public void setCashScale3(Integer cashScale3) {
		this.cashScale3 = cashScale3;
	}

	public BigDecimal getCashQuota3() {
		return cashQuota3;
	}

	public void setCashQuota3(BigDecimal cashQuota3) {
		this.cashQuota3 = cashQuota3;
	}

	public String getTransCostSingTransfer() {
		return transCostSingTransfer;
	}

	public void setTransCostSingTransfer(String transCostSingTransfer) {
		this.transCostSingTransfer = transCostSingTransfer;
	}

	public String getTransChannelTransfer() {
		return transChannelTransfer;
	}

	public void setTransChannelTransfer(String transChannelTransfer) {
		this.transChannelTransfer = transChannelTransfer;
	}

	public BigDecimal getTransferFixedCharge() {
		return transferFixedCharge;
	}

	public void setTransferFixedCharge(BigDecimal transferFixedCharge) {
		this.transferFixedCharge = transferFixedCharge;
	}

	public BigDecimal getTransferMinCharge() {
		return transferMinCharge;
	}

	public void setTransferMinCharge(BigDecimal transferMinCharge) {
		this.transferMinCharge = transferMinCharge;
	}

	public BigDecimal getTransferMaxCharge() {
		return transferMaxCharge;
	}

	public void setTransferMaxCharge(BigDecimal transferMaxCharge) {
		this.transferMaxCharge = transferMaxCharge;
	}

	public Integer getTransferScale1() {
		return transferScale1;
	}

	public void setTransferScale1(Integer transferScale1) {
		this.transferScale1 = transferScale1;
	}

	public BigDecimal getTransferQuota1() {
		return transferQuota1;
	}

	public void setTransferQuota1(BigDecimal transferQuota1) {
		this.transferQuota1 = transferQuota1;
	}

	public Integer getTransferScale2() {
		return transferScale2;
	}

	public void setTransferScale2(Integer transferScale2) {
		this.transferScale2 = transferScale2;
	}

	public BigDecimal getTransferQuota2() {
		return transferQuota2;
	}

	public void setTransferQuota2(BigDecimal transferQuota2) {
		this.transferQuota2 = transferQuota2;
	}

	public Integer getTransferScale3() {
		return transferScale3;
	}

	public void setTransferScale3(Integer transferScale3) {
		this.transferScale3 = transferScale3;
	}

	public BigDecimal getTransferQuota3() {
		return transferQuota3;
	}

	public void setTransferQuota3(BigDecimal transferQuota3) {
		this.transferQuota3 = transferQuota3;
	}

	@Override
	public String toString() {
		return "X9904BO [operationMode=" + operationMode
				+ ", overdraftCostCode=" + overdraftCostCode + ", orderIndex="
				+ orderIndex + ", businessTypeCode=" + businessTypeCode
				+ ", currency=" + currency + ", transDesc=" + transDesc
				+ ", transCostSingCash=" + transCostSingCash
				+ ", transChannelCash=" + transChannelCash
				+ ", cashFixedCharge=" + cashFixedCharge + ", cashMinCharge="
				+ cashMinCharge + ", cashMaxCharge=" + cashMaxCharge
				+ ", cashScale1=" + cashScale1 + ", cashQuota1=" + cashQuota1
				+ ", cashScale2=" + cashScale2 + ", cashQuota2=" + cashQuota2
				+ ", cashScale3=" + cashScale3 + ", cashQuota3=" + cashQuota3
				+ ", transCostSingTransfer=" + transCostSingTransfer
				+ ", transChannelTransfer=" + transChannelTransfer
				+ ", transferFixedCharge=" + transferFixedCharge
				+ ", transferMinCharge=" + transferMinCharge
				+ ", transferMaxCharge=" + transferMaxCharge
				+ ", transferScale1=" + transferScale1 + ", transferQuota1="
				+ transferQuota1 + ", transferScale2=" + transferScale2
				+ ", transferQuota2=" + transferQuota2 + ", transferScale3="
				+ transferScale3 + ", transferQuota3=" + transferQuota3 + "]";
	}
	
}
