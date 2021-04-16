package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5592BO extends BeanVO implements Serializable{
	
	private static final long serialVersionUID = -6294800859169407752L;
	
	protected String artifactNo;
    /** 构件描述 : 构件描述 [50,0] */
    protected String artifactDesc;
    /** 构件类型 : 构件类型 A：账户类构件B：余额类构件T：技术类构件P：产品类构件X：非对象类构件M：媒介类构件 [1,0] */
    protected String artifactType;
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
	@Override
	public String toString() {
		return "X5592BO [artifactNo=" + artifactNo + ", artifactDesc=" + artifactDesc + ", artifactType=" + artifactType
				+ "]";
	}
}
