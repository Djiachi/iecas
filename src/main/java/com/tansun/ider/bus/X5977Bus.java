package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5631BO;

public interface X5977Bus {
    /**
     * 分期账户子账户信息查询
     *
     * @param x5631BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5631BO x5631BO) throws Exception;
}
