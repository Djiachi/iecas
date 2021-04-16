package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5335Bus;
import com.tansun.ider.dao.issue.CoreExcptTransInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreExcptTransInfo;
import com.tansun.ider.dao.issue.entity.CoreExcptTransInfoKey;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreExcptTransInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.model.bo.X5330BO;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 异常交易操作
 * 
 * @author yanzhaofei 2018年10月15日
 */
@Service
public class X5335BusImpl implements X5335Bus {
	@Autowired
	private CoreExcptTransInfoDao coreExcptTransInfoDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	/**
	 * N-未处理 P-交易删除 Y-人工重发入账
	 */
	private static String transBillingState_N = "N";
	private static String transBillingState_P = "P";
	private static String transBillingState_Y = "Y";
	@Value("${global.target.service.url.card}")
	private String cardUrl;

	@Override
	public Object busExecute(X5330BO x5330bo) throws Exception {
		ResponseVO responseVO = new ResponseVO();
		String id = x5330bo.getTransId();
		String transFlag = x5330bo.getTransFlag();
		//String externalIdentificationNo = x5330bo.getExternalIdentificationNo();
		String newExternalIdentificationNo = x5330bo.getNewExternalIdentificationNo();
		String type = x5330bo.getType();
		String transDate = x5330bo.getTransDate();
		CoreExcptTransInfoSqlBuilder coreExcptTransInfoSqlBuilder = new CoreExcptTransInfoSqlBuilder();
		CoreExcptTransInfoKey coreExcptTransInfoKey = new CoreExcptTransInfoKey();
		coreExcptTransInfoSqlBuilder.andIdEqualTo(id);
		coreExcptTransInfoKey.setId(id);
		CoreExcptTransInfo coreExcptTransInfo = coreExcptTransInfoDao.selectByPrimaryKey(coreExcptTransInfoKey);
		if (transBillingState_N.equals(coreExcptTransInfo.getTransBillingState())) {
			EventCommArea ecomm = setEventCommAreaData(coreExcptTransInfo,transFlag);
			if (transBillingState_Y.equals(type)) {
				if(StringUtil.isNotBlank(transDate)){
					coreExcptTransInfo.setTransDate(transDate);
				}
				if (StringUtil.isNotBlank(newExternalIdentificationNo)) {
					if (!newExternalIdentificationNo.equals(coreExcptTransInfo.getExternalIdentificationNo())) {
						// 如果重新输入了外部识别号，校验与原外部识别号是否为同一持卡人
						CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
						coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(newExternalIdentificationNo);
						CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
								.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
						if (coreMediaBasicInfo == null) {
							throw new Exception("定位不到客户");
						}
						String customerNoNew = coreMediaBasicInfo.getMainCustomerNo();
						coreMediaBasicInfoSqlBuilder.clear();
						coreMediaBasicInfoSqlBuilder
								.andExternalIdentificationNoEqualTo(coreExcptTransInfo.getExternalIdentificationNo());
						coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
						if (coreMediaBasicInfo == null) {
							throw new Exception("定位不到客户");
						}
						String customerNoOld = coreMediaBasicInfo.getMainCustomerNo();
						if (!customerNoNew.equals(customerNoOld)) {
							throw new Exception("外部识别号与原异常交易外部识别号的持卡人不一致");
						} else {
							coreExcptTransInfo.setExternalIdentificationNo(newExternalIdentificationNo);
						}
					}
				}
				responseVO = executeEvent(coreExcptTransInfo.getEventNo(), ecomm);
				// logger.info("&&&&&&&&&responseVO.getReturnCode()={}----------$$$$$$---",
				// responseVO.getReturnCode());
				if (WSC.DEFAULT_RETURN_CODE.equals(responseVO.getReturnCode())) {
					coreExcptTransInfo.setVersion(coreExcptTransInfo.getVersion() + 1);
					coreExcptTransInfo.setTransBillingState(transBillingState_Y);
					coreExcptTransInfoDao.updateBySqlBuilderSelective(coreExcptTransInfo, coreExcptTransInfoSqlBuilder);
				} else {
					throw new Exception("入账失败！");
				}
				// 同步触发，需等待触发事件处理结束
			} else if (transBillingState_P.equals(type)) {
				coreExcptTransInfoSqlBuilder.andVersionEqualTo(coreExcptTransInfo.getVersion());
				coreExcptTransInfo.setVersion(coreExcptTransInfo.getVersion() + 1);
				coreExcptTransInfo.setTransBillingState(transBillingState_P);
				coreExcptTransInfoDao.updateBySqlBuilderSelective(coreExcptTransInfo, coreExcptTransInfoSqlBuilder);
			}
		} else {
			throw new Exception("该交易已完成重入账或者删除操作。");
		}
		return null;
	}

