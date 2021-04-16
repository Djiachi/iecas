package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5420BO;

public interface X5420Bus {

	/**
	 * @Description: 查询产品形式
	 * @param x5420BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5420BO x5420BO) throws Exception;
	
}
