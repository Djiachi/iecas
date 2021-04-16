package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5517媒介对象
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:19
 */
public class X5517BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 媒介对象代码 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String mediaObjectCode;
    /** 媒介对象描述 [9,0] Not NULL */
    private String mediaObjectMsg;
    /** 媒介类型:R实体卡,V虚拟卡 [1,0] */
    private String mediaObjectType;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getMediaObjectCode() {
        return mediaObjectCode;
    }

    public void setMediaObjectCode(String mediaObjectCode) {
        this.mediaObjectCode = mediaObjectCode;
    }

    public String getMediaObjectMsg() {
        return mediaObjectMsg;
    }

    public void setMediaObjectMsg(String mediaObjectMsg) {
        this.mediaObjectMsg = mediaObjectMsg;
    }

    public String getMediaObjectType() {
        return mediaObjectType;
    }

    public void setMediaObjectType(String mediaObjectType) {
        this.mediaObjectType = mediaObjectType;
    }

    @Override
    public String toString() {
        return "X5517BO [operationMode=" + operationMode + ", mediaObjectCode=" + mediaObjectCode + ", mediaObjectMsg="
                + mediaObjectMsg + ", mediaObjectType=" + mediaObjectType + "]";
    }

}
