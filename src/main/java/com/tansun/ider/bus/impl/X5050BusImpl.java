package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5050Bus;
import com.tansun.ider.dao.beta.CoreArtifactInstanRelDao;
import com.tansun.ider.dao.beta.entity.AuthCipherKey;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreArtifactInstanRel;
import com.tansun.ider.dao.beta.entity.CoreCardPool;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSpecialCard;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.beta.sqlbuilder.CoreOperationModeSqlBuilder;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerNumberRuleDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBindDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerNumberRule;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerNumberRuleSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.EncryptionStatus;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.MediaObjectType;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.GenerateCardNumBean;
import com.tansun.ider.model.ResultGns;
import com.tansun.ider.model.bo.X5050BO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.ManagerCardNumService;
import com.tansun.ider.service.PciService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CacheUtils;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.OperationModeUtil;
import com.tansun.ider.util.SecurityUtil;

/**
 * @version:1.0
 * @Description: 媒介单元基本信息新建
 * @author: wangtie
 */

@Service
public class X5050BusImpl implements X5050Bus {

	private static Logger logger = LoggerFactory.getLogger(X5050BusImpl.class);

	static final String ApplicationCardType_1 = "1";  //单申主卡
	static final String ApplicationCardType_2 = "2";  //单申附卡
	static final String ApplicationCardType_3 = "3";  //主附同申-主卡
	static final String ApplicationCardType_4 = "4";  //主附同申-附卡
	
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreCustomerNumberRuleDao coreCustomerNumberRuleDao;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreCustomerBillDayDao coreCustomerBillDayDao;
	@Resource
	private CoreProductDao coreProductDao;
	@Resource
	private CoreMediaBindDao coreMediaBindDao;
	@Autowired
	public OperationModeUtil operationModeUtil;
	@Autowired
	private CoreProductFormDao coreProductFormDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CardUtil cardUtil;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Autowired
	private PciService pciService;
	@Autowired
	private ManagerCardNumService managerCardNumService;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreArtifactInstanRelDao coreArtifactInstanRelDao;
	
	private static final String BSS_AD_01_0003 = "BSS.AD.01.0003";//媒介资料建立
	
	public final static String ELEMENT_NO_T04AAA0100 = "T04AAA0100";
	public final static String ARTIFACTNO_T04 = "T04";
	public final static String OPERATION_MODE = "A01";
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object busExecute(X5050BO x5050bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5050bo, eventCommAreaNonFinance);
		String businessProgramNo = x5050bo.getBusinessProgramNo();
		String productForm = x5050bo.getProductForm();
		String operatorId = x5050bo.getOperatorId();
		String customerNo = eventCommAreaNonFinance.getMainCustomerNo();
		String IdNumber = eventCommAreaNonFinance.getIdNumber();
		String idType = eventCommAreaNonFinance.getIdType();
		eventCommAreaNonFinance.setCustomerNo(customerNo);
		String productObjectCode = eventCommAreaNonFinance.getProductObjectCode();
		// 失效原因字段
		String invalidReason = eventCommAreaNonFinance.getInvalidReason();
		
		/**
		 * 增加校验：媒介资料建立事件，申请附卡时，主副客户号不能相同
		 * add by wangxi 2019/7/11 cyy提
		 */
		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5050bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		//BSS_AD_01_0003事件	媒介资料建立事件，申请附卡时，主副客户号不能相同
		if (BSS_AD_01_0003.equals(eventNo)){
			String subCustomerNo = x5050bo.getSubCustomerNo();
			if(StringUtil.isNotBlank(subCustomerNo)){
				if(subCustomerNo.equals(customerNo)){
					throw new BusinessException("CUS-00131");//主副客户代码一致，请检查！
				}
			}
		}
		
		// 主附标识 1：主卡 2：附属卡
		String mainSupplyIndicator = eventCommAreaNonFinance.getMainSupplyIndicator();
		// 产品账单日，产品线构件编号
		List<CoreActivityArtifactRel> artifactList = x5050bo.getActivityArtifactList();
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		// 如果是公务卡，获取预算单位编码
		String budgetOrgCode = getBudgetOrgCode(customerNo, productObjectCode);
		if (StringUtil.isNotBlank(budgetOrgCode)) {
			eventCommAreaNonFinance.setBudgetOrgCode(budgetOrgCode);
		}
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(IdNumber)) {
			coreCustomerSqlBuilder.andIdNumberEqualTo(IdNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
		} else {
			throw new BusinessException("COR-10048");
		}
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if (null == coreCustomer) {
			throw new BusinessException("CUS-00014", "客户基本信息");
		}
		if (operatorId == null) {
			operatorId = "system";
		}
		CoreCorporationEntity coreCorporationEntity = cardUtil
				.getSystemUnitNoCoreCorporationEntity(coreCustomer.getInstitutionId());
		eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
		eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
		eventCommAreaNonFinance.setCorporationEntityNo(coreCorporationEntity.getCorporationEntityNo());
		eventCommAreaNonFinance.setCorporation(coreCorporationEntity.getCorporationEntityNo());
		eventCommAreaNonFinance.setCustomerNo(coreCustomer.getCustomerNo());
		AuthCipherKey authCipherKey = pciService.queryAuthCipherKey("PCI", coreCustomer.getOperationMode());
		
//		CoreArtifactInstanRelSqlBuilder coreArtifactInstanRelSqlBuilder = new CoreArtifactInstanRelSqlBuilder();
//		coreArtifactInstanRelSqlBuilder.andOperationModeEqualTo(OPERATION_MODE);
//		coreArtifactInstanRelSqlBuilder.andArtifactNoEqualTo(ARTIFACTNO_T04);
//		coreArtifactInstanRelSqlBuilder.andElementNoEqualTo(ELEMENT_NO_T04AAA0100);
//		CoreArtifactInstanRel coreArtifactInstanRel = coreArtifactInstanRelDao.selectBySqlBuilder(coreArtifactInstanRelSqlBuilder);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constant.PARAM_OPER_MODE, OPERATION_MODE);
		params.put("artifactNo", ARTIFACTNO_T04);
		params.put("elementNo", ELEMENT_NO_T04AAA0100);
		params.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		List<CoreArtifactInstanRel>  coreArtifactInstanRelList  = betaCommonParamService.queryArtifactInstanRel(params);
		
		// 新增
		if (null == invalidReason) {
			Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
			eventCommAreaNonFinance.setOperationMode(coreCustomer.getOperationMode());
			// 数据校验
			Map<String, String> mapStr = checkData(eventCommAreaNonFinance);
			eventCommAreaNonFinance.setServiceCode(mapStr.get("serviceCode").toString());
			// 000010001 机构号
			CoreMediaBasicInfo coreMediaBasicInfo = new CoreMediaBasicInfo();
			ReflexUtil.setFieldsValues(coreMediaBasicInfo, map);
			
			if (StringUtil.isNotBlank(x5050bo.getApplicationCardType())) {
				if (ApplicationCardType_2.equals(x5050bo.getApplicationCardType()) || ApplicationCardType_4.equals(x5050bo.getApplicationCardType())) {
					if (StringUtil.isNotBlank(x5050bo.getSupIdType()) && StringUtil.isNotBlank(x5050bo.getSupIdNumber())) {
						CoreCustomer coreCustomerObj = (CoreCustomer)queryCustomerService.queryCustomer(x5050bo.getSupIdType(), x5050bo.getSupIdNumber(), null);
						if (null != coreCustomerObj) {
							coreMediaBasicInfo.setMainCustomerNo(coreCustomerObj.getCustomerNo());
							eventCommAreaNonFinance.setSubCustomerNo(coreCustomerObj.getCustomerNo());
							eventCommAreaNonFinance.setSupplyCustomerNo(coreCustomerObj.getCustomerNo());
						}else {
							throw new BusinessException("CUS-00121");
						}
					}else {
						throw new BusinessException("CUS-00120");
					}
				}
			}
			coreMediaBasicInfo.setId(RandomUtil.getUUID());
			//开附属卡
			String mediaUnitCode = "";
			if (StringUtil.isNotBlank(eventCommAreaNonFinance.getApplicationCardType())) {
				if (ApplicationCardType_2.equals(eventCommAreaNonFinance.getApplicationCardType()) ||
						ApplicationCardType_4.equals(eventCommAreaNonFinance.getApplicationCardType())) {
					if (StringUtil.isNotBlank(eventCommAreaNonFinance.getSupIdType()) && 
							StringUtil.isNotBlank(eventCommAreaNonFinance.getSupIdNumber())) {
						CoreCustomer coreCustomerObj = (CoreCustomer)queryCustomerService.queryCustomer(eventCommAreaNonFinance.getSupIdType(),
								eventCommAreaNonFinance.getSupIdNumber(), null);
						coreMediaBasicInfo.setMainCustomerNo(coreCustomerObj.getCustomerNo());
						mediaUnitCode = getCoreCustomerNumberRule(coreCustomerObj.getCustomerNo());
					}
				}else {
					//开主卡
					coreMediaBasicInfo.setMainCustomerNo(eventCommAreaNonFinance.getCustomerNo());
					mediaUnitCode = getCoreCustomerNumberRule(eventCommAreaNonFinance.getCustomerNo());
				}
			}else {
				coreMediaBasicInfo.setMainCustomerNo(eventCommAreaNonFinance.getCustomerNo());
				mediaUnitCode = getCoreCustomerNumberRule(eventCommAreaNonFinance.getCustomerNo());
			}
			coreMediaBasicInfo.setMediaUnitCode(mediaUnitCode);

			CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreCustomer.getOperationMode(),
					eventCommAreaNonFinance.getProductObjectCode());
			
			String binNo = coreProductObject.getBinNo().toString();
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnitForBinNo(binNo, coreCorporationEntity,
					operatorId);
			String operationDate = "";
			if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
				operationDate = coreSystemUnit.getCurrProcessDate();
			} else {
				operationDate = coreSystemUnit.getNextProcessDate();
			}
			String externalIdentificationNoIn = x5050bo.getExternalIdentificationNoIn();
			// 运营模式
			String operationMode = coreCustomer.getOperationMode();
			EventCommArea eventCommArea_424 = new EventCommArea();
			eventCommArea_424.setEcommProdObjId(productObjectCode);
			eventCommArea_424.setEcommOperMode(operationMode);
			// 判断是否自动生成配对号
			if (StringUtil.isNotBlank(externalIdentificationNoIn)) {
				String externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424,
						externalIdentificationNoIn, binNo, artifactList, operationDate,operatorId,coreCorporationEntity.getCorporationEntityNo());
				if (StringUtil.isNotBlank(coreSystemUnit.getPanEncryptFlag())
						&& coreSystemUnit.getPanEncryptFlag().equals(YesOrNo.YES.getValue())) {
					String pwdKey = authCipherKey.getKeySingle();
					String externalIdentificationNoPwd = SecurityUtil.pwdTrans(pwdKey, EncryptionStatus.E.getValue(), "1",
							externalIdentificationNo, null);
					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNoPwd);
					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNoPwd);
				} else {
					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNo);
					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
				}
			}else if (null != coreArtifactInstanRelList && !coreArtifactInstanRelList.isEmpty()){
				//查询预留卡号表
				CoreCardPool coreCardPool = httpQueryService.queryCoreCardPool(binNo, coreCustomer.getCorporationEntityNo());
				if (null != coreCardPool ) {
					if (StringUtil.isNotBlank(coreCardPool.getCardNumber())) {
						coreMediaBasicInfo.setExternalIdentificationNo(coreCardPool.getCardNumber());
						eventCommAreaNonFinance.setExternalIdentificationNo(coreCardPool.getCardNumber());
					}else {
						throw new BusinessException("CUS-00151");
					}
//					httpQueryService.updateCoreCardPool(binNo, coreCustomer.getCorporationEntityNo(), coreCardPool.getCardNumber());
				}else {
					throw new BusinessException("CUS-00150");
				}
			}else {
				String externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424,
						externalIdentificationNoIn, binNo, artifactList, operationDate,operatorId,coreCorporationEntity.getCorporationEntityNo());
				if (StringUtil.isNotBlank(coreSystemUnit.getPanEncryptFlag())
						&& coreSystemUnit.getPanEncryptFlag().equals(YesOrNo.YES.getValue())) {
					String pwdKey = authCipherKey.getKeySingle();
					String externalIdentificationNoPwd = SecurityUtil.pwdTrans(pwdKey, EncryptionStatus.E.getValue(), "1",
							externalIdentificationNo, null);
					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNoPwd);
					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNoPwd);
				} else {
					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNo);
					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
				}
			}
