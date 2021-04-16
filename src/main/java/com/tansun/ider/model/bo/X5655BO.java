package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 生成制卡记录
 * @author qianyp
 */
public class X5655BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -7559868727100840157L;
	
	 /** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
    /** 有效期 MMYY [4,0] */
	private String expirationDate;
	 /** 卡版的样式代码 [2,0] */
	private String formatCode;
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getFormatCode() {
		return formatCode;
	}
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
}
