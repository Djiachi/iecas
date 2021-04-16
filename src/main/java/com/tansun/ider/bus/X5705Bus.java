package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5705BO;
/**
 * 
 * @ClassName X5705Bus
 * @Description TODO(VISA调单申请建立)
 * @author yanyingzhao
 * @Date 2019年1月253日 下午5:36:09
 * @version 1.0.0
 */
public interface X5705Bus {
	/**
	 * VISA调单申请建立
	 * 
	 * @param X5705BO
	 * @return
	 * @throws Exception
	 */
	public String busExecute(X5705BO x5705bo) throws Exception;
	
}