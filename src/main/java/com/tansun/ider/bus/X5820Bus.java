/**  

* @Title: X5820Bus.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/  
package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5820BO;

/**  

* @ClassName: X5820Bus  

* @Function: 统一通知流水表登记

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/
public interface X5820Bus {
    
    /**
     * 
     * @MethodName: busExecute
     * @param x5810BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5820BO x5820BO) throws Exception;

}
