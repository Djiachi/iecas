/**
 * 
 */
package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5055BO;

/**
 * @Desc:去除封锁码查询
 * @Author wt
 * @Date 2018年5月15日 上午9:57:18
 */
public interface X5055Bus {
    
    /**
     * 
     * @param cRDIQ010001
     * @return
     * @throws Exception
     */
    public Object busExecute(X5055BO x5055BO) throws Exception;
    
    /**
     * 
    *
    * @MethodName createExecute 
    * @Description: 目前批量生成预制卡时使用
    * @param x5055BO
    * @return  
    * @return: int
     * @throws Exception 
     */
    public int createExecute(X5055BO x5055BO) throws Exception;
}