//			if (null == coreArtifactInstanRelList && coreArtifactInstanRelList.isEmpty() && StringUtil.isNotBlank(externalIdentificationNoIn) ) {
//				String externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424,
//						externalIdentificationNoIn, binNo, artifactList, operationDate,operatorId,coreCorporationEntity.getCorporationEntityNo());
//				if (StringUtil.isNotBlank(coreSystemUnit.getPanEncryptFlag())
//						&& coreSystemUnit.getPanEncryptFlag().equals(YesOrNo.YES.getValue())) {
//					String pwdKey = authCipherKey.getKeySingle();
//					String externalIdentificationNoPwd = SecurityUtil.pwdTrans(pwdKey, EncryptionStatus.E.getValue(), "1",
//							externalIdentificationNo, null);
//					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNoPwd);
//					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNoPwd);
//				} else {
//					coreMediaBasicInfo.setExternalIdentificationNo(externalIdentificationNo);
//					eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
//				}
//			}else {
//				
//			}
			coreMediaBasicInfo.setOperationMode(operationMode);
			// 当前营业日期
			CoreOperationModeSqlBuilder coreOperationModeSqlBuilder = new CoreOperationModeSqlBuilder();
			coreOperationModeSqlBuilder.andOperationModeEqualTo(operationMode);
			coreMediaBasicInfo.setCreateDate(operationDate);
			// 是否有效标识
			coreMediaBasicInfo.setInvalidFlag("Y");
			// 查询产品形式代码
			productForm = queryProductForm(coreCustomer.getCustomerNo(), productObjectCode,
					(String) mapStr.get("mediaObjectForm"), eventCommAreaNonFinance.getSubCustomerNo(),
					eventCommAreaNonFinance.getMainSupplyIndicator());
			coreMediaBasicInfo.setProductForm(productForm);
			coreMediaBasicInfo.setVersion(1);
			// 获取有效期
			EventCommArea eventCommArea = new EventCommArea();
			eventCommArea.setEcommOperMode(operationMode);
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setOperationDate(operationDate);
			// 获取有效期
			String expirationDate = x5050bo.getExpirationDate();
            if(StringUtil.isEmpty(expirationDate)){
             expirationDate = getExpirationDate(eventCommAreaNonFinance, artifactList);
            }
			coreMediaBasicInfo.setExpirationDate(expirationDate);
			String mediaObjectType = (String) map.get("mediaObjectType");
			// 实体卡上送2 虚拟卡上送5
			if (MediaObjectType.V.getValue().equals(mediaObjectType)) {
				// 虚拟卡无须激活
				coreMediaBasicInfo.setActivationFlag("5");
				eventCommAreaNonFinance.setActivationFlag("5");
			} else {
				coreMediaBasicInfo.setActivationFlag("2");
				eventCommAreaNonFinance.setActivationFlag("2");
			}
			coreMediaBasicInfo.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
			@SuppressWarnings("unused")
			int result = coreMediaBasicInfoDao.insert(coreMediaBasicInfo);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaBasicInfo, coreMediaBasicInfo.getId(),
					coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfo.getMainCustomerNo(),
					coreMediaBasicInfo.getMediaUnitCode(), null, null);
			List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
					.queryProductBusinessScope(productObjectCode, eventCommAreaNonFinance.getOperationMode());
			CoreProductBusinessScope coreProductBusinessScope = null;
			if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
				coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
			} else {
				throw new BusinessException("CUS-00064");
			}
			if (coreProductBusinessScope != null) {
				businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
				eventCommAreaNonFinance.setBusinessProgramNo(coreProductBusinessScope.getBusinessProgramNo());
			}
			eventCommAreaNonFinance.setMediaObjectCode(coreMediaBasicInfo.getMediaObjectCode());
			eventCommAreaNonFinance.setExpirationDate(expirationDate);
			eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
			eventCommAreaNonFinance.setOperationDate(operationDate);
			eventCommAreaNonFinance.setOperationMode(operationMode);
			// country 所属国家
			eventCommAreaNonFinance.setCountry(mapStr.get("country"));
			// 媒介对象类型
			eventCommAreaNonFinance.setMediaObjectType(mapStr.get("mediaObjectType"));
			// 激活状态
			eventCommAreaNonFinance.setActivationFlag(coreMediaBasicInfo.getActivationFlag());
			eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfo.getInvalidFlag());
			eventCommAreaNonFinance.setInvalidReason(null);
			eventCommAreaNonFinance.setInvalidReasonOld(null);
			eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
			List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			List<ResultGns> coreMediaBasicInfoList1 = new ArrayList<>();
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
				ResultGns resultGns = new ResultGns();
				resultGns.setExternalIdentificationNo(coreMediaBasicInfo2.getExternalIdentificationNo());
				coreMediaBasicInfoList1.add(resultGns);
			}
			eventCommAreaNonFinance.setCoreMediaBasicInfoList(coreMediaBasicInfoList1);
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
			eventCommAreaNonFinance.setMainCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
			/** 新增媒介同步授权   */ 
			eventCommAreaNonFinance.setWhetherProcess("");
			List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();
			AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
			authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
			authEventCommAreaNonFinanceBean.setProductForm(coreMediaBasicInfo.getProductForm());
			authEventCommAreaNonFinanceBean.setExpirationDate(expirationDate);
			authEventCommAreaNonFinanceBean.setSystemUnitNo(coreCustomer.getSystemUnitNo());
			authEventCommAreaNonFinanceBean.setSupplyCustomerNo(eventCommAreaNonFinance.getSubCustomerNo());
			//给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
			if(mainSupplyIndicator.equals("2")){//add qianyp 申请附属卡给授权主产品形式
				CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderTemp = new CoreMediaBasicInfoSqlBuilder();
				coreMediaBasicInfoSqlBuilderTemp.andMainCustomerNoEqualTo(customerNo);
				coreMediaBasicInfoSqlBuilderTemp.andProductObjectCodeEqualTo(eventCommAreaNonFinance.getProductObjectCode());
				coreMediaBasicInfoSqlBuilderTemp.andMainSupplyIndicatorEqualTo("1");
				List<CoreMediaBasicInfo> coreMediaBasicInfos = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilderTemp);
				if(coreMediaBasicInfos!=null && coreMediaBasicInfos.size()>0){
					authEventCommAreaNonFinanceBean.setMainProductForm(coreMediaBasicInfos.get(0).getProductForm());
				}
			}
			CachedBeanCopy.copyProperties(coreCustomer, authEventCommAreaNonFinanceBean);
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		} else if (InvalidReasonStatus.TRF.getValue().equals(invalidReason)) {
			// 转卡
			// 生成外部识别号，生成媒介单元代码，生成新有效期
			// 运营日期
			CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreCustomer.getOperationMode(),
					eventCommAreaNonFinance.getProductObjectCode());
			String binNo = coreProductObject.getBinNo().toString();
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnitForBinNo(binNo, coreCorporationEntity,
					operatorId);
			String operationDate = "";
			if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
				operationDate = coreSystemUnit.getCurrProcessDate();
			} else {
				operationDate = coreSystemUnit.getNextProcessDate();
			}
			String externalIdentificationNoIn = x5050bo.getExternalIdentificationNoIn();
			// 运营模式
			String operationMode = coreCustomer.getOperationMode();
			EventCommArea eventCommArea_424 = new EventCommArea();
			eventCommArea_424.setEcommProdObjId(productObjectCode);
			eventCommArea_424.setEcommOperMode(operationMode);
			// 判断是否自动生成配对号
			String externalIdentificationNo = "";
