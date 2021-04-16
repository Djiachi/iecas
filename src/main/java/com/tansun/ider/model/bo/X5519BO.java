package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreArtifact;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5519业务形态构件
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5519BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 所属业务形态 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String businessPattern;
    /** 构件编号 [3,0] Not NULL */
    //@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String artifactNo;
    /** 关联的构件编号 */
    private List<CoreArtifact> list;

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

    public List<CoreArtifact> getList() {
        return list;
    }

    public void setList(List<CoreArtifact> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "X5519BO [operationMode=" + operationMode + ", businessPattern=" + businessPattern + ", artifactNo="
                + artifactNo + ", list=" + list + "]";
    }

}
