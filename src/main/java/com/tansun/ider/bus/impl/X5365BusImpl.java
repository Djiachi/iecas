package com.tansun.ider.bus.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5365Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.BatchDateProcessType;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5365BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BatchUtil;
import com.tansun.ider.util.CardUtils;

/**
 * @version:1.0
 * @Description: 到期换卡筛选条件
 * @author: admin
 */
@Service
public class X5365BusImpl implements X5365Bus {

	private static Logger logger = LoggerFactory.getLogger(X5365BusImpl.class);
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private BatchUtil batchUtil;
	@Value("${global.target.service.url.nofn}")
	private String authUrl;
	@Autowired
	private HttpQueryService httpQueryService;
	
	@Override
	public Object busExecute(X5365BO x5365bo) throws Exception {
		List<CoreActivityArtifactRel> artifactList = x5365bo.getActivityArtifactList();
		//到期换卡仅保存对单个媒介单元的处理
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder1.orInvalidReasonIsNull();
		coreMediaBasicInfoSqlBuilder1.orInvalidReasonEqualTo(InvalidReasonStatus.PNA.getValue());
		coreMediaBasicInfoSqlBuilder1.orInvalidReasonEqualTo(InvalidReasonStatus.RNA.getValue());
		coreMediaBasicInfoSqlBuilder1.orInvalidReasonEqualTo("");
		coreMediaBasicInfoSqlBuilder.and(coreMediaBasicInfoSqlBuilder1);
		//获取外部识别号
		String externalIdentificationNo = x5365bo.getExternalIdentificationNo();
		if(externalIdentificationNo ==null || externalIdentificationNo.length()==0){
			throw new BusinessException("无法获取外部识别号");
		}
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(null == coreMediaBasicInfoList || coreMediaBasicInfoList.isEmpty()){
			throw new BusinessException("CUS-00085");
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		try {
			CoreMediaBasicInfo coreMediaBasicInfo1 = null;
			for (CoreMediaBasicInfo coreMediaBasicInfo : coreMediaBasicInfoList) {
				if (coreMediaBasicInfo.getInvalidFlag().equals("Y")) {
					coreMediaBasicInfo1 =coreMediaBasicInfo;
				}
			}
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo1.getMainCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			boolean flag2 = false;
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : coreMediaBasicInfoList) {
				// 无效原因为RNA || PNA
				if (InvalidReasonStatus.RNA.getValue().equals(coreMediaBasicInfo2.getInvalidReason()) || 
						InvalidReasonStatus.PNA.getValue().equals(coreMediaBasicInfo2.getInvalidReason())) {
					//判断有效期的值
					String mmyy1 = coreMediaBasicInfo1.getExpirationDate();
					String yyyymm1 = coreSystemUnit.getNextProcessDate().substring(0, 2) + mmyy1.substring(2, 4) +mmyy1.substring(0, 2);
					String mmyy2 = coreMediaBasicInfo2.getExpirationDate();
					String yyyymm2 = coreSystemUnit.getNextProcessDate().substring(0, 2) + mmyy2.substring(2, 4) +mmyy2.substring(0, 2);
					Integer  i = yyyymm1.compareTo(yyyymm2);
					if (i < 0 ) {
						flag2= true;
						break;
					}	
				}
			}
			// 不执行到期换卡操作
			if (!flag2) {
				// 到期换卡日期
				boolean isFlag = reissueCardDate(coreMediaBasicInfo1, artifactList);
				if (isFlag) {
					// 执行到期换卡操作
					eventCommAreaNonFinance.setMediaUnitCode(coreMediaBasicInfo1.getMediaUnitCode());
					eventCommAreaNonFinance.setOperationMode(coreMediaBasicInfo1.getOperationMode());
					eventCommAreaNonFinance.setEventNo("ISS.OP.01.0014");
					eventCommAreaNonFinance
							.setExternalIdentificationNo(coreMediaBasicInfo1.getExternalIdentificationNo());
					eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.EXP.getValue());
					batchUtil.triggerEventNonFinance(eventCommAreaNonFinance, authUrl);
				} else {
					eventCommAreaNonFinance.setWhetherProcess("1");
					logger.info("改卡不需要执行到期换卡操作， 媒介对象代码 > " + coreMediaBasicInfo1.getMediaObjectCode()
							+ ", 外部识别号  >" + coreMediaBasicInfo1.getExternalIdentificationNo());
				}
			}else {
				eventCommAreaNonFinance.setWhetherProcess("1");
				logger.info("改卡不需要执行到期换卡操作， 媒介对象代码 > " + coreMediaBasicInfo1.getMediaObjectCode()
						+ ", 外部识别号  >" + coreMediaBasicInfo1.getExternalIdentificationNo());
			}
			
			
		} catch (Exception e) {
			logger.error("ReissueChangeCard处理异常", e);
		}
		
