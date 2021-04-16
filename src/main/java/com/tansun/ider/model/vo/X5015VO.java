package com.tansun.ider.model.vo;

import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;

public class X5015VO {

	/* 媒介单元代码 */
	private String mediaUnitCode;
	/* 媒介对象 */
	private String mediaObjectCode;
	/* 增加外部识别号 */
	private String externalIdentificationNumber;
	/* 媒介单元基本信息 */
	private CoreMediaBasicInfo coreMediaBasicInfo;

	public X5015VO() {
		super();
	}

	public X5015VO(String mediaUnitCode, String mediaObjectCode, String externalIdentificationNumber,
			CoreMediaBasicInfo coreMediaBasicInfo) {
		super();
		this.mediaUnitCode = mediaUnitCode;
		this.mediaObjectCode = mediaObjectCode;
		this.externalIdentificationNumber = externalIdentificationNumber;
		this.coreMediaBasicInfo = coreMediaBasicInfo;
	}

	public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	public String getMediaObjectCode() {
		return mediaObjectCode;
	}

	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}

	public String getExternalIdentificationNumber() {
		return externalIdentificationNumber;
	}

	public void setExternalIdentificationNumber(String externalIdentificationNumber) {
		this.externalIdentificationNumber = externalIdentificationNumber;
	}

	public CoreMediaBasicInfo getCoreMediaBasicInfo() {
		return coreMediaBasicInfo;
	}

	public void setCoreMediaBasicInfo(CoreMediaBasicInfo coreMediaBasicInfo) {
		this.coreMediaBasicInfo = coreMediaBasicInfo;
	}

	@Override
	public String toString() {
		return "X5015VO [mediaUnitCode=" + mediaUnitCode + ", mediaObjectCode=" + mediaObjectCode
				+ ", externalIdentificationNumber=" + externalIdentificationNumber + ", coreMediaBasicInfo="
				+ coreMediaBasicInfo + "]";
	}

}
