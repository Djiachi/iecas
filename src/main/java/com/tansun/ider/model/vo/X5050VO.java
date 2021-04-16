package com.tansun.ider.model.vo;

import java.util.List;

import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;

/**
 * @Desc:客户封锁码列表查询
 * @Author wt
 * @Date 2018年5月4日 下午4:31:42
 */
public class X5050VO {

	/* 客户封锁码表 */
	private List<CoreCustomerEffectiveCode> coreCustomerEffectiveCode;

	public List<CoreCustomerEffectiveCode> getCoreCustomerEffectiveCode() {
		return coreCustomerEffectiveCode;
	}

	public void setCoreCustomerEffectiveCode(List<CoreCustomerEffectiveCode> coreCustomerEffectiveCode) {
		this.coreCustomerEffectiveCode = coreCustomerEffectiveCode;
	}
	
}
