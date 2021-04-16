package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5610BO;

public interface X5610Bus {

    /**
     * 分期账户信息查询
     * 
     * @param x5610BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5610BO x5610BO) throws Exception;

}
