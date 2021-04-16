package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5020BO;

public interface X5020Bus {

	/**
	 * 创建客户新增
	 * 
	 * @param CRDAD010001
	 * @return
	 */
	public Object busExecute(X5020BO x5020BO) throws Exception;
	
}
