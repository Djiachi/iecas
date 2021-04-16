/**  

* @Title: X5396ListVO.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年5月20日  

* @version R04.00 

*/  
package com.tansun.ider.model.vo;


/**  

* @ClassName: X5396ListVO  

* @Function:

* @Description:  

* @author baiyu

* @date 2019年5月20日  

* @version R04.00 

*/
public class X5396DateVO {
   
    /**开始日期*/
    private String startDate;
    /**结束日期*/
    private String endDate;
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public X5396DateVO() {
        
    }
    public X5396DateVO(String startDate, String endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    
}
