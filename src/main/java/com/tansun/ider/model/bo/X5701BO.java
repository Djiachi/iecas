package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5701BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 1450393749870811590L;

	  protected String temName;
	  
	  protected String accountScene;
	  
	  protected String paramDesc;
	  
	  protected String eventNo;

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getTemName() {
		return temName;
	}

	public void setTemName(String temName) {
		this.temName = temName;
	}

	public String getAccountScene() {
		return accountScene;
	}

	public void setAccountScene(String accountScene) {
		this.accountScene = accountScene;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	@Override
	public String toString() {
		return "X5701BO [temName=" + temName + ", accountScene=" + accountScene + ", paramDesc=" + paramDesc + "]";
	}
	
}
