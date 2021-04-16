package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.discovery.converters.Auto;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5438Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.CanInstalmentAccountMapper;
import com.tansun.ider.enums.TransState;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5438BO;
import com.tansun.ider.model.vo.X5438VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CachedBeanCopy;

import org.springframework.util.CollectionUtils;

/**
 * 可分期交易历史查询
 * @author liuyanxi
 *
 */
@Service
public class X5438BusImpl implements X5438Bus {

    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreCustomerBillDayDao coreCustomerBillDayDao;// 客户业务项目表
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private CoreTransHistDao coreTransHistDao;
    @Autowired
    private CanInstalmentAccountMapper canInstalmentAccountMapper;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;// 媒介基本信息
    @Resource
    private HttpQueryServiceImpl httpQueryServiceImpl;

    @Override
    public Object busExecute(X5438BO x5438bo) throws Exception {
        String externalIdentificationNo = x5438bo.getExternalIdentificationNo();
        String customerNo = queryCustomerService.queryCoreMediaBasicInfo(x5438bo.getIdNumber(), x5438bo.getExternalIdentificationNo());
        CoreMediaBasicInfo coreMedia = this.queryCustomerMedia(externalIdentificationNo);
        String operationMode = coreMedia.getOperationMode();
        String productObjectCode = coreMedia.getProductObjectCode();
        List<CoreProductBusinessScope> productBusinessScope = httpQueryServiceImpl.queryProductBusinessScope(productObjectCode, operationMode);
        String businessProgramNo = productBusinessScope.get(0).getBusinessProgramNo();
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreTransHistSqlBuilder.andCycleNumberEqualTo(coreCustomerBillDay.getCurrentCycleNumber());
        //coreTransHistSqlBuilder.andCurrencyCodeNotEqualTo("999");
        coreTransHistSqlBuilder.andActivityNoEqualTo("X8010");
        coreTransHistSqlBuilder.andBalanceTypeEqualTo("P");
        coreTransHistSqlBuilder.andLogLevelEqualTo("A");
        coreTransHistSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        coreTransHistSqlBuilder.andEventNoEqualTo("ISS.PT.40.0001");
        CoreTransHistSqlBuilder coreTransHistSqlBuilderTemp = new CoreTransHistSqlBuilder();
        coreTransHistSqlBuilderTemp.orTransStateEqualTo(TransState.STATE_NOR.getValue());
        coreTransHistSqlBuilderTemp.orTransStateEqualTo(TransState.STATE_PIN.getValue());
        CoreTransHistSqlBuilder coreTransHistSqlBuilderTemp1 = new CoreTransHistSqlBuilder();
        coreTransHistSqlBuilderTemp1.orSubLogLevelEqualTo("Y");
        coreTransHistSqlBuilderTemp1.orSubLogLevelIsNull();
        coreTransHistSqlBuilder.and(coreTransHistSqlBuilderTemp).and(coreTransHistSqlBuilderTemp1);
        if (null != x5438bo.getStartDate() && null != x5438bo.getEndDate()) {
            if (x5438bo.getEndDate().compareTo(x5438bo.getStartDate()) <= 0) {
                throw new BusinessException("COR-12038");
            }
            coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(x5438bo.getStartDate());
            coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(x5438bo.getEndDate());
        }
        Integer totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
        PageBean<X5438VO> page = new PageBean<>();
        page.setTotalCount(totalCount);
        if (null != x5438bo.getPageSize() && null != x5438bo.getIndexNo()) {
        	coreTransHistSqlBuilder.setPageSize(x5438bo.getPageSize());
        	coreTransHistSqlBuilder.setIndexNo(x5438bo.getIndexNo());
            page.setPageSize(x5438bo.getPageSize());
            page.setIndexNo(x5438bo.getIndexNo());
        }
        boolean changeCycleNumberMark = x5438bo.isChangeCycleNumberMark();
        if (totalCount > 0) {
            List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
            List<X5438VO> listVo = new ArrayList<>();
            for (CoreTransHist coreTransHist : list) {
            	X5438VO x5438vo = new X5438VO();
            	CachedBeanCopy.copyProperties(coreTransHist, x5438vo);
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
                amountConversion(x5438vo, coreOperationMode.getAccountCurrency());
                if (changeCycleNumberMark) {
                    changeCycleNumber(x5438vo);
                }
                if (StringUtil.isNotEmpty(x5438vo.getBusinessTypeCode())) {
                    // 获取业务类型描述
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                    		coreTransHist.getBusinessTypeCode());
                    if (coreBusinessType != null) {
                    	x5438vo.setBusinessDesc(coreBusinessType.getBusinessDesc());
                    }
                }
                if (StringUtil.isNotEmpty(coreTransHist.getBalanceObjectCode())) {
                    // 获取余额对象代码描述
                    CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode,
                            x5438vo.getBalanceObjectCode());
                    if (coreBalanceObject != null) {
                        x5438vo.setObjectDesc(coreBalanceObject.getObjectDesc());
                    }
                }
                // 获取币种描述
                if (StringUtil.isNotEmpty(x5438vo.getPostingCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5438vo.getPostingCurrencyCode());
                    if (null != coreCurrency) {
                        x5438vo.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5438vo.getCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5438vo.getCurrencyCode());
                    if (null != coreCurrency) {
                        x5438vo.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                if (StringUtil.isNotEmpty(x5438vo.getTransCurrCde())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5438vo.getTransCurrCde());
                    if (null != coreCurrency) {
                        x5438vo.setTransCurrDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                listVo.add(x5438vo);
            }
            //增加如果是分期综合管理交易分期页面过来的，过滤一下
            boolean ifTrans = x5438bo.isIfTrans();
            if(ifTrans){
                //根据条件过滤
            	listVo = chooseTransStage(listVo);
            }
            page.setRows(listVo);
        }
        return page;
    }

    private void amountConversion(X5438VO x5438vo, String accountCurrency) throws Exception {
        if (x5438vo.getTransAmount() != null && !BigDecimal.ZERO.equals(x5438vo.getTransAmount())) {
            Integer transCurrPoint = x5438vo.getTransCurrPoint();
            if (transCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getTransCurrCde())) {
                    queryCurrencyCode = x5438vo.getTransCurrCde();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency transCurrCde = httpQueryService.queryCurrency(queryCurrencyCode);
                transCurrPoint = transCurrCde.getDecimalPosition();
            }
            BigDecimal transAmount = CurrencyConversionUtil.reduce(x5438vo.getTransAmount(), transCurrPoint);
            x5438vo.setTransAmount(transAmount);
            if (x5438vo.getAdjustedAmount() != null && !BigDecimal.ZERO.equals(x5438vo.getAdjustedAmount())) {
                BigDecimal adjustedAmount = CurrencyConversionUtil.reduce(x5438vo.getAdjustedAmount(),transCurrPoint);
                x5438vo.setAdjustedAmount(adjustedAmount);
            }
        }

        if (x5438vo.getPostingAmount() != null && !BigDecimal.ZERO.equals(x5438vo.getPostingAmount())) {
            Integer postingCurrencyPoint = x5438vo.getPostingCurrencyPoint();
            if (postingCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getPostingCurrencyCode())) {
                    queryCurrencyCode = x5438vo.getPostingCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency postingCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                postingCurrencyPoint = postingCurrencyCode.getDecimalPosition();
            }
            BigDecimal postingAmount = CurrencyConversionUtil.reduce(x5438vo.getPostingAmount(),
                    postingCurrencyPoint);
            x5438vo.setPostingAmount(postingAmount);
        }

