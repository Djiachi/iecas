package com.tansun.ider.model.vo;

import java.math.BigDecimal;

public class X5430VO {

	/** 客户代码 [12,0] */
	private String customerNo;
	/** 业务项目代码 [9,0] Not NULL */
	private String businessProgramNo;
	/** 定价对象 [3,0] */
	private String pricingObject;
	/** 定价对象代码 [3,0] */
	private String pricingObjectCode;
	/** 标签号 [9,0] */
	private String pricingTag;
	/** 标签生效日期 [10,0] */
	private String custTagEffectiveDate;
	/** 标签失效日期 [10,0] */
	private String custTagExpirationDate;
    /** 运行模式 [3,0] Not NULL */
    private String operationMode;
    private String id;
    /** 描述 [50,0] Not NULL */
    private String pricingDesc;
    /** D-差异化 P-个性化 A-活动 [1,0] */
    private String pricingType;
    /** 优先级 [10,0] */
    private Integer performOrder;
    /** 定价方式 I-继承 O-覆盖 C-取优 [1,0] */
    private String pricingMethod;
    /** 取值类型 D：数值  P：百分比 如果定价方式为O，则取值类型只能为P [1,0] Not NULL */
    private String pcdType;
    /** 取值 [18,0] */
    private BigDecimal pcdValue;
    /** 取值小数位 [10,0] */
    private Integer pcdPoint;
    /** 附卡基准年费 [18,0] */
    private BigDecimal supplementBaseFee;
    /** 定价对象描述*/
    private String pricingObjectDesc;
    /** 定价层级 [1,0] */
    private String pricingLevel;
    /** 定价层级代码 [12,0] */
    private String pricingLevelCode;
    /** 设置日期 [10,0] Not NULL */
    private String settingDate;
    /** 设置时间 [13,0] Not NULL */
    private String settingTime;
    /** 设置人员代码 [20,0] Not NULL */
    private String settingUpUserid;
    /** 解除人员代码 [20,0] */
    private String removalUserid;
    /** 解除日期 [10,0] */
    private String removeDate;
    /** 状态(Y-有效 D-已解除) [1,0] Not NULL */
    private String state;
    /** 目前可选择币种 [3,0] Not NULL */
    private String pricingScope;
    /** 币种描述 */
    private String currencyDesc;
  
    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getPricingScope() {
        return pricingScope;
    }

    public void setPricingScope(String pricingScope) {
        this.pricingScope = pricingScope;
    }
    public String getPricingLevel() {
		return pricingLevel;
	}
	public void setPricingLevel(String pricingLevel) {
		this.pricingLevel = pricingLevel;
	}
	public String getPricingLevelCode() {
		return pricingLevelCode;
	}
	public void setPricingLevelCode(String pricingLevelCode) {
		this.pricingLevelCode = pricingLevelCode;
	}
	public String getSettingDate() {
		return settingDate;
	}
	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}
	public String getSettingTime() {
		return settingTime;
	}
	public void setSettingTime(String settingTime) {
		this.settingTime = settingTime;
	}
	public String getSettingUpUserid() {
		return settingUpUserid;
	}
	public void setSettingUpUserid(String settingUpUserid) {
		this.settingUpUserid = settingUpUserid;
	}
	public String getRemovalUserid() {
		return removalUserid;
	}
	public void setRemovalUserid(String removalUserid) {
		this.removalUserid = removalUserid;
	}
	public String getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPricingObjectDesc() {
        return pricingObjectDesc;
    }
    public void setPricingObjectDesc(String pricingObjectDesc) {
        this.pricingObjectDesc = pricingObjectDesc;
    }
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public String getPricingObject() {
		return pricingObject;
	}
	public void setPricingObject(String pricingObject) {
		this.pricingObject = pricingObject;
	}
	public String getPricingObjectCode() {
		return pricingObjectCode;
	}
	public void setPricingObjectCode(String pricingObjectCode) {
		this.pricingObjectCode = pricingObjectCode;
	}
	public String getPricingTag() {
		return pricingTag;
	}
	public void setPricingTag(String pricingTag) {
		this.pricingTag = pricingTag;
	}
	public String getCustTagEffectiveDate() {
		return custTagEffectiveDate;
	}
	public void setCustTagEffectiveDate(String custTagEffectiveDate) {
		this.custTagEffectiveDate = custTagEffectiveDate;
	}
	public String getCustTagExpirationDate() {
		return custTagExpirationDate;
	}
	public void setCustTagExpirationDate(String custTagExpirationDate) {
		this.custTagExpirationDate = custTagExpirationDate;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getPricingDesc() {
        return pricingDesc;
    }
    public void setPricingDesc(String pricingDesc) {
        this.pricingDesc = pricingDesc;
    }
    public String getPricingType() {
        return pricingType;
    }
    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }
    public Integer getPerformOrder() {
        return performOrder;
    }
    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }
    public String getPricingMethod() {
        return pricingMethod;
    }
    public void setPricingMethod(String pricingMethod) {
        this.pricingMethod = pricingMethod;
    }
    public String getPcdType() {
        return pcdType;
    }
    public void setPcdType(String pcdType) {
        this.pcdType = pcdType;
    }
    public BigDecimal getPcdValue() {
        return pcdValue;
    }
    public void setPcdValue(BigDecimal pcdValue) {
        this.pcdValue = pcdValue;
    }
    public Integer getPcdPoint() {
        return pcdPoint;
    }
    public void setPcdPoint(Integer pcdPoint) {
        this.pcdPoint = pcdPoint;
    }
    public BigDecimal getSupplementBaseFee() {
        return supplementBaseFee;
    }
    public void setSupplementBaseFee(BigDecimal supplementBaseFee) {
        this.supplementBaseFee = supplementBaseFee;
    }
    
}
