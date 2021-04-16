package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5515Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreCustomerDelinquencyDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelinquency;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelinquencySqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5515BO;
import com.tansun.ider.model.vo.X5515VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;

/**
 * 客户延滞状况查询
 * 
 * @author lianhuan 2018年10月25日
 */
@Service
public class X5515BusImpl implements X5515Bus {
    private static final Integer DELAY_MAX_CYCLE_NO = 9999;
    private static final Integer LAST_DELAY_MAX_CYCLE_NO = 9990;

    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreCustomerDelinquencyDao coreCustomerDelinquencyDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private QueryCustomerService queryCustomerService;

    @Override
    public Object busExecute(X5515BO x5515bo) throws Exception {
        String idNumber = x5515bo.getIdNumber();
        String idType = x5515bo.getIdType();
        String delinquencyLevel = x5515bo.getDelinquencyLevel();
        String levelCode = x5515bo.getLevelCode();
        String productObjectNo = x5515bo.getProductObjectNo();
        String currencyCode = x5515bo.getCurrencyCode();
        String externalIdentificationNo = x5515bo.getExternalIdentificationNo();
        String customerNo = null;
        String entrys = Constant.EMPTY_LIST;
        PageBean<X5515VO> page = new PageBean<>();
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        String operationMode = "";
        if (object instanceof CoreCustomer) {
            CoreCustomer coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
            operationMode = coreMediaBasicInfo.getOperationMode();
        }

        CoreCustomerDelinquencySqlBuilder coreCustomerDelinquencySqlBuilder = new CoreCustomerDelinquencySqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreCustomerDelinquencySqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotEmpty(x5515bo.getType())) {
            if ("1".equals(x5515bo.getType())) {
                // 1代表查询汇总记录
                coreCustomerDelinquencySqlBuilder.andCycleNoEqualTo(DELAY_MAX_CYCLE_NO);
            } else {
                coreCustomerDelinquencySqlBuilder.andCycleNoLessThan(LAST_DELAY_MAX_CYCLE_NO);
                if (StringUtil.isNotEmpty(x5515bo.getId())) {
                    CoreCustomerDelinquencySqlBuilder sqlBuilder = new CoreCustomerDelinquencySqlBuilder();
                    sqlBuilder.andIdEqualTo(x5515bo.getId());
                    CoreCustomerDelinquency coreCustomerDelinquency = coreCustomerDelinquencyDao
                            .selectBySqlBuilder(sqlBuilder);
                    if (null != coreCustomerDelinquency) {
                        delinquencyLevel = coreCustomerDelinquency.getDelinquencyLevel();
                        levelCode = coreCustomerDelinquency.getLevelCode();
                        productObjectNo = coreCustomerDelinquency.getProductObjectNo();
                        currencyCode = coreCustomerDelinquency.getCurrencyCode();
                    }
                }
            }
        }
        if (StringUtil.isNotEmpty(delinquencyLevel)) {
            coreCustomerDelinquencySqlBuilder.andDelinquencyLevelEqualTo(delinquencyLevel);
        }
        if (StringUtil.isNotEmpty(levelCode)) {
            coreCustomerDelinquencySqlBuilder.andLevelCodeEqualTo(levelCode);
        }
        if (StringUtil.isNotEmpty(productObjectNo)) {
            coreCustomerDelinquencySqlBuilder.andProductObjectNoEqualTo(productObjectNo);
        }
        if (StringUtil.isNotEmpty(currencyCode)) {
            coreCustomerDelinquencySqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        int totalCount = coreCustomerDelinquencyDao.countBySqlBuilder(coreCustomerDelinquencySqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5515bo.getPageSize() && null != x5515bo.getIndexNo()) {
            coreCustomerDelinquencySqlBuilder.orderByDelinquencyLevel(false);
            coreCustomerDelinquencySqlBuilder.orderByLevelCode(false);
            coreCustomerDelinquencySqlBuilder.orderByProductObjectNo(false);
            coreCustomerDelinquencySqlBuilder.orderByCurrencyCode(false);
            coreCustomerDelinquencySqlBuilder.orderByCycleNo(false);
            coreCustomerDelinquencySqlBuilder.setPageSize(x5515bo.getPageSize());
            coreCustomerDelinquencySqlBuilder.setIndexNo(x5515bo.getIndexNo());
            page.setPageSize(x5515bo.getPageSize());
            page.setIndexNo(x5515bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreCustomerDelinquency> list = coreCustomerDelinquencyDao
                    .selectListBySqlBuilder(coreCustomerDelinquencySqlBuilder);
            List<X5515VO> listX5515VO = new ArrayList<X5515VO>();
            for (CoreCustomerDelinquency coreCustomerDelinquency : list) {
                X5515VO x5515VO = new X5515VO();
                // 金额转换
                amountConversion(coreCustomerDelinquency, coreCustomerDelinquency.getCurrencyCode(), x5515VO);
                CachedBeanCopy.copyProperties(coreCustomerDelinquency, x5515VO);
                if (StringUtil.isNotBlank(coreCustomerDelinquency.getProductObjectNo())) {
                    CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,
                            coreCustomerDelinquency.getProductObjectNo());

                    if (coreProductObject != null) {
                        x5515VO.setProductDesc(coreProductObject.getProductDesc());
                    }
                }
                String delinLevel = coreCustomerDelinquency.getDelinquencyLevel();
                if (StringUtil.isNotBlank(delinLevel)) {
                    if (delinLevel.equals("A")) {
                        CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                                coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessType) {
                            x5515VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                        }
                    } else if (delinLevel.equals("G")) {
                        CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,
                                coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessProgram) {
                            x5515VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
                        }
                    } else if (delinLevel.equals("P")) {
                        CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,
                                coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessProgram) {
                            // x5515VO.setProductDesc(coreBusinessProgram.getProgramDesc());
                            x5515VO.setProductLevelCodeDesc(coreBusinessProgram.getProgramDesc());
                        }
                    }

                }
                listX5515VO.add(x5515VO);
            }
            page.setRows(listX5515VO);
            if (null != listX5515VO && !listX5515VO.isEmpty()) {
                entrys = listX5515VO.get(0).getId();
            }
            // 记录查询日志
            CoreEvent tempObject = new CoreEvent();
            paramsUtil.logNonInsert(x5515bo.getCoreEventActivityRel().getEventNo(),
                    x5515bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                    x5515bo.getOperatorId());
        }
        return page;
    }

    private void amountConversion(CoreCustomerDelinquency coreCustomerDelinquency, String currencyCode, X5515VO x5515VO)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreCustomerDelinquency.getCurrCyclePaymentMin() != null) {
            BigDecimal occurrAmount = CurrencyConversionUtil.reduce(coreCustomerDelinquency.getCurrCyclePaymentMin(),
                    decimalPlaces);
            coreCustomerDelinquency.setCurrCyclePaymentMin(occurrAmount);
        }
        if (coreCustomerDelinquency.getCurrCycleBeginPaymentMin() != null) {
            BigDecimal currCycleBeginPaymentMin = CurrencyConversionUtil
                    .reduce(coreCustomerDelinquency.getCurrCycleBeginPaymentMin(), decimalPlaces);
            coreCustomerDelinquency.setCurrCycleBeginPaymentMin(currCycleBeginPaymentMin);
        }
        if (null != coreCurrency) {
            x5515VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }

    }
}
