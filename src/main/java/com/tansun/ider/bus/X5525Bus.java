package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5525BO;

public interface X5525Bus {

    /**
     * 发生额节点查询
     * 
     * @param x5525BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5525BO x5525BO) throws Exception;

}
