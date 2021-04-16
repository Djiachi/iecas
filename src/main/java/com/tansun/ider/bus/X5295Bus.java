package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5295BO;

public interface X5295Bus {
	
	/**
	 * 
	* @Description: 实时制卡处理
	* @param x5295BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5295BO x5295BO) throws Exception;
	
}
