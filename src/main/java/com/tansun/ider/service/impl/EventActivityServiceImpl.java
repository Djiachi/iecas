package com.tansun.ider.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.loginterceptor.LogInterceptor;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.util.ThreadLocalUtil;
import com.tansun.ider.dao.beta.CoreProductObjectDao;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreActivityEventTrigger;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.sqlbuilder.CoreProductObjectSqlBuilder;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerContrlEventDao;
import com.tansun.ider.dao.issue.CoreCustomerDelinquencyDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlEvent;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelinquency;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlEventSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelinquencySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.EncryptionStatus;
import com.tansun.ider.enums.TriggerEventType;
import com.tansun.ider.enums.TriggerType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.framwork.exception.ExternalSystemException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.service.EventActivityService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ChildTaskStartUtil;
import com.tansun.ider.util.FeeTriggerUtil;
import com.tansun.ider.util.PciUtil;
import com.tansun.ider.web.WSC;

@Service
public class EventActivityServiceImpl implements EventActivityService {
	private static Logger logger = LoggerFactory.getLogger(EventActivityServiceImpl.class);
	@Autowired
	private PciUtil pciUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Value("${global.target.service.url.auth}")
	private String authUrl;
	@Value("${global.target.service.url.card}")
	private String cardUrl;
	@Value("${global.target.service.url.nofn}")
	private String nonFinUrl;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreProductObjectDao coreProductObjectDao;
	@Autowired
	private CoreCustomerContrlEventDao coreCustomerContrlEventDao;
	@Resource
	private CoreCustomerDelinquencyDao coreCustomerDelinquencyDao;
	@Resource
	private CoreAccountDao coreAccountDao;
	@Resource
	private CoreProductDao coreProductDao;
	@Resource
	private QueryCustomerService queryCustomerService;
	@Autowired
	private FeeTriggerUtil feeTriggerUtil;
	@Autowired
	private EventActivityService eventActivityServiceImpl;

