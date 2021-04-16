package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 货币
 * @Author lianhuan
 * @Date 2018年4月25日下午2:48:15
 */
public class X5512BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -5666151452489974741L;

    /** 货币代码 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String currencyCode;
    /** 币种描述 [50,0] */
    private String description;
    /** 小数位位数 [1,0] */
    private String decimalPlaces;
    /** 货币简称 [9,0] */
    private String referred;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getReferred() {
        return referred;
    }

    public void setReferred(String referred) {
        this.referred = referred;
    }

    @Override
    public String toString() {
        return "X5512BO [currencyCode=" + currencyCode + ", description=" + description + ", decimalPlaces="
                + decimalPlaces + ", referred=" + referred + "]";
    }

}
