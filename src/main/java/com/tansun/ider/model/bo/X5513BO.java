package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 产品对象
 * @Author lianhuan
 * @Date 2018年4月25日下午3:03:49
 */
public class X5513BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    private String operationMode;
    /** 产品对象代码 [9,0] Not NULL */
    private String productObjectCode;
    /** 产品描述 [50,0] */
    private String productDesc;
    /** 发行卡组织 [1,0] */
    private String issueCardOrgan;
    /** 卡BIN [6,0] */
    private Integer cardBin;
    /** 还款优先级，数值越小优先级越高 [3,0] */
    private Integer repaymentPriority;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getIssueCardOrgan() {
        return issueCardOrgan;
    }

    public void setIssueCardOrgan(String issueCardOrgan) {
        this.issueCardOrgan = issueCardOrgan;
    }

    public Integer getCardBin() {
        return cardBin;
    }

    public void setCardBin(Integer cardBin) {
        this.cardBin = cardBin;
    }

    public Integer getRepaymentPriority() {
        return repaymentPriority;
    }

    public void setRepaymentPriority(Integer repaymentPriority) {
        this.repaymentPriority = repaymentPriority;
    }

    @Override
    public String toString() {
        return "X5513BO [operationMode=" + operationMode + ", productObjectCode=" + productObjectCode + ", productDesc="
                + productDesc + ", issueCardOrgan=" + issueCardOrgan + ", cardBin=" + cardBin + ", repaymentPriority="
                + repaymentPriority + "]";
    }

}
