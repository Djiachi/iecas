package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5436Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5436BO;
import com.tansun.ider.model.vo.X5436VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;

/**
 * 关联交易历史查询
 *
 * @author huangyayun 2018年10月15日
 */
@Service
public class X5436BusImpl implements X5436Bus {

    @Autowired
    private CoreTransHistDao coreTransHistDao;

    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;

    @Override
    public Object busExecute(X5436BO x5436bo) throws Exception {
        String idNumber = x5436bo.getIdNumber();
        String externalIdentificationNo = x5436bo.getExternalIdentificationNo();
        String eventNo = x5436bo.getEventNo();
        String activityNo = x5436bo.getActivityNo();
        String logLevel = x5436bo.getLogLevel();
        String globalSerialNumbr = x5436bo.getGlobalSerialNumbr();
        String globalSerialNoRel = x5436bo.getGlobalSerialNumbrRelative();
        String accountId = x5436bo.getAccountId();
        String currencyCode = x5436bo.getCurrencyCode();
        String queryType = x5436bo.getQueryType();
        String startDate = x5436bo.getStartDate();
        String endDate = x5436bo.getEndDate();
        String transProperty = x5436bo.getTransProperty();
        String balanceType = x5436bo.getBalanceType();
        String operationMode = "";
        String customerNo = queryCustomerService.queryCoreMediaBasicInfo(idNumber, externalIdentificationNo);
        String entrys = Constant.EMPTY_LIST;
        boolean changeCycleNumberMark = x5436bo.isChangeCycleNumberMark();
        PageBean<X5436VO> page = new PageBean<>();
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotEmpty(externalIdentificationNo)) {
        	if (StringUtil.isNotBlank(queryType) && "8".equals(queryType)) {
				
			}else {
				coreTransHistSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			}
            
        }
        if (StringUtil.isNotEmpty(activityNo)) {
            coreTransHistSqlBuilder.andActivityNoEqualTo(activityNo);
        }
        if (StringUtil.isNotEmpty(accountId)) {
            coreTransHistSqlBuilder.andAccountIdEqualTo(accountId);
        }
        if (StringUtil.isNotEmpty(currencyCode)) {
            coreTransHistSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        if (StringUtil.isNotEmpty(transProperty)) {
            coreTransHistSqlBuilder.andTransPropertyEqualTo(transProperty);
        }
        if (StringUtil.isNotEmpty(balanceType)) {
            coreTransHistSqlBuilder.andBalanceTypeEqualTo(balanceType);
        }
                coreTransHistSqlBuilder.andEventNoNotEqualTo(eventNo);
                coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().orActivityNoEqualTo("X8040")
                        .orActivityNoEqualTo("X8050").orActivityNoEqualTo("X8060"));
            
