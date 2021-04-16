package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5320BO;

public interface X5320Bus {

	public Object busExecute(X5320BO x5320BO) throws Exception;
	
	public Object queryCustomerBillDay(X5320BO x5320BO) throws Exception;
	
}
