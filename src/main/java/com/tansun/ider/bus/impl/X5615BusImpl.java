package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5615Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreStageType;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreAheadPartrepayRecordDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentPlanDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreLoanChangeRecordDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreInstallmentPlan;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentPlanSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.enums.StageType;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.LoanArtifactParameterBO;
import com.tansun.ider.model.RepayPlanBO;
import com.tansun.ider.model.bo.X5615BO;
import com.tansun.ider.model.vo.X5615VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.GenerateInstallmentPlan;
import com.tansun.ider.util.PlanUtil;
import com.tansun.ider.util.RepayPlanUtil;

/**
 * 分期账户信息+分期计划查询
 *
 * @author huangyayun 2018年10月30日
 */
@Service
public class X5615BusImpl implements X5615Bus {
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Resource
    private CoreInstallmentPlanDao coreInstallmentPlanDao;// 分期计划信息表
    @Resource
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;// 分期交易账户表
    @Resource
    private CoreAccountDao coreAccountDao;
    @Resource
    private CoreAheadPartrepayRecordDao coreAheadPartrepayRecordDao;
    @Resource
    private CoreLoanChangeRecordDao coreLoanChangeRecordDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    private RepayPlanUtil repayPlanUtil;
    @Resource
    private GenerateInstallmentPlan generateInstallmentPlan;

