package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5300BO;

public interface X5300Bus {
	
	/**
	 * 
	* @Description: 更新媒介制卡信息
	* @param x5300BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5300BO x5300BO) throws Exception;
	
}
