package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;


/**
 * @Desc: 发卡例外
 * @Author kangxuw
 * @Date 2018年5月21日下午3:03:34
 */
public class X8031BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -5904792180269424559L;
	//客户号
	private String customerNo;
	//例外描述
	private String excptDesc;
	
	private String id;
	//外部识别号
	private String externalIdentificationNo;
	//产品对象代码
	private String productObjectCode;
	//交易识别代码 R001 消费类  C001 取现类 P00
	private String transIdentifiNo;
	//入账币种
	private String currencyCode;
	//授权回应
	private String authResp;

	private Date gmtCreate;

	private Date gmtModified;

	private Integer version;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExcptDesc() {
		return excptDesc;
	}

	public void setExcptDesc(String excptDesc) {
		this.excptDesc = excptDesc;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAuthResp() {
		return authResp;
	}

	public void setAuthResp(String authResp) {
		this.authResp = authResp;
	}



	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}



	@Override
	public String toString() {
		return "X8031BO [customerNo=" + customerNo + ", excptDesc=" + excptDesc + ", id=" + id
				+ ", externalIdentificationNo=" + externalIdentificationNo + ", productObjectCode=" + productObjectCode
				+ ", transIdentifiNo=" + transIdentifiNo + ", currencyCode=" + currencyCode + ", authResp=" + authResp
				+ ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", version=" + version + "]";
	}

}
