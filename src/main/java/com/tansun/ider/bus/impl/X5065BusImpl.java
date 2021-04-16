package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5065Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.ResultGns;
import com.tansun.ider.model.bo.X5065BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 媒介激活
 * @author: admin
 */
@Service
public class X5065BusImpl implements X5065Bus {

	private static Logger logger = LoggerFactory.getLogger(X5065BusImpl.class);

	private static final String EVENTNO_BSSAD010003 = "BSS.AD.01.0003";
	private static final String EVENTNO_ISSOP010014 = "ISS.OP.01.0014";
	private static final String EVENTNO_ISSOP010003 = "ISS.OP.01.0003";
	private static final String EVENTNO_ISSOP010004 = "ISS.OP.01.0004";
	private static final String EVENTNO_BSSAD019001 = "BSS.AD.01.9001";
	private static final String EVENTNO_ISSOP010005 = "ISS.OP.01.0005";

	/** 人工激活操作 */
	private static final String ACTIVATION_1 = "1";
	/** 自动激活操作 */
	private static final String ACTIVATION_2 = "2";
	/** 不执行自动激活操作 */
	private static final String ACTIVATION_3 = "3";
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	public OperationModeUtil operationModeUtil;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
    private CoreProductFormDao coreProductFormDao;
	
	private static final String BSS_OP_01_0004 = "BSS.OP.01.0004";//媒介激活

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object busExecute(X5065BO x5065bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5065bo,eventCommAreaNonFinance);
		// 失效原因字段
		String invalidReason = x5065bo.getInvalidReason();
		// 外部识别号
		String externalIdentificationNo = x5065bo.getExternalIdentificationNo();
		// 身份证号
		String idNumber = x5065bo.getIdNumber();	
		String idType = x5065bo.getIdType();
		String mediaUnitCode = x5065bo.getMediaUnitCode();
		// 当前操作员
		String operatorId = x5065bo.getOperatorId();
		List<CoreActivityArtifactRel> artifactList = x5065bo.getActivityArtifactList();
		if (StringUtil.isBlank(externalIdentificationNo) && StringUtil.isBlank(idNumber)
				&& StringUtil.isBlank(idType)) {
			throw new BusinessException("COR-10048");
		}
		
		/**
		 * 媒介激活，对无效卡激活成功，改为无效且无效原因不等于='RNA'和‘PNA'的卡不允许激活
		 * add by wangxi 2019/7/19  cyy提
		 */
		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5065bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		//BSS_OP_01_0004事件	媒介激活事件，判断无效原因不等于='RNA'和‘PNA'的卡
		if (BSS_OP_01_0004.equals(eventNo)){
			if(StringUtil.isNotBlank(invalidReason)){
				/** TRF-转卡 	EXP-到期	BRK-毁损	CLS-关闭	情况不允许激活**/
				if(invalidReason.equals(InvalidReasonStatus.TRF.getValue())
						|| invalidReason.equals(InvalidReasonStatus.EXP.getValue())
						|| invalidReason.equals(InvalidReasonStatus.BRK.getValue())
						|| invalidReason.equals(InvalidReasonStatus.CLS.getValue())){
					throw new BusinessException("CUS-00132");//该卡片已失效不支持激活操作！
				}else{
					invalidReason = null;
				}
			}
		}
		
