package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5650BO;

public interface X5650Bus {
    /**
     * 消贷分期账户信息查询
     *
     * @param x5650BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5650BO x5650BO) throws Exception;
}
