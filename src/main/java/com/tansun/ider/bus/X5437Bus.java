package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5437BO;

public interface X5437Bus {

	/**
	 * 可分期账单查询
	 * @param x5437BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5437BO x5437BO) throws Exception;

}
