package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5705BO;
/**
 * VISA调单申请维护
 * 
 * @ClassName X5730Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年1月26日 上午10:25:36
 * @version 1.0.0
 */
public interface X5730Bus {
	/**
	 * VISA调单申请维护
	 * 
	 * @param x5705bo
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5705BO x5705bo) throws Exception;
}
