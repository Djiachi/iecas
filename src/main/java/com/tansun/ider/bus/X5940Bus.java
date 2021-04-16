package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5940BO;

public interface X5940Bus {

	/**
	 * @Description: 交易二次识别参数删除
	 * @param x5940BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5940BO x5940BO) throws Exception;

}