		if (StringUtil.isBlank(invalidReason)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilder.andActivationFlagNotEqualTo("1");
			if (StringUtil.isNotEmpty(mediaUnitCode)) {
				coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
			} else {
				throw new BusinessException("CUS-00025", "外部识别号");
			}
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00080", "媒介单元代码");
			}
			CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,coreMediaBasicInfoAfter);
			EventCommArea eventCommArea = new EventCommArea();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo.getOperationMode());
			eventCommAreaNonFinance.setProductForm(coreMediaBasicInfo.getProductForm());
			eventCommAreaNonFinance.setCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
			String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
			if (activationFlag.equals(ACTIVATION_3)) {
				eventCommAreaNonFinance.setWhetherProcess("1");
				return eventCommAreaNonFinance;
			}
			// 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活',
			if ("1".equals(coreMediaBasicInfo.getActivationFlag())) {
				throw new BusinessException("CUS-00033");
			}
			coreMediaBasicInfo.setActivationFlag("1");
			if (activationFlag.equals(ACTIVATION_1)) {
				coreMediaBasicInfo.setActivationMode(ACTIVATION_1);
			} else if (activationFlag.equals(ACTIVATION_2)) {
				coreMediaBasicInfo.setActivationMode(ACTIVATION_2);
			}
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			String operateDate = coreSystemUnit.getNextProcessDate();
			coreMediaBasicInfo.setActivationDate(operateDate);
			coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
			coreMediaBasicInfo.setInvalidFlag("Y");
			coreMediaBasicInfo.setInvalidReason("");
			int result = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
					coreMediaBasicInfoSqlBuilder);
			if (result != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			if (StringUtil.isBlank(x5065bo.getBusinessProgramNo())) {
				List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
						.queryProductBusinessScope(coreMediaBasicInfo.getProductObjectCode(), eventCommAreaNonFinance.getOperationMode());
				CoreProductBusinessScope coreProductBusinessScope = null;
				if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
					coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
				} else {
					throw new BusinessException("CUS-00064");
				}
				String businessProgramNo = "";
				if (coreProductBusinessScope != null) {
					businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
					x5065bo.setBusinessProgramNo(businessProgramNo);
				}
			}
			List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
			Map<String, Object> triggerEventParamsOld = new HashMap<String, Object>();
			EventCommAreaNonFinance eventCommAreaNonFinanceOld = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceOld.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceOld.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinanceOld);
			eventCommAreaNonFinanceOld.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
			triggerEventParamsOld.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceOld);
			eventCommAreaTriggerEventList.add(triggerEventParamsOld);
			// 第一次传递参数
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
			eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
			eventCommAreaNonFinance.setCorporationEntityNo(coreCustomer.getCorporationEntityNo());
			eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
			eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder1.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
			coreMediaBasicInfoSqlBuilder1.andInvalidFlagEqualTo("Y");
			List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder1);
			List<ResultGns> coreMediaBasicInfoList1 = new ArrayList<>();
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
				ResultGns resultGns = new ResultGns();
				resultGns.setExternalIdentificationNo(coreMediaBasicInfo2.getExternalIdentificationNo());
				coreMediaBasicInfoList1.add(resultGns);
			}
			eventCommAreaNonFinance.setCoreMediaBasicInfoList(coreMediaBasicInfoList1);
			eventCommAreaNonFinance.setAuthDataSynFlag("1");
			// 当前操作员
			if (operatorId == null) {
				operatorId = "system";
			}
			nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
					ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
					coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
					coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
			
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderOld = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderOld.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderOld.andMediaUnitCodeNotEqualTo(mediaUnitCode);
			List<CoreMediaBasicInfo> listCoreMediaBasicInfo = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderOld);
			if (null != listCoreMediaBasicInfo && !listCoreMediaBasicInfo.isEmpty()) {
				for (CoreMediaBasicInfo coreMediaBasicInfo2 : listCoreMediaBasicInfo) {
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilderStr.andMediaUnitCodeEqualTo(coreMediaBasicInfo2.getMediaUnitCode());
					coreMediaBasicInfoSqlBuilderStr
							.andExternalIdentificationNoEqualTo(coreMediaBasicInfo2.getExternalIdentificationNo());
					coreMediaBasicInfo2.setInvalidFlag("N");
					coreMediaBasicInfo2.setInvalidReason(InvalidReasonStatus.CLS.getValue());
					int re = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo2,
							coreMediaBasicInfoSqlBuilderStr);
					if (re != 1) {
						throw new BusinessException("CUS-00012", "媒介单元");
					}
					Map<String, Object> triggerEventParamsNew = new HashMap<String, Object>();
					EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
					eventCommAreaNonFinanceNew.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
					eventCommAreaNonFinanceNew.setAuthDataSynFlag("1");
					eventCommAreaNonFinanceNew.setInvalidReasonOld(coreMediaBasicInfo2.getInvalidReason());
					CachedBeanCopy.copyProperties(coreMediaBasicInfo2,eventCommAreaNonFinanceNew);
					triggerEventParamsNew.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
					eventCommAreaTriggerEventList.add(triggerEventParamsNew);
					
				}
			}
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		} else if (invalidReason.equals(InvalidReasonStatus.RNA.getValue())) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,coreMediaBasicInfoAfter);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
			EventCommArea eventCommArea = new EventCommArea();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
			eventCommAreaNonFinance.setProductForm(coreMediaBasicInfo.getProductForm());
			String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
			if (activationFlag.equals(ACTIVATION_3)) {
				return eventCommAreaNonFinance;
			}
			// 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活',
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			String operateDate = coreSystemUnit.getNextProcessDate();
			coreMediaBasicInfo.setInvalidFlag("N");
			coreMediaBasicInfo.setInvalidReason(InvalidReasonStatus.EXP.getValue());
			coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
			int result = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
					coreMediaBasicInfoSqlBuilderStr);
			// 当前操作员
			if (operatorId == null) {
				operatorId = "system";
			}
			nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
					ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
					coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
					coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);

			if (result != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			// 第一次传递参数
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
			eventCommAreaNonFinance.setAuthDataSynFlag("1");
			eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
			/**
			 * 需要将新卡有效期标识修改，然后修改测试结果
			 */
			coreMediaBasicInfoSqlBuilderStr.clear();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.andActivationFlagEqualTo("3");
			coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.RNA.getValue());
			List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			if (listCoreMediaBasicInfos != null && !listCoreMediaBasicInfos.isEmpty()) {
				for (CoreMediaBasicInfo coreMediaBasicInfo2 : listCoreMediaBasicInfos) {
					CoreMediaBasicInfo coreMediaBasicInfoAfterO = new CoreMediaBasicInfo();
					CachedBeanCopy.copyProperties(coreMediaBasicInfo2,coreMediaBasicInfoAfterO);
					coreMediaBasicInfoSqlBuilderStr.clear();
					coreMediaBasicInfoSqlBuilderStr.andIdEqualTo(coreMediaBasicInfo2.getId());
					coreMediaBasicInfo2.setActivationFlag("1");
					if (activationFlag.equals(ACTIVATION_1)) {
						coreMediaBasicInfo2.setActivationMode(ACTIVATION_1);
					} else if (activationFlag.equals(ACTIVATION_2)) {
						coreMediaBasicInfo2.setActivationMode(ACTIVATION_2);
					}
					coreMediaBasicInfo2.setActivationDate(operateDate);
					coreMediaBasicInfo2.setInvalidFlag("Y");
					coreMediaBasicInfo2.setInvalidReason("");
					coreMediaBasicInfo2.setVersion(coreMediaBasicInfo2.getVersion() + 1);
					coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo2,
							coreMediaBasicInfoSqlBuilderStr);
					if (operatorId == null) {
						operatorId = "system";
					}
					nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
							ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfterO, coreMediaBasicInfo,
							coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
							coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
					EventCommAreaNonFinance eventCommAreaNonFinanceStr = new EventCommAreaNonFinance();
					CachedBeanCopy.copyProperties(coreMediaBasicInfo2,eventCommAreaNonFinanceStr);
					eventCommAreaNonFinanceStr.setAuthDataSynFlag("1");
					eventCommAreaNonFinanceStr.setInvalidReasonOld(coreMediaBasicInfo2.getInvalidReason());
				}
			}
		} else if (invalidReason.equals(InvalidReasonStatus.PNA.getValue())) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderOld = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderOld.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderOld.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfoOld = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderOld);
			EventCommArea eventCommArea = new EventCommArea();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfoOld.getProductObjectCode());
			eventCommAreaNonFinance.setProductForm(coreMediaBasicInfoOld.getProductForm());
			String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
			if (activationFlag.equals(ACTIVATION_3)) {
				return eventCommAreaNonFinance;
			}
			coreMediaBasicInfoOld.setInvalidReason(InvalidReasonStatus.CLS.getValue());
			coreMediaBasicInfoOld.setInvalidFlag("N");
			coreMediaBasicInfoOld.setVersion(coreMediaBasicInfoOld.getVersion() + 1);
			int resultOld = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfoOld,
					coreMediaBasicInfoSqlBuilderOld);
			if (resultOld != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			Map<String, Object> triggerEventParamsOld = new HashMap<String, Object>();
			EventCommAreaNonFinance eventCommAreaNonFinanceOld = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceOld.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceOld.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoOld,eventCommAreaNonFinanceOld);
			triggerEventParamsOld.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceOld);
			List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
			eventCommAreaTriggerEventList.add(triggerEventParamsOld);
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.andMediaUnitCodeEqualTo(x5065bo.getMediaUnitCode());
			coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.PNA.getValue());
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,coreMediaBasicInfoAfter);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
			// 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活',
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			String operateDate = coreSystemUnit.getNextProcessDate();
			coreMediaBasicInfo.setInvalidFlag("Y");
			coreMediaBasicInfo.setInvalidReason("");
			coreMediaBasicInfo.setActivationFlag("1");
			coreMediaBasicInfo.setActivationMode(activationFlag);
			coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
			int result = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
					coreMediaBasicInfoSqlBuilderStr);
			if (result != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();

			EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceNew.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceNew.setAuthDataSynFlag("1");
			eventCommAreaNonFinanceNew.setInvalidReason("");
			eventCommAreaNonFinanceNew.setInvalidReasonOld("");
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinanceNew);
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			// 当前操作员
			if (operatorId == null) {
				operatorId = "system";
			}
			nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
					ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
					coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
					coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
			// 第一次传递参数
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
			eventCommAreaNonFinance.setAuthDataSynFlag("1");
			eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
			/**
			 * 需要将新卡有效期标识修改，然后修改测试结果
			 */
			coreMediaBasicInfoSqlBuilderStr.clear();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.andActivationFlagEqualTo("3");
			coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.RNA.getValue());
			List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		}else if (invalidReason.equals(InvalidReasonStatus.TRF.getValue())) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderOld = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderOld.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderOld.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfoOld = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderOld);
			EventCommArea eventCommArea = new EventCommArea();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfoOld.getProductObjectCode());
			eventCommAreaNonFinance.setProductForm(coreMediaBasicInfoOld.getProductForm());
			String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
			if (activationFlag.equals(ACTIVATION_3)) {
				return eventCommAreaNonFinance;
			}
			coreMediaBasicInfoOld.setActivationFlag("1");
			if (activationFlag.equals(ACTIVATION_1)) {
				coreMediaBasicInfoOld.setActivationMode(ACTIVATION_1);
			} else if (activationFlag.equals(ACTIVATION_2)) {
				coreMediaBasicInfoOld.setActivationMode(ACTIVATION_2);
			}
