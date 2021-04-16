package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4080Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingRuleMaster;
import com.tansun.ider.dao.beta.entity.CoreAccountingSubEntry;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreAccountingInterfaceDao;
import com.tansun.ider.dao.issue.entity.CoreAccountingInterface;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountingInterfaceSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X4080BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.util.CacheUtils;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CurrencyUtils;

/**
 * Title: X4080BusImpl
 * Description: 会计接口文件查询
 * Copyright: veredholdings.com Copyright (C) 2019
 * @author yanyingzhao
 * @since 2019年6月20日
 */
@Service
public class X4080BusImpl implements X4080Bus {

    @Autowired
    private CoreAccountingInterfaceDao coreAccountingInterfaceDao;

    @Autowired
    private QueryCustomerService queryCustomerService;

    @Autowired
    private HttpQueryService httpQueryService;

    @Autowired
    private CurrencyUtils currencyUtils;

    private static String QUERY_ALL = "ALL";

    @Override
    public Object busExecute(X4080BO x4080bo) throws Exception {
        PageBean<X4080BO> page = new PageBean<>();
        // 全局流水号
        String globalSerialNumber = x4080bo.getGlobalSerialNumber();
        // 记账规则代码
        String accountingRuleCode = x4080bo.getAccountingRuleCode();
        // 子表序号
        String subTableSequence = x4080bo.getSubTableSequence();
        // 入账日期 yyyy-MM-dd
        String postingDate = x4080bo.getPostingDate();
        // 账号
        String accountId = x4080bo.getAccountId();
        // 证件类型
        String idType = x4080bo.getIdType();
        // 证件号码
        String idNumber = x4080bo.getIdNumber();
        // 外部识别号
        String externalIdentificationNo = x4080bo.getExternalIdentificationNo();

        CoreAccountingInterfaceSqlBuilder coreAccountingInterfaceSqlBuilder = new CoreAccountingInterfaceSqlBuilder();

        if (StringUtil.isNotBlank(globalSerialNumber)) {
            coreAccountingInterfaceSqlBuilder.andGlobalSerialNumberLikeBoth(globalSerialNumber);
        }
        if (StringUtil.isNotBlank(accountingRuleCode)) {
            coreAccountingInterfaceSqlBuilder.andAccountingRuleCodeLikeBoth(accountingRuleCode);
        }
        if (StringUtil.isNotBlank(subTableSequence)) {
            coreAccountingInterfaceSqlBuilder.andSubTableSequenceLikeBoth(subTableSequence);
        }
        if (StringUtil.isNotBlank(postingDate)) {
            coreAccountingInterfaceSqlBuilder.andPostingDateEqualTo(postingDate);
        }
        if (StringUtil.isNotBlank(accountId)) {
            coreAccountingInterfaceSqlBuilder.andAccountIdLikeBoth(accountId);
        }

        CoreCustomer coreCustomer = null;
        String customerNo = "";
        // 获取运营模式
        String operationMode = "";
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
            coreAccountingInterfaceSqlBuilder.andAccountIdLikeRigth(customerNo);
        } else if (object instanceof CoreMediaBasicInfo) {
            CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                operationMode = coreMediaBasicInfo.getOperationMode();
                coreAccountingInterfaceSqlBuilder.andAccountIdLikeRigth(customerNo);
            }
        }
        String modifyType = x4080bo.getModifyType();
        if (StringUtil.isNotEmpty(modifyType)) {
            modifyType = modifyType.toUpperCase();
        }
        if (!StringUtils.equals(QUERY_ALL, modifyType)) {
            CoreAccountingInterfaceSqlBuilder accountingInterfaceFileSqlBuilder = new CoreAccountingInterfaceSqlBuilder();
            accountingInterfaceFileSqlBuilder.andAccountingRuleCodeLikeRigth("ABS");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("CHG");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("WRT");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("TAXI-C");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("TAXI-W");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("TAXF-C");
            accountingInterfaceFileSqlBuilder.orAccountingRuleCodeLikeRigth("TAXF-W");
            coreAccountingInterfaceSqlBuilder.and(accountingInterfaceFileSqlBuilder);
        }

        int totalCount = coreAccountingInterfaceDao.countBySqlBuilder(coreAccountingInterfaceSqlBuilder);
        page.setTotalCount(totalCount);

        if (null != x4080bo.getPageSize() && null != x4080bo.getIndexNo()) {
            coreAccountingInterfaceSqlBuilder.setPageSize(x4080bo.getPageSize());
            coreAccountingInterfaceSqlBuilder.setIndexNo(x4080bo.getIndexNo());
            page.setPageSize(x4080bo.getPageSize());
            page.setIndexNo(x4080bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<X4080BO> listBO = new ArrayList<X4080BO>();
            //排序：账户，全局流水号、记账规则码
            coreAccountingInterfaceSqlBuilder.orderByAccountId(false);
            coreAccountingInterfaceSqlBuilder.orderByGlobalSerialNumber(false);
            coreAccountingInterfaceSqlBuilder.orderByAccountingRuleCode(false);
            coreAccountingInterfaceSqlBuilder.orderByPostingDate(false);
            List<CoreAccountingInterface> list = coreAccountingInterfaceDao
                    .selectListBySqlBuilder(coreAccountingInterfaceSqlBuilder);
            List<CoreCurrency> coreCurrencies = httpQueryService.queryCurrencyList("all");

            for (CoreAccountingInterface CoreAccountingInterface : list) {
                X4080BO x4080BO = new X4080BO();
                CachedBeanCopy.copyProperties(CoreAccountingInterface, x4080BO);

                BigDecimal actualPostingAmount = x4080BO.getActualPostingAmount();
                actualPostingAmount = currencyUtils.conversionAmount(actualPostingAmount,
                        x4080BO.getPostingCurrencyCode(), CurrencyUtils.output);
                x4080BO.setActualPostingAmount(actualPostingAmount);

                if (StringUtil.isNotBlank(x4080BO.getAccountingRuleCode()) && StringUtil.isNotBlank(operationMode)) {
                    List<CoreAccountingRuleMaster> listAccountingRuleMaster = httpQueryService
                            .queryAccountingRuleMaster(operationMode, x4080BO.getAccountingRuleCode(), null, null, null,
                                    null, null, null, null);
                    if (null != listAccountingRuleMaster && listAccountingRuleMaster.size() != 0) {
                        x4080BO.setAccountingRuleDesc(listAccountingRuleMaster.get(0).getAccountingRuleDesc());
                    }
                }

                if (StringUtil.isNotBlank(x4080BO.getAtomicActionNumber())
                        && StringUtil.isNotBlank(x4080BO.getAccountingStatusCode())) { // 原子动作编号
                    List<CoreAccountingSubEntry> listAccountingRuleSubEntry = httpQueryService
                            .queryAccountingRuleSubEntry(operationMode, accountingRuleCode, subTableSequence,
                                    x4080bo.getAccountingStatusCode());
                    if (null != listAccountingRuleSubEntry && listAccountingRuleSubEntry.size() != 0) {
                        for (CoreAccountingSubEntry CoreAccountingSubEntry : listAccountingRuleSubEntry) {
                            if (x4080BO.getAtomicActionNumber()
                                    .equals(CoreAccountingSubEntry.getEngineElement1())) {
                                x4080BO.setAtomicActionNumberDesc(CoreAccountingSubEntry.getEngineElement1Desc());
                            }
                            if (x4080BO.getAmountTypeNumber().equals(CoreAccountingSubEntry.getEngineElement2())) {
                                x4080BO.setAmountTypeNumberDesc(CoreAccountingSubEntry.getEngineElement2Desc());
                            }
                        }
                    }
                }
                // 币种
                if (StringUtil.isNotEmpty(x4080BO.getPostingCurrencyCode())) {
                    x4080BO.setCurrencyDesc(getCurrencyDesc(x4080BO.getPostingCurrencyCode(), coreCurrencies));
                }
                listBO.add(x4080BO);
            }
            page.setRows(listBO);
        }
        return page;
    }

    @Override
    public Object requestQueryList(X4080BO x4080bo) throws Exception {
        // 全局流水号
        String globalSerialNumber = x4080bo.getGlobalSerialNumber();
        // 记账规则代码
        String accountingRuleCode = x4080bo.getAccountingRuleCode();
        // 子表序号
        String subTableSequence = x4080bo.getSubTableSequence();
        // 入账日期 yyyy-MM-dd
        String postingDate = x4080bo.getPostingDate();
        // 账号
        String accountId = x4080bo.getAccountId();
        // 证件类型
        String idType = x4080bo.getIdType();
        // 证件号码
        String idNumber = x4080bo.getIdNumber();
        // 外部识别号
        String externalIdentificationNo = x4080bo.getExternalIdentificationNo();

        CoreAccountingInterfaceSqlBuilder CoreAccountingInterfaceSqlBuilder = new CoreAccountingInterfaceSqlBuilder();

        if (StringUtil.isNotBlank(globalSerialNumber)) {
            CoreAccountingInterfaceSqlBuilder.andGlobalSerialNumberEqualTo(globalSerialNumber);
        }
        if (StringUtil.isNotBlank(accountingRuleCode)) {
            CoreAccountingInterfaceSqlBuilder.andAccountingRuleCodeEqualTo(accountingRuleCode);
        }
        if (StringUtil.isNotBlank(subTableSequence)) {
            CoreAccountingInterfaceSqlBuilder.andSubTableSequenceEqualTo(subTableSequence);
        }
        if (StringUtil.isNotBlank(postingDate)) {
            CoreAccountingInterfaceSqlBuilder.andPostingDateEqualTo(postingDate);
        }
        if (StringUtil.isNotBlank(accountId)) {
            CoreAccountingInterfaceSqlBuilder.andAccountIdEqualTo(accountId);
        }

        CoreCustomer coreCustomer = null;
        String customerNo = "";
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            CoreAccountingInterfaceSqlBuilder.andAccountIdLikeRigth(customerNo);
        } else if (object instanceof CoreMediaBasicInfo) {
            CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                CoreAccountingInterfaceSqlBuilder.andAccountIdLikeRigth(customerNo);
            }
        }

        List<CoreAccountingInterface> list = coreAccountingInterfaceDao
                .selectListBySqlBuilder(CoreAccountingInterfaceSqlBuilder);
        if (null != list && StringUtil.isNotBlank(x4080bo.getRedisKey())) {
            CacheUtils.getInstance().addMap(CoreAccountingInterface.class, x4080bo.getRedisKey(), list);
        }
        return list;
    }

    /**
     * 匹配币种描述
     * 
     * @param currencyCode
     *            币种编码
     * @param coreCurrencies
     *            币种列表
     * @return 币种描述
     */
    private String getCurrencyDesc(String currencyCode, List<CoreCurrency> coreCurrencies) {
        CoreCurrency coreCurrency;
        Optional<CoreCurrency> first = coreCurrencies.stream().parallel()
                .filter(x -> StringUtils.equals(x.getCurrencyCode(), currencyCode)).findFirst();
        if (first.isPresent()) {
            coreCurrency = first.get();
            return coreCurrency.getCurrencyDesc();
        }
        return null;
    }
}
