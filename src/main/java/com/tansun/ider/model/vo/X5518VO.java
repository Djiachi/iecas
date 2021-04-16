package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * 授权正负面清单vo 组合bean
 * @author kangx
 */
public class X5518VO extends BeanVO implements Serializable{

	private static final long serialVersionUID = 642948974239567134L;
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "X8815VO [id=" + id + "]";
	}

}
