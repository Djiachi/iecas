package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5615BO;

public interface X5615Bus {

    /**
     * 分期账户信息+分期计划信息查询
     * 
     * @param x5615BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5615BO x5615BO) throws Exception;

}