//			if (null == coreArtifactInstanRelList && coreArtifactInstanRelList.isEmpty()) {
		    if (StringUtil.isNotBlank(externalIdentificationNoIn)) {
				externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424,
						externalIdentificationNoIn, binNo, artifactList, operationDate,operatorId,coreCorporationEntity.getCorporationEntityNo());
			}else if(null != coreArtifactInstanRelList && !coreArtifactInstanRelList.isEmpty()){
				//查询预留卡号表
				CoreCardPool coreCardPool = httpQueryService.queryCoreCardPool(binNo, coreCustomer.getCorporationEntityNo());
				if (null != coreCardPool) {
					externalIdentificationNo = coreCardPool.getCardNumber();
				}else {
					throw new BusinessException("CUS-00150");
				}
			}else {
				externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424,
						externalIdentificationNoIn, binNo, artifactList, operationDate,operatorId,coreCorporationEntity.getCorporationEntityNo());
			}
			// 有效期
			EventCommArea eventCommArea = new EventCommArea();
			eventCommArea.setEcommOperMode(eventCommAreaNonFinance.getOperationMode());
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setOperationDate(operationDate);
			// 有效期
            String expirationDate="";
            //紧急替代卡，以输入新的有效期
            String newExpirationDate = x5050bo.getNewExpirationDate();
            if(StringUtil.isNotBlank(newExpirationDate)){
            	//检查输入的有效期MMYY必须大于系统单元下一处理日月份MMYY
            	String nextProcessDate = coreSystemUnit.getNextProcessDate();
            	Double result = DateUtil.daysBetween(nextProcessDate, newExpirationDate,"yyyy-MM");
            	if(result<=0){
            		throw new BusinessException("CUS-00145");
            	}
            	expirationDate = newExpirationDate.split("-")[1]+newExpirationDate.split("-")[0].substring(2,4);
            } else {
            	expirationDate = getExpirationDate(eventCommAreaNonFinance, artifactList);
            }
			CoreMediaBasicInfo coreMediaBasicInfoStr = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreMediaBasicInfoStr);
			CachedBeanCopy.copyProperties(coreMediaBasicInfoStr, x5050bo);
			// 媒介单元代码
			String mediaUnitCode = getCoreCustomerNumberRule(customerNo);
			coreMediaBasicInfoStr.setProductForm(x5050bo.getProductForm());
			coreMediaBasicInfoStr.setMediaUnitCode(mediaUnitCode);
			coreMediaBasicInfoStr.setId(RandomUtil.getUUID());
			coreMediaBasicInfoStr.setExpirationDate(expirationDate);
			if (StringUtil.isNotBlank(coreSystemUnit.getPanEncryptFlag())
					&& coreSystemUnit.getPanEncryptFlag().equals(YesOrNo.YES.getValue())) {
				String pwdKey = authCipherKey.getKeySingle();
				String externalIdentificationNoPwd = SecurityUtil.pwdTrans(pwdKey, EncryptionStatus.E.getValue(), "1",
						externalIdentificationNo, null);
				coreMediaBasicInfoStr.setExternalIdentificationNo(externalIdentificationNoPwd);
				eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNoPwd);
			} else {
				coreMediaBasicInfoStr.setExternalIdentificationNo(externalIdentificationNo);
				eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
			}
			coreMediaBasicInfoStr.setActivationFlag("4");
			eventCommAreaNonFinance.setActivationFlag("4");
			coreMediaBasicInfoStr.setInvalidFlag("Y");
			eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfoStr.getInvalidFlag());
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoStr.getOperationMode());
			coreMediaBasicInfoStr.setVersion(1);
			coreMediaBasicInfoStr.setCreateDate(operationDate);
			coreMediaBasicInfoStr.setTimeStamp(null);
			coreMediaBasicInfoStr.setInvalidReason(null);
			coreMediaBasicInfoStr.setActivationDate(null);
			coreMediaBasicInfoStr.setMediaObjectCode(x5050bo.getMediaObjectCode());
			coreMediaBasicInfoStr.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
			@SuppressWarnings("unused")
			int result = coreMediaBasicInfoDao.insert(coreMediaBasicInfoStr);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaBasicInfoStr, coreMediaBasicInfoStr.getId(),
					coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
					coreMediaBasicInfoStr.getMediaUnitCode(), null, null);
			List<CoreMediaBind> listCoreMediaBinds = eventCommAreaNonFinance.getListCoreMediaBinds();
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					coreMediaBind.setMediaUnitCode(mediaUnitCode);
					coreMediaBind.setId(RandomUtil.getUUID());
					coreMediaBind.setVersion(1);
				}
				@SuppressWarnings("unused")
				int resultMediaBinds = coreMediaBindDao.insertUseBatch(listCoreMediaBinds);
			}
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
							ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
							coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
							coreMediaBind.getMediaUnitCode(), null, null);
				}
			}
			eventCommAreaNonFinance.setExpirationDate(expirationDate);
			eventCommAreaNonFinance.setMediaObjectCode(x5050bo.getMediaObjectCode());
			eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
			List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
					.queryProductBusinessScope(productObjectCode, eventCommAreaNonFinance.getOperationMode());
			CoreProductBusinessScope coreProductBusinessScope = null;
			if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
				coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
			} else {
				throw new BusinessException("CUS-00064");
			}
			businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoStr.getOperationMode());
			// country 所属国家
			eventCommAreaNonFinance.setCountry(x5050bo.getCountry());
			// 媒介对象类型
			eventCommAreaNonFinance.setMediaObjectType(x5050bo.getMediaObjectType());
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.TRF.getValue());
			eventCommAreaNonFinance.setInvalidReasonOld(null);
			eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
			eventCommAreaNonFinance.setSystemUnitNo(coreSystemUnit.getSystemUnitNo());
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoStr.getOperationMode());
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
//			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
			List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			List<ResultGns> coreMediaBasicInfoList1 = new ArrayList<>();
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
				ResultGns resultGns = new ResultGns();
				resultGns.setExternalIdentificationNo(coreMediaBasicInfo2.getExternalIdentificationNo());
				coreMediaBasicInfoList1.add(resultGns);
			}
			eventCommAreaNonFinance.setCoreMediaBasicInfoList(coreMediaBasicInfoList1);
			
			/**
			 * 副客户号取产品形式表里的media_holde_no
			 * add by wangxi 2019/7/3  cyy提
			 */
