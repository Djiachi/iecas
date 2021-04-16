package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5310BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -5694685372799253433L;
	/** 产品对象代码 */
	@NotBlank(message = "产品对象必须输入，且长度必须大于0!")
	private String productObjectCode;
	/** 运行模式 */
	@NotBlank(message = "运营模式必须输入，且长度必须大于0!")
	private String operationMode;
	/** 产品描述  */
	private String programDesc;
	/** 账单日设置按照格式  */
	private String cycleFrequency;
	/** 业务项目代码  */
	private String businessProgramNo;
	
	
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}

	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}

	public String getCycleFrequency() {
		return cycleFrequency;
	}

	public void setCycleFrequency(String cycleFrequency) {
		this.cycleFrequency = cycleFrequency;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	
	
}
