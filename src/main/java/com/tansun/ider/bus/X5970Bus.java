package com.tansun.ider.bus;
import com.tansun.ider.model.bo.X5970BO;

/**
 * 交易级分配情况
 * @author qianyp
 */

public interface X5970Bus {
    
    public Object busExecute(X5970BO x5970bo) throws Exception;

}
