package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5519VO extends BeanVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5384077450816799237L;

	  /** 运营模式 : 运营模式 [3,0] Not NULL */
    protected String operationMode;
    /** 所属业务形态 : 所属业务形态 [9,0] Not NULL */
    protected String businessPattern;
    /** 构件编号 : 构件编号 [3,0] Not NULL */
    protected String artifactNo;
    /** 构件编号 : 构件描述 [3,0] Not NULL */
    protected String artifactDesc;
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getBusinessPattern() {
		return businessPattern;
	}
	public void setBusinessPattern(String businessPattern) {
		this.businessPattern = businessPattern;
	}
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
	@Override
	public String toString() {
		return "X5519VO [operationMode=" + operationMode + ", businessPattern=" + businessPattern + ", artifactNo="
				+ artifactNo + ", artifactDesc=" + artifactDesc + "]";
	}
    
	
}
