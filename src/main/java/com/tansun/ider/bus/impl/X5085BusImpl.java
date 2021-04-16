package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5085Bus;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreCustomerRemarksDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreCustomerPersonInfo;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerPersonInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerPersonInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerRemarksSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.bo.X5085BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.NonFinancialLogUtil;

@Service
public class X5085BusImpl implements X5085Bus {

	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreCustomerPersonInfoDaoImpl coreCustomerPersonInfoDaoImpl;
	@Autowired
	private CoreCustomerAddrDao coreCustomerAddrDao;
	@Autowired
	private CoreCustomerRemarksDao coreCustomerRemarksDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	
	
	@Override
	public Object busExecute(X5085BO x5085bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		//  将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5085bo, eventCommAreaNonFinance);
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		String idType = eventCommAreaNonFinance.getIdType();
		String customerName = eventCommAreaNonFinance.getCustomerName();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		String customerNo1 = "";
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idNumber) && StringUtil.isNotBlank(idType)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			if (StringUtil.isNotBlank(customerName)) {
				coreCustomerSqlBuilder.andCustomerNameEqualTo(customerName);
			}
			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null == coreCustomer) {
				throw new BusinessException("CUS-00014", "客户基本信息");
			}else {
				customerNo1 = coreCustomer.getCustomerNo();
			}
		}
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00014", "媒介单元基本信息");
			}else {
				customerNo1 = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
		CachedBeanCopy.copyProperties(coreCustomer, eventCommAreaNonFinance);
		coreCustomer.setPaymentMark(x5085bo.getPaymentMark());
		coreCustomer.setVersion(coreCustomer.getVersion() + 1);
		int result = coreCustomerDaoImpl.updateBySqlBuilderSelective(coreCustomer, coreCustomerSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "客户基本信息");
		}
		
		/**
		 * 内容：客户快捷申请新建客户时，手机号应该要必输，而且在法人内需唯一
		 * 		[一个法人有多个客户，每个客户的手机号不能相同]
         * 版本：R0400.SIT
         * 时间：2019/05/28
         * add by wangxi
		 */
		//根据所属机构去查询得到法人实体编号,因为需要根据法人实体编号来做判断条件
        String organNo = x5085bo.getInstitutionId();
        String corporationEntityNo = "";
        if(null != organNo){
        	CoreOrgan coreOrgan = httpQueryService.queryOrgan(organNo);
        	//法人实体编号
        	corporationEntityNo = coreOrgan.getCorporationEntityNo();
        }
        //手机号码
		String mobilePhone = eventCommAreaNonFinance.getMobilePhone();
		if (StringUtil.isNotBlank(mobilePhone)){
			//根据请求参数“手机号码”去判断该手机号在数据库中是否存在，如果存在则报错，不存在则修改
		    CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilderQuery = new CoreCustomerPersonInfoSqlBuilder();
		    //修改操作时，没有修改手机号码报手机号已存在，所以修改为，首先判断该客户下是否有手机号，如果手机号和传入的手机号一致时，不进校验。
		    coreCustomerPersonInfoSqlBuilderQuery.andCustomerNoEqualTo(customerNo1);
		    CoreCustomerPersonInfo coreCustomerPersonInfo1 = coreCustomerPersonInfoDaoImpl.selectBySqlBuilder(coreCustomerPersonInfoSqlBuilderQuery);
		    Boolean flag = true;
		    if(null != coreCustomerPersonInfo1){
		    	String phone = coreCustomerPersonInfo1.getMobilePhone();
		    	if(StringUtil.isNotBlank(phone) && phone.equals(mobilePhone)){
		    		flag = false;
		    	}
		    }
		    if(flag){
		    	CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilder = new CoreCustomerPersonInfoSqlBuilder();
		    	coreCustomerPersonInfoSqlBuilder.andMobilePhoneEqualTo(mobilePhone);
			    List<CoreCustomerPersonInfo> coreCustomerPersonInfoQueryList =
			            coreCustomerPersonInfoDaoImpl.selectListBySqlBuilder(coreCustomerPersonInfoSqlBuilder);
			    //查询结果不为空，判断法人内手机号是否唯一，如果数据库中已经存在，抛错
			    if (null != coreCustomerPersonInfoQueryList && coreCustomerPersonInfoQueryList.size() > 0){
			    	String customerNo = "";
			    	CoreCustomerSqlBuilder coreCustomerSqlBuilderQuery = new CoreCustomerSqlBuilder();
			    	if(StringUtil.isNotBlank(corporationEntityNo)){
			    		coreCustomerSqlBuilderQuery.andCorporationEntityNoEqualTo(corporationEntityNo);
			    	}
			    	
			    	for (CoreCustomerPersonInfo coreCustomerPersonInfo : coreCustomerPersonInfoQueryList) {
			    		customerNo = coreCustomerPersonInfo.getCustomerNo();
			    		coreCustomerSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
			    		coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilderQuery);
			    		if(null != coreCustomer){
			    				throw new BusinessException("CUS-00097");//法人手机号重复，请重新输入
			    			
			    		}
					}
			    } 
		    }
		    
		    
		    
		    //执行修改客户个人信息表的操作
		    CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilder = new CoreCustomerPersonInfoSqlBuilder();
		    coreCustomerPersonInfoSqlBuilder.andCustomerNoEqualTo(customerNo1);
		    CoreCustomerPersonInfo coreCustomerPersonInfo = coreCustomerPersonInfoDaoImpl
		    		.selectBySqlBuilder(coreCustomerPersonInfoSqlBuilder);
		    CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreCustomerPersonInfo);
		    coreCustomerPersonInfo.setVersion(coreCustomerPersonInfo.getVersion() + 1);
		    int resultCode = coreCustomerPersonInfoDaoImpl.updateBySqlBuilderSelective(coreCustomerPersonInfo,
		    		coreCustomerPersonInfoSqlBuilder);
		    if (resultCode != 1) {
		    	throw new BusinessException("CUS-00012", "客户个人信息表");
		    }
			
		} else {
		    throw new BusinessException("CUS-00098");//请输入手机号
		}
		
		
		List<CoreCustomerAddr> coreCustomerAddrs = eventCommAreaNonFinance.getCoreCoreCustomerAddrs();
		if (coreCustomerAddrs != null && !coreCustomerAddrs.isEmpty()) {
			for (CoreCustomerAddr coreCustomerAddr : coreCustomerAddrs) {
				CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
				coreCustomerAddrSqlBuilder.andIdEqualTo(coreCustomerAddr.getId());
				CoreCustomerAddr coreCustomerAddrStr = coreCustomerAddrDao
						.selectBySqlBuilder(coreCustomerAddrSqlBuilder);
				CachedBeanCopy.copyProperties(coreCustomerAddrStr, coreCustomerAddrStr);
				coreCustomerAddrStr.setVersion(coreCustomerAddrStr.getVersion() + 1);
				int resultAddr = coreCustomerAddrDao.updateBySqlBuilderSelective(coreCustomerAddrStr,
						coreCustomerAddrSqlBuilder);
				if (resultAddr != 1) {
					throw new BusinessException("CUS-00012", "客户地址信息！");
				}
			}
		}
		
		String operatorId = x5085bo.getOperatorId();    //获取操作员ID
		if (operatorId == null) {
			operatorId = "system";
		}
		
