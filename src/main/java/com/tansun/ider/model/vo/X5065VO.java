package com.tansun.ider.model.vo;

public class X5065VO {

	/** 运营模式 : 运营模式 [3,0] Not NULL */
	protected String operationMode;
	/** 实例类型 : 实例类型 D：核算状态，E：事件，B：封锁码 [1,0] */
	protected String instanDimen;
	/** 实例代码 : 实例代码 当实例类型为D时，核算状态码与账户基本信息表中的核算状态码一致。 为E时，来自事件表的事件编码 [14,0] */
	protected String instanCode;
	/** 构件编号 : 构件编号 [3,0] Not NULL */
	protected String artifactNo;
	/** 执行顺序 : 执行顺序 [10,0] */
	protected Integer performOrder;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	/** 元件编号 : 元件编号 [15,0] Not NULL */
	protected String elementNo;
	/** 元件描述 : 元件描述 [50,0] Not NULL */
	protected String elementDesc;
	/** 版本号 [10,0] */
	protected Integer version;
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getInstanDimen() {
		return instanDimen;
	}
	public void setInstanDimen(String instanDimen) {
		this.instanDimen = instanDimen;
	}
	public String getInstanCode() {
		return instanCode;
	}
	public void setInstanCode(String instanCode) {
		this.instanCode = instanCode;
	}
	public String getArtifactNo() {
		return artifactNo;
	}
	public void setArtifactNo(String artifactNo) {
		this.artifactNo = artifactNo;
	}
	public Integer getPerformOrder() {
		return performOrder;
	}
	public void setPerformOrder(Integer performOrder) {
		this.performOrder = performOrder;
	}
	public String getElementNo() {
		return elementNo;
	}
	public void setElementNo(String elementNo) {
		this.elementNo = elementNo;
	}
	public String getElementDesc() {
		return elementDesc;
	}
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "X5065VO [operationMode=" + operationMode + ", instanDimen=" + instanDimen + ", instanCode=" + instanCode
				+ ", artifactNo=" + artifactNo + ", performOrder=" + performOrder + ", elementNo=" + elementNo
				+ ", elementDesc=" + elementDesc + ", version=" + version + "]";
	}
	
}
