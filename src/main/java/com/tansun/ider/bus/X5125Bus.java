package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5125BO;

public interface X5125Bus {

	/**
	 * 
	* @Description: 媒介资料查询
	* @param x5125BO
	* @return
	* @throws Exception
	 */
    public Object busExecute(X5125BO x5125BO) throws Exception;
	
}
