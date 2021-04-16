package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5775BO;

/**
 * 
 * @ClassName X5775Bus
 * @Description TODO(MC拒付建立)
 * @author yanyingzhao
 * @Date 2019年2月20日 下午5:36:09
 * @version 1.0.0
 */

public interface X5775Bus {
	/**
	 * MC拒付建立
	 * 
	 * @param X5775BO
	 * @return
	 * @throws Exception
	 */
	public String busExecute(X5775BO x5775bo) throws Exception;

}