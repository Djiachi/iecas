package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 最近一期未支付计划信息查询
 *
 * @author liuyanxi
 *
 */
public class X5645BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 证件号码 [30,0] */
    private String idNumber;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 分期类型 */
    private String accountId;
    /** 原交易流水号 */
    private String oldGlobalSerialNumbr;
    /** 币种 */
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOldGlobalSerialNumbr() {
        return oldGlobalSerialNumbr;
    }

    public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
        this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
    }

}
