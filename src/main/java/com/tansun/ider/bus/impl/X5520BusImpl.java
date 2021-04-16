package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5520Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreAccountCycleFiciDao;
import com.tansun.ider.dao.issue.entity.CoreAccountCycleFici;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountCycleFiciSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5520BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 查询账户周期金融信息
 * 
 * @author lianhuan 2018年10月25日
 */
@Service
public class X5520BusImpl implements X5520Bus {

    @Resource
    private CoreAccountCycleFiciDao coreAccountCycleFiciDao;
    @Autowired
    private HttpQueryService httpQueryService;
	@Autowired
	private ParamsUtil paramsUtil;

    @Override
    public Object busExecute(X5520BO x5520bo) throws Exception {
        String accountId = x5520bo.getAccountId();
        String currencyCode = x5520bo.getCurrencyCode();
        Integer currentCycleNumber = x5520bo.getCurrentCycleNumber();
        
        String entrys =Constant.EMPTY_LIST;
        PageBean<X5520BO> page = new PageBean<>();
        CoreAccountCycleFiciSqlBuilder coreAccountCycleFiciSqlBuilder = new CoreAccountCycleFiciSqlBuilder();
        if (StringUtil.isNotEmpty(accountId)) {
            coreAccountCycleFiciSqlBuilder.andAccountIdEqualTo(accountId);
        }
        if (StringUtil.isNotEmpty(currencyCode)) {
            coreAccountCycleFiciSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        if (currentCycleNumber != null) {
            coreAccountCycleFiciSqlBuilder.andCurrentCycleNumberEqualTo(currentCycleNumber);
        }
        int totalCount = coreAccountCycleFiciDao.countBySqlBuilder(coreAccountCycleFiciSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5520bo.getPageSize() && null != x5520bo.getIndexNo()) {
            coreAccountCycleFiciSqlBuilder.setPageSize(x5520bo.getPageSize());
            coreAccountCycleFiciSqlBuilder.setIndexNo(x5520bo.getIndexNo());
            page.setPageSize(x5520bo.getPageSize());
            page.setIndexNo(x5520bo.getIndexNo());
        }
        if (totalCount > 0) {
            coreAccountCycleFiciSqlBuilder.orderByCurrentCycleNumber(true);
            coreAccountCycleFiciSqlBuilder.orderByGmtCreate(false);
        	List<X5520BO> x5520BOList = new ArrayList<>();
            List<CoreAccountCycleFici> list = coreAccountCycleFiciDao
                    .selectListBySqlBuilder(coreAccountCycleFiciSqlBuilder);
            //遍历次数
            int foreachIndex = 0;
            X5520BO tempBo = null ;
            for (CoreAccountCycleFici coreAccountCycleFici : list) {
                X5520BO x5520BO = new X5520BO();
                // 金额转换
                amountConversion(coreAccountCycleFici, currencyCode, x5520BO);
                CachedBeanCopy.copyProperties(coreAccountCycleFici, x5520BO);
                if (foreachIndex == 0 || foreachIndex == list.size()) {
                    foreachIndex++;
                    tempBo = x5520BO;
                    continue;
                } else {
                    tempBo.setDebitAmount(x5520BO.getDebitAmount());
                    tempBo.setDebitNum(x5520BO.getDebitNum());
                    tempBo.setCreditNum(x5520BO.getCreditNum());
                    x5520BOList.add(tempBo);
                    tempBo = x5520BO;
                }
                foreachIndex++;
            }
            page.setRows(x5520BOList);
			if(null != list && !list.isEmpty()){
				entrys = list.get(0).getId();
			}
			//记录查询日志
			CoreEvent tempObject = new CoreEvent();
			paramsUtil.logNonInsert(x5520bo.getCoreEventActivityRel().getEventNo(), x5520bo.getCoreEventActivityRel().getActivityNo(),
					tempObject, tempObject, entrys, x5520bo.getOperatorId());
        }
        return page;
    }

    private void amountConversion(CoreAccountCycleFici coreAccountCycleFici, String currencyCode,X5520BO x5520BO) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        //获取币种描述
        if(null != coreCurrency){
        	x5520BO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }
        if (coreAccountCycleFici.getGraceBalance() != null) {
            BigDecimal beginGraceBalance = CurrencyConversionUtil.reduce(coreAccountCycleFici.getBeginGraceBalance(),
                    decimalPlaces);
            coreAccountCycleFici.setBeginGraceBalance(beginGraceBalance);
        }
        if (coreAccountCycleFici.getGraceBalance() != null) {
            BigDecimal graceBalance = CurrencyConversionUtil.reduce(coreAccountCycleFici.getGraceBalance(),
                    decimalPlaces);
            coreAccountCycleFici.setGraceBalance(graceBalance);
        }
        if (coreAccountCycleFici.getPaymentWithinGrace() != null) {
            BigDecimal paymentWithinGrace = CurrencyConversionUtil.reduce(coreAccountCycleFici.getPaymentWithinGrace(),
                    decimalPlaces);
            coreAccountCycleFici.setPaymentWithinGrace(paymentWithinGrace);
        }
        if (coreAccountCycleFici.getPaymentAfterGrace() != null) {
            BigDecimal paymentAfterGrace = CurrencyConversionUtil.reduce(coreAccountCycleFici.getPaymentAfterGrace(),
                    decimalPlaces);
            coreAccountCycleFici.setPaymentAfterGrace(paymentAfterGrace);
        }
        if (coreAccountCycleFici.getCreditAdjustAmount() != null) {
            BigDecimal creditAdjustAmount = CurrencyConversionUtil.reduce(coreAccountCycleFici.getCreditAdjustAmount(),
                    decimalPlaces);
            coreAccountCycleFici.setCreditAdjustAmount(creditAdjustAmount);
        }
        if (coreAccountCycleFici.getBillCreditAdjustAmount() != null) {
            BigDecimal billCreditAdjustAmount = CurrencyConversionUtil
                    .reduce(coreAccountCycleFici.getBillCreditAdjustAmount(), decimalPlaces);
            coreAccountCycleFici.setBillCreditAdjustAmount(billCreditAdjustAmount);
        }
        if (coreAccountCycleFici.getPaymentRevWithinGrace() != null) {
            BigDecimal paymentRevWithinGrace = CurrencyConversionUtil
                    .reduce(coreAccountCycleFici.getPaymentRevWithinGrace(), decimalPlaces);
            coreAccountCycleFici.setPaymentRevWithinGrace(paymentRevWithinGrace);
        }
        if (coreAccountCycleFici.getPaymentRevAfterGrace() != null) {
            BigDecimal paymentRevAfterGrace = CurrencyConversionUtil
                    .reduce(coreAccountCycleFici.getPaymentRevAfterGrace(), decimalPlaces);
            coreAccountCycleFici.setPaymentRevAfterGrace(paymentRevAfterGrace);
        }
        if (coreAccountCycleFici.getFullToleranceAmount() != null) {
            BigDecimal fullToleranceAmount = CurrencyConversionUtil
                    .reduce(coreAccountCycleFici.getFullToleranceAmount(), decimalPlaces);
            coreAccountCycleFici.setFullToleranceAmount(fullToleranceAmount);
        }
        if (coreAccountCycleFici.getPostingAmount() != null) {
            BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreAccountCycleFici.getPostingAmount(),
                    decimalPlaces);
            coreAccountCycleFici.setPostingAmount(postingAmount);
        }
        if (coreAccountCycleFici.getDebitAmount() != null) {
            BigDecimal debitAmount = CurrencyConversionUtil.reduce(coreAccountCycleFici.getDebitAmount(),
                    decimalPlaces);
            coreAccountCycleFici.setDebitAmount(debitAmount);
        }
    }

}
