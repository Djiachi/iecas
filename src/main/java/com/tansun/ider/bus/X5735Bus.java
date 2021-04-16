package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5735BO;

public interface X5735Bus {
	
    /**
     * 	附加表信息查询	
     * 
     * @param X5735BO
     * @return
     * @throws Exception
     */
	 public Object busExecute(X5735BO x5735BO) throws Exception;

}

