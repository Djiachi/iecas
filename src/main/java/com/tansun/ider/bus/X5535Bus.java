package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5535BO;

public interface X5535Bus {

    /**
     * 计息控制链表查询
     * 
     * @param x5535BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5535BO x5535BO) throws Exception;

    public Object executeForInterest(X5535BO x5535bo) throws Exception;

}
