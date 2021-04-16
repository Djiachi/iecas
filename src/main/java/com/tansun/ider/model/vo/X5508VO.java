package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc:X5508构件基础元件
 * @Author lianhuan
 * @Date 2018年4月24日下午4:16:00
 */
public class X5508VO implements Serializable {

    private static final long serialVersionUID = 5035069993911420241L;
    private String id;
    /** 构件编号 [15,0] Not NULL */
    protected String artifactNo;
    /** 构件描述 [15,0] Not NULL */
    protected String artifactDesc;
    /** 元件编号 [15,0] Not NULL */
    protected String elementNo;
    /** 执行顺序 [10,0] */
    protected Integer performOrder;
    /** [19,0] */
    protected Date gmtCreate;
    /** [19,0] */
    protected Date gmtModified;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    private String elementDesc;
    private String pcdNo;
    private String pcdMsg;

    public String getElementDesc() {
        return elementDesc;
    }

    public void setElementDesc(String elementDesc) {
        this.elementDesc = elementDesc;
    }

    public String getArtifactNo() {
        return artifactNo;
    }

    public void setArtifactNo(String artifactNo) {
        this.artifactNo = artifactNo;
    }

    public String getElementNo() {
        return elementNo;
    }

    public void setElementNo(String elementNo) {
        this.elementNo = elementNo;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPcdNo() {
        return pcdNo;
    }

    public void setPcdNo(String pcdNo) {
        this.pcdNo = pcdNo;
    }

    public String getPcdMsg() {
        return pcdMsg;
    }

    public void setPcdMsg(String pcdMsg) {
        this.pcdMsg = pcdMsg;
    }

    public String getArtifactDesc() {
		return artifactDesc;
	}

	public void setArtifactDesc(String artifactDesc) {
		this.artifactDesc = artifactDesc;
	}

	@Override
    public String toString() {
        return "X5508VO [id=" + id + ", artifactNo=" + artifactNo + ", elementNo=" + elementNo + ", performOrder="
                + performOrder + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", version=" + version
                + ", elementDesc=" + elementDesc + ", pcdNo=" + pcdNo + ", pcdMsg=" + pcdMsg + "]";
    }

}
