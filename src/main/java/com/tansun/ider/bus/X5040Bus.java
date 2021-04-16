package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5040BO;

public interface X5040Bus {

	/**
	 * 产品单元基本信息新建
	 * 
	 * @param CRD.AD.01.0002
	 * @return 
	 */
	public Object busExecute(X5040BO  x5040BO) throws Exception;
	
}
