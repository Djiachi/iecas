package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5980BO;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择+封包
 */
public interface X5980Bus {

    /** 资产选择+封包
     * @param x5980BO
     * @return
     * @throws Exception
     */
    Object busExecute(X5980BO x5980BO) throws Exception;

}
