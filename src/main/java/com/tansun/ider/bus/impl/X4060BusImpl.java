package com.tansun.ider.bus.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4060Bus;
import com.tansun.ider.dao.beta.CoreEffectivenessCodeDao;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4060BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
/**
 * 
 * @Description:公务卡停用
 * @author: sunyaoyao
 * @date:   2019年5月22日 下午3:22:58   
 *
 */
@Service
public class X4060BusImpl implements X4060Bus {
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Resource
	private CoreEffectivenessCodeDao coreEffectivenessCodeDao;
	@Resource
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Override
	public Object busExecute(X4060BO x4060bo) throws Exception {
		String eventNo = x4060bo.getEventNo();
		String externalIdentificationNo = x4060bo.getExternalIdentificationNo();
		//获取封锁码
		if(StringUtil.isBlank(eventNo)){
			throw new BusinessException("CUS-12061");
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
		CoreEvent coreEvent = httpQueryService.queryEvent(eventNo);
		if(coreEvent==null){
			throw new BusinessException("CUS-12062");
		}
		String effectivenessCodeType = coreEvent.getEffectivenessCodeType();
		Integer effectivenessCodeScene = coreEvent.getEffectivenessCodeScene();
		if(StringUtil.isBlank(externalIdentificationNo)){
			throw new BusinessException("CUS-12063");
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andInvalidFlagNotEqualTo("N");
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(null == coreMediaBasicInfo){
			throw new BusinessException("CUS-00083");
		}
		String operationMode = coreMediaBasicInfo.getOperationMode();
		String customerNo = coreMediaBasicInfo.getMainCustomerNo();
		CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(coreMediaBasicInfo.getOperationMode(), effectivenessCodeType, effectivenessCodeScene+"");
		if(coreEffectivenessCode==null){
			throw new BusinessException("CUS-12064");
		}
		boolean exsitBlockCode = checkExsitBlockCode(coreMediaBasicInfo, coreEffectivenessCode,eventNo);
		if(exsitBlockCode){
			throw new BusinessException("CUS-12065");
		}
		Map<String, Object> triggerEventParamsNew = new HashMap<String, Object>();
		EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
		//运营模式
		eventCommAreaNonFinanceNew.setOperationMode(operationMode);
		//客户号
		eventCommAreaNonFinanceNew.setCustomerNo(customerNo);
		//事件号
		eventCommAreaNonFinanceNew.setSpEventNo(eventNo);
		//管控层级		
		eventCommAreaNonFinanceNew.setSceneTriggerObject(coreEvent.getSceneTriggerObject());
		//层级代码
		eventCommAreaNonFinanceNew.setLevelCode(coreMediaBasicInfo.getProductObjectCode());
		//操作员标识
		if(StringUtil.isBlank(x4060bo.getOperatorId())){
			x4060bo.setOperatorId("system");
		}
		eventCommAreaNonFinanceNew.setOperatorId(x4060bo.getOperatorId());
//		eventCommAreaNonFinanceNew.setEffectivenessCodeScene(effectivenessCodeScene);
//		eventCommAreaNonFinanceNew.setEffectivenessCodeType(effectivenessCodeType);
		triggerEventParamsNew.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
		eventCommAreaTriggerEventList.add(triggerEventParamsNew);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		return eventCommAreaNonFinance;
	}
	/**
	 * 检查当前媒介是否已经封锁
	 * @param coreMediaBasicInfo
	 * @param coreBlockCode
	 * @return
	 * @throws Exception
	 */
	private boolean checkExsitBlockCode(CoreMediaBasicInfo coreMediaBasicInfo,CoreEffectivenessCode coreEffectivenessCode,String eventNo) throws Exception{
		CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
		coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(eventNo);
		List<CoreCustomerEffectiveCode> list = coreCustomerEffectiveCodeDao.selectListBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
//		coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeTypeEqualTo(coreEffectivenessCode.getEffectivenessCodeType());
//		coreCustomerEffectiveCodeSqlBuilder.andContrlLevelEqualTo(coreEffectivenessCode.getEffectivenessCodeScope());
//		coreCustomerEffectiveCodeSqlBuilder.andLevelCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
//		coreCustomerEffectiveCode = coreCustomerEffectiveCodeDao.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
		if(list.size()!=0){
			return true;
		}else{
			return false;
		}
	}

}
