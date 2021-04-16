/**
 * 
 */
package com.tansun.ider.model.bo;

/**
 * @Desc:
 * @Author wt
 * @Date 2018年5月28日 下午5:10:20
 */
public class X5573BO {

    /** 创建一个全局流水号 */
    private String globalEventNo;
    /** 赋予初始值 */
    private String ecommEventId; // 事件ID CRDPR40G000001
    /** 客户号 */
    private String customerNo;
    /** 客户姓名 */
    private String customerName;
    /** 身份证号 */
    private String credentialNumber;
    public String getGlobalEventNo() {
        return globalEventNo;
    }
    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }
    public String getEcommEventId() {
        return ecommEventId;
    }
    public void setEcommEventId(String ecommEventId) {
        this.ecommEventId = ecommEventId;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCredentialNumber() {
        return credentialNumber;
    }
    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }
    @Override
    public String toString() {
        return "X5573BO [globalEventNo=" + globalEventNo + ", ecommEventId=" + ecommEventId + ", customerNo="
                + customerNo + ", customerName=" + customerName + ", credentialNumber=" + credentialNumber + "]";
    }
    
}
