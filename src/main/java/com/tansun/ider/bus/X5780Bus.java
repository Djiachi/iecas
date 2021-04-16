package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5775BO;
/**
 * MC拒付查询
 * 
 * @ClassName X5780Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月20日 下午5:09:37
 * @version 1.0.0
 */
public interface X5780Bus {
	/**
	 * MC拒付查询
	 * 
	 * @param X5775BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5775BO x5775bo) throws Exception;

}