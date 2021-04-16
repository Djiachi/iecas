package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5210Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.beta.impl.CoreProductObjectDaoImpl;
import com.tansun.ider.dao.beta.sqlbuilder.CoreProductObjectSqlBuilder;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreAccountDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreBalanceUnitDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.InvalidFlagType;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.MainSupplyIndicatorType;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.QuotaBean;
import com.tansun.ider.model.bo.X5210BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 媒介注销
 * @author: admin
 */
@Service
public class X5210BusImpl implements X5210Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Resource
	private CoreProductObjectDaoImpl coreProductObjectDaoImpl;
	@Autowired
	private CoreAccountDaoImpl coreAccountDaoImpl;
	@Autowired
	private CoreBalanceUnitDaoImpl coreBalanceUnitDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private HttpQueryServiceByGns httpQueryServiceByGns;
	@Value("${global.target.service.url.auth}")
	private String authUrl;
	@Autowired
	private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
	@Autowired
	private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
	@Value("${global.target.service.nofn.SP013003}")
	private String spEventNo;
	
	@Override
	public Object busExecute(X5210BO x5210bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5210bo, eventCommAreaNonFinance);
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		String mediaUnitCode = x5210bo.getMediaUnitCode();
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderQuery = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilderQuery.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			//因为有某个外部识别号可查出多条记录，但是有效的媒介只能查出一条。需要做媒介注销操作只可对有效的媒介进行注销操作，所以加有效卡标识条件  add by wangxi 2019/7/4
			coreMediaBasicInfoSqlBuilderQuery.andInvalidFlagEqualTo("Y");
		} else {
			if (StringUtil.isNotBlank(mediaUnitCode)) {
				coreMediaBasicInfoSqlBuilderQuery.andMediaUnitCodeEqualTo(x5210bo.getMediaUnitCode());
			}
		}
		coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderQuery);
		if (null == coreMediaBasicInfo) {
			throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
		}
		externalIdentificationNo = coreMediaBasicInfo.getExternalIdentificationNo();
		
		// if
		// (!MediaStateType.NEW_HAIR.getValue().equals(coreMediaBasicInfo.getStatusCode())
		// &&!MediaStateType.ACTIVE.getValue().equals(coreMediaBasicInfo.getStatusCode())
		// &&
		// !MediaStateType.NO_ACTIVE.getValue().equals(coreMediaBasicInfo.getStatusCode()))
		// {
		// throw new BusinessException("CUS-00034");
		// }
		//公务卡检查余额
		checkOfficialCardBalance(coreMediaBasicInfo);
		// 主客户号
		String customerNo = coreMediaBasicInfo.getMainCustomerNo();
		// 查询产品线代码
		CoreProductObjectSqlBuilder coreProductObjectSqlBuilder = new CoreProductObjectSqlBuilder();
		coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
		// 主客户标识
		coreMediaBasicInfoSqlBuilder.andMainSupplyIndicatorEqualTo(MainSupplyIndicatorType.MAIN_CARD.getValue());
		// 是否有效标识	
		coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(InvalidFlagType.Y.getValue());//判断是否为最后一张有效主卡	add by wangxi 	2019/5/29
		//产品对象代码-因需要判断的是客户+产品下的最后一张主卡，所以需要加上产品对象代码的条件进行查询操作	add by wangxi 2019/7/3 cyy提
		coreMediaBasicInfoSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
		List<CoreMediaBasicInfo> listCoreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);

		if (listCoreMediaBasicInfo != null && !listCoreMediaBasicInfo.isEmpty()) {
			if (listCoreMediaBasicInfo.size() == 1) {
				CoreMediaBasicInfo coreMediaBasicInfoStr = listCoreMediaBasicInfo.get(0);
				if (externalIdentificationNo.equals(coreMediaBasicInfoStr.getExternalIdentificationNo())) {
					// 最后一张卡不允许销卡
					throw new BusinessException("CUS-00035");
				}
			}
		}
		
		
		
		
		/*
		 * 注释掉原因：
		 * 1.主卡
		 * 	 产品下有多于一张主卡时，不管余额是不是0都能注销 ，所以不需要对余额单元进行判断。
		 * 	 产品下只剩下一张主卡时，不允许注销，可通过客户产品注销进行注销。
		 * 2.附卡
		 * 	附属卡不做判断直接注销。
		 * 
		 * 	add by wangxi 2019/5/29
		 *
		// 判断余额单元是否为0
		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		// coreAccountSqlBuilder.andProductLineCodeEqualTo(productLineCode);
		coreAccountSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
		coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
		List<CoreAccount> listCoreAccounts = coreAccountDaoImpl.selectListBySqlBuilder(coreAccountSqlBuilder);
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		CachedBeanCopy.copyProperties(coreMediaBasicInfoAfter, coreMediaBasicInfo);
		if (listCoreAccounts != null && !listCoreAccounts.isEmpty()) {
			for (CoreAccount coreAccount : listCoreAccounts) {
				CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
				coreBalanceUnitSqlBuilder.andAccountIdEqualTo(coreAccount.getAccountId());
				List<CoreBalanceUnit> listCoreBalanceUnits = coreBalanceUnitDaoImpl
						.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
				for (CoreBalanceUnit coreBalanceUnit : listCoreBalanceUnits) {
					if (BigDecimal.ZERO.compareTo(coreBalanceUnit.getBalance()) != 0) {
						throw new BusinessException("CUS-00036");
					}
				}
			}
		}*/
		
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
		
		coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
		coreMediaBasicInfo.setInvalidFlag("N");
		coreMediaBasicInfo.setInvalidReason(InvalidReasonStatus.CLS.getValue());
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
		// coreMediaBasicInfo.setStatusCode(MediaStateType.CLOSE.getValue());
		coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		// coreMediaBasicInfoSqlBuilderStr
		// .andExternalIdentificationNoEqualTo(coreMediaBasicInfo.getExternalIdentificationNo());
		coreMediaBasicInfoSqlBuilderStr.andMediaUnitCodeEqualTo(x5210bo.getMediaUnitCode());
		int result = coreMediaBasicInfoDaoImpl.updateBySqlBuilderSelective(coreMediaBasicInfo,
				coreMediaBasicInfoSqlBuilderStr);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介基本信息表");
		}
		String operatorId = x5210bo.getOperatorId(); // 获取操作员ID
		if (operatorId == null) {
			operatorId = "system";
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if (null != coreCustomer) {
			// 通过系统单元编号获取当前日志标识
			String systemUnitNo = coreCustomer.getSystemUnitNo();
			// CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new
			// CoreSystemUnitSqlBuilder();
			// coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo(systemUnitNo);
			// CoreSystemUnit coreSystemUnit =
			// coreSystemUnitDao.selectBySqlBuilder(coreSystemUnitSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
			String currLogFlag = coreSystemUnit.getCurrLogFlag();
			x5210bo.setCurrLogFlag(currLogFlag);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5210bo.getEventNo(), x5210bo.getActivityNo(),
					ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
					coreMediaBasicInfo.getId(), x5210bo.getCurrLogFlag(), operatorId, customerNo,
					coreMediaBasicInfo.getMediaUnitCode(), null, null);
		}
		
		eventCommAreaNonFinance.setExpirationDate(coreMediaBasicInfo.getExpirationDate());//有效期
		
		/**
		 * 获取媒介注销事件上封锁码的封锁码类别、封锁码场景序号    add by wangxi 
		 */
		CoreEvent coreEvent = httpQueryService.queryEvent("BSS.OP.01.0018");
        String blockCodeType = coreEvent.getEffectivenessCodeType();//生效码类别
        Integer blockCodeScene = coreEvent.getEffectivenessCodeScene();//生效码序号
        //查询封锁码表
        CoreEffectivenessCode coreEffectivenessCode =
                httpQueryService.queryEffectivenessCode(coreMediaBasicInfo.getOperationMode(),
                        blockCodeType, blockCodeScene + "");
        //对该媒介上封锁码
        eventCommAreaNonFinance.setCustomerNo(customerNo);//客户号
        eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo.getOperationMode());//运营模式
        eventCommAreaNonFinance.setEffectivenessCodeScope(coreEffectivenessCode.getEffectivenessCodeScope());//生效码范围
        eventCommAreaNonFinance.setLevelCode(coreMediaBasicInfo.getMediaUnitCode());//层级代码[媒介单元代码]
        eventCommAreaNonFinance.setOperatorId(x5210bo.getOperatorId());//操作员
		eventCommAreaNonFinance.setEffectivenessCodeType(blockCodeType);//封锁码类别
        eventCommAreaNonFinance.setEffectivenessCodeScene(blockCodeScene);//封锁码场景
        eventCommAreaNonFinance.setSceneTriggerObject("M");//场景触发对象
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
	 * 公务卡判断余额
	 * @Description: TODO()   
	 * @param: @param coreMediaBasicInfo
	 * @param: @throws Exception      
	 * @return: void      
	 * @throws
	 */
	private void checkOfficialCardBalance(CoreMediaBasicInfo coreMediaBasicInfo) throws Exception{
		EventCommArea eventCommArea = new EventCommArea();
		String operationMode = coreMediaBasicInfo.getOperationMode();
		String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
		eventCommArea.setEcommOperMode(operationMode);
		eventCommArea.setEcommProdObjId(productObjectCode);
		List<CoreProductBusinessScope> queryProductBusinessScope = httpQueryService.queryProductBusinessScope(productObjectCode,operationMode);
		for (CoreProductBusinessScope coreProductBusinessScope : queryProductBusinessScope) {
			eventCommArea.setEcommBusinessProgramCode(coreProductBusinessScope.getBusinessProgramNo());
			CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
			Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_505, eventCommArea);
			Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (Constant.OFFICIAL_CARD.equals(entry.getKey())) { // 505AAA1003
					// 判断余额单元是否为0
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
					coreAccountSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
					List<CoreAccount> listCoreAccounts = coreAccountDaoImpl.selectListBySqlBuilder(coreAccountSqlBuilder);
					CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
					CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
					if (listCoreAccounts != null && !listCoreAccounts.isEmpty()) {
						for (CoreAccount coreAccount : listCoreAccounts) {
							CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
							coreBalanceUnitSqlBuilder.andAccountIdEqualTo(coreAccount.getAccountId());
							List<CoreBalanceUnit> listCoreBalanceUnits = coreBalanceUnitDaoImpl
									.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
							for (CoreBalanceUnit coreBalanceUnit : listCoreBalanceUnits) {
								if (BigDecimal.ZERO.compareTo(coreBalanceUnit.getBalance()) != 0) {
									throw new BusinessException("该卡还有余额");
								}
							}
						}
					}
					//恢复预算单位额度
					restorationQuota(coreMediaBasicInfo);
				}
			}
		}
	}
	/**
	 * 
	 * @Description: TODO()   
	 * @param: @param coreMediaBasicInfo
	 * @param: @throws Exception      
	 * @return: void      
	 * @throws
	 */
	private void restorationQuota(CoreMediaBasicInfo coreMediaBasicInfo) throws Exception{
		String mainCustomerNo = coreMediaBasicInfo.getMainCustomerNo();
		String externalIdentificationNo = coreMediaBasicInfo.getExternalIdentificationNo();
		QuotaBean quotaBean = httpQueryServiceByGns.queryGnsQuota(authUrl, coreMediaBasicInfo.getExternalIdentificationNo());
		BigDecimal allQuota = BigDecimal.ZERO;
		String dateNow = DateUtil.format(new Date(), "yyyy-MM-dd");
		if(quotaBean==null){
			throw new BusinessException("该卡查询额度失败");
		}
		//临时额度
		BigDecimal tempLimit = quotaBean.getTempLimit();
		//永久额度
		BigDecimal permLimit = quotaBean.getPermLimit();
		allQuota = allQuota.add(permLimit);
		if(tempLimit !=null 
				&& BigDecimal.ZERO.compareTo(tempLimit)<0
				&& quotaBean.getTempLimitEffectvDate().compareTo(dateNow)<=0
				&& quotaBean.getTempLimitExpireDate().compareTo(dateNow)>=0){
			allQuota = allQuota.add(tempLimit);
		}
		CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
		coreBudgetOrgCustRelSqlBuilder.andCustomerNoEqualTo(mainCustomerNo);
		coreBudgetOrgCustRelSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		CoreBudgetOrgCustRel coreBudgetOrgCustRel = coreBudgetOrgCustRelDao.selectBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
		if(coreBudgetOrgCustRel==null){
			throw new BusinessException("该卡在预算单位单位员工关系表中不存在");
		}
		String idNumber = coreBudgetOrgCustRel.getBudgetOrgCode();
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
		CoreCustomer customer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if(customer==null){
			throw new BusinessException("该卡所属预算单位不存在");
		}
		//预算单位客户号
		String customerNo = customer.getCustomerNo();
		CoreBudgetOrgAddInfoSqlBuilder builder = new CoreBudgetOrgAddInfoSqlBuilder();
		builder.andCustomerNoEqualTo(customerNo);
		CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(builder);
		BigDecimal orgRestQuota = coreBudgetOrgAddInfo.getOrgRestQuota();
		CoreBudgetOrgAddInfo newCoreBudgetOrgAddInfo = new CoreBudgetOrgAddInfo();
		CachedBeanCopy.copyProperties(coreBudgetOrgAddInfo, newCoreBudgetOrgAddInfo);
		//恢复额度
		builder.andVersionEqualTo(newCoreBudgetOrgAddInfo.getVersion());
		int decimal = 2;
        BigDecimal needQuota = CurrencyConversionUtil.expand(allQuota, decimal);
		BigDecimal newRestQuota = orgRestQuota.add(needQuota);
		newCoreBudgetOrgAddInfo.setOrgRestQuota(newRestQuota);
		newCoreBudgetOrgAddInfo.setVersion(newCoreBudgetOrgAddInfo.getVersion()+1);
		int i = coreBudgetOrgAddInfoDao.updateBySqlBuilder(newCoreBudgetOrgAddInfo, builder);
		if(i!=1){
			throw new BusinessException("恢复额度失败");
		}
	}
}
