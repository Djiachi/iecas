package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5075BO;

public interface X5075Bus {

	/**
	 * 
	* @Description: 转卡媒介资料查询
	* @param x5060BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5075BO x5075BO) throws Exception;
	
}
