package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5310BO;

/**
 * 判断该产品对应的业务项目是否需要输入账单日
 * @author wt
 *
 */
public interface X5310Bus {

	 public Object busExecute(X5310BO x5310BO) throws Exception;
	
}
