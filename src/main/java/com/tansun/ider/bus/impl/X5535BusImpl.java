package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5535Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreInterestContrlChainDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreInterestContrlChain;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInterestContrlChainSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5535BO;
import com.tansun.ider.model.vo.X5535VO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 查询计息控制链表
 * 
 * @author lianhuan 2018年10月25日
 */
@Service
public class X5535BusImpl implements X5535Bus {

    @Autowired
    private CoreInterestContrlChainDao coreInterestContrlChainDao;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreAccountDao coreAccountDao;
    
    
    @Override
    public Object busExecute(X5535BO x5535bo) throws Exception {
    	SpringUtil.getBean(ValidatorUtil.class).validate(x5535bo);
        PageBean<X5535VO> page = new PageBean<>();
        String currencyCode = x5535bo.getCurrencyCode();
        String cycleNumber = x5535bo.getCycleNumber();
        String balanceUnitCode = x5535bo.getBalanceUnitCode();
        String accountId = x5535bo.getAccountId();
        
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        if (null == coreAccount ) {
        	throw new BusinessException("CUS-00014","账户基本");
		}
        String operationMode = coreAccount.getOperationMode();
        CoreInterestContrlChainSqlBuilder coreInterestContrlChainSqlBuilder = new CoreInterestContrlChainSqlBuilder();
        if (StringUtil.isNotBlank(cycleNumber)) {
        	coreInterestContrlChainSqlBuilder.andBillingCycleNoEqualTo(Integer.valueOf(cycleNumber));
		}
        if (StringUtil.isNotBlank(balanceUnitCode)) {
            coreInterestContrlChainSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);  
        }
        int totalCount = coreInterestContrlChainDao.countBySqlBuilder(coreInterestContrlChainSqlBuilder);
        page.setTotalCount(totalCount);