        if (x5438vo.getSettlementAmount() != null
                && !BigDecimal.ZERO.equals(x5438vo.getSettlementAmount())) {
            Integer settlementCurrencyPoint = x5438vo.getSettlementCurrencyPoint();
            if (settlementCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getSettlementCurrencyCode())) {
                    queryCurrencyCode = x5438vo.getSettlementCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settlementCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                settlementCurrencyPoint = settlementCurrencyCode.getDecimalPosition();
            }
            BigDecimal settlementAmount = CurrencyConversionUtil.reduce(x5438vo.getSettlementAmount(),
                    settlementCurrencyPoint);
            x5438vo.setSettlementAmount(settlementAmount);
        }

        if (x5438vo.getActualPostingAmount() != null
                && !BigDecimal.ZERO.equals(x5438vo.getActualPostingAmount())
                && StringUtil.isNotBlank(x5438vo.getCurrencyCode())) {
            CoreCurrency currencyCode = httpQueryService.queryCurrency(x5438vo.getCurrencyCode());
            int currencyCodePoint = currencyCode.getDecimalPosition();
            BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(x5438vo.getActualPostingAmount(),
                    currencyCodePoint);
            x5438vo.setActualPostingAmount(actualPostingAmount);
        }

        if (x5438vo.getSettleDistriAmount() != null
                && !BigDecimal.ZERO.equals(x5438vo.getSettleDistriAmount())) {
            Integer settleDistriCurrPoint = x5438vo.getSettleDistriCurrPoint();
            if (settleDistriCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getSettleDistriCurrency())) {
                    queryCurrencyCode = x5438vo.getSettleDistriCurrency();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settleDistriCurrency = httpQueryService.queryCurrency(queryCurrencyCode);
                settleDistriCurrPoint = settleDistriCurrency.getDecimalPosition();
            }
            BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(x5438vo.getSettleDistriAmount(),
                    settleDistriCurrPoint);
            x5438vo.setSettleDistriAmount(settleDistriAmount);
        }

        if (x5438vo.getOverpayFrzAmount() != null
                && !BigDecimal.ZERO.equals(x5438vo.getOverpayFrzAmount())) {
            Integer overpayFrzCurrPoint = x5438vo.getOverpayFrzCurrPoint();
            if (overpayFrzCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getOverpayFrzCurrCode())) {
                    queryCurrencyCode = x5438vo.getOverpayFrzCurrCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }

                CoreCurrency overpayFrzCurrCode = httpQueryService.queryCurrency(queryCurrencyCode);
                overpayFrzCurrPoint = overpayFrzCurrCode.getDecimalPosition();
            }
            BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(x5438vo.getOverpayFrzAmount(),
                    overpayFrzCurrPoint);
            x5438vo.setOverpayFrzAmount(overpayFrzAmount);
        }

        if (x5438vo.getInstallmentAmount() != null && !BigDecimal.ZERO.equals(x5438vo.getInstallmentAmount())) {
            Integer postingCurrencyPoint = x5438vo.getPostingCurrencyPoint();
            if (postingCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(x5438vo.getPostingCurrencyCode())) {
                    queryCurrencyCode = x5438vo.getPostingCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency postingCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                postingCurrencyPoint = postingCurrencyCode.getDecimalPosition();
            }
            BigDecimal installmentAmount = CurrencyConversionUtil.reduce(x5438vo.getInstallmentAmount(),
                    postingCurrencyPoint);
            x5438vo.setInstallmentAmount(installmentAmount);
        }else{
            x5438vo.setInstallmentAmount(BigDecimal.ZERO);
        }


    }

    /**
     *
     *
     * @MethodName changeCycleNumber
     * @Description: 从余额单元里查询建立周期号替换掉交易历史的周期号
     * @param coreTransHist
     * @param x5438VO
     * @throws Exception
     * @return: void
     */
    private void changeCycleNumber(X5438VO x5438vo) throws Exception {
        String balanceUnitCode = x5438vo.getEntityKey();
        if (StringUtil.isNotBlank(balanceUnitCode)) {
            CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
            coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
            CoreBalanceUnit coreBalanceUnit = coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
            if (coreBalanceUnit == null) {
                // 余额单元信息为空
                throw new BusinessException("CUS-00032");
            }
            x5438vo.setCycleNumber(coreBalanceUnit.getCycleNumber());
        } else {
            throw new BusinessException("CUS-00133");
        }

    }

    /**
     * 查询客户下所有有效媒介
     *
     * @param externalIdentificationNo
     * @return
     * @throws Exception
     */
    private CoreMediaBasicInfo queryCustomerMedia(String externalIdentificationNo) throws Exception {
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(Constant.MEDIA_INVALID_FLAG);
        coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if (null == coreMediaBasicInfo) {
            throw new BusinessException("COR-00001");
        }
        return coreMediaBasicInfo;
    }


    private List<X5438VO>  chooseTransStage(List<X5438VO> list){
        List<X5438VO> newlist = new ArrayList<>();
           for (X5438VO x5438VO : list) {
               String eventNo = x5438VO.getEventNo();
               String transState = x5438VO.getTransState();
               if ("ISS.PT.40.0001".equals(eventNo) && ("NOR".equals(transState) || "PIN".equals(transState))) {
                   newlist.add(x5438VO);
               }
           }
        return  newlist;
    }
}
