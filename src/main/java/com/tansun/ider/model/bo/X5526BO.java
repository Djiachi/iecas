package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 定价标签
 * @Author lianhuan
 * @Date 2018年4月25日下午3:03:34
 */
public class X5526BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 定价标签 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String priceTag;
    /** 描述 [50,0] Not NULL */
    private String desc;
    /** D-差异化 P-个性化 A-活动 [1,0] */
    private String priceArea;
    /** 定价对象 [8,0] Not NULL */
    private String priceObj;
    /** 优先级 [10,0] */
    private Integer performOrder;
    /** I-继承 O-覆盖 [1,0] */
    private String priceModel;
    /** 取值类型 D：数值 P：百分比 [1,0] Not NULL */
    private String valTyp;
    /** 取值 [10,0] */
    private Long value;
    /** 取值小数位 [10,0] */
    private Integer valPoint;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getPriceTag() {
        return priceTag;
    }

    public void setPriceTag(String priceTag) {
        this.priceTag = priceTag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPriceArea() {
        return priceArea;
    }

    public void setPriceArea(String priceArea) {
        this.priceArea = priceArea;
    }

    public String getPriceObj() {
        return priceObj;
    }

    public void setPriceObj(String priceObj) {
        this.priceObj = priceObj;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    public String getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(String priceModel) {
        this.priceModel = priceModel;
    }

    public String getValTyp() {
        return valTyp;
    }

    public void setValTyp(String valTyp) {
        this.valTyp = valTyp;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Integer getValPoint() {
        return valPoint;
    }

    public void setValPoint(Integer valPoint) {
        this.valPoint = valPoint;
    }

    @Override
    public String toString() {
        return "X5527BO [operationMode=" + operationMode + ", priceTag=" + priceTag + ", desc=" + desc + ", priceArea="
                + priceArea + ", priceObj=" + priceObj + ", performOrder=" + performOrder + ", priceModel=" + priceModel
                + ", valTyp=" + valTyp + ", value=" + value + ", valPoint=" + valPoint + "]";
    }

}
