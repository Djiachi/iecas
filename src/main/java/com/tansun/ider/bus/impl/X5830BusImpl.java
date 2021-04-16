package com.tansun.ider.bus.impl;

import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5830Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5830BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BatchUtil;
import com.tansun.ider.util.CardUtil;

/**
 * 产品升降级
 * @author wangxi
 *
 */
@Service
public class X5830BusImpl implements X5830Bus {
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreProductDaoImpl coreProductDaoImpl;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;
    @Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
    @Autowired
	private CardUtil cardUtil;
    @Autowired
	private BatchUtil batchUtil;
    @Value("${global.target.service.url.nofn}")
	private String authUrl;

	@Override
    public Object busExecute(X5830BO x5830BO) throws Exception {
    	// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5830BO,eventCommAreaNonFinance);
		CoreEventActivityRel coreEventActivityRel = x5830BO.getCoreEventActivityRel();
		//产品对象代码
		String productObjectCode = x5830BO.getProductObjectCode();
		//产品对象代码-新
		String productObjectCodeNew = x5830BO.getProductObjectCodeNew();
		if(StringUtil.isNotBlank(productObjectCode) && StringUtil.isNotBlank(productObjectCodeNew)){
        	if(productObjectCode.equals(productObjectCodeNew)){
        		throw new BusinessException("选择的产品对象与当前产品对象一致，请重新选择！");
        	}
        }
		String operatorId = x5830BO.getOperatorId();
		
		// 检查产品线是否存在
        String customerNo = eventCommAreaNonFinance.getCustomerNo();
        String idType = eventCommAreaNonFinance.getIdType();
        String idNumber = eventCommAreaNonFinance.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
            coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
        }
        else {
            throw new BusinessException("COR-10047");
        }
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
        String operationMode = coreCustomer.getOperationMode();
        
        /**
         * 2. 根据新产品对象代码去产品对象表中进行查询，得到卡bin
         * 		新选产品的卡BIN需要与旧产品相同，否则提示重新选择
         */
        //产品对象代码
        CoreProductObject coreProductObject = checkDate(operationMode,productObjectCode);
        String binNo = coreProductObject.getBinNo().toString();//发行卡BIN
        //新产品对象代码
        CoreProductObject coreProductObjectNew = checkDate(operationMode,productObjectCodeNew);
        String productCodeSet = coreProductObjectNew.getProductCodeSet();
        String binNoNew = coreProductObjectNew.getBinNo().toString();//发行卡BIN
        if(StringUtil.isNotBlank(binNo) && StringUtil.isNotBlank(binNoNew)){
        	if(!binNoNew.equals(binNo)){
        		throw new BusinessException("选择产品的卡BIN:"+binNoNew+"与当前产品的卡BIN:"+binNo+"不一致，请重新选择！");
        	}
        }
        
        /**
         * 3. 根据客户号+产品对象代码-新 去产品对象表中进行查询
         * 		所选产品对象在客户产品表是否存在,不存在，通过后台新增,存在，继续
         */
        if (operatorId == null) {
            operatorId = "system";
        }
        CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
        if(StringUtil.isNotBlank(customerNo)){
        	coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if(StringUtil.isNotBlank(productObjectCodeNew)){
        	coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCodeNew);
        }
        CoreProduct coreProduct = coreProductDaoImpl.selectBySqlBuilder(coreProductSqlBuilder);
        
        //获取新建日期的值，取系统单元表内下一处理日
        CoreCorporationEntity coreCorporationEntity = cardUtil
				.getSystemUnitNoCoreCorporationEntity(coreCustomer.getInstitutionId());
		CoreSystemUnit systemUnit = httpQueryService.querySystemUnitForBinNo(binNo, coreCorporationEntity,
				operatorId);
        String operationDate = "";
		if (Constant.EOD.equals(systemUnit.getSystemOperateState())) {
			operationDate = systemUnit.getCurrProcessDate();
		} else {
			operationDate = systemUnit.getNextProcessDate();
		}
        
        if(null == coreProduct){//不存在，通过后台新增
        	CoreProduct coreProductAdd = new CoreProduct();
        	coreProductAdd.setId(RandomUtil.getUUID());
        	coreProductAdd.setOperationMode(coreProductObjectNew.getOperationMode());//运营模式
        	coreProductAdd.setStatusCode("1");//状态 1 正常
        	coreProductAdd.setVersion(1);
        	coreProductAdd.setProductObjectCode(productObjectCodeNew);//产品对象代码
        	coreProductAdd.setCustomerNo(customerNo);//客户代码
            if(StringUtil.isNotBlank(productCodeSet)){
            	coreProductAdd.setProductCodeSet(productCodeSet);//套卡对方产品对象代码
    		}
            
    		coreProductAdd.setCreateDate(operationDate);//申请日期赋值
            
            @SuppressWarnings("unused")
			int i = coreProductDaoImpl.insert(coreProductAdd);
        }
        
        /**
         * 进行媒介升降级前循环传值操作，通过活动进行触发媒介升降级事件，传入是单条外部识别号
         */
        this.checkMadiaBasicInfo(eventCommAreaNonFinance,coreEventActivityRel);
        
        
		return eventCommAreaNonFinance;
    }

	/**
	 * 
	 * @param eventCommAreaNonFinance
	 * @throws Exception 
	 */
	private void checkMadiaBasicInfo(EventCommAreaNonFinance eventCommAreaNonFinance,CoreEventActivityRel coreEventActivityRel) throws Exception {
		
		//客户号
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		//产品对象代码
        String productObjectCode = eventCommAreaNonFinance.getProductObjectCode();
		
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		List<CoreMediaBasicInfo> coreMediaBasicInfoList = null;
		if(StringUtil.isNotBlank(customerNo)){
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
		}
		coreMediaBasicInfoList = coreMediaBasicInfoDaoImpl.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(null == coreMediaBasicInfoList || coreMediaBasicInfoList.isEmpty()){
			throw new BusinessException("CUS-00085");
		}
		for (CoreMediaBasicInfo coreMediaBasicInfo : coreMediaBasicInfoList) {
			if (coreMediaBasicInfo.getInvalidFlag().equals("Y") 
			        && coreMediaBasicInfo.getProductObjectCode().equals(productObjectCode)) {//媒介为有效卡并且产品对象代码
				// 执行媒介升降级操作
				eventCommAreaNonFinance.setExternalIdentificationNo(coreMediaBasicInfo.getExternalIdentificationNo());
				eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo.getOperationMode());
				eventCommAreaNonFinance.setEventNo(coreEventActivityRel.getTriggerNo());
				eventCommAreaNonFinance
						.setExternalIdentificationNo(coreMediaBasicInfo.getExternalIdentificationNo());
				eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.CHP.getValue());
				batchUtil.triggerEventNonFinance(eventCommAreaNonFinance, authUrl);
			}
		}
		
		
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
