package com.tansun.ider.model.vo;

public class X5325VO {

	/** 有效期 */
	private String expirationDate;
	/** 卡组织 */
	private String cardAssociations;
	/** 卡版代号 */
	private String cardCode;
	/** 持卡人姓名 */
	protected String mediaUserName;
	/** 实时制卡标识 */
	protected String makeCardSigns;

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCardAssociations() {
		return cardAssociations;
	}

	public void setCardAssociations(String cardAssociations) {
		this.cardAssociations = cardAssociations;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getMediaUserName() {
		return mediaUserName;
	}

	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}

	public String getMakeCardSigns() {
		return makeCardSigns;
	}

	public void setMakeCardSigns(String makeCardSigns) {
		this.makeCardSigns = makeCardSigns;
	}

	@Override
	public String toString() {
		return "X5325VO [expirationDate=" + expirationDate + ", cardAssociations=" + cardAssociations + ", cardCode="
				+ cardCode + ", mediaUserName=" + mediaUserName + ", makeCardSigns=" + makeCardSigns + "]";
	}

}