//			coreMediaBasicInfoOld.set
			coreMediaBasicInfoOld.setVersion(coreMediaBasicInfoOld.getVersion() + 1);
			int resultOld = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfoOld,
					coreMediaBasicInfoSqlBuilderOld);
			if (resultOld != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			Map<String, Object> triggerEventParamsOld = new HashMap<String, Object>();
			EventCommAreaNonFinance eventCommAreaNonFinanceOld = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceOld.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceOld.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoOld,eventCommAreaNonFinanceOld);
			triggerEventParamsOld.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceOld);
			List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
			eventCommAreaTriggerEventList.add(triggerEventParamsOld);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		}else if (invalidReason.equals(InvalidReasonStatus.BRK.getValue())) {
			
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderOld = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderOld.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderOld.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfoOld = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderOld);
			
			EventCommArea eventCommArea = new EventCommArea();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfoOld.getProductObjectCode());
			eventCommAreaNonFinance.setProductForm(coreMediaBasicInfoOld.getProductForm());
			String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
			if (activationFlag.equals(ACTIVATION_3)) {
				return eventCommAreaNonFinance;
			}
			coreMediaBasicInfoOld.setInvalidReason(InvalidReasonStatus.CLS.getValue());
			coreMediaBasicInfoOld.setInvalidFlag("N");
			coreMediaBasicInfoOld.setVersion(coreMediaBasicInfoOld.getVersion() + 1);
			int resultOld = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfoOld,
					coreMediaBasicInfoSqlBuilderOld);
			if (resultOld != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			Map<String, Object> triggerEventParamsOld = new HashMap<String, Object>();
			EventCommAreaNonFinance eventCommAreaNonFinanceOld = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceOld.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceOld.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoOld,eventCommAreaNonFinanceOld);
			triggerEventParamsOld.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceOld);
			List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
			eventCommAreaTriggerEventList.add(triggerEventParamsOld);
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.andMediaUnitCodeEqualTo(x5065bo.getMediaUnitCode());
			coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.PNA.getValue());
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,coreMediaBasicInfoAfter);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
			// 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活',
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			String operateDate = coreSystemUnit.getNextProcessDate();
			coreMediaBasicInfo.setInvalidFlag("N");//毁损新卡有效改成无效
			coreMediaBasicInfo.setInvalidReason("");
			coreMediaBasicInfo.setActivationFlag("1");
			coreMediaBasicInfo.setActivationMode(activationFlag);
			coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
			int result = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
					coreMediaBasicInfoSqlBuilderStr);
			if (result != 1) {
				throw new BusinessException("CUS-00012", "媒介单元");
			}
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();

			EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
			eventCommAreaNonFinanceNew.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
			eventCommAreaNonFinanceNew.setAuthDataSynFlag("1");
			eventCommAreaNonFinanceNew.setInvalidReason("");
			eventCommAreaNonFinanceNew.setInvalidReasonOld("");
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinanceNew);
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			// 当前操作员
			if (operatorId == null) {
				operatorId = "system";
			}
			nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
					ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
					coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
					coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
			// 第一次传递参数
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
			eventCommAreaNonFinance.setAuthDataSynFlag("1");
			eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
			/**
			 * 需要将新卡有效期标识修改，然后修改测试结果
			 */
			coreMediaBasicInfoSqlBuilderStr.clear();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.andActivationFlagEqualTo("3");
			coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.RNA.getValue());
			List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		}else if (invalidReason.equals(InvalidReasonStatus.CHP.getValue())) {
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderOld = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilderOld.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            coreMediaBasicInfoSqlBuilderOld.andInvalidFlagEqualTo("Y");
            CoreMediaBasicInfo coreMediaBasicInfoOld = coreMediaBasicInfoDao
                    .selectBySqlBuilder(coreMediaBasicInfoSqlBuilderOld);
            EventCommArea eventCommArea = new EventCommArea();
            eventCommAreaNonFinance.setEventCommArea(eventCommArea);
            eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfoOld.getProductObjectCode());
            eventCommAreaNonFinance.setProductForm(coreMediaBasicInfoOld.getProductForm());
            String activationFlag = yesOrNoActivated(eventCommAreaNonFinance, artifactList);
            if (activationFlag.equals(ACTIVATION_3)) {
                return eventCommAreaNonFinance;
            }
            coreMediaBasicInfoOld.setInvalidReason(InvalidReasonStatus.CLS.getValue());
            coreMediaBasicInfoOld.setInvalidFlag("N");
            coreMediaBasicInfoOld.setVersion(coreMediaBasicInfoOld.getVersion() + 1);
            int resultOld = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfoOld,
                    coreMediaBasicInfoSqlBuilderOld);
            if (resultOld != 1) {
                throw new BusinessException("CUS-00012", "媒介单元");
            }
            Map<String, Object> triggerEventParamsOld = new HashMap<String, Object>();
            EventCommAreaNonFinance eventCommAreaNonFinanceOld = new EventCommAreaNonFinance();
            eventCommAreaNonFinanceOld.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
            eventCommAreaNonFinanceOld.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoOld,eventCommAreaNonFinanceOld);
            triggerEventParamsOld.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceOld);
            List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
            eventCommAreaTriggerEventList.add(triggerEventParamsOld);
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
            coreMediaBasicInfoSqlBuilderStr.andMediaUnitCodeEqualTo(x5065bo.getMediaUnitCode());
            coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.CHP.getValue());
            CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
                    .selectBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
            CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,coreMediaBasicInfoAfter);
            if (null == coreMediaBasicInfo) {
                throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
            }
            // 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活',
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
            String operateDate = coreSystemUnit.getNextProcessDate();
            coreMediaBasicInfo.setInvalidFlag("Y");
            coreMediaBasicInfo.setInvalidReason("");
            coreMediaBasicInfo.setActivationFlag("1");
            coreMediaBasicInfo.setActivationMode(activationFlag);
            coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
            int result = coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
                    coreMediaBasicInfoSqlBuilderStr);
            if (result != 1) {
                throw new BusinessException("CUS-00012", "媒介单元");
            }
            Map<String, Object> triggerEventParams = new HashMap<String, Object>();

            EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
            eventCommAreaNonFinanceNew.setBusinessProgramNo(x5065bo.getBusinessProgramNo());
            eventCommAreaNonFinanceNew.setAuthDataSynFlag("1");
            eventCommAreaNonFinanceNew.setInvalidReason("");
            eventCommAreaNonFinanceNew.setInvalidReasonOld("");
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinanceNew);
            triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinanceNew);
            eventCommAreaTriggerEventList.add(triggerEventParams);
            // 当前操作员
            if (operatorId == null) {
                operatorId = "system";
            }
            nonFinancialLogUtil.createNonFinancialActivityLog(x5065bo.getEventNo(), x5065bo.getActivityNo(),
                    ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
                    coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
                    coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
            // 第一次传递参数
			CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
            eventCommAreaNonFinance.setAuthDataSynFlag("1");
            eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
            /**
             * 需要将新卡有效期标识修改，然后修改测试结果
             */
            coreMediaBasicInfoSqlBuilderStr.clear();
            coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("N");
            coreMediaBasicInfoSqlBuilderStr.andActivationFlagEqualTo("3");
            coreMediaBasicInfoSqlBuilderStr.andInvalidReasonEqualTo(InvalidReasonStatus.CHP.getValue());
            List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDao
                    .selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
            eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
        }
		return eventCommAreaNonFinance;
	}

	/**
	 * @Description: 判断当前内容是否需要激活操作
	 * @param eventCommAreaNonFinance
	 * @param artifactList
	 * @return boolean 是否需要激活,操作。
	 * @throws Exception
	 */
	private String yesOrNoActivated(EventCommAreaNonFinance eventCommAreaNonFinance,
			List<CoreActivityArtifactRel> artifactList) throws Exception {
		checkMainProductForm(eventCommAreaNonFinance.getProductForm());
		if (eventCommAreaNonFinance.getEventNo().equals(EVENTNO_BSSAD010003)
				|| eventCommAreaNonFinance.getEventNo().equals(EVENTNO_ISSOP010014)
				|| eventCommAreaNonFinance.getEventNo().equals(EVENTNO_ISSOP010003)
				|| eventCommAreaNonFinance.getEventNo().equals(EVENTNO_ISSOP010004)
				|| eventCommAreaNonFinance.getEventNo().equals(EVENTNO_BSSAD019001)
				|| eventCommAreaNonFinance.getEventNo().equals(EVENTNO_ISSOP010005)) {
			Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_405, artifactList);
			if (!checkResult) {
				throw new BusinessException("COR-10002");
			}
			eventCommAreaNonFinance.getEventCommArea()
					.setEcommProdObjId(eventCommAreaNonFinance.getProductObjectCode());
			eventCommAreaNonFinance.getEventCommArea().setEcommOperMode(eventCommAreaNonFinance.getOperationMode());
			eventCommAreaNonFinance.getEventCommArea().setEcommCustId(eventCommAreaNonFinance.getCustomerNo());
			eventCommAreaNonFinance.getEventCommArea().setEcommProcessingDate(eventCommAreaNonFinance.getOperationDate());
			CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
			Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_405,
					eventCommAreaNonFinance.getEventCommArea());
			Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (Constant.NOACTIVATION.equals(entry.getKey())) {
					// 不执行激活操作
					return ACTIVATION_3;
				} else if (Constant.EXECUTION_ACTIVATION.equals(entry.getKey())) {
					// 执行激活操作
					return ACTIVATION_2;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
				}
			}
		} else {
			return ACTIVATION_1;
		}

		return "";
	}

	/**
	 * 
	 * @Description:
	 * @param eventCommArea
	 * @param coreAccount
	 * @param interestAmount
	 * @param interestPostingBalanceObject
	 * @param paramList
	 * @throws Exception
	 */
	private void triggerEvent(EventCommAreaNonFinance eventCommAreaNonFinance, List<Map<String, Object>> paramList)
			throws Exception {
		Map<String, Object> paramsMap = new HashMap<String, Object>(1);
		paramsMap.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinance);
		paramList.add(paramsMap);
	}
	
	public void checkMainProductForm(String productForm) throws Exception{
		//DPAN激活：需检查主产品形式的“有效”媒介的激活标识必须为“1-已激活”;
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
        coreProductFormSqlBuilder.andProductFormEqualTo(productForm);
        CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
		if(coreProductForm!=null){
		    if(coreProductForm.getMediaObjectForm().equals("T")){
		        String mainProductForm=coreProductForm.getMainProductForm();
		        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
		        coreMediaBasicInfoSqlBuilder1.andProductFormEqualTo(mainProductForm);
		        //coreMediaBasicInfoSqlBuilder1.andInvalidFlagEqualTo("Y");
		        CoreMediaBasicInfo coreMediaBasicInfo1 = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder1);
		        if(coreMediaBasicInfo1!=null){
		            if(!coreMediaBasicInfo1.getActivationFlag().equals("1")){
		                throw new BusinessException("CUS-00143");
		            }
		        }
		    }
		}
	}

}
