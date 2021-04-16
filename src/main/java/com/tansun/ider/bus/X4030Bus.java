package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4030BO;

/**
 * <p> Title: X4030Bus </p>
 * <p> Description: 公务卡授信</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public interface X4030Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4030BO
     * @return
     */
    public Object busExecute(X4030BO x4030BO) throws Exception;
}
