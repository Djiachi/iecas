package com.tansun.ider.model.vo;
/**
 * 
 * @Description:TODO()   
 * @author: sunyaoyao
 * @date:   2019年5月22日 上午9:44:59   
 *
 */
public class X4055VO {
	//持卡人姓名
	private String customerName;
	 /** 外部识别号 [32,0] Not NULL */
    protected String externalIdentificationNo;
    /** 持卡人证件类型 [1,0] */
    protected String idType;
    /** 持卡人证件号码 [30,0] */
    protected String idNumber;
    /** 持卡人信息所在片区 [30,0] */
    protected String customerArea;
    //公务卡类型
    protected String officialCardType;
    //invalidFlag有效标识
    protected String invalidFlag;
    private String mediaUnitCode;
    
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMediaUnitCode() {
		return mediaUnitCode;
	}
	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
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
	public String getCustomerArea() {
		return customerArea;
	}
	public void setCustomerArea(String customerArea) {
		this.customerArea = customerArea;
	}
	public String getOfficialCardType() {
		return officialCardType;
	}
	public void setOfficialCardType(String officialCardType) {
		this.officialCardType = officialCardType;
	}
	public String getInvalidFlag() {
		return invalidFlag;
	}
	public void setInvalidFlag(String invalidFlag) {
		this.invalidFlag = invalidFlag;
	}
    
}
