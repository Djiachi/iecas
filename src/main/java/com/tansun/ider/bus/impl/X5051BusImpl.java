package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5051Bus;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreOperationModeCurrency;
import com.tansun.ider.dao.beta.entity.CorePcdInstan;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.CreditGrantCurrencyBean;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5051BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.CommonInterfaceForArtServiceImpl;
import com.tansun.ider.util.BetaUtilInvoker;
import com.tansun.ider.util.CacheUtils;
import com.tansun.ider.web.WSC;

/**
 * 按照产品归属币种,分别授信
 * @author admin
 *
 */
@Service
public class X5051BusImpl implements X5051Bus {

	@Autowired
	private CommonInterfaceForArtServiceImpl commonInterfaceForArtServiceImpl;
	@Autowired
	private HttpQueryService httpQueryService;
	
	@Value("${global.target.service.url.beta}")
	private String betaUrl;
	
	/** 261构件**/
	 private static final String ARTIFACT_NO_261 = "261";
	 /** 262构件**/
	 private static final String ARTIFACT_NO_262 = "262";
	 /** 261AAA0199元件**/
	 private static final String ELEMENT_NO_261AAA0199 = "261AAA0199";
	 /** 262AAA0102元件**/
	 private static final String ELEMENT_NO_262AAA0102 = "262AAA0102";
	 /** 262AAA0400元件*/
	 private static final String ELEMENT_NO_262AAA0400 = "262AAA0400";
	 /** 262AAA0401元件*/
	 private static final String ELEMENT_NO_262AAA0401 = "262AAA0401";
	 /** 401构件**/
	 private static final String ARTIFACT_NO_401 = "401";
	 /** 401AAA0100元件**/
	 private static final String ELEMENT_NO_401AAA0100 = "401AAA0100";
	 /** 401AAA0101元件**/
	 private static final String ELEMENT_NO_401AAA0101 = "401AAA0101";
	 /** 401AAA0102元件**/
	 private static final String ELEMENT_NO_401AAA0102 = "401AAA0102";
	 /** 401AAA0103元件**/
	 private static final String ELEMENT_NO_401AAA0103 = "401AAA0103";
	/** 授信类型   P： 永久额度   T：临时额度 **/
	 private static final String creditType =  "P";
	 
