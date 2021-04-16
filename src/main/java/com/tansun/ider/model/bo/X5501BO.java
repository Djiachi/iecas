package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5501事件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:31
 */
public class X5501BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 事件编号 [14,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String eventNo;
    /** 事件描述 事件描述 [50,0] */
    private String eventDesc;
    /** 事件记账方向 [1,0] */
    private String eventBookKeepingDirec;
    /** 对应余额对象 [9,0] */
    private String eventBalanceObject;
    /** 交易识别代码 R001 消费类 C001 取现类 P001 还款类 [32,0] Not NULL */
    private String transIdentifiNo;
    /** 还款类型L：指定币种还款 B：本币购汇还款 S：指定账户还款 [1,0] */
    private String repaymentType;
    private String type;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventBookKeepingDirec() {
        return eventBookKeepingDirec;
    }

    public void setEventBookKeepingDirec(String eventBookKeepingDirec) {
        this.eventBookKeepingDirec = eventBookKeepingDirec;
    }

    public String getEventBalanceObject() {
        return eventBalanceObject;
    }

    public void setEventBalanceObject(String eventBalanceObject) {
        this.eventBalanceObject = eventBalanceObject;
    }

    public String getTransIdentifiNo() {
        return transIdentifiNo;
    }

    public void setTransIdentifiNo(String transIdentifiNo) {
        this.transIdentifiNo = transIdentifiNo;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "X5501BO [eventNo=" + eventNo + ", eventDesc=" + eventDesc + ", eventBookKeepingDirec="
                + eventBookKeepingDirec + ", eventBalanceObject=" + eventBalanceObject + ", transIdentifiNo="
                + transIdentifiNo + ", repaymentType=" + repaymentType + ", type=" + type + "]";
    }

    

}
