package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5900BO;

public interface X5901Bus {

	/**
	 * 非金融历史生成
	 * @param x5901BO
	 * @return
	 * @throws Exception
	 */
    public Object busExecute(X5900BO x5900BO) throws Exception;
	
}
