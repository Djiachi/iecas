package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5720BO;

public interface X5710Bus {

    /**
     * MC调单申请查询
     * @Description (TODO这里用一句话描述这个方法的作用)
     * @param x5720BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5720BO x5720BO) throws Exception;

}
