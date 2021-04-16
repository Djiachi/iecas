package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5965Bus;
import com.tansun.ider.dao.nonfinance.mapper.OlTransPostMapper;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5965BO;
import com.tansun.ider.model.vo.X5965VO;
import com.tansun.ider.service.QueryCustomerService;

@Service
public class X5965BusImpl implements X5965Bus {
    
    @Autowired
    private OlTransPostMapper olTransPostMapper;
    
    @Autowired
    private QueryCustomerService queryCustomerService;
    
    public Object busExecute(X5965BO x5965bo) throws Exception {
        String customerNo = queryCustomerService.queryCoreMediaBasicInfo(x5965bo.getIdNumber(),null);
        if (StringUtil.isNotEmpty(customerNo)){
            x5965bo.setCustomerNo(customerNo);
        }
        PageBean<X5965VO> page = new PageBean<>();
        if (null != x5965bo.getPageSize() && null != x5965bo.getIndexNo()) {
            x5965bo.setPageSize(x5965bo.getPageSize());
            x5965bo.setIndexNo(x5965bo.getIndexNo());
            page.setPageSize(x5965bo.getPageSize());
            page.setIndexNo(x5965bo.getIndexNo());
        }
        int totalCount = olTransPostMapper.selectOlTransPostConut(x5965bo);
        page.setTotalCount(totalCount);
        if (totalCount > 0) {
            List<X5965VO> x5965VOList = olTransPostMapper.selectOlTransPosts(x5965bo);
            for (X5965VO x5965vo : x5965VOList) {
                // 金额转换
                amountConversion(x5965vo);
            }
            page.setRows(x5965VOList);
        }
        return page;
    }
    
    /**
     * 金额转换
     */
    private void amountConversion(X5965VO x5965vo){
        if(x5965vo.getTransAmount()!=null && x5965vo.getTransCurrDecimal()!=null){//交易金额
            BigDecimal disputeAmount = CurrencyConversionUtil.reduce(x5965vo.getTransAmount(),x5965vo.getTransCurrDecimal());
            x5965vo.setTransAmount(disputeAmount);
        }
        if(x5965vo.getCurrPrincipalBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额当期本金入账金额
            BigDecimal currPrincipalBalance = CurrencyConversionUtil.reduce(x5965vo.getCurrPrincipalBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setCurrPrincipalBalance(currPrincipalBalance);
        }
        if(x5965vo.getBillPrincipalBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额账单本金入账金额
            BigDecimal billPrincipalBalance = CurrencyConversionUtil.reduce(x5965vo.getBillPrincipalBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setBillPrincipalBalance(billPrincipalBalance);
        }
        if(x5965vo.getCurrInterestBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额当期利息入账金额
            BigDecimal currInterestBalance = CurrencyConversionUtil.reduce(x5965vo.getCurrInterestBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setCurrInterestBalance(currInterestBalance);
        }
        if(x5965vo.getBillInterestBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额账单利息入账金额
            BigDecimal billInterestBalance = CurrencyConversionUtil.reduce(x5965vo.getBillInterestBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setBillInterestBalance(billInterestBalance);
        }
        if(x5965vo.getCurrCostBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额当期费用入账金额
            BigDecimal currCostBalance = CurrencyConversionUtil.reduce(x5965vo.getCurrCostBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setCurrCostBalance(currCostBalance);
        }
        if(x5965vo.getBillCostBalance()!=null && x5965vo.getCurrDeciaml()!=null){//实时余额账单费用入账金额
            BigDecimal billCostBalance = CurrencyConversionUtil.reduce(x5965vo.getBillCostBalance(),x5965vo.getCurrDeciaml());
            x5965vo.setBillCostBalance(billCostBalance);
        }
        if(x5965vo.getPostingAmt()!=null && x5965vo.getPostingCurrDecimal()!=null){//入账金额
            BigDecimal postingAmt = CurrencyConversionUtil.reduce(x5965vo.getPostingAmt(),x5965vo.getPostingCurrDecimal());
            x5965vo.setPostingAmt(postingAmt);
        }
        if(x5965vo.getClearAmount()!=null && x5965vo.getClearCurrDecimal()!=null){//清算金额 
            BigDecimal clearAmount = CurrencyConversionUtil.reduce(x5965vo.getClearAmount(),x5965vo.getClearCurrDecimal());
            x5965vo.setClearAmount(clearAmount);
        }
        if(x5965vo.getSettleDistriAmount()!=null && x5965vo.getSettleDistriCurrPoint()!=null){//清算分配金额 
            BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(x5965vo.getSettleDistriAmount(),x5965vo.getSettleDistriCurrPoint());
            x5965vo.setSettleDistriAmount(settleDistriAmount);
        }
        if(x5965vo.getOverpayFrzAmount()!=null && x5965vo.getOverpayFrzCurrPoint()!=null){//溢缴款冻结金额
            BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(x5965vo.getOverpayFrzAmount(),x5965vo.getOverpayFrzCurrPoint());
            x5965vo.setOverpayFrzAmount(overpayFrzAmount);
        }
        if(x5965vo.getOverpayAmount()!=null && x5965vo.getPostingCurrDecimal()!=null){//溢缴款金额 
            BigDecimal overpayAmount = CurrencyConversionUtil.reduce(x5965vo.getOverpayAmount(),x5965vo.getPostingCurrDecimal());
            x5965vo.setOverpayAmount(overpayAmount);
        }
        if(x5965vo.getOriPostingAmt()!=null && x5965vo.getPostingCurrDecimal()!=null){//余额分配前的入账金额 
            BigDecimal oriPostingAmt = CurrencyConversionUtil.reduce(x5965vo.getOriPostingAmt(),x5965vo.getPostingCurrDecimal());
            x5965vo.setOriPostingAmt(oriPostingAmt);
        }
    }
}
