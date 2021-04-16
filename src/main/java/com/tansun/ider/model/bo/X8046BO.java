package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 管控场景识别表
 * @Author kangxuw
 * @Date 2018年5月23日下午3:03:34
 */
public class X8046BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -7346387936783174044L;
	private String id;
	private String operationMode;
	private String contrlSceneCode;
	private String authSceneCode;
	private String transLocation;
	private Integer mccFrom1;
	private Integer mccTo1;
	private Integer mccFrom2;
	private Integer mccTo2;
	private Integer mccFrom3;
	private Integer mccTo3;
	private Integer mccFrom4;
	private Integer mccTo4;
	private Integer mccFrom5;
	private Integer mccTo5;
	private Integer mccFrom6;
	private Integer mccTo6;
	private Integer mccFrom7;
	private Integer mccTo7;
	private Integer mccFrom8;
	private Integer mccTo8;
	private Integer mccFrom9;
	private Integer mccTo9;
	private Integer mccFrom10;
	private Integer mccTo10;
	private Integer mccFrom11;
	private Integer mccTo11;
	private Integer mccFrom12;
	private Integer mccTo12;
	private String transMode1;
	private String transMode2;
	private String transMode3;
	private String transMode4;
	private String transMode5;
	private Date gmtCreate;
	private Date gmtModified;
	private Integer version;

	public String getAuthSceneCode() {
		return authSceneCode;
	}

	public String getContrlSceneCode() {
		return contrlSceneCode;
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

	public Integer getMccFrom1() {
		return mccFrom1;
	}

	public Integer getMccFrom10() {
		return mccFrom10;
	}

	public Integer getMccFrom11() {
		return mccFrom11;
	}

	public Integer getMccFrom12() {
		return mccFrom12;
	}

	public Integer getMccFrom2() {
		return mccFrom2;
	}

	public Integer getMccFrom3() {
		return mccFrom3;
	}

	public Integer getMccFrom4() {
		return mccFrom4;
	}

	public Integer getMccFrom5() {
		return mccFrom5;
	}

	public Integer getMccFrom6() {
		return mccFrom6;
	}

	public Integer getMccFrom7() {
		return mccFrom7;
	}

	public Integer getMccFrom8() {
		return mccFrom8;
	}

	public Integer getMccFrom9() {
		return mccFrom9;
	}

	public Integer getMccTo1() {
		return mccTo1;
	}

	public Integer getMccTo10() {
		return mccTo10;
	}

	public Integer getMccTo11() {
		return mccTo11;
	}

	public Integer getMccTo12() {
		return mccTo12;
	}

	public Integer getMccTo2() {
		return mccTo2;
	}

	public Integer getMccTo3() {
		return mccTo3;
	}

	public Integer getMccTo4() {
		return mccTo4;
	}

	public Integer getMccTo5() {
		return mccTo5;
	}

	public Integer getMccTo6() {
		return mccTo6;
	}

	public Integer getMccTo7() {
		return mccTo7;
	}

	public Integer getMccTo8() {
		return mccTo8;
	}

	public Integer getMccTo9() {
		return mccTo9;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public String getTransLocation() {
		return transLocation;
	}

	public String getTransMode1() {
		return transMode1;
	}

	public String getTransMode2() {
		return transMode2;
	}

	public String getTransMode3() {
		return transMode3;
	}

	public String getTransMode4() {
		return transMode4;
	}

	public String getTransMode5() {
		return transMode5;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}

	public void setContrlSceneCode(String contrlSceneCode) {
		this.contrlSceneCode = contrlSceneCode;
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

	public void setMccFrom1(Integer mccFrom1) {
		this.mccFrom1 = mccFrom1;
	}

	public void setMccFrom10(Integer mccFrom10) {
		this.mccFrom10 = mccFrom10;
	}

	public void setMccFrom11(Integer mccFrom11) {
		this.mccFrom11 = mccFrom11;
	}

	public void setMccFrom12(Integer mccFrom12) {
		this.mccFrom12 = mccFrom12;
	}

	public void setMccFrom2(Integer mccFrom2) {
		this.mccFrom2 = mccFrom2;
	}

	public void setMccFrom3(Integer mccFrom3) {
		this.mccFrom3 = mccFrom3;
	}

	public void setMccFrom4(Integer mccFrom4) {
		this.mccFrom4 = mccFrom4;
	}

	public void setMccFrom5(Integer mccFrom5) {
		this.mccFrom5 = mccFrom5;
	}

	public void setMccFrom6(Integer mccFrom6) {
		this.mccFrom6 = mccFrom6;
	}

	public void setMccFrom7(Integer mccFrom7) {
		this.mccFrom7 = mccFrom7;
	}

	public void setMccFrom8(Integer mccFrom8) {
		this.mccFrom8 = mccFrom8;
	}

	public void setMccFrom9(Integer mccFrom9) {
		this.mccFrom9 = mccFrom9;
	}

	public void setMccTo1(Integer mccTo1) {
		this.mccTo1 = mccTo1;
	}

	public void setMccTo10(Integer mccTo10) {
		this.mccTo10 = mccTo10;
	}

	public void setMccTo11(Integer mccTo11) {
		this.mccTo11 = mccTo11;
	}

	public void setMccTo12(Integer mccTo12) {
		this.mccTo12 = mccTo12;
	}

	public void setMccTo2(Integer mccTo2) {
		this.mccTo2 = mccTo2;
	}

	public void setMccTo3(Integer mccTo3) {
		this.mccTo3 = mccTo3;
	}

	public void setMccTo4(Integer mccTo4) {
		this.mccTo4 = mccTo4;
	}

	public void setMccTo5(Integer mccTo5) {
		this.mccTo5 = mccTo5;
	}

	public void setMccTo6(Integer mccTo6) {
		this.mccTo6 = mccTo6;
	}

	public void setMccTo7(Integer mccTo7) {
		this.mccTo7 = mccTo7;
	}

	public void setMccTo8(Integer mccTo8) {
		this.mccTo8 = mccTo8;
	}

	public void setMccTo9(Integer mccTo9) {
		this.mccTo9 = mccTo9;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public void setTransLocation(String transLocation) {
		this.transLocation = transLocation;
	}

	public void setTransMode1(String transMode1) {
		this.transMode1 = transMode1;
	}

	public void setTransMode2(String transMode2) {
		this.transMode2 = transMode2;
	}

	public void setTransMode3(String transMode3) {
		this.transMode3 = transMode3;
	}

	public void setTransMode4(String transMode4) {
		this.transMode4 = transMode4;
	}

	public void setTransMode5(String transMode5) {
		this.transMode5 = transMode5;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8046BO [id=" + id + ", operationMode=" + operationMode + ", contrlSceneCode=" + contrlSceneCode
				+ ", authSceneCode=" + authSceneCode + ", transLocation=" + transLocation + ", mccFrom1=" + mccFrom1
				+ ", mccTo1=" + mccTo1 + ", mccFrom2=" + mccFrom2 + ", mccTo2=" + mccTo2 + ", mccFrom3=" + mccFrom3
				+ ", mccTo3=" + mccTo3 + ", mccFrom4=" + mccFrom4 + ", mccTo4=" + mccTo4 + ", mccFrom5=" + mccFrom5
				+ ", mccTo5=" + mccTo5 + ", mccFrom6=" + mccFrom6 + ", mccTo6=" + mccTo6 + ", mccFrom7=" + mccFrom7
				+ ", mccTo7=" + mccTo7 + ", mccFrom8=" + mccFrom8 + ", mccTo8=" + mccTo8 + ", mccFrom9=" + mccFrom9
				+ ", mccTo9=" + mccTo9 + ", mccFrom10=" + mccFrom10 + ", mccTo10=" + mccTo10 + ", mccFrom11="
				+ mccFrom11 + ", mccTo11=" + mccTo11 + ", mccFrom12=" + mccFrom12 + ", mccTo12=" + mccTo12
				+ ", transMode1=" + transMode1 + ", transMode2=" + transMode2 + ", transMode3=" + transMode3
				+ ", transMode4=" + transMode4 + ", transMode5=" + transMode5 + ", gmtCreate=" + gmtCreate
				+ ", gmtModified=" + gmtModified + ", version=" + version + "]";
	}

}
