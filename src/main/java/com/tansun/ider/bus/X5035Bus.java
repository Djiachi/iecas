package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5035BO;

public interface X5035Bus {
	
	/**
	 * 产品单元基本信息新建
	 * 
	 * @param CRD.AD.01.0002
	 * @return 
	 */
	public Object busExecute(X5035BO  x5035BO) throws Exception;

}
