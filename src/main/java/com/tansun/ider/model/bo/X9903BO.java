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
 * @Author wt
 * @Date 2018年5月23日 上午9:46:34
 */

public class X9903BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 5035068883811420241L;
	
	/** 活动代码 */
    private String actionCode;
	
    /** 产品对象代码 */
    private String productObjectCode;
	
    /** 业务类型代码 */
	private String businessTypeCode;
	
    /** 余额对象代码 */
    private String balanceObjectCode;
	
    /** 交易日期 */
    private String transDate;
	
	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}

	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	@Override
	public String toString() {
		return "X9903BO [actionCode=" + actionCode + ", productObjectCode="
				+ productObjectCode + ", businessTypeCode=" + businessTypeCode
				+ ", balanceObjectCode=" + balanceObjectCode + ", transDate="
				+ transDate + "]";
	}

}
