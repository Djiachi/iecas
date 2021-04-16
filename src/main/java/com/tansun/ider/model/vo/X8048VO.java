package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;
/**
 * X8048VO
 * @author kangx
 * 授权管控场景维护
 */
public class X8048VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = 8993569835462623298L;
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
	private String sceneDesc;
	private String highRiskArea1;
	private String highRiskArea2;
	private String highRiskArea3;
	private String highRiskArea4;
	private String highRiskArea5;
	private Integer version;
	private String differentCode;
	/** 管控开始时间 [23,0] */
    protected String startTime;
    /** 管控结束时间 [23,0] */
    protected String endTime;
    /** 优先级 [3,0] */
    protected Integer priority;
    
	private String DF_pnListCheckFlag;
	private String DF_listCode;
	private String DF_transLimitLevel;
	private String DF_transLimitCode;
	private String DF_id;
	private Integer DF_version;

	public String getDF_pnListCheckFlag() {
		return DF_pnListCheckFlag;
	}
	public void setDF_pnListCheckFlag(String dF_pnListCheckFlag) {
		DF_pnListCheckFlag = dF_pnListCheckFlag;
	}
	public String getDF_listCode() {
		return DF_listCode;
	}
	
	public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    @Override
	public String toString() {
		return "X8048VO [id=" + id + ", operationMode=" + operationMode + ", contrlSceneCode=" + contrlSceneCode
				+ ", authSceneCode=" + authSceneCode + ", transLocation=" + transLocation + ", mccFrom1=" + mccFrom1
				+ ", mccTo1=" + mccTo1 + ", mccFrom2=" + mccFrom2 + ", mccTo2=" + mccTo2 + ", mccFrom3=" + mccFrom3
				+ ", mccTo3=" + mccTo3 + ", mccFrom4=" + mccFrom4 + ", mccTo4=" + mccTo4 + ", mccFrom5=" + mccFrom5
				+ ", mccTo5=" + mccTo5 + ", mccFrom6=" + mccFrom6 + ", mccTo6=" + mccTo6 + ", mccFrom7=" + mccFrom7
				+ ", mccTo7=" + mccTo7 + ", mccFrom8=" + mccFrom8 + ", mccTo8=" + mccTo8 + ", mccFrom9=" + mccFrom9
				+ ", mccTo9=" + mccTo9 + ", mccFrom10=" + mccFrom10 + ", mccTo10=" + mccTo10 + ", mccFrom11="
				+ mccFrom11 + ", mccTo11=" + mccTo11 + ", mccFrom12=" + mccFrom12 + ", mccTo12=" + mccTo12
				+ ", transMode1=" + transMode1 + ", transMode2=" + transMode2 + ", transMode3=" + transMode3
				+ ", transMode4=" + transMode4 + ", transMode5=" + transMode5 + ", gmtCreate=" + gmtCreate
				+ ", gmtModified=" + gmtModified + ", sceneDesc=" + sceneDesc + ", highRiskArea1=" + highRiskArea1
				+ ", highRiskArea2=" + highRiskArea2 + ", highRiskArea3=" + highRiskArea3 + ", highRiskArea4="
				+ highRiskArea4 + ", highRiskArea5=" + highRiskArea5 + ", version=" + version + ", differentCode="
				+ differentCode + ", DF_pnListCheckFlag=" + DF_pnListCheckFlag + ", DF_listCode=" + DF_listCode
				+ ", DF_transLimitLevel=" + DF_transLimitLevel + ", DF_transLimitCode=" + DF_transLimitCode + ", DF_id="
				+ DF_id + ", DF_version=" + DF_version + "]";
	}
	public void setDF_listCode(String dF_listCode) {
		DF_listCode = dF_listCode;
	}
	public String getDF_transLimitLevel() {
		return DF_transLimitLevel;
	}
	public void setDF_transLimitLevel(String dF_transLimitLevel) {
		DF_transLimitLevel = dF_transLimitLevel;
	}
	public String getDF_transLimitCode() {
		return DF_transLimitCode;
	}
	public void setDF_transLimitCode(String dF_transLimitCode) {
		DF_transLimitCode = dF_transLimitCode;
	}
	public String getDF_id() {
		return DF_id;
	}
	public void setDF_id(String dF_id) {
		DF_id = dF_id;
	}
	public Integer getDF_version() {
		return DF_version;
	}
	public void setDF_version(Integer dF_version) {
		DF_version = dF_version;
	}
	public String getAuthSceneCode() {
		return authSceneCode;
	}
	public String getContrlSceneCode() {
		return contrlSceneCode;
	}
	public String getDifferentCode() {
		return differentCode;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public Date getGmtModified() {
		return gmtModified;
	}
	public String getHighRiskArea1() {
		return highRiskArea1;
	}
	public String getHighRiskArea2() {
		return highRiskArea2;
	}
	public String getHighRiskArea3() {
		return highRiskArea3;
	}
	public String getHighRiskArea4() {
		return highRiskArea4;
	}
	public String getHighRiskArea5() {
		return highRiskArea5;
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
	public String getSceneDesc() {
		return sceneDesc;
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
	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	public void setHighRiskArea1(String highRiskArea1) {
		this.highRiskArea1 = highRiskArea1;
	}
	public void setHighRiskArea2(String highRiskArea2) {
		this.highRiskArea2 = highRiskArea2;
	}
	public void setHighRiskArea3(String highRiskArea3) {
		this.highRiskArea3 = highRiskArea3;
	}
	public void setHighRiskArea4(String highRiskArea4) {
		this.highRiskArea4 = highRiskArea4;
	}
	public void setHighRiskArea5(String highRiskArea5) {
		this.highRiskArea5 = highRiskArea5;
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
	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
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
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
