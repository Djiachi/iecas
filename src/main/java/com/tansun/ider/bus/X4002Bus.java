package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X4002BO;
/**
 * 
 * @Description:TODO(报销标识)   
 * @author: sunyaoyao
 * @date:   2019年5月9日 上午9:46:03   
 *
 */
public interface X4002Bus {
	/**
	 * 交易标识
	 * 
	 * @param cRDAD010001
	 * @return
	 */
	public Object busExecute(X4002BO x4005bo) throws Exception;
}
