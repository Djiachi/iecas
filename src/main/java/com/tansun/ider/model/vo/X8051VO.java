package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.dao.beta.entity.AuthSceneDistin;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.framwork.commun.PageBean;

public class X8051VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = -2563282886664492663L;
	private String authSceneCode;
	private String authSceneMsg;
	PageBean<AuthSceneDistin> page = new PageBean<>();
	public String getAuthSceneCode() {
		return authSceneCode;
	}
	public String getAuthSceneMsg() {
		return authSceneMsg;
	}
	public PageBean<AuthSceneDistin> getPage() {
		return page;
	}
	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}
	public void setAuthSceneMsg(String authSceneMsg) {
		this.authSceneMsg = authSceneMsg;
	}
	public void setPage(PageBean<AuthSceneDistin> page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "X8051VO [authSceneCode=" + authSceneCode + ", authSceneMsg=" + authSceneMsg + ", page=" + page + "]";
	}
}
