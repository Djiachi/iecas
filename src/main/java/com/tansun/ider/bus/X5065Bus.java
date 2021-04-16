package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5065BO;

public interface X5065Bus {
	
	/**
	 * 
	* @Description: 媒介激活
	* @param x5060BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5065BO x5065BO) throws Exception;

}
