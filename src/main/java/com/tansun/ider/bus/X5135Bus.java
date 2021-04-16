package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5135BO;

public interface X5135Bus {

	/**
	 * 
	* @Description: 媒介资料查询
	* @param x5125BO
	* @return
	* @throws Exception
	 */
    public Object busExecute(X5135BO x5135BO) throws Exception;
	
}
