package com.tansun.ider.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreActivity;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreNmnyLog;
import com.tansun.ider.dao.issue.entity.CoreNmnyLogB;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.service.HttpQueryService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @version:1.0
 * @Description: 非金融日志公共类
 * @author: admin
 */
@Service
public class NonFinancialLogUtil {

	private static Logger logger = LoggerFactory.getLogger(NonFinancialLogUtil.class);

	@Autowired
	public CoreNmnyLogDao coreNmnyLogDao;
	@Autowired
	public CoreNmnyLogBDao coreNmnyLogBDao;
	@Autowired
	private HttpQueryService httpQueryService;
	
	/**
	 * 
	 * @Description: 记录非金融日志以及参数日志
	 * @param eventNo
	 *            事件编号
	 * @param activityNo
	 *            活动编号
	 * @param modificationType
	 *            修改类型
	 * @param effectFlag
	 *            生效标识
	 * @param logLevel
	 *            日志级别
	 * @param beforeObj
	 *            修改前对象
	 * @param afterObj
	 *            修改后对象
	 * @param entrys
	 *            关联表主键ID
	 * @param currLogFlag
	 *            当前日志标志
	 * @param operatorId
	 *            操作员ID
	 * @param customerNo
	 *            客户号
	 * @param entityKey
	 *            账户代码/产品代码/媒介单元代码/业务项目代码/地址类型
	 * @param currencyCode
	 *            货币代码
	 * @throws Exception
	 */
	public void createNonFinancialActivityLog(String eventNo, String activityNo, String modifyType, String effectFlag,
			Object beforeObj, Object afterObj, String entrys, String currLogFlag, String operatorId, String customerNo,
			String entityKey, String currencyCode, String logLevel) throws Exception {
		if (afterObj == null && beforeObj == null) {
			throw new BusinessException("CUS-00077");
		}
		if (eventNo == null || activityNo == null || modifyType == null || currLogFlag == null || operatorId == null) {
			throw new BusinessException("CUS-00077");
		}
		//查询交易不记录日志
		if (ModificationType.IQQ.getValue().equals(modifyType)) {
			return;
		}
		if (logger.isDebugEnabled()) {
			if (beforeObj != null) {
				logger.debug("obj1={}", beforeObj.toString());
			}
			if (afterObj != null) {
				logger.debug("obj2={} ", afterObj.toString());
			}
		}
		CoreActivity coreActivity = httpQueryService.queryCoreActivity(activityNo);
		String beforeJson = null;
		String afterJson = null;
		String relativeTableName = null;
		// String customerNo = null;
		// String entityKey = null;
		// String currencyCode = null;
		if (afterObj == null && beforeObj != null) {
			throw new BusinessException("CUS-00077");
		} else if (afterObj != null && beforeObj == null) {
			// 修改前是null，修改后是对象内容
			// 这种场景的就是新增
			Map<String, Object> mapBeforeObj = MapTransformUtils.objectToMap(beforeObj);
			Map<String, Object> mapAfterObj = MapTransformUtils.objectToMap(afterObj);
			Class clazz = afterObj.getClass();
			relativeTableName = clazz.getName();
			@SuppressWarnings({ "unused", "unchecked" })
			Object object1 = MapTransformUtils.mapToObject(mapBeforeObj, clazz);
			@SuppressWarnings({ "unused", "unchecked" })
			Object object2 = MapTransformUtils.mapToObject(mapAfterObj, clazz);
			if (object1 != null) {
				beforeJson = JSON.toJSONString(object1, SerializerFeature.DisableCircularReferenceDetect);
			} else {
				beforeJson = null;
			}
			if (object2 != null) {
				afterJson = JSON.toJSONString(object2,SerializerFeature.DisableCircularReferenceDetect);
			} else {
				afterJson = null;
			}
		} else if (afterObj != null && beforeObj != null) {
			Map<String, Object> mapBeforeObj = MapTransformUtils.objectToMap(beforeObj);
			Map<String, Object> mapAfterObj = MapTransformUtils.objectToMap(afterObj);
			/*
			 * if
			 * (coreActivity.getLogLevel().equals(LogLevelStatus.C.getValue()))
			 * { customerNo = (String) mapBeforeObj.get("customerNo"); if
			 * (coreActivity.getActivityNo().equals("X5010") ||
			 * coreActivity.getActivityNo().equals("X5090") ||
			 * "X5110".equals(coreActivity.getActivityNo())) { entityKey =
			 * (String) mapBeforeObj.get("type"); } } else if
			 * (coreActivity.getLogLevel().equals(LogLevelStatus.P.getValue()))
			 * { entityKey = (String) mapBeforeObj.get("productObjectCode"); }
			 * else if
			 * (coreActivity.getLogLevel().equals(LogLevelStatus.M.getValue()))
			 * { entityKey = (String) mapBeforeObj.get("mediaUnitCode"); } else
			 * if
			 * (coreActivity.getLogLevel().equals(LogLevelStatus.A.getValue()))
			 * { entityKey = (String) mapBeforeObj.get("accountId") ;
			 * currencyCode = (String) mapBeforeObj.get("currencyCode"); } else
			 * if
			 * (coreActivity.getLogLevel().equals(LogLevelStatus.D.getValue()))
			 * { entityKey = (String) mapBeforeObj.get("businessProgramNo"); }
			 */
			Class clazz = beforeObj.getClass();
			relativeTableName = clazz.getName();
			List<String> delList = new ArrayList<>();
			Iterator<Map.Entry<String, Object>> entries = mapBeforeObj.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Object> entry1 = entries.next();
				String mapBeforeObjKey = entry1.getKey();
				String str1 = (mapBeforeObj.get(mapBeforeObjKey) == null ? "" : mapBeforeObj.get(mapBeforeObjKey))
						.toString();
				String str2 = (mapAfterObj.get(mapBeforeObjKey) == null ? ""
						: mapAfterObj.get(mapBeforeObjKey).toString()).toString();
				if (!str1.equals(str2)) {

				} else {
					delList.add(mapBeforeObjKey);
				}
			}
			for (String attribute : delList) {
				mapBeforeObj.remove(attribute);
				mapAfterObj.remove(attribute);
			}
			Object object1 = MapTransformUtils.mapToObject(mapBeforeObj, clazz);
			Object object2 = MapTransformUtils.mapToObject(mapAfterObj, clazz);

			if (object1 != null) {
				beforeJson = JSON.toJSONString(object1,SerializerFeature.DisableCircularReferenceDetect);
			} else {
				beforeJson = null;
			}
			if (object2 != null) {
				afterJson = JSON.toJSONString(object2,SerializerFeature.DisableCircularReferenceDetect);
			} else {
				afterJson = null;
			}
		}

