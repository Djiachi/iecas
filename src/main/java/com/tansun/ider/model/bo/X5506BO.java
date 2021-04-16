package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5506运营模式
 * @Author lianhuan
 * @Date 2018年4月24日下午5:42:00
 */
public class X5506BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -5666151452489974741L;

    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 运营核算币种 [3,0] Not NULL */
    private String accountCurrency;
    /** 模式名称 [50,0] Not NULL */
    private String modeName;
    /** 上一利息累计日期 [10,0] */
    private String prevInterestAccumulate;
    /** 本次利息累计日期 [10,0] */
    private String currInterestAccumulate;
    /** 下一次利息累计日期 [10,0] */
    private String nextInterestAccumulate;
    /** 营业日期 [10,0] Not NULL */
    private String operationDate;
    /** 额度树编号 [3,0] */
    private String creditTreeId;
    /** 溢缴款业务类型 [9,0] */
    private String excessContriServTyp;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getPrevInterestAccumulate() {
        return prevInterestAccumulate;
    }

    public void setPrevInterestAccumulate(String prevInterestAccumulate) {
        this.prevInterestAccumulate = prevInterestAccumulate;
    }

    public String getCurrInterestAccumulate() {
        return currInterestAccumulate;
    }

    public void setCurrInterestAccumulate(String currInterestAccumulate) {
        this.currInterestAccumulate = currInterestAccumulate;
    }

    public String getNextInterestAccumulate() {
        return nextInterestAccumulate;
    }

    public void setNextInterestAccumulate(String nextInterestAccumulate) {
        this.nextInterestAccumulate = nextInterestAccumulate;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getCreditTreeId() {
        return creditTreeId;
    }

    public void setCreditTreeId(String creditTreeId) {
        this.creditTreeId = creditTreeId;
    }

    public String getExcessContriServTyp() {
        return excessContriServTyp;
    }

    public void setExcessContriServTyp(String excessContriServTyp) {
        this.excessContriServTyp = excessContriServTyp;
    }

    @Override
    public String toString() {
        return "X5506BO [operationMode=" + operationMode + ", accountCurrency=" + accountCurrency + ", modeName="
                + modeName + ", prevInterestAccumulate=" + prevInterestAccumulate + ", currInterestAccumulate="
                + currInterestAccumulate + ", nextInterestAccumulate=" + nextInterestAccumulate + ", operationDate="
                + operationDate + ", creditTreeId=" + creditTreeId + ", excessContriServTyp=" + excessContriServTyp
                + "]";
    }

}
