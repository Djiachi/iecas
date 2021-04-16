package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5507构件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:17
 */
public class X5507BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 构件编号 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
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

    @Override
    public String toString() {
        return "X5507BO [artifactNo=" + artifactNo + ", artifactDesc=" + artifactDesc + ", artifactType=" + artifactType
                + ", nonObjInstanDimen=" + nonObjInstanDimen + "]";
    }

}
