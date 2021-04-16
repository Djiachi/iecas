package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4040BO;

/**
 * <p> Title: X4040Bus </p>
 * <p> Description: 个人公务卡最大额度查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public interface X4040Bus {
    /**
     * 预算单位附件信息维护
     *
     * @param x4030BO
     * @return
     */
    public Object busExecute(X4040BO x4040BO) throws Exception;
}
