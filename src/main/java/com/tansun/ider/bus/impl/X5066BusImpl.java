package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5066Bus;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreOperationModeCurrency;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.model.AdjustmentLimitBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5065BO;
import com.tansun.ider.model.bo.X7250BO;
import com.tansun.ider.model.bo.X7410BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BetaUtilInvoker;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CommonInterfaceUtil;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 客户额度调整
 * @author: admin
 */
@Service
public class X5066BusImpl implements X5066Bus {
	
	private static Logger logger = LoggerFactory.getLogger(X5066BusImpl.class);
	/** 262AAA0400元件 -- 永久额度调整**/
	private static final String ELEMENT_NO_262AAA0400 = "262AAA0400";
	/** 262AAA0401元件 -- 临时额度调整**/
	private static final String ELEMENT_NO_262AAA0401 = "262AAA0401";
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	public OperationModeUtil operationModeUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	private static final String BSS_OP_01_0004 = "BSS.OP.01.0004";//媒介激活
	
	@Value("${global.target.service.url.beta}")
	private String betaUrl;
	@Value("${global.target.service.url.auth}")
	private String authUrl;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object busExecute(X5065BO x5065bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5065bo,eventCommAreaNonFinance);
		String eventNo=eventCommAreaNonFinance.getEventNo();
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		String operatorId = eventCommAreaNonFinance.getOperatorId();
		// 判断是否需要调用调额交易
		if (BSS_OP_01_0004.equals(eventNo)) {
			boolean flag = this.triggerAdjustmentTransaction(eventCommAreaNonFinance, eventCommAreaNonFinance.getOperationMode(), eventCommAreaNonFinance.getProductObjectCode());
			if (flag) {
				// 需要调用 LMS.IQ.01.0004 调这个返回额度和节点 调LMS.PM.01.0015 返回节点
				Map<String, String> params = new HashMap<String, String>();
				// 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
				params.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
				params.put(Constant.PRODUCT_OBJECT_CODE, eventCommAreaNonFinance.getProductObjectCode());
				params.put(Constant.PARAM_OPER_MODE, eventCommAreaNonFinance.getOperationMode());
				params.put(Constant.AUTH_SYN_FLAG, "1");
				// 同步调起金融事件
				String result = BetaUtilInvoker.getInstance().execute(authUrl, EVENT.LMS_IQ_01_0021,
						JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect));
				List<X7250BO> x7205boAddList = new ArrayList<>();
				if (StringUtil.isNotBlank(result)) {
					x7205boAddList = JSON.parseArray(result, X7250BO.class);
				}
				// 查询授信额度节点
				params.put(Constant.CUSTOMER_NO, eventCommAreaNonFinance.getMainCustomerNo());
				String result1 = BetaUtilInvoker.getInstance().execute(authUrl, EVENT.LMS_IQ_01_0004,
						JSON.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect));
				if (StringUtil.isNotBlank(result1)) {
					// 反循环授信的节点
					List<X7410BO> x7410boAddList = new ArrayList<X7410BO>();
					x7410boAddList = JSON.parseArray(result1, X7410BO.class);
					if (null != x7205boAddList && !x7205boAddList.isEmpty() && null != x7410boAddList
							&& !x7410boAddList.isEmpty()) {
						// 获取当前应用节点需要调额金额
						List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
						for (X7250BO x7250BO : x7205boAddList) {
							for (X7410BO x7410BO : x7410boAddList) {
								if (x7250BO.getCreditNodeNo().equals(x7410BO.getCreditNodeNo())) {
									// 则当前额度节点需要调整额度,反之则不需要调整额度节点
									String operationMode = eventCommAreaNonFinance.getOperationMode();
									CoreOperationMode coreOperationMode = httpQueryService
											.queryOperationMode(operationMode);
									//运营币种当作授信币种
									String accountCurrency = coreOperationMode.getAccountCurrency();
									if (accountCurrency.equals(x7410BO.getCurrencyCode())) {
										//判断当前授信节点的额度构建
										String limitType = this.adjustmentType(x7250BO.getCreditNodeNo(), eventCommAreaNonFinance,operationMode);
										AdjustmentLimitBean adjustmentLimitBean =  new AdjustmentLimitBean();
										adjustmentLimitBean.setCorporation(eventCommAreaNonFinance.getCorporationEntityNo());
										adjustmentLimitBean.setCustomerNo(eventCommAreaNonFinance.getMainCustomerNo());
										adjustmentLimitBean.setExternalIdentificationNo(externalIdentificationNo);
										adjustmentLimitBean.setProductObjectCode(eventCommAreaNonFinance.getProductObjectCode());
										adjustmentLimitBean.setCreditNodeNo(x7250BO.getCreditNodeNo());
										adjustmentLimitBean.setCurrencyCode(accountCurrency);
										adjustmentLimitBean.setAdjustType(limitType);
										adjustmentLimitBean.setAdjusClass("B");
										BigDecimal creditLimit = BigDecimal.ZERO;
										if ("1".equals(limitType)) { //永久额度
											creditLimit = x7410BO.getPermLimit();
										}else if ("3".equals(limitType)) {//临时额度
											creditLimit = x7410BO.getTempLimit();
										}
										adjustmentLimitBean.setCreditLimit(creditLimit.toString());
										adjustmentLimitBean.setLimitEffectvDate(x7410BO.getTempLimitEffectvDate());
										adjustmentLimitBean.setLimitExpireDate(x7410BO.getTempLimitExpireDate());
										adjustmentLimitBean.setOperationMode(operationMode);
										adjustmentLimitBean.setOperatorId(operatorId);
										Map<String, Object> triggerEventParamsNew = new HashMap<String, Object>();
										adjustmentLimitBean.setAuthDataSynFlag("1");
										triggerEventParamsNew.put(Constant.KEY_TRIGGER_PARAMS, adjustmentLimitBean);
										eventCommAreaTriggerEventList.add(triggerEventParamsNew);
									}else {
										//判断当前授信节点的额度构建
										String limitType = this.adjustmentType(x7250BO.getCreditNodeNo(), eventCommAreaNonFinance, operationMode);
										AdjustmentLimitBean adjustmentLimitBean =  new AdjustmentLimitBean();
										adjustmentLimitBean.setCorporation(eventCommAreaNonFinance.getCorporationEntityNo());
										adjustmentLimitBean.setCustomerNo(eventCommAreaNonFinance.getMainCustomerNo());
										adjustmentLimitBean.setExternalIdentificationNo(externalIdentificationNo);
										adjustmentLimitBean.setProductObjectCode(eventCommAreaNonFinance.getProductObjectCode());
										adjustmentLimitBean.setCreditNodeNo(x7250BO.getCreditNodeNo());
										adjustmentLimitBean.setCurrencyCode(accountCurrency);
										adjustmentLimitBean.setAdjustType(limitType);
										adjustmentLimitBean.setAdjusClass("B");
										BigDecimal creditLimit = BigDecimal.ZERO;
										if ("1".equals(limitType)) {
											creditLimit= proportionConversion(operationMode, x7410BO.getCurrencyCode(), accountCurrency, false, x7410BO.getPermLimit());
										}else if ("3".equals(limitType)) {
											creditLimit = proportionConversion(operationMode, x7410BO.getCurrencyCode(), accountCurrency, false, x7410BO.getTempLimit());
										}
										adjustmentLimitBean.setCreditLimit(creditLimit.toString());
										adjustmentLimitBean.setLimitEffectvDate(x7410BO.getTempLimitEffectvDate());
										adjustmentLimitBean.setLimitExpireDate(x7410BO.getTempLimitExpireDate());
										adjustmentLimitBean.setOperationMode(operationMode);
										adjustmentLimitBean.setOperatorId(operatorId);
										Map<String, Object> triggerEventParamsNew = new HashMap<String, Object>();
										adjustmentLimitBean.setAuthDataSynFlag("1");
										triggerEventParamsNew.put(Constant.KEY_TRIGGER_PARAMS, adjustmentLimitBean);
										eventCommAreaTriggerEventList.add(triggerEventParamsNew);
									}
								}
							}
						}
						eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
					}
				} else {
					//不需要调额
				}
			} else {
				//不需要调额
			}
		}
		return eventCommAreaNonFinance;
	}

	/**
	 * 判断是否需要调用调额接口
	 * @param eventCommAreaNonFinance
	 * @param artifactList
	 * @param operationMode
	 * @param productObjectCode
	 * @return
	 * @throws Exception
	 */
	private boolean triggerAdjustmentTransaction(EventCommAreaNonFinance eventCommAreaNonFinance, String operationMode, String productObjectCode)
			throws Exception {
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(operationMode);
		eventCommArea.setEcommProdObjId(productObjectCode);
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_410, eventCommArea);
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		boolean flag = false;
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.TRIGGER_ADJUSTMENT_TRANSACTION_N.equals(entry.getKey())) { // 410AAA0200
				flag = false;
			} else if (Constant.TRIGGER_ADJUSTMENT_TRANSACTION_Y.equals(entry.getKey())) { // 410AAA0201
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 通过额度节点获取调额类型
	 * @param creditNodeNo
	 * @param eventCommAreaNonFinance
	 * @param artifactList
	 * @param operationMode
	 * @return
	 * @throws Exception
	 */
	private String adjustmentType(String creditNodeNo,EventCommAreaNonFinance eventCommAreaNonFinance, String operationMode) throws Exception{
		String limitType = null;
		// 授信节点262构件对应的元件为262AAA0102时，需要判断产品对象存不存在，不存在时，交易拒绝
		Map<String,String> creditNodeMap262 = CommonInterfaceUtil.getComponent(creditNodeNo, operationMode, BSC.ARTIFACT_NO_262);
		if (!creditNodeMap262.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = creditNodeMap262.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				// 额度调整
				if (ELEMENT_NO_262AAA0400.equals(entry.getKey())) {  
//					调额类型
					limitType = "1";
				} else if (ELEMENT_NO_262AAA0401.equals(entry.getKey())) {
					limitType = "3";
				} else {
					continue;
				}
			}
	    }
		return limitType;
	}
	
	private BigDecimal proportionConversion(String operationMode,String currencyCode,String accountCurrency,boolean convertToaccountCurrency,BigDecimal creditLimit )throws Exception{
		if (StringUtil.isNotBlank(currencyCode)|| StringUtil.isNotBlank(accountCurrency)) {
			
		}
		if (creditLimit.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		if (currencyCode.equals(accountCurrency)) {
			return creditLimit;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("币种转换 ==>传值币种: "+ currencyCode + "==> 运营币种: " +accountCurrency);
		}
		//获取授信比例
		CoreOperationModeCurrency operationModeCurrency = httpQueryService.queryOneOperationModeCurrency(operationMode, currencyCode);
		if (null == operationModeCurrency) {
			
		}
		BigDecimal creditProportion = new BigDecimal(operationModeCurrency.getCreditProportion());
		BigDecimal transFerAmat = BigDecimal.ZERO;
		if (convertToaccountCurrency) {
			transFerAmat = creditLimit.divide(creditProportion,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_DOWN);
		}else {
			transFerAmat = creditLimit.multiply(creditProportion).setScale(2,BigDecimal.ROUND_DOWN);
		}
		return transFerAmat;
	}

}
