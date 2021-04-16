package com.tansun.ider.model.vo;

public class X5265VO {
	
	
	/** 外部识别号 */
	protected String externalIdentificationNo;
	/** 客户代码 [12,0] Not NULL */
    protected String customerNo;
    /** 管控项目编号 [10,0] Not NULL */
    protected String controlProjectNo;
    /** 管控项目描述 */
    private String controlDesc;
    /** 管控序号 [10,0] */
    protected Integer contrlSerialNo;
    /** 管控层级 C-客户级 A-业务类型级 P-产品级 M-媒介级 [1,0] */
    protected String contrlLevel;
    /** 层级代码 账户代码/产品单元代码/媒介单元代码 [18,0] */
    protected String levelCode;
    /** 币种 层级代码为账户时有值 [3,0] */
    protected String currencyCode;
    /** 封锁码类别 [1,0] Not NULL */
    protected String blockCodeType;
    /** 封锁码场景 [2,0] Not NULL */
    protected String blockCodeScene;
    /** 管控来源  BLCK-封锁码 [4,0] */
    protected String contrlSource;
    /** 管控开始日期 [10,0] */
    protected String contrlStartDate;
    /** 管控结束日期 [10,0] */
    protected String contrlEndDate;
    /** 币种描述 */
    protected String currencyDesc;
    /** 管控模式 */
    private String controlMode;
    /** 生效码类别 [1,0] */
    protected String effectivenessCodeType;
    /** 生效码序号 [10,0] */
    protected Integer effectivenessCodeScene;
    
    public String getControlMode() {
		return controlMode;
	}
	public void setControlMode(String controlMode) {
		this.controlMode = controlMode;
	}
	public String getCurrencyDesc() {
        return currencyDesc;
    }
    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public Integer getContrlSerialNo() {
		return contrlSerialNo;
	}
	public void setContrlSerialNo(Integer contrlSerialNo) {
		this.contrlSerialNo = contrlSerialNo;
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
	public String getBlockCodeType() {
		return blockCodeType;
	}
	public void setBlockCodeType(String blockCodeType) {
		this.blockCodeType = blockCodeType;
	}
	public String getBlockCodeScene() {
		return blockCodeScene;
	}
	public void setBlockCodeScene(String blockCodeScene) {
		this.blockCodeScene = blockCodeScene;
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
	public String getControlProjectNo() {
		return controlProjectNo;
	}
	public void setControlProjectNo(String controlProjectNo) {
		this.controlProjectNo = controlProjectNo;
	}
	public String getControlDesc() {
		return controlDesc;
	}
	public void setControlDesc(String controlDesc) {
		this.controlDesc = controlDesc;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
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
    
}
