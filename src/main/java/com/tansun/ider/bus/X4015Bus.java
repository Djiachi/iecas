package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4015BO;

/**
 * <p> Title: X4015Bus </p>
 * <p> Description:查询个人公务卡最大限额</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
public interface X4015Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4015BO
     * @return
     */
    public Object busExecute(X4015BO x4015BO) throws Exception;
}
