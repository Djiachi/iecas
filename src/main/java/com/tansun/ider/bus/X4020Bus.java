package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4020BO;

/**
 * <p> Title: X4020Bus </p>
 * <p> Description: 处理预算单位片区号和客户的不同时，保存预算单位单位员工关系信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public interface X4020Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4010BO
     * @return
     */
    public Object busExecute(X4020BO x4020BO) throws Exception;
}
