package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.dao.beta.entity.CoreArtifact;

public class X5567VO implements Serializable{
	
	private static final long serialVersionUID = 1504556546609234210L;

	private String elementNo;
	
	private String elementDesc;
	
	private CoreArtifact coreArtifact ;
	 /** 构件编号 [3,0] Not NULL */
    private String artifactNo;
    /** 构件描述 [50,0] */
    private String artifactDesc;
    /** 构件类型 A：账户类构件B：余额类构件T：技术类构件P：产品类构件X：跨形态构件M：媒介类构件 [1,0] */
    private String artifactType;
    /** D：核算状态，E：事件 只对构件类型为‘X’有效 [1,0] */
    private String nonObjInstanDimen;

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

    public String getNonObjInstanDimen() {
        return nonObjInstanDimen;
    }

    public void setNonObjInstanDimen(String nonObjInstanDimen) {
        this.nonObjInstanDimen = nonObjInstanDimen;
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

	public CoreArtifact getCoreArtifact() {
		return coreArtifact;
	}

	public void setCoreArtifact(CoreArtifact coreArtifact) {
		this.coreArtifact = coreArtifact;
	}
	
	
	
	
}
