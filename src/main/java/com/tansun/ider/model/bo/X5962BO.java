package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5962BO extends BeanVO implements Serializable {
    
    private static final long serialVersionUID = 1336069936475486515L;
    
    /** ID [12,0] */
    protected String id;
    /** 客户号 [12,0] */
    protected String customerNo;
    /** 收费项目编号 [8,0] Not NULL */
    protected String feeItemNo;
    /** 账户代码/媒介单元代码/产品单元代码 [18,0] Not NULL */
    protected String entityKey;
    /** 货币代码 [3,0] */
    protected String currencyCode;
    /** 免除周期 [6,0] */
    protected String waiveCycleNo;
    /** 已执行次数 [10,0] */
    protected Integer executedNum;
    /** 已免除次数 [10,0] */
    protected Integer waivedNum;
    /** 首次免除日期 [10,0] */
    protected String firstWaiveDate;
    /** 上次免除日期 [10,0] */
    protected String lastWaiveDate;
    /** 上次免除金额 [18,0] */
    protected BigDecimal lastWaiveAmt;
    /** 累计免除金额 [18,0] */
    protected BigDecimal accumultWaiveAmt;
    private String instanCode1;
	private String instanCode2;
	private String instanCode3;
	private String instanCode4;
	private String instanCode5;
    /**
     * @return the customerNo
     */
    public String getCustomerNo() {
        return customerNo;
    }
    /**
     * @param customerNo the customerNo to set
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    /**
     * @return the feeItemNo
     */
    public String getFeeItemNo() {
        return feeItemNo;
    }
    /**
     * @param feeItemNo the feeItemNo to set
     */
    public void setFeeItemNo(String feeItemNo) {
        this.feeItemNo = feeItemNo;
    }
    /**
     * @return the entityKey
     */
    public String getEntityKey() {
        return entityKey;
    }
    /**
     * @param entityKey the entityKey to set
     */
    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }
    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    /**
     * @return the waiveCycleNo
     */
    public String getWaiveCycleNo() {
        return waiveCycleNo;
    }
    /**
     * @param waiveCycleNo the waiveCycleNo to set
     */
    public void setWaiveCycleNo(String waiveCycleNo) {
        this.waiveCycleNo = waiveCycleNo;
    }
    /**
     * @return the executedNum
     */
    public Integer getExecutedNum() {
        return executedNum;
    }
    /**
     * @param executedNum the executedNum to set
     */
    public void setExecutedNum(Integer executedNum) {
        this.executedNum = executedNum;
    }
    /**
     * @return the waivedNum
     */
    public Integer getWaivedNum() {
        return waivedNum;
    }
    /**
     * @param waivedNum the waivedNum to set
     */
    public void setWaivedNum(Integer waivedNum) {
        this.waivedNum = waivedNum;
    }
    /**
     * @return the firstWaiveDate
     */
    public String getFirstWaiveDate() {
        return firstWaiveDate;
    }
    /**
     * @param firstWaiveDate the firstWaiveDate to set
     */
    public void setFirstWaiveDate(String firstWaiveDate) {
        this.firstWaiveDate = firstWaiveDate;
    }
    /**
     * @return the lastWaiveDate
     */
    public String getLastWaiveDate() {
        return lastWaiveDate;
    }
    /**
     * @param lastWaiveDate the lastWaiveDate to set
     */
    public void setLastWaiveDate(String lastWaiveDate) {
        this.lastWaiveDate = lastWaiveDate;
    }
    /**
     * @return the lastWaiveAmt
     */
    public BigDecimal getLastWaiveAmt() {
        return lastWaiveAmt;
    }
    /**
     * @param lastWaiveAmt the lastWaiveAmt to set
     */
    public void setLastWaiveAmt(BigDecimal lastWaiveAmt) {
        this.lastWaiveAmt = lastWaiveAmt;
    }
    /**
     * @return the accumultWaiveAmt
     */
    public BigDecimal getAccumultWaiveAmt() {
        return accumultWaiveAmt;
    }
    /**
     * @param accumultWaiveAmt the accumultWaiveAmt to set
     */
    public void setAccumultWaiveAmt(BigDecimal accumultWaiveAmt) {
        this.accumultWaiveAmt = accumultWaiveAmt;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstanCode1() {
		return instanCode1;
	}
	public void setInstanCode1(String instanCode1) {
		this.instanCode1 = instanCode1;
	}
	public String getInstanCode2() {
		return instanCode2;
	}
	public void setInstanCode2(String instanCode2) {
		this.instanCode2 = instanCode2;
	}
	public String getInstanCode3() {
		return instanCode3;
	}
	public void setInstanCode3(String instanCode3) {
		this.instanCode3 = instanCode3;
	}
	public String getInstanCode4() {
		return instanCode4;
	}
	public void setInstanCode4(String instanCode4) {
		this.instanCode4 = instanCode4;
	}
	public String getInstanCode5() {
		return instanCode5;
	}
	public void setInstanCode5(String instanCode5) {
		this.instanCode5 = instanCode5;
	}
	@Override
	public String toString() {
		return "X5962BO [id=" + id + ", customerNo=" + customerNo + ", feeItemNo=" + feeItemNo + ", entityKey="
				+ entityKey + ", currencyCode=" + currencyCode + ", waiveCycleNo=" + waiveCycleNo + ", executedNum="
				+ executedNum + ", waivedNum=" + waivedNum + ", firstWaiveDate=" + firstWaiveDate + ", lastWaiveDate="
				+ lastWaiveDate + ", lastWaiveAmt=" + lastWaiveAmt + ", accumultWaiveAmt=" + accumultWaiveAmt
				+ ", instanCode1=" + instanCode1 + ", instanCode2=" + instanCode2 + ", instanCode3=" + instanCode3
				+ ", instanCode4=" + instanCode4 + ", instanCode5=" + instanCode5 + "]";
	}
    
}
