package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5550BO;

public interface X5550Bus {

    /**
     * 存款账户信息查询
     * 
     * @param x5550BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5550BO x5550BO) throws Exception;
	
}