    @Override
    public Object busExecute(X5615BO x5615bo) throws Exception {

        CoreInstallmentPlanSqlBuilder coreInstallmentPlanSqlBuilder = new CoreInstallmentPlanSqlBuilder();
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        CoreInstallmentTransAcct coreInstallmentTransAcct = null;
        CoreAccount coreAccount = null;
        if (StringUtil.isNotEmpty(x5615bo.getAccountId())) {
            coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(x5615bo.getAccountId());
            coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(x5615bo.getCurrencyCode());
            coreBalanceUnitSqlBuilder.andAccountIdEqualTo(x5615bo.getAccountId());
            coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(x5615bo.getCurrencyCode());
            coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(x5615bo.getAccountId());
            coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(x5615bo.getCurrencyCode());
            coreInstallmentTransAcct = coreInstallmentTransAcctDao.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
            if (coreInstallmentTransAcct == null) {
                throw new BusinessException("COR-12008");
            }
            coreAccount = queryAccount(coreInstallmentTransAcct.getAccountId(), coreInstallmentTransAcct.getCurrencyCode());
        }
        else if (StringUtil.isNotEmpty(x5615bo.getOldGlobalSerialNumbr())) {
            coreInstallmentTransAcctSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5615bo.getOldGlobalSerialNumbr());
            coreInstallmentTransAcct = coreInstallmentTransAcctDao.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
            if (coreInstallmentTransAcct == null) {
                throw new BusinessException("COR-12008");
            }
            coreAccount = queryAccount(coreInstallmentTransAcct.getAccountId(), coreInstallmentTransAcct.getCurrencyCode());
            amountConversion(coreInstallmentTransAcct, coreInstallmentTransAcct.getCurrencyCode());
            descConversion(coreAccount.getOperationMode(),coreInstallmentTransAcct);
            coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(coreInstallmentTransAcct.getAccountId());
            coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(coreInstallmentTransAcct.getCurrencyCode());
            coreBalanceUnitSqlBuilder.andAccountIdEqualTo(coreInstallmentTransAcct.getAccountId());
            coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(coreInstallmentTransAcct.getCurrencyCode());
        }
        PageBean<CoreInstallmentPlan> page = new PageBean<CoreInstallmentPlan>();
        // 如果当前期次等于贷款期次，则划款计划全部生成完成,如果本金都生成了则不需要计算
        int totalCount = coreInstallmentPlanDao.countBySqlBuilder(coreInstallmentPlanSqlBuilder);
        if (totalCount == coreInstallmentTransAcct.getLoanTerm()
                || (coreInstallmentTransAcct.getLoanPrincipalUndue() == null ? BigDecimal.ZERO
                        : coreInstallmentTransAcct.getLoanPrincipalUndue()).compareTo(BigDecimal.ZERO) < 1) {
            if (null != x5615bo.getPageSize() && null != x5615bo.getIndexNo()) {
                coreInstallmentPlanSqlBuilder.orderByTermNo(false);
                coreInstallmentPlanSqlBuilder.setPageSize(x5615bo.getPageSize());
                coreInstallmentPlanSqlBuilder.setIndexNo(x5615bo.getIndexNo());
                page.setPageSize(x5615bo.getPageSize());
                page.setIndexNo(x5615bo.getIndexNo());
            }
            List<CoreInstallmentPlan> list = coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
            for (CoreInstallmentPlan coreInstallmentPlan : list) {
                amountConversion(coreInstallmentPlan, coreInstallmentPlan.getCurrencyCode());
            }
            page.setRows(list);
        }
        else {
            CoreStageType loanCoreStageType = httpQueryService.queryCoreStageType(coreAccount.getOperationMode(),coreInstallmentTransAcct.getLoanType());
            List<CoreInstallmentPlan> generateCoreInstallmentPlans = null;
            if (StageType.LoanStage.getValue().equals(loanCoreStageType.getStageType())) {
                RepayPlanBO repayPlanBO = generateInstallmentPlan.getCoreInstallmentPlanList(coreInstallmentTransAcct);
                generateCoreInstallmentPlans = repayPlanBO.getCoreInstallmentPlanList();
                page.setTotalCount(coreInstallmentTransAcct.getLoanTerm());
                if (CollectionUtils.isNotEmpty(generateCoreInstallmentPlans)
                        && generateCoreInstallmentPlans.size() == coreInstallmentTransAcct.getLoanTerm()) {
                    for (CoreInstallmentPlan coreInstallmentPlan : generateCoreInstallmentPlans) {
                        amountConversion(coreInstallmentPlan, coreInstallmentPlan.getCurrencyCode());
                    }
                    page.setRows(generateCoreInstallmentPlans);
                }
                else {
                    List<CoreInstallmentPlan> coreInstallmentPlanlist =
                            coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
                    if (CollectionUtils.isNotEmpty(generateCoreInstallmentPlans) && CollectionUtils.isNotEmpty(coreInstallmentPlanlist)) {
                        List<CoreInstallmentPlan> optionalCoreInstallmentPlans = generateCoreInstallmentPlans.stream()
                            .filter(item -> coreInstallmentPlanlist.size() + 1 <= item.getTermNo()).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(optionalCoreInstallmentPlans)) {
                            coreInstallmentPlanlist.addAll(optionalCoreInstallmentPlans);
                        }
                    }
                    for (CoreInstallmentPlan coreInstallmentPlan : coreInstallmentPlanlist) {
                        amountConversion(coreInstallmentPlan, coreInstallmentPlan.getCurrencyCode());
                    }
                    page.setRows(coreInstallmentPlanlist);
                }
                List<CoreInstallmentPlan> pagecoreInstallmentPlanList = new ArrayList<>();
                if (null != x5615bo.getPageSize() && x5615bo.getPageNum() > 0) {
                    page.setPageSize(x5615bo.getPageSize());
                    int pageNo = x5615bo.getPageNum();// 相当于pageNo
                    int count = x5615bo.getPageSize();// 相当于pageSize
                    int size = page.getRows().size();
                    int pageCount = size / count;
                    int fromIndex = count * (pageNo - 1);
                    int toIndex = fromIndex + count;
                    if (toIndex >= size) {
                        toIndex = size;
                    }
                    if (pageNo > pageCount + 1) {
                        fromIndex = 0;
                        toIndex = 0;
                    }
                    pagecoreInstallmentPlanList = page.getRows().subList(fromIndex, toIndex);
                }
                else {
                    throw new BusinessException("分页处理失败!");
                }
                page.setRowsCount(pagecoreInstallmentPlanList.size());
                if (pagecoreInstallmentPlanList.size() > 0) {
                    page.setRows(pagecoreInstallmentPlanList);
                }
            }
            else {
                String status = coreInstallmentTransAcct.getStatus();
                page.setTotalCount(coreInstallmentTransAcct.getLoanTerm());
                if (null != x5615bo.getPageSize() && null != x5615bo.getIndexNo()) {
                    coreInstallmentPlanSqlBuilder.orderByTermNo(false);
                    coreInstallmentPlanSqlBuilder.setPageSize(x5615bo.getPageSize());
                    coreInstallmentPlanSqlBuilder.setIndexNo(x5615bo.getIndexNo());
                    page.setPageSize(x5615bo.getPageSize());
                    page.setIndexNo(x5615bo.getIndexNo());
                }
                List<CoreInstallmentPlan> list = coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
                // 如果该交易已撤销，不拼还款计划
                if (!("0".equals(status) || "3".equals(status) || "4".equals(status))) {
                    generateCoreInstallmentPlans = PlanUtil.generateCoreInstallmentPlans(coreInstallmentTransAcct, null);
                    list.addAll(generateCoreInstallmentPlans);
                }
                for (CoreInstallmentPlan coreInstallmentPlan : list) {
                    amountConversion(coreInstallmentPlan, coreInstallmentPlan.getCurrencyCode());
                }
                page.setRows(list);
            }

        }
        BigDecimal totalBalance = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(x5615bo.getAccountId()) && StringUtil.isEmpty(x5615bo.getOldGlobalSerialNumbr())) {
            int totalCount1 = coreBalanceUnitDao.countBySqlBuilder(coreBalanceUnitSqlBuilder);
            if (totalCount1 > 0) {
                List<CoreBalanceUnit> list = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
                for (CoreBalanceUnit coreBalanceUnit : list) {
                    totalBalance = totalBalance.add(CurrencyConversionUtil.reduce(coreBalanceUnit.getBalance(),
                        httpQueryService.queryCurrency(coreBalanceUnit.getCurrencyCode()).getDecimalPosition()));
                }
            }
        }
        X5615VO x5615vo = new X5615VO();
        if (coreInstallmentTransAcct != null) {
            CachedBeanCopy.copyProperties(coreInstallmentTransAcct, x5615vo);
        }
        x5615vo.setTotalBalance(totalBalance);
        page.setObj(x5615vo);
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
    }

    private void amountConversion(CoreInstallmentPlan coreInstallmentPlan, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        BigDecimal principalAmount = CurrencyConversionUtil.reduce(coreInstallmentPlan.getPrincipalAmount(), decimalPlaces);
        coreInstallmentPlan.setPrincipalAmount(principalAmount);
        BigDecimal interestAmount = CurrencyConversionUtil.reduce(coreInstallmentPlan.getInterestAmount(), decimalPlaces);
        coreInstallmentPlan.setInterestAmount(interestAmount);
        BigDecimal feeAmount = CurrencyConversionUtil.reduce(coreInstallmentPlan.getFeeAmount(), decimalPlaces);
        coreInstallmentPlan.setFeeAmount(feeAmount);
        BigDecimal endCurrentPrincipal = CurrencyConversionUtil.reduce(coreInstallmentPlan.getEndCurrentPrincipal(), decimalPlaces);
        coreInstallmentPlan.setEndCurrentPrincipal(endCurrentPrincipal);
        BigDecimal endCurrentInterest = CurrencyConversionUtil.reduce(coreInstallmentPlan.getEndCurrentInterest(), decimalPlaces);
        coreInstallmentPlan.setEndCurrentInterest(endCurrentInterest);
        BigDecimal endCurrentFee = CurrencyConversionUtil.reduce(coreInstallmentPlan.getEndCurrentFee(), decimalPlaces);
        coreInstallmentPlan.setEndCurrentFee(endCurrentFee);
        BigDecimal principalBalance = CurrencyConversionUtil.reduce(coreInstallmentPlan.getPrincipalBalance(), decimalPlaces);
        coreInstallmentPlan.setPrincipalBalance(principalBalance);
    }

    private void descConversion(String operationMode,CoreInstallmentTransAcct coreInstallmentTransAcct) throws Exception {
        if (StringUtil.isNotBlank(coreInstallmentTransAcct.getLoanType())) {
            CoreStageType coreStageType = httpQueryService.queryCoreStageType(operationMode,coreInstallmentTransAcct.getLoanType());
            if (coreStageType != null) {
                coreInstallmentTransAcct.setLoanType(coreStageType.getStageTypeDesc());
            }
        }
    }

    public static LoanArtifactParameterBO handleMap(Map<String, String> throwDateTypeMap, Map<String, String> nextBillDateMap) {
        Iterator<Map.Entry<String, String>> itThrow = throwDateTypeMap.entrySet().iterator();
        Iterator<Map.Entry<String, String>> itBill = nextBillDateMap.entrySet().iterator();
        LoanArtifactParameterBO parameterBO = new LoanArtifactParameterBO();
        while (itThrow.hasNext()) {
            Map.Entry<String, String> entry = itThrow.next();
            parameterBO.setThrowDayType(entry.getKey());
            parameterBO.setThrowDayValue(new Double(entry.getValue()));
        }
        while (itBill.hasNext()) {
            Map.Entry<String, String> entry = itBill.next();
            if (Constant.CYCLE_BY_WEEK.equals(entry.getKey()) || Constant.CYCLE_BY_MONTH.equals(entry.getKey())) {
                parameterBO.setBillDateType(entry.getKey());
                parameterBO.setBillDateValue(new BigDecimal(entry.getValue()));
                break;
            }
        }
        if (StringUtil.isBlank(parameterBO.getThrowDayType()) || StringUtil.isEmpty(parameterBO.getThrowDayType())
                || StringUtil.isBlank(parameterBO.getBillDateType()) || StringUtil.isEmpty(parameterBO.getBillDateType())) {
            throw new BusinessException("COR-10003");
        }
        return parameterBO;
    };

    /**
     * 查询交易账户对应的分期付款计划信息
     *
     * @param accountId
     *            账户号
     * @param throwedPeriod
     *            已抛帐期次
     * @param currencyCode
     *            币种
     * @return
     * @throws ExceptionCoreInstallmentTransAcct
     */
    public List<CoreInstallmentPlan> getCoreInstallmentPlan(String accountId, String currencyCode) throws Exception {
        CoreInstallmentPlanSqlBuilder coreInstallmentPlanSqlBuilder = new CoreInstallmentPlanSqlBuilder();
        coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(accountId);
        coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        coreInstallmentPlanSqlBuilder.orderByTermNo(true);
        // coreInstallmentPlanSqlBuilder.andTermNoEqualTo(throwedPeriod);
        List<CoreInstallmentPlan> coreInstallmentPlans = coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
        return coreInstallmentPlans;
    }
    
    /**
     * 查询账户
     *
     * @param accountId
     *            账号
     * @return
     * @throws Exception
     */
    public CoreAccount queryAccount(String accountId, String currencyCode) throws Exception {
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        return coreAccount;
    }

    /**
     * 查询交易账户对应的首期分期付款计划信息
     *
     * @param accountId
     *            账户号
     * @param throwedPeriod
     *            已抛帐期次
     * @param currencyCode
     *            币种
     * @return
     * @throws ExceptionCoreInstallmentTransAcct
     */
    public CoreInstallmentPlan getFirstCoreInstallmentPlan(String accountId, String currencyCode) throws Exception {
        CoreInstallmentPlan coreInstallmentPlan = null;
        CoreInstallmentPlanSqlBuilder coreInstallmentPlanSqlBuilder = new CoreInstallmentPlanSqlBuilder();
        coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(accountId);
        coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        coreInstallmentPlanSqlBuilder.orderByTermNo(false);
        List<CoreInstallmentPlan> selectListBySqlBuilder = coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
        if (CollectionUtils.isNotEmpty(selectListBySqlBuilder)) {
            coreInstallmentPlan = selectListBySqlBuilder.get(0);
        }
        return coreInstallmentPlan;
    }

}
