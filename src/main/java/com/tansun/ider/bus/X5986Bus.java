package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5980BO;

/**
 * @Author: PanQi
 * @Date: 2020/04/10
 * @updater:
 * @description: 资产证券化查询-子账户明细查询
 */
public interface X5986Bus {

    /** 资产证券化查询
     * @param x5980BO
     * @return
     * @throws Exception
     */
    Object busExecute(X5980BO x5980BO) throws Exception;

}
