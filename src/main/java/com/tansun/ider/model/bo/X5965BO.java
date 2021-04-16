package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 交易入账
 * @author qianyp
 */
public class X5965BO extends BeanVO implements Serializable{
    
    private static final long serialVersionUID = -973083846745094553L;
   
    /** 证件号码 */
    private String idNumber;
    /** 证件类型*/
    private String idType;
    /** 证件号码 [19,0] */
    private String externalIdentificationNo;
    /** 发生起始日期 yyyy-MM-dd [10,0] */
    private String startDate;
    /** 发生结束日期 yyyy-MM-dd [10,0] */
    private String endDate;
    /** 交易入账状态 N-未入账 F-入账失败 R-重发入账 Y-入账成功 [1,0] */
    private String transBillingState;
    /** 客户号 [12,0] */
    private String customerNo;
    public String getIdNumber() {
        return idNumber;
    }
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }
    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getTransBillingState() {
        return transBillingState;
    }
    public void setTransBillingState(String transBillingState) {
        this.transBillingState = transBillingState;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    @Override
    public String toString() {
        return "X5965BO [idNumber=" + idNumber + ", idType=" + idType + ", externalIdentificationNo="
                + externalIdentificationNo + ", startDate=" + startDate + ", endDate=" + endDate
                + ", transBillingState=" + transBillingState + ", customerNo=" + customerNo + "]";
    }
}
