package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * @Desc: 授权例外名单
 * @Author kangxuw
 * @Date 2018年5月21日下午3:03:34
 */
public class X8033BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -6558007544898798201L;
	//外部识别号
	private String externalIdentificationNo;
	
	private String id;
	//卡组织,V：VISA M：MC C：CUP
	private String cardAssociations;
	//管控原因	
	private String contrlReason;
	//管控建立日期
	private Date contrlCrtTm;
	//管控删除日期
	private Date contrlDelTm;
	
	private Date gmtCreate;

	private Date gmtModified;

	private Integer version;

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardAssociations() {
		return cardAssociations;
	}

	public void setCardAssociations(String cardAssociations) {
		this.cardAssociations = cardAssociations;
	}

	public String getContrlReason() {
		return contrlReason;
	}

	public void setContrlReason(String contrlReason) {
		this.contrlReason = contrlReason;
	}

	public Date getContrlCrtTm() {
		return contrlCrtTm;
	}

	public void setContrlCrtTm(Date contrlCrtTm) {
		this.contrlCrtTm = contrlCrtTm;
	}

	public Date getContrlDelTm() {
		return contrlDelTm;
	}

	public void setContrlDelTm(Date contrlDelTm) {
		this.contrlDelTm = contrlDelTm;
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
		return "X8033BO [externalIdentificationNo=" + externalIdentificationNo + ", id=" + id + ", cardAssociations="
				+ cardAssociations + ", contrlReason=" + contrlReason + ", contrlCrtTm=" + contrlCrtTm
				+ ", contrlDelTm=" + contrlDelTm + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified
				+ ", version=" + version + "]";
	}

}
