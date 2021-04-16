package com.tansun.ider.model.vo;

public class X5020VO {

	/** 产品对象代码 [9,0] Not NULL */
	private String productObjectCode;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	/** 主附标识 [1,0] Not NULL */
	private String mainAttachment;
	/** 状态 [1,0] */
	private String state;
	/** 媒介对象代码 [9,0] Not NULL */
	protected String mediaObjectCode;
	/** 是否可转卡 [9,0] Not NULL */
	protected String convertibleCard;

	public X5020VO() {
		super();
	}

	public X5020VO(String productObjectCode, String externalIdentificationNo, String mainAttachment, String state,
			String mediaObjectCode, String convertibleCard) {
		super();
		this.productObjectCode = productObjectCode;
		this.externalIdentificationNo = externalIdentificationNo;
		this.mainAttachment = mainAttachment;
		this.state = state;
		this.mediaObjectCode = mediaObjectCode;
		this.convertibleCard = convertibleCard;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getMainAttachment() {
		return mainAttachment;
	}

	public void setMainAttachment(String mainAttachment) {
		this.mainAttachment = mainAttachment;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMediaObjectCode() {
		return mediaObjectCode;
	}

	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}

	public String getConvertibleCard() {
		return convertibleCard;
	}

	public void setConvertibleCard(String convertibleCard) {
		this.convertibleCard = convertibleCard;
	}

	@Override
	public String toString() {
		return "X5020VO [productObjectCode=" + productObjectCode + ", externalIdentificationNo="
				+ externalIdentificationNo + ", mainAttachment=" + mainAttachment + ", state=" + state
				+ ", mediaObjectCode=" + mediaObjectCode + ", convertibleCard=" + convertibleCard + "]";
	}

}
