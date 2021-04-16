package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5503活动
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:48
 */
public class X5503BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** [5,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String actNo;
    /** [50,0] */
    private String actDesc;
    /** 生命周期节点 [4,0] */
    private String lifecycleNode;
    /** 日志类型 [1,0] */
    private String logType;
    /** 日志层级 [1,0] */
    private String logLevel;

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getLifecycleNode() {
        return lifecycleNode;
    }

    public void setLifecycleNode(String lifecycleNode) {
        this.lifecycleNode = lifecycleNode;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public String toString() {
        return "X5503BO [actNo=" + actNo + ", actDesc=" + actDesc + ", lifecycleNode=" + lifecycleNode + ", logType="
                + logType + ", logLevel=" + logLevel + "]";
    }

}
