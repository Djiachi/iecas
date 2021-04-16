package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5431Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgramScope;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CorePcd;
import com.tansun.ider.dao.beta.entity.CorePriceTag;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5430BO;
import com.tansun.ider.model.vo.X5430VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 客户定价视图查询
 * 
 * @author zhangte
 * @date 2019年8月7日
 */
@Service
public class X5431BusImpl implements X5431Bus {
    private static Logger logger = LoggerFactory.getLogger(X5431BusImpl.class);
    @Autowired
    private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreProductDao coreProductDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5430BO x5430bo) throws Exception {

        String idNumber = x5430bo.getIdNumber();
        String idType = x5430bo.getIdType();
        String externalIdentificationNo = x5430bo.getExternalIdentificationNo();
        String customerNo = "";
        String operationMode = "";
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        CoreCustomer coreCustomer = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                operationMode = coreMediaBasicInfo.getOperationMode();
            }
        }
        PageBean<X5430VO> page = new PageBean<>();

        CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
        coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerBusinessTypeSqlBuilder.andPricingObjectCodeEqualTo(x5430bo.getPricingObjectCode());
        coreCustomerBusinessTypeSqlBuilder.andPricingObjectEqualTo(x5430bo.getPricingObject());
        int totalCount = coreCustomerBusinessTypeDao.countBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5430bo.getPageSize() && null != x5430bo.getIndexNo()) {
            coreCustomerBusinessTypeSqlBuilder.setPageSize(x5430bo.getPageSize());
            coreCustomerBusinessTypeSqlBuilder.setIndexNo(x5430bo.getIndexNo());
            page.setPageSize(x5430bo.getPageSize());
            page.setIndexNo(x5430bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreCustomerBusinessType> listCoreCustomerBusinessType = coreCustomerBusinessTypeDao
                    .selectListBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
            List<X5430VO> list = new ArrayList<X5430VO>();
            for (CoreCustomerBusinessType coreCustomerBusinessType : listCoreCustomerBusinessType) {
                X5430VO x5430VO = new X5430VO();
                // 查询定价标签信息
                CorePriceTag priceTag = httpQueryService.queryPriceTag(operationMode,
                        coreCustomerBusinessType.getPricingTag(), null);
                if (priceTag != null) {
                    addPricingObjectDesc(priceTag,x5430VO);
                    CachedBeanCopy.copyProperties(priceTag, x5430VO);
                }
                CachedBeanCopy.copyProperties(coreCustomerBusinessType, x5430VO);
                list.add(x5430VO);
            }
            page.setRows(list);
        }
        return page;
    }

    /**
     * 
     *
     * @MethodName addProductDesc
     * @Description: 返回对象添加产品描述字段
     * @param list
     * @return: List<X0390VO>
     * @throws Exception
     */
    private X5430VO addPricingObjectDesc(CorePriceTag corePriceTag, X5430VO x5430VO) throws Exception {
        logger.debug("---------X5430BusImpl.addProductDesc    start");
        logger.debug("---------X5430BusImpl.addProductDesc    corePriceTag: " + corePriceTag);
        String pricingObject = corePriceTag.getPricingObject();
        logger.debug("---------X5430BusImpl.addProductDesc    pricingObject: " + pricingObject);
        String pricingObjectCode = corePriceTag.getPricingObjectCode();
        logger.debug("---------X5430BusImpl.addProductDesc    pricingObjectCode: " + pricingObjectCode);
        if (StringUtil.isNotBlank(pricingObjectCode)) {
            String pricingObjectDesc = null;
            if (Constant.TAG_TYPE_P.equals(pricingObject)) {
                CorePcd corePcd = httpQueryService.queryPcdNo(pricingObjectCode);
                logger.debug("---------X5430BusImpl.addProductDesc   corePcd: " + corePcd);
                if (corePcd != null) {
                    pricingObjectDesc = corePcd.getPcdDesc();
                    logger.debug("---------X5430BusImpl.addProductDesc     pricingObjectDesc: " + pricingObjectDesc);
                }
            } else if (Constant.TAG_TYPE_F.equals(pricingObject)) {
                CoreFeeItem coreFeeItem = httpQueryService.queryCoreFeeItem(pricingObjectCode);
                logger.debug("---------X5430BusImpl.addProductDesc   coreFeeItem: " + coreFeeItem);
                if (coreFeeItem != null) {
                    pricingObjectDesc = coreFeeItem.getFeeDesc();
                    logger.debug("---------X5430BusImpl.addProductDesc     pricingObjectDesc: " + pricingObjectDesc);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("---------X5430BusImpl.addProductDesc   pricingObject unmatch  id: "
                            + corePriceTag.getId());
                }
            }
            x5430VO.setPricingObjectDesc(pricingObjectDesc);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("---------X5430BusImpl.addProductDesc   pricingObjectCode is null   id: "
                        + corePriceTag.getId());
            }
        }
        return x5430VO;

    }

	@Override
	public Object instanBusExecute(X5430BO x5430bo) throws Exception {

        String idNumber = x5430bo.getIdNumber();
        String idType = x5430bo.getIdType();
        String externalIdentificationNo = x5430bo.getExternalIdentificationNo();
        String customerNo = "";
        String operationMode = "";
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        CoreCustomer coreCustomer = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                operationMode = coreMediaBasicInfo.getOperationMode();
            }
        }
        
        // 实例代码list
        List<String> pricingLevelCodeList = new ArrayList<>();

        // 查询客户下所有业务项目
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        List<CoreCustomerBillDay> list = coreCustomerBillDayDao
        		.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
        if(null!=list && !list.isEmpty()){
        	// 查询客户下所有业务类型
        	for (CoreCustomerBillDay coreCustomerBillDay : list) {
        		pricingLevelCodeList.add(coreCustomerBillDay.getBusinessProgramNo());
        		List<CoreBusinessProgramScope> listBus = httpQueryService
        				.queryBusinessProgramScope(coreCustomerBillDay.getBusinessProgramNo(), operationMode);
        		if(null!=listBus && !listBus.isEmpty()){
        			for (CoreBusinessProgramScope coreBusinessProgramScope : listBus) {
        				pricingLevelCodeList.add(coreBusinessProgramScope.getBusinessTypeCode());
        			}
        		}
        	}
        }
        // 查询客户下所有产品对象
        CoreProductSqlBuilder coreProductSqlBuilder =new CoreProductSqlBuilder();
        coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
        List<CoreProduct> proList = coreProductDao.selectListBySqlBuilder(coreProductSqlBuilder);
        if(null!=proList && !proList.isEmpty()){
        	for(CoreProduct coreProduct :proList){
        		pricingLevelCodeList.add(coreProduct.getProductObjectCode());
        	}
        }
        // 查询客户下所有媒介对象
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder =new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
        List<CoreMediaBasicInfo> mediaList = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if(null!=mediaList && !mediaList.isEmpty()){
        	for(CoreMediaBasicInfo obj :mediaList){
        		if(!pricingLevelCodeList.contains(obj.getMediaObjectCode())){
        			pricingLevelCodeList.add(obj.getMediaObjectCode());
        		}
        	}
        }
        
        return pricingLevelCodeList;
    }
	
	 @Override
	 public Object pricingBusExecute(X5430BO x5430bo) throws Exception {
		    String idNumber = x5430bo.getIdNumber();
	        String idType = x5430bo.getIdType();
	        String externalIdentificationNo = x5430bo.getExternalIdentificationNo();
	        String customerNo = "";
	        CoreMediaBasicInfo coreMediaBasicInfo = null;
	        CoreCustomer coreCustomer = null;
	        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
	        if (object instanceof CoreCustomer) {
	            coreCustomer = (CoreCustomer) object;
	            customerNo = coreCustomer.getCustomerNo();
	        } else if (object instanceof CoreMediaBasicInfo) {
	            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
	            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
	                customerNo = coreMediaBasicInfo.getMainCustomerNo();
	            }
	        }
	        CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
	        coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
	        coreCustomerBusinessTypeSqlBuilder.andPricingObjectCodeEqualTo(x5430bo.getPricingObjectCode());
	        coreCustomerBusinessTypeSqlBuilder.andPricingObjectEqualTo(x5430bo.getPricingObject());
	        int totalCount = coreCustomerBusinessTypeDao.countBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
			return totalCount;
	 }
}
