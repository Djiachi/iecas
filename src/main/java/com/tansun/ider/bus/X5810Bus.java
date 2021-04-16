package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5810BO;

/**
 * 
 * @ClassName:  X5810Bus   
 * @Description:客户产品注销   
 * @author: wangxi
 * @date:   2019年4月22日 下午3:34:19   
 *
 */
public interface X5810Bus {

	/**
	 * 
	 * @Title: busExecute   
	 * @Description: 客户产品注销   
	 * @param: @param x5000BO
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Object      
	 * @throws
	 */
	public Object busExecute(X5810BO x5810BO) throws Exception;
	
}
