package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 信用卡分期子账户信息查询
 *
 * @author cuiguangchao 2019年03月28日
 */
public class X5631BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 5035068883811420241L;

	/** 交易流水号 */
	private String globalTransSerialNo;

	/** 账户类型 R-循环账户 T-交易账户 B-不良资产 [1,0] */
	private String accountType;

	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}

	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "X5631BO{" +
				"globalTransSerialNo='" + globalTransSerialNo + '\'' +
				", accountType='" + accountType + '\'' +
				'}';
	}
}
