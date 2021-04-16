package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5600BO;

public interface X5600Bus {

    /**
     * 统计分期金额
     * 
     * @param x5600BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5600BO x5600BO) throws Exception;

}
