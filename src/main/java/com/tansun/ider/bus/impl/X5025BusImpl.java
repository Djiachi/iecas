package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5025Bus;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerPersonInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerPersonInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerPersonInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.bo.X5025BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户个人信息新建活动设计
 * @author: admin
 */
@Service
public class X5025BusImpl implements X5025Bus {

	@Autowired
	private CoreCustomerPersonInfoDaoImpl coreCustomerPersonInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
    @Autowired
    private HttpQueryService httpQueryService;
    
    private static final String BSS_AD_01_0001 = "BSS.AD.01.0001";//客户资料建立
	
	@Override
	public Object busExecute(X5025BO x5025bo) throws Exception {

		//判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5025bo.getIsNew())) {
			if ("1".equals(x5025bo.getIsNew())) {
				return x5025bo;
			}
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5025bo, eventCommAreaNonFinance);
        String operatorId = x5025bo.getOperatorId();

        //根据所属机构去查询得到法人实体编号
        String organNo = x5025bo.getInstitutionId();
        String corporationEntityNo = "";
        if(null != organNo){
        	CoreOrgan coreOrgan = httpQueryService.queryOrgan(organNo);
        	//法人实体编号
        	corporationEntityNo = coreOrgan.getCorporationEntityNo();
        }
        
        /**
         * 内容：客户快捷申请新建客户时，手机号应该要必输，而且在法人内需唯一
         * 版本：R0400.SIT
         * 时间：2019/05/24
         * add by wangxi
         */
		String mobilePhone = x5025bo.getMobilePhone();//手机号码
		if (StringUtil.isNotBlank(mobilePhone)){
		    //根据请求参数“手机号码”去判断该手机号在数据库中是否存在，存在则报错，不存在则新增
		    CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilder = new CoreCustomerPersonInfoSqlBuilder();
		    coreCustomerPersonInfoSqlBuilder.andMobilePhoneEqualTo(mobilePhone);
		    List<CoreCustomerPersonInfo> coreCustomerPersonInfoQueryList =
		            coreCustomerPersonInfoDaoImpl.selectListBySqlBuilder(coreCustomerPersonInfoSqlBuilder);
		    //查询结果不为空，则数据库中已经存在该条数据，抛错
		    if (null != coreCustomerPersonInfoQueryList && coreCustomerPersonInfoQueryList.size() > 0){
		    	
		    	CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		    	String customerNo = "";
		    	CoreCustomer coreCustomer = null;
		    	
		    	if(StringUtil.isNotBlank(corporationEntityNo)){
		    		coreCustomerSqlBuilder.andCorporationEntityNoEqualTo(corporationEntityNo);
		    	}
		    	
		    	for (CoreCustomerPersonInfo coreCustomerPersonInfo : coreCustomerPersonInfoQueryList) {
		    		customerNo = coreCustomerPersonInfo.getCustomerNo();
		    		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		    		coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
		    		if(null != coreCustomer){
		    			throw new BusinessException("CUS-00097");//法人手机号重复，请重新输入
		    		}
				}
		    	
		    } else {
		        //结果为空则继续新增操作
		        Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
		        CoreCustomerPersonInfo coreCustomerPersonInfo = new CoreCustomerPersonInfo();
		        ReflexUtil.setFieldsValues(coreCustomerPersonInfo, map);
		        coreCustomerPersonInfo.setId(RandomUtil.getUUID());
		        coreCustomerPersonInfo.setVersion(1);
		        @SuppressWarnings("unused")
		        int result = coreCustomerPersonInfoDaoImpl.insert(coreCustomerPersonInfo);
		        if (operatorId == null) {
		            operatorId = "system";
		        }
		        
		        /**
		         * 生成非金融日志
		         */
		        nonFinancialLogUtil.createNonFinancialActivityLog(x5025bo.getEventNo(), x5025bo.getActivityNo(),
		                ModificationType.ADD.getValue(), null, null, coreCustomerPersonInfo, coreCustomerPersonInfo.getId(),
		                x5025bo.getCurrLogFlag(), operatorId, coreCustomerPersonInfo.getCustomerNo(),
		                coreCustomerPersonInfo.getCustomerNo(), null,null);
		    }
		    
		} else {
		    throw new BusinessException("CUS-00098");//请输入手机号
		}
		
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		
		/**
		 * 同步授权：
		 * 活动触发事件表-识别维度代码recog_dimen_code字段为null的删掉，触发事件调用的解决方法
		 * add by wangxi 2019/7/22
		 */
		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5025bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		//BSS.AD.01.0001-客户资料建立
		if(BSS_AD_01_0001.equals(eventNo)){//如果调用客户资料建立事件走同步授权操作
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
		}
		
		return eventCommAreaNonFinance;
	}

}
