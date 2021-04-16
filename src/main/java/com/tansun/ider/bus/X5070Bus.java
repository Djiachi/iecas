package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5070BO;

public interface X5070Bus {
	
	/**
	 * 
	* @Description: 转卡列表查询
	* @param x5060BO
	* @return
	* @throws Exception
	 */
	public Object busExecute(X5070BO x5070BO) throws Exception;
	
}
