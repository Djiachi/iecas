package com.tansun.ider.model.bo;

/**
 * <p> Title: X4020BO </p>
 * <p> Description: 处理预算单位片区号和客户的不同时，保存预算单位单位员工关系信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public class X4020BO {

    // 客户编号
    private String customerNo;
    // 客户所在片区号
    private String custGnsInfo;
    // 持卡人证件类型
    private String idType;
    // 持卡人证件编号
    private String idNumber;
    // 预算单位编号
    private String budgetOrgCode;
    // 客户外部识别号
    private String externalIdentificationNo;

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustGnsInfo() {
        return custGnsInfo;
    }

    public void setCustGnsInfo(String custGnsInfo) {
        this.custGnsInfo = custGnsInfo;
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

    public String getBudgetOrgCode() {
        return budgetOrgCode;
    }

    public void setBudgetOrgCode(String budgetUnitCode) {
        this.budgetOrgCode = budgetUnitCode;
    }

}
