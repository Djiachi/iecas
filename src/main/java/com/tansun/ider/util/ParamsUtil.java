package com.tansun.ider.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreParameterActivityLog;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.CommLog;
import com.tansun.ider.service.CommonLog;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParamsUtil {
	@Autowired
	private CommonLog commonLog;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;

	@Value("${global.target.service.url.auth}")
	private String authUrl;

	// 新增参数日志
	public void logInsert(List<CoreParameterActivityLog> coreParameterList) throws Exception {
		CommLog commLog = new CommLog();
		if (coreParameterList != null && coreParameterList.size() != 0) {
			commLog.setCoreParameterActivityLogList(coreParameterList);
			commonLog.logInsert(commLog);
		}
	}

	/**
	 * 新增非金融参数日志
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param eventNo
	 * @param activityNo
	 * @param afterObj
	 * @param beforeObj
	 * @param entrys
	 * @param operatorId
	 * @throws Exception
	 */
	public void logNonInsert(String eventNo, String activityNo, Object afterObj, Object beforeObj, String entrys,
			String operatorId) throws Exception {
		if(StringUtil.isBlank(entrys)){
			entrys =Constant.EMPTY_LIST;
		}
//		CoreSystemUnit coreSystemUnit = this.getUserSystemUnit(operatorId);
//		nonFinancialLogUtil.createNonFinancialActivityLog(eventNo, activityNo, this.getModifyType(eventNo), null,
//				beforeObj, afterObj, entrys, coreSystemUnit.getCurrLogFlag(), operatorId, "", "", "",null);
	}

	// 新增参数日志(特殊)
	public void logNonInsertSpecial(String eventNo, String activityNo, String modificationType, String effectFlag,
			Object afterObj, Object beforeObj, String entrys, String operatorId) throws Exception {
		if(StringUtil.isBlank(entrys)){
			entrys =Constant.EMPTY_LIST;
		}
//		CoreSystemUnit coreSystemUnit = this.getUserSystemUnit(operatorId);
//		nonFinancialLogUtil.createNonFinancialActivityLog(eventNo, activityNo, modificationType, effectFlag, beforeObj,
//				afterObj, entrys, coreSystemUnit.getCurrLogFlag(), operatorId, "", "", "",null);
	}

	// 触发事件
	public void trigerEvent(String triggerEventNo, CoreEventActivityRel dto, Map<String, Object> triggerEventParams)
			throws Exception {
		CoreEvent event = httpQueryService.queryEvent(triggerEventNo);
		String eventType = event.getEventType();
		triggerEventParams.put("authDataSynFlag", "1");
		// 事件编号判断触发授权事件
		String params = JSON.toJSONString(triggerEventParams, SerializerFeature.DisableCircularReferenceDetect);
		String interactMode = dto.getTriggerEventInteractMode();
		if ("SYNC".equals(interactMode)) {
			// 同步触发，需等待触发事件处理结束
			RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(type);
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			String response = restTemplate.postForObject(authUrl + triggerEventNo, entity, String.class);
			ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
			if (!"000000".equals(responseVO.getReturnCode())) {
				throw new BusinessException(responseVO.getReturnCode());
			}
		} else if ("ASYNC".equals(interactMode)) {
			// 异步触发，无需等待触发事件处理结束
			dto.setTriggerNo(triggerEventNo);
			ChildTaskStartUtil.getInstance().startChildTask(triggerEventNo, dto, eventType, params);
		}

	}

	// 参数日志修改类型
	public String getModifyType(String eventId) throws Exception {
		String modifyType = "";
		if (StringUtil.isNotBlank(eventId)) {
			String eventSecond = eventId.substring(4, 6);
			if ("AD".equals(eventSecond)) {
				modifyType = ModificationType.ADD.getValue();
			} else if ("UP".equals(eventSecond)) {
				modifyType = ModificationType.UPD.getValue();
			} else if ("IQ".equals(eventSecond)) {
				modifyType = ModificationType.IQQ.getValue();
			}
		}
		return modifyType;
	}

	/**
	 * redis缓存set delete
	 * 
	 * @param <T>
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param eventId
	 * @return
	 * @throws Exception
	 */
	public <T> void redisCommit(Object classObject, String keyTable, Class<T> clazz) throws Exception {
		CacheUtils.getInstance().deleteMap(clazz);
		String key = Constant.PARAMS_FLAG + keyTable;
		CacheUtils.getInstance().addMap(clazz, key, classObject);
	}

	/**
	 * 获取登录用户所属机构所属法人下运营模式列表
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	/*public List<String> getOperationModeList(String loginName) throws Exception {
		List<String> operationModeList = new ArrayList<String>();
		// 查询用户所在机构
		if (StringUtil.isNotBlank(loginName)) {
			CoreUserSqlBuilder coreUserSqlBuilder = new CoreUserSqlBuilder();
			coreUserSqlBuilder.andLoginNameEqualTo(loginName);
			CoreUser coreUser = coreUserDao.selectBySqlBuilder(coreUserSqlBuilder);
			if (null != coreUser) {
				if (StringUtil.isNotBlank(coreUser.getOrganization())) {
					CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreUser.getOrganization());
					if (null != coreOrgan) {// 查询机构法人
						if (StringUtil.isNotBlank(coreOrgan.getCorporationEntityNo())) {
							CoreOrganSqlBuilder coreOrganSqlBuilder = new CoreOrganSqlBuilder();
							coreOrganSqlBuilder.andCorporationEntityNoEqualTo(coreOrgan.getCorporationEntityNo());
							List<CoreOrgan> coreOrganList = coreOrganDao.selectListBySqlBuilder(coreOrganSqlBuilder);
							if (null != coreOrganList && !coreOrganList.isEmpty()) {
								for (CoreOrgan organ : coreOrganList) {
									if (StringUtil.isNotBlank(organ.getOperationMode())) {
										operationModeList.add(organ.getOperationMode());
									}
								}
							}
						}
					}
				}
			}
		}
		// 去重
		HashSet<String> h = new HashSet<String>(operationModeList);
		operationModeList.clear();
		operationModeList.addAll(h);
		return operationModeList;
	}*/

	/**
	 * 获取登录用户系统单元
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	/*public CoreSystemUnit getUserSystemUnit(String loginName) throws Exception {
		CoreSystemUnit coreSystemUnit = null;
		// 查询用户所在机构
		if (StringUtil.isNotBlank(loginName)) {
			CoreUserSqlBuilder coreUserSqlBuilder = new CoreUserSqlBuilder();
			coreUserSqlBuilder.andLoginNameEqualTo(loginName);
			CoreUser coreUser = coreUserDao.selectBySqlBuilder(coreUserSqlBuilder);
			if (null != coreUser) {
				if (StringUtil.isNotBlank(coreUser.getOrganization())) {
					CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreUser.getOrganization());
					if (null != coreOrgan) {// 查询机构法人
						if (StringUtil.isNotBlank(coreOrgan.getCorporationEntityNo())) {
							CoreCorporationEntity coreCorporationEntity = httpQueryService
									.queryCoreCorporationEntity(coreOrgan.getCorporationEntityNo());
							if (null != coreCorporationEntity) {// 查询法人系统单元
								if (StringUtil.isNotBlank(coreCorporationEntity.getSystemUnitNo())) {
									coreSystemUnit = httpQueryService
											.querySystemUnit(coreCorporationEntity.getSystemUnitNo());
								}
							}

						}
					}
				}

			}
		}
		return coreSystemUnit;
	}*/

	/**
	 * 仅允许设置了运营模式的机构下的用户可建立、修改
	 * 修改时，检查如果用户机构的“运营模式”与修改记录的运营模式相同则允许，否则提示“无权限修改该数据”；
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param loginName
	 * @param eventId
	 * @param operationMode
	 *            修改记录的运营模式
	 * @throws Exception
	 */
	/*public void getUserOperationAuth(String loginName, String eventId, String operationMode) throws Exception {
		// 查询用户所在机构
		if (StringUtil.isNotBlank(loginName)) {
			CoreUserSqlBuilder coreUserSqlBuilder = new CoreUserSqlBuilder();
			coreUserSqlBuilder.andLoginNameEqualTo(loginName);
			CoreUser coreUser = coreUserDao.selectBySqlBuilder(coreUserSqlBuilder);
			if (null != coreUser) {
				if ( !"1".equals(coreUser.getAdminFlag()) &&  StringUtil.isNotBlank(coreUser.getOrganization())) {
					CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreUser.getOrganization());
					if (null != coreOrgan) {// 查询机构法人
						if (StringUtil.isBlank(coreOrgan.getOperationMode())) {
							if (ModificationType.ADD.getValue().equals(getModifyType(eventId))) {
								throw new BusinessException("COR-10023");
							} else if (ModificationType.UPD.getValue().equals(getModifyType(eventId))) {
								throw new BusinessException("COR-10022");
							}
						} else if (ModificationType.UPD.getValue().equals(getModifyType(eventId))) {
							if (!coreOrgan.getOperationMode().equals(operationMode)) {
								throw new BusinessException("COR-10022");
							}
						}
					}
				}
			}
		}
	}*/

	/**
	 * 获取利用反射获取类里面的值和名称
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = field.get(obj);
			map.put(fieldName, value);
		}
		return map;
	}
}
