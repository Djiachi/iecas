package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5625BO;

public interface X5625Bus {
	/**
     * 查询账单日
     * 
     * @param x5625BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5625BO x5625BO) throws Exception;
}
