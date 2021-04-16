package com.tansun.ider.model.bo;

import java.io.Serializable;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 顺序号
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5527BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 序号类型，CUS：客户号 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String serialType;
    /** 下一顺序号 [10,0] Not NULL */
    @Digits(integer = 10, fraction = 0)
    private Integer nextSerialNo;

    public String getSerialType() {
        return serialType;
    }

    public void setSerialType(String serialType) {
        this.serialType = serialType;
    }

    public Integer getNextSerialNo() {
        return nextSerialNo;
    }

    public void setNextSerialNo(Integer nextSerialNo) {
        this.nextSerialNo = nextSerialNo;
    }

    @Override
    public String toString() {
        return "X5526BO [serialType=" + serialType + ", nextSerialNo=" + nextSerialNo + "]";
    }
}
