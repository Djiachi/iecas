package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5000BO;

/**
 * 
 * ClassName:X5000Service 
 * desc:个人客户新增创建
 * 
 * @author wt
 * @date 2018年4月10日 下午2:16:19
 * 
 */
public interface X5000Bus {

	/**
	 * 创建客户新增
	 * 
	 * @param cRDAD010001
	 * @return
	 */
	public Object busExecute(X5000BO x5000BO) throws Exception;

}
