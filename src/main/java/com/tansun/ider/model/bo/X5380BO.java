package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 
 * @Desc: 结息节点查询
 * @Author lianhuan
 * @Date 2018年7月4日下午1:48:35
 */
public class X5380BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -2609422155573070583L;
    /** 余额单元代码 : 余额单元代码 : 余额单元代码 : 余额单元代码 [19,0] Not NULL */
    private String balanceUnitCode;
    /** 发生日期 yyyy-MM-dd : 发生日期 : 发生日期 yyyy-MM-dd : 发生日期 [10,0] Not NULL */
    private String occurrDate;
    /** 节点类别 : 节点类别 WI-免息 BI-结息 [2,0] Not NULL */
    private String nodeTyp;
    /** 节点状态 : 节点状态 0，正常结息 1 余额结息 9重记 [1,0] Not NULL */
    private String nodeStatus;
    /** 利息金额 [18,0] Not NULL */
    private BigDecimal interestAmount;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [11,6] Not NULL */
    private Date timestamp;
    /** 版本号 [10,0] Not NULL */
    private Integer version;

    public String getBalanceUnitCode() {
        return balanceUnitCode;
    }

    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }

    public String getOccurrDate() {
        return occurrDate;
    }

    public void setOccurrDate(String occurrDate) {
        this.occurrDate = occurrDate;
    }

    public String getNodeTyp() {
        return nodeTyp;
    }

    public void setNodeTyp(String nodeTyp) {
        this.nodeTyp = nodeTyp;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "X5380BO [balanceUnitCode=" + balanceUnitCode + ", occurrDate=" + occurrDate + ", nodeTyp=" + nodeTyp
                + ", nodeStatus=" + nodeStatus + ", interestAmount=" + interestAmount + ", gmtCreate=" + gmtCreate
                + ", timestamp=" + timestamp + ", version=" + version + "]";
    }

}
