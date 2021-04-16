package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class X5800VO implements Serializable{
    private static final long serialVersionUID = 3439292824964800537L;
    /** 余额单元代码 */
    protected String balanceUnitCode;
    /** 节点类别 */
    protected String nodeTyp;
    /** 发生金额 */
    protected BigDecimal occurrAmount;
    /** 起息日期 */
    protected String interestStartDate;
    /** 上次结息日 */
    protected String prevInterestProcesseDate;
    /** 结息日期 */
    protected String settleDate;
    /** 计息天数 */
    protected int calIntDays;
    /** 利率 */
    protected BigDecimal rate;
    /** 利息金额 */
    protected BigDecimal interestAmount;
    /**
     * @return the balanceUnitCode
     */
    public String getBalanceUnitCode() {
        return balanceUnitCode;
    }
    /**
     * @param balanceUnitCode the balanceUnitCode to set
     */
    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }
    /**
     * @return the nodeTyp
     */
    public String getNodeTyp() {
        return nodeTyp;
    }
    /**
     * @param nodeTyp the nodeTyp to set
     */
    public void setNodeTyp(String nodeTyp) {
        this.nodeTyp = nodeTyp;
    }
    /**
     * @return the occurrAmount
     */
    public BigDecimal getOccurrAmount() {
        return occurrAmount;
    }
    /**
     * @param occurrAmount the occurrAmount to set
     */
    public void setOccurrAmount(BigDecimal occurrAmount) {
        this.occurrAmount = occurrAmount;
    }
    /**
     * @return the interestStartDate
     */
    public String getInterestStartDate() {
        return interestStartDate;
    }
    /**
     * @param interestStartDate the interestStartDate to set
     */
    public void setInterestStartDate(String interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    /**
     * @return the settleDate
     */
    public String getSettleDate() {
        return settleDate;
    }
    /**
     * @param settleDate the settleDate to set
     */
    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }
    /**
     * @return the calIntDays
     */
    public int getCalIntDays() {
        return calIntDays;
    }
    /**
     * @param calIntDays the calIntDays to set
     */
    public void setCalIntDays(int calIntDays) {
        this.calIntDays = calIntDays;
    }
    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }
    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    /**
     * @return the interestAmount
     */
    public BigDecimal getInterestAmount() {
        return interestAmount;
    }
    /**
     * @param interestAmount the interestAmount to set
     */
    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }
    
    /**
     * @return the prevInterestProcesseDate
     */
    public String getPrevInterestProcesseDate() {
        return prevInterestProcesseDate;
    }
    /**
     * @param prevInterestProcesseDate the prevInterestProcesseDate to set
     */
    public void setPrevInterestProcesseDate(String prevInterestProcesseDate) {
        this.prevInterestProcesseDate = prevInterestProcesseDate;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "X5800VO [balanceUnitCode=" + balanceUnitCode + ", nodeTyp=" + nodeTyp + ", occurrAmount=" + occurrAmount
                + ", interestStartDate=" + interestStartDate + ", prevInterestProcesseDate=" + prevInterestProcesseDate
                + ", settleDate=" + settleDate + ", calIntDays=" + calIntDays + ", rate=" + rate + ", interestAmount="
                + interestAmount + "]";
    }
    
}
