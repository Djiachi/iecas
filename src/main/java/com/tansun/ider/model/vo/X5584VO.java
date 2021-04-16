package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5584VO extends BeanVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7076827518416699616L;
    private String id;
    /** 运营模式 [3,0] Not NULL */
    private String operationMode;
    /** 实例类型 D：核算状态，E：事件，B：封锁码 [1,0] */
    private String instanDimen;
    /** 实例代码 当实例类型为D时，核算状态码与账户基本信息表中的核算状态码一致。 为E时，来自事件表的事件编码 [14,0] */
    private String instanCode;
    /** 构件编号 [3,0] Not NULL */
    private String artifactNo;
    /** 元件编号 [15,0] Not NULL */
    private String elementNo;
    /** 执行顺序 [10,0] */
    private Integer performOrder;
    /** 构件编号 [3,0] Not NULL */
    private String artifactDesc;
    /** 元件编号 [15,0] Not NULL */
    private String elementDesc;
    /** PCD编号 [8,0] Not NULL */
    private String pcdNo;
    /** 币种 [3,0] */
    private String currencyCode;
    /** 取值类型 D：数值 P：百分比 [1,0] Not NULL */
    private String pcdType;
    /** 取值 [10,0] */
    @Digits(integer = 10, fraction = 0)
    private BigDecimal pcdValue;
    /** 取值小数位 [3,0] */
    @Digits(integer = 3, fraction = 0)
    private Integer pcdPoint;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getInstanDimen() {
        return instanDimen;
    }

    public void setInstanDimen(String instanDimen) {
        this.instanDimen = instanDimen;
    }

    public String getInstanCode() {
        return instanCode;
    }

    public void setInstanCode(String instanCode) {
        this.instanCode = instanCode;
    }

    public String getArtifactNo() {
        return artifactNo;
    }

    public void setArtifactNo(String artifactNo) {
        this.artifactNo = artifactNo;
    }

    public String getElementNo() {
        return elementNo;
    }

    public void setElementNo(String elementNo) {
        this.elementNo = elementNo;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    public String getArtifactDesc() {
        return artifactDesc;
    }

    public void setArtifactDesc(String artifactDesc) {
        this.artifactDesc = artifactDesc;
    }

    public String getElementDesc() {
        return elementDesc;
    }

    public void setElementDesc(String elementDesc) {
        this.elementDesc = elementDesc;
    }

    public String getPcdNo() {
        return pcdNo;
    }

    public void setPcdNo(String pcdNo) {
        this.pcdNo = pcdNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPcdType() {
        return pcdType;
    }

    public void setPcdType(String pcdType) {
        this.pcdType = pcdType;
    }

    public BigDecimal getPcdValue() {
        return pcdValue;
    }

    public void setPcdValue(BigDecimal pcdValue) {
        this.pcdValue = pcdValue;
    }

    public Integer getPcdPoint() {
        return pcdPoint;
    }

    public void setPcdPoint(Integer pcdPoint) {
        this.pcdPoint = pcdPoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "X5584VO [id=" + id + ", operationMode=" + operationMode + ", instanDimen=" + instanDimen
                + ", instanCode=" + instanCode + ", artifactNo=" + artifactNo + ", elementNo=" + elementNo
                + ", performOrder=" + performOrder + ", artifactDesc=" + artifactDesc + ", elementDesc=" + elementDesc
                + ", pcdNo=" + pcdNo + ", currencyCode=" + currencyCode + ", pcdType=" + pcdType + ", pcdValue="
                + pcdValue + ", pcdPoint=" + pcdPoint + "]";
    }

}
