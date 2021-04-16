package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5271BO;

/**
 * 关联套卡查询
 * @author wangxi
 *
 */
public interface X5271Bus {
    /**
     * 关联套卡查询
     * @param x5271BO
     * @return
     */
    public Object busExecute(X5271BO x5271BO) throws Exception;
}