	/**
	 * 同步触发事件
	 * 
	 * @param coreExcptTransInfo
	 * @param eventNo
	 * @throws Exception
	 */
	private ResponseVO executeEvent(String eventNo, EventCommArea ecomm) throws Exception {
		// 根据事件编号触发入账事件
		String url = cardUrl;
		String params = JSON.toJSONString(ecomm, SerializerFeature.DisableCircularReferenceDetect);
		// logger.info("&&&&&&&&&url={}---------------------------------------------
		// ", url);
		params = JSON.toJSONString(ecomm,SerializerFeature.DisableCircularReferenceDetect);
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(WSC.DEFAULT_RETURN_CODE);
		responseVO.setReturnMsg(WSC.DEFAULT_RETURN_MESSAGE);
		RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
		HttpHeaders headers = new HttpHeaders();
		MediaType mType = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(mType);
		HttpEntity<String> entity = new HttpEntity<String>(params, headers);
		String returnMsg = restTemplate.postForObject(url + eventNo, entity, String.class);
		return responseVO = JSONObject.parseObject(returnMsg, ResponseVO.class, Feature.DisableCircularReferenceDetect);
	}

	/**
	 * 公共区赋值
	 * 
	 * @param coreExcptTransInfo
	 * @return
	 */
	private EventCommArea setEventCommAreaData(CoreExcptTransInfo coreExcptTransInfo,String transFlag) {
		EventCommArea ecommArea = new EventCommArea();
		ecommArea.setEcommPostingAcctNmbr(coreExcptTransInfo.getAccountId());
		ecommArea.setEcommAcquireReferenceNumbr(coreExcptTransInfo.getAcquireReferenceNumbr());
		ecommArea.setEcommAuthCde(coreExcptTransInfo.getAuthCode());
		ecommArea.setEcommFeeCtdAmount(coreExcptTransInfo.getBillCostBalance());
		ecommArea.setEcommIntCtdAmount(coreExcptTransInfo.getBillInterestBalance());
		ecommArea.setEcommPrinCtdAmount(coreExcptTransInfo.getBillPrincipalBalance());
		ecommArea.setEcommBusineseType(coreExcptTransInfo.getBusinessTypeCode());
		ecommArea.setEcommChannelCde(coreExcptTransInfo.getChannelCde());
		ecommArea.setEcommClearAmount(coreExcptTransInfo.getClearAmount());
		ecommArea.setEcommClearCurr(coreExcptTransInfo.getClearCurrCode());
		ecommArea.setEcommClearCurrDecimal(coreExcptTransInfo.getClearCurrDecimal());
		ecommArea.setEcommFeeStmtAmount(coreExcptTransInfo.getCurrCostBalance());
		ecommArea.setEcommIntStmtAmount(coreExcptTransInfo.getCurrInterestBalance());
		ecommArea.setEcommPrinStmtAmount(coreExcptTransInfo.getCurrPrincipalBalance());
		ecommArea.setEcommCustId(coreExcptTransInfo.getCustomerNo());
		ecommArea.setEcommEventId(coreExcptTransInfo.getEventNo());
		ecommArea.setEcommEntryId(coreExcptTransInfo.getExternalIdentificationNo());
		ecommArea.setEcommGlobalSerialNumbr(coreExcptTransInfo.getGlobalTransSerialNo());
		ecommArea.setEcommMcc(coreExcptTransInfo.getMcc());
		ecommArea.setEcommMerchantCde(coreExcptTransInfo.getMerchantCde());
		ecommArea.setEcommOperMode(coreExcptTransInfo.getOperationMode());
		ecommArea.setEcommOrigGlobalSerialNumbr(coreExcptTransInfo.getOriGlobalTransSerialNo());
		ecommArea.setEcommTransPostingAmount(coreExcptTransInfo.getPostingAmt());
		ecommArea.setEcommTransPostingCurr(coreExcptTransInfo.getPostingCurrCode());
		ecommArea.setEcommTransPostingCurrDecimal(coreExcptTransInfo.getPostingCurrDecimal());
		ecommArea.setEcommPostingExchangeRate(coreExcptTransInfo.getPostingExchangeRate());
		ecommArea.setEcommSourceCde(coreExcptTransInfo.getSourceCde());
		ecommArea.setEcommTransAmount(coreExcptTransInfo.getTransAmount());
		ecommArea.setEcommTransCityCde(coreExcptTransInfo.getTransCityCde());
		ecommArea.setEcommTransCountryCde(coreExcptTransInfo.getTransCountryCde());
		ecommArea.setEcommTransCurr(coreExcptTransInfo.getTransCurrCode());
		ecommArea.setEcommTransCurrDecimal(coreExcptTransInfo.getTransCurrDecimal());
		ecommArea.setEcommTransDate(coreExcptTransInfo.getTransDate());
		ecommArea.setEcommTransDesc(coreExcptTransInfo.getTransDesc());
		ecommArea.setEcommTransTime(coreExcptTransInfo.getTransTime());
		ecommArea.setEcommMandatoryEntry(transFlag);
		return ecommArea;
	}
}