		return eventCommAreaNonFinance;
	}

	/**
	 * 处理筛选出需要到期换卡的数据
	 * 
	 * @param coreCustomer
	 * @throws Exception
	 */
	private boolean reissueCardDate(CoreMediaBasicInfo coreMediaBasicInfo, List<CoreActivityArtifactRel> artifactList)
			throws Exception {
		// 到期换卡日期计算
		// a) 查询媒介对应的到期换卡PCD，即查询PCD实例表中运营模式为媒介单元基本信息中的运营模式，
		// 构件实例代码1为媒介对象代码，pcd编号为“305AAA01”的数据。该PCD为月数，
		// 代表有效期前N个月换卡。
		// b) 媒介单元基本信息表中的有效期格式为yyMM，需将年份前两位用系统当前处理日期的年份前两位补全，
		// 将日期后两位用媒介单元基本信息中的新建日后两位dd补全，然后计算出一个yyyyMMdd格式的有效日期，
		// 用有效日期减去PCD月数，即是到期换卡日期。
		String operationMode = coreMediaBasicInfo.getOperationMode();
		String mediaObjectCode = coreMediaBasicInfo.getMediaObjectCode();
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(operationMode);
		eventCommArea.setEcommMediaObjId(mediaObjectCode);
		String month = getReissueMonth(eventCommArea, artifactList);
		if (StringUtil.isBlank(month)) {
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
		String currProcessDate = coreSystemUnit.getCurrProcessDate();
		// 上一处理日
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
		CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
		if (BatchDateProcessType.BWD.getValue().equals(coreOperationMode.getBatchDateProcessType())) {
			double sumTotal1 = DateUtil.daysBetween(startDateStr, coreSystemUnit.getCurrProcessDate(), "yyyy-MM-dd");
			double sumTotal2 = DateUtil.daysBetween(startDateStr, coreSystemUnit.getLastProcessDate(), "yyyy-MM-dd");
			if ((sumTotal1 > 0 || sumTotal1 ==0 ) && (sumTotal2 < 0 )) {
				return true;
			}else {
				return false; 
			}
		}else if (BatchDateProcessType.FWD.getValue().equals(coreOperationMode.getBatchDateProcessType())) {
			double sumTotal1 = DateUtil.daysBetween(startDateStr, coreSystemUnit.getNextProcessDate(), "yyyy-MM-dd");
			double sumTotal2 = DateUtil.daysBetween(startDateStr, coreSystemUnit.getCurrProcessDate(), "yyyy-MM-dd");
			if ((sumTotal1 > 0 || sumTotal1 == 0 ) && (sumTotal2 < 0 )) {
				return true;
			}else {
				return false;
			}
		}
		// 3，媒介的状态，激活状态，封锁码限制。
		// b) 查询媒介单元基本信息表中的激活状态为已激活的媒介
		// c)
		// 根据媒介单元代码查询封锁码管控视图中层级代码为媒介单元代码的数据，若查到元件代码为“601AAA0101”的数据，则换卡失败，否则可以换卡
		if (!coreMediaBasicInfo.getActivationFlag().equals("1")) {
			return false;
		}
		// 判读是否已经到期换卡，新卡未激活
		return false;
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

	public static void main(String[] args) throws ParseException {
		String yy = "2021-07-15".substring(0, 2);;
		System.out.println(yy);
	}

}
