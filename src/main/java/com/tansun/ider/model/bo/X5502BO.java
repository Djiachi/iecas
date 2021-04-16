package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreActivity;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5502事件关联活动
 * @Author lianhuan
 * @Date 2018年4月25日下午3:05:02
 */
public class X5502BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 事件编号 [14,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String eventNo;
    /** 活动编号 [5,0] Not NULL */
    private String actNo;
    /** 触发类型 G：总控触发 A：由活动触发 [1,0] */
    private String triggerTyp;
    /** 触发事件编号 [14,0] */
    private String triggerNo;
    /** 执行顺序 [2,0] */
    private Integer performOrder;

    /** 关联活动 */
    private List<CoreActivity> list;

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

    public String getTriggerTyp() {
        return triggerTyp;
    }

    public void setTriggerTyp(String triggerTyp) {
        this.triggerTyp = triggerTyp;
    }

    public String getTriggerNo() {
        return triggerNo;
    }

    public void setTriggerNo(String triggerNo) {
        this.triggerNo = triggerNo;
    }

    public List<CoreActivity> getList() {
        return list;
    }

    public void setList(List<CoreActivity> list) {
        this.list = list;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    @Override
    public String toString() {
        return "X5502BO [eventNo=" + eventNo + ", actNo=" + actNo + ", triggerTyp=" + triggerTyp + ", triggerNo="
                + triggerNo + ", performOrder=" + performOrder + ", list=" + list + "]";
    }

}
