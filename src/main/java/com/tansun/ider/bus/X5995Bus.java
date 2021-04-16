package com.tansun.ider.bus;


import com.tansun.ider.model.bo.X5995BO;

/**
 * 
* @ClassName: X5995Bus 
* @Description: 强制结息（产品注销时实时结息）
* @author by
* @date 2019年8月20日 下午12:22:35 
*
 */
public interface X5995Bus {

	public Object busExecute(X5995BO x5995BO) throws Exception;
	
}
