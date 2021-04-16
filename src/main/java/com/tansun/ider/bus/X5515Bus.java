package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5515BO;

public interface X5515Bus {

	/**
	 * 客户延滞状况查询
	 * @param x5515BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5515BO x5515BO) throws Exception;
	
}
