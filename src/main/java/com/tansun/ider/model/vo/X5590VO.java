package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5590VO extends BeanVO implements Serializable{

	private static final long serialVersionUID = 3111969411086446657L;
	/**
	 * 构建编号
	 */
	private String artifactNo ;
	/**
	 * 构建描述
	 */
	private String artifactDesc ;
	/**
	 * 构建类型
	 */
	private String artifactType ;
	/**
	 * 实例化元件编号
	 */
	private String elementNo ;
	/**
	 * 元件描述
	 */
	private String elementDesc ;
	/**
	 * pcd编号
	 */
	private String pcdNo ;
	/**
	 * 币种
	 */
	private String currencyCode ;
	/**
	 * 取值类型
	 */
	private String pcdType ;
	/**
	 *  取值
	 */
	private BigDecimal pcdValue ;
	/**
	 * 取值小数位
	 */
	private Integer pcdPoint ;
	/**
	 * 运营模式
	 */
	private String	operationMode;
	/**
	 * 对象类型
	 */
	private String	objectType;
	/**
	 * 对象代码
	 */
	private String	objectCode;
	/**
	 * 对象代码2
	 */
	private String	objectCode2;
	
	public String getArtifactNo() {
		return artifactNo;
	}
	public void setArtifactNo(String artifactNo) {
		this.artifactNo = artifactNo;
	}
	public String getArtifactDesc() {
		return artifactDesc;
	}
	public void setArtifactDesc(String artifactDesc) {
		this.artifactDesc = artifactDesc;
	}
	public String getArtifactType() {
		return artifactType;
	}
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	public String getElementNo() {
		return elementNo;
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
	public String getPcdNo() {
		return pcdNo;
	}
	public void setPcdNo(String pcdNo) {
		this.pcdNo = pcdNo;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getPcdType() {
		return pcdType;
	}
	public void setPcdType(String pcdType) {
		this.pcdType = pcdType;
	}
	
	public BigDecimal getPcdValue() {
		return pcdValue;
	}
	public void setPcdValue(BigDecimal pcdValue) {
		this.pcdValue = pcdValue;
	}
	public Integer getPcdPoint() {
		return pcdPoint;
	}
	public void setPcdPoint(Integer pcdPoint) {
		this.pcdPoint = pcdPoint;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
	public String getObjectCode2() {
		return objectCode2;
	}
	public void setObjectCode2(String objectCode2) {
		this.objectCode2 = objectCode2;
	}
	@Override
	public String toString() {
		return "X5280VO [artifactNo=" + artifactNo + ", artifactDesc=" + artifactDesc + ", artifactType=" + artifactType
				+ ", elementNo=" + elementNo + ", elementDesc=" + elementDesc + ", pcdNo=" + pcdNo + ", currencyCode="
				+ currencyCode + ", pcdType=" + pcdType + ", pcdValue=" + pcdValue + ", pcdPoint=" + pcdPoint
				+ ", operationMode=" + operationMode + ", objectType=" + objectType + ", objectCode=" + objectCode
				+ ", objectCode2=" + objectCode2 + "]";
	}
	
	
	

}
