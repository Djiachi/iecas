package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4035BO;

/**
 * <p> Title: X4030Bus </p>
 * <p> Description: 公务卡额度调整</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public interface X4035Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4030BO
     * @return
     */
    public Object busExecute(X4035BO x4035BO) throws Exception;
}
