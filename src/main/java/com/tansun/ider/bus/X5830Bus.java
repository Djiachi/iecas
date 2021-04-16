package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5830BO;

/**
 * 产品升降级
 * @author wangxi
 *
 */
public interface X5830Bus {
    /**
     * 产品升降级
     * @param x5830BO
     * @return
     */
    public Object busExecute(X5830BO x5830BO) throws Exception;
}
