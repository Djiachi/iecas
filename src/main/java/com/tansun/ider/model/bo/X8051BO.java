package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * 授权场景识别BO
 * @author kangx
 */
public class X8051BO extends BeanVO implements Serializable {
	private static final long serialVersionUID = -3583799819168614621L;
	private String id;
	private String authSceneCode;
	private String messageTyp;
	private String processCode;
	private String posConditionCode;
	private String cardAssociations;
	private String gmtCreate;
	private Date timestamp;
	private Integer version;

	public String getAuthSceneCode() {
		return authSceneCode;
	}

	public String getCardAssociations() {
		return cardAssociations;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public String getId() {
		return id;
	}

	public String getMessageTyp() {
		return messageTyp;
	}

	public String getPosConditionCode() {
		return posConditionCode;
	}

	public String getProcessCode() {
		return processCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}

	public void setCardAssociations(String cardAssociations) {
		this.cardAssociations = cardAssociations;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMessageTyp(String messageTyp) {
		this.messageTyp = messageTyp;
	}

	public void setPosConditionCode(String posConditionCode) {
		this.posConditionCode = posConditionCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8058BO [id=" + id + ", authSceneCode=" + authSceneCode + ", messageTyp=" + messageTyp
				+ ", processCode=" + processCode + ", posConditionCode=" + posConditionCode + ", cardAssociations="
				+ cardAssociations + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version=" + version
				+ "]";
	}
}
