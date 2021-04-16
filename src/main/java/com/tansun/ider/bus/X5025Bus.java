package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5025BO;

public interface X5025Bus {
	
	/**
	 * 创建客户个人信息
	 * 
	 * @param CRDAD010001
	 * @return
	 */
	public Object busExecute(X5025BO  x5025BO) throws Exception;

}
