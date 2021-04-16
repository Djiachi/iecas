package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5760BO;

/**
 * VISA拒付查询
 * 
 * @ClassName X5765Bus
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月15日 下午5:09:37
 * @version 1.0.0
 */
public interface X5765Bus {
	/**
	 * VISA拒付查询
	 * 
	 * @param X5760BO
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5760BO x5760bo) throws Exception;

}
