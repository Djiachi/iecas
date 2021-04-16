package com.tansun.ider.model.bo;

/**
 * <p> Title: X4015BO </p>
 * <p> Description: 查询个人公务卡最大限额接口入参</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
public class X4015BO {

    /** 全局事件编号 */
    private String globalEventNo;
    /** 运营模式 */
    private String operationMode;
    /** 客户代码 */
    private String customerNo;
    /** 产品对象代码 */
    private String productObjectCode;
    /** 预算单位编码 */
    private String budgetOrgCode;

    public String getBudgetOrgCode() {
        return budgetOrgCode;
    }

    public void setBudgetOrgCode(String budgetOrgCode) {
        this.budgetOrgCode = budgetOrgCode;
    }

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
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

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

}
