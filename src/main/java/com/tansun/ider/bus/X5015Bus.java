package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5015BO;

public interface X5015Bus {

	/**
	 * 创建客户新增
	 * 
	 * @param CRDAD010001
	 * @return
	 */
	public Object busExecute(X5015BO x5015BO) throws Exception;
	
}
