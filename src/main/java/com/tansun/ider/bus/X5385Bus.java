package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5385BO;

/**
 * 账单产品对象维度查询
 * 
 * @author huangyayun
 * @date 2018年10月18日
 */
public interface X5385Bus {

    /**
     * 账单产品对象维度查询
     * 
     * @param x5385BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5385BO x5385BO) throws Exception;
}
