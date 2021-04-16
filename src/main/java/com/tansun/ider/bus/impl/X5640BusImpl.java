package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5640Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentPlanDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CorePaymentPlanDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CorePaymentPlan;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CorePaymentPlanSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5640BO;
import com.tansun.ider.model.vo.X5640VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 分期账户信息+支付计划信息查询
 *
 * @author liuyanxi
 *
 */
@Service
public class X5640BusImpl implements X5640Bus {
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Resource
    private CoreInstallmentPlanDao coreInstallmentPlanDao;// 分期计划信息表
    @Resource
    private CorePaymentPlanDao corePaymentPlanDao;
    @Resource
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;// 分期交易账户表
    @Resource
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5640BO x5640bo) throws Exception {
        CorePaymentPlanSqlBuilder corePaymentPlanSqlBuilder = new CorePaymentPlanSqlBuilder();
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        corePaymentPlanSqlBuilder.andAccountIdEqualTo(x5640bo.getAccountId());
        coreBalanceUnitSqlBuilder.andAccountIdEqualTo(x5640bo.getAccountId());
        coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(x5640bo.getAccountId());
        coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(x5640bo.getCurrencyCode());
        CoreInstallmentTransAcct coreInstallmentTransAcct =
                coreInstallmentTransAcctDao.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
        if (coreInstallmentTransAcct == null) {
            throw new BusinessException("COR-12008");
        }
        amountConversion(coreInstallmentTransAcct, coreInstallmentTransAcct.getCurrencyCode());
        corePaymentPlanSqlBuilder.andAccountIdEqualTo(coreInstallmentTransAcct.getAccountId());
        corePaymentPlanSqlBuilder.andCurrencyCodeEqualTo(coreInstallmentTransAcct.getCurrencyCode());
        coreBalanceUnitSqlBuilder.andAccountIdEqualTo(coreInstallmentTransAcct.getAccountId());
        coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(coreInstallmentTransAcct.getCurrencyCode());
        PageBean<CorePaymentPlan> page = new PageBean<>();
        int totalCount = corePaymentPlanDao.countBySqlBuilder(corePaymentPlanSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5640bo.getPageSize() && null != x5640bo.getIndexNo()) {
            corePaymentPlanSqlBuilder.setPageSize(x5640bo.getPageSize());
            corePaymentPlanSqlBuilder.setIndexNo(x5640bo.getIndexNo());
            page.setPageSize(x5640bo.getPageSize());
            page.setIndexNo(x5640bo.getIndexNo());
        }
        // 按日期升序
        corePaymentPlanSqlBuilder.orderByPaymentDate(false);
        if (totalCount > 0) {
            List<CorePaymentPlan> list = corePaymentPlanDao.selectListBySqlBuilder(corePaymentPlanSqlBuilder);
            for (CorePaymentPlan corePaymentPlan : list) {
                amountConversion(corePaymentPlan, coreInstallmentTransAcct.getCurrencyCode());
            }
            page.setRows(list);
        }
        BigDecimal totalBalance = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(x5640bo.getAccountId()) && StringUtil.isEmpty(x5640bo.getOldGlobalSerialNumbr())) {
            int totalCount1 = coreBalanceUnitDao.countBySqlBuilder(coreBalanceUnitSqlBuilder);
            if (totalCount1 > 0) {
                List<CoreBalanceUnit> list = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
                for (CoreBalanceUnit coreBalanceUnit : list) {
                    totalBalance = totalBalance.add(CurrencyConversionUtil.reduce(coreBalanceUnit.getBalance(),
                        httpQueryService.queryCurrency(coreBalanceUnit.getCurrencyCode()).getDecimalPosition()));
                }
            }
        }
        X5640VO x5640vo = new X5640VO();
        if (coreInstallmentTransAcct != null) {
            CachedBeanCopy.copyProperties(coreInstallmentTransAcct, x5640vo);
        }
        x5640vo.setTotalBalance(totalBalance);
        page.setObj(x5640vo);
        return page;
    }

    private void amountConversion(CoreInstallmentTransAcct coreInstallmentTransAcct, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        BigDecimal loanAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getLoanAmount(), decimalPlaces);
        coreInstallmentTransAcct.setLoanAmount(loanAmount);
        BigDecimal remainPrincipalAmount =
                CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainPrincipalAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainPrincipalAmount(remainPrincipalAmount);
        BigDecimal feeAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getFeeAmount(), decimalPlaces);
        coreInstallmentTransAcct.setFeeAmount(feeAmount);
        BigDecimal remainFeeAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainFeeAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainFeeAmount(remainFeeAmount);
        BigDecimal interAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getInterAmount(), decimalPlaces);
        coreInstallmentTransAcct.setInterAmount(interAmount);
        BigDecimal remainInterAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainInterAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainInterAmount(remainInterAmount);
        if (coreInstallmentTransAcct.getPrepaidAmount() != null) {
            BigDecimal prePaidAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getPrepaidAmount(), decimalPlaces);
            coreInstallmentTransAcct.setPrepaidAmount(prePaidAmount);
        }
    }

    private void amountConversion(CorePaymentPlan corePaymentPlan, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        BigDecimal paymentAmount = CurrencyConversionUtil.reduce(corePaymentPlan.getPaymentAmount(), decimalPlaces);
        corePaymentPlan.setPaymentAmount(paymentAmount);
        ;
    }

}