	final String productObjectNo = "productObjectNo";
	final String transPostingCurr = "transPostingCurr";
	final String externalIdentificationNo = "externalIdentificationNo";
	final String delinquencyLevel = "delinquencyLevel";
	final String levelCode = "levelCode";
	final String eventId = "eventId";
	final String customerNo = "customerNo";
	final String mediaUnitCode = "mediaUnitCode";

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object businessHandler(String eventId, String globalEventNo, List<CoreEventActivityRel> actList)
			throws Exception {
		Object result = null;
		// Object resultBo = null;
		Object returnResult = null;
		Object returnResult1 = null;
		long invokeStart1 = System.currentTimeMillis();
		for (CoreEventActivityRel dto : actList) {
			long invokeStart = System.currentTimeMillis();
			// 6.1 通过活动号获取构件
			List<CoreActivityArtifactRel> activityArtifactList = httpQueryService
					.queryActivityArtifactRel(dto.getActivityNo());
			// 6.2 封装公共参数，获取最新的事件公共区内容
			String eventJsonStr = (String) ThreadLocalUtil.get(globalEventNo); // 获取最新的线程本地变量信息
			Map<String, Object> paramsMap = this.getCommonParams(activityArtifactList, globalEventNo, dto,
					eventJsonStr);
			// 6.3 调用活动
			ActionService coreService = (ActionService) SpringUtil.getBean(dto.getActivityNo());
			result = coreService.execute(paramsMap); // 内部方法转换为JSON后进行传递
			if ("X5065".equals(dto.getActivityNo()) && EVENT.BSS_AD_01_9001.equals(eventId) || EVENT.BSS_AD_01_0001.equals(eventId)
					|| EVENT.BSS_AD_01_0003.equals(eventId) || EVENT.ISS_OP_01_0003.equals(eventId)) {
				returnResult = result;
			}
			// 6.4 将 result 放到事件公共区
			ThreadLocalUtil.set(globalEventNo,
					JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
			// 6.5 异步调起事件
			if (StringUtil.isNotBlank(dto.getTriggerTyp())) {
				EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
				CachedBeanCopy.copyProperties(result ,eventCommAreaNonFinance);
				if (null == eventCommAreaNonFinance.getWhetherProcess() || StringUtil.isBlank(eventCommAreaNonFinance.getWhetherProcess())) {
					returnResult1 = this.triggerNonFinanceEvent(eventId, dto, result);
				}
			}
			long invokeEnd = System.currentTimeMillis();
			if (logger.isDebugEnabled()) {
				logger.debug("================= >事件={},调用该活动={}, 所耗费时间={}毫秒", eventId, dto.getActivityNo(),
						(invokeEnd - invokeStart));
			}
		}
		// 信审-gns 返回结果处理
		if (EVENT.BSS_AD_01_9001.equals(eventId) || EVENT.BSS_AD_01_0001.equals(eventId)
				|| EVENT.BSS_AD_01_0003.equals(eventId) || EVENT.ISS_OP_01_0003.equals(eventId)) {
			if (null != returnResult) {
				result = returnResult;
			}
			if (!EVENT.BSS_AD_01_9001.equals(eventId)) {
				if (null != returnResult1) {
					result = returnResult1;
				}
			}
			EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
			CachedBeanCopy.copyProperties(result ,eventCommAreaNonFinance);
			String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
			Map<String, Object> map = new HashMap<String, Object>();
			// 客户号
			map.put("customerNo", eventCommAreaNonFinance.getCustomerNo());
			// 外部识别号
			map.put("externalIdentificationNo", externalIdentificationNo);
			// 外部识别号list
			map.put("externalIdentificationNoList", eventCommAreaNonFinance.getCoreMediaBasicInfoList());
			// 机构号
			map.put("institutionId", eventCommAreaNonFinance.getInstitutionId());
			// 证件类型
			map.put("idType", eventCommAreaNonFinance.getIdType());
			// 身份证号
			map.put("idNumber", eventCommAreaNonFinance.getIdNumber());
			// 法人
			map.put("corporation", eventCommAreaNonFinance.getCorporationEntityNo());
			map.put("isNew", eventCommAreaNonFinance.getIsNew());
			map.put("eventNo", eventCommAreaNonFinance.getEventNo());
			map.put("businessProgramNo", eventCommAreaNonFinance.getBusinessProgramNo());
			map.put("productObjectCode", eventCommAreaNonFinance.getProductObjectCode());
			map.put("systemUnitNo", eventCommAreaNonFinance.getSystemUnitNo());
			map.put("mediaUnitCode", eventCommAreaNonFinance.getMediaUnitCode());
			map.put("operationMode", eventCommAreaNonFinance.getOperationMode());
			map.put("mainCustomerNo", eventCommAreaNonFinance.getMainCustomerNo());
			result = map;
		}
		long invokeEnd1 = System.currentTimeMillis();
		logger.debug("================= >事件={}, 所耗费时间={}毫秒", eventId, (invokeEnd1 - invokeStart1));
		return result;
	}

	/**
	 * 封装公共参数
	 * 
	 * @param eventJsonStr
	 * @param activityArtifactList
	 * @param globalEventNo
	 * @param dto
	 * @return
	 */
	private Map<String, Object> getCommonParams(List<CoreActivityArtifactRel> activityArtifactList,
			String globalEventNo, CoreEventActivityRel dto, String eventCommAreaNonFinance) {
		Map<String, Object> paramsMap = new HashMap<String, Object>(16);
		// 活动构件清单
		paramsMap.put(WSC.ACTION_ARTIFACT_KEY, activityArtifactList);
		// 全局事件流水号
		paramsMap.put(WSC.EVENT_ID, globalEventNo);
		// 事件公共存储区
		paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, eventCommAreaNonFinance);
		// 异步Trigger参数使用
		paramsMap.put(WSC.ACTIVITY_INFO, dto);
		return paramsMap;
	}

	/**
	 * 
	 * @Description: 总控触发事件
	 * @param dto
	 * @param eventCommArea
	 * @return
	 * @throws Exception
	 */
	private Object triggerNonFinanceEvent(String eventId, CoreEventActivityRel dto, Object result) throws Exception {
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		// 触发类型
		String triggerTyp = dto.getTriggerTyp();
		// 将result 转换成 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(result , eventCommAreaNonFinance );
		List<Map<String, Object>> triggerEventList = eventCommAreaNonFinance.getEventCommAreaTriggerEventList();
		// 查询活动触发事件表
		if (triggerEventList != null && triggerEventList.size() > 0) {
			for (Map<String, Object> triggerEventParams : triggerEventList) {
				// 反射传递参数
				Object obj = triggerEventParams.get(Constant.KEY_TRIGGER_PARAMS);
				if (obj == null) {
					return null;
				}
				// 触发事件编号
				String triggerEventNo = dto.getTriggerNo();
				if (TriggerType.TRIGGER_TYPE_GENERAL.getValue().equals(triggerTyp)) {
					if (StringUtil.isNotEmpty(triggerEventNo)) {
						// 若list中触发事件编号不为空，则直接触发该事件
						responseVO = triggerEventMode(triggerEventNo, obj, dto);
					}
				}
			}
		} else {
			// 查询活动触发事件表
			List<CoreActivityEventTrigger> listCoreActivityEventTriggers = httpQueryService
					.queryListCoreActivityEventTrigger(dto.getEventNo(), dto.getActivityNo());
			if (listCoreActivityEventTriggers != null && listCoreActivityEventTriggers.size() > 0) {
				for (CoreActivityEventTrigger coreActivityEventTrigger : listCoreActivityEventTriggers) {
					// 触发事件编号
					String triggerEventNo = coreActivityEventTrigger.getTriggerNo();
					if (TriggerType.TRIGGER_TYPE_GENERAL.getValue().equals(triggerTyp)) {
						if (StringUtil.isNotEmpty(triggerEventNo)) {
							// 若list中触发事件编号不为空，则直接触发该事件
							responseVO = triggerEventMode(triggerEventNo, result, dto);
						}
					}
				}
			}
		}
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
		return eventCommAreaNonFinance;
	}

