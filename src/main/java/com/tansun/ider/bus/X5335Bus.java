package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5330BO;

public interface X5335Bus {
    /**
     * 异常交易操作
     * 
     * @param x5330BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5330BO x5330BO) throws Exception;

}
