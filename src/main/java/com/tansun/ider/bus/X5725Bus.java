package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5705BO;

/**
 * VISA调单申请查询
 * 
 * @ClassName X5725Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年1月26日 下午5:09:37
 * @version 1.0.0
 */

public interface X5725Bus {
	/**
	 * VISA调单申请查询
	 * 
	 * @param X5705BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5705BO x5705bo) throws Exception;

}
