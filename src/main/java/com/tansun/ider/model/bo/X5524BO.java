package com.tansun.ider.model.bo;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5524 非对象构件实例
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5524BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    private String id;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 实例类型 D：核算状态，E：事件，B：封锁码 [1,0] */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String instanDimen;
    /** 实例代码 当实例类型为D时，核算状态码与账户基本信息表中的核算状态码一致。 为E时，来自事件表的事件编码 [14,0] */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String instanCode;
    /** 构件编号 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String artifactNo;
    /** 元件编号 [15,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String elementNo;
    /** 执行顺序 [10,0] */
    @Digits(integer = 10, fraction = 0)
    private Integer performOrder;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "X5524BO [id=" + id + ", operationMode=" + operationMode + ", instanDimen=" + instanDimen
                + ", instanCode=" + instanCode + ", artifactNo=" + artifactNo + ", elementNo=" + elementNo
                + ", performOrder=" + performOrder + "]";
    }

}
