package com.tansun.ider.model.bo;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

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
 */

public class X7071BO {
	
	
	/** 分期控制id */
	  private String stageId;
	/** 分期控制名称 */
	 @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	  private String stageControlName;
	  /** 适用类型 */
	 @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	  private String appType;
	  /** 手续费收取方式*/
	 @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	  private String feeCollectType;
	  /** 发卡业务类型 */
	 @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	  private String projectType;
	  /** 最小金额（自动分期用） */
	  private BigDecimal minAmt;
	  /** 最大金额（自动分期用） */
	  private BigDecimal maxAmt;
	  /** 费率 */
	  @DecimalMin("0")
	  private BigDecimal feeRate;
	  /** 期次 */
	  @Min(0)
	  private Integer period;
	  /** 费率类型 */
	/*  private BigDecimal feeRateType;*/
	 
	  
	public String getStageId() {
			return stageId;
	}
	public void setStageId(String stageId) {
			this.stageId = stageId;
	}
	public String getStageControlName() {
		return stageControlName;
	}
	public void setStageControlName(String stageControlName) {
		this.stageControlName = stageControlName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getFeeCollectType() {
		return feeCollectType;
	}
	public void setFeeCollectType(String feeCollectType) {
		this.feeCollectType = feeCollectType;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public BigDecimal getMinAmt() {
		return minAmt;
	}
	public void setMinAmt(BigDecimal minAmt) {
		this.minAmt = minAmt;
	}
	public BigDecimal getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}

	

    
}
