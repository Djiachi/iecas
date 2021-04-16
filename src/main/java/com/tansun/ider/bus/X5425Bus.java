package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5425BO;

public interface X5425Bus {

	
	/**
	 * @Description: 查询产品形式
	 * @param x5425BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5425BO x5425BO) throws Exception;
	
}
