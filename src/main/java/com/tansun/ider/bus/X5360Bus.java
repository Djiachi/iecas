package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5360BO;

public interface X5360Bus {

	/**
	 * 
	* @Description: 更新媒介有效期
	* @param x5300BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5360BO x5360BO) throws Exception;
	
}
