package com.tansun.ider.bus.impl;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.bus.X4045Bus;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.X4045VO;
import com.tansun.ider.model.bo.X4045BO;
import com.tansun.ider.util.CardUtil;

/**
 * <p> Title: X4045BusImpl </p>
 * <p> Description: 单位公务卡额度调整</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
@Service
public class X4045BusImpl implements X4045Bus {
	@Resource
	private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	
    @Override
    public Object busExecute(X4045BO x4045bo) throws Exception {
		    CoreCustomer coreCustomer = queryCustomer(x4045bo.getIdNumber(),x4045bo.getIdType());
		    X4045VO x4045vo = new X4045VO();
		    if ("7".equals(coreCustomer.getIdType())) {
			   CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = queryBudgetOrgAddInfo(coreCustomer);
			   CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
			   int currencyDecimal = cardUtil.getCurrencyDecimal("156");
			   coreBudgetOrgAddInfo.setOrgAllQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getOrgAllQuota(), currencyDecimal));
			   coreBudgetOrgAddInfo.setPersonMaxQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getPersonMaxQuota(), currencyDecimal));
			   CachedBeanCopy.copyProperties(coreCustomer, x4045vo);
			   CachedBeanCopy.copyProperties(coreBudgetOrgAddInfo, x4045vo);
	    	}else if ("1".equals(coreCustomer.getIdType())) {
	    	   CoreMediaBasicInfo coreMediaBasicInfo = queryMediaInfo(x4045bo.getExternalIdentificationNo());
	    	   CachedBeanCopy.copyProperties(coreMediaBasicInfo, x4045vo);
	    	   CachedBeanCopy.copyProperties(coreCustomer, x4045vo);
	    	}
		   return x4045vo;
    }

	private CoreBudgetOrgAddInfo queryBudgetOrgAddInfo(CoreCustomer coreCustomer) throws Exception {
		CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
	        coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
	        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
	        if(coreBudgetOrgAddInfo == null){
	        	//TODO 预算单位信息不存在
	        	throw new BusinessException("CUS-12043");
	        }
	        return coreBudgetOrgAddInfo;
	}
    
	private CoreCustomer queryCustomer(String budgetOrgCode, String idType) throws Exception {
		CoreCustomerSqlBuilder coreCustomerSqlBuilder  = new CoreCustomerSqlBuilder();
       coreCustomerSqlBuilder.andIdNumberEqualTo(budgetOrgCode);
        coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if(coreCustomer == null){
        	//TODO 客户信息不存在
        	throw new BusinessException("CUS-00005");
        }
		return coreCustomer;
	} 
	
	private CoreMediaBasicInfo queryMediaInfo(String externalIdentificationNo) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (null == coreMediaBasicInfo) {
			//TODO 媒介信息不存在
			throw new BusinessException("CUS-00018");
		}
		return coreMediaBasicInfo;
	}
}
