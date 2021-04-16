package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5655BO;

public interface X5655Bus {
	
    /**
     * 生成制卡记录
     * @param x5655BO
     * @return
     * @throws Exception
     */
    public Object busExecute(X5655BO x5655BO) throws Exception;

}
