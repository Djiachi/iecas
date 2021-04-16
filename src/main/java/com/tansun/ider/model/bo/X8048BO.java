package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 授权正面清单
 * @Author kangxuw
 * @Date 2018年5月24日下午3:03:34
 */
public class X8048BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 5240297614573763504L;
	private String id;
	private String operationMode;
	private String differentCode;
	private String listCode;
	private String listTyp;
	private String listProperties;
	private Integer listSerialNumbr;
	private String listProject1;
	private String listProject2;
	private String listProject3;
	private String listProject4;
	private String listProject5;
	private String listProject6;
	private String listProject7;
	private String listProject8;
	private String listProject9;
	private String listProject10;
	private Date gmtCreate;
	private Date gmtModified;
	private Integer version;

	public String getDifferentCode() {
		return differentCode;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public String getId() {
		return id;
	}

	public String getListCode() {
		return listCode;
	}

	public String getListProject1() {
		return listProject1;
	}

	public String getListProject10() {
		return listProject10;
	}

	public String getListProject2() {
		return listProject2;
	}

	public String getListProject3() {
		return listProject3;
	}

	public String getListProject4() {
		return listProject4;
	}

	public String getListProject5() {
		return listProject5;
	}

	public String getListProject6() {
		return listProject6;
	}

	public String getListProject7() {
		return listProject7;
	}

	public String getListProject8() {
		return listProject8;
	}

	public String getListProject9() {
		return listProject9;
	}

	public String getListProperties() {
		return listProperties;
	}

	public Integer getListSerialNumbr() {
		return listSerialNumbr;
	}

	public String getListTyp() {
		return listTyp;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public Integer getVersion() {
		return version;
	}

	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	public void setListProject1(String listProject1) {
		this.listProject1 = listProject1;
	}

	public void setListProject10(String listProject10) {
		this.listProject10 = listProject10;
	}

	public void setListProject2(String listProject2) {
		this.listProject2 = listProject2;
	}

	public void setListProject3(String listProject3) {
		this.listProject3 = listProject3;
	}

	public void setListProject4(String listProject4) {
		this.listProject4 = listProject4;
	}

	public void setListProject5(String listProject5) {
		this.listProject5 = listProject5;
	}

	public void setListProject6(String listProject6) {
		this.listProject6 = listProject6;
	}

	public void setListProject7(String listProject7) {
		this.listProject7 = listProject7;
	}

	public void setListProject8(String listProject8) {
		this.listProject8 = listProject8;
	}

	public void setListProject9(String listProject9) {
		this.listProject9 = listProject9;
	}

	public void setListProperties(String listProperties) {
		this.listProperties = listProperties;
	}

	public void setListSerialNumbr(Integer listSerialNumbr) {
		this.listSerialNumbr = listSerialNumbr;
	}

	public void setListTyp(String listTyp) {
		this.listTyp = listTyp;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8048BO [id=" + id + ", operationMode=" + operationMode + ", differentCode=" + differentCode
				+ ", listCode=" + listCode + ", listTyp=" + listTyp + ", listProperties=" + listProperties
				+ ", listSerialNumbr=" + listSerialNumbr + ", listProject1=" + listProject1 + ", listProject2="
				+ listProject2 + ", listProject3=" + listProject3 + ", listProject4=" + listProject4 + ", listProject5="
				+ listProject5 + ", listProject6=" + listProject6 + ", listProject7=" + listProject7 + ", listProject8="
				+ listProject8 + ", listProject9=" + listProject9 + ", listProject10=" + listProject10 + ", gmtCreate="
				+ gmtCreate + ", gmtModified=" + gmtModified + ", version=" + version + "]";
	}

}
