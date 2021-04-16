package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5395BO;

/**
 * 账单查询-交易明细查询
 * 
 * @author huangyayun
 * @date 2018年10月18日
 */
public interface X5395Bus {

    /**
     * 账单查询-交易明细查询
     * 
     * @param x5395BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5395BO x5395BO) throws Exception;
}