        if (null != x5535bo.getPageSize() && null != x5535bo.getIndexNo()) {
            coreInterestContrlChainSqlBuilder.setPageSize(x5535bo.getPageSize());
            coreInterestContrlChainSqlBuilder.setIndexNo(x5535bo.getIndexNo());
            page.setPageSize(x5535bo.getPageSize());
            page.setIndexNo(x5535bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreInterestContrlChain> list = coreInterestContrlChainDao
                    .selectListBySqlBuilder(coreInterestContrlChainSqlBuilder);
            List<X5535VO> x5535VOList = new ArrayList<X5535VO>();
            for (CoreInterestContrlChain coreInterestContrlChain : list) {
            	X5535VO x5535VO = new X5535VO();
                // 金额转换
                amountConversion(coreInterestContrlChain, currencyCode);
                CachedBeanCopy.copyProperties(coreInterestContrlChain, x5535VO);
                //根据原结息余额单元代码、原结息日期、节点类别=BI查询引起该笔利息调整的节点（原B9节点）; 需显示原结息金额
                CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
                coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(coreInterestContrlChain.getBalanceUnitCode());
                CoreBalanceUnit coreBalanceUnit =  coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
                if (null == coreBalanceUnit) {
                	throw new BusinessException("CUS-00014","余额单元");
				}
                //获取余额单元描述
                CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, coreBalanceUnit.getBalanceObjectCode());
                x5535VO.setBalanceObjectDesc(coreBalanceObject.getObjectDesc());
                x5535VO.setBalanceObjectCode(coreBalanceUnit.getBalanceObjectCode());
                x5535VO.setCycleNumber(coreBalanceUnit.getCycleNumber());
                x5535VO.setBillingCycleNo(coreInterestContrlChain.getBillingCycleNo());
                x5535VOList.add(x5535VO);
            }
            page.setRows(x5535VOList);
        }
        return page;
    }

    private void amountConversion(CoreInterestContrlChain coreInterestContrlChain, String currencyCode)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreInterestContrlChain.getCurrInterestAmount() != null  && !coreInterestContrlChain.getCurrInterestAmount().toString().equals("0")) {
            BigDecimal currInterestAmount = CurrencyConversionUtil
                    .reduce(coreInterestContrlChain.getCurrInterestAmount(), decimalPlaces);
            coreInterestContrlChain.setCurrInterestAmount(currInterestAmount);
        }
        if (coreInterestContrlChain.getLastInterestAmount() != null && !coreInterestContrlChain.getLastInterestAmount().toString().equals("0")) {
            BigDecimal lastInterestAmount = CurrencyConversionUtil
                    .reduce(coreInterestContrlChain.getLastInterestAmount(), decimalPlaces);
            coreInterestContrlChain.setLastInterestAmount(lastInterestAmount);
        }
        if (coreInterestContrlChain.getOriInterestAmount() != null && !coreInterestContrlChain.getOriInterestAmount().toString().equals("0")) {
            BigDecimal oriInterestAmount = CurrencyConversionUtil
                    .reduce(coreInterestContrlChain.getOriInterestAmount(), decimalPlaces);
            coreInterestContrlChain.setOriInterestAmount(oriInterestAmount);
        }
    }

    @Override
    public Object executeForInterest(X5535BO x5535bo) throws Exception {
        SpringUtil.getBean(ValidatorUtil.class).validate(x5535bo);
        PageBean<X5535VO> page = new PageBean<>();
//        String balanceUnitCode = x5535bo.getBalanceUnitCode();
        String balanceObjectCode = x5535bo.getBalanceObjectCode();
        String currencyCode = x5535bo.getCurrencyCode();
        String cycleNumber = x5535bo.getCycleNumber();
        
        String accountId = x5535bo.getAccountId();
        
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        if (null == coreAccount ) {
            throw new BusinessException("CUS-00014","账户基本");
        }
        String operationMode = coreAccount.getOperationMode();
        CoreInterestContrlChainSqlBuilder coreInterestContrlChainSqlBuilder = new CoreInterestContrlChainSqlBuilder();
        if (StringUtil.isNotBlank(cycleNumber)) {
            coreInterestContrlChainSqlBuilder.andBillingCycleNoEqualTo(Integer.valueOf(cycleNumber));
        }
        //查询账户下余额单元
        if (StringUtil.isNotBlank(accountId)) {
            CoreBalanceUnitSqlBuilder  coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
            coreBalanceUnitSqlBuilder.andAccountIdEqualTo(accountId);
            if (StringUtil.isNotBlank(currencyCode)) {
                coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
            }
            List<CoreBalanceUnit>  balanceList = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
            if(null!=balanceList && !balanceList.isEmpty()){
                  CoreInterestContrlChainSqlBuilder coreInterestContrlChainSqlBuildertemp = new CoreInterestContrlChainSqlBuilder();
                for(CoreBalanceUnit coreBalanceUnit :balanceList){
                    coreInterestContrlChainSqlBuildertemp.orBalanceUnitCodeEqualTo(coreBalanceUnit.getBalanceUnitCode());
                }
                coreInterestContrlChainSqlBuilder.and(coreInterestContrlChainSqlBuildertemp);
            }
        }
//        coreInterestContrlChainSqlBuilder.orderBy(false);
        int totalCount = coreInterestContrlChainDao.countBySqlBuilder(coreInterestContrlChainSqlBuilder);
        page.setTotalCount(totalCount);

        if (null != x5535bo.getPageSize() && null != x5535bo.getIndexNo()) {
            coreInterestContrlChainSqlBuilder.setPageSize(x5535bo.getPageSize());
            coreInterestContrlChainSqlBuilder.setIndexNo(x5535bo.getIndexNo());
            page.setPageSize(x5535bo.getPageSize());
            page.setIndexNo(x5535bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreInterestContrlChain> list = coreInterestContrlChainDao
                    .selectListBySqlBuilder(coreInterestContrlChainSqlBuilder);
            List<X5535VO> x5535VOList = new ArrayList<X5535VO>();
            for (CoreInterestContrlChain coreInterestContrlChain : list) {
                X5535VO x5535VO = new X5535VO();
                // 金额转换
                amountConversion(coreInterestContrlChain, currencyCode);
                CachedBeanCopy.copyProperties(coreInterestContrlChain, x5535VO);
                CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
                coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(coreInterestContrlChain.getBalanceUnitCode());
                CoreBalanceUnit coreBalanceUnit =  coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
                if (null == coreBalanceUnit) {
                    throw new BusinessException("CUS-00014","余额单元");
                }
                //获取余额单元描述
                CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, coreBalanceUnit.getBalanceObjectCode());
                x5535VO.setBalanceObjectDesc(coreBalanceObject.getObjectDesc());
                x5535VO.setBalanceObjectCode(coreBalanceUnit.getBalanceObjectCode());
                x5535VO.setCycleNumber(coreBalanceUnit.getCycleNumber());
                x5535VO.setBillingCycleNo(coreInterestContrlChain.getBillingCycleNo());
                x5535VOList.add(x5535VO);
            }
            
            page.setRows(x5535VOList);
        }
        return page;
    }
}
