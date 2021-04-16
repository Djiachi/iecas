package com.tansun.ider.model.bo;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;

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
 * @Author huangyayun
 * @Date 2018年5月29日
 * 分期金额（*），期次，分期类型（*），客群，产品，消费地区，商户id，mcc,授权编码，卡号，如是自动分期，期次可不填。
 */

public class X7074BO {
		/** 分期金额 */
		@DecimalMin("0")
		private BigDecimal ecommInstallAmount;
		/** 分期期次*/
		private int ecommInstallNbr;
		/** 分期类型 */
		@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
		private String ecommInstallTyp;
		/** 卡号 */
		@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
		private String ecommB002PanNbr;
		/** 客群 */
		private String ecommCustomerRiskGroup;
		/** mcc */
		private String ecommB018MerchType;
		/** 产品 */
		private String ecommProductObj;
		/** 商户id */
		private String ecommB042CardAccptId;
		/** 地区 */
		private String area;
		/** 渠道 */
		private String appChannel;
		/** 币种 */
		private String ecommB006ChBillCurr;
		/** 账单日 */
		private String billDay;
		/** 交易日期 */
		private String ecommB007GmtDateTime;
		/** 授权编号 */
		private String ecommAuthCode;
		/** 客户编号 */
		private String ecommCustomerNo;
		
		public BigDecimal getEcommInstallAmount() {
			return ecommInstallAmount;
		}
		public void setEcommInstallAmount(BigDecimal ecommInstallAmount) {
			this.ecommInstallAmount = ecommInstallAmount;
		}
		public int getEcommInstallNbr() {
			return ecommInstallNbr;
		}
		public void setEcommInstallNbr(int ecommInstallNbr) {
			this.ecommInstallNbr = ecommInstallNbr;
		}
		public String getEcommInstallTyp() {
			return ecommInstallTyp;
		}
		public void setEcommInstallTyp(String ecommInstallTyp) {
			this.ecommInstallTyp = ecommInstallTyp;
		}
		public String getEcommB002PanNbr() {
			return ecommB002PanNbr;
		}
		public void setEcommB002PanNbr(String ecommB002PanNbr) {
			this.ecommB002PanNbr = ecommB002PanNbr;
		}
		public String getEcommCustomerRiskGroup() {
			return ecommCustomerRiskGroup;
		}
		public void setEcommCustomerRiskGroup(String ecommCustomerRiskGroup) {
			this.ecommCustomerRiskGroup = ecommCustomerRiskGroup;
		}
		public String getEcommB018MerchType() {
			return ecommB018MerchType;
		}
		public void setEcommB018MerchType(String ecommB018MerchType) {
			this.ecommB018MerchType = ecommB018MerchType;
		}
		public String getEcommProductObj() {
			return ecommProductObj;
		}
		public void setEcommProductObj(String ecommProductObj) {
			this.ecommProductObj = ecommProductObj;
		}
		public String getEcommB042CardAccptId() {
			return ecommB042CardAccptId;
		}
		public void setEcommB042CardAccptId(String ecommB042CardAccptId) {
			this.ecommB042CardAccptId = ecommB042CardAccptId;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getAppChannel() {
			return appChannel;
		}
		public void setAppChannel(String appChannel) {
			this.appChannel = appChannel;
		}
		public String getEcommB006ChBillCurr() {
			return ecommB006ChBillCurr;
		}
		public void setEcommB006ChBillCurr(String ecommB006ChBillCurr) {
			this.ecommB006ChBillCurr = ecommB006ChBillCurr;
		}
		public String getBillDay() {
			return billDay;
		}
		public void setBillDay(String billDay) {
			this.billDay = billDay;
		}
		public String getEcommB007GmtDateTime() {
			return ecommB007GmtDateTime;
		}
		public void setEcommB007GmtDateTime(String ecommB007GmtDateTime) {
			this.ecommB007GmtDateTime = ecommB007GmtDateTime;
		}
		public String getEcommAuthCode() {
			return ecommAuthCode;
		}
		public void setEcommAuthCode(String ecommAuthCode) {
			this.ecommAuthCode = ecommAuthCode;
		}
		public String getEcommCustomerNo() {
			return ecommCustomerNo;
		}
		public void setEcommCustomerNo(String ecommCustomerNo) {
			this.ecommCustomerNo = ecommCustomerNo;
		}
		@Override
		public String toString() {
			return "X7074BO [ecommInstallAmount=" + ecommInstallAmount + ", ecommInstallNbr=" + ecommInstallNbr
					+ ", ecommInstallTyp=" + ecommInstallTyp + ", ecommB002PanNbr=" + ecommB002PanNbr
					+ ", ecommCustomerRiskGroup=" + ecommCustomerRiskGroup + ", ecommB018MerchType="
					+ ecommB018MerchType + ", ecommProductObj=" + ecommProductObj + ", ecommB042CardAccptId="
					+ ecommB042CardAccptId + ", area=" + area + ", appChannel=" + appChannel + ", ecommB006ChBillCurr="
					+ ecommB006ChBillCurr + ", billDay=" + billDay + ", ecommB007GmtDateTime=" + ecommB007GmtDateTime
					+ ", ecommAuthCode=" + ecommAuthCode + ", ecommCustomerNo=" + ecommCustomerNo + "]";
		}
	
		
}
