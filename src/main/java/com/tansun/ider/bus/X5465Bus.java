package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5465BO;

public interface X5465Bus {

	/**
	 * 客户延滞状况 维护查询
	 * @param x5465BO
	 * @return
	 * @throws Exception
	 */
	Object busExecute(X5465BO x5465BO) throws Exception;
	
}