	/**
	 * 
	 * @Description:
	 * @param triggerEventNo
	 * @param eventCommArea
	 * @param dto
	 * @param triggerEventParams
	 * @return
	 * @throws Exception
	 */
	private ResponseVO triggerEventMode(String triggerEventNo, Object eventCommArea, CoreEventActivityRel dto)
			throws Exception {
		String hand01 = "customerNo";
		String hand02 = "externalIdentificationNo";
		String hand03 = "mainCustomerNo";
		String hand04 = "externalIdentificationNo_cip";
		// 根据事件编号判断触发发卡事件还是授权事件
		String url = "";
		String params = "";
		// 查询触发事件的事件类别
		CoreEvent coreEvent = httpQueryService.queryEvent(triggerEventNo);
		String eventType = coreEvent.getEventType();
		// 如果同步的参数有 外部识别号需要将其解密操作
		Map<String, Object> mapFeilds = ReflexUtil.getFieldsValues(eventCommArea);
		String externalIdentificationNo_ = (String) mapFeilds.get(hand02);
		if (null != externalIdentificationNo_) {
			String externalIdentificationNo = (String) mapFeilds.get(hand02);
			if (externalIdentificationNo.length() > 19) {

				/*
				 * if (mapFeilds.containsKey(hand01)) { customerNo = (String)
				 * mapFeilds.get(hand01); } else { customerNo = (String)
				 * mapFeilds.get(hand03); }
				 */
				CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
				coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
				CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
						.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
				String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
				CoreProductObjectSqlBuilder coreProductObjectSqlBuilder = new CoreProductObjectSqlBuilder();
				coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
				CoreProductObject coreProductObject = coreProductObjectDao
						.selectBySqlBuilder(coreProductObjectSqlBuilder);
				String binNo = coreProductObject.getBinNo().toString();
				String externalIdentificationNoNew = pciUtil.getUserData3(externalIdentificationNo, binNo,
						EncryptionStatus.D.getValue(), YesOrNo.NO.getValue());
				ReflexUtil.setFieldValue(eventCommArea, hand02, externalIdentificationNoNew);
			}
		}
		if (TriggerEventType.MONY.getValue().equals(eventType)) {
			url = cardUrl;
			params = JSON.toJSONString(eventCommArea, SerializerFeature.DisableCircularReferenceDetect);
		} else if (TriggerEventType.AUS.getValue().equals(coreEvent.getEventNo().substring(0, 3)) || 
				TriggerEventType.LMS.getValue().equals(coreEvent.getEventNo().substring(0, 3))) {
			url = authUrl;
			params = JSON.toJSONString(eventCommArea, SerializerFeature.DisableCircularReferenceDetect);
		} else if (TriggerEventType.NMNY.getValue().equals(eventType)) {
			url = nonFinUrl;
			params = JSON.toJSONString(eventCommArea, SerializerFeature.DisableCircularReferenceDetect);
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		String triggerEventInteractMode = dto.getTriggerEventInteractMode();

		if (StringUtil.isNotBlank(triggerEventInteractMode)) {
			if ("SYNC".equals(triggerEventInteractMode)) {
				// 同步触发，需等待触发事件处理结束
				RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
				HttpHeaders headers = new HttpHeaders();
				MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
				headers.setContentType(type);
				HttpEntity<String> entity = new HttpEntity<String>(params, headers);
				String response = restTemplate.postForObject(url + triggerEventNo, entity, String.class);
				ResponseVO returnVO = JSON.parseObject(response, ResponseVO.class,
						Feature.DisableCircularReferenceDetect);
				if (!Constant.SUCCESS_CODE.equals(returnVO.getReturnCode())) {
					throw new BusinessException(returnVO.getReturnMsg());
				}
			} else if ("ASYNC".equals(triggerEventInteractMode)) {
				// 异步触发，无需等待触发事件处理结束
				dto.setTriggerNo(triggerEventNo);
				ChildTaskStartUtil.getInstance().startChildTask(dto.getEventNo(), dto, params, url);
			}
		}
		return responseVO;
	}

	/**
	 * 
	 * @Description:批量事件
	 * @param triggerEventNo
	 * @param eventCommArea
	 * @param dto
	 * @param triggerEventParams
	 * @return
	 * @throws Exception
	 */
	public ResponseVO customerCtrlHandle(CoreEvent coreEvent, Map<String, Object> bodyMap) {
		if (logger.isDebugEnabled()) {
			logger.debug("执行客户号为：" + bodyMap.get(customerNo));
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		try {
			Object obj = bodyMap.get(customerNo);
			if (obj == null) {
				throw new BusinessException("COR-10001");
			}
			String customerNo = obj.toString();
			String eventId = coreEvent.getEventNo();
			// 事件表中的循环类型
			String cycleType = coreEvent.getCycleType();
			// 根据循环类型，使用不同的条件匹配客户管控事件表
			if (Constant.CONTROL_C.equals(cycleType)) {
				// 客户级，根据客户号+管控层级（C）+层级代码（客户号）+事件号匹配
				CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
				coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
				coreCustomerContrlEventSqlBuilder.andContrlLevelEqualTo(Constant.CONTROL_C);
				coreCustomerContrlEventSqlBuilder.andLevelCodeEqualTo(customerNo);
				coreCustomerContrlEventSqlBuilder.andEventNoEqualTo(eventId);
				List<CoreCustomerContrlEvent> list = coreCustomerContrlEventDao
						.selectListBySqlBuilder(coreCustomerContrlEventSqlBuilder);
				if (list == null) {
					return webService(eventId, bodyMap, true);
				}
			} else if (Constant.CONTROL_M.equals(cycleType)) {
				// 媒介级，查询该客户下所有媒介单元，根据客户号+管控层级（C）+层级代码（客户号）+事件编号
				List<CoreMediaBasicInfo> mediaList = queryCustomerMediaList(customerNo, coreEvent.getEventNo());
				if (mediaList != null && !mediaList.isEmpty()) {
					for (CoreMediaBasicInfo coreMediaBasicInfo : mediaList) {
						String mediaUnitCode = coreMediaBasicInfo.getMediaUnitCode();
						CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
						coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
						coreCustomerContrlEventSqlBuilder.andEventNoEqualTo(eventId);
						coreCustomerContrlEventSqlBuilder.andContrlLevelEqualTo(Constant.CONTROL_M)
								.andLevelCodeEqualTo(mediaUnitCode);
						List<CoreCustomerContrlEvent> list = coreCustomerContrlEventDao
								.selectListBySqlBuilder(coreCustomerContrlEventSqlBuilder);
						if (null != list && !list.isEmpty()) {

						} else {
							bodyMap.put(externalIdentificationNo, coreMediaBasicInfo.getExternalIdentificationNo());
							bodyMap.put("mediaUnitCode", coreMediaBasicInfo.getMediaUnitCode());
							webService(eventId, bodyMap, true);
						}
					}
				} else {
					return responseVO;
				}
			} else if (Constant.CONTROL_D.equals(cycleType)) {
				List<CoreCustomerDelinquency> delinquencyList = queryCustomerDelinquencyList(customerNo);
				for (CoreCustomerDelinquency coreCustomerDelinquency : delinquencyList) {
					String delinquencyLevel = coreCustomerDelinquency.getDelinquencyLevel();
					String levelCode = coreCustomerDelinquency.getLevelCode();
					String currencyCode = coreCustomerDelinquency.getCurrencyCode();// 币种
					CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
					coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreCustomerContrlEventSqlBuilder.andEventNoEqualTo(eventId);
					CoreAccount coreAccount = queryBusinessProgramCode(coreCustomerDelinquency);
					// 获取业务项目
					String businessProgramNo = coreAccount.getBusinessProgramNo();
					// 获取业务类型
					String businessTypeCode = coreAccount.getBusinessTypeCode();

					if (Constant.CONTROL_G.equals(delinquencyLevel)) {
						coreCustomerContrlEventSqlBuilder.or(new CoreCustomerContrlEventSqlBuilder()
								.andContrlLevelEqualTo(Constant.CONTROL_G).andLevelCodeEqualTo(businessProgramNo));
						coreCustomerContrlEventSqlBuilder.or(new CoreCustomerContrlEventSqlBuilder()
								.andContrlLevelEqualTo(Constant.CONTROL_C).andLevelCodeEqualTo(customerNo));
					} else if (Constant.CONTROL_A.equals(delinquencyLevel)) {
						coreCustomerContrlEventSqlBuilder.or(new CoreCustomerContrlEventSqlBuilder()
								.andContrlLevelEqualTo(Constant.CONTROL_C).andLevelCodeEqualTo(customerNo));
						coreCustomerContrlEventSqlBuilder.or(new CoreCustomerContrlEventSqlBuilder()
								.andContrlLevelEqualTo(Constant.CONTROL_A).andLevelCodeEqualTo(businessTypeCode));
						// 如果延滞层级是A，则根据层级代码和币种查询账户表中的业务项目
						coreCustomerContrlEventSqlBuilder.or(new CoreCustomerContrlEventSqlBuilder()
								.andContrlLevelEqualTo(Constant.CONTROL_G).andLevelCodeEqualTo(businessProgramNo));
					}
					List<CoreCustomerContrlEvent> list = coreCustomerContrlEventDao
							.selectListBySqlBuilder(coreCustomerContrlEventSqlBuilder);
					if (list == null) {
						bodyMap.put(delinquencyLevel, delinquencyLevel);
						bodyMap.put(levelCode, levelCode);
						bodyMap.put(productObjectNo, coreCustomerDelinquency.getProductObjectNo());
						bodyMap.put(transPostingCurr, coreCustomerDelinquency.getCurrencyCode());
						webService(eventId, bodyMap, true);
					}
				}
			} else if (Constant.CONTROL_P.equals(cycleType)) {
				// 产品级，查询该客户下所有产品单元，根据客户号+管控层级（C）+层级代码（客户号）+事件编号
				List<CoreProduct> productList = queryCustomerProductList(customerNo);
				if (productList != null && !productList.isEmpty()) {
					for (CoreProduct coreProduct : productList) {
						bodyMap.put("customerNo", coreProduct.getCustomerNo());
						bodyMap.put("productObjectCode", coreProduct.getProductObjectCode());
						ResponseVO responseVO1 = webService(eventId, bodyMap, true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseVO;
	}

	public ResponseVO nonBatchCtrlHandle(String eventId, CoreEvent coreEvent, Map<String, Object> bodyMap) {
		if (logger.isDebugEnabled()) {
			logger.debug("Request [{}] Message =>{}", eventId, bodyMap);
		}
		long start = System.currentTimeMillis();
		// 对分页处理
		Integer pageNum = (Integer) bodyMap.get("pageNum");
		Integer pageSize = (Integer) bodyMap.get("pageSize");
		if (pageNum != null && pageSize != null && pageNum > 0) {
			bodyMap.put("indexNo", ((pageNum - 1) * pageSize));
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		// 1. 获取全局不重复流水号
		String globalEventNo = (String) ThreadLocalUtil.get(LogInterceptor.HTTP_REQUEST_MESSAGE_NUM);
		try {
			// 获取事件活动对应关系表
			List<CoreEventActivityRel> list = httpQueryService.queryEventActivityRel(eventId);
			// 1. 初始化并赋值事件公共区
			EventActivityService eventActivityService = SpringUtil.getBean(EventActivityService.class);
			EventCommAreaNonFinance eventCommAreaNonFinance = (EventCommAreaNonFinance) eventActivityService
					.getEventCommAreaNonFinance(eventId, bodyMap);

			// 媒介单元
			String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
			// 外部识别号
			String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
			// 客户号
			String customerNo = eventCommAreaNonFinance.getCustomerNo();
			// 产品对象
			String productObjectCode = eventCommAreaNonFinance.getProductObjectCode();
			// 业务项目
			String businessProgramNo = eventCommAreaNonFinance.getBusinessProgramNo();
			// 业务类型
			String businessType = eventCommAreaNonFinance.getBusinessType();
			if (StringUtil.isNotBlank(mediaUnitCode)) {
				if (StringUtil.isBlank(customerNo)) {
					CoreMediaBasicInfo coreMediaBasicInfo = queryMediaUnitCodeMediaBasicInfo(mediaUnitCode);
					customerNo = coreMediaBasicInfo.getMainCustomerNo();
				}
				if (StringUtil.isBlank(productObjectCode)) {
					CoreMediaBasicInfo coreMediaBasicInfo = queryMediaUnitCodeMediaBasicInfo(mediaUnitCode);
					productObjectCode = coreMediaBasicInfo.getProductObjectCode();
				}
			} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
				if (StringUtil.isBlank(customerNo)) {
					CoreMediaBasicInfo coreMediaBasicInfo = queryExternalIdentificationNoMediaBasicInfo(
							externalIdentificationNo);
					customerNo = coreMediaBasicInfo.getMainCustomerNo();
				}
				if (StringUtil.isBlank(productObjectCode)) {
					CoreMediaBasicInfo coreMediaBasicInfo = queryExternalIdentificationNoMediaBasicInfo(
							externalIdentificationNo);
					productObjectCode = coreMediaBasicInfo.getProductObjectCode();
				}
			}
			if (StringUtil.isBlank(customerNo)) {
				return webService(eventId, bodyMap, true);
			} else {
				String eventNo = "";
				// 客户号+管控层级(c)+层级代码（客户号）
				List<CoreCustomerContrlEvent> listC = queryCustomerContrlEvent(customerNo, Constant.CONTROL_C,
						customerNo);
				if (!queryExist(listC, eventId)) {
					if (StringUtil.isNotBlank(productObjectCode)) {
						// 客户号+管控层级(P)+层级代码（产品对象）
						List<CoreCustomerContrlEvent> listP = queryCustomerContrlEvent(customerNo, Constant.CONTROL_P,
								productObjectCode);
						if (!queryExist(listP, eventId)) {
							if (StringUtil.isNotBlank(businessProgramNo)) {
								// 客户号+管控层级(G)+层级代码（业务项目）
								List<CoreCustomerContrlEvent> listG = queryCustomerContrlEvent(customerNo,
										Constant.CONTROL_G, businessProgramNo);
								if (!queryExist(listG, eventId)) {
									if (StringUtil.isNotBlank(mediaUnitCode)) {
										// 客户号+管控层级(M)+层级代码（媒介单元）
										List<CoreCustomerContrlEvent> listM = queryCustomerContrlEvent(customerNo,
												Constant.CONTROL_M, mediaUnitCode);
										if (!queryExist(listM, eventId)) {
											if (StringUtil.isNotBlank(businessType)) {
												// 客户号+管控层级(A)+层级代码（业务类型）
												List<CoreCustomerContrlEvent> listA = queryCustomerContrlEvent(
														customerNo, Constant.CONTROL_A, businessType);
												if (!queryExist(listA, eventId)) {
													return webService(eventId, bodyMap, true);
												} else {
													throw new BusinessException("CUS-00118");
												}
											} else {
												return webService(eventId, bodyMap, true);
											}
										} else {
											throw new BusinessException("CUS-00116");
										}
									}
								} else {
									throw new BusinessException("CUS-00117");
								}
							} else {
								if (StringUtil.isNotBlank(mediaUnitCode)) {
									// 客户号+管控层级(M)+层级代码（媒介单元）
									List<CoreCustomerContrlEvent> listM = queryCustomerContrlEvent(customerNo,
											Constant.CONTROL_M, mediaUnitCode);
									if (!queryExist(listM, eventId)) {
										if (StringUtil.isNotBlank(businessType)) {
											// 客户号+管控层级(A)+层级代码（业务类型）
											List<CoreCustomerContrlEvent> listA = queryCustomerContrlEvent(customerNo,
													Constant.CONTROL_A, businessType);
											if (!queryExist(listA, eventId)) {
												return webService(eventId, bodyMap, true);
											} else {
												throw new BusinessException("CUS-00118");
											}
										} else {
											return webService(eventId, bodyMap, true);
										}
									} else {
										throw new BusinessException("CUS-00116");
									}
								}
							}
						} else {
							throw new BusinessException("CUS-00115");
						}
					}
				} else {
					throw new BusinessException("CUS-00114");
				}
			}

		} catch (Exception e) {
			if (e instanceof ExternalSystemException) {
				responseVO.setReturnCode(((ExternalSystemException) e).getErrCode());
				responseVO.setReturnMsg(((ExternalSystemException) e).toString());
			} else if (e instanceof BusinessException) {
				responseVO.setReturnCode(((BusinessException) e).getErrCode());
				responseVO.setReturnMsg(((BusinessException) e).toString());
			} else if (e instanceof TooManyResultsException) {
				responseVO.setReturnCode(WSC.TOOMANY_EXCEPTION_CODE);
				responseVO.setReturnMsg(WSC.TOOMANY_EXCEPTION_MSG);
			} else {
				e.printStackTrace();
				logger.error(WSC.DEFAULT_ERROR_RETURN_CODE + "==>" + e.getMessage(), e);
				responseVO.setReturnCode(WSC.DEFAULT_ERROR_RETURN_CODE);
				responseVO.setReturnMsg(e.getMessage());
			}
		} finally {
			long end = System.currentTimeMillis();
			responseVO.setExpendMillisecond(String.valueOf(end - start));
			// 清理ThreadLocalUtil
			ThreadLocalUtil.destroy();
			if (logger.isDebugEnabled()) {
				logger.debug("Response [{}] Message =>{}", eventId, responseVO);
			}
		}
		return responseVO;
	}

	public ResponseVO webService(String eventId, Map<String, Object> bodyMap, boolean someBatchFlag) {
		if (logger.isDebugEnabled()) {
			logger.debug("Request [{}] Message =>{}", eventId, bodyMap);
		}
		long start = System.currentTimeMillis();
		// 对分页处理
		Integer pageNum = (Integer) bodyMap.get("pageNum");
		Integer pageSize = (Integer) bodyMap.get("pageSize");
		if (pageNum != null && pageSize != null && pageNum > 0) {
			bodyMap.put("indexNo", ((pageNum - 1) * pageSize));
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		// 1. 获取全局不重复流水号
		String globalEventNo = (String) ThreadLocalUtil.get(LogInterceptor.HTTP_REQUEST_MESSAGE_NUM);
		try {
			CoreEvent coreEvent = httpQueryService.queryEvent(eventId);
			if (null == coreEvent) {
				throw new BusinessException("CUS-00014", "事件编号表");// 机构表
			}
			boolean flag = queryCustomerService.checkCorporationEntity(bodyMap, eventId);
			if (!flag) {
				responseVO.setReturnCode(WSC.DEFAULT_ERROR_RETURN_CODE);
				responseVO.setReturnMsg("客户与登录用户的法人不一致！");
			}
			String threadLocalParams = JSON.toJSONString(bodyMap, SerializerFeature.DisableCircularReferenceDetect);
			// 4. 创建事件公共区的线程本地变量副本
			ThreadLocalUtil.set(globalEventNo, threadLocalParams); // 序列化
			// 5. 获取事件活动配置
			List<CoreEventActivityRel> actList = httpQueryService.queryEventActivityRel(eventId);
			// 6. 调起活动，顺序执行
			Object result = null;
			if (someBatchFlag) {
				result = eventActivityServiceImpl.businessHandler(eventId, globalEventNo, actList);
			}
			// 调起手续费交易
			if (StringUtil.isNotBlank(coreEvent.getFeeItemNo())) {
				if(bodyMap.containsKey("isGetLossFee")){
					String lossFeeFlag = (String)bodyMap.get("isGetLossFee");
				    if (lossFeeFlag.equals(YesOrNo.YES.getValue())) {
				    	responseVO =  this.requestFeeHandler(result);
				 	} 
				}else{
					responseVO =  this.requestFeeHandler(result); 
				}
			}
			responseVO.setReturnData(result);
		} catch (Exception e) {
			if (e instanceof ExternalSystemException) {
				responseVO.setReturnCode(((ExternalSystemException) e).getErrCode());
				responseVO.setReturnMsg(((ExternalSystemException) e).toString());
			} else if (e instanceof BusinessException) {
				responseVO.setReturnCode(((BusinessException) e).getErrCode());
				responseVO.setReturnMsg(((BusinessException) e).toString());
			} else if (e instanceof TooManyResultsException) {
				responseVO.setReturnCode(WSC.TOOMANY_EXCEPTION_CODE);
				responseVO.setReturnMsg(WSC.TOOMANY_EXCEPTION_MSG);
			} else {
				e.printStackTrace();
				logger.error(WSC.DEFAULT_ERROR_RETURN_CODE + "==>" + e.getMessage(), e);
				responseVO.setReturnCode(WSC.DEFAULT_ERROR_RETURN_CODE);
				responseVO.setReturnMsg(e.getMessage());
			}
		} finally {
			long end = System.currentTimeMillis();
			responseVO.setExpendMillisecond(String.valueOf(end - start));
			// 清理ThreadLocalUtil
			ThreadLocalUtil.destroy();
			if (logger.isDebugEnabled()) {
				logger.debug("Response [{}] Message =>{}", eventId, responseVO);
			}
		}
		return responseVO;
	}

	/**
	 * 公共service-事件公共区定义，所有参数全部从缓存中取值
	 * 
	 * @param bodyMap
	 * @return EventCommArea
	 * @throws Exception
	 */
	public EventCommAreaNonFinance getEventCommAreaNonFinance(String eventId, Map<String, Object> map)
			throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		/**
		 * 将参数部分内容通过反射将信息setter到事件公共区里面 包括异步Trigger事件中的参数，比如账号或币种信息赋值到事件公共区里面
		 */
		ReflexUtil.setFieldsValues(eventCommAreaNonFinance, map);
		if (StringUtil.isNotBlank(eventCommAreaNonFinance.getMediaUnitCode())) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(eventCommAreaNonFinance.getMediaUnitCode());
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null != coreMediaBasicInfo) {
				eventCommAreaNonFinance.setCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
				eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
			}
		}
		return eventCommAreaNonFinance;
	}

	/**
	 * 查询客户下所有有效媒介
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private List<CoreMediaBasicInfo> queryCustomerMediaList(String customerNo,String eventNo) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
		if (!"BSS.BN.50.0010".equals(eventNo)) {
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(Constant.MEDIA_INVALID_FLAG);
		}
		List<CoreMediaBasicInfo> mediaList = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (null == mediaList) {
			throw new BusinessException("COR-00001");
		}
		return mediaList;
	}

	/**
	 * 根据客户号查询客户延滞信息
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private List<CoreCustomerDelinquency> queryCustomerDelinquencyList(String customerNo) throws Exception {
		CoreCustomerDelinquencySqlBuilder coreCustomerDelinquencySqlBuilder = new CoreCustomerDelinquencySqlBuilder();
		coreCustomerDelinquencySqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerDelinquencyDao.selectListBySqlBuilder(coreCustomerDelinquencySqlBuilder);
		return null;
	}

	/**
	 * 根据延滞表里的账号和币种查询账户表的业务项目代码
	 * 
	 * @param coreCustomerDelinquency
	 * @return
	 * @throws Exception
	 */
	private CoreAccount queryBusinessProgramCode(CoreCustomerDelinquency coreCustomerDelinquency) throws Exception {
		String accountId = coreCustomerDelinquency.getLevelCode();
		String currencyCode = coreCustomerDelinquency.getCurrencyCode();
		CoreAccount account = coreAccountDao.selectBySqlBuilder(
				new CoreAccountSqlBuilder().andAccountIdEqualTo(accountId).andCurrencyCodeEqualTo(currencyCode));
		if (account == null) {
			throw new BusinessException("COR-10001");
		}
		if (StringUtil.isBlank(account.getBusinessProgramNo())) {
			throw new BusinessException("COR-10062");
		}
		return account;
	}

	/**
	 * 查询客户下所有有产品
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private List<CoreProduct> queryCustomerProductList(String customerNo) throws Exception {
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		List<CoreProduct> productList = coreProductDao.selectListBySqlBuilder(coreProductSqlBuilder);
		if (null == productList) {
			throw new BusinessException("无法定位产品单元基本信息");
		}
		return productList;
	}

	@SuppressWarnings("rawtypes")
	public ResponseVO requestFeeHandler(Object obj) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		if(obj instanceof HashMap){
			CachedBeanCopy.mapToBean((HashMap<String, Object>)obj, eventCommAreaNonFinance);
		}else{
			CachedBeanCopy.copyProperties(obj ,eventCommAreaNonFinance);
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		HashMap<String, Object> requestMap = new HashMap<>();
			requestMap.put("ecommEventId", eventCommAreaNonFinance.getEventNo());
			requestMap.put("ecommEntryId", eventCommAreaNonFinance.getExternalIdentificationNo());
			String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
			if (StringUtil.isNotBlank(externalIdentificationNo)) {
				CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService
						.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
				if (null != coreMediaBasicInfo) {
					CoreCustomer coreCustomer = queryCustomerService
							.queryCustomer(coreMediaBasicInfo.getMainCustomerNo());
					requestMap.put("ecommProdObjId", coreMediaBasicInfo.getProductObjectCode());
					requestMap.put("ecommSystemUnitNo", coreCustomer.getSystemUnitNo());
					requestMap.put("ecommMediaUnit", coreMediaBasicInfo.getMediaUnitCode());
					requestMap.put("ecommProcessingDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
					requestMap.put("ecommProcessingTime", DateUtil.format(new Date(), "HH:mm:ss"));
					requestMap.put("ecommOperMode", coreMediaBasicInfo.getOperationMode());
					requestMap.put("ecommCustId", coreMediaBasicInfo.getMainCustomerNo());
					ResponseVO responseVO1 = feeTriggerUtil.feeHandle(requestMap);
					if (!Constant.SUCCESS_CODE.equals(responseVO1.getReturnCode())) {
						if ("BSS.OP.01.0004".equals(eventCommAreaNonFinance.getEventNo())) {
							throw new BusinessException("CUS-00123");
						}else if ("ISS.OP.01.0003".equals(eventCommAreaNonFinance.getEventNo())) {
							throw new BusinessException("CUS-00123");
						}else {
							throw new BusinessException("CUS-00123",responseVO1.getReturnMsg());
						}
				    }
					return responseVO1;
				}
			}
		return responseVO;
	}

	/**
	 * 根据媒介单元查询媒介单元基本信息
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private CoreMediaBasicInfo queryMediaUnitCodeMediaBasicInfo(String mediaUnitCode) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (coreMediaBasicInfo == null) {
			throw new BusinessException("无法定位媒介单元基本信息");
		}
		return coreMediaBasicInfo;
	}

	/**
	 * 查询媒介单元基本信息
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private CoreMediaBasicInfo queryExternalIdentificationNoMediaBasicInfo(String externalIdentificationNo)
			throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (coreMediaBasicInfo == null) {
			throw new BusinessException("无法定位媒介单元基本信息");
		}
		return coreMediaBasicInfo;
	}

	/**
	 * 查询客户管控事件
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private List<CoreCustomerContrlEvent> queryCustomerContrlEvent(String customerNo, String contrlLevel,
			String levelCode) throws Exception {
		CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
		coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerContrlEventSqlBuilder.andContrlLevelEqualTo(contrlLevel);
		coreCustomerContrlEventSqlBuilder.andLevelCodeEqualTo(levelCode);
		List<CoreCustomerContrlEvent> list = coreCustomerContrlEventDao
				.selectListBySqlBuilder(coreCustomerContrlEventSqlBuilder);
		return list;
	}

	/**
	 * 查询是否存在
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private Boolean queryExist(List<CoreCustomerContrlEvent> list, String eventId) throws Exception {
		Boolean isExist = false;
		if (list != null && !list.isEmpty()) {
			for (CoreCustomerContrlEvent temp : list) {
				if (temp.getEventNo().equals(eventId)) {
					return true;
				}
			}
		}
		return isExist;
	}
}
