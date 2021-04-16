package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5390BO;

/**
 * 账单产品对象+业务类型维度查询
 * 
 * @author huangyayun
 * @date 2018年10月18日
 */
public interface X5390Bus {

    /**
     * 账单产品对象+业务类型维度查询
     * 
     * @param x5390BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5390BO x5390BO) throws Exception;
}
