/**  

* @Title: X5396Bus.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年4月30日  

* @version R04.00 

*/
package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5396BO;

/**
 * 
 * @ClassName: X5396Bus
 * 
 * @Function: 未出账单查询
 * 
 * @Description:
 * 
 * @author baiyu
 * 
 * @date 2019年4月30日
 * 
 * @version R04.00
 * 
 */
public interface X5396Bus {
    /**
     * 
     * @MethodName: busX5396
     * 
     * @Description: 未出账单查询
     * 
     * @param x5396BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5396BO x5396BO) throws Exception;

}
