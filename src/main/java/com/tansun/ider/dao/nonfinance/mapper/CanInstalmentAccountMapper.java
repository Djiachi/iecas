package com.tansun.ider.dao.nonfinance.mapper;

import java.util.List;

import com.tansun.ider.model.bo.X5438BO;
import com.tansun.ider.model.vo.X5438VO;

/**
 * 可交易分期账户查询
 * @author liuyanxi
 *
 */
public interface CanInstalmentAccountMapper {


    /**
     * 可分期账户查询
     * @param x5438BO
     * @return
     * @throws Exception
     */
    public List<X5438VO> selectCanInstalmentAccount(X5438BO x5438BO) throws Exception;
    
    /**
     * 可分期账户统计
     * @param x5438BO
     * @return
     * @throws Exception
     */
    public Integer countCanInstalmentAccount(X5438BO x5438BO) throws Exception;
}