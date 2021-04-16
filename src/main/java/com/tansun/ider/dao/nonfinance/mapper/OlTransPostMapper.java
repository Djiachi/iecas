package com.tansun.ider.dao.nonfinance.mapper;

import java.util.List;
import com.tansun.ider.model.bo.X5965BO;
import com.tansun.ider.model.vo.X5965VO;

/**
 * 交易入账查询
 * @author qianyp
 */
public interface OlTransPostMapper {


    /**
     * 交易入账查询
     */
    public List<X5965VO> selectOlTransPosts(X5965BO x5965bo) throws Exception;
    
    public Integer selectOlTransPostConut(X5965BO x5965bo) throws Exception;
}