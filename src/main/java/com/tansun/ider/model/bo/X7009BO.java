package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 未达授权查询
 * @author Administrator
 */
public class X7009BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String id;
    /** 交易处理时间 yyyy-MM-dd HH:mm:ss.SSS */
    private String transProcessTm;
    /** 交易处理交易处理顺序号 */
    private Integer transProcessNumber;
    /** 客户号 */
    private String ecommCustomerNo;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransProcessTm() {
		return transProcessTm;
	}
	public void setTransProcessTm(String transProcessTm) {
		this.transProcessTm = transProcessTm;
	}
	public Integer getTransProcessNumber() {
		return transProcessNumber;
	}
	public void setTransProcessNumber(Integer transProcessNumber) {
		this.transProcessNumber = transProcessNumber;
	}
	public String getEcommCustomerNo() {
		return ecommCustomerNo;
	}
	public void setEcommCustomerNo(String ecommCustomerNo) {
		this.ecommCustomerNo = ecommCustomerNo;
	}
	
	@Override
	public String toString() {
		return "X7009BO [id=" + id + ", transProcessTm=" + transProcessTm + ", transProcessNumber=" + transProcessNumber
				+ ", ecommCustomerNo=" + ecommCustomerNo + "]";
	}
	
}
