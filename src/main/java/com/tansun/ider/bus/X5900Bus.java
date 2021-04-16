package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5900BO;

public interface X5900Bus {

	/**
	 * 
	* @Description: 切日
	* @param x5900BO
	* @return
	 */
    public Object busExecute(X5900BO x5900BO) throws Exception;
	
}