//			String productObjectCode = eventCommAreaNonFinance.getProductObjectCode();
			CoreProductForm coreProductForm = null;
			CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
			coreProductFormSqlBuilder.andProductHolderNoEqualTo(customerNo);
			coreProductFormSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
			coreProductFormSqlBuilder.andProductFormEqualTo(productForm);
			coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
			eventCommAreaNonFinance.setSubCustomerNo(coreProductForm.getMediaHolderNo());
			eventCommAreaNonFinance.setSupplyCustomerNo(coreProductForm.getMediaHolderNo());
			/** 转卡媒介同步授权   */ 
			eventCommAreaNonFinance.setWhetherProcess("");
			List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();
			AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
			authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoStr, authEventCommAreaNonFinanceBean);
			//给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		} else if (InvalidReasonStatus.EXP.getValue().equals(invalidReason)) {
			// 到期换卡
			// 不生成外部识别号，生成媒介单元代码，生成新有效期
			CoreSystemUnit coreSystemUnit = operationModeUtil.getcoreOperationMode(customerNo);
			String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
			// 有效期
			EventCommArea eventCommArea = new EventCommArea();
			eventCommArea.setEcommOperMode(eventCommAreaNonFinance.getOperationMode());
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			String operateDate = "";
			if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
				operateDate = coreSystemUnit.getCurrProcessDate();
			} else {
				operateDate = coreSystemUnit.getNextProcessDate();
			}
			eventCommAreaNonFinance.setOperationDate(operateDate);
			// 有效期
			String expirationDate = getExpirationDate(eventCommAreaNonFinance, artifactList);
			CoreMediaBasicInfo coreMediaBasicInfoStr = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreMediaBasicInfoStr);
			CachedBeanCopy.copyProperties(coreMediaBasicInfoStr, x5050bo);
			// 媒介单元代码
			String mediaUnitCode = getCoreCustomerNumberRule(customerNo);
			coreMediaBasicInfoStr.setProductForm(x5050bo.getProductForm());
			coreMediaBasicInfoStr.setMediaUnitCode(mediaUnitCode);
			coreMediaBasicInfoStr.setId(RandomUtil.getUUID());
			coreMediaBasicInfoStr.setExpirationDate(expirationDate);
			coreMediaBasicInfoStr.setActivationFlag("3");
			eventCommAreaNonFinance.setActivationFlag("3");
			coreMediaBasicInfoStr.setInvalidFlag("N");
			coreMediaBasicInfoStr.setInvalidReason(InvalidReasonStatus.RNA.getValue());
			eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfoStr.getInvalidFlag());
			coreMediaBasicInfoStr.setVersion(1);
			coreMediaBasicInfoStr.setCreateDate(operateDate);
			coreMediaBasicInfoStr.setTimeStamp(null);
			coreMediaBasicInfoStr.setActivationDate(null);
			// String externalIdentificationNoPwd =
			// SecurityUtil.pwdTrans(pwdKey, EncryptionStatus.E.getValue(), "1",
			// externalIdentificationNo, null);
			coreMediaBasicInfoStr.setExternalIdentificationNo(externalIdentificationNo);
			coreMediaBasicInfoStr.setMediaObjectCode(x5050bo.getMediaObjectCode());
			coreMediaBasicInfoStr.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
			@SuppressWarnings("unused")
			int result = coreMediaBasicInfoDao.insert(coreMediaBasicInfoStr);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaBasicInfoStr, coreMediaBasicInfoStr.getId(),
					coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
					coreMediaBasicInfoStr.getMediaUnitCode(), null, null);
			List<CoreMediaBind> listCoreMediaBinds = eventCommAreaNonFinance.getListCoreMediaBinds();
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					coreMediaBind.setMediaUnitCode(mediaUnitCode);
					coreMediaBind.setId(RandomUtil.getUUID());
					coreMediaBind.setVersion(1);
				}
				@SuppressWarnings("unused")
				int resultMediaBinds = coreMediaBindDao.insertUseBatch(listCoreMediaBinds);
			}
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
							ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
							coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
							coreMediaBind.getMediaUnitCode(), null, null);
				}
			}
			eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
			eventCommAreaNonFinance.setExpirationDate(expirationDate);
			eventCommAreaNonFinance.setMediaObjectCode(x5050bo.getMediaObjectCode());
			eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
			List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
					.queryProductBusinessScope(productObjectCode, eventCommAreaNonFinance.getOperationMode());
			CoreProductBusinessScope coreProductBusinessScope = null;
			if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
				coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
			} else {
				throw new BusinessException("CUS-00064");
			}
			businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoStr.getOperationMode());
			// country 所属国家
			eventCommAreaNonFinance.setCountry(x5050bo.getCountry());
			// 媒介对象类型
			eventCommAreaNonFinance.setMediaObjectType(x5050bo.getMediaObjectType());
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.EXP.getValue());
			eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.RNA.getValue());
			eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
		} else if (InvalidReasonStatus.BRK.getValue().equals(invalidReason)) {
			// 毁损补发
			// 生成新的媒介单元代码，沿用之前的外部识别号，新的有效期
			CoreMediaBasicInfo coreMediaBasicInfoBrk = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(x5050bo, coreMediaBasicInfoBrk);
			coreMediaBasicInfoBrk.setId(RandomUtil.getUUID());
			coreMediaBasicInfoBrk.setExpirationDate(eventCommAreaNonFinance.getExpirationDate());
			coreMediaBasicInfoBrk.setExternalIdentificationNo(eventCommAreaNonFinance.getExternalIdentificationNo());
			coreMediaBasicInfoBrk.setActivationFlag("3");
			eventCommAreaNonFinance.setActivationFlag("3");
			// 媒介单元代码
			String mediaUnitCode = getCoreCustomerNumberRule(eventCommAreaNonFinance.getCustomerNo());
			coreMediaBasicInfoBrk.setMediaUnitCode(mediaUnitCode);
			// 查询系统单元编号

			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			String operationDate = coreSystemUnit.getNextProcessDate();

			coreMediaBasicInfoBrk.setOperationMode(eventCommAreaNonFinance.getOperationMode());
			//毁损补发卡片状态修改为： 新卡无效，旧卡有效。       所以这里新开卡的是否有效标志改为无效
            coreMediaBasicInfoBrk.setInvalidFlag("N");
			coreMediaBasicInfoBrk.setInvalidReason(null);
			coreMediaBasicInfoBrk.setCreateDate(coreSystemUnit.getNextProcessDate());
			coreMediaBasicInfoBrk.setActivationDate(null);
			coreMediaBasicInfoBrk.setVersion(1);
			coreMediaBasicInfoBrk.setTimeStamp(null);
			// 获取有效期
			String expirationDate = computeExpirationDate(eventCommAreaNonFinance, coreMediaBasicInfoBrk, artifactList);
			eventCommAreaNonFinance.setExpirationDate(expirationDate);
			coreMediaBasicInfoBrk.setExpirationDate(expirationDate);
			coreMediaBasicInfoBrk.setCreateDate(operationDate);
			coreMediaBasicInfoBrk.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
			coreMediaBasicInfoDao.insert(coreMediaBasicInfoBrk);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaBasicInfoBrk, coreMediaBasicInfoBrk.getId(),
					coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoBrk.getMainCustomerNo(),
					coreMediaBasicInfoBrk.getMediaUnitCode(), null, null);
			// 媒介绑定
			List<CoreMediaBind> listCoreMediaBinds = eventCommAreaNonFinance.getListCoreMediaBinds();
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					coreMediaBind.setMediaUnitCode(mediaUnitCode);
					coreMediaBind.setId(RandomUtil.getUUID());
					coreMediaBind.setVersion(1);
				}
				@SuppressWarnings("unused")
				int resultMediaBinds = coreMediaBindDao.insertUseBatch(listCoreMediaBinds);
			}
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
							ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
							coreSystemUnit.getCurrLogFlag(), operatorId, coreMediaBasicInfoBrk.getMainCustomerNo(),
							coreMediaBind.getMediaUnitCode(), null, null);
				}
			}
			eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
			List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
					.queryProductBusinessScope(productObjectCode, eventCommAreaNonFinance.getOperationMode());
			CoreProductBusinessScope coreProductBusinessScope = null;
			if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
				coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
			} else {
				throw new BusinessException("CUS-00064");
			}
			businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoBrk.getOperationMode());
			// country 所属国家
			eventCommAreaNonFinance.setCountry(x5050bo.getCountry());
			// 媒介对象类型
			eventCommAreaNonFinance.setMediaObjectType(x5050bo.getMediaObjectType());
			eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfoBrk.getInvalidFlag());
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.BRK.getValue());
			eventCommAreaNonFinance.setInvalidReasonOld(null);
			eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
			/** 毁损补发媒介同步授权   */ 
			eventCommAreaNonFinance.setWhetherProcess("");
			List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();
			AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
			CachedBeanCopy.copyProperties(coreMediaBasicInfoBrk, authEventCommAreaNonFinanceBean);
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
			authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
			//给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		} else if (InvalidReasonStatus.PNA.getValue().equals(invalidReason)) {
			// 提前续卡操作
			// 不生成外部识别号，生成媒介单元代码，生成新有效期
			String currLogFlag = x5050bo.getCurrLogFlag();
			String nextProcessDate = x5050bo.getNextProcessDate();
			String externalIdentificationNo = x5050bo.getExternalIdentificationNo();
			// 有效期
			EventCommArea eventCommArea = new EventCommArea();
			eventCommArea.setEcommOperMode(x5050bo.getOperationMode());
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			eventCommAreaNonFinance.setOperationDate(nextProcessDate);
			// 生成新的有效期
			String expirationDate = getExpirationDate(eventCommAreaNonFinance, artifactList);
			//旧卡有效期
            String expirationDateOld = x5050bo.getExpirationDate();
            //提前续卡报错信息，新旧两张卡有效期相同不允许提前续卡。 报错改成‘新申请卡片不允许当月提前续卡！’,比较有效期的时候按YYMM格式做比较-新卡>旧卡  add by wangxi 2019/8/30
            int expirationDateYY = Integer.parseInt(expirationDate.substring(2, 4));//新卡-年
            int expirationDateMM = Integer.parseInt(expirationDate.substring(0, 2));//新卡-月
            int expirationDateOldYY = Integer.parseInt(expirationDateOld.substring(2, 4));//旧卡-年
            int expirationDateOldMM = Integer.parseInt(expirationDateOld.substring(0, 2));//旧卡-月
            
            if (expirationDateYY == expirationDateOldYY) {//新卡和旧卡的年相同，比较月
                if(expirationDateMM <= expirationDateOldMM){//如果新卡的月小于旧卡的月，那个不允许提前续卡，抛错。
                    throw new BusinessException("CUS-00081");//新申请卡片不允许当月提前续卡！
                }
            } else {
                eventCommAreaNonFinance.setExpirationDate(expirationDate);
                x5050bo.setExpirationDate(expirationDate);
            }
			/*if (expirationDate.equals(x5050bo.getExpirationDate())) {
				throw new BusinessException("CUS-00081");
			} else {
				eventCommAreaNonFinance.setExpirationDate(expirationDate);
				x5050bo.setExpirationDate(expirationDate);
			}*/
			CoreMediaBasicInfo coreMediaBasicInfoStr = new CoreMediaBasicInfo();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreMediaBasicInfoStr);
			// 生成新的媒介单元代码
			String mediaUnitCode = getCoreCustomerNumberRule(customerNo);
			coreMediaBasicInfoStr.setProductForm(x5050bo.getProductForm());
			coreMediaBasicInfoStr.setMediaUnitCode(mediaUnitCode);
			coreMediaBasicInfoStr.setId(RandomUtil.getUUID());
			coreMediaBasicInfoStr.setExpirationDate(expirationDate);
			coreMediaBasicInfoStr.setActivationFlag("6");
			eventCommAreaNonFinance.setActivationFlag("6");
			coreMediaBasicInfoStr.setInvalidFlag("N");
			coreMediaBasicInfoStr.setInvalidReason(InvalidReasonStatus.PNA.getValue());
			eventCommAreaNonFinance.setInvalidFlag("N");
			coreMediaBasicInfoStr.setCreateDate(nextProcessDate);
			coreMediaBasicInfoStr.setGmtCreate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			coreMediaBasicInfoStr.setActivationDate(null);
			coreMediaBasicInfoStr.setExternalIdentificationNo(externalIdentificationNo);
			coreMediaBasicInfoStr.setMediaObjectCode(x5050bo.getMediaObjectCode());
			coreMediaBasicInfoStr.setVersion(1);
			coreMediaBasicInfoStr.setProductObjectCode(productObjectCode);
			coreMediaBasicInfoStr.setMainCustomerNo(customerNo);
			coreMediaBasicInfoStr.setInstitutionId(x5050bo.getInstitutionId());
			coreMediaBasicInfoStr.setOperationMode(x5050bo.getOperationMode());
			coreMediaBasicInfoStr.setMainSupplyIndicator(x5050bo.getMainSupplyIndicator());
			coreMediaBasicInfoStr.setMediaUserName(x5050bo.getMediaUserName());
			@SuppressWarnings("unused")
			int result = coreMediaBasicInfoDao.insert(coreMediaBasicInfoStr);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaBasicInfoStr, coreMediaBasicInfoStr.getId(),
					currLogFlag, operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
					coreMediaBasicInfoStr.getMediaUnitCode(), null, null);

			List<CoreMediaBind> listCoreMediaBinds = eventCommAreaNonFinance.getListCoreMediaBinds();
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					coreMediaBind.setMediaUnitCode(mediaUnitCode);
					coreMediaBind.setId(RandomUtil.getUUID());
					coreMediaBind.setVersion(1);
				}
				@SuppressWarnings("unused")
				int resultMediaBinds = coreMediaBindDao.insertUseBatch(listCoreMediaBinds);
			}
			if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
				for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
					nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
							ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
							currLogFlag, operatorId, coreMediaBasicInfoStr.getMainCustomerNo(),
							coreMediaBind.getMediaUnitCode(), null, null);
				}
			}
			eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
			eventCommAreaNonFinance.setExpirationDate(expirationDate);
			eventCommAreaNonFinance.setMediaObjectCode(x5050bo.getMediaObjectCode());
			eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
			List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
					.queryProductBusinessScope(productObjectCode, x5050bo.getOperationMode());
			CoreProductBusinessScope coreProductBusinessScope = null;
			if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
				coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
			} else {
				throw new BusinessException("CUS-00064");
			}
			if (coreProductBusinessScope != null) {
				businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
				eventCommAreaNonFinance.setBusinessProgramNo(coreProductBusinessScope.getBusinessProgramNo());
			}
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoStr.getOperationMode());
			// country 所属国家
			eventCommAreaNonFinance.setCountry(x5050bo.getCountry());
			// 媒介对象类型
			eventCommAreaNonFinance.setMediaObjectType(x5050bo.getMediaObjectType());
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.PNA.getValue());
			eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.PNA.getValue());
			eventCommAreaNonFinance.setCurrLogFlag(currLogFlag);
			eventCommAreaNonFinance.setMediaUnitCode(coreMediaBasicInfoStr.getMediaUnitCode());
			/** 提前续卡媒介同步授权   */ 
			eventCommAreaNonFinance.setWhetherProcess("");
			List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
			Map<String, Object> triggerEventParams = new HashMap<String, Object>();
			AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
			authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
			CachedBeanCopy.copyProperties(coreMediaBasicInfoStr, authEventCommAreaNonFinanceBean);
			authEventCommAreaNonFinanceBean.setProductObjectCode(productObjectCode);
			//给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
			eventCommAreaTriggerEventList.add(triggerEventParams);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		}else if (InvalidReasonStatus.DPAN.getValue().equals(invalidReason)) {
			// 申请DPAN媒介类型
            Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
            String mediaUnitCodeOld = eventCommAreaNonFinance.getMediaUnitCode();
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderDpan = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilderDpan.andMediaUnitCodeEqualTo(mediaUnitCodeOld);
            CoreMediaBasicInfo coreMediaBasicInfoD = coreMediaBasicInfoDao
                    .selectBySqlBuilder(coreMediaBasicInfoSqlBuilderDpan);
            eventCommAreaNonFinance.setOperationMode(coreCustomer.getOperationMode());
            // 数据校验
            Map<String, String> mapStr = checkData(eventCommAreaNonFinance);
            eventCommAreaNonFinance.setServiceCode(mapStr.get("serviceCode").toString());
            CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
            // 产品持有人代码
            coreProductFormSqlBuilder.andProductHolderNoEqualTo(coreCustomer.getCustomerNo());
            // 产品对象
            coreProductFormSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
            // 媒介形式
            coreProductFormSqlBuilder.andMediaObjectFormEqualTo((String) mapStr.get("mediaObjectForm"));
            if (StringUtil.isNotBlank(eventCommAreaNonFinance.getMainProductForm())) {
                coreProductFormSqlBuilder.andMainProductFormEqualTo(eventCommAreaNonFinance.getMainProductForm());
            }
            if (StringUtil.isNotBlank(eventCommAreaNonFinance.getDeviceNumber())) {
                coreProductFormSqlBuilder.andDeviceNumberEqualTo(eventCommAreaNonFinance.getDeviceNumber());
            }
            CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
            if (null != coreProductForm) {
                throw new BusinessException("CUS-00141");
            }
            productForm = queryProductForm(coreCustomer.getCustomerNo(), productObjectCode,
                    (String) mapStr.get("mediaObjectForm"), eventCommAreaNonFinance.getSubCustomerNo(),
                    eventCommAreaNonFinance.getMainSupplyIndicator(), coreMediaBasicInfoD.getProductForm(),
                    eventCommAreaNonFinance.getDeviceNumber());
            // 000010001 机构号
            CoreMediaBasicInfo coreMediaBasicInfoDpan = new CoreMediaBasicInfo();
            ReflexUtil.setFieldsValues(coreMediaBasicInfoDpan, map);
            // 客户编号+M+sssss = 媒介单元代码
            String mediaUnitCode = getCoreCustomerNumberRule(eventCommAreaNonFinance.getCustomerNo());
            coreMediaBasicInfoDpan.setId(RandomUtil.getUUID());
            coreMediaBasicInfoDpan.setMediaUnitCode(mediaUnitCode);
            // 产品持有人代码、产品对象、媒介持有人代码与主产品形式相同。
            coreMediaBasicInfoDpan.setProductObjectCode(eventCommAreaNonFinance.getProductObjectCode());
            // 卡类型4已被占用，dpan卡类型使用5
            CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo("0", coreCorporationEntity, operatorId,"5");
            if (null == coreIssueCardBin) {
                throw new BusinessException("CUS-00142", coreCorporationEntity.getCorporationEntityNo());
            }
            Integer binNo = coreIssueCardBin.getBinNo();
            CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnitForBinNo(binNo.toString(),
                    coreCorporationEntity, operatorId);
            String operationDate = "";
            if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
                operationDate = coreSystemUnit.getCurrProcessDate();
            } else {
                operationDate = coreSystemUnit.getNextProcessDate();
            }
            // 运营模式
            String operationMode = coreCustomer.getOperationMode();
            EventCommArea eventCommArea_424 = new EventCommArea();
            eventCommArea_424.setEcommProdObjId(productObjectCode);
            eventCommArea_424.setEcommOperMode(operationMode);
            String externalIdentificationNo = null;
            // 判断是否自动生成配对号
            if (null == coreArtifactInstanRelList || coreArtifactInstanRelList.isEmpty()) {
            	externalIdentificationNo = this.automaticNumAssig(BSC.ARTIFACT_NO_424, eventCommArea_424, null,
                        binNo.toString(), artifactList, operationDate, operatorId,
                        coreCorporationEntity.getCorporationEntityNo());
			}else {
				//查询预留卡号表
				CoreCardPool coreCardPool = httpQueryService.queryCoreCardPool(binNo.toString(), coreCustomer.getCorporationEntityNo());
				if (null != coreCardPool) {
					externalIdentificationNo = coreCardPool.getCardNumber();
				}else {
					throw new BusinessException("CUS-00150");
				}
			}
            coreMediaBasicInfoDpan.setExternalIdentificationNo(externalIdentificationNo);
            coreMediaBasicInfoDpan.setActivationFlag("2");
            coreMediaBasicInfoDpan.setActivationDate(null);
