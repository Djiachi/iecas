package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5805BO;
/**
 * 用户长期未使用上封锁码
 * @author sunyaoyao
 *
 */
public interface X5805Bus {
	public Object busExecute(X5805BO x5805bo) throws Exception;
}
