package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * @Desc: 授权欺诈名单
 * @Author kangxuw
 * @Date 2018年5月23日下午3:03:34
 */
public class X8036BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -8510046433408609683L;
	
	//卡BIN
	private Integer cardBin;
	//外部识别号
	private String externalIdentificationNo;
	//欺诈建立日期
	private Date frdCrtTm;
	//欺诈删除日期
	private Date frdDelTm;
	//输入来源
	private String inputSource;
	//授权应答码
	private String authRespCode;
	
	private Date gmtCreate;
	
	private Date gmtModified;
	//版本号
	private Integer version;
	
	
	public Integer getCardBin() {
		return cardBin;
	}


	public void setCardBin(Integer cardBin) {
		this.cardBin = cardBin;
	}


	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}


	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}


	public Date getFrdCrtTm() {
		return frdCrtTm;
	}


	public void setFrdCrtTm(Date frdCrtTm) {
		this.frdCrtTm = frdCrtTm;
	}


	public Date getFrdDelTm() {
		return frdDelTm;
	}


	public void setFrdDelTm(Date frdDelTm) {
		this.frdDelTm = frdDelTm;
	}


	public String getInputSource() {
		return inputSource;
	}


	public void setInputSource(String inputSource) {
		this.inputSource = inputSource;
	}


	public String getAuthRespCode() {
		return authRespCode;
	}


	public void setAuthRespCode(String authRespCode) {
		this.authRespCode = authRespCode;
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
		return "X8036BO [cardBin=" + cardBin + ", externalIdentificationNo=" + externalIdentificationNo + ", frdCrtTm="
				+ frdCrtTm + ", frdDelTm=" + frdDelTm + ", inputSource=" + inputSource + ", authRespCode="
				+ authRespCode + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", version=" + version
				+ "]";
	}
	
	

}
