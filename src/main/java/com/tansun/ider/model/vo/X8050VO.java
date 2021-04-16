package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 授权正负面清单vo 组合bean
 * 
 * @author kangx
 */
public class X8050VO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 642948974239567134L;
	private String operationMode;
	private String differentCode;
	private String listCode;
	private String listTyp;
	private String listProperties;
//	private List<AuthPositivNegativList> x8048list;

	public String getDifferentCode() {
		return differentCode;
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

	/*public List<AuthPositivNegativList> getX8048list() {
		return x8048list;
	}*/

	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
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

	/*public void setX8048list(List<AuthPositivNegativList> x8048list) {
		this.x8048list = x8048list;
	}*/

	/*@Override
	public String toString() {
		return "X8050VO [operationMode=" + operationMode + ", differentCode=" + differentCode + ", listCode=" + listCode
				+ ", listTyp=" + listTyp + ", listProperties=" + listProperties + ", x8048list=" + x8048list + "]";
	}*/
}
