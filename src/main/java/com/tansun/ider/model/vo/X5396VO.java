/**  

* @Title: X5396VO.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年4月30日  

* @version R04.00 

*/
package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.framwork.commun.PageBean;

/**
 * 
 * @author baiyu
 * 
 * @date 2019年4月30日
 * 
 * @version R04.00
 * 
 */
public class X5396VO {
    /** 货币代码 */
    private String currencyCode;
    /** 汇总金额 */
    private BigDecimal actualPostingAmount;
    /** 货币名称 */
    private String currencyName;

    public X5396VO() {

    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getActualPostingAmount() {
        return actualPostingAmount;
    }

    public void setActualPostingAmount(BigDecimal actualPostingAmount) {
        this.actualPostingAmount = actualPostingAmount;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}
