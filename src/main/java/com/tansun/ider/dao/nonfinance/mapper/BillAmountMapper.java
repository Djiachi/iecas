package com.tansun.ider.dao.nonfinance.mapper;

import java.util.List;

import com.tansun.ider.model.bo.X5600BO;
import com.tansun.ider.model.vo.X5600VO;

/**
 * BusinessTypeBillSummaryMapper
 * 
 * @author huangyayun
 * @version V1.0
 */
public interface BillAmountMapper {


    /**
     * 根据客户号查询最多账单分期金额
     * 
     * @param X5600BO
     * @return
     * @throws Exception
     */
    public List<X5600VO> selectSumAmt(X5600BO x5600BO) throws Exception;
}