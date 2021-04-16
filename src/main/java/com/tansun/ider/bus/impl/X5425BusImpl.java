package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5425Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5425BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 客户业务标签建立
 * @author: wt
 */
@Service
public class X5425BusImpl implements X5425Bus {

    @Autowired
    private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
	public OperationModeUtil operationModeUtil;
    
    //  新增一条记录
    public static final String addOneFlag  = "1";
    //  新增多条记录
    public static final String addListFlag = "2";
    
    @Override
    public Object busExecute(X5425BO x5425bo) throws Exception {
    	
    	CoreSystemUnit coreSystemUnit = null;
    	String addFlag  = x5425bo.getAddFlag();
    	String customerNo = x5425bo.getCustomerNo();
    	// 获取当前日志标识，生成非金融活动日志
        String operatorId = x5425bo.getOperatorId();
        if (operatorId == null) {
            operatorId = "system";
        }
    	if (addListFlag.equals(addFlag)) {
    		List<CoreCustomerBusinessType> coreCustomerBusinessTypeList = x5425bo.getCoreCustomerBusinessTypeList();
    		if (null != coreCustomerBusinessTypeList && !coreCustomerBusinessTypeList.isEmpty()) {
    			ArrayList<CoreCustomerBusinessType> listCoreCustomerBusinessTypes = new ArrayList<CoreCustomerBusinessType>();
    			for (CoreCustomerBusinessType coreCustomerBusinessType : coreCustomerBusinessTypeList) {
    				 CoreCustomerBusinessType coreCustomerBusinessType1 = new CoreCustomerBusinessType();
    				 CachedBeanCopy.copyProperties(coreCustomerBusinessType,coreCustomerBusinessType1);
    				 coreCustomerBusinessType1.setCustomerNo(customerNo);
    				 coreCustomerBusinessType1.setId(RandomUtil.getUUID());
    				 coreCustomerBusinessType1.setVersion(1);
		 	         if (StringUtil.isBlank(coreCustomerBusinessType.getPricingLevelCode())) {
		 	        	coreCustomerBusinessType1.setPricingLevelCode(x5425bo.getCustomerNo());
		 	         }
		 	         // 判断客户定价标签是否存在
		 	         existCustomerBusinessType(coreCustomerBusinessType1);
		 	         // 若不存在，则插入数据
		 	         coreCustomerBusinessType1.setId(RandomUtil.getUUID());
		 	         coreCustomerBusinessType1.setGmtCreate(new Date());
		 	         coreSystemUnit = operationModeUtil.getcoreOperationMode(x5425bo.getCustomerNo());
		 			 String operationDate = "";
		 			 if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
		 				operationDate = coreSystemUnit.getCurrProcessDate();
		 			 } else {
		 				operationDate = coreSystemUnit.getNextProcessDate();
		 			 }
		 			 //定价区域为"V-动态定价"，则将客户定价标签表中的状态赋值为"S-系统"
		 			 String pricingType = x5425bo.getPricingType();
		 			 if("V".equals(pricingType)){
		 				coreCustomerBusinessType1.setState(Constant.FLAG_S);
		 			 } else{
		 				coreCustomerBusinessType1.setState(Constant.FLAG_Y);
		 			 }
		 			coreCustomerBusinessType1.setSettingUpUserid(x5425bo.getOperatorId());
		 			coreCustomerBusinessType1.setSettingDate(operationDate);
		 			coreCustomerBusinessType1.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
		 			listCoreCustomerBusinessTypes.add(coreCustomerBusinessType1);
				}
    			coreCustomerBusinessTypeDao.insertUseBatch(listCoreCustomerBusinessTypes);
			}
    	}else {
    		CoreCustomerBusinessType coreCustomerBusinessType = new CoreCustomerBusinessType();
 	        coreCustomerBusinessType.setVersion(1);
 	        CachedBeanCopy.copyProperties(x5425bo,coreCustomerBusinessType);
 	        if (StringUtil.isBlank(coreCustomerBusinessType.getPricingLevelCode())) {
 	            coreCustomerBusinessType.setPricingLevelCode(x5425bo.getCustomerNo());
 	        }
 	        // 判断客户定价标签是否存在
 	        existCustomerBusinessType(coreCustomerBusinessType);
 	        // 若不存在，则插入数据
 	        coreCustomerBusinessType.setId(RandomUtil.getUUID());
 	        coreCustomerBusinessType.setGmtCreate(new Date());
 	        coreSystemUnit = operationModeUtil.getcoreOperationMode(x5425bo.getCustomerNo());
 			String operationDate = "";
 			if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
 				operationDate = coreSystemUnit.getCurrProcessDate();
 			} else {
 				operationDate = coreSystemUnit.getNextProcessDate();
 			}
 			//定价区域为"V-动态定价"，则将客户定价标签表中的状态赋值为"S-系统"
 			String pricingType = x5425bo.getPricingType();
 			if("V".equals(pricingType)){
 			    coreCustomerBusinessType.setState(Constant.FLAG_S);
 			}else{
 			    coreCustomerBusinessType.setState(Constant.FLAG_Y);
 			}
 			coreCustomerBusinessType.setSettingUpUserid(x5425bo.getOperatorId());
 	        coreCustomerBusinessType.setSettingDate(operationDate);
 	        coreCustomerBusinessType.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
 	        coreCustomerBusinessTypeDao.insert(coreCustomerBusinessType);
 	        nonFinancialLogUtil.createNonFinancialActivityLog(x5425bo.getEventNo(), x5425bo.getActivityNo(),
 	                ModificationType.ADD.getValue(), null, null, coreCustomerBusinessType, coreCustomerBusinessType.getId(),
 	                coreSystemUnit.getCurrLogFlag(), operatorId, coreCustomerBusinessType.getCustomerNo(),
 	                coreCustomerBusinessType.getCustomerNo(), null, null);
		}
        // 清除本地缓存
        return "ok";
    }

    /**
     * 判断该客户定价标签是否存在，若存在则提示
     * 
     * @param coreCustomerBusinessType
     * @throws Exception
     */
    private void existCustomerBusinessType(CoreCustomerBusinessType coreCustomerBusinessType) throws Exception {
        CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
        coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(coreCustomerBusinessType.getCustomerNo());
        coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(coreCustomerBusinessType.getPricingLevelCode());
        coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(coreCustomerBusinessType.getPricingTag());
        List<CoreCustomerBusinessType> list = coreCustomerBusinessTypeDao
                .selectListBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
        if (list != null && list.size() > 0) {
            throw new BusinessException("PARAM-00003", "该客户定价标签");
        }
    }

}
