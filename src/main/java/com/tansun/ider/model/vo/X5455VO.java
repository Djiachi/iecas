package com.tansun.ider.model.vo;

public class X5455VO {

	/** 外部识别号 */
	protected String externalIdentificationNo;
	/** 管控项目编号描述 */
	protected String controlDesc;
	/** 管控动作 NE-禁止执行 IE-立即执行（用于分期提前结清事件）*/
	protected String controlField;
	/** 客户代码 [12,0] Not NULL */
    protected String customerNo;
    /** 管控项目编号 [9,0] Not NULL */
    protected String controlProjectNo;
    /** 管控层级 C-客户级 A-业务类型级 P-产品级 M-媒介级 [1,0] */
    protected String contrlLevel;
    /** 层级代码 账户代码/产品单元代码/媒介单元代码 [23,0] */
    protected String levelCode;
    /** 币种 层级代码为账户时有值 [3,0] */
    protected String currencyCode;
    /** 生效码类别 [1,0] */
    protected String effectivenessCodeType;
    /** 生效码序号 [10,0] */
    protected Integer effectivenessCodeScene;
    /** 管控来源  BLCK-封锁码 [4,0] */
    protected String contrlSource;
    /** 管控开始日期 [10,0] */
    protected String contrlStartDate;
    /** 管控结束日期 [10,0] */
    protected String contrlEndDate;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 币种描述 */
    protected String currencyDesc;
    /** 状况描述 */
    protected String effectivenessCodeDesc;
    /** 事件描述 */
    protected String eventDesc;
    
    public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getCurrencyDesc() {
        return currencyDesc;
    }
    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }
	public String getControlDesc() {
		return controlDesc;
	}
	public void setControlDesc(String controlDesc) {
		this.controlDesc = controlDesc;
	}
	public String getControlField() {
		return controlField;
	}
	public void setControlField(String controlField) {
		this.controlField = controlField;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getControlProjectNo() {
		return controlProjectNo;
	}
	public void setControlProjectNo(String controlProjectNo) {
		this.controlProjectNo = controlProjectNo;
	}
	public String getContrlLevel() {
		return contrlLevel;
	}
	public void setContrlLevel(String contrlLevel) {
		this.contrlLevel = contrlLevel;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getEffectivenessCodeType() {
		return effectivenessCodeType;
	}
	public void setEffectivenessCodeType(String effectivenessCodeType) {
		this.effectivenessCodeType = effectivenessCodeType;
	}
	public Integer getEffectivenessCodeScene() {
		return effectivenessCodeScene;
	}
	public void setEffectivenessCodeScene(Integer effectivenessCodeScene) {
		this.effectivenessCodeScene = effectivenessCodeScene;
	}
	public String getContrlSource() {
		return contrlSource;
	}
	public void setContrlSource(String contrlSource) {
		this.contrlSource = contrlSource;
	}
	public String getContrlStartDate() {
		return contrlStartDate;
	}
	public void setContrlStartDate(String contrlStartDate) {
		this.contrlStartDate = contrlStartDate;
	}
	public String getContrlEndDate() {
		return contrlEndDate;
	}
	public void setContrlEndDate(String contrlEndDate) {
		this.contrlEndDate = contrlEndDate;
	}
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getEffectivenessCodeDesc() {
		return effectivenessCodeDesc;
	}
	public void setEffectivenessCodeDesc(String effectivenessCodeDesc) {
		this.effectivenessCodeDesc = effectivenessCodeDesc;
	}
    
    
}
