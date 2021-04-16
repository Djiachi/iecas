package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5645BO;

public interface X5645Bus {

    /**
     * 最近一期未支付计划信息查询
     * @param x5645BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5645BO x5645BO) throws Exception;

}
