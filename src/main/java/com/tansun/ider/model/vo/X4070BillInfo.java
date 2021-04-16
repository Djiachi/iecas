package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * <p> Title: X4070BillInfo </p>
 * <p> Description: 已出账单信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年6月13日
 */
public class X4070BillInfo {
    /** 账单所属客户姓名 */
    private String customerName;
    /** 外部识别号 */
    private String externalIdentificationNo;
    /** 账单周期 */
    private Integer currentCycleNumber;
    /** 账单金额 */
    private BigDecimal postingAmount;
    /** 币种 */
    private String currencyCode;
    /** 产品描述 */
    private String productDesc;
    /** 账单创建时间 */
    private String gmtCreate;

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public Integer getCurrentCycleNumber() {
        return currentCycleNumber;
    }

    public void setCurrentCycleNumber(Integer currentCycleNumber) {
        this.currentCycleNumber = currentCycleNumber;
    }

    public BigDecimal getPostingAmount() {
        return postingAmount;
    }

    public void setPostingAmount(BigDecimal postingAmount) {
        this.postingAmount = postingAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

}
