package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5080Bus;
import com.tansun.ider.dao.beta.CoreMediaObjectDao;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBindDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaLabelInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductFormDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLabelInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.ELE;
import com.tansun.ider.model.bo.X5080BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 转卡处理
 * @author: admin
 */
@Service
public class X5080BusImpl implements X5080Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreMediaBindDaoImpl coreMediaBindDaoImpl;
	@Resource
	private CoreMediaLabelInfoDaoImpl coreMediaLabelInfoDaoImpl;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private CoreProductFormDaoImpl coreProductFormDaoImpl;
	@Resource
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreMediaObjectDao coreMediaObjectDao;
	@Autowired
	private CoreCustomerContrlViewDao coreCustomerContrlViewDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreProductDao coreProductDao;
	@Autowired
    private CardUtil cardUtil;
	@Value("${global.target.service.nofn.SP013004}")
	private String spEventNo;
	

	@Override
	public Object busExecute(X5080BO x5080bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5080bo, eventCommAreaNonFinance);
		String mediaUnitCode = x5080bo.getMediaUnitCode();
		String operatorId = x5080bo.getOperatorId();
		// 新发卡
		if (operatorId == null) {
			operatorId = "system";
		}
		List<CoreActivityArtifactRel> artifactList = x5080bo.getActivityArtifactList();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		if (StringUtil.isNotBlank(mediaUnitCode)) {
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		}
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (null == coreMediaBasicInfo) {
			throw new BusinessException("CUS-00014", "媒介单元基本");
		}
		//如果是公务卡，获取预算单位编码
		String budgetOrgCode = getBudgetOrgCode(coreMediaBasicInfo);
		if(StringUtil.isNotBlank(budgetOrgCode)){
			eventCommAreaNonFinance.setBudgetOrgCode(budgetOrgCode);
		}
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
		/**
		 * 判断是否已经设置封锁码
		 */
		CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
		coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		coreCustomerContrlViewSqlBuilder.andContrlLevelEqualTo("M");
		coreCustomerContrlViewSqlBuilder.andContrlSerialNoEqualTo(0);
		coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		List<CoreCustomerContrlView> listCoreCustomerContrlViews = coreCustomerContrlViewDao
				.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
		if (null != listCoreCustomerContrlViews && !listCoreCustomerContrlViews.isEmpty()) {
			for (CoreCustomerContrlView coreCustomerContrlView : listCoreCustomerContrlViews) {
				if (ELE.E_211AA0101.equals(coreCustomerContrlView.getControlProjectNo())) {
					throw new BusinessException("CUS-00030");
				}
			}
		}
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
		// 是否可转卡标志
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(coreMediaBasicInfo.getOperationMode());
		String isTransferCard = CardUtils.getTransferCard(eventCommArea, artifactList, coreMediaBasicInfo);
		if ("N".equals(isTransferCard)) {
			throw new BusinessException("CUS-00030");
		}
		// 修改媒介状态
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInformationSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInformationSqlBuilderStr.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		coreMediaBasicInfo.setInvalidFlag("N");
		coreMediaBasicInfo.setInvalidReason(InvalidReasonStatus.TRF.getValue());
		coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
		int result = coreMediaBasicInfoDaoImpl.updateBySqlBuilderSelective(coreMediaBasicInfo,
				coreMediaBasicInformationSqlBuilderStr);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介单元基本信息");
		}
		// 查询运营模式，运营日期
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
		eventCommAreaNonFinance.setCustomerNo(coreCustomer.getCustomerNo());
		eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
		eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
		if (coreCustomer != null) {
//			CoreOperationModeSqlBuilder coreOperationModeSqlBuilder = new CoreOperationModeSqlBuilder();
//			coreOperationModeSqlBuilder.andOperationModeEqualTo(coreCustomer.getOperationMode());
//			CoreOperationMode coreOperationMode = coreOperationModeDaoImpl
//					.selectBySqlBuilder(coreOperationModeSqlBuilder);
			CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(coreCustomer.getOperationMode());
			if (null != coreOperationMode) {
				CachedBeanCopy.copyProperties(coreOperationMode, eventCommAreaNonFinance);
			}
		}
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		nonFinancialLogUtil.createNonFinancialActivityLog(x5080bo.getEventNo(), x5080bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
				coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
				coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null,null);
		// 将媒介状态修改后内容,同步授权工程
		// 查询媒介单元基本信息，并将其放到公共区
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
		eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfo.getInvalidFlag());
		eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.TRF.getValue());
		// 查询媒介绑定信息，并将其放到公共区
		CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
		coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		List<CoreMediaBind> listCoreMediaBinds = coreMediaBindDaoImpl.selectListBySqlBuilder(coreMediaBindSqlBuilder);
		if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
			eventCommAreaNonFinance.setListCoreMediaBinds(listCoreMediaBinds);
		}
		// 查询媒介标签信息，并将其放到公共区
		CoreMediaLabelInfoSqlBuilder coreMediaLabelInfoSqlBuilder = new CoreMediaLabelInfoSqlBuilder();
		coreMediaLabelInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		List<CoreMediaLabelInfo> listCoreMediaLabelInfos = coreMediaLabelInfoDaoImpl
				.selectListBySqlBuilder(coreMediaLabelInfoSqlBuilder);
		if (null != listCoreMediaLabelInfos && !listCoreMediaLabelInfos.isEmpty()) {
			eventCommAreaNonFinance.setCoreMediaLabelInfos(listCoreMediaLabelInfos);
		}
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		coreProductFormSqlBuilder.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
		CoreProductForm coreProductForm = coreProductFormDaoImpl.selectBySqlBuilder(coreProductFormSqlBuilder);
		eventCommAreaNonFinance.setProductForm(coreProductForm.getProductForm());
		// 查询出 凸字信息
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		CachedBeanCopy.copyProperties(coreMediaCardInfo, eventCommAreaNonFinance);
		// 查询国家码
		String institutionId = coreMediaBasicInfo.getInstitutionId();
