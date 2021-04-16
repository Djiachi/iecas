package com.tansun.ider.model.bo;

import java.math.BigDecimal;

/**
 * <p> Title: X4040BO </p>
 * <p> Description: 个人公务卡最大额度查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public class X4045BO {
    /** 证件类型 [1,0] */
    protected String idType;
	/** 证件号码 [30,0] */
    protected String idNumber;
    /** 外部识别号 [32,0] Not NULL */
    protected String externalIdentificationNo;
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
    
}
