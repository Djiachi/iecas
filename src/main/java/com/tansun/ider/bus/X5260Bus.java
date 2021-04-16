/**
 * 
 */
package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5260BO;

/**
 * @Desc:统一利率
 * @Author yuanyanjiao
 * @Date 2018年9月28日 上午9:57:18
 */
public interface X5260Bus {
    
    /**
     * 
     * @param cRDIQ010001
     * @return
     * @throws Exception
     */
    public Object busExecute(X5260BO x5260BO) throws Exception;
}
