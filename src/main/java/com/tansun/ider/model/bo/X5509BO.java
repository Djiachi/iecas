package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5509元件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:42
 */
public class X5509BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035069993911420241L;
    /** 元件编号 [15,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String elementNo;
    /** 元件描述 [50,0] Not NULL */
    private String elementDesc;
    /** 构件编号 [15,0] Not NULL */
    private String artifactNo;
    /** 是否是查询可选元件 */
    private String isSelectFlag;

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

    public String getArtifactNo() {
        return artifactNo;
    }

    public void setArtifactNo(String artifactNo) {
        this.artifactNo = artifactNo;
    }

    public String getIsSelectFlag() {
        return isSelectFlag;
    }

    public void setIsSelectFlag(String isSelectFlag) {
        this.isSelectFlag = isSelectFlag;
    }

    @Override
    public String toString() {
        return "X5509BO [elementNo=" + elementNo + ", elementDesc=" + elementDesc + ", artifactNo=" + artifactNo
                + ", isSelectFlag=" + isSelectFlag + "]";
    }

}