//		CoreOrganSqlBuilder coreOrganSqlBuilder = new CoreOrganSqlBuilder();
//		coreOrganSqlBuilder.andOrganNoEqualTo(institutionId);
//		CoreOrgan coreOrgan = coreOrganDaoImpl.selectBySqlBuilder(coreOrganSqlBuilder);
		
		/**
		 * 媒介挂失成功后弹出授权例外名单页面，前端需要后端提供外部识别号和卡组织。所以需要将卡组织查出来进行赋值操作
		 * add by wangxi 2019/9/11
		 */
		/*-------------------------start-------------------------------
		//得到法人实体信息
        CoreCorporationEntity coreCorporationEntity = cardUtil.getSystemUnitNoCoreCorporationEntity(institutionId);
        //产品对象代码
        String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
        //运营模式
        String operationMode = coreMediaBasicInfo.getOperationMode();
        //得到产品对象信息
        CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, productObjectCode);
        //发行卡BIN
        String binNo = coreProductObject.getBinNo().toString();
        //得到发行卡BIN信息
        CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(binNo,coreCorporationEntity,operatorId);
        //卡组织
        String cardScheme = coreIssueCardBin.getCardScheme();
        if(StringUtil.isNotBlank(cardScheme)){
            eventCommAreaNonFinance.setCardScheme(cardScheme);//卡组织赋值
        }
        ------------------------end---------------------------------*/
		
		CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);
		
		if (coreOrgan == null) {
			throw new BusinessException("CUS-00014", "机构表");// 机构表
		}
		// 国家码
		eventCommAreaNonFinance.setCountry(coreOrgan.getCountry());
		// 查询媒介状态
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(eventCommAreaNonFinance.getOperationMode(), eventCommAreaNonFinance.getMediaObjectCode());
//		CoreMediaObjectSqlBuilder coreMediaObjectSqlBuilder = new CoreMediaObjectSqlBuilder();
//		coreMediaObjectSqlBuilder.andMediaObjectCodeEqualTo(eventCommAreaNonFinance.getMediaObjectCode());
//		CoreMediaObject coreMediaObject = coreMediaObjectDao.selectBySqlBuilder(coreMediaObjectSqlBuilder);
		if (null == coreMediaObject) {
			throw new BusinessException("CUS-00014", "媒介对象表");// 机构表
		}
		eventCommAreaNonFinance.setServiceCode(coreMediaObject.getServiceCode());
		eventCommAreaNonFinance.setMediaObjectType(coreMediaObject.getMediaObjectType());
		// 转卡标志
		eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.TRF.getValue());
		eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.TRF.getValue());
		// 转出媒介单元代码
		eventCommAreaNonFinance.setTransferMediaCode(coreMediaBasicInfo.getMediaUnitCode());
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		
		/**
         * 获取媒介注销事件上封锁码的封锁码类别、封锁码场景序号    add by wangxi 
         */
        CoreEvent coreEvent = httpQueryService.queryEvent("ISS.OP.01.0003");
        String effectivenessCodeType = coreEvent.getEffectivenessCodeType();//封锁码类别
        Integer effectivenessCodeScene = coreEvent.getEffectivenessCodeScene();//封锁码场景序号
        String customerNo = coreMediaBasicInfo.getMainCustomerNo();
        //查询封锁码表
        CoreEffectivenessCode coreEffectivenessCode =
                httpQueryService.queryEffectivenessCode(coreMediaBasicInfo.getOperationMode(),
                		effectivenessCodeType, effectivenessCodeScene + "");
        //对该媒介上封锁码
        eventCommAreaNonFinance.setCustomerNo(customerNo);//客户号
        eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo.getOperationMode());//运营模式
        eventCommAreaNonFinance.setEffectivenessCodeScope(coreEffectivenessCode.getEffectivenessCodeScope());//管控层级
        eventCommAreaNonFinance.setLevelCode(coreMediaBasicInfo.getMediaUnitCode());//层级代码
        eventCommAreaNonFinance.setOperatorId(x5080bo.getOperatorId());//操作员
        eventCommAreaNonFinance.setEffectivenessCodeType(effectivenessCodeType);//封锁码类别
        eventCommAreaNonFinance.setEffectivenessCodeScene(effectivenessCodeScene);//封锁码场景
        eventCommAreaNonFinance.setSceneTriggerObject("M");//场景触发对象-管控层级 必输
        //授权需要传值字段
        eventCommAreaNonFinance.setAuthDataSynFlag("1");
        eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());//产品对象
        eventCommAreaNonFinance.setMainCustomerNo(customerNo);//主客户号
        //授权同步卡状态
        eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfo.getInvalidFlag());//是否有效标识
        eventCommAreaNonFinance.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());//失效原因
        //上封锁码传递事件号
        eventCommAreaNonFinance.setSpEventNo(spEventNo);
		
		return eventCommAreaNonFinance;
	}
	/**
	 * 如果是公务卡，获取预算单位编码
	 * @Description: TODO()   
	 * @param: @param coreMediaBasicInfo
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String      
	 * @throws
	 */
	private String getBudgetOrgCode(CoreMediaBasicInfo coreMediaBasicInfo) throws Exception{
		String customerNo = coreMediaBasicInfo.getMainCustomerNo();
		String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
		if(coreProduct!=null){
			return coreProduct.getBudgetOrgCode();
		}else{
			return "";
		}
	}
}
