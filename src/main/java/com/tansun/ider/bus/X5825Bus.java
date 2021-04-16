package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5825BO;

/**
 * 媒介升降级
 * @author wangxi
 *
 */
public interface X5825Bus {
    /**
     * 媒介升降级
     * @param x5271BO
     * @return
     */
    public Object busExecute(X5825BO x5825BO) throws Exception;
}
