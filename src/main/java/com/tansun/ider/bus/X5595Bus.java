package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5580BO;

public interface X5595Bus {
	/**
	 * 删除客户个性化元件
	 * 
	 * @param x5580bo
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5580BO x5580Bo) throws Exception;

}