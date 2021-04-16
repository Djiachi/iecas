package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5545BO;

public interface X5545Bus {

    /**
     * 客户交易统计息查询
     * 
     * @param x5545BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5545BO x5545BO) throws Exception;

}
