package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4070BO;

/**
 * <p> Title: X4070Bus </p>
 * <p> Description: 预算单位已出账单查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月24日
 */
public interface X4070Bus {
    /**
     * 预算单位已出账单查询
     *
     * @param x4030BO
     * @return
     */
    public Object busExecute(X4070BO x4070BO) throws Exception;
}
