package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5514产品业务范围
 * @Author lianhuan
 * @Date 2014年4月25日下午3:03:34
 */
public class X5514BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 产品对象代码 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String productObjectCode;
    /** 业务类型代码 [9,0] Not NULL */
//    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String businessTypeCode;
    /** 产品关联的业务类型 */
    private List<CoreBusinessType> list;

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

    @Override
    public String toString() {
        return "X5514BO [operationMode=" + operationMode + ", productObjectCode=" + productObjectCode
                + ", businessTypeCode=" + businessTypeCode + ", list=" + list + "]";
    }

}
