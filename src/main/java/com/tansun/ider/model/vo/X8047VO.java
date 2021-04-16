package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X8047VO  extends BeanVO implements Serializable {

	private static final long serialVersionUID = -722060279222038885L;
	//管控场景id
	private String id;
	//运营模式
	private String operationMode;
	//管控场景代码
	private String contrlSceneCode;
	//授权场景编码
	private String authSceneCode;
	//差异化代码
	private String differentCode;
	public String getAuthSceneCode() {
		return authSceneCode;
	}
	public String getContrlSceneCode() {
		return contrlSceneCode;
	}
	public String getDifferentCode() {
		return differentCode;
	}
	public String getId() {
		return id;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}
	public void setContrlSceneCode(String contrlSceneCode) {
		this.contrlSceneCode = contrlSceneCode;
	}
	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	@Override
	public String toString() {
		return "X8047VO [id=" + id + ", operationMode=" + operationMode + ", contrlSceneCode=" + contrlSceneCode
				+ ", authSceneCode=" + authSceneCode + ", differentCode=" + differentCode + "]";
	}
}
