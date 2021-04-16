package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4040Bus;
import com.tansun.ider.dao.issue.*;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.X4045VO;
import com.tansun.ider.model.bo.X4040BO;
import com.tansun.ider.model.bo.X4045BO;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p> Title: X4040BusImpl </p>
 * <p> Description: 个人公务卡最大额度查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
@Service
public class X4040BusImpl implements X4040Bus {
	@Resource
	private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
	@Resource
	private HttpQueryServiceByGns httpQueryServiceByGns;
	@Resource
	private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreProductDao coreProductDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${global.target.service.url.nofn}")
	private String nofnUrl;
	@Value("${gns.global.target.service.url.nofn}")
	private String gnsnofnUrl;
	
	
	
    @Override
    public Object busExecute(X4040BO x4040bo) throws Exception {
    	X4045VO x4045vo = null;
    	String externalIdentificationNo = x4040bo.getExternalIdentificationNo();
    	// 根据客外部识别号查找媒介信息
    	CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerMedia(externalIdentificationNo);
    	// 根据媒介信息查找产品信息
    	CoreProduct coreProduct = queryProduct(coreMediaBasicInfo.getMainCustomerNo(),coreMediaBasicInfo.getProductObjectCode());
    	// 查找客户信息
    	/*CoreCustomer coreCustomer = queryCustomer(coreMediaBasicInfo);*/
    	// 判断是否在所在片区
    	boolean flag = false;
    	if("ider-nofn".equals(applicationName)){
    		flag = true;
    	}
    	else{
    		String gnsArea = applicationName.substring(applicationName.length()-3, applicationName.length());
    		if(gnsArea.equals(coreProduct.getBudgetOrgArea()) || StringUtil.isBlank(coreProduct.getBudgetOrgArea())){
    			flag = true;
    		}
    	}
    	
    	if (flag) {
			// 查找当前片区
		 /*  CoreCustomer coreCustomer = queryCustomer(coreProduct.getBudgetOrgCode());
		   CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = queryBudgetOrgAddInfo(coreCustomer);
		   CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
	       int currencyDecimal = cardUtil.getCurrencyDecimal("156");
	   	   coreBudgetOrgAddInfo.setOrgAllQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getOrgAllQuota(), currencyDecimal));
	   	   coreBudgetOrgAddInfo.setPersonMaxQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getPersonMaxQuota(), currencyDecimal));
	       x4045vo = new X4045VO();
		   CachedBeanCopy.copyProperties(x4045vo, coreCustomer);
		   CachedBeanCopy.copyProperties(x4045vo, coreBudgetOrgAddInfo);*/
    		// 5.4 调用活动
            ActionService coreService = (ActionService) SpringUtil.getBean("X4045");
            // 内部方法转换为JSON后进行传递
            HashMap<String, Object> paramsMap = new HashMap<String, Object>();
            X4045BO x4045BO = new X4045BO();
            x4045BO.setIdNumber(coreProduct.getBudgetOrgCode());
            paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, JSON.toJSONString(x4045BO, SerializerFeature.DisableCircularReferenceDetect));
            return coreService.execute(paramsMap);
		}else{
			// 调用gns接口
			x4045vo = httpQueryServiceByGns.queryBudgetOrgCustInfoByGns(gnsnofnUrl, coreProduct.getBudgetOrgCode());
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

	private CoreCustomer queryCustomer(String budgetOrgCode) throws Exception {
		CoreCustomerSqlBuilder coreCustomerSqlBuilder  = new CoreCustomerSqlBuilder();
       coreCustomerSqlBuilder.andIdNumberEqualTo(budgetOrgCode);
        coreCustomerSqlBuilder.andIdTypeEqualTo("7");
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if(coreCustomer == null){
        	//TODO 预算单位不存在
        	throw new BusinessException("CUS-12043");
        }
		return coreCustomer;
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
    
    /**
     * 查找产品信息
     *
     * @param customer
     * @throws Exception
     */
    private CoreProduct queryProduct(String customerNo,String productCode) throws Exception{
    	CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
    	coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
    	coreProductSqlBuilder.andProductObjectCodeEqualTo(productCode);
    	CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
    	if (null == coreProduct) {
			//TODO 产品信息不存在
    		throw new BusinessException("CUS-00006");
		}
    	return coreProduct;
    }    
 
}
