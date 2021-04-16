package com.tansun.ider.bus.impl;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Resource;
import com.tansun.ider.util.CachedBeanCopy;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.bus.X5811Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreSecondCancelAbnormal;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreSecondCancelAbnormalSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5811BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.QueryCustomerServiceImpl;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreSecondCancelAbnormalDaoImpl;

@Service
public class X5811BusImpl implements X5811Bus{
	
	@Resource
	private CoreProductDao coreProductDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreProductDaoImpl coreProductDaoImpl;
	@Autowired
	private QueryCustomerServiceImpl queryCustomerServiceImpl;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreSecondCancelAbnormalDaoImpl coreSecondCancelAbnormalDaoImpl;
	
	@Override
	public Object busExecute(X5811BO x5811bo) throws Exception {
		EventCommArea eventCommArea = new EventCommArea();
		//对客户单个产品进行处理
		String customerNo = x5811bo.getCustomerNo();
		String productObjectCode = x5811bo.getProductObjectCode();
		if(customerNo ==null || customerNo.length()==0){
			throw new BusinessException("无法获取客户号");
		}
		if(productObjectCode ==null || productObjectCode.length()==0){
			throw new BusinessException("无法获取产品对象代码");
		}
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
		if(null == coreProduct){
			throw new BusinessException("无法获取产品单元基本信息");
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if(null == coreCustomer){
			throw new BusinessException("无法获取客户基本信息");
		}
		//获取当前处理日
  		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo()); 
  		String currProcessDate = "";
		if("EOD".equals(coreSystemUnit.getSystemOperateState())){
			currProcessDate = coreSystemUnit.getCurrProcessDate();
		}else if("NOR".equals(coreSystemUnit.getSystemOperateState())){
			currProcessDate = coreSystemUnit.getNextProcessDate();
		}else{
			throw new BusinessException("该系统单元为0");
		}
  		
		String statusCode = coreProduct.getStatusCode();//获取客户产品状态
		String operationMode = coreProduct.getOperationMode();//获取运营模式
		String productCancelDate = coreProduct.getProductCancelDate();//获取产品无效日期
		if("5".equals(statusCode)){
			eventCommArea.setEcommOperMode(operationMode);
			eventCommArea.setEcommProdObjId(productObjectCode);
			Iterator<Map.Entry<String, String>> it = null;
			CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
			Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_423, eventCommArea);
			it = elePcdResultMap.entrySet().iterator();
			while (it!=null&&it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (Constant.SECOND_CANCEL.equals(entry.getKey())) { // 423AAA0100
					long setDate = this.setDate(currProcessDate,productCancelDate);
    				//如果（当前系统处理日-产品注销日期）>423AAA01 PCD设定天数，
	    			if(setDate>Long.valueOf(entry.getValue())){
	    				Map<String,String> map = queryCustomerServiceImpl.checkProductLogout(customerNo,productObjectCode);
	    				String isAllowCancel = "";
	    				String abnormalReasonCode = "";
	    				String abnormalReasonDesc = "";
	    				if(map != null && map.size() !=0){
	    					for(Map.Entry<String, String> mapString:map.entrySet()){
	    						String key = mapString.getKey();
	    						String value = mapString.getValue();
	    						if(key.equals("allowCancel")){//获取是否允许注销
	    							isAllowCancel = value;
	    						}else if(key.equals("0000")){
	    							abnormalReasonCode = key;
	    							abnormalReasonDesc = value;
	    						}else if(key.equals("0001")){
	    							abnormalReasonCode = key;
	    							abnormalReasonDesc = value;
	    						}else if(key.equals("0002")){
	    							abnormalReasonCode = key;
	    							abnormalReasonDesc = value;
	    						}else if(key.equals("0003")){
	    							abnormalReasonCode = key;
	    							abnormalReasonDesc = value;
	    						}else if(key.equals("0004")){
	    							abnormalReasonCode = key;
	    							abnormalReasonDesc = value;
	    						}
	    					}
	    				}else{
	    					throw new BusinessException("无法获取需要注销产品账户余额信息");
	    				}
	    				
    					String operatorId = "admin";
    					x5811bo.setActivityNo("X5811");
    					x5811bo.setEventNo("BSS.BN.50.0026");
    					
	    				if("Y".equals(isAllowCancel)){//成功更新客户产品状态、更新关闭日期
	    					CoreProduct coreProductAfter = new CoreProduct();
	    					CachedBeanCopy.copyProperties(coreProduct, coreProductAfter);
	    					coreProduct.setStatusCode("8");
	    					coreProduct.setCloseDate(currProcessDate);
	    					coreProduct.setVersion(coreProduct.getVersion()+1);
	    					
	    					int result = coreProductDaoImpl.updateBySqlBuilderSelective(coreProduct,
	    							coreProductSqlBuilder);
	    					if (result != 1) {
	    						throw new BusinessException("CUS-00012", " 产品单元基本信息");
	    					}
	    					//记录非金融日志
	    					nonFinancialLogUtil.createNonFinancialActivityLog(x5811bo.getEventNo(),x5811bo.getActivityNo(),ModificationType.UPD.getValue(), null,
	    							coreProductAfter,coreProduct,coreProduct.getId(),coreSystemUnit.getCurrLogFlag(),
	    							operatorId,coreProduct.getCustomerNo(),coreProduct.getCustomerNo(),null,null);
	    				}else if("N".equals(isAllowCancel)){
	    					CoreSecondCancelAbnormal coreSecondCancelAbnormal = new CoreSecondCancelAbnormal();
	    					CachedBeanCopy.copyProperties(coreProduct, coreSecondCancelAbnormal);
	    					coreSecondCancelAbnormal.setId(RandomUtil.getUUID());
	    					coreSecondCancelAbnormal.setVersion(1);
	    					coreSecondCancelAbnormal.setAbnormalReasonCode(abnormalReasonCode);
	    					coreSecondCancelAbnormal.setAbnormalReasonDesc(abnormalReasonDesc);
	    					
	    					int result1 = coreSecondCancelAbnormalDaoImpl.insert(coreSecondCancelAbnormal);
	    					
	    					//记录非金融日志
	    			        nonFinancialLogUtil.createNonFinancialActivityLog(x5811bo.getEventNo(), x5811bo.getActivityNo(), ModificationType.ADD.getValue(),
	    			                null, null, coreCustomer, coreCustomer.getId(), coreSystemUnit.getCurrLogFlag(), operatorId, customerNo, customerNo, null,
	    			                null);
	    				}else{
	    					throw new BusinessException("无法获取时候撤销标志位");
	    				}
	    			}
				}else if (Constant.NO_SECOND_CANCEL.equals(entry.getKey())) { // 423AAA0199
					//不做处理
				}
			}
		}else{
			return "";
		}
		return "";
		
	}
	
	/**
	 * 计算当前处理日与产品注销日期差值
	 * @param dateString
	 * @return
	 */
	private long setDate(String currProcessDate,String productCancelDate) throws Exception{
		if(productCancelDate != null && productCancelDate.length()!=0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long currDate = sdf.parse(currProcessDate).getTime();
			long  proCancellDate = sdf.parse(productCancelDate).getTime();
			//计算间隔多少天，则除以毫秒到天的转换公式
	        long betweenDate = (currDate - proCancellDate) / (1000 * 60 * 60 * 24); 
	        return betweenDate;
		}else{
			throw new BusinessException("注销产品无效日期为空");
		}
	}
	
}