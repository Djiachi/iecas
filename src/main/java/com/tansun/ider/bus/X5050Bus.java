package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5050BO;

/**
 * @Desc:客户封锁码列表查询
 * @Author wt
 * @Date 2018年5月4日 上午9:28:14
 */
public interface X5050Bus {

	/**
	 * 创建媒介单元资料信息
	 * 
	 * @param cRDAD010003
	 * @return
	 * @throws Exception
	 */
	public Object busExecute(X5050BO x5050BO) throws Exception;
}
