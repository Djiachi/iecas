package com.tansun.ider.model.vo;

public class X5510VO {

    /** 描述 [50,0] */
    protected String effectivenessCodeDesc;
    /** 事件描述 [50,0] */
    protected String eventDesc;
	/** 客户号 [12,0] Not NULL */
    protected String customerNo;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 设置日期 [10,0] Not NULL */
    protected String settingDate;
    /** 设置时间 [9,0] Not NULL */
    protected String settingTime;
    /** 生效码类别 [1,0] Not NULL */
    protected String effectivenessCodeType;
    /** 生效码序号 [2,0] Not NULL */
    protected String effectivenessCodeScene;
    /** 场景触发对象层级 C/G/A/P/M C-客户级/G-业务项目级/A-业务类型级/P-产品级/M-媒介级 [1,0] Not NULL */
    protected String sceneTriggerObjectLevel;
    /** 场景触发对象代码  账户代码/产品对象代码/媒介单元代码/业务类型代码/余额对象代码/业务项目代码 [23,0] Not NULL */
    protected String sceneTriggerObjectCode;
    /** 币种 层级代码为账户时有值 [3,0] */
    protected String currencyCode;
    /** 当前生效码日期 yyyy-MM-dd [10,0] Not NULL */
    protected String currEffectivenessDate;
    /** 生效码解除时系统自动赋值 yyyy-MM-dd [10,0] */
    protected String endEffectivenessDate;
    /** 设置人员代码 [20,0] Not NULL */
    protected String settingUpUserid;
    /** 解除人员代码 [20,0] */
    protected String removalUserid;
    /** 解除日期 [10,0] */
    protected String removeDate;
    /** 状态 [1,0] Not NULL */
    protected String state;
    /** 运营模式 */
    protected String operationMode;
    
    private String currencyDesc;
    
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getSettingDate() {
		return settingDate;
	}
	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}
	public String getSettingTime() {
		return settingTime;
	}
	public void setSettingTime(String settingTime) {
		this.settingTime = settingTime;
	}
	public String getEffectivenessCodeType() {
		return effectivenessCodeType;
	}
	public void setEffectivenessCodeType(String effectivenessCodeType) {
		this.effectivenessCodeType = effectivenessCodeType;
	}
	public String getEffectivenessCodeScene() {
		return effectivenessCodeScene;
	}
	public void setEffectivenessCodeScene(String effectivenessCodeScene) {
		this.effectivenessCodeScene = effectivenessCodeScene;
	}
	public String getSceneTriggerObjectLevel() {
		return sceneTriggerObjectLevel;
	}
	public void setSceneTriggerObjectLevel(String sceneTriggerObjectLevel) {
		this.sceneTriggerObjectLevel = sceneTriggerObjectLevel;
	}
	public String getSceneTriggerObjectCode() {
		return sceneTriggerObjectCode;
	}
	public void setSceneTriggerObjectCode(String sceneTriggerObjectCode) {
		this.sceneTriggerObjectCode = sceneTriggerObjectCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrEffectivenessDate() {
		return currEffectivenessDate;
	}
	public void setCurrEffectivenessDate(String currEffectivenessDate) {
		this.currEffectivenessDate = currEffectivenessDate;
	}
	public String getEndEffectivenessDate() {
		return endEffectivenessDate;
	}
	public void setEndEffectivenessDate(String endEffectivenessDate) {
		this.endEffectivenessDate = endEffectivenessDate;
	}
	public String getSettingUpUserid() {
		return settingUpUserid;
	}
	public void setSettingUpUserid(String settingUpUserid) {
		this.settingUpUserid = settingUpUserid;
	}
	public String getRemovalUserid() {
		return removalUserid;
	}
	public void setRemovalUserid(String removalUserid) {
		this.removalUserid = removalUserid;
	}
	public String getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getEffectivenessCodeDesc() {
		return effectivenessCodeDesc;
	}
	public void setEffectivenessCodeDesc(String effectivenessCodeDesc) {
		this.effectivenessCodeDesc = effectivenessCodeDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	
}