//            coreMediaBasicInfoDpan.setActivationMode("");
            coreMediaBasicInfoDpan.setApplicationNumber(eventCommAreaNonFinance.getApplicationNumber());
            coreMediaBasicInfoDpan.setBranchNumber(eventCommAreaNonFinance.getBranchNumber());
            coreMediaBasicInfoDpan.setMediaUserName(x5050bo.getMediaUserName());
            coreMediaBasicInfoDpan.setExpirationDate("9999");
            coreMediaBasicInfoDpan.setInstitutionId(x5050bo.getInstitutionId());
            coreMediaBasicInfoDpan.setInvalidFlag("Y");
            coreMediaBasicInfoDpan.setInvalidReason("");
            coreMediaBasicInfoDpan.setMainCustomerNo(customerNo);
            coreMediaBasicInfoDpan.setMainSupplyIndicator(mainSupplyIndicator);
            coreMediaBasicInfoDpan.setMarketerCode(eventCommAreaNonFinance.getMarketerCode());
            coreMediaBasicInfoDpan.setMediaDispatchMethod(eventCommAreaNonFinance.getMediaDispatchMethod());
            coreMediaBasicInfoDpan.setMediaObjectCode(eventCommAreaNonFinance.getMediaObjectCode());
            coreMediaBasicInfoDpan.setOperationMode(operationMode);
            coreMediaBasicInfoDpan.setPinDispatchMethod(eventCommAreaNonFinance.getPinDispatchMethod());
            coreMediaBasicInfoDpan.setProductForm(productForm);
            coreMediaBasicInfoDpan.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
            coreMediaBasicInfoDpan.setVersion(1);
            int result = coreMediaBasicInfoDao.insert(coreMediaBasicInfoDpan);

            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
            List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao
                    .selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
            List<ResultGns> coreMediaBasicInfoList1 = new ArrayList<>();
            for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
                ResultGns resultGns = new ResultGns();
                resultGns.setExternalIdentificationNo(coreMediaBasicInfo2.getExternalIdentificationNo());
                coreMediaBasicInfoList1.add(resultGns);
            }
            eventCommAreaNonFinance.setCoreMediaBasicInfoList(coreMediaBasicInfoList1);
            customerNo = coreMediaBasicInfoDpan.getMainCustomerNo();
            eventCommAreaNonFinance.setMainCustomerNo(coreMediaBasicInfoDpan.getMainCustomerNo());
            eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
            eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
            /** 新增媒介同步授权 */
            eventCommAreaNonFinance.setWhetherProcess("");
            List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
            Map<String, Object> triggerEventParams = new HashMap<String, Object>();
            AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
            CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
            CachedBeanCopy.copyProperties(coreMediaBasicInfoDpan, authEventCommAreaNonFinanceBean);
            authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
            authEventCommAreaNonFinanceBean.setProductForm(coreMediaBasicInfoDpan.getProductForm());
            authEventCommAreaNonFinanceBean.setExpirationDate(coreMediaBasicInfoDpan.getExpirationDate());
            authEventCommAreaNonFinanceBean.setSystemUnitNo(coreCustomer.getSystemUnitNo());
            authEventCommAreaNonFinanceBean.setMainProductForm(coreMediaBasicInfoD.getProductForm());
            CachedBeanCopy.copyProperties(coreCustomer, authEventCommAreaNonFinanceBean);
            authEventCommAreaNonFinanceBean.setMediaObjectType("T");
            //给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
            triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
            eventCommAreaTriggerEventList.add(triggerEventParams);
            eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
            eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
            } else if (InvalidReasonStatus.CHP.getValue().equals(invalidReason)) {
            // 媒介升降级操作
            // 不生成外部识别号，生成媒介单元代码，生成新有效期
		    CoreMediaBasicInfo coreMediaBasicInfoChp = new CoreMediaBasicInfo();
		    CachedBeanCopy.copyProperties(x5050bo,coreMediaBasicInfoChp);
		    coreMediaBasicInfoChp.setId(RandomUtil.getUUID());
		    coreMediaBasicInfoChp.setExpirationDate(eventCommAreaNonFinance.getExpirationDate());
		    coreMediaBasicInfoChp.setExternalIdentificationNo(eventCommAreaNonFinance.getExternalIdentificationNo());
		    //激活状态标识 1：已激活 2：新发卡未激活 3：毁卡补发/续卡未激活 4：转卡未激活  5： 无需激活 6: 提前续卡为激活 7:产品升降级未激活
            coreMediaBasicInfoChp.setActivationFlag("7");
            eventCommAreaNonFinance.setActivationFlag("7");
            // 媒介单元代码
            String mediaUnitCode = getCoreCustomerNumberRule(eventCommAreaNonFinance.getCustomerNo());
            coreMediaBasicInfoChp.setMediaUnitCode(mediaUnitCode);
            // 查询系统单元编号
            CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
            String operationDate = coreSystemUnit.getNextProcessDate();
            coreMediaBasicInfoChp.setCreateDate(operationDate);
            coreMediaBasicInfoChp.setOperationMode(x5050bo.getOperationMode());
            coreMediaBasicInfoChp.setInvalidFlag("N");
            eventCommAreaNonFinance.setInvalidFlag("N");
            coreMediaBasicInfoChp.setInvalidReason(InvalidReasonStatus.CHP.getValue());
            coreMediaBasicInfoChp.setActivationDate(null);
            coreMediaBasicInfoChp.setVersion(1);
            coreMediaBasicInfoChp.setTimeStamp(null);

            // 获取有效期
            String expirationDate = computeExpirationDate(eventCommAreaNonFinance, coreMediaBasicInfoChp, artifactList);
            eventCommAreaNonFinance.setExpirationDate(expirationDate);
            coreMediaBasicInfoChp.setExpirationDate(expirationDate);
            coreMediaBasicInfoChp.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));

            String currLogFlag = x5050bo.getCurrLogFlag();
            String externalIdentificationNo = x5050bo.getExternalIdentificationNo();
            coreMediaBasicInfoChp.setProductForm(x5050bo.getProductForm());
            coreMediaBasicInfoChp.setMediaObjectCode(x5050bo.getMediaObjectCode());
            coreMediaBasicInfoChp.setProductObjectCode(productObjectCode);
            coreMediaBasicInfoChp.setMainCustomerNo(customerNo);
            coreMediaBasicInfoChp.setInstitutionId(x5050bo.getInstitutionId());
            coreMediaBasicInfoChp.setMainSupplyIndicator(x5050bo.getMainSupplyIndicator());
            coreMediaBasicInfoChp.setMediaUserName(x5050bo.getMediaUserName());
            coreMediaBasicInfoDao.insert(coreMediaBasicInfoChp);

            nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
                    ModificationType.ADD.getValue(), null, null, coreMediaBasicInfoChp, coreMediaBasicInfoChp.getId(),
                    currLogFlag, operatorId, coreMediaBasicInfoChp.getMainCustomerNo(),
                    coreMediaBasicInfoChp.getMediaUnitCode(), null, null);

            List<CoreMediaBind> listCoreMediaBinds = eventCommAreaNonFinance.getListCoreMediaBinds();
            if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
                for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
                    coreMediaBind.setMediaUnitCode(mediaUnitCode);
                    coreMediaBind.setId(RandomUtil.getUUID());
                    coreMediaBind.setVersion(1);
                }
                @SuppressWarnings("unused")
                int resultMediaBinds = coreMediaBindDao.insertUseBatch(listCoreMediaBinds);
            }
            if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
                for (CoreMediaBind coreMediaBind : listCoreMediaBinds) {
                    nonFinancialLogUtil.createNonFinancialActivityLog(x5050bo.getEventNo(), x5050bo.getActivityNo(),
                            ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
                            currLogFlag, operatorId, coreMediaBasicInfoChp.getMainCustomerNo(),
                            coreMediaBind.getMediaUnitCode(), null, null);
                }
            }
            eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
            eventCommAreaNonFinance.setMediaObjectCode(x5050bo.getMediaObjectCode());
            eventCommAreaNonFinance.setMediaUnitCode(mediaUnitCode);
            List<CoreProductBusinessScope> listCoreProductBusinessScopes = httpQueryService
                    .queryProductBusinessScope(productObjectCode, x5050bo.getOperationMode());
            CoreProductBusinessScope coreProductBusinessScope = null;
            if (null != listCoreProductBusinessScopes && !listCoreProductBusinessScopes.isEmpty()) {
                coreProductBusinessScope = listCoreProductBusinessScopes.get(0);
            } else {
                throw new BusinessException("CUS-00064");
            }
            if (coreProductBusinessScope != null) {
                businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
                eventCommAreaNonFinance.setBusinessProgramNo(coreProductBusinessScope.getBusinessProgramNo());
            }
            eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfoChp.getOperationMode());
            // country 所属国家
            eventCommAreaNonFinance.setCountry(x5050bo.getCountry());
            // 媒介对象类型
            eventCommAreaNonFinance.setMediaObjectType(x5050bo.getMediaObjectType());
            eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.CHP.getValue());
            eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.CHP.getValue());
            eventCommAreaNonFinance.setCurrLogFlag(currLogFlag);
            eventCommAreaNonFinance.setMediaUnitCode(coreMediaBasicInfoChp.getMediaUnitCode());
            /** 媒介升降级同步授权   */
            eventCommAreaNonFinance.setWhetherProcess("");
            List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
            Map<String, Object> triggerEventParams = new HashMap<String, Object>();
            AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
            CachedBeanCopy.copyProperties(eventCommAreaNonFinance,authEventCommAreaNonFinanceBean);
            authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
            CachedBeanCopy.copyProperties(coreMediaBasicInfoChp,authEventCommAreaNonFinanceBean);
            authEventCommAreaNonFinanceBean.setProductObjectCode(productObjectCode);
            //给授权传递账单日期
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			//coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			List<AuthEventCommAreaNonFinanceBean> authList = new ArrayList<AuthEventCommAreaNonFinanceBean>();
			if(listCoreCustomerBillDays!=null && listCoreCustomerBillDays.size()>0){
				for(CoreCustomerBillDay customerBillDay:listCoreCustomerBillDays){
					AuthEventCommAreaNonFinanceBean authEventCommAreaBean = new AuthEventCommAreaNonFinanceBean();
					CachedBeanCopy.copyProperties(authEventCommAreaNonFinanceBean, authEventCommAreaBean);
					authEventCommAreaBean.setCycleRefreshDate(customerBillDay.getNextBillDate());
					authEventCommAreaBean.setBusinessProgram(customerBillDay.getBusinessProgramNo());
					authEventCommAreaBean.setBusinessProgramNo(customerBillDay.getBusinessProgramNo());
					authList.add(authEventCommAreaBean);
				}
				authEventCommAreaNonFinanceBean.setAuths(authList);
			}
            triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
            eventCommAreaTriggerEventList.add(triggerEventParams);
            eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
            eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
        } else {
			// 没有这种情况
		}
		eventCommAreaNonFinance.setSystemUnitNo(coreCustomer.getSystemUnitNo());
		eventCommAreaNonFinance.setProductForm(productForm);
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		eventCommAreaNonFinance.setRetailVerifyPswFlag("1");
		eventCommAreaNonFinance.setProductObjectCode(productObjectCode);
		eventCommAreaNonFinance.setRetailVerifyPswAmount(new BigDecimal("0"));
		eventCommAreaNonFinance.setMainCustomerNo(customerNo);
		eventCommAreaNonFinance.setMainSupplyIndicator(mainSupplyIndicator);
		// 业务项目代码
		eventCommAreaNonFinance.setBusinessProgramNo(businessProgramNo);
		eventCommAreaNonFinance.setSupplyCustomerNo(eventCommAreaNonFinance.getSubCustomerNo());
		return eventCommAreaNonFinance;
	}

	/**
	 * 如果是公务卡，获取预算单位编码 @Description: TODO() @param: @param
	 * coreMediaBasicInfo @param: @return @param: @throws Exception @return:
	 * String @throws
	 */
	private String getBudgetOrgCode(String customerNo, String productObjectCode) throws Exception {
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
		if (coreProduct != null) {
			return coreProduct.getBudgetOrgCode();
		} else {
			return "";
		}
	}

	private Map<String, String> checkData(EventCommAreaNonFinance eventCommAreaNonFinance) throws Exception {
		// 2 参考《产品与媒介类数据结构》文档中的“媒介基本信息”页的各类信息将字段段赋值
		// 3 若字段为新建时必输项、但输入信息中无此字段，则报错
		// 4 根据必输信息“运营模式”+“产品对象代码”+“客户代码”从“产品基本信息”表中定位对应记录，若定位失败则报错处理。
		// 5 根据必输信息“运营机构”从“运营机构“表中定位对应记录，若定位失败则报错
		// 6 若“主附标识”取值为2，则需检核“副客户代码”需有值，且与“主客户代码”字段取值不同
		Map<String, String> map = new HashMap<>();
		// 检查产品基本信息
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andOperationModeEqualTo(eventCommAreaNonFinance.getOperationMode());
		coreProductSqlBuilder.andProductObjectCodeEqualTo(eventCommAreaNonFinance.getProductObjectCode());
		coreProductSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
		CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
		if (coreProduct == null) {
			throw new BusinessException("CUS-00014", "产品基本");// 机构表
		}
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(coreProduct.getOperationMode(),
				eventCommAreaNonFinance.getMediaObjectCode());
		if (coreMediaObject == null) {
			throw new BusinessException("CUS-00014", "媒介对象");// 机构表
		}
		// 检查机构表
		String institutionId = eventCommAreaNonFinance.getInstitutionId();

		CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);

		if (coreOrgan == null) {
			throw new BusinessException("CUS-00014", "机构表");// 机构表
		}
		map.put("country", coreOrgan.getCountry());
		map.put("mediaObjectType", coreMediaObject.getMediaObjectType());
		map.put("serviceCode", coreMediaObject.getServiceCode());
		map.put("mediaObjectForm", coreMediaObject.getMediaObjectForm());
		// 检查主附标识取值
		String mainSupplyIndicator = eventCommAreaNonFinance.getMainSupplyIndicator();
		if ("2".equals(mainSupplyIndicator)) {// 主附标识 1：主卡 2：附属卡
			/**
			 * 如果为附属卡，增加如下检查:1.主客户产品下存在有效媒介;2.附客户号存在
			 * 当 主附标识为2时，去库里进行查询，查询“媒介单元基本信息”表内客户下是否存在有效媒介
			 * add by wangxi 2019/6/10
			 */
			String customerNo = null;
			if (StringUtil.isNotBlank(eventCommAreaNonFinance.getSupIdNumber()) && StringUtil.isNotBlank(
					eventCommAreaNonFinance.getSupIdType())) {
				String supIdNumber = eventCommAreaNonFinance.getSupIdNumber();
				String supIdType = eventCommAreaNonFinance.getSupIdType();
				CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
				coreCustomerSqlBuilder.andIdNumberEqualTo(supIdNumber);
				coreCustomerSqlBuilder.andIdTypeEqualTo(supIdType);
				CoreCustomer supCoreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
				customerNo = supCoreCustomer.getCustomerNo();//客户代码
			}else {
				customerNo = eventCommAreaNonFinance.getCustomerNo();
			}
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
			List<CoreMediaBasicInfo> coreMediaBasicInfoList =
					coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if(null != coreMediaBasicInfoList && coreMediaBasicInfoList.size() > 0){
				//定义一个判断参数
				int flag = 0;
				for (CoreMediaBasicInfo coreMediaBasicInfo : coreMediaBasicInfoList) {
					String invalidFlag = coreMediaBasicInfo.getInvalidFlag();
					//某一条数据符合时,进入判断
					if ("Y".equals(invalidFlag)) {
						flag = 1;
						//值被修改后跳出循环
						break;
					}
				}
				//判断,是否要抛错
				if(flag == 0){
					throw new BusinessException("CUS-00106");//该产品当前无有效主卡，不能申请附属卡
				}
				String subCustomerNo = null;
				// 副客户代码
				if (StringUtil.isNotBlank(eventCommAreaNonFinance.getSupIdNumber()) && StringUtil.isNotBlank(
						eventCommAreaNonFinance.getSupIdType())) {
					subCustomerNo = eventCommAreaNonFinance.getCustomerNo();
				}else {
					subCustomerNo = eventCommAreaNonFinance.getSubCustomerNo();
				}
				if (StringUtil.isBlank(subCustomerNo)) {
					throw new BusinessException("CUS-00013", "副客户代码");// 副客户代码
				}
				
			} else {
				throw new BusinessException("CUS-00106");//该产品当前无有效主卡，不能申请附属卡
			}
			
		}

		return map;
	}


	/**
	 * 
	 * @Description: 获取有效期
	 * @param eventCommAreaNonFinance
	 * @return
	 * @throws Exception
	 */
	public String getExpirationDate(EventCommAreaNonFinance eventCommAreaNonFinance,
			List<CoreActivityArtifactRel> artifactList) throws Exception {
		/**
		 * 有效期生成 1. 使用哪个媒介对象、构件编号、构件类型(M)、调用 "活动构件公共程序"，获取生成元件于生效PCD值 2. 获取元件编号为
		 * "301AAA0100" 表示自动生成有效期，根据PCD值存储的月数于当前系统营业日生成本媒体有效期，格式MMYY 3. 获取元件编号为
		 * "301AAA0101" 表示手动生成有效期，则根据报文输入的有效期直接将相关字段赋值，若输入报文无有效期或小于当前日期，则报错
		 */
		// 运营模式
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_301, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		@SuppressWarnings("unused")
		int calInterestDay = 0;
		// 获取元件信息
		eventCommAreaNonFinance.getEventCommArea().setEcommMediaObjId(eventCommAreaNonFinance.getMediaObjectCode());
		String termValidity = "";
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_301,
				eventCommAreaNonFinance.getEventCommArea());
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.AUTOMATIC_GENERATION_MEDIA_VALIDITY.equals(entry.getKey())) {
				termValidity = entry.getValue().toString();
			} else if (Constant.MANUAL_INPUT_MEDIUM_VALIDITY_PERIOD.equals(entry.getKey())) {
				termValidity = "";
			}
			if (logger.isDebugEnabled()) {
				logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
			}
		}

		@SuppressWarnings("unused")
		String expirationDate = "";
		if (null == termValidity || "".equals(termValidity)) {
			// 获取上送的生效日期
			expirationDate = eventCommAreaNonFinance.getExpirationDate();
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD");
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(DateUtil.parse(eventCommAreaNonFinance.getOperationDate()));
			rightNow.add(Calendar.MONTH, Integer.parseInt(termValidity));// 日期加60个月
			String reStr = sdf.format(rightNow.getTime());
			String termValidityYY = reStr.substring(2, 4);
			String termValidityMM = reStr.substring(4, 6);
			// 有效期MMYY
			termValidity = termValidityMM + termValidityYY;
		}
		return termValidity;
	}

	/**
	 * 
	 * @Description: 生成媒介单元代码
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private String getCoreCustomerNumberRule(String customerNo) throws Exception {
		CoreCustomerNumberRuleSqlBuilder coreCustomerNumberRuleSqlBuilder = new CoreCustomerNumberRuleSqlBuilder();
		coreCustomerNumberRuleSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerNumberRuleSqlBuilder.andSeqTypeEqualTo(Constant.MEDIA_ARTI);
		CoreCustomerNumberRule coreCustomerNumberRule = coreCustomerNumberRuleDao
				.selectBySqlBuilder(coreCustomerNumberRuleSqlBuilder);
		// 下一顺序号
		int nextSeqNo = 0;
		int lowerOrderNumber = 0;
		if (null != coreCustomerNumberRule) {
			nextSeqNo = coreCustomerNumberRule.getNextSeqNo();
			String key = "seqNo_" + Constant.MEDIA_ARTI + "_" + customerNo;
			Integer seqNo = (Integer) CacheUtils.getInstance().getMap(Integer.class, key);
			if (seqNo == null) {
				CacheUtils.getInstance().addMap(Integer.class, key, nextSeqNo);
				lowerOrderNumber = nextSeqNo;
			} else {
				lowerOrderNumber = seqNo + 1;
				if (lowerOrderNumber < nextSeqNo) {
					lowerOrderNumber = nextSeqNo;
				}
				CacheUtils.getInstance().addMap(Integer.class, key, lowerOrderNumber);
			}
			nextSeqNo = lowerOrderNumber + 1;
			// 新生成的余额单元代码更新客户序号规则表
			coreCustomerNumberRule.setNextSeqNo(nextSeqNo);
			// 版本号+1
			coreCustomerNumberRule.setVersion(coreCustomerNumberRule.getVersion() + 1);
			coreCustomerNumberRuleSqlBuilder.andNextSeqNoLessThan(nextSeqNo);
			coreCustomerNumberRuleDao.updateBySqlBuilderSelective(coreCustomerNumberRule,
					coreCustomerNumberRuleSqlBuilder);
		}
		return customerNo + Constant.MEDIA_ARTI + String.format("%05d", lowerOrderNumber);
	}

	/**
	 * 
	 * @Description: 查询产品形式代码
	 * @param productHolderNo
	 * @param productObjectCode
	 * @param mediaObjectForm
	 * @param mediaHolderNo
	 * @return
	 * @throws Exception
	 */
	public String queryProductForm(String productHolderNo, String productObjectCode, String mediaObjectForm,
			String mediaHolderNo, String mainSupplyIndicator) throws Exception {
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		// 产品持有人代码
		coreProductFormSqlBuilder.andProductHolderNoEqualTo(productHolderNo);
		// 产品对象
		coreProductFormSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		// 媒介形式
		coreProductFormSqlBuilder.andMediaObjectFormEqualTo(mediaObjectForm);
		// 媒介持有人代码
		if (StringUtil.isNotBlank(mediaHolderNo)) {
			coreProductFormSqlBuilder.andMediaHolderNoEqualTo(mediaHolderNo);
		}else {
			coreProductFormSqlBuilder.andMediaHolderNoEqualTo(productHolderNo);
		}
		CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
		if (coreProductForm != null) {
			return coreProductForm.getProductForm();
		} else {
			// 新增一条产品形式代码
			CoreProductForm coreProductFormIn = new CoreProductForm();
			coreProductFormIn.setId(RandomUtil.getUUID());
			coreProductFormIn.setMainSupplyIndicator(mainSupplyIndicator);
			if (StringUtil.isNotBlank(mediaHolderNo)) {
				coreProductFormIn.setMediaHolderNo(mediaHolderNo);
			} else {
				coreProductFormIn.setMediaHolderNo(productHolderNo);
			}
			coreProductFormIn.setMediaObjectForm(mediaObjectForm);
			coreProductFormIn.setProductHolderNo(productHolderNo);
			coreProductFormIn.setProductObjectCode(productObjectCode);
			coreProductFormIn.setVersion(1);
			// 获取产品形式代码
			String productForm = this.getProductForm(productHolderNo);
			coreProductFormIn.setProductForm(productForm);
			coreProductFormDao.insert(coreProductFormIn);
			return coreProductFormIn.getProductForm();
		}
	}
	
	/**
     * 
     * @Description: 查询产品形式代码
     * @param productHolderNo
     * @param productObjectCode
     * @param mediaObjectForm
     * @param mediaHolderNo
     * @return
     * @throws Exception
     */
    public String queryProductForm(String productHolderNo, String productObjectCode, String mediaObjectForm,
            String mediaHolderNo, String mainSupplyIndicator, String mainProductFrom, String deviceNumber)
            throws Exception {
        CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
        // 产品持有人代码
        coreProductFormSqlBuilder.andProductHolderNoEqualTo(productHolderNo);
        // 产品对象
        coreProductFormSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        // 媒介形式
        coreProductFormSqlBuilder.andMediaObjectFormEqualTo(mediaObjectForm);
        // 媒介持有人代码
        if (StringUtil.isNotBlank(mediaHolderNo)) {
            coreProductFormSqlBuilder.andMediaHolderNoEqualTo(mediaHolderNo);
        } else {
            coreProductFormSqlBuilder.andMediaHolderNoEqualTo(productHolderNo);
        }
        if (StringUtil.isNotBlank(productHolderNo)) {
            coreProductFormSqlBuilder.andMainProductFormEqualTo(productHolderNo);
        }
        if (StringUtil.isNotBlank(deviceNumber)) {
            coreProductFormSqlBuilder.andDeviceNumberEqualTo(deviceNumber);
        }
        CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
        if (coreProductForm != null) {
            return coreProductForm.getProductForm();
        } else {
            // 新增一条产品形式代码
            CoreProductForm coreProductFormIn = new CoreProductForm();
            coreProductFormIn.setId(RandomUtil.getUUID());
            coreProductFormIn.setMainSupplyIndicator(mainSupplyIndicator);
            if (StringUtil.isNotBlank(mediaHolderNo)) {
                coreProductFormIn.setMediaHolderNo(mediaHolderNo);
            } else {
                coreProductFormIn.setMediaHolderNo(productHolderNo);
            }
            coreProductFormIn.setMediaObjectForm(mediaObjectForm);
            coreProductFormIn.setProductHolderNo(productHolderNo);
            coreProductFormIn.setProductObjectCode(productObjectCode);
            coreProductFormIn.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
            coreProductFormIn.setVersion(1);
            // 获取产品形式代码
            String productForm = null;
            if (StringUtil.isNotBlank(mainProductFrom) || StringUtil.isNotBlank(deviceNumber)) {
                productForm = this.getProductForm(productHolderNo);
            } else {
                productForm = this.getProductForm(productHolderNo);
            }
            coreProductFormIn.setProductForm(productForm);
            if (StringUtil.isNotBlank(mainProductFrom)) {
                coreProductFormIn.setMainProductForm(mainProductFrom);
            }
            if (StringUtil.isNotBlank(deviceNumber)) {
                coreProductFormIn.setDeviceNumber(deviceNumber);
            }
            coreProductFormDao.insert(coreProductFormIn);
            return coreProductFormIn.getProductForm();
        }
    }

	/**
	 * 
	 * @Description: 生成产品形式代码
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	private String getProductForm(String customerNo) throws Exception {
		CoreCustomerNumberRuleSqlBuilder coreCustomerNumberRuleSqlBuilder = new CoreCustomerNumberRuleSqlBuilder();
		coreCustomerNumberRuleSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerNumberRuleSqlBuilder.andSeqTypeEqualTo(Constant.PRODUCT_FORM);
		CoreCustomerNumberRule coreCustomerNumberRule = coreCustomerNumberRuleDao
				.selectBySqlBuilder(coreCustomerNumberRuleSqlBuilder);
		// 下一顺序号
		int nextSeqNo = 0;
		int lowerOrderNumber = 0;
		if (null != coreCustomerNumberRule) {
			nextSeqNo = coreCustomerNumberRule.getNextSeqNo();
			String key = "seqNo_" + Constant.PRODUCT_FORM + "_" + customerNo;
			Integer seqNo = (Integer) CacheUtils.getInstance().getMap(Integer.class, key);
			if (seqNo == null) {
				CacheUtils.getInstance().addMap(Integer.class, key, nextSeqNo);
				lowerOrderNumber = nextSeqNo;
			} else {
				lowerOrderNumber = seqNo + 1;
				if (lowerOrderNumber < nextSeqNo) {
					lowerOrderNumber = nextSeqNo;
				}
				CacheUtils.getInstance().addMap(Integer.class, key, lowerOrderNumber);
			}
			nextSeqNo = lowerOrderNumber + 1;
			// 新生成的余额单元代码更新客户序号规则表
			coreCustomerNumberRule.setNextSeqNo(nextSeqNo);
			// 版本号+1
			int version = coreCustomerNumberRule.getVersion() + 1;
			coreCustomerNumberRule.setVersion(version);
			coreCustomerNumberRuleSqlBuilder.andNextSeqNoLessThan(nextSeqNo);
			coreCustomerNumberRuleDao.updateBySqlBuilderSelective(coreCustomerNumberRule,
					coreCustomerNumberRuleSqlBuilder);
		}
		return customerNo + Constant.PRODUCT_FORM + String.format("%05d", lowerOrderNumber);
	}

	/**
	 * 
	 * @Description: 获取有效期
	 * @param eventCommAreaNonFinance
	 * @return
	 * @throws Exception
	 */
	private String computeExpirationDate(EventCommAreaNonFinance eventCommAreaNonFinance,
			CoreMediaBasicInfo coreMediaBasicInfo, List<CoreActivityArtifactRel> artifactList) throws Exception {
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_301, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommMediaObjId(eventCommAreaNonFinance.getMediaObjectCode());
		eventCommArea.setEcommOperMode(coreMediaBasicInfo.getOperationMode());
		// eventCommAreaNonFinance.getEventCommArea().setEcommMediaObjId(eventCommAreaNonFinance.getMediaObjectCode());
		// eventCommAreaNonFinance.getEventCommArea().setEcommOperMode(coreMediaBasicInfo.getOperationMode());
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> resultPcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_301, eventCommArea);
		Iterator<Map.Entry<String, String>> it = resultPcdResultMap.entrySet().iterator();
		String termValidity = "";
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.AUTOMATIC_GENERATION_MEDIA_VALIDITY.equals(entry.getKey())) {
				// 获取value
				termValidity = entry.getValue().toString();
			} else if (Constant.MANUAL_INPUT_MEDIUM_VALIDITY_PERIOD.equals(entry.getKey())) {
				termValidity = "";
			}
		}
		String operationMode = coreMediaBasicInfo.getOperationMode();
		CoreOperationModeSqlBuilder coreOperationModeSqlBuilder = new CoreOperationModeSqlBuilder();
		coreOperationModeSqlBuilder.andOperationModeEqualTo(operationMode);
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = operationModeUtil.getcoreOperationMode(coreCustomer.getCustomerNo());
		String operationDate = "";
		if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
			operationDate = coreSystemUnit.getCurrProcessDate();
		} else {
			operationDate = coreSystemUnit.getNextProcessDate();
		}
		// 1. 用系统当前运营日期计算出N个月之后的日期为Date1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(DateUtil.parse(operationDate, "yyyyMMdd"));
		rightNow.add(Calendar.MONTH, Integer.parseInt(termValidity));// 日期加60个月
		String Date1 = sdf.format(rightNow.getTime());
		// 2. 取媒介单元基本信息中的有效期MMyy，取系统当前运营日期的年份前两位yy，
		// 取媒介单元基本信息中的新建日期中的dd，将yy+MMyy+dd拼接成yyyyMMdd格式的日期，为Date2。
		String expirationDate = coreMediaBasicInfo.getExpirationDate();
		String expirationDateMM = expirationDate.substring(0, 2);
		String expirationDateYY = expirationDate.substring(2, 4);
		String yy = operationDate.substring(0, 2);
		String dd = coreMediaBasicInfo.getCreateDate().substring(8, 10);
		String Date2 = yy + expirationDateYY + expirationDateMM + dd;
		// 3. 将Date2加一个月，计算出Date3。
		@SuppressWarnings("unused")
		Calendar rightNowDate2 = Calendar.getInstance();
		rightNow.setTime(DateUtil.parse(Date2, "yyyyMMdd"));
		rightNow.add(Calendar.MONTH, 1);// 日期加60个月
		String Date3 = sdf.format(rightNow.getTime());
		// 4. 如果Date1>Date2，则新的有效期 = Date1，否则新的有效期 = Date3。
		String termValidity1 = "";
		if (Date1.compareTo(Date2) > 0) {
			// 有效期MMYY
			termValidity1 = Date1;
		} else {
			termValidity1 = Date3;
		}
		String termValidityYY = termValidity1.substring(2, 4);
		String termValidityMM = termValidity1.substring(4, 6);
		// 有效期MMYY
		termValidity = termValidityMM + termValidityYY;
		return termValidity;
	}

	/**
	 * 判断是否自动配号
	 * 
	 * @param artifactNo
	 * @param eventCommArea
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String automaticNumAssig(String artifactNo, EventCommArea eventCommArea, String externalIdentificationNo_in,
			String binNo, List<CoreActivityArtifactRel> artifactList, String operationDate, String operatorId,String corporationEntityNo)
			throws Exception {
		// 验证该活动是否配置构件信息
		Boolean element01 = false; // 是否跳过自动配号 false-自动配号 true-不自动配号
		String element02 = ""; //
		Boolean element03 = false; //
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_424, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> resultMap = artService.getElementByArtifact(artifactNo, eventCommArea);
		Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			// 424AAA01
			if (Constant.AUTOMATIC_STANDARD_BIN_FORMAT.equals(key)) {
				element01 = true;
			} else if (Constant.AUTOMATIC_NON_STANDARD_BIN_FORMAT.equals(key)) {
				element01 = false;
			}
			// 424AAA02
			if (Constant.AUTOMATIC_ALLOW_OPTIONAL_CARD_NUMBER.equals(key)) {
				element02 = "true"; //
			} else if (Constant.AUTOMATIC_NO_OPTIONAL_CARD_NUMBER_ALLOWED.equals(key)) {
				element02 = "false"; //
			}
			// 424AAA03
			if (Constant.AUTOMATIC_NUMBER_ASSIGNMENT.equals(key)) {
				element03 = true;
			} else if (Constant.AUTOMATIC_NUMBER_NOT_APPLY.equals(key)) {
				element03 = false;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
			}
		}
		String externalIdentificationNo = "";
		if (element01) {
			if ("false".equals(element02) && element03) {
				// 检查外部识别号必须为空，并根据卡BIN随机生成新的号码；
				if (StringUtil.isNotBlank(externalIdentificationNo_in)) {
					throw new BusinessException("CUS-00092");
				}
				GenerateCardNumBean generateCardNumBean = managerCardNumService.generateExternalIdentificationNo(binNo, null,corporationEntityNo);
				externalIdentificationNo = generateCardNumBean.getExternalIdentificationNo();
//				return generateCardNumBean.getExternalIdentificationNo();
			} else if ("true".equals(element02) && element03) {
				// 检查如果外部识别号已输入，则跳过自动配号处理，进行特殊号码检查；如果外部识别号未输入，则根据卡BIN随机生成新的号码
				if (StringUtil.isNotBlank(externalIdentificationNo_in)) {
					// 外部识别号必须输入测试
					//externalIdentificationNo = this.checkeEternalIdentificationNo(externalIdentificationNo_in, operationDate,
							//operatorId,binNo,corporationEntityNo);
					//需要更新
					
					externalIdentificationNo =  externalIdentificationNo_in;
				}else {
					//根据卡Bin进行自动配号检查
					boolean flag = true;
					GenerateCardNumBean generateCardNumBean = null;
					do {
						generateCardNumBean = managerCardNumService.generateExternalIdentificationNo(binNo, null,corporationEntityNo);
						if (YesOrNo.NO.getValue().equals(generateCardNumBean.getFlag())) {
							externalIdentificationNo = generateCardNumBean.getExternalIdentificationNo();
							flag = false;
						}
					} while (flag);
//					return externalIdentificationNo;
					
				}
				
			} else {
				// 不存在这样的场景
				throw new BusinessException("CUS-00091", BSC.ARTIFACT_NO_424);
			}
		} else {
			if (StringUtil.isNotBlank(externalIdentificationNo_in)) {
				// 外部识别号必须输入测试
				//externalIdentificationNo = this.checkeEternalIdentificationNo(externalIdentificationNo_in, operationDate,
						//operatorId,binNo,corporationEntityNo);
				//需要更新
				
				externalIdentificationNo = externalIdentificationNo_in;
			}else {
				throw new BusinessException("CUS-00096");
			}
//			return externalIdentificationNo;
		}
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			managerCardNumService.updCoreSpecialCard(externalIdentificationNo);
		}
		
		return externalIdentificationNo;
	}

	/**
	 * 验证输入外部识别号是否符合条件
	 * 
	 * @param externalIdentificationNo_in
	 * @return
	 * @throws Exception
	 */
	public String checkeEternalIdentificationNo(String externalIdentificationNoIn, String operationDate,
			String operatorId,String binNo,String corporationEntityNo) throws Exception {
		// 验证一下卡BIN
		CoreSpecialCard coreSpecialCard = httpQueryService.queryCoreSpecialCard(externalIdentificationNoIn);
		if (null == coreSpecialCard) {
			throw new BusinessException("CUS-00090");
		}
		String cardStatus = coreSpecialCard.getCardStatus();
		if (("P".equals(cardStatus) || "Y".equals(cardStatus))) {
			throw new BusinessException("CUS-00093");
		}
		String key = coreSpecialCard.getCardNumber();
		Map<String, String> coreSpecialCardMap = new HashMap<String, String>();
		coreSpecialCardMap.put("cardNumber", coreSpecialCard.getCardNumber());
		coreSpecialCardMap.put("redisKey", key);
		// 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
		coreSpecialCardMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		coreSpecialCardMap.put("operatorId", operatorId);
		coreSpecialCardMap.put("startDate", operationDate);
		coreSpecialCardMap.put("cardStatus", "Y");
		int result = betaCommonParamService.updateCoreSpecialCard(coreSpecialCardMap);
		if (1!= result) {
			throw new BusinessException("CUS-00075", "预留特殊卡号表");
		}
		String cardNumber = coreSpecialCard.getCardNumber();
		String segmentNumber = cardNumber.substring(binNo.length(), binNo.length()+3);
		// 剩余 预留特殊卡号卡量表  剩余特殊卡量 -1 
		Map<String, String> coreSpecialCalorimeterMap = new HashMap<String, String>();
		// 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
		coreSpecialCalorimeterMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		coreSpecialCalorimeterMap.put("operatorId", operatorId);
		coreSpecialCalorimeterMap.put("cardBin", binNo);
		coreSpecialCalorimeterMap.put("corporationEntityNo", corporationEntityNo);
		coreSpecialCalorimeterMap.put("segmentNumber", segmentNumber);
		int result1 = betaCommonParamService.updateCoreSpecialCalorimeter(coreSpecialCalorimeterMap);
		if (1!= result1) {
			throw new BusinessException("CUS-00075", "预留特殊卡号卡量表");
		}
		String externalIdentificationNo = externalIdentificationNoIn;
		return externalIdentificationNo;
	}
	
}
