package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4080BO;

/**
 * <p> Title: X4080Bus </p>
 * <p> Description: 会计接口文件查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author yanyingzhao
 * @since 2019年6月20日
 */
public interface X4080Bus {
    /**
     * 会计接口文件查询
     *
     * @param X4080BO
     * @return
     */
    public Object busExecute(X4080BO x4080bo) throws Exception;
    
    public Object requestQueryList(X4080BO x4080bo) throws Exception;
}
