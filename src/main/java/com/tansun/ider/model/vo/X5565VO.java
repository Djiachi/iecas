package com.tansun.ider.model.vo;

import java.util.List;

import com.tansun.ider.model.CoreAccountBean;
import com.tansun.ider.model.CoreBalanceBean;

public class X5565VO {

	private CoreAccountBean coreAccountBean;
	private List<CoreBalanceBean> listcoreBalanceBean;
	
	public CoreAccountBean getCoreAccountBean() {
		return coreAccountBean;
	}
	public void setCoreAccountBean(CoreAccountBean coreAccountBean) {
		this.coreAccountBean = coreAccountBean;
	}
	public List<CoreBalanceBean> getListcoreBalanceBean() {
		return listcoreBalanceBean;
	}
	public void setListcoreBalanceBean(List<CoreBalanceBean> listcoreBalanceBean) {
		this.listcoreBalanceBean = listcoreBalanceBean;
	}
	@Override
	public String toString() {
		return "X5565VO [coreAccountBean=" + coreAccountBean + ", listcoreBalanceBean=" + listcoreBalanceBean + "]";
	}
	
}
