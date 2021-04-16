package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4045BO;

/**
 * <p> Title: X4045Bus </p>
 * <p> Description: 预算单位信息查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public interface X4045Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4030BO
     * @return
     */
    public Object busExecute(X4045BO x4045BO) throws Exception;
}
