package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5430BO;

public interface X5431Bus {

	/**
	 * 客户定价视图查询
	 * @param x5430BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5430BO x5430BO) throws Exception;
	
	/**
	 * 客户实例代码查询
	 * @param x5430BO
	 * @return
	 * @throws Exception
	 */
	public Object instanBusExecute(X5430BO x5430BO) throws Exception;
	
	/**
	 * 查询是否含有定价标签
	 * @param x5430bo
	 * @return
	 * @throws Exception
	 */
	public Object pricingBusExecute(X5430BO x5430bo) throws Exception;
	
}
