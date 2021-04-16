package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4075BO;

/**
 * <p> Title: X4070Bus </p>
 * <p> Description: 通过gns查询产品对象账单摘要信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月24日
 */
public interface X4075Bus {
    /**
     * 通过gns查询产品对象账单摘要信息
     *
     * @param x4075BO
     * @return
     */
    public Object busExecute(X4075BO x4075BO) throws Exception;
}
