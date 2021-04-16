package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4090Bus;
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
import com.tansun.ider.model.bo.X4090BO;
import com.tansun.ider.model.vo.X4090VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 结售汇记录查询
 * 
 * @author yanyingzhao 2019年08月07日
 */
@Service
public class X4090BusImpl implements X4090Bus {
	
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
    public Object busExecute(X4090BO x4090bo) throws Exception {
        String idNumber = x4090bo.getIdNumber();
        String externalIdentificationNo = x4090bo.getExternalIdentificationNo();
        String eventNo = x4090bo.getEventNo();
        String activityNo = x4090bo.getActivityNo();
        String startDate = x4090bo.getStartDate();
        String endDate = x4090bo.getEndDate();
        String queryType = x4090bo.getQueryType();
        String operationMode = "";
        String customerNo = queryCustomerService.queryCoreMediaBasicInfo(idNumber, externalIdentificationNo);
        String entrys = Constant.EMPTY_LIST;
        String logLevel = x4090bo.getLogLevel();
        PageBean<X4090VO> page = new PageBean<>();
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
//        if (StringUtil.isNotEmpty(externalIdentificationNo)) {
//            coreTransHistSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//        }
        if (StringUtil.isNotEmpty(activityNo)) {
            coreTransHistSqlBuilder.andActivityNoEqualTo(activityNo);
        }
        if (StringUtil.isNotEmpty(queryType)) {
            if ("1".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoNotEqualTo(eventNo);
            } else if ("2".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoEqualTo(eventNo);
            } else if ("3".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoLikeLeft(eventNo);
                coreTransHistSqlBuilder.andTransPropertyEqualTo("O");
            } else if ("4".equals(queryType)) {
                coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().orActivityNoEqualTo("X8040")
                        .orActivityNoEqualTo("X8050").orActivityNoEqualTo("X8060"));
            } else if ("5".equals(queryType)) {
            	coreTransHistSqlBuilder.andEventNoLikeBoth(eventNo);
            }else if ("6".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoNotEqualTo(eventNo);
                coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().orActivityNoEqualTo("X8040")
                        .orActivityNoEqualTo("X8050").orActivityNoEqualTo("X8060"));
            }
        }
        if (StringUtil.isNotEmpty(startDate)) {
            coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(startDate);
        }
        if (StringUtil.isNotEmpty(endDate)) {
            coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(endDate);
        }
        if (StringUtil.isNotEmpty(logLevel)){
        	coreTransHistSqlBuilder.andLogLevelEqualTo(logLevel);
        }
        coreTransHistSqlBuilder.andPostingAmountGreaterThan(BigDecimal.ZERO);
        
        //查询运营模式
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer == null) {
            // 抛出异常CUS-00005客户信息查询失败
            throw new BusinessException("CUS-00005");
        }
        operationMode = coreCustomer.getOperationMode();
        String accountCurrency = "";
        if(StringUtil.isNotEmpty(operationMode)){
            //获取运营币种
            CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
            accountCurrency = coreOperationMode.getAccountCurrency();
        }
        
        List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
        List<CoreTransHist> list1 = new ArrayList<CoreTransHist>();
        for(CoreTransHist coreTransHist:list){
        	if(StringUtil.isNotEmpty(coreTransHist.getPostingCurrencyCode()) && StringUtil.isNotEmpty(coreTransHist.getSettlementCurrencyCode()) && StringUtil.isNotEmpty(accountCurrency)){
        		if(!coreTransHist.getPostingCurrencyCode().equals(coreTransHist.getSettlementCurrencyCode()) && !"000".equals(coreTransHist.getSettlementCurrencyCode())){
            		if(coreTransHist.getPostingCurrencyCode().equals(accountCurrency) || coreTransHist.getSettlementCurrencyCode().equals(accountCurrency)){
            			list1.add(coreTransHist);
            		}
            	}
        	}

        }
        int totalCount = list1.size();
        page.setTotalCount(totalCount);
        if (null != x4090bo.getPageSize() && null != x4090bo.getIndexNo()) {
            coreTransHistSqlBuilder.orderByOccurrDate(false);
            coreTransHistSqlBuilder.setPageSize(x4090bo.getPageSize());
            coreTransHistSqlBuilder.setIndexNo(x4090bo.getIndexNo());
            page.setPageSize(x4090bo.getPageSize());
            page.setIndexNo(x4090bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<X4090VO> listVO = new ArrayList<X4090VO>();
            X4090VO x4090VO = null;
            for (CoreTransHist coreTransHist : list1) {
            	x4090VO = new X4090VO();
                // 金额转换
                amountConversion(coreTransHist, accountCurrency);
                CachedBeanCopy.copyProperties(coreTransHist, x4090VO);
                if (StringUtil.isNotEmpty(x4090VO.getBusinessTypeCode())) {
                    // 获取业务类型描述
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                    		x4090VO.getBusinessTypeCode());
                    if (coreBusinessType != null) {
                    	x4090VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x4090VO.getBalanceObjectCode())) {
                    // 获取余额对象代码描述
                    CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode,
                    		x4090VO.getBalanceObjectCode());
                    if (coreBalanceObject != null) {
                    	x4090VO.setObjectDesc(coreBalanceObject.getObjectDesc());
                    }
                }
                // 获取币种描述
                if (StringUtil.isNotEmpty(x4090VO.getPostingCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x4090VO.getPostingCurrencyCode());
                    if (null != coreCurrency) {
                    	x4090VO.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x4090VO.getCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x4090VO.getCurrencyCode());
                    if (null != coreCurrency) {
                    	x4090VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x4090VO.getTransCurrCde())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x4090VO.getTransCurrCde());
                    if (null != coreCurrency) {
                    	x4090VO.setTransCurrDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x4090VO.getEventNo())) {
                	CoreEvent coreEvent = httpQueryService.queryEvent(x4090VO.getEventNo());
                	if(null != coreEvent){
                		x4090VO.setEventDesc(coreEvent.getEventDesc());
                	}
                }

                listVO.add(x4090VO);
            }
            page.setRows(listVO);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x4090bo.getCoreEventActivityRel().getEventNo(),
        		x4090bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
        		x4090bo.getOperatorId());
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

}