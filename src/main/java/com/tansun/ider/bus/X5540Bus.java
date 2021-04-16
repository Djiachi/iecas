package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5540BO;

public interface X5540Bus {

    /**
     * 费用免除信息查询
     * 
     * @param x5540BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5540BO x5540BO) throws Exception;

}
