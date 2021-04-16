package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5702BO  extends BeanVO implements Serializable {

	private static final long serialVersionUID = -1565218311024221764L;

	 protected String subjectNbr;
	  
	 protected String subjectNme;

	public String getSubjectNbr() {
		return subjectNbr;
	}

	public void setSubjectNbr(String subjectNbr) {
		this.subjectNbr = subjectNbr;
	}

	public String getSubjectNme() {
		return subjectNme;
	}

	public void setSubjectNme(String subjectNme) {
		this.subjectNme = subjectNme;
	}
}
