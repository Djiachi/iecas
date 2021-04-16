package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5580VO extends BeanVO implements Serializable{
	
	/** id */
	private String id;
//	/** 事件编号 [14,0] NOT NULL */
//	private String eventNo;
//	/** 活动编号[8,0] NOT NULL */
//	private String activityNo;
//	/** 操作员Id */
//	private String operatorId;
//	/** 证件号 [30,0] NOT NULL */
//	private String idNumber;
//	/** 外部识别号 [19,0] NOT NULL */
//	private String externalIdentificationNo;
	/** 客户代码 [12,0] NOT NULL KEY */
	private String customerNo;
	/** 元件编号 [10,0] NOT NULL */
	private String elementNo;
	/** 元件描述 [10,0] NOT NULL */
	private String elementDesc;
	/** 创建日期 [10,0] NOT NULL */
	// 文档中写的是8位，日期格式应为10位：YYYY-MM-DD
	private String gmtCreate;
	/** 生效日期 [10,0] NOT NULL */
	private String effectDate;
	/** 失效日期 [10,0] NOT NULL */
//	// 时间戳和版本号文档中没有
	private String uneffectDate;
//	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
//	private Date timestamp;
//	/** 版本号 [10,0] */
//	private Integer version;
	private String  setDate;
    public String getSetDate() {
        return setDate;
    }
    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }
    public String getElementNo() {
		return elementNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public void setElementNo(String elementNo) {
		this.elementNo = elementNo;
	}
	public String getElementDesc() {
		return elementDesc;
	}
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}
	public String getUneffectDate() {
		return uneffectDate;
	}
	public void setUneffectDate(String uneffectDate) {
		this.uneffectDate = uneffectDate;
	}
}
