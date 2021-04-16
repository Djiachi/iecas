package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5760BO;

/**
 * VISA拒付维护
 * 
 * @ClassName X5770Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月15日 上午10:25:36
 * @version 1.0.0
 */
public interface X5770Bus {
	/**
	 * VISA拒付维护
	 * 
	 * @param x5760bo
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5760BO x5760bo) throws Exception;

}