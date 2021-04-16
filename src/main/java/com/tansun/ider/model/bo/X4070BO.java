package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

public class X4070BO extends BeanVO implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = -4056734798009500771L;
    /** 公共区 */
    private EventCommArea eventCommArea;
    /** 操作人 */
    private String operatorId;
    /** 事件号 */
    private String eventNo;
    /** 预算单位编码 */
    private String idNumber;
    /** 账单日期 */
    private String billDay;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 页码 */
    private int pageNum;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getBillDay() {
        return billDay;
    }

    public void setBillDay(String billDate) {
        this.billDay = billDate;
    }

    public EventCommArea getEventCommArea() {
        return eventCommArea;
    }

    public void setEventCommArea(EventCommArea eventCommArea) {
        this.eventCommArea = eventCommArea;
    }

    @Override
    public String getOperatorId() {
        return operatorId;
    }

    @Override
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String externalIdentificationNo) {
        this.idNumber = externalIdentificationNo;
    }

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }

    @Override
    public String toString() {
        return "X4050BO [idNumber=" + idNumber + ", globalEventNo=" + globalEventNo + "]";
    }

}
