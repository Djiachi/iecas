package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5060BO;

public interface X5060Bus {
	
	 /**
     * @param CRD.AD.01.0003
     * @return
     * @throws Exception
     */
    public Object busExecute(X5060BO x5060BO) throws Exception;
	
}
