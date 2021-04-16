package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5975BO;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询
 */
public interface X5975Bus {

	/**
	 * 资产选择账户查询
	 * @param x5975BO
	 * @return
	 * @throws Exception
	 */
	Object busExecute(X5975BO x5975BO) throws Exception;
	
}
