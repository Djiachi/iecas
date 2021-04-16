package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.ider.bus.X5505Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingStatus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.enums.AccountingStatus;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5505BO;
import com.tansun.ider.model.vo.X5505VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * @version:1.0
 * @Description: 余额单元查询
 * @author: lianhuan
 */
@Service
public class X5505BusImpl implements X5505Bus {

    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5505BO x5505bo) throws Exception {
        PageBean<X5505VO> page = new PageBean<>();
        // 身份证号
        String accountId = x5505bo.getAccountId();
        // 外部识别号
        String currencyCode = x5505bo.getCurrencyCode();
        String operationMode = x5505bo.getOperationMode();
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        coreBalanceUnitSqlBuilder.andAccountIdEqualTo(accountId);
        coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        int totalCount = coreBalanceUnitDao.countBySqlBuilder(coreBalanceUnitSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5505bo.getPageSize() && null != x5505bo.getIndexNo()) {
            coreBalanceUnitSqlBuilder.setIndexNo(x5505bo.getIndexNo());
            coreBalanceUnitSqlBuilder.setPageSize(x5505bo.getPageSize());
            page.setPageSize(x5505bo.getPageSize());
            page.setIndexNo(x5505bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<X5505VO> x5505vos = new ArrayList<X5505VO>();
            coreBalanceUnitSqlBuilder.orderByCycleNumber(true);
            coreBalanceUnitSqlBuilder.orderByBalanceUnitCode(false);
            List<CoreBalanceUnit> listCoreAccount = coreBalanceUnitDao
                    .selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
            // 增加核算状态描述
            List<CoreAccountingStatus> coreAccountingStatusList = httpQueryService
                    .queryCoreAccountingStatusList(operationMode, Constant.RESULT_TYPE_ALL);
            for (CoreBalanceUnit coreBalanceUnit : listCoreAccount) {
                X5505VO x5505vo = new X5505VO();
                // 金额转换及币种描述获取
                amountConversion(coreBalanceUnit, currencyCode, x5505vo);
                CachedBeanCopy.copyProperties(coreBalanceUnit, x5505vo);
                String balanceObjectCode = x5505vo.getBalanceObjectCode();
                String balanceObjectDesc = queryBalObjDesc(x5505bo.getOperationMode(), balanceObjectCode);
                x5505vo.setBalanceObjectDesc(balanceObjectDesc);
                // 核算状态描述
                String accountingStatusCode;
                if (StringUtils.isEmpty(coreBalanceUnit.getAccountingStatusCode())) {
                    accountingStatusCode = AccountingStatus.NORMAL.getValue();
                } else {
                    accountingStatusCode = coreBalanceUnit.getAccountingStatusCode();
                }
                String accountingStatusCodeDesc = getAccountingStatusCodeDesc(accountingStatusCode,
                        coreAccountingStatusList);
                x5505vo.setAccountingStatusCodeDesc(accountingStatusCodeDesc);
                x5505vos.add(x5505vo);
            }
            page.setRows(x5505vos);
        }
        return page;
    }

    /**
     * 页面需要返显余额对象描述
     *
     * @param balanceObjectCode
     * @param balanceObjectCode
     * @return
     * @throws Exception
     */
    private String queryBalObjDesc(String operationMode, String balanceObjectCode) throws Exception {
        CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, balanceObjectCode);
        if (coreBalanceObject == null) {
            throw new BusinessException("PARAM-00002", "运营模式" + operationMode + "下的余额对象" + balanceObjectCode);
        }
        return coreBalanceObject.getObjectDesc();
    }

    /**
     * 金额转换:从数据库中查出来后需要缩小
     *
     * @param coreBalanceUnit
     * @param currencyCode
     * @throws Exception
     */
    private void amountConversion(CoreBalanceUnit coreBalanceUnit, String currencyCode, X5505VO x5505vo)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreBalanceUnit.getBalance() != null) {
            BigDecimal balance = CurrencyConversionUtil.reduce(coreBalanceUnit.getBalance(), decimalPlaces);
            coreBalanceUnit.setBalance(balance);
        }
        if (coreBalanceUnit.getAccumulatedInterest() != null) {
            BigDecimal accumulatedInterest = CurrencyConversionUtil.reduce(coreBalanceUnit.getAccumulatedInterest(),
                    decimalPlaces);
            coreBalanceUnit.setAccumulatedInterest(accumulatedInterest);
        }
        if (coreBalanceUnit.getCurrentMinPayment() != null) {
            BigDecimal currentMinPayment = CurrencyConversionUtil.reduce(coreBalanceUnit.getCurrentMinPayment(),
                    decimalPlaces);
            coreBalanceUnit.setCurrentMinPayment(currentMinPayment);
        }
        if (null != coreCurrency) {
            x5505vo.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }

    }

    /**
     * 匹配核算状态描述
     *
     * @param accountingStatusCode
     *            核算状态编码
     * @param accountingStatuses
     *            核算状态列表
     * @return 币种描述
     */
    private String getAccountingStatusCodeDesc(String accountingStatusCode,
                                               List<CoreAccountingStatus> accountingStatuses) {

        CoreAccountingStatus accountingStatus;

        Optional<CoreAccountingStatus> first = accountingStatuses.stream().parallel()
                .filter(x -> StringUtils.equals(x.getAccountingStatus(), accountingStatusCode)).findFirst();
        if (first.isPresent()) {
            accountingStatus = first.get();
            return accountingStatus.getAccountingDesc();
        }
        return null;
    }

}
