package com.tansun.ider.model.bo;

/**
 * <p> Title: X4025BO </p>
 * <p> Description: 预算单位与客户关联关系建立</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月11日
 */
public class X4025BO {
	
	/** 附属卡 */
	private String supplyCustomerNo;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 产品对象代码 */
    private String productObjectCode;
    /** 预算单位编码 */
    private String budgetOrgCode;
    /** 外部识别号 */
    private String externalIdentificationNo;
    /** 客户号 */
    private String customerNo;
    /** 运行模式 */
    private String operationMode;
    /** 媒介单元代码 [18,0] Not NULL */
    private String mediaUnitCode;
    /** 营业日期 [10,0] Not NULL */
    private String operationDate;
  

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
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

    public String getBudgetOrgCode() {
        return budgetOrgCode;
    }

    public void setBudgetOrgCode(String budgetUnitCode) {
        this.budgetOrgCode = budgetUnitCode;
    }

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

	public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	public String getSupplyCustomerNo() {
		return supplyCustomerNo;
	}

	public void setSupplyCustomerNo(String supplyCustomerNo) {
		this.supplyCustomerNo = supplyCustomerNo;
	}

}
