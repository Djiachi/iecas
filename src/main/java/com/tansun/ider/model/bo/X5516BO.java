package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.model.vo.X5516VO;

/**
 * @Desc: X5516对象PCD实例
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:19
 */
public class X5516BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** P：产品对象 M：媒介对象 A：业务类型 ，对应账户类构件 B：余额对象 O:运营模式 [1,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String objectType;
    /** 对象代码 [9,0] Not NULL */
    private String objectCode;
    /** 只有在对象类型为B的时候才会有值，且为产品对象编码其他情况为空 [9,0] */
    private String objectCode2;
    /** PCD编号 [8,0] Not NULL */
    private String pcdNo;
    /** 币种 [3,0] */
    private String currencyCode;
    /** 取值类型 D：数值 P：百分比 [1,0] Not NULL */
    private String pcdType;
    /** 取值 [10,0] */
    private BigDecimal pcdValue;
    /** 取值小数位 [10,0] */
    private Integer pcdPoint;
    
    /** pcdlist  */
    private List<X5516VO> pcdList;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectCode2() {
        return objectCode2;
    }

    public void setObjectCode2(String objectCode2) {
        this.objectCode2 = objectCode2;
    }

    public String getPcdNo() {
        return pcdNo;
    }

    public void setPcdNo(String pcdNo) {
        this.pcdNo = pcdNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPcdType() {
        return pcdType;
    }

    public void setPcdType(String pcdType) {
        this.pcdType = pcdType;
    }

    

    public BigDecimal getPcdValue() {
		return pcdValue;
	}

	public void setPcdValue(BigDecimal pcdValue) {
		this.pcdValue = pcdValue;
	}

	public Integer getPcdPoint() {
        return pcdPoint;
    }

    public void setPcdPoint(Integer pcdPoint) {
        this.pcdPoint = pcdPoint;
    }

	public List<X5516VO> getPcdList() {
		return pcdList;
	}

	public void setPcdList(List<X5516VO> pcdList) {
		this.pcdList = pcdList;
	}

	@Override
	public String toString() {
		return "X5516BO [operationMode=" + operationMode + ", objectType=" + objectType + ", objectCode=" + objectCode
				+ ", objectCode2=" + objectCode2 + ", pcdNo=" + pcdNo + ", currencyCode=" + currencyCode + ", pcdType="
				+ pcdType + ", pcdValue=" + pcdValue + ", pcdPoint=" + pcdPoint + "]";
	}

   

}
