package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc: X5504活动关联构件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:29
 */
public class X5502VO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    private String id;
    /** 事件编号 : 事件编号 [14,0] Not NULL */
    private String eventNo;
    /** 活动编号 : 活动编号 [5,0] Not NULL */
    private String actNo;
    /** 触发事件编号 : 触发事件编号 [14,0] */
    private String triggerNo;
    /** 触发类型 : 触发类型 G：总控触发 A：由活动触发 [1,0] */
    private String triggerTyp;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    private Date timestamp;
    /** 版本号 : 版本号 [10,0] Not NULL */
    private Integer version;
    /** 执行顺序 [2,0] */
    private Integer performOrder;
    /** 活动描述 */
    private String actDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getTriggerNo() {
        return triggerNo;
    }

    public void setTriggerNo(String triggerNo) {
        this.triggerNo = triggerNo;
    }

    public String getTriggerTyp() {
        return triggerTyp;
    }

    public void setTriggerTyp(String triggerTyp) {
        this.triggerTyp = triggerTyp;
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

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    @Override
    public String toString() {
        return "X5502VO [id=" + id + ", eventNo=" + eventNo + ", actNo=" + actNo + ", triggerNo=" + triggerNo
                + ", triggerTyp=" + triggerTyp + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version="
                + version + ", performOrder=" + performOrder + ", actDesc=" + actDesc + "]";
    }

}
