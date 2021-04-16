package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5130BO;

public interface X5130Bus {

	/**
	 * 
	* @Description: 媒介资料查询
	* @param x5125BO
	* @return
	* @throws Exception
	 */
    public Object busExecute(X5130BO x5130BO) throws Exception;
	
}
