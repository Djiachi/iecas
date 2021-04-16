package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4001BO;

/**
 * <p> Title: X4001Bus </p>
 * <p> Description: 新增预算单位附件信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月23日
 */
public interface X4001Bus {
    /**
     * 预算单位附件信息新增
     *
     * @param x4001BO
     * @return
     */
    public Object busExecute(X4001BO x4001BO) throws Exception;
}
