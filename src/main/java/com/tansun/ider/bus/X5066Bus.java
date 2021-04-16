package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5065BO;

public interface X5066Bus {
	
	/**
	 * 
	* @Description: 客户额度调整
	* @param x5060BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5065BO x5065BO) throws Exception;

}
