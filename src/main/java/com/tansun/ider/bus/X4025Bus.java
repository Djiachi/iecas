package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4025BO;

/**
 * <p> Title: X4025Bus </p>
 * <p> Description: 预算单位与员工客户关联关系建立</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月11日
 */
public interface X4025Bus {
    /**
     * 预算单位与员工客户关联关系建立
     *
     * @param x4010BO
     * @return
     */
    public Object busExecute(X4025BO x4025BO) throws Exception;
}
