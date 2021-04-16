package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5775BO;
/**
 * MC拒付维护
 * 
 * @ClassName X5785Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月20日 上午10:25:36
 * @version 1.0.0
 */
public interface X5785Bus {
	/**
	 * MC拒付维护
	 * 
	 * @param x5775bo
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5775BO x5775bo) throws Exception;

}