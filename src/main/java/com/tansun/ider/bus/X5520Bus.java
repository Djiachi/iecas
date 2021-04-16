package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5520BO;

public interface X5520Bus {

	/**
	 * 账户周期金融信息查询
	 * @param x5520BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5520BO x5520BO) throws Exception;
	
}
