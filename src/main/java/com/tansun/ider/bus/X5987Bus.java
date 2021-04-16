package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5975BO;

/**
 * @Author: PanQi
 * @Date: 2020/05/12
 * @updater:
 * @description: 资产证券化查询 循环账户
 */
public interface X5987Bus {

	/**
	 * 资产选择账户查询
	 * @param x5975BO
	 * @return
	 * @throws Exception
	 */
	Object busExecute(X5975BO x5975BO) throws Exception;
	
}
