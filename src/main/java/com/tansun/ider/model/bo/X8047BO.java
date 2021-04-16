package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 差异管控场景表
 * @Author kangxuw
 * @Date 2018年5月23日下午3:03:34
 */
public class X8047BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 6166566459528052349L;
	private String id;
	private String operationMode;
	private String contrlSceneCode;
	private String differentCode;
	private String pnListCheckFlag;
	private String listCode;
	private String transLimitLevel;
	private String transLimitCode;
	private String sceneDesc;
	private Date gmtCreate;
	private Date gmtModified;
	private Integer version;

	public String getContrlSceneCode() {
		return contrlSceneCode;
	}

	public String getDifferentCode() {
		return differentCode;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public String getId() {
		return id;
	}

	public String getListCode() {
		return listCode;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public String getPnListCheckFlag() {
		return pnListCheckFlag;
	}

	public String getSceneDesc() {
		return sceneDesc;
	}

	public String getTransLimitCode() {
		return transLimitCode;
	}

	public String getTransLimitLevel() {
		return transLimitLevel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setContrlSceneCode(String contrlSceneCode) {
		this.contrlSceneCode = contrlSceneCode;
	}

	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public void setPnListCheckFlag(String pnListCheckFlag) {
		this.pnListCheckFlag = pnListCheckFlag;
	}

	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
	}

	public void setTransLimitCode(String transLimitCode) {
		this.transLimitCode = transLimitCode;
	}

	public void setTransLimitLevel(String transLimitLevel) {
		this.transLimitLevel = transLimitLevel;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8047BO [id=" + id + ", operationMode=" + operationMode + ", contrlSceneCode=" + contrlSceneCode
				+ ", differentCode=" + differentCode + ", pnListCheckFlag=" + pnListCheckFlag + ", listCode=" + listCode
				+ ", transLimitLevel=" + transLimitLevel + ", transLimitCode=" + transLimitCode + ", sceneDesc="
				+ sceneDesc + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", version=" + version
				+ "]";
	}
}
