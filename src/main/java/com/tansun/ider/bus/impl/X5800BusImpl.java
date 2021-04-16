package com.tansun.ider.bus.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5800Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBindDao;
import com.tansun.ider.dao.issue.CoreMediaCardInfoDao;
import com.tansun.ider.dao.issue.CoreMediaLabelInfoDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLabelInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5800BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 提前换卡验证操作
 */
@Service
public class X5800BusImpl implements X5800Bus {

	private static Logger logger = LoggerFactory.getLogger(X5800BusImpl.class);

	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private QueryCustomerService QueryCustomerService;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreMediaBindDao coreMediaBindDao;
	@Autowired
	private CoreMediaLabelInfoDao coreMediaLabelInfoDao;
	@Autowired
	private CoreProductFormDao coreProductFormDao;
	@Autowired
	private CoreMediaCardInfoDao coreMediaCardInfoDao;
	@Autowired
	private X5050BusImpl x5050Bus;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object busExecute(X5800BO x5800bo) throws Exception {
		// 外部识别号
		String externalIdentificationNo = x5800bo.getExternalIdentificationNo();
		// 操作员Id
		String operatorId = x5800bo.getOperatorId();
		// 活动关联构件
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		// 获取媒介信息
		Object object = QueryCustomerService.queryCustomer(null, null, externalIdentificationNo);
		if (object instanceof CoreCustomer) {
			CoreCustomer coreCustomer = (CoreCustomer) object;
			throw new BusinessException("CUS-00076");
		} else if (object instanceof CoreMediaBasicInfo) {
			coreMediaBasicInfo = (CoreMediaBasicInfo) object;
		}
		String mainCustomerNo = coreMediaBasicInfo.getMainCustomerNo();
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(mainCustomerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if(coreCustomer==null){
			throw new BusinessException("CUS-12000");
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder1.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
		List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao
				.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder1);
		
		if(null == coreMediaBasicInfoList || coreMediaBasicInfoList.isEmpty()){
			throw new BusinessException("CUS-00085");
		}
		
		CoreMediaBasicInfo coreMediaBasicInfo1 = null;
		for (CoreMediaBasicInfo coreMediaBasicInfo3 : coreMediaBasicInfoList) {
			if (coreMediaBasicInfo3.getInvalidFlag().equals("Y")) {
				coreMediaBasicInfo1 =coreMediaBasicInfo3;
			}
		}
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		
		for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
			if (InvalidReasonStatus.PNA.getValue().equals(coreMediaBasicInfo2.getInvalidReason())) {
				// 该卡有效期大于当前有效媒介的有效期
				String mmyy1 = coreMediaBasicInfo1.getExpirationDate();
				String yyyymm1 =  coreSystemUnit.getNextProcessDate().substring(0, 2) + mmyy1.substring(2, 4) +mmyy1.substring(0, 2);
				String mmyy2 = coreMediaBasicInfo2.getExpirationDate();
				String yyyymm2 =  coreSystemUnit.getNextProcessDate().substring(0, 2) + mmyy2.substring(2, 4) +mmyy2.substring(0, 2);
				Integer  i = yyyymm1.compareTo(yyyymm2);
				if (i < 0 ) {
					throw new BusinessException("CUS-00086");
				}
			}
			if (InvalidReasonStatus.RNA.getValue().equals(coreMediaBasicInfo2.getInvalidReason())) {
				// 该卡有效期大于当前有效媒介的有效期
				String mmyy1 = coreMediaBasicInfo1.getExpirationDate();
				String yyyymm1 = coreSystemUnit.getNextProcessDate().substring(0, 2)+ mmyy1.substring(2, 4) +mmyy1.substring(0, 2);
				String mmyy2 = coreMediaBasicInfo2.getExpirationDate();
				String yyyymm2 = coreSystemUnit.getNextProcessDate().substring(0, 2)+ mmyy2.substring(2, 4) +mmyy2.substring(0, 2);
				Integer  i = yyyymm1.compareTo(yyyymm2);
				if (i < 0 ) {
					throw new BusinessException("CUS-00108");
				}
			}
		}
		// 如果系统日期YYMM>媒介有效期，报错‘媒介已过期’
		// 当前处理日期
		String currProcessDate = coreSystemUnit.getCurrProcessDate();
		String yyyyMMdd = this.getExpirationDate(currProcessDate, coreMediaBasicInfo);
		double sumTotal = DateUtil.daysBetween(currProcessDate, yyyyMMdd, "yyyy-MM-dd");
		if (sumTotal <= 0) {
			throw new BusinessException("CUS-00072");
		}
		if (InvalidReasonStatus.PNA.getValue().equals(coreMediaBasicInfo.getInvalidReason())) {
			throw new BusinessException("CUS-00073");
		}
		if (InvalidReasonStatus.RNA.getValue().equals(coreMediaBasicInfo.getInvalidReason())) {
			throw new BusinessException("CUS-00074");
		}
		if (!"1".equals(coreMediaBasicInfo.getActivationFlag())) {
			throw new BusinessException("CUS-00082");
		}
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
		// 修改媒介状态
		// int result =
		// coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo,
		// coreMediaBasicInformationSqlBuilderStr);
		// if (result != 1) {
		// throw new BusinessException("CUS-00075", "媒介单元");
		// }
		// 记录媒介日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5800bo.getEventNo(), x5800bo.getActivityNo(),
				ModificationType.UPD.getValue(), null, coreMediaBasicInfoAfter, coreMediaBasicInfo,
				coreMediaBasicInfo.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
				coreMediaBasicInfo.getMainCustomerNo(), coreMediaBasicInfo.getMediaUnitCode(), null, null);

		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
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
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		coreProductFormSqlBuilder.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
		CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
		eventCommAreaNonFinance.setProductForm(coreProductForm.getProductForm());
		// 查询出 凸字信息
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDao.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		if (null != coreMediaCardInfo) {
			CachedBeanCopy.copyProperties(coreMediaCardInfo, eventCommAreaNonFinance);
		}
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(eventCommAreaNonFinance.getOperationMode(),
				eventCommAreaNonFinance.getMediaObjectCode());
		if (null == coreMediaObject) {
			throw new BusinessException("CUS-00014", "媒介对象表");// 机构表
		}
		String institutionId = coreMediaBasicInfo.getInstitutionId();
		CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);
		if (coreOrgan == null) {
			throw new BusinessException("CUS-00014", "机构表");// 机构表
		}
		// 国家码
		eventCommAreaNonFinance.setCountry(coreOrgan.getCountry());
		// 服务代码
		eventCommAreaNonFinance.setServiceCode(coreMediaObject.getServiceCode());
		// 媒介类型
		eventCommAreaNonFinance.setMediaObjectType(coreMediaObject.getMediaObjectType());
		// 转卡标志
		eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.PNA.getValue());
		eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.PNA.getValue());
		// 转出媒介单元代码
		eventCommAreaNonFinance.setTransferMediaCode(coreMediaBasicInfo.getMediaUnitCode());
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
		eventCommAreaNonFinance.setOperationDate(coreSystemUnit.getNextProcessDate());
		eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
		eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
		eventCommAreaNonFinance.setOperatorId(operatorId);
		eventCommAreaNonFinance.setCurrProcessDate(currProcessDate);
		eventCommAreaNonFinance.setNextProcessDate(coreSystemUnit.getNextProcessDate());
		eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
		eventCommAreaNonFinance.setExpirationDate(coreMediaBasicInfo.getExpirationDate());
		
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
		authEventCommAreaNonFinanceBean.setInvalidReasonOld("");
		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
		eventCommAreaTriggerEventList.add(triggerEventParams);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		
		return eventCommAreaNonFinance;
	}

	/**
	 * 处理筛选出需要到期换卡的数据
	 * 
	 */
	private boolean reissueCardDate(CoreMediaBasicInfo coreMediaBasicInfo, List<CoreActivityArtifactRel> artifactList)
			throws Exception {
		/*
		 * 查询媒介对应的到期换卡PCD，即查询PCD实例表中运营模式为媒介单元基本信息中的运营模式，
		 * 构件实例代码1为媒介对象代码，pcd编号为“305AAA01”的数据。该PCD为月数，代表有效期前N个月换卡。
		 * 媒介单元基本信息表中的有效期格式为yyMM，需将年份前两位用系统当前处理日期的年份前两位补全，
		 * 将日期后两位用媒介单元基本信息中的新建日后两位dd补全，然后计算出一个yyyyMMdd格式的有效日期，用有效日期减去PCD月数，
		 * 即是到期换卡日期。
		 */
		String operationMode = coreMediaBasicInfo.getOperationMode();
		String mediaObjectCode = coreMediaBasicInfo.getMediaObjectCode();
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(operationMode);
		eventCommArea.setEcommMediaObjId(mediaObjectCode);
		String month = getReissueMonth(eventCommArea, artifactList);
		if (StringUtil.isBlank(month)) {
			logger.info("该卡不需要执行到期换卡操作， 媒介对象代码 > " + coreMediaBasicInfo.getMediaObjectCode() + ", 外部识别号  >"
					+ coreMediaBasicInfo.getExternalIdentificationNo());
			return false;
		}
		String MMYY = coreMediaBasicInfo.getExpirationDate();
		String YY = MMYY.substring(2, 4);
		String MM = MMYY.substring(0, 2);
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		String customerNo = coreMediaBasicInfo.getMainCustomerNo();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);

		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		// 当前营业日期
		String currProcessDate = coreSystemUnit.getCurrProcessDate();
		// 上一处理日
		String lastProcessDate = coreSystemUnit.getLastProcessDate();
		String YY1 = currProcessDate.substring(0, 2);
		// 新建日期 2020-02-26
		String createDate = coreMediaBasicInfo.getCreateDate();
		String dd = createDate.substring(8, 10);
		// 到期换卡日期
		String yyyyMMdd = YY1 + YY + "-" + MM + "-" + dd;
		// 2，用系统当前处理日期和第一步中计算出的到期换卡日期相比较。只要到期换卡日期小于等于系统当前处理日期的媒介，都需要换卡。
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reissueDate = sdf.parse(yyyyMMdd);
		Calendar ca = Calendar.getInstance();
		ca.setTime(reissueDate);
		ca.add(Calendar.MONTH, -Integer.valueOf(month));
		Date startDate = ca.getTime();
		String startDateStr = sdf.format(startDate);
		@SuppressWarnings("unused")
		double sumTotal1 = DateUtil.daysBetween(startDateStr, currProcessDate, "yyyy-MM-dd");
		@SuppressWarnings("unused")
		double sumTotal2 = DateUtil.daysBetween(startDateStr, lastProcessDate, "yyyy-MM-dd");
		// 根据媒介单元代码查询封锁码管控视图中层级代码为媒介单元代码的数据，若查到元件代码为“601AAA0101”的数据，则换卡失败，否则可以换卡
		return true;
	}

	/**
	 * 
	 * @Description: 查询到期换卡有效期前N个月
	 * @param eventCommArea
	 * @param artifactList
	 * @return
	 * @throws Exception
	 */
	private String getReissueMonth(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList)
			throws Exception {
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_305, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> resultPcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_305, eventCommArea);
		Iterator<Map.Entry<String, String>> it = resultPcdResultMap.entrySet().iterator();
		String termValidity = "";
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.REISSUE_CHANGE_CARD.equals(entry.getKey())) {
				// 获取 PCD为月数，代表有效期前N个月换卡
				termValidity = entry.getValue().toString();
			} else if (Constant.REISSUE_CHANGE_CARD_NOT.equals(entry.getKey())) {
				termValidity = "";
			}
		}
		return termValidity;
	}

	private String getExpirationDate(String currProcessDate, CoreMediaBasicInfo coreMediaBasicInfo) {
		String createDate = coreMediaBasicInfo.getCreateDate();
		String MMYY = coreMediaBasicInfo.getExpirationDate();
		String YY = MMYY.substring(2, 4);
		String MM = MMYY.substring(0, 2);
		String YY1 = currProcessDate.substring(0, 2);
		String dd = createDate.substring(8, 10);
		// 到期换卡有效期
		String yyyyMMdd = YY1 + YY + "-" + MM + "-" + dd;
		return yyyyMMdd;
	}

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String yyyyMMdd = "2018-12-06";
		String currProcessDate = "2018-12-07";
		Date reissueDate = sdf.parse(yyyyMMdd);
		Date cprocessDate = sdf.parse(currProcessDate);
		boolean flag = reissueDate.before(cprocessDate);
		System.out.println(flag);
		if (flag) {
			System.out.println(flag);
		}
	}

}
