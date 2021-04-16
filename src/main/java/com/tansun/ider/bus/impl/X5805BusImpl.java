package com.tansun.ider.bus.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5805Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5805BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.OperationModeUtil;

@Service
public class X5805BusImpl implements X5805Bus{

	private static final String system = "system";
	
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Resource
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private OperationModeUtil operationModeUtil;
	@Override
	public Object busExecute(X5805BO x5805bo) throws Exception {
		String eventNo = x5805bo.getEventNo();
		String operatorId = x5805bo.getOperatorId();
		//获取封锁码
		if(StringUtil.isBlank(eventNo)){
			throw new BusinessException("CUS-12061");
		}
		CoreEventActivityRel coreEventActivityRel = x5805bo.getCoreEventActivityRel();
		String triggerNo = coreEventActivityRel.getTriggerNo();
		if (StringUtil.isBlank(triggerNo)) {
			throw new BusinessException("CUS-00140");
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
		EventCommArea eventCommArea = new EventCommArea();
		//长期使用上封锁码保存为对单个媒介单元进行处理
		CoreEvent coreEvent = httpQueryService.queryEvent(triggerNo);//获取长期未使用上封锁码的封锁码类别、封锁码场景序号
		String effectivenessCodeType = coreEvent.getEffectivenessCodeType();
		Integer effectivenessCodeScene = coreEvent.getEffectivenessCodeScene();
		//获取外部识别号
		String externalIdentificationNo = x5805bo.getExternalIdentificationNo();
		if(StringUtil.isBlank(externalIdentificationNo)){
			throw new BusinessException("无法获取外部识别号");
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andInvalidFlagNotEqualTo("N");
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(null == coreMediaBasicInfo){
			throw new BusinessException("CUS-00124");
		}
		String operationMode = "";
		String productObjectCode = "";
		CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(coreMediaBasicInfo.getOperationMode(), effectivenessCodeType, effectivenessCodeScene+"");
		if(coreEffectivenessCode!=null){
			//检查该媒介是否已经上了封锁码
			boolean checkExsitBlockCode = this.checkExsitBlockCode(coreMediaBasicInfo,coreEffectivenessCode,triggerNo);
			//如果没上封锁码
			if(checkExsitBlockCode){
				//查询构件实例化获得pcd值
				operationMode = coreMediaBasicInfo.getOperationMode();
				productObjectCode = coreMediaBasicInfo.getProductObjectCode();
				String customerNo = coreMediaBasicInfo.getMainCustomerNo();
				eventCommArea.setEcommOperMode(operationMode);
				eventCommArea.setEcommProdObjId(productObjectCode);
				Iterator<Map.Entry<String, String>> it = null;
				CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
				Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_421, eventCommArea);
				it = elePcdResultMap.entrySet().iterator();
				while (it!=null&&it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					if (Constant.ADD_BLOCK_CODE.equals(entry.getKey())) { // 421AAA0100
						String prevActivityDate = coreMediaBasicInfo.getPrevActivityDate();
						long dealingDate = this.dealingDate(prevActivityDate,coreMediaBasicInfo);
	    				//如果（当前系统处理日-媒介上一活动日）>421AAA01 PCD设定天数，
		    			if(dealingDate>Long.valueOf(entry.getValue())){
		    				Map<String, Object> triggerEventParamsNew = new HashMap<String, Object>();
		    				EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
		    				//触发封锁码更新事件BSS.OP.01.0007对该媒介上封锁码，
		    				//运营模式
		    				eventCommAreaNonFinanceNew.setOperationMode(operationMode);
		    				//客户号
		    				eventCommAreaNonFinanceNew.setCustomerNo(customerNo);
		    				//事件号
		    				eventCommAreaNonFinanceNew.setSpEventNo(triggerNo);
		    				//管控层级		
		    				eventCommAreaNonFinanceNew.setSceneTriggerObject(coreEvent.getSceneTriggerObject());
		    				//层级代码
		    				eventCommAreaNonFinanceNew.setLevelCode(coreMediaBasicInfo.getMediaUnitCode());
		    				//操作员Id
		    				eventCommAreaNonFinanceNew.setOperatorId(operatorId);
		    				//生效码类别
		    				eventCommAreaNonFinanceNew.setEffectivenessCodeType(effectivenessCodeType);
		    				//生效码序号
		    				eventCommAreaNonFinanceNew.setEffectivenessCodeScene(effectivenessCodeScene);
		    				//操作员标识
		    				if(StringUtil.isBlank(x5805bo.getOperatorId())){
		    					x5805bo.setOperatorId("system");
		    				}
		    				eventCommAreaNonFinanceNew.setOperatorId(x5805bo.getOperatorId());
		    				triggerEventParamsNew.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
		    				eventCommAreaTriggerEventList.add(triggerEventParamsNew);
	    				}
		    			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
					} else if (Constant.No_BLOCK_CODE.equals(entry.getKey())) { // 421AAA0199
						//不做处理
					}
				}
			}
		}else{
			if (!system.equals(operatorId)) {
				throw new BusinessException("CUS-00045");
			}
		}
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
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 处理日期相隔分段
	 * @param dateString
	 * @return
	 */
	private long dealingDate(String prevActivityDate,CoreMediaBasicInfo coreMediaBasicInfo) throws Exception{
		if(StringUtil.isNotBlank(prevActivityDate)){
			CoreSystemUnit systemUnit = operationModeUtil.getcoreOperationMode(coreMediaBasicInfo.getMainCustomerNo());
			if(systemUnit!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String currProcessDate = "";
				if("EOD".equals(systemUnit.getSystemOperateState())){
					currProcessDate = systemUnit.getCurrProcessDate();
				}else if("NOR".equals(systemUnit.getSystemOperateState())){
					currProcessDate = systemUnit.getNextProcessDate();
				}else{
					throw new BusinessException("该系统单元为0");
				}
				long prevDate = sdf.parse(prevActivityDate).getTime();
				long currDate = sdf.parse(currProcessDate).getTime();
				//计算间隔多少天，则除以毫秒到天的转换公式
		        long betweenDate = (currDate - prevDate) / (1000 * 60 * 60 * 24); 
		        return betweenDate;
			}else{
				throw new BusinessException("无法获取系统单元");
			}
		}else{
			return 0;
		}
	}
}