	@Override
	public Object busExecute(X5051BO x5051bo) throws Exception {
		//产品对象代码
	    String productObjectCode = x5051bo.getProductObjectCode();
	    String operationMode = x5051bo.getOperationMode();
	    String customerNo = x5051bo.getCustomerNo();
	    String externalIdentificationNo = x5051bo.getExternalIdentificationNo();
	    String creditNodeNo = x5051bo.getCreditNodeNo();
	    String corporation = x5051bo.getCorporation();
	    String creditCurrencyCode = x5051bo.getCurrencyCode();
	    String quotaCreditLimit = x5051bo.getCreditLimit();
//	    currencyCode
	    String binNo  = externalIdentificationNo.substring(0, 6);
	  //获取运营模式
		  CoreOperationMode coreOperationMode = null;
		  String keyOpe = Constant.PARAMS_FLAG + operationMode;
		  coreOperationMode = (CoreOperationMode)CacheUtils.getInstance().getMap(CoreOperationMode.class,keyOpe);
		  if ( null == coreOperationMode ) {
		   Map<String, String> params = new HashMap<String, String>();
		         params.put(Constant.PARAM_OPER_MODE, operationMode);
		         params.put(WSC.REDIS_KEY, keyOpe);
		         // 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
		         params.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		         // 同步调起金融事件
		         String result = BetaUtilInvoker.getInstance().execute(betaUrl, EVENT.COS_IQ_02_0006, JSON.toJSONString(params,SerializerFeature.DisableCircularReferenceDetect));
		         if (StringUtil.isNotBlank(result)) {
		          coreOperationMode = JSON.parseObject(result, CoreOperationMode.class, Feature.DisableCircularReferenceDetect);
		         } else {
		          throw new BusinessException("AUTH-00011");
		         }
		  }
		  //运营币种
		  String accountCurrency = coreOperationMode.getAccountCurrency();
	    
		//清算币种
		  String settleCurrency = null;   
		  //单双币卡标识   fasle： 双币卡   true： 单币卡
		   String keyBin = Constant.PARAMS_FLAG + binNo;
		   CoreIssueCardBin coreIssueCardBin = (CoreIssueCardBin) CacheUtils.getInstance().getMap(CoreIssueCardBin.class, keyBin);
		   if (null == coreIssueCardBin) {
		    Map<String, String> params = new HashMap<String, String>();
		    params.put("binNo", binNo);
		    params.put(WSC.REDIS_KEY, keyOpe);
		          // 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
		          params.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		          // 同步调起金融事件
		          String result = BetaUtilInvoker.getInstance().execute(betaUrl, EVENT.COS_IQ_02_0011, JSON.toJSONString(params,SerializerFeature.DisableCircularReferenceDetect));
		          if (StringUtil.isNotBlank(result)) {
		           coreIssueCardBin = JSON.parseObject(result, CoreIssueCardBin.class,Feature.DisableCircularReferenceDetect);
		          } else {
		           throw new BusinessException("AUTH-00180");
		          }
		   }
		   //清算币种
		settleCurrency = coreIssueCardBin.getSettlementCurrency();
	    //获取产品对应币种
	    List<String> currencyCodeList = getCreditCurr(operationMode, productObjectCode, creditNodeNo, binNo,accountCurrency,settleCurrency);
	    //获取授信额度
//	        授信接口中的币种和产品授信币种不一致时，需重算授信额度，计算方式为：
//	        授信额度 = 运营币种额度 * 授信比例；如果接口中授信币种不为运营币种，则先计算运营币种额度，
//	        运营币种额度  = 授信币种额度 / 授信比例；
	    EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
	    if (null != currencyCodeList && !currencyCodeList.isEmpty()) {
	    	//不为空情况
	    	List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
	    	if (currencyCodeList.size() ==1) {
	    		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
	    		CreditGrantCurrencyBean creditGrantCurrencyBean = new CreditGrantCurrencyBean();
	    		creditGrantCurrencyBean.setCurrencyCode(currencyCodeList.get(0));
	    		creditGrantCurrencyBean.setCustomerNo(customerNo);
	    		creditGrantCurrencyBean.setCorporation(corporation);
	    		creditGrantCurrencyBean.setCreditType(creditType);
	    		creditGrantCurrencyBean.setCreditLimit(new BigDecimal(quotaCreditLimit));
	    		creditGrantCurrencyBean.setCreditNodeNo(creditNodeNo);
	    		creditGrantCurrencyBean.setExternalIdentificationNo(externalIdentificationNo);
	    		creditGrantCurrencyBean.setOperationMode(operationMode);
	    		creditGrantCurrencyBean.setProductObjectCode(productObjectCode);
	    		creditGrantCurrencyBean.setAuthDataSynFlag("1");
	    		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, creditGrantCurrencyBean);
	    		eventCommAreaTriggerEventList.add(triggerEventParams);
	    		eventCommAreaNonFinance.setAuthDataSynFlag("1");
	    		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
	    		return eventCommAreaNonFinance;
			}
	    	for (String currencyCode : currencyCodeList) {
	    		//计算额度
	    		BigDecimal creditLimit = BigDecimal.ZERO;
//	    		queryOperationModeCurrency
	    		//判断授信币种是不是运营币种
	    		if (!coreOperationMode.getAccountCurrency().equals(creditCurrencyCode)) {
	    			throw new BusinessException("CUS-00147");
				}
	    		if (currencyCode.equals(coreOperationMode.getAccountCurrency())) {
	    			creditLimit = new BigDecimal(quotaCreditLimit);
				}else {
					CoreOperationModeCurrency coreOperationModeCurrency = httpQueryService.queryOneOperationModeCurrency(operationMode, currencyCode);
					creditLimit = new BigDecimal(quotaCreditLimit).multiply(new BigDecimal(coreOperationModeCurrency.getCreditProportion()));
				}
	    		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
	    		CreditGrantCurrencyBean creditGrantCurrencyBean = new CreditGrantCurrencyBean();
	    		creditGrantCurrencyBean.setCurrencyCode(currencyCode);
	    		creditGrantCurrencyBean.setCustomerNo(customerNo);
	    		creditGrantCurrencyBean.setCorporation(corporation);
	    		creditGrantCurrencyBean.setCreditType(creditType);
	    		creditGrantCurrencyBean.setCreditLimit(creditLimit);
	    		creditGrantCurrencyBean.setCreditNodeNo(creditNodeNo);
	    		creditGrantCurrencyBean.setExternalIdentificationNo(externalIdentificationNo);
	    		creditGrantCurrencyBean.setOperationMode(operationMode);
	    		creditGrantCurrencyBean.setProductObjectCode(productObjectCode);
	    		creditGrantCurrencyBean.setAuthDataSynFlag("1");
	    		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, creditGrantCurrencyBean);
	    		eventCommAreaTriggerEventList.add(triggerEventParams);
			}
	    	eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
	    	return eventCommAreaNonFinance;
		}else {
			//为空情况
			return x5051bo;
		}
	}
	
	
	public List<String> getCreditCurr(String operationMode, String productObjectCode, String creditNodeNo,String binNo,
			   String accountCurrency,String settleCurrency) throws Exception {
		
		//事件公共区
		List<String> pcdList = new ArrayList<String>();
		Map<String, String> elePcdResultMap = new HashMap<String, String>(16);
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(operationMode);
		eventCommArea.setEcommProdObjId(productObjectCode);
		elePcdResultMap = commonInterfaceForArtServiceImpl.getElementByArtifact(ARTIFACT_NO_401, eventCommArea);
		if (!elePcdResultMap.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				List<CorePcdInstan> corePcdInstanList = JSON.parseArray(entry.getValue(),CorePcdInstan.class);
				for (CorePcdInstan corePcdInstan : corePcdInstanList) {
					pcdList.add(corePcdInstan.getPcdValue());
				}
			}
		}
		
		//进行261构件判断，确定授信节点维度
		Map<String,String> creditNodeMap = getComponent(creditNodeNo, operationMode, ARTIFACT_NO_261);
		boolean  dimenNonFlag =false;
		if (!creditNodeMap.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = creditNodeMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (ELEMENT_NO_261AAA0199.equals(entry.getKey())) {
					//不支持应用节点
					dimenNonFlag = true;
					break;
				} else {
					continue;
				}
			}
		}
		
		Map<String,String> creditNodeMap262 = getComponent(creditNodeNo, operationMode, ARTIFACT_NO_262);
		if (!creditNodeMap262.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = creditNodeMap262.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				// 人工关联授信
				if (ELEMENT_NO_262AAA0102.equals(entry.getKey())) {  
					if (dimenNonFlag) {
						throw new BusinessException("AUTH-00173");
					} else if (StringUtil.isEmpty(productObjectCode)) {
						throw new BusinessException("AUTH-00172");
					}
					
				} else {
					continue;
				}
			}
	   }
		
		List<String> currList = new ArrayList<String>();
		Map<String,String> creditCurrMap = commonInterfaceForArtServiceImpl.getElementByArtifact(ARTIFACT_NO_401, eventCommArea);
		if (!creditCurrMap.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = creditCurrMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (ELEMENT_NO_401AAA0100.equals(entry.getKey())) {  
					//元件为“401AAA0100”时，应用节点币种为运营币种（运营币种后续单独处理，此处不进行操作）
					currList.add(accountCurrency);
				} else if (ELEMENT_NO_401AAA0101.equals(entry.getKey())) {
					//元件为“401AAA0101”时，应用节点币种为 清算币种（运营币种后续单独处理，此处不进行操作）
					currList.add(settleCurrency);
				} else if (ELEMENT_NO_401AAA0102.equals(entry.getKey())) { 
					//元件为“401AAA0102”时，应用节点币种为 PCD币种list + 运营币种（运营币种后续单独处理，此处不进行操作）
//					currList.addAll(pcdList);
					currList.add(accountCurrency);
					currList.add(settleCurrency);
				}else {
					continue;
				}
			}
		}else {
			currList.add(accountCurrency);
		}
		
		List<String> creditCurrList = new ArrayList<String>(); 
		//维度为不支持应用节点时，授信币种只能为运营币种
		if(dimenNonFlag) {
			creditCurrList.add(accountCurrency);
		} else {
			creditCurrList.add(accountCurrency);
			//通过401构件获取的币种list
			for (String appCurr : currList) {
				creditCurrList.add(appCurr);
			}
		}
		// 币种去重
		creditCurrList = currList.stream().distinct().collect(Collectors.toList());
		return currList;
	}
	
	/**
	 * 是否收取挂失费
	 * @param creditNodeNo
	 * @param operationMode
	 * @param artifactNo
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getComponent(String creditNodeNo, String operationMode, String artifactNo)
            throws Exception {
        EventCommArea eventCommArea = new EventCommArea();
        // 运营模式
        eventCommArea.setEcommOperMode(operationMode);
        // 额度节点编号
        eventCommArea.setEcommAuthCreditNode(creditNodeNo);
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        return artService.getElementByArtifact(artifactNo, eventCommArea);
    }
	
}
