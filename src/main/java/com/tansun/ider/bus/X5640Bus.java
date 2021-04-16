package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5640BO;

public interface X5640Bus {

    /**
     * 分期账户信息+支付计划信息查询
     * @param x5640BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5640BO x5640BO) throws Exception;

}
