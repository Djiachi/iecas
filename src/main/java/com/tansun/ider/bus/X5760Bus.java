package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5760BO;
/**
 * 
 * @ClassName X5760Bus
 * @Description TODO(VISA拒付建立)
 * @author yanyingzhao
 * @Date 2019年2月14日 下午5:36:09
 * @version 1.0.0
 */
public interface X5760Bus {
	/**
	 * VISA拒付建立
	 * 
	 * @param X5760BO
	 * @return
	 * @throws Exception
	 */
	public String busExecute(X5760BO x5760bo) throws Exception;
	
}