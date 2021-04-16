package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5271Bus;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5271BO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 关联套卡查询-只支持根据外部识别号进线查询
 * @author wangxi
 *
 */
@Service
public class X5271BusImpl implements X5271Bus {
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;

    @Override
    public Object busExecute(X5271BO x5271BO) throws Exception {
    	
    	PageBean<CoreMediaBasicInfo> page = new PageBean<>();
        //外部识别号
        String externalIdentificationNo = x5271BO.getExternalIdentificationNo();
        
        /**
         * 1. 获取该外部识别号的产品对象代码
         * 		-根据外部识别号去媒介基本信息表中进行查询操作，得到该卡号的产品对象代码
         */
        List<CoreMediaBasicInfo> coreMediaBasicInfoList = null;
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderQuery = new CoreMediaBasicInfoSqlBuilder();
        if(StringUtil.isNotBlank(externalIdentificationNo)){
        	coreMediaBasicInfoSqlBuilderQuery.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        }
        coreMediaBasicInfoList = coreMediaBasicInfoDaoImpl.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderQuery);
        String customerNo = null;
        String productObjectCode = null;
        String operationMode = null;
        if(null != coreMediaBasicInfoList && coreMediaBasicInfoList.size() > 0){
        	//产品对象代码
        	productObjectCode = coreMediaBasicInfoList.get(0).getProductObjectCode(); 
        	//运营模式
        	operationMode = coreMediaBasicInfoList.get(0).getOperationMode();
        	//客户号
        	customerNo = coreMediaBasicInfoList.get(0).getMainCustomerNo();
        }
        
        /**
         * 2. 根据上述产品对象代码获取套卡对方产品对象代码
         * 		-根据产品对象代码去产品对象表中进行查询操作，得到该产品的套卡对方产品对象代码
         */
        
        CoreProductObject coreProductObject = checkDate(operationMode,productObjectCode);
        //套卡对方产品对象代码
        String productCodeSet = coreProductObject.getProductCodeSet();
        
        /**
         * 3. 根据客户号+套卡对方产品代码获取所有有效的媒介单元并展示
         * 		-该卡和套卡的信息都展示
         */
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
        if(StringUtil.isNotBlank(customerNo)){
        	coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
        }
        if(StringUtil.isNotBlank(productCodeSet)){
        	coreMediaBasicInfoSqlBuilderStr.orProductObjectCodeEqualTo(productCodeSet);
        	coreMediaBasicInfoSqlBuilderStr.orProductObjectCodeEqualTo(productObjectCode);
        }else{
        	coreMediaBasicInfoSqlBuilderStr.andProductObjectCodeEqualTo(productObjectCode);
        }
        coreMediaBasicInfoSqlBuilder.and(coreMediaBasicInfoSqlBuilderStr);
        
        int totalCount = coreMediaBasicInfoDaoImpl.countBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		page.setTotalCount(totalCount);
		
		if (null != x5271BO.getPageSize() && null != x5271BO.getIndexNo()) {
			coreMediaBasicInfoSqlBuilder.setPageSize(x5271BO.getPageSize());
			coreMediaBasicInfoSqlBuilder.setIndexNo(x5271BO.getIndexNo());
			page.setPageSize(x5271BO.getPageSize());
			page.setIndexNo(x5271BO.getIndexNo());
		}
		
		if (totalCount > 0) {
			List<CoreMediaBasicInfo> coreMediaBasicInfoSet =
					coreMediaBasicInfoDaoImpl.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			page.setRows(coreMediaBasicInfoSet);
			
		}
		
		return page; 
    }

	/**
     * 检查产品对象代码
     * @param eventCommAreaNonFinance
     * @throws Exception
     */
    private CoreProductObject checkDate(String operationMode, String productObjectCode) throws Exception {
   	 CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,productObjectCode);
   	        if (coreProductObject == null) {
   	            throw new BusinessException("CUS-00014", "产品对象表"); // 产品对象表
   	        }
   	        return coreProductObject;
	}

}
