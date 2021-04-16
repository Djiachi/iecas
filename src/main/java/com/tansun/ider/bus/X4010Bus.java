package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4010BO;

/**
 * <p> Title: X4010Bus </p>
 * <p> Description: 预算单位附件信息维护</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
public interface X4010Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4010BO
     * @return
     */
    public Object busExecute(X4010BO x4010BO) throws Exception;
}
