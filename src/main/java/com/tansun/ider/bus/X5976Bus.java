package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5630BO;

/**
 * @Author: PanQi
 * @Date: 2020/04/09
 * @updater:
 * @description: 资产选择账户查询-交易账户
 */
public interface X5976Bus {
    /**
     * 分期账户信息查询
     *
     * @param x5630BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5630BO x5630BO) throws Exception;
}
