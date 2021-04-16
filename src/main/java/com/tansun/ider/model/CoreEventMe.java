package com.tansun.ider.model;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.dao.beta.entity.CoreEvent;

public class CoreEventMe extends CoreEvent implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1967819793105046682L;
	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 事件描述 [50,0] */
    protected String eventDesc;
    /** 事件记账方向 借记：D 贷记：C 可变: A [1,0] */
    protected String eventBookKeepingDirec;
    /** 对应余额对象 : 对应余额对象             [9,0] */
    protected String eventBalanceObject;
    /** 对应余额类型 P：本金；F：费用；I：利息； [1,0] */
    protected String eventBalanceType;
    /** 事件类别 MONY-金融类(RT,PT) NMNY-非金融类(AD,CS,IQ,OP,UP) AUTH-授权类(AU) OTHR-其他类（BH,AP） [4,0] */
    protected String eventType;
    /** 交易统计组别 MEMB-参与免年费统计 [4,0] */
    protected String transStatisticGroup;
    /** 交易识别代码 R001 消费类  C001 取现类 P001 还款类 [4,0] */
    protected String transIdentificationCode;
    /** 还款类型  P：还款 R：还款还原 [1,0] */
    protected String paymentType;
    /**  交易类型 Y:授权还款类 (AU.20) X:授权贷记类 （AU.43） D: 发卡争议类 (05、06/07) C:发卡贷记类(13、41、43、61) [1,0] */
    protected String transType;
    /** 7*24小时标识  空/N  默认为空，设置N表示不支持7*24小时，目前仅手工录入事件为N [1,0] */
    protected String ongoingProcessFlag;
    /** 授权场景码 [6,0] */
    protected String authSceneCode;
    /** 收费项目编号 [8,0] */
    protected String feeItemNo;
    /** 允许争议标识    默认为空，设置N为不允许争议 [1,0] */
    protected String disputeFlag;
    /** 资金核对代码 [5,0] */
    protected String fundCheckCode;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 分期类型:MERH：商户分期TXAT：自动分期CASH：现金分期SPCL：专项分期STMT：账单分期TRAN：交易分期 LOAN:信贷分期 [4,0] */
    protected String installType;
    /** 菜单编号 [32,0] Not NULL */
    protected String menuNo;
    /** 菜单名称 [20,0] Not NULL */
    protected String menuName;
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getEventBookKeepingDirec() {
		return eventBookKeepingDirec;
	}
	public void setEventBookKeepingDirec(String eventBookKeepingDirec) {
		this.eventBookKeepingDirec = eventBookKeepingDirec;
	}
	public String getEventBalanceObject() {
		return eventBalanceObject;
	}
	public void setEventBalanceObject(String eventBalanceObject) {
		this.eventBalanceObject = eventBalanceObject;
	}
	public String getEventBalanceType() {
		return eventBalanceType;
	}
	public void setEventBalanceType(String eventBalanceType) {
		this.eventBalanceType = eventBalanceType;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getTransStatisticGroup() {
		return transStatisticGroup;
	}
	public void setTransStatisticGroup(String transStatisticGroup) {
		this.transStatisticGroup = transStatisticGroup;
	}
	public String getTransIdentificationCode() {
		return transIdentificationCode;
	}
	public void setTransIdentificationCode(String transIdentificationCode) {
		this.transIdentificationCode = transIdentificationCode;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getOngoingProcessFlag() {
		return ongoingProcessFlag;
	}
	public void setOngoingProcessFlag(String ongoingProcessFlag) {
		this.ongoingProcessFlag = ongoingProcessFlag;
	}
	public String getAuthSceneCode() {
		return authSceneCode;
	}
	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}
	public String getFeeItemNo() {
		return feeItemNo;
	}
	public void setFeeItemNo(String feeItemNo) {
		this.feeItemNo = feeItemNo;
	}
	public String getDisputeFlag() {
		return disputeFlag;
	}
	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}
	public String getFundCheckCode() {
		return fundCheckCode;
	}
	public void setFundCheckCode(String fundCheckCode) {
		this.fundCheckCode = fundCheckCode;
	}
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getInstallType() {
		return installType;
	}
	public void setInstallType(String installType) {
		this.installType = installType;
	}
	public String getMenuNo() {
		return menuNo;
	}
	public void setMenuNo(String menuNo) {
		this.menuNo = menuNo;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
}
