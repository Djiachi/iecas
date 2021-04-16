package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5005BO;

/**
 * @version:1.0
* @Description: 客户序号规则表活动设计
* @author: wt  
* @date: date{time}
 */
public interface X5005Bus {

	/**
	 * 创建客户新增
	 * 
	 * @param CRDAD010001
	 * @return
	 */
	public Object busExecute(X5005BO x5005BO) throws Exception;
	
}
