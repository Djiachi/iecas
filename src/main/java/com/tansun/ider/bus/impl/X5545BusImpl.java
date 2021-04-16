package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5545Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreTransIdNo;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreTransStatisticsDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreTransStatistics;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransStatisticsSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5545BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 客户交易统计查询
 * 
 * @author lianhuan 2018年10月25日
 */
@Service
public class X5545BusImpl implements X5545Bus {

    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreTransStatisticsDao coreTransStatisticsDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Autowired
    private QueryCustomerService queryCustomerService;
	@Autowired
	private ParamsUtil paramsUtil;

    @Override
    public Object busExecute(X5545BO x5545bo) throws Exception {
        String idNumber = x5545bo.getIdNumber();
        String idType = x5545bo.getIdType();
        String externalIdentificationNo = x5545bo.getExternalIdentificationNo();
        String customerNo = null;
        String entrys =Constant.EMPTY_LIST;
        PageBean<X5545BO> page = new PageBean<>();
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
		}
//        if (StringUtil.isNotBlank(idNumber)) {
//            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
//            if (coreCustomer != null) {
//                customerNo = coreCustomer.getCustomerNo();
//            }
//        }
//        if (StringUtil.isNotBlank(externalIdentificationNo)) {
//            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//            coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//            CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
//                    .selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//            if (coreMediaBasicInfo != null) {
//                customerNo = coreMediaBasicInfo.getMainCustomerNo();
//            }
//        }
        CoreTransStatisticsSqlBuilder coreTransStatisticsSqlBuilder = new CoreTransStatisticsSqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreTransStatisticsSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        int totalCount = coreTransStatisticsDao.countBySqlBuilder(coreTransStatisticsSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5545bo.getPageSize() && null != x5545bo.getIndexNo()) {
            coreTransStatisticsSqlBuilder.orderByCycleNo(false);
            coreTransStatisticsSqlBuilder.setPageSize(x5545bo.getPageSize());
            coreTransStatisticsSqlBuilder.setIndexNo(x5545bo.getIndexNo());
            page.setPageSize(x5545bo.getPageSize());
            page.setIndexNo(x5545bo.getIndexNo());
        }
        if (totalCount > 0) {
        	List<X5545BO> x5545BOList = new ArrayList<>();
            List<CoreTransStatistics> list = coreTransStatisticsDao
                    .selectListBySqlBuilder(coreTransStatisticsSqlBuilder);
            for (CoreTransStatistics coreTransStatistics : list) {
            	X5545BO x5545BO = new X5545BO();
            	CachedBeanCopy.copyProperties(coreTransStatistics, x5545BO);
                // 金额转换及币种描述
                amountConversion(coreTransStatistics,x5545BO);
                x5545BOList.add(x5545BO);
            }
            page.setRows(x5545BOList);
			if(null != list && !list.isEmpty()){
				entrys = list.get(0).getId();
			}
			//记录查询日志
			CoreEvent tempObject = new CoreEvent();
			paramsUtil.logNonInsert(x5545bo.getCoreEventActivityRel().getEventNo(), x5545bo.getCoreEventActivityRel().getActivityNo(),
					tempObject, tempObject, entrys, x5545bo.getOperatorId());
        }
        return page;
    }

    private void amountConversion(CoreTransStatistics coreTransStatistics,X5545BO x5545BO) throws Exception {
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(coreTransStatistics.getCustomerNo());
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer == null) {
            throw new BusinessException("CUS-00022");
        }
        String operationMode = coreCustomer.getOperationMode();
        CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
        String currencyCode = null;
        if(StringUtil.isNotBlank(coreTransStatistics.getCurrencyCode())){
        	currencyCode = coreTransStatistics.getCurrencyCode();
        }else{
        	currencyCode = coreOperationMode.getAccountCurrency();
        }
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreTransStatistics.getAccumultTransAmtCredit() != null) {
            BigDecimal accumultTransAmtCredit = CurrencyConversionUtil
                    .reduce(coreTransStatistics.getAccumultTransAmtCredit(), decimalPlaces);
            x5545BO.setAccumultTransAmtCredit(accumultTransAmtCredit);
        }
        if (coreTransStatistics.getAccumultTransAmtDebit() != null) {
            BigDecimal accumultTransAmtDebit = CurrencyConversionUtil
                    .reduce(coreTransStatistics.getAccumultTransAmtDebit(), decimalPlaces);
            x5545BO.setAccumultTransAmtDebit(accumultTransAmtDebit);
        }
        if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5545BO.getProductObjectCode())){
        	CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, x5545BO.getProductObjectCode());
        	if(null != coreProductObject){
        		x5545BO.setProductDesc(coreProductObject.getProductDesc());
        	}
        }
        if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5545BO.getTransIdentifiNo())){
        	CoreTransIdNo coreTransIdNo = httpQueryService.queryTransIdNo(operationMode, x5545BO.getTransIdentifiNo());
        	if(null != coreTransIdNo){
        		x5545BO.setTransIdentifiDesc(coreTransIdNo.getTransIdentifiDesc());
        	}
        }
        if(null != coreCurrency){
        	x5545BO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }
        
    }

}
