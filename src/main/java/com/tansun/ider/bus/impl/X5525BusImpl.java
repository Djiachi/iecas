package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5525Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreOccurrAmountChainDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreOccurrAmountChain;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOccurrAmountChainSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5525BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.TableSubCoreOccurrAmountChain;

/**
 * 查询发生额详情
 * 
 * @author lianhuan 2018年10月25日
 */
@Service
public class X5525BusImpl implements X5525Bus {

    @Autowired
    private CoreOccurrAmountChainDao coreOccurrAmountChainDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;

    @Override
    public Object busExecute(X5525BO x5525bo) throws Exception {
        // 判断输入的各字段是否为空
        SpringUtil.getBean(ValidatorUtil.class).validate(x5525bo);
        String balanceUnitCode = x5525bo.getBalanceUnitCode();
        String interestStartDate = x5525bo.getInterestStartDate();
        String nodeTyp = x5525bo.getNodeTyp();
        PageBean<CoreOccurrAmountChain> page = new PageBean<>();
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        if (StringUtil.isNotBlank(balanceUnitCode)) {
            coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
        }
        CoreBalanceUnit coreBalanceUnit = coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
        String currencyCode = coreBalanceUnit.getCurrencyCode();

        CoreOccurrAmountChainSqlBuilder coreOccurrAmountChainSqlBuilder = new CoreOccurrAmountChainSqlBuilder();
        if (StringUtil.isNotBlank(balanceUnitCode)) {
            coreOccurrAmountChainSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
        }
        if (StringUtil.isNotBlank(interestStartDate)) {
            coreOccurrAmountChainSqlBuilder.andInterestStartDateEqualTo(interestStartDate);
        }
        if (StringUtil.isNotBlank(nodeTyp)) {
            coreOccurrAmountChainSqlBuilder.andNodeTypEqualTo(nodeTyp);
        }
        SpringUtil.getBean(TableSubCoreOccurrAmountChain.class).tableSub(coreOccurrAmountChainSqlBuilder, balanceUnitCode);
        int totalCount = coreOccurrAmountChainDao.countBySqlBuilder(coreOccurrAmountChainSqlBuilder);
        page.setTotalCount(totalCount);

        if (null != x5525bo.getPageSize() && null != x5525bo.getIndexNo()) {
            coreOccurrAmountChainSqlBuilder.setPageSize(x5525bo.getPageSize());
            coreOccurrAmountChainSqlBuilder.setIndexNo(x5525bo.getIndexNo());
            page.setPageSize(x5525bo.getPageSize());
            page.setIndexNo(x5525bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreOccurrAmountChain> list = coreOccurrAmountChainDao
                    .selectListBySqlBuilder(coreOccurrAmountChainSqlBuilder);
            for (CoreOccurrAmountChain occurrAmountChain : list) {
                amountConversion(occurrAmountChain, currencyCode);
            }
            page.setRows(list);
        }
        return page;
    }

    private void amountConversion(CoreOccurrAmountChain coreOccurrAmountChain, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreOccurrAmountChain.getOccurrAmount() != null
                && !coreOccurrAmountChain.getOccurrAmount().toString().equals("0")) {
            BigDecimal occurrAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getOccurrAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setOccurrAmount(occurrAmount);
        }
/*        if (coreOccurrAmountChain.getDebitAmount() != null
                && !coreOccurrAmountChain.getDebitAmount().toString().equals("0")) {
            BigDecimal debitAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getDebitAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setDebitAmount(debitAmount);
        }
        if (coreOccurrAmountChain.getCreditAmount() != null
                && !coreOccurrAmountChain.getCreditAmount().toString().equals("0")) {
            BigDecimal creditAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getCreditAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setCreditAmount(creditAmount);
        }
        if (coreOccurrAmountChain.getPaymentAmount() != null
                && !coreOccurrAmountChain.getPaymentAmount().toString().equals("0")) {
            BigDecimal paymentAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getPaymentAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setPaymentAmount(paymentAmount);
        }
        if (coreOccurrAmountChain.getNetoutAdjustAmount() != null
                && !coreOccurrAmountChain.getNetoutAdjustAmount().toString().equals("0")) {
            BigDecimal netoutAdjustAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getNetoutAdjustAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setNetoutAdjustAmount(netoutAdjustAmount);
        }
        if (coreOccurrAmountChain.getPaymentRevAmount() != null
                && !coreOccurrAmountChain.getPaymentRevAmount().toString().equals("0")) {
            BigDecimal paymentRevAmount = CurrencyConversionUtil.reduce(coreOccurrAmountChain.getPaymentRevAmount(),
                    decimalPlaces);
            coreOccurrAmountChain.setPaymentRevAmount(paymentRevAmount);
        }*/
    }
}
