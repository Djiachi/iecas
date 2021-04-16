package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreArtifact;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5504活动关联构件
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:29
 */
public class X5504BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 活动编号 [5,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String actNo;
    /** 构件编号 [3,0] Not NULL */
    private String artifactNo;
    /** 活动关联的构件 */
    private List<CoreArtifact> list;

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

    public List<CoreArtifact> getList() {
        return list;
    }

    public void setList(List<CoreArtifact> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "X5504BO [actNo=" + actNo + ", artifactNo=" + artifactNo + ", list=" + list + "]";
    }

}
