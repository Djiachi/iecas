package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreElement;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:X5508构件基础元件
 * @Author lianhuan
 * @Date 2018年4月24日下午4:16:00
 */
public class X5508BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035069993911420241L;
    /** 构件编号 [15,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String artifactNo;
    /** 元件编号 [15,0] Not NULL */
    private String elementNo;
    /** 执行顺序 [3,0] */
    private Integer performOrder;
    /** 构件关联的元件 */
    private List<CoreElement> list;

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

    public List<CoreElement> getList() {
        return list;
    }

    public void setList(List<CoreElement> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "X5508BO [artifactNo=" + artifactNo + ", elementNo=" + elementNo + ", performOrder=" + performOrder
                + ", list=" + list + "]";
    }

}
