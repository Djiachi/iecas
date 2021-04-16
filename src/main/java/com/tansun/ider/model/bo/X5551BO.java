package com.tansun.ider.model.bo;

import com.tansun.ider.service.business.EventCommArea;

/**
 *
 */

public class X5551BO {

    private String customerNo;

    private String accountId;

    private String businessProgramNo;

    private String businessTypeCode;

    private String ecommOperMode;

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

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEcommOperMode() {
        return ecommOperMode;
    }

    public void setEcommOperMode(String ecommOperMode) {
        this.ecommOperMode = ecommOperMode;
    }
}
