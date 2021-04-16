package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5518业务形态
 * @Author lianhuan
 * @Date 2018年4月25日下午3:03:34
 */
public class X5518BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 业务形态编号 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String businessPattern;
    /** 账户组织形式 R：循环 T：交易 [1,0] */
    private String accountOrganizeTyp;
    /** 账户类型 D：借 C ： 贷 [255,0] */
    private String accountBookKeepingDirec;
    /** 描述 [64,0] Not NULL */
    private String description;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getBusinessPattern() {
        return businessPattern;
    }

    public void setBusinessPattern(String businessPattern) {
        this.businessPattern = businessPattern;
    }

    public String getAccountOrganizeTyp() {
        return accountOrganizeTyp;
    }

    public void setAccountOrganizeTyp(String accountOrganizeTyp) {
        this.accountOrganizeTyp = accountOrganizeTyp;
    }

    public String getAccountBookKeepingDirec() {
        return accountBookKeepingDirec;
    }

    public void setAccountBookKeepingDirec(String accountBookKeepingDirec) {
        this.accountBookKeepingDirec = accountBookKeepingDirec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "X5518BO [operationMode=" + operationMode + ", businessPattern=" + businessPattern
                + ", accountOrganizeTyp=" + accountOrganizeTyp + ", accountBookKeepingDirec=" + accountBookKeepingDirec
                + ", description=" + description + "]";
    }

}
