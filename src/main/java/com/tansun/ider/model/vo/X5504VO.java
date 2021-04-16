package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc: X5504活动关联构件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:29
 */
public class X5504VO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    private String id;
    /** 活动编号 [5,0] Not NULL */
    private String actNo;
    /** 构件编号 [3,0] Not NULL */
    private String artifactNo;
    /** [19,0] */
    private Date gmtCreate;
    /** [19,0] */
    private Date gmtModified;
    /** 版本号 [10,0] Not NULL */
    private Integer version;
    private String artifactDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getArtifactNo() {
        return artifactNo;
    }

    public void setArtifactNo(String artifactNo) {
        this.artifactNo = artifactNo;
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

    public String getArtifactDesc() {
        return artifactDesc;
    }

    public void setArtifactDesc(String artifactDesc) {
        this.artifactDesc = artifactDesc;
    }

    @Override
    public String toString() {
        return "X5504VO [id=" + id + ", actNo=" + actNo + ", artifactNo=" + artifactNo + ", gmtCreate=" + gmtCreate
                + ", gmtModified=" + gmtModified + ", version=" + version + ", artifactDesc=" + artifactDesc + "]";
    }

}
