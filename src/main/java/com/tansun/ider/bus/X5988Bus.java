package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5630BO;

/**
 * @Author: PanQi
 * @Date: 2020/05/12
 * @updater:
 * @description: 资产证券化查询 分期账户
 *
 */
public interface X5988Bus {
    /**
     * 分期账户信息查询
     *
     * @param x5630BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5630BO x5630BO) throws Exception;
}
