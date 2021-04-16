package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5811BO;
/**
 * 客户产品二次注销事件
 * @author yanyingzhao
 *
 */
public interface X5811Bus {
	
	public Object busExecute(X5811BO x5811bo) throws Exception;
}
