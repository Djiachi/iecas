package com.tansun.ider.dao.nonfinance.mapper;

import java.util.List;

import com.tansun.ider.model.bo.X5340BO;
import com.tansun.ider.model.vo.X5340VO;

/**
 * DisputeAccountMapper
 * 
 * @author huangyayun
 * @version V1.0
 */
public interface DisputeAccountMapper {


    /**
     * 查询争议账户
     * 
     * @param X5340BO
     * @return
     * @throws Exception
     */
    public List<X5340VO> selectDisputeAccount(X5340BO x5340BO) throws Exception;
    public Integer selectDisputeAccountConut(X5340BO x5340BO) throws Exception;
}