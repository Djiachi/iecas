package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5521余额对象
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5521BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 余额对象代码 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String balanceObjectCode;
    /** 描述 [50,0] Not NULL */
    private String objectDesc;
    /** 取值类型，P-本金 I-利息 F-费用 [1,0] Not NULL */
    private String objectType;
    /** 利息入账余额对象 [9,0] */
    private String interestBilledBalanceObject;
    /** 还款优先级 [10,0] */
    private Integer duePriority;
    /** 开始日期 [19,0] */
    protected Date beginDate;
    /** 结束日期 [19,0] */
    protected Date endDate;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getBalanceObjectCode() {
        return balanceObjectCode;
    }

    public void setBalanceObjectCode(String balanceObjectCode) {
        this.balanceObjectCode = balanceObjectCode;
    }

    public String getObjectDesc() {
        return objectDesc;
    }

    public void setObjectDesc(String objectDesc) {
        this.objectDesc = objectDesc;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getInterestBilledBalanceObject() {
        return interestBilledBalanceObject;
    }

    public void setInterestBilledBalanceObject(String interestBilledBalanceObject) {
        this.interestBilledBalanceObject = interestBilledBalanceObject;
    }

    public Integer getDuePriority() {
        return duePriority;
    }

    public void setDuePriority(Integer duePriority) {
        this.duePriority = duePriority;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "X5521BO [operationMode=" + operationMode + ", balanceObjectCode=" + balanceObjectCode + ", objectDesc="
                + objectDesc + ", objectType=" + objectType + ", interestBilledBalanceObject="
                + interestBilledBalanceObject + ", duePriority=" + duePriority + ", beginDate=" + beginDate
                + ", endDate=" + endDate + "]";
    }
}
