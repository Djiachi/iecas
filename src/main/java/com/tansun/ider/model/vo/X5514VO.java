package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5514产品业务范围
 * @Author lianhuan
 * @Date 2014年4月25日下午3:03:34
 */
public class X5514VO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    private String operationMode;
    /** 产品对象代码 [9,0] Not NULL */
    private String productObjectCode;
    /** 业务类型代码 [9,0] Not NULL */
    private String businessTypeCode;
    /** 产品关联的业务类型 */
    private List<CoreBusinessType> list;
    private String productDesc;
    private String businessDesc;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public List<CoreBusinessType> getList() {
        return list;
    }

    public void setList(List<CoreBusinessType> list) {
        this.list = list;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    @Override
    public String toString() {
        return "X5514VO [operationMode=" + operationMode + ", productObjectCode=" + productObjectCode
                + ", businessTypeCode=" + businessTypeCode + ", list=" + list + ", productDesc=" + productDesc
                + ", businessDesc=" + businessDesc + "]";
    }

}
