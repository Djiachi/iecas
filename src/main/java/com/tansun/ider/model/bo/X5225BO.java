package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:客户备注信息建立
 * @Author wt
 * @Date 2018年5月25日 下午4:12:55
 */
public class X5225BO extends BeanVO implements Serializable{

	
	/**主证件类型 */
	private String idType;
	/** 主证件号码 */
	private String idNumber;
	/** 外部识别号 */
	private String externalIdentificationNo;
    /** 创建一个全局流水号 */
    private String globalEventNo;
    /** core_custormer */
    private String customerNo;
    /** 备注信息 */
    private List<CoreCustomerRemarks> coreCustomerRemarkss;
    public String getGlobalEventNo() {
        return globalEventNo;
    }
    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public List<CoreCustomerRemarks> getCoreCustomerRemarkss() {
        return coreCustomerRemarkss;
    }
    public void setCoreCustomerRemarkss(List<CoreCustomerRemarks> coreCustomerRemarkss) {
        this.coreCustomerRemarkss = coreCustomerRemarkss;
    }
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	@Override
	public String toString() {
		return "X5225BO [idType=" + idType + ", idNumber=" + idNumber + ", externalIdentificationNo="
				+ externalIdentificationNo + ", globalEventNo=" + globalEventNo + ", customerNo=" + customerNo
				+ ", coreCustomerRemarkss=" + coreCustomerRemarkss + "]";
	}
    
}
