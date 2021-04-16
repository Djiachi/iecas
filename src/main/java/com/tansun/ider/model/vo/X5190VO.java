package com.tansun.ider.model.vo;

import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;

public class X5190VO {

	private CoreCustomerContrlView coreCustomerContrlView ;
	
	private 	CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderStr;

	public CoreCustomerContrlView getCoreCustomerContrlView() {
		return coreCustomerContrlView;
	}

	public void setCoreCustomerContrlView(CoreCustomerContrlView coreCustomerContrlView) {
		this.coreCustomerContrlView = coreCustomerContrlView;
	}

	public CoreCustomerContrlViewSqlBuilder getCoreCustomerContrlViewSqlBuilderStr() {
		return coreCustomerContrlViewSqlBuilderStr;
	}

	public void setCoreCustomerContrlViewSqlBuilderStr(
			CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderStr) {
		this.coreCustomerContrlViewSqlBuilderStr = coreCustomerContrlViewSqlBuilderStr;
	}
	
	
}
