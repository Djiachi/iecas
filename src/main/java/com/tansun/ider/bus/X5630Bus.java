package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5630BO;

public interface X5630Bus {
    /**
     * 分期账户信息查询
     *
     * @param x5610BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5630BO x5630BO) throws Exception;
}
