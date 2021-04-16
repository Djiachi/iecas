package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X9598BO;

/**
 * 分期资产证券化未抛本金处理
 */
public interface X9598Bus {

    public Object busExecute(X9598BO x9598BO) throws Exception;

}
