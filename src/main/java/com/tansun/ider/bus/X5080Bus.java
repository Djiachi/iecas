package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5080BO;

public interface X5080Bus {

	/**
	 * 
	* @Description: 转卡处理
	* @param x5080BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5080BO x5080BO) throws Exception;
	
}
