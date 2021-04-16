package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5522 汇率
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5522BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 报价货币 [10,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String referCurrency;
    /** 基准货币 [10,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String baseCurrency;
    /** 买入价 [14,9] Not NULL */
    private BigDecimal buyingPrice;
    /** 卖出价 [14,9] Not NULL */
    private BigDecimal sellingPrice;
    /** 汇兑费 [7,5] Not NULL */
    private BigDecimal markUp;
    /** 日期 [19,0] */
    @Past
    private Date rateIssueDate;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getReferCurrency() {
        return referCurrency;
    }

    public void setReferCurrency(String referCurrency) {
        this.referCurrency = referCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getMarkUp() {
        return markUp;
    }

    public void setMarkUp(BigDecimal markUp) {
        this.markUp = markUp;
    }

    public Date getRateIssueDate() {
        return rateIssueDate;
    }

    public void setRateIssueDate(Date rateIssueDate) {
        this.rateIssueDate = rateIssueDate;
    }

    @Override
    public String toString() {
        return "X5522BO [operationMode=" + operationMode + ", referCurrency=" + referCurrency + ", baseCurrency="
                + baseCurrency + ", buyingPrice=" + buyingPrice + ", sellingPrice=" + sellingPrice + ", markUp="
                + markUp + ", rateIssueDate=" + rateIssueDate + "]";
    }

}
