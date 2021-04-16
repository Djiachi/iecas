package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5010BO;

public interface X5010Bus {

	/**
	 * 创建客户新增
	 * 
	 * @param CRDAD010001
	 * @return
	 */
	public Object busExecute(X5010BO x5010BO) throws Exception;
}