//		String customerNo1 = eventCommAreaNonFinance.getCustomerNo();
//		CoreCustomerRemarks coreCustomerRemarks1 = null;
//		if (StringUtil.isNotBlank(customerNo)) {
//			CoreCustomerRemarksSqlBuilder coreCustomerRemarksSqlBuilder = new CoreCustomerRemarksSqlBuilder();
//			coreCustomerRemarksSqlBuilder.andCustomerNoEqualTo(customerNo);
//			coreCustomerRemarks1 = coreCustomerRemarksDao.selectBySqlBuilder(coreCustomerRemarksSqlBuilder);
//		}
//		
//		CoreCustomerRemarks coreCustomerRemarksAfter = new CoreCustomerRemarks();
//		CachedBeanCopy.copyProperties(coreCustomerRemarksAfter, coreCustomerRemarks1);
//		coreCustomerRemarks1.setVersion(coreCustomerRemarks1.getVersion() + 1);
		
		List<CoreCustomerRemarks> coreCustomerRemarkss = eventCommAreaNonFinance.getCoreCustomerRemarkss();
		//备注信息，已存在的不做修改，不存在的新增
		if (coreCustomerRemarkss != null && coreCustomerRemarkss.size() > 0) {
			for (CoreCustomerRemarks coreCustomerRemarks : coreCustomerRemarkss) {
				//获取该客户号最大周期号
				int maxSerialNo = 0;
				CoreCustomerRemarksSqlBuilder coreCustomerRemarksSerialNo = new CoreCustomerRemarksSqlBuilder();
				List<CoreCustomerRemarks> list = coreCustomerRemarksDao.selectListBySqlBuilder(coreCustomerRemarksSerialNo.
						andCustomerNoEqualTo(customerNo1));
				if(null != list && list.size() != 0){
					for(CoreCustomerRemarks coreCustomerRemark : list){
						if(coreCustomerRemark.getSerialNo()>maxSerialNo){
							maxSerialNo = coreCustomerRemark.getSerialNo();
						}
					}
				}
				if(StringUtil.isNotBlank(customerNo1)){
					CoreCustomerRemarksSqlBuilder coreCustomerRemarksSqlBuilder = new CoreCustomerRemarksSqlBuilder();
					coreCustomerRemarksSqlBuilder.andCustomerNoEqualTo(coreCustomerRemarks.getCustomerNo());
					coreCustomerRemarksSqlBuilder.andSerialNoEqualTo(coreCustomerRemarks.getSerialNo());
					CoreCustomerRemarks coreCustomerRemarksStr = coreCustomerRemarksDao
							.selectBySqlBuilder(coreCustomerRemarksSqlBuilder);
					if(null == coreCustomerRemarksStr){
						coreCustomerRemarks.setId(RandomUtil.getUUID());
						coreCustomerRemarks.setCustomerNo(customerNo1);
						coreCustomerRemarks.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
						coreCustomerRemarks.setVersion(1);
						coreCustomerRemarks.setSerialNo(maxSerialNo+1);
						coreCustomerRemarksDao.insert(coreCustomerRemarks);
						
						if(null != coreCustomer){
							//通过系统单元编号获取当前日志标识
							String systemUnitNo = coreCustomer.getSystemUnitNo();
							CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
							String currLogFlag = coreSystemUnit.getCurrLogFlag();
							x5085bo.setCurrLogFlag(currLogFlag);
							nonFinancialLogUtil.createNonFinancialActivityLog(x5085bo.getEventNo(), x5085bo.getActivityNo(), ModificationType.UPD.getValue(), null, null,
									coreCustomerRemarks, coreCustomerRemarks.getId(), x5085bo.getCurrLogFlag(),operatorId , customerNo1, customerNo1, null,null);
							}
					}
				}
			}
		}
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		
		/**
		 * 同步授权：
		 * 活动触发事件表-识别维度代码recog_dimen_code字段为null的删掉，触发事件调用的解决方法
		 * add by wangxi 2019/7/22
		 */
		eventCommAreaNonFinance.setWhetherProcess("");
		List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
		AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
		CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
		authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
		eventCommAreaTriggerEventList.add(triggerEventParams);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		
		return eventCommAreaNonFinance;
	}
}
