package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5360Bus;
import com.tansun.ider.dao.beta.CoreMediaObjectDao;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreMediaBindDao;
import com.tansun.ider.dao.issue.CoreMediaCardInfoDao;
import com.tansun.ider.dao.issue.CoreMediaLabelInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
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
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.bo.X5360BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 毁损补发
 * @author: admin
 */
@Service
public class X5360BusImpl implements X5360Bus {

	@Resource
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreMediaBindDao coreMediaBindDao;
	@Autowired
	private CoreMediaLabelInfoDao coreMediaLabelInfoDao;
//	@Resource
//	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreMediaCardInfoDao coreMediaCardInfoDao;
	@Autowired
	private CoreProductFormDao coreProductFormDao;
//	@Autowired
//	private CoreOrganDaoImpl coreOrganDaoImpl;
	@Autowired
	private CoreMediaObjectDao coreMediaObjectDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreProductDao coreProductDao;

	@Override
	public Object busExecute(X5360BO x5360BO) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5360BO, eventCommAreaNonFinance);
		@SuppressWarnings("unused")
		String globalEventNo = x5360BO.getGlobalEventNo();
		String invalidReason = x5360BO.getInvalidReason();
		String operatorId = x5360BO.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		@SuppressWarnings("unused")
		List<CoreActivityArtifactRel> ListCoreActivityArtifactRels = x5360BO.getActivityArtifactList();
		// 身份证号
		@SuppressWarnings("unused")
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 通过身份证号或者外部识别号查询媒介基本信息
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		List<CoreMediaBasicInfo> listCoreMediaBasicInfo = queryCustomerService
				.queryCoreMediaBasicInfoList(externalIdentificationNo, "Y");
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		if (!listCoreMediaBasicInfo.isEmpty()) {
			coreMediaBasicInfo = listCoreMediaBasicInfo.get(0);
			CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
		} else {
			throw new BusinessException("CUS-00048");// 机构表
		}
		//如果是公务卡，获取预算单位编码
		String budgetOrgCode = getBudgetOrgCode(coreMediaBasicInfo);
		if(StringUtil.isNotBlank(budgetOrgCode)){
			eventCommAreaNonFinance.setBudgetOrgCode(budgetOrgCode);
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder
				.andExternalIdentificationNoEqualTo(coreMediaBasicInfo.getExternalIdentificationNo());
		coreMediaBasicInfoSqlBuilder.andIdEqualTo(coreMediaBasicInfo.getId());
		if (InvalidReasonStatus.BRK.getValue().equals(invalidReason)) {
			//毁损补发卡片状态修改为： 新卡无效，旧卡有效。         所以这里旧卡改为有效    add by wangxi 2019/9/3
			coreMediaBasicInfo.setInvalidFlag("Y");
			coreMediaBasicInfo.setInvalidReason(InvalidReasonStatus.BRK.getValue());
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.BRK.getValue());
			eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.BRK.getValue());
		} else if (InvalidReasonStatus.EXP.getValue().equals(invalidReason)) {
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.EXP.getValue());
			coreMediaBasicInfo.setInvalidReason("");
		}
		coreMediaBasicInfoDaoImpl.updateBySqlBuilderSelective(coreMediaBasicInfo, coreMediaBasicInfoSqlBuilder);
		CoreCustomerSqlBuilder coreCustomerSqlBuilderAfter = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilderAfter.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilderAfter);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		nonFinancialLogUtil.createNonFinancialActivityLog(x5360BO.getEventNo(), x5360BO.getActivityNo(),
				ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
				coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
				coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);
		eventCommAreaNonFinance.setExternalIdentificationNo(coreMediaBasicInfo.getExternalIdentificationNo());
		// 查询媒介绑定信息，并将其放到公共区
		CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
		coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		List<CoreMediaBind> listCoreMediaBinds = coreMediaBindDao.selectListBySqlBuilder(coreMediaBindSqlBuilder);
		if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
			eventCommAreaNonFinance.setListCoreMediaBinds(listCoreMediaBinds);
		}
		// 查询媒介标签信息，并将其放到公共区
		CoreMediaLabelInfoSqlBuilder coreMediaLabelInfoSqlBuilder = new CoreMediaLabelInfoSqlBuilder();
		coreMediaLabelInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		List<CoreMediaLabelInfo> listCoreMediaLabelInfos = coreMediaLabelInfoDao
				.selectListBySqlBuilder(coreMediaLabelInfoSqlBuilder);
		if (null != listCoreMediaLabelInfos && !listCoreMediaLabelInfos.isEmpty()) {
			eventCommAreaNonFinance.setCoreMediaLabelInfos(listCoreMediaLabelInfos);
		}
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDao.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		//报空指针，增加非空判断 add by wangxi 2019/7/4
		if(null != coreMediaCardInfo){
			eventCommAreaNonFinance.setEmbosserName1(
					coreMediaCardInfo.getEmbosserName1() == null ? "" : coreMediaCardInfo.getEmbosserName1());
		}
			eventCommAreaNonFinance.setMediaObjectCode(coreMediaBasicInfo.getMediaObjectCode());
			eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo.getOperationMode());
			eventCommAreaNonFinance.setCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
			eventCommAreaNonFinance.setMainCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
			// 获取有效期
			eventCommAreaNonFinance.setInvalidFlag(coreMediaBasicInfo.getInvalidFlag());
			eventCommAreaNonFinance.setTransferMediaCode(coreMediaBasicInfo.getMediaUnitCode());
			eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
		// CoreProductBusinessScopeSqlBuilder coreProductBusinessScopeSqlBuilder
		// = new CoreProductBusinessScopeSqlBuilder();
		// coreProductBusinessScopeSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
		// coreProductBusinessScopeSqlBuilder.andOperationModeEqualTo(coreMediaBasicInfo.getOperationMode());
		// CoreProductBusinessScope coreProductBusinessScope =
		// coreProductBusinessScopeDao
		// .selectBySqlBuilder(coreProductBusinessScopeSqlBuilder);
		//TODO
		 List<CoreProductBusinessScope>  coreProductBusinessScopeList = httpQueryService.queryProductBusinessScope(
				coreMediaBasicInfo.getProductObjectCode(), coreMediaBasicInfo.getOperationMode());
		 CoreProductBusinessScope coreProductBusinessScope =  coreProductBusinessScopeList.get(0);
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
		eventCommAreaNonFinance.setExpirationDate(coreMediaBasicInfo.getExpirationDate());
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		coreProductFormSqlBuilder.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
		CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
		eventCommAreaNonFinance.setProductForm(coreProductForm.getProductForm());
		// 查询国家码
		String institutionId = coreMediaBasicInfo.getInstitutionId();
