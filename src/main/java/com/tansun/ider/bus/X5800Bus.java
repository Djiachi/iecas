package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5800BO;

public interface X5800Bus {

	/**
	 * 提前续卡
	 * @param x5800BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5800BO x5800BO) throws Exception;
}
