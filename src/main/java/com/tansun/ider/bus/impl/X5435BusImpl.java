package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.*;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5435Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingStatus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5435BO;
import com.tansun.ider.model.vo.X5435VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 交易历史查询
 * 
 * @author lianhuan 2018年10月15日
 */
@Service
public class X5435BusImpl implements X5435Bus {

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
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Override
    public Object busExecute(X5435BO x5435bo) throws Exception {
    	String subLogLevel = x5435bo.getSubLogLevel();
    	String idNumber = x5435bo.getIdNumber();
        String externalIdentificationNo = x5435bo.getExternalIdentificationNo();
        String eventNo = x5435bo.getEventNo();
        String activityNo = x5435bo.getActivityNo();
        String logLevel = x5435bo.getLogLevel();
        String globalSerialNumbr = x5435bo.getGlobalSerialNumbr();
        String globalSerialNoRel = x5435bo.getGlobalSerialNumbrRelative();
        String accountId = x5435bo.getAccountId();
        String currencyCode = x5435bo.getCurrencyCode();
        String queryType = x5435bo.getQueryType();
        String startDate = x5435bo.getStartDate();
        String endDate = x5435bo.getEndDate();
        String transProperty = x5435bo.getTransProperty();
        String balanceType = x5435bo.getBalanceType();
        String businessType = x5435bo.getBusinessType();
        String accFlag = x5435bo.getAccFlag();
        String operationMode = "";
        String customerNo = queryCustomerService.queryCoreMediaBasicInfo(idNumber, externalIdentificationNo);
        List<String> eventNoList = x5435bo.getEventNoList();
        String eventNoLikeStr = x5435bo.getEventNoLikeStr();
        String entrys = Constant.EMPTY_LIST;
        boolean changeCycleNumberMark = x5435bo.isChangeCycleNumberMark();
        PageBean<X5435VO> page = new PageBean<>();
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        if (accFlag != null && accFlag.equals("mainAcc")) {
            if (accountId != null && currencyCode != null) {
                CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
                coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
                coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
                CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
                if ("Q".equals(coreAccount.getSubAccIdentify())) {
                    coreTransHistSqlBuilder.andSubLogLevelEqualTo("LMT");
                }
                if ("L".equals(coreAccount.getSubAccIdentify())) {
                    coreTransHistSqlBuilder.andSubLogLevelEqualTo("LON");
                }
                if ("P".equals(coreAccount.getSubAccIdentify()) || "S".equals(coreAccount.getSubAccIdentify())) {
                    CoreTransHistSqlBuilder coreTransHistSqlBuilder1 = new CoreTransHistSqlBuilder();
                    coreTransHistSqlBuilder1.orSubLogLevelEqualTo("Y");
                    coreTransHistSqlBuilder1.orSubLogLevelIsNull();
                    coreTransHistSqlBuilder.and(coreTransHistSqlBuilder1);
                }
            }
        } else {
            if (StringUtil.isNotEmpty(subLogLevel)) {
                coreTransHistSqlBuilder.andSubLogLevelEqualTo(subLogLevel);
            } else {
                coreTransHistSqlBuilder.andSubLogLevelIsNull();
            }
        }
        if (StringUtil.isNotEmpty(eventNoLikeStr)) {
            coreTransHistSqlBuilder.andEventNoLikeBoth(eventNoLikeStr);
        }
        if (StringUtil.isNotEmpty(customerNo)) {
            coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotEmpty(externalIdentificationNo)) {
            if (Constant.FLAG_Y.equals(x5435bo.getFlag())) {
                this.dealCardNoSearch(externalIdentificationNo, coreTransHistSqlBuilder, x5435bo);
            } else {
                coreTransHistSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            }
        }
        if (StringUtil.isNotEmpty(globalSerialNumbr)) {
            coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
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
        //不显示PT.27.9999
        coreTransHistSqlBuilder.andEventNoNotLikeBoth("PT.27.9999");
        if(StringUtil.isNotEmpty(businessType)){
        	if("00".equals(businessType)){ //争议交易
        		CoreTransHistSqlBuilder newTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.05");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.06");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.07");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.05");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.06");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.07");
        		coreTransHistSqlBuilder.and(newTransHistSqlBuilder);
        	}else if("01".equals(businessType)){ //借记交易
        		CoreTransHistSqlBuilder newTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.12");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.40");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.60");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.12");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.40");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.60");
        		coreTransHistSqlBuilder.and(newTransHistSqlBuilder);
        	}else if("02".equals(businessType)){ //贷记交易
        		CoreTransHistSqlBuilder newTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.13");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.41");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.43");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.61");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.13");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.41");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.43");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.61");
        		coreTransHistSqlBuilder.and(newTransHistSqlBuilder);
        	}else if("03".equals(businessType)){ //还款/还款还原
        		CoreTransHistSqlBuilder newTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.20");
        		newTransHistSqlBuilder.orEventNoLikeBoth("PT.27");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.20");
        		newTransHistSqlBuilder.orEventNoLikeBoth("RT.27");
        		coreTransHistSqlBuilder.and(newTransHistSqlBuilder);
        	}else{ //分期交易
        		CoreTransHistSqlBuilder newTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        		newTransHistSqlBuilder.orEventNoLikeBoth("XT.00");
        		coreTransHistSqlBuilder.and(newTransHistSqlBuilder);
        	}
        }
        if (StringUtil.isNotEmpty(queryType)) {
            if ("1".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoNotEqualTo(eventNo);
            } else if ("2".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoEqualTo(eventNo);
            } else if ("3".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoLikeLeft(eventNo);
                // coreTransHistSqlBuilder.andEventNoEqualTo(eventNo);
                coreTransHistSqlBuilder.andTransPropertyEqualTo("O");
            } else if ("4".equals(queryType)) {
                coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().orActivityNoEqualTo("X8040")
                        .orActivityNoEqualTo("X8050").orActivityNoEqualTo("X8060"));
            } else if ("5".equals(queryType)) {
            	coreTransHistSqlBuilder.andEventNoLikeBoth(eventNo);
            } else if ("6".equals(queryType)) {
                coreTransHistSqlBuilder.andEventNoNotEqualTo(eventNo);
                coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().orActivityNoEqualTo("X8040")
                        .orActivityNoEqualTo("X8050").orActivityNoEqualTo("X8060"));
            }     
        }
        if (eventNoList != null && eventNoList.size() > 0) {
            CoreTransHistSqlBuilder secondCoreTransHistSqlBuilder = null;
            if (eventNoList.size() == 1) {
                coreTransHistSqlBuilder.andEventNoEqualTo(eventNoList.get(0));
            }else{
                secondCoreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
            }
            for (int index = 0; index < eventNoList.size() && eventNoList.size() > 1; index++) {
                String eventNoStr = eventNoList.get(index);
                secondCoreTransHistSqlBuilder.orEventNoEqualTo(eventNoStr);
            }
            if (eventNoList.size() > 1) {
                coreTransHistSqlBuilder.and(secondCoreTransHistSqlBuilder);
            }
        }
        if (StringUtil.isNotEmpty(startDate)) {
            coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(startDate);
        }
        if (StringUtil.isNotEmpty(endDate)) {
            coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(endDate);
        }
        if (StringUtil.isNotEmpty(logLevel)) {
            coreTransHistSqlBuilder.andLogLevelEqualTo(logLevel);
        }
        if (StringUtil.isNotEmpty(globalSerialNoRel)) {
            coreTransHistSqlBuilder.andGlobalSerialNumbrRelativeEqualTo(globalSerialNoRel);
        }

        if (StringUtil.isNotEmpty(activityNo)) {
            //判断是否有分隔符
            if(activityNo.indexOf("|")>-1){
                CoreTransHistSqlBuilder transHistSqlBuilder = new CoreTransHistSqlBuilder();
                String []str = activityNo.split("\\|");
                for(int i=0;i<str.length;i++){
                    transHistSqlBuilder.orActivityNoEqualTo(str[i]);
                }
                coreTransHistSqlBuilder.and(transHistSqlBuilder);
            }else {
                coreTransHistSqlBuilder.andActivityNoEqualTo(activityNo);
            }
        }

        coreTransHistSqlBuilder.orderByOccurrDate(false);
        coreTransHistSqlBuilder.orderByOccurrTime(false);
        int totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5435bo.getPageSize() && null != x5435bo.getIndexNo()) {
            coreTransHistSqlBuilder.orderByOccurrDate(false);
            coreTransHistSqlBuilder.setPageSize(x5435bo.getPageSize());
            coreTransHistSqlBuilder.setIndexNo(x5435bo.getIndexNo());
            page.setPageSize(x5435bo.getPageSize());
            page.setIndexNo(x5435bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
            List<X5435VO> listVO = new ArrayList<X5435VO>();
            X5435VO x5435VO = null;
            for (CoreTransHist coreTransHist : list) {
                x5435VO = new X5435VO();
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
                CachedBeanCopy.copyProperties(coreTransHist, x5435VO);
                x5435VO.setOperationMode(operationMode);
                if (StringUtil.isNotEmpty(x5435VO.getBusinessTypeCode())) {
                    // 获取业务类型描述
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                            x5435VO.getBusinessTypeCode());
                    if (coreBusinessType != null) {
                        x5435VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5435VO.getBalanceObjectCode())) {
                    // 获取余额对象代码描述
                    CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode,
                            x5435VO.getBalanceObjectCode());
                    if (coreBalanceObject != null) {
                        x5435VO.setObjectDesc(coreBalanceObject.getObjectDesc());
                    }
                }
                // 获取币种描述
                if (StringUtil.isNotEmpty(x5435VO.getPostingCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5435VO.getPostingCurrencyCode());
                    if (null != coreCurrency) {
                        x5435VO.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5435VO.getCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5435VO.getCurrencyCode());
                    if (null != coreCurrency) {
                        x5435VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5435VO.getTransCurrCde())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5435VO.getTransCurrCde());
                    if (null != coreCurrency) {
                        x5435VO.setTransCurrDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                
                if(StringUtil.isNotEmpty(x5435VO.getAccountingStatusCode())){
                	CoreAccountingStatus accountingStatus = httpQueryService.queryCoreAccountingStatus(operationMode,x5435VO.getAccountingStatusCode());
                	if(accountingStatus!=null){
                		x5435VO.setAccountingStatusCodeDesc(accountingStatus.getAccountingDesc());
                	}
                }
                listVO.add(x5435VO);
            }
            page.setRows(listVO);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5435bo.getCoreEventActivityRel().getEventNo(),
                x5435bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5435bo.getOperatorId());
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
        
        if (null != coreTransHist.getRejectedAmount() && !BigDecimal.ZERO.equals(coreTransHist.getRejectedAmount())) {
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
            BigDecimal rejectedAmount = CurrencyConversionUtil.reduce(coreTransHist.getRejectedAmount(),
                    postingCurrencyPoint);
            coreTransHist.setRejectedAmount(rejectedAmount);
        }

    }

    /**
     * 
     *
     * @MethodName changeCycleNumber
     * @Description: 从余额单元里查询建立周期号替换掉交易历史的周期号
     * @param coreTransHist
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
    /**
     * 
    *
    * @MethodName dealCardNoSearch 
    * @Description: 外部识别号查询是进行特殊处理 
    * @param externalIdentificationNo
    * @param coreTransHistSqlBuilder
    * @param x5435bo
    * @throws Exception  
    * @return: void
     */
    private CoreTransHistSqlBuilder dealCardNoSearch(String externalIdentificationNo,CoreTransHistSqlBuilder coreTransHistSqlBuilder,X5435BO x5435bo) throws Exception{
        if (StringUtil.isNotBlank(externalIdentificationNo)) {       
            String reproductObjectCode = "";
            String operationMode = "";
            String productObjectCode = "";
            String customerNo="";
            Object object = queryCustomerService.queryCustomer(null, null, externalIdentificationNo);
            if(object instanceof CoreCustomer){
                CoreCustomer coreCustomer = (CoreCustomer)object;
                customerNo = coreCustomer.getCustomerNo();
                operationMode = coreCustomer.getOperationMode();
            }else if(object instanceof CoreMediaBasicInfo){
                CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                operationMode = coreMediaBasicInfo.getOperationMode();
                productObjectCode = coreMediaBasicInfo.getProductObjectCode();
            }
            if (StringUtil.isNotBlank(customerNo)) {
                coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
            }
            EventCommArea eventCommArea = new EventCommArea();
            eventCommArea.setEcommOperMode(operationMode);//运营模式
            eventCommArea.setEcommProdObjId(productObjectCode);//产品对象代码
            List<CoreActivityArtifactRel> artifactList = x5435bo.getActivityArtifactList();
            if (null != artifactList && !artifactList.isEmpty()) {
            reproductObjectCode = this.getProObjCode(eventCommArea, artifactList);
             //集中核算
             if("0".equals(reproductObjectCode)){
                 coreTransHistSqlBuilder.andProductObjectCodeEqualTo("0");
               
             }else{//反之，正常进行赋值操作
                 coreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
             }
             List<CoreProductBusinessScope> coreProductBusinessScopeList =httpQueryService.queryProductBusinessScope(productObjectCode, operationMode);
             if (null != coreProductBusinessScopeList && !coreProductBusinessScopeList.isEmpty()) {
                 CoreTransHistSqlBuilder coreTransHistSqlBuilder1 = new CoreTransHistSqlBuilder();
                 for (CoreProductBusinessScope coreProductBusinessScope : coreProductBusinessScopeList) {
                    String businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
                    if (StringUtil.isNotBlank(businessProgramNo)) {
                        coreTransHistSqlBuilder1.orBusinessProgramCodeEqualTo(businessProgramNo);
                    }
                }
                coreTransHistSqlBuilder.and(coreTransHistSqlBuilder1);
             }
            }
        }  
        return coreTransHistSqlBuilder;
    }   
    private String getProObjCode(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList) throws Exception {
        String productOjectCode = "";
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                // 单独核算
                productOjectCode = eventCommArea.getEcommProdObjId();
            } else {
                // 集中核算，产品对象置空
                productOjectCode = "0";
            }
        }
        return productOjectCode;
    }
}