//		CoreOrganSqlBuilder coreOrganSqlBuilder = new CoreOrganSqlBuilder();
//		coreOrganSqlBuilder.andOrganNoEqualTo(institutionId);
//		CoreOrgan coreOrgan = coreOrganDaoImpl.selectBySqlBuilder(coreOrganSqlBuilder);
		CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);
		if (coreOrgan == null) {
			throw new BusinessException("CUS-00014", "机构表");// 机构表
		}
		// 国家码
		eventCommAreaNonFinance.setCountry(coreOrgan.getCountry());
		// 查询媒介状态
//		CoreMediaObjectSqlBuilder coreMediaObjectSqlBuilder = new CoreMediaObjectSqlBuilder();
//		coreMediaObjectSqlBuilder.andMediaObjectCodeEqualTo(eventCommAreaNonFinance.getMediaObjectCode());
//		CoreMediaObject coreMediaObject = coreMediaObjectDao.selectBySqlBuilder(coreMediaObjectSqlBuilder);
		
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(coreMediaBasicInfo.getOperationMode(), eventCommAreaNonFinance.getMediaObjectCode());
		if (null == coreMediaObject) {
			throw new BusinessException("CUS-00014", "媒介对象表");// 机构表
		}
		/**
		 * 毁损补发，只支持media_object_form='R'的媒介做毁损补发
		 * add by wangxi 2019/7/4 cyy提
		 */
		//媒介形式 R ： 实体  V：虚拟
		String mediaObjectForm = coreMediaObject.getMediaObjectForm();
		if(StringUtil.isNotBlank(mediaObjectForm)){
			if(!"R".equals(mediaObjectForm)){
				throw new BusinessException("CUS-00126");// 该卡片不是实体卡，无法毁损补发！
			}
		}
		
		eventCommAreaNonFinance.setMediaObjectType(coreMediaObject.getMediaObjectType());
		eventCommAreaNonFinance.setServiceCode(coreMediaObject.getServiceCode());
		eventCommAreaNonFinance.setActivationDate(null);
		eventCommAreaNonFinance.setBusinessProgramNo(coreProductBusinessScope.getBusinessProgramNo());
		if (InvalidReasonStatus.EXP.getValue().equals(invalidReason)) {
			eventCommAreaNonFinance.setInvalidReasonOld(null);
			eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.EXP.getValue());
		}
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
		eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
		eventCommAreaNonFinance.setCustomerNo(coreCustomer.getCustomerNo());
		
		/**
		 * 同步授权：
		 * 活动触发事件表-识别维度代码recog_dimen_code字段为null的删掉，触发事件调用的解决方法
		 * add by wangxi 2019/7/22
		 */
		eventCommAreaNonFinance.setWhetherProcess("");
		List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
		AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
		CachedBeanCopy.copyProperties(eventCommAreaNonFinance, authEventCommAreaNonFinanceBean);
		authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
		eventCommAreaTriggerEventList.add(triggerEventParams);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		
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
	public static void main(String[] args) {
		String expirationDate = "1234";
		String expirationDateMM = expirationDate.substring(0, 2);
		System.out.println(expirationDateMM);
		String expirationDateYY = expirationDate.substring(2, 4);
		System.out.println(expirationDateYY);
		String dd = "2018-08-06".substring(8, 10);
		System.out.println(dd);
	}

}
