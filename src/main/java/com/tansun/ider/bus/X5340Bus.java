package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5340BO;

/**
 * 争议账户信息查询
 * 
 * @author huangyayun
 * @date 2018年10月18日
 */
public interface X5340Bus {

    /**
     * 账单产品对象维度查询
     * 
     * @param x5340BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5340BO x5340BO) throws Exception;
}
