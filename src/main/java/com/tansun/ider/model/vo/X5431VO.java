package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.model.bo.X0135BO;

public class X5431VO extends BeanVO implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -6160321391053327L;
    /** 运行模式 */
    private String operationMode;
    /** 定价对象 [3,0] */
    private String pricingObject;
    /** 定价对象 编号，PCD编号/收费项目编号 [8,0] Not NULL */
    private String pricingObjectCode;
    /** 存在PCD的构件实例化LIST */
    private List<X0075VO> artifactList;
    /** 收费项目实例化LIST */
    private List<X0135BO> feeItemInstanList;
    /** 客户定价标签LIST */
    private List<X5430VO> customerPriceTagList;
    /** 定价对象描述*/
    private String pricingObjectDesc;
    /** 实例代码list*/
    private List<String> pricingLevelCodeList;
    
    public String getOperationMode() {
        return operationMode;
    }
    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }
    public List<String> getPricingLevelCodeList() {
        return pricingLevelCodeList;
    }
    public void setPricingLevelCodeList(List<String> pricingLevelCodeList) {
        this.pricingLevelCodeList = pricingLevelCodeList;
    }
    public List<X5430VO> getCustomerPriceTagList() {
        return customerPriceTagList;
    }
    public void setCustomerPriceTagList(List<X5430VO> customerPriceTagList) {
        this.customerPriceTagList = customerPriceTagList;
    }
    public String getPricingObjectDesc() {
        return pricingObjectDesc;
    }
    public void setPricingObjectDesc(String pricingObjectDesc) {
        this.pricingObjectDesc = pricingObjectDesc;
    }
    public String getPricingObject() {
        return pricingObject;
    }
    public void setPricingObject(String pricingObject) {
        this.pricingObject = pricingObject;
    }
    public String getPricingObjectCode() {
        return pricingObjectCode;
    }
    public void setPricingObjectCode(String pricingObjectCode) {
        this.pricingObjectCode = pricingObjectCode;
    }
    public List<X0075VO> getArtifactList() {
        return artifactList;
    }
    public void setArtifactList(List<X0075VO> artifactList) {
        this.artifactList = artifactList;
    }
    public List<X0135BO> getFeeItemInstanList() {
        return feeItemInstanList;
    }
    public void setFeeItemInstanList(List<X0135BO> feeItemInstanList) {
        this.feeItemInstanList = feeItemInstanList;
    }
    @Override
    public String toString() {
        return "X5431VO [operationMode=" + operationMode + ", pricingObject=" + pricingObject + ", pricingObjectCode="
                + pricingObjectCode + ", artifactList=" + artifactList + ", feeItemInstanList=" + feeItemInstanList
                + ", customerPriceTagList=" + customerPriceTagList + ", pricingObjectDesc=" + pricingObjectDesc
                + ", pricingLevelCodeList=" + pricingLevelCodeList + "]";
    }
}
