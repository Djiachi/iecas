package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5030BO;

public interface X5030Bus {
	
	/**
	 * 产品单元基本信息新建
	 * 
	 * @param CRD.AD.01.0002
	 * @return
	 */
	public Object busExecute(X5030BO  x5030BO) throws Exception;
	
}
