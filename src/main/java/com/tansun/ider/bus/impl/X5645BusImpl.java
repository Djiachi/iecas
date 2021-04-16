package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.bus.X5645Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentPlanDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CorePaymentPlanDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CorePaymentPlan;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CorePaymentPlanSqlBuilder;
import com.tansun.ider.enums.IouStatus;
import com.tansun.ider.enums.PaymentStatus;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5645BO;
import com.tansun.ider.model.vo.X5645VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CardUtil;

/**
 * 最近一期未支付计划信息查询
 *
 * @author liuyanxi
 *
 */
@Service
public class X5645BusImpl implements X5645Bus {
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
    public Object busExecute(X5645BO x5645bo) throws Exception {
        String accountId = x5645bo.getAccountId();
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(accountId);
        coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(x5645bo.getCurrencyCode());
        CoreInstallmentTransAcct coreInstallmentTransAcct =
                coreInstallmentTransAcctDao.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
        if (coreInstallmentTransAcct == null) {
            throw new BusinessException("COR-12008");
        }
        if (IouStatus.revoke.getValue().equals(coreInstallmentTransAcct.getStatus())
                || IouStatus.FRT.getValue().equals(coreInstallmentTransAcct.getStatus())) {
            // 已撤销或者已冲正
            throw new BusinessException("COR-12041");
        }

        if (PaymentStatus.PAID.getValue().equals(coreInstallmentTransAcct.getPayStatus())) {
            // 已全部支付报错
            throw new BusinessException("COR-12040");
        }

        if (coreInstallmentTransAcct.getLoanAmount().compareTo(coreInstallmentTransAcct.getPrepaidAmount()) <= 0) {
            // 已全部支付
            throw new BusinessException("COR-12040");
        }
        CoreSystemUnit coreSystemUnit = this.getProcessDate(coreInstallmentTransAcct);
        List<CorePaymentPlan> list = getCorePaymentPlan(coreInstallmentTransAcct.getAccountId(), coreSystemUnit.getCurrProcessDate(),
            coreSystemUnit.getNextProcessDate(), coreInstallmentTransAcct.getCurrencyCode());
        X5645VO x5645vo = new X5645VO();
        x5645vo.setAccountId(coreInstallmentTransAcct.getAccountId());
        CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
        int currencyDecimal = cardUtil.getCurrencyDecimal(coreInstallmentTransAcct.getCurrencyCode());
        x5645vo.setPaymentDate(coreSystemUnit.getNextProcessDate());
        BigDecimal payAmt = BigDecimal.ZERO;
        if (list == null || list.size() == 0) {
            payAmt = coreInstallmentTransAcct.getLoanAmount().subtract(coreInstallmentTransAcct.getPrepaidAmount());
            // TODO查询系统单元的下一处理日
            // return x5645vo;
        }

        for (CorePaymentPlan corePaymentPlan : list) {
            if (PaymentStatus.NONPAID.getValue().equals(corePaymentPlan.getPaymentStatus())) {
                payAmt = payAmt.add(corePaymentPlan.getPaymentAmount());
            }
        }
        if (BigDecimal.ZERO.compareTo(payAmt) >= 0) {
            throw new BusinessException("COR-12040");
        }
        payAmt = CurrencyConversionUtil.reduce(payAmt, currencyDecimal);
        x5645vo.setPayableAmount(payAmt);
        return x5645vo;
    }

    /**
     * 获取系统处理日期
     *
     * @param coreInstallmentTransAcct
     * @return
     * @throws Exception
     */
    private CoreSystemUnit getProcessDate(CoreInstallmentTransAcct coreInstallmentTransAcct) throws Exception {
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(coreInstallmentTransAcct.getCustomerNo());
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        return coreSystemUnit;
    }

    /**
     * 查找当天支付计划
     *
     * @param accountId
     * @param payDate
     * @return
     * @throws Exception
     */
    private List<CorePaymentPlan> getCorePaymentPlan(String accountId, String lastDealDate, String currentDealDate, String currencyCode)
            throws Exception {
        CorePaymentPlanSqlBuilder corePaymentPlanSqlBuilder = new CorePaymentPlanSqlBuilder();
        corePaymentPlanSqlBuilder.andAccountIdEqualTo(accountId);
        corePaymentPlanSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        corePaymentPlanSqlBuilder.andPaymentDateGreaterThan(lastDealDate).andPaymentDateLessThanOrEqualTo(currentDealDate);
        List<CorePaymentPlan> corePaymentPlans = corePaymentPlanDao.selectListBySqlBuilder(corePaymentPlanSqlBuilder);
        // 假如没查到则取没有支付的
        if (corePaymentPlans == null || corePaymentPlans.size() < 1) {
            corePaymentPlanSqlBuilder.clear();
            corePaymentPlanSqlBuilder.andAccountIdEqualTo(accountId);
            corePaymentPlanSqlBuilder.andPaymentStatusEqualTo(PaymentStatus.NONPAID.getValue());
            corePaymentPlanSqlBuilder.orderByPaymentDate(false);
            corePaymentPlans = corePaymentPlanDao.selectListBySqlBuilder(corePaymentPlanSqlBuilder);
            if (corePaymentPlans != null && corePaymentPlans.size() > 0) {
                List<CorePaymentPlan> corePaymentPlanList = new ArrayList<CorePaymentPlan>();
                corePaymentPlanList.add(corePaymentPlans.get(0));
                return corePaymentPlanList;
            }
        }
        return corePaymentPlans;
    }

}
