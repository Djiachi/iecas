package com.tansun.ider.model.bo;

import java.io.Serializable;


import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 计息控制链表查询
 * 
 * @author lianhuan 2018年10月25日
 */
public class X5535BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 5035068883811420241L;
    /** 余额单元代码 [18,0] */
    private String balanceUnitCode;
    private String currencyCode;
    private String cycleNumber;
    private String balanceObjectCode;
    private String accountId;
    private String flag;
    
    
    public String getBalanceObjectCode() {
		return balanceObjectCode;
	}

	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}

	public String getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(String cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public String getBalanceUnitCode() {
        return balanceUnitCode;
    }

    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
	public String toString() {
		return "X5535BO [balanceUnitCode=" + balanceUnitCode + ", currencyCode=" + currencyCode + ", cycleNumber="
				+ cycleNumber + ", balanceObjectCode=" + balanceObjectCode + ", accountId=" + accountId + "]";
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