		if (StringUtil.isBlank(logLevel)) {
			logLevel = coreActivity.getLogLevel();
		}

		// logLevel R-参数级
		if (logLevel.equals("R")) {
//			CoreParmLog coreParmLog = new CoreParmLog();
//			coreParmLog.setEventNo(eventNo);
//			coreParmLog.setActivityNo(activityNo);
//			coreParmLog.setId(RandomUtil.getUUID());
//			coreParmLog.setEffectFlag(effectFlag); // 生效标识
//			coreParmLog.setLogLevel(logLevel);
//			coreParmLog.setModifyBefore(beforeJson);
//			coreParmLog.setModifyAfter(afterJson);
//			coreParmLog.setOccurrDate(DateUtil.getDate());
//			coreParmLog.setOccurrTime(DateUtil.format(new Date(), "HH:mm:ss.SSS"));
//			coreParmLog.setOperatorId(operatorId); // 操作员
//			coreParmLog.setRelativeTableName(relativeTableName); // 表名
//			coreParmLog.setRelativeTableId(entrys);
//			coreParmLog.setModifyType(modifyType);
//			coreParmLog.setVersion(1);
			if (currLogFlag.equals("A")) {
//				coreParmLogDao.insert(coreParmLog);
//				betaCommonParamServiceImpl.queryCore
				
			} else {
//				CoreParmLogB coreParmLogB = new CoreParmLogB();
//				CachedBeanCopy.copyProperties(coreParmLogB, coreParmLog);
//				coreParmLogBDao.insert(coreParmLogB);
			}

		} else {
			CoreNmnyLog coreNmnyLog = new CoreNmnyLog();
			coreNmnyLog.setEventNo(eventNo);
			coreNmnyLog.setActivityNo(activityNo);
			coreNmnyLog.setId(RandomUtil.getUUID());
			coreNmnyLog.setEffectFlag(effectFlag); // 生效标识
			coreNmnyLog.setLogLevel(logLevel);
			coreNmnyLog.setModifyBefore(beforeJson);
			coreNmnyLog.setModifyAfter(afterJson);
			coreNmnyLog.setOccurrDate(DateUtil.getDate());
			coreNmnyLog.setOccurrTime(DateUtil.format(new Date(), "HH:mm:ss.SSS"));
			coreNmnyLog.setOperatorId(operatorId); // 操作员
			coreNmnyLog.setRelativeTableName(relativeTableName); // 表名
			//当表为CoreCustomerAddr时,entityKey赋值，否则为空
			if(StringUtil.isNotBlank(relativeTableName) && StringUtil.isNotBlank(logLevel)){
				if("C".equals(logLevel) && relativeTableName.indexOf("CoreCustomerAddr") != -1){
					coreNmnyLog.setEntityKey(entityKey);
				}
			}
			coreNmnyLog.setRelativeTableId(entrys);
			coreNmnyLog.setModifyType(modifyType);
			coreNmnyLog.setCustomerNo(customerNo);
			coreNmnyLog.setCurrencyCode(currencyCode);
			coreNmnyLog.setVersion(1);
			coreNmnyLog.setTransHisFlag("N");
			if (currLogFlag.equals("A")) {
				coreNmnyLogDao.insert(coreNmnyLog);
			} else {
				CoreNmnyLogB coreNmnyLogB = new CoreNmnyLogB();
				CachedBeanCopy.copyProperties(coreNmnyLog,coreNmnyLogB);
				coreNmnyLogBDao.insert(coreNmnyLogB);
			}
		}
	}

	/**
	 * 
	 * @Description:
	 * @param object
	 * @return
	 */
	public static Field[] getAllFields(Object object) {
		Class clazz = object.getClass();
		List<Field> fieldList = new ArrayList<>();
		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		Field[] fields = new Field[fieldList.size()];
		fieldList.toArray(fields);
		return fields;
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("1", "1");
		String str = (String) map.get("2");
	}

}
