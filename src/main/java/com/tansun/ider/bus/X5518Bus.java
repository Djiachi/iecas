package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5115BO;

public interface X5518Bus {

	/**
	 * 客户延滞状况查询
	 * @param x5515BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5115BO x5115BO) throws Exception;
	
}
