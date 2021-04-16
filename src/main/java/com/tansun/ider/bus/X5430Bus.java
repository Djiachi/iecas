package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5430BO;

public interface X5430Bus {

	/**
	 * 
	* @Description: 客户定价标签查询
	* @param x5430BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5430BO x5430BO) throws Exception;
	
}