        if (StringUtil.isNotEmpty(startDate)) {
            coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(startDate);
        }
        if (StringUtil.isNotEmpty(endDate)) {
            coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(endDate);
        }
        if (StringUtil.isNotEmpty(logLevel)) {
            coreTransHistSqlBuilder.andLogLevelEqualTo(logLevel);
        }
        if (StringUtil.isEmpty(globalSerialNoRel)) {
            coreTransHistSqlBuilder.andGlobalSerialNumbrRelativeEqualTo(globalSerialNumbr);
        }
        else{
        	CoreTransHistSqlBuilder coreTransHistSqlBuilderTemp = new  CoreTransHistSqlBuilder();
        	CoreTransHistSqlBuilder coreTransHistSqlBuilder1 = new CoreTransHistSqlBuilder().andGlobalSerialNumbrEqualTo(globalSerialNoRel);
        	CoreTransHistSqlBuilder coreTransHistSqlBuilder2 = new CoreTransHistSqlBuilder().andGlobalSerialNumbrRelativeEqualTo(globalSerialNoRel).andGlobalSerialNumbrNotEqualTo(globalSerialNumbr);
        	coreTransHistSqlBuilderTemp.or(coreTransHistSqlBuilder1);
        	coreTransHistSqlBuilderTemp.or(coreTransHistSqlBuilder2);
        	coreTransHistSqlBuilder.and(coreTransHistSqlBuilderTemp);
        }
        coreTransHistSqlBuilder.orderByOccurrDate(false);
        coreTransHistSqlBuilder.orderByOccurrTime(false);
        int totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5436bo.getPageSize() && null != x5436bo.getIndexNo()) {
            coreTransHistSqlBuilder.orderByOccurrDate(false);
            coreTransHistSqlBuilder.setPageSize(x5436bo.getPageSize());
            coreTransHistSqlBuilder.setIndexNo(x5436bo.getIndexNo());
            page.setPageSize(x5436bo.getPageSize());
            page.setIndexNo(x5436bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
            List<X5436VO> listVO = new ArrayList<X5436VO>();
            X5436VO x5436VO = null;
            for (CoreTransHist coreTransHist : list) {
                x5436VO = new X5436VO();
                // 金额转换
                CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
                coreCustomerSqlBuilder.andCustomerNoEqualTo(coreTransHist.getCustomerNo());
                CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
                if (coreCustomer == null) {
                    // 抛出异常CUS-00005客户信息查询失败
                    throw new BusinessException("CUS-00005");
                }
                operationMode = coreCustomer.getOperationMode();
                CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
                amountConversion(coreTransHist, coreOperationMode.getAccountCurrency());
                if (changeCycleNumberMark) {
                    changeCycleNumber(coreTransHist);
                }
                CachedBeanCopy.copyProperties(coreTransHist, x5436VO);
                if (StringUtil.isNotEmpty(x5436VO.getBusinessTypeCode())) {
                    // 获取业务类型描述
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                            x5436VO.getBusinessTypeCode());
                    if (coreBusinessType != null) {
                        x5436VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5436VO.getBalanceObjectCode())) {
                    // 获取余额对象代码描述
                    CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode,
                            x5436VO.getBalanceObjectCode());
                    if (coreBalanceObject != null) {
                        x5436VO.setObjectDesc(coreBalanceObject.getObjectDesc());
                    }
                }
                // 获取币种描述
                if (StringUtil.isNotEmpty(x5436VO.getPostingCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5436VO.getPostingCurrencyCode());
                    if (null != coreCurrency) {
                        x5436VO.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5436VO.getCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5436VO.getCurrencyCode());
                    if (null != coreCurrency) {
                        x5436VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5436VO.getTransCurrCde())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5436VO.getTransCurrCde());
                    if (null != coreCurrency) {
                        x5436VO.setTransCurrDesc(coreCurrency.getCurrencyDesc());
                    }
                }

                listVO.add(x5436VO);
            }
            page.setRows(listVO);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5436bo.getCoreEventActivityRel().getEventNo(),
                x5436bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5436bo.getOperatorId());
        return page;
    }

    private void amountConversion(CoreTransHist coreTransHist, String accountCurrency) throws Exception {
        if (coreTransHist.getTransAmount() != null && !BigDecimal.ZERO.equals(coreTransHist.getTransAmount())) {
            Integer transCurrPoint = coreTransHist.getTransCurrPoint();
            if (transCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getTransCurrCde())) {
                    queryCurrencyCode = coreTransHist.getTransCurrCde();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency transCurrCde = httpQueryService.queryCurrency(queryCurrencyCode);
                transCurrPoint = transCurrCde.getDecimalPosition();
            }
            BigDecimal transAmount = CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(), transCurrPoint);
            coreTransHist.setTransAmount(transAmount);
            if (coreTransHist.getAdjustedAmount() != null && !BigDecimal.ZERO.equals(coreTransHist.getAdjustedAmount())) {
                BigDecimal adjustedAmount = CurrencyConversionUtil.reduce(coreTransHist.getAdjustedAmount(),transCurrPoint);
                coreTransHist.setAdjustedAmount(adjustedAmount);
            } 
        }

        if (coreTransHist.getPostingAmount() != null && !BigDecimal.ZERO.equals(coreTransHist.getPostingAmount())) {
            Integer postingCurrencyPoint = coreTransHist.getPostingCurrencyPoint();
            if (postingCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getPostingCurrencyCode())) {
                    queryCurrencyCode = coreTransHist.getPostingCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency postingCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                postingCurrencyPoint = postingCurrencyCode.getDecimalPosition();
            }
            BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreTransHist.getPostingAmount(),
                    postingCurrencyPoint);
            coreTransHist.setPostingAmount(postingAmount);
        }

        if (coreTransHist.getSettlementAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getSettlementAmount())) {
            Integer settlementCurrencyPoint = coreTransHist.getSettlementCurrencyPoint();
            if (settlementCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getSettlementCurrencyCode())) {
                    queryCurrencyCode = coreTransHist.getSettlementCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settlementCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                settlementCurrencyPoint = settlementCurrencyCode.getDecimalPosition();
            }
            BigDecimal settlementAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettlementAmount(),
                    settlementCurrencyPoint);
            coreTransHist.setSettlementAmount(settlementAmount);
        }

        if (coreTransHist.getActualPostingAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getActualPostingAmount())
                && StringUtil.isNotBlank(coreTransHist.getCurrencyCode())) {
            CoreCurrency currencyCode = httpQueryService.queryCurrency(coreTransHist.getCurrencyCode());
            int currencyCodePoint = currencyCode.getDecimalPosition();
            BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(),
                    currencyCodePoint);
            coreTransHist.setActualPostingAmount(actualPostingAmount);
        }

        if (coreTransHist.getSettleDistriAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getSettleDistriAmount())) {
            Integer settleDistriCurrPoint = coreTransHist.getSettleDistriCurrPoint();
            if (settleDistriCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getSettleDistriCurrency())) {
                    queryCurrencyCode = coreTransHist.getSettleDistriCurrency();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settleDistriCurrency = httpQueryService.queryCurrency(queryCurrencyCode);
                settleDistriCurrPoint = settleDistriCurrency.getDecimalPosition();
            }
            BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettleDistriAmount(),
                    settleDistriCurrPoint);
            coreTransHist.setSettleDistriAmount(settleDistriAmount);
        }

        if (coreTransHist.getOverpayFrzAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getOverpayFrzAmount())) {
            Integer overpayFrzCurrPoint = coreTransHist.getOverpayFrzCurrPoint();
            if (overpayFrzCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getOverpayFrzCurrCode())) {
                    queryCurrencyCode = coreTransHist.getOverpayFrzCurrCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }

                CoreCurrency overpayFrzCurrCode = httpQueryService.queryCurrency(queryCurrencyCode);
                overpayFrzCurrPoint = overpayFrzCurrCode.getDecimalPosition();
            }
            BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayFrzAmount(),
                    overpayFrzCurrPoint);
            coreTransHist.setOverpayFrzAmount(overpayFrzAmount);
        }

    }

    /**
     * 
     *
     * @MethodName changeCycleNumber
     * @Description: 从余额单元里查询建立周期号替换掉交易历史的周期号
     * @param coreTransHist
     * @param x5436VO
     * @throws Exception
     * @return: void
     */
    private void changeCycleNumber(CoreTransHist coreTransHist) throws Exception {
        String balanceUnitCode = coreTransHist.getEntityKey();
        if (StringUtil.isNotBlank(balanceUnitCode)) {
            CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
            coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
            CoreBalanceUnit coreBalanceUnit = coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
            if (coreBalanceUnit == null) {
                // 余额单元信息为空
                throw new BusinessException("CUS-00032");
            }
            coreTransHist.setCycleNumber(coreBalanceUnit.getCycleNumber());
        } else {
            throw new BusinessException("CUS-00133");
        }

    }
}
