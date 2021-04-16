package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5551BO;

public interface X5551Bus {
    /**
     * 存款账户信息查询
     *
     * @param x5551BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5551BO x5551BO) throws Exception;
}
