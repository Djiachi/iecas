package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 信用卡分期账户信息查询
 *
 * @author cuiguangchao 2019年03月28日
 */
public class X5630BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 证件号码 [30,0] */
    private String idNumber;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 分期类型 */
    private String stageType;
    /** 开始时间 */
    private String beginDate;
    /** 结束时间 */
    private String endDate;
    /** 原交易流水号 */
    private String oldGlobalSerialNumbr;
    /** 账户类型 R-循环账户 T-交易账户 B-不良资产 [1,0] */
    private String accountType;

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

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOldGlobalSerialNumbr() {
        return oldGlobalSerialNumbr;
    }

    public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
        this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
    }

    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "X5630BO{" +
                "idNumber='" + idNumber + '\'' +
                ", externalIdentificationNo='" + externalIdentificationNo + '\'' +
                ", stageType='" + stageType + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", oldGlobalSerialNumbr='" + oldGlobalSerialNumbr + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
