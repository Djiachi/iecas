package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * 授权正负面清单vo列表
 * @author kangx
 *
 */
public class X8049VO extends BeanVO implements Serializable{

	private static final long serialVersionUID = 642948974239567134L;
	private String id;
	private String operationMode;
	private String differentCode;
	private String listCode;
	private String listTyp;
	private String listProperties;
	public String getDifferentCode() {
		return differentCode;
	}
	public String getId() {
		return id;
	}
	public String getListCode() {
		return listCode;
	}
	public String getListProperties() {
		return listProperties;
	}
	public String getListTyp() {
		return listTyp;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setListCode(String listCode) {
		this.listCode = listCode;
	}
	public void setListProperties(String listProperties) {
		this.listProperties = listProperties;
	}
	public void setListTyp(String listTyp) {
		this.listTyp = listTyp;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	@Override
	public String toString() {
		return "X8049VO [id=" + id + ", operationMode=" + operationMode + ", differentCode=" + differentCode
				+ ", listCode=" + listCode + ", listTyp=" + listTyp + ", listProperties=" + listProperties + "]";
	}

}
