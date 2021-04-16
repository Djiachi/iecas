package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5040Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.beta.sqlbuilder.CoreProductObjectSqlBuilder;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.impl.CoreCustomerBillDayDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.DirectDebitStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.BusinessProgramNoCycleDays;
import com.tansun.ider.model.bo.X5040BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 客户业务项目新建
 * @author: admin
 */
@Service
public class X5040BusImpl implements X5040Bus {

	// 505AAA0103标识
	public boolean zeroElemnetflag = false;
	
	@Autowired
	private CoreCustomerBillDayDaoImpl coreCustomerBillDayDaoImpl;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private OperationModeUtil operationModeUtil;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryServiceImpl httpQueryServiceImpl;
	
	
	@Override
	public Object busExecute(X5040BO x5040bo) throws Exception {
		
		//判断是是否新增产品
		if (StringUtil.isNotBlank(x5040bo.getIsNew())) {
			if ("2".equals(x5040bo.getIsNew())) {
				return x5040bo;
			}
		}
		
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5040bo, eventCommAreaNonFinance);
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		String operationMode = eventCommAreaNonFinance.getOperationMode();
		String productObjectCode = x5040bo.getProductObjectCode();
		String operatorId = x5040bo.getOperatorId();
		// 产品账单日，产品线构件编号
		List<CoreActivityArtifactRel> artifactList = x5040bo.getActivityArtifactList();
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreProductObjectSqlBuilder coreProductObjectSqlBuilder = new CoreProductObjectSqlBuilder();
		coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		// 运营模式
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommOperMode(operationMode);
		// 业务项目代码
		List<CoreProductBusinessScope> listCoreProductBusinessScopes = 
				httpQueryServiceImpl.queryProductBusinessScope(eventCommAreaNonFinance.getProductObjectCode(), eventCommAreaNonFinance.getOperationMode());
		
		// 根据产品对象代码查询出业务项目代码
		/**
		 * 检查业务项目代码,输入是否符合规则
		 */
		if (DirectDebitStatus.SETUP.getValue().equals(eventCommAreaNonFinance.getDirectDebitStatus())) {
			if (StringUtil.isBlank(eventCommAreaNonFinance.getDirectDebitMode())
					|| StringUtil.isBlank(eventCommAreaNonFinance.getDirectDebitAccountNo())
					|| StringUtil.isBlank(eventCommAreaNonFinance.getDirectDebitBankNo())) {
				throw new BusinessException("CUS-00040"); // 约定扣款方式、约定扣款状态、约定扣款账号、约定扣款银行号
			}
		}
		if (operatorId == null) {
			operatorId = "system";
		}
		List<CoreCustomerBillDay> listCoreCustomerBillDays = new ArrayList<CoreCustomerBillDay>();
		for (CoreProductBusinessScope coreProductBusinessScope : listCoreProductBusinessScopes) {
			
			CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
			coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
			coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(coreProductBusinessScope.getBusinessProgramNo());
			CoreCustomerBillDay coreCustomerBillDayStr = coreCustomerBillDayDaoImpl
					.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
			eventCommArea.setEcommBusinessProgramCode(coreProductBusinessScope.getBusinessProgramNo());
			//检查是否是公务卡产品，公务卡产品账单日跟随预算单位设定
			Integer officialBillDay = checkOfficialCard(eventCommArea, artifactList, x5040bo);
			if (coreCustomerBillDayStr != null&&officialBillDay==null) {
				break;
			}else if(coreCustomerBillDayStr != null&&officialBillDay!=null){
				//只需更新一下账单日即可
				coreCustomerBillDaySqlBuilder.andVersionEqualTo(coreCustomerBillDayStr.getVersion());
				coreCustomerBillDayStr.setBillDay(officialBillDay);
				coreCustomerBillDayStr.setVersion(coreCustomerBillDayStr.getVersion()+1);
				int i = coreCustomerBillDayDaoImpl.updateBySqlBuilder(coreCustomerBillDayStr, coreCustomerBillDaySqlBuilder);
				if(i!=1){
					throw new BusinessException("修改公务卡产品账单日失败");
				}
				break;
			}
			// 创建产品统一信息表
			CoreCustomerBillDay coreCustomerBillDay = new CoreCustomerBillDay();
			coreCustomerBillDay.setId(RandomUtil.getUUID());
			coreCustomerBillDay.setBusinessProgramNo(coreProductBusinessScope.getBusinessProgramNo());
			coreCustomerBillDay.setDirectDebitAccountNo(eventCommAreaNonFinance.getDirectDebitAccountNo());
			coreCustomerBillDay.setDirectDebitBankNo(eventCommAreaNonFinance.getDirectDebitBankNo());
			coreCustomerBillDay.setDirectDebitMode(eventCommAreaNonFinance.getDirectDebitMode());
			coreCustomerBillDay.setDirectDebitStatus(eventCommAreaNonFinance.getDirectDebitStatus());
			coreCustomerBillDay.setExchangePaymentFlag(eventCommAreaNonFinance.getExchangePaymentFlag());
			coreCustomerBillDay.setCurrentCycleNumber(1);
			coreCustomerBillDay.setCustomerNo(customerNo);
			coreCustomerBillDay.setGmtCreate(eventCommAreaNonFinance.getCurrProcessDate());
			coreCustomerBillDay.setVersion(1);
			CoreSystemUnit coreSystemUnit1 = operationModeUtil.getcoreOperationMode(customerNo);
			String operationDate = coreSystemUnit1.getNextProcessDate();
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			Integer billDay=null;
			if(officialBillDay!=null){
				//公务卡产品，拿预算单位的账单日赋值
				billDay = officialBillDay;
				coreCustomerBillDay.setBillDay(billDay);
			}else{
				// 获取 505构件
				billDay = getBillDay(eventCommAreaNonFinance, artifactList, coreCustomer,x5040bo,coreProductBusinessScope.getBusinessProgramNo());
				coreCustomerBillDay.setBillDay(billDay);
			}
			if (null != billDay) {
				eventCommAreaNonFinance.setBillDay(billDay.toString());
			}else {
				eventCommAreaNonFinance.setBillDay(null);
			}
			eventCommAreaNonFinance.setOperationDate(operationDate);
			// 获取下一账单日
			String nextBillDate = null;
			//实例是505AAA0102时,账单日和下一张单日赋值为0。
			if(zeroElemnetflag){
				nextBillDate = "0";
				zeroElemnetflag = false;
			}else{
				nextBillDate = getNextBillDate(eventCommAreaNonFinance, artifactList);
			}
			eventCommAreaNonFinance.setNextBillDate(nextBillDate);
			// 下一账单日期
			coreCustomerBillDay.setNextBillDate(nextBillDate);
			
			@SuppressWarnings("unused")
			int result = coreCustomerBillDayDaoImpl.insert(coreCustomerBillDay);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5040bo.getEventNo(), x5040bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreCustomerBillDay, coreCustomerBillDay.getId(),
					eventCommAreaNonFinance.getCurrLogFlag(), operatorId, coreCustomerBillDay.getCustomerNo(),
					coreCustomerBillDay.getBusinessProgramNo(), null,null);
			listCoreCustomerBillDays.add(coreCustomerBillDay);
		}
		eventCommAreaNonFinance.setListCoreCustomerBillDays(listCoreCustomerBillDays);
		return eventCommAreaNonFinance;
	}

	/**
	 * 
	 * @Description: 获取账单日
	 * @param eventCommArea
	 * @param artifactList
	 * @param coreCustomer
	 * @return
	 * @throws Exception
	 */
	private Integer getBillDay(EventCommAreaNonFinance eventCommAreaNonFinance, List<CoreActivityArtifactRel> artifactList,
			CoreCustomer coreCustomer,X5040BO x5040bo,String businessProgramNo) throws Exception {
		// 转换list
		List<BusinessProgramNoCycleDays> businessProgramNoCycleDaysList = eventCommAreaNonFinance.getBusinessProgramNoCycleDaysList();
		// 账单处理
		Map<String, String> businessProgramNoCycleDaysMap = new HashMap<>();
		if (null !=businessProgramNoCycleDaysList && !businessProgramNoCycleDaysList.isEmpty()) {
	        for (BusinessProgramNoCycleDays businessProgramNoCycleDays : businessProgramNoCycleDaysList) {
	        	businessProgramNoCycleDaysMap.put(businessProgramNoCycleDays.getBusinessProgramNo(), businessProgramNoCycleDays.getCycleFrequencyDay());
	        }
		}
		Integer billDay = null;
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_505, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_505, eventCommAreaNonFinance.getEventCommArea());
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.ACC_DATE_GENERATE_N1.equals(entry.getKey())) { // 505AAA0100
				// 获取客户信息表表中账单日
				billDay = Integer.valueOf(coreCustomer.getBillDay());
			} else if (Constant.ACC_DATE_GENERATE_N2.equals(entry.getKey())) { // 505AAA0101
				// 获取账单日为，新增产品创建账单日
				String cycleFrequencyDay = businessProgramNoCycleDaysMap.get(businessProgramNo);
				if (StringUtil.isNotBlank(cycleFrequencyDay)) {
					billDay = Integer.valueOf(cycleFrequencyDay);
				}else {
					throw new BusinessException("CUS-00103",businessProgramNo);
				}
			}else if (Constant.ACC_DATE_GENERATE_ZERO.equals(entry.getKey()) || Constant.ACC_DATE_GENERATE_Y.equals(entry.getKey())) { // 505AAA0102
				// 账单日、下一账单日赋值为0
				zeroElemnetflag = true;
				billDay = 0;
			}
		}
		if(null==billDay){
			throw new BusinessException("CUS-00079",BSC.ARTIFACT_NO_505);
		}
		return billDay;
	}
	/**
	 * 检查是否是公务卡产品，如果是，获取账单日跟随预算单位账单日
	 * @Description: TODO()   
	 * @param: @param eventCommArea
	 * @param: @param artifactList
	 * @param: @param x5040bo
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Integer      
	 * @throws
	 */
	private Integer checkOfficialCard(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList,X5040BO x5040bo) throws Exception{
		Integer billDay = null;
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_501, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_501, eventCommArea);
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.OFFICIAL_CARD.equals(entry.getKey())) { // 501AAA0103
				//公务卡产品，从预算单位记录获取账单日，以保证个人单位公务卡与预算单位中的账单日一致
				billDay = getOfficalCardBillDay(x5040bo);
			}
		}
		return billDay;
	}
	/**
	 * 获取预算单位账单日
	 * @Description: TODO()   
	 * @param: @param x5040bo
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Integer      
	 * @throws
	 */
	private Integer getOfficalCardBillDay(X5040BO x5040bo) throws Exception{
		String idNumber = x5040bo.getBudgetOrgCode();
		if(StringUtil.isNotBlank(idNumber)){
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if(coreCustomer!=null){
				return Integer.parseInt(coreCustomer.getBillDay());
			}else{
				throw new BusinessException("获取不到预算单位！");
			}
		}else{
			throw new BusinessException("获取不到预算单位编码！");
		}
	}
	/**
	 * 
	 * @Description: 获取下一账单日
	 * @param eventCommArea
	 * @param artifactList
	 * @param coreCustomer
	 * @return
	 * @throws Exception
	 */
	private String getNextBillDate(EventCommAreaNonFinance eventCommAreaNonFinance, List<CoreActivityArtifactRel> artifactList)
			throws Exception {
		// 506 构件
		// 按月 元件编号1 下一处理日 + PCD
		// 按周 元件编号2 下一处理日 +( PCD * 7 )
		String nextBillDate = null;
		String nextProcessDate  = eventCommAreaNonFinance.getNextProcessDate();
		String billDay = eventCommAreaNonFinance.getBillDay();
		String pcd = null;
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_506, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommAreaNonFinance.getEventCommArea());
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) { // 506AAA0100
				pcd = entry.getValue().toString().trim();
				nextBillDate = getNextBillDatePcd(nextProcessDate, pcd,billDay);
			} else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) { // 506AAA0101
				BigDecimal result =new BigDecimal(entry.getValue().toString().trim());
				nextBillDate = getNextBillDateWeekPcd(nextProcessDate, result,billDay);
			}
		}
		return nextBillDate;
	}

	
	/**
	 * 账单日- 账单按照周格式
	 * @param operationDate
	 * @param pcd
	 * @param billDay
	 * @return
	 * @throws ParseException 
	 * @throws Throwable 
	 */
	private static String getNextBillDateWeekPcd(String operationDate, BigDecimal pcd,String billDay) throws ParseException {
		String weekDayInt = dayForWeek(operationDate);
		int weekDay = Integer.valueOf(weekDayInt);
		//比较下一处理日是周几,然后输入的今天是星期几比较
		int billDays = Integer.valueOf(billDay);
		if (weekDay > billDays) {
			//往前加 pcd周
			int xDay = weekDay - billDays;
			String operationBillDay = beforeOrAfterNumberDay(operationDate, -xDay);
			return dayOfWeeks(operationBillDay,pcd.intValue());
		}else if (weekDay < billDays) {
			//往后加 pcd -1周
			int xDay = weekDay - billDays;
			String operationBillDay = beforeOrAfterNumberDay(operationDate, -xDay);
			return dayOfWeeks(operationBillDay, pcd.intValue()-1);
		}else {
			return operationDate;
		}
	}
	
	@SuppressWarnings("static-access")
	public static String dayOfWeeks(String operationBillDay ,int weeks) throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(operationBillDay);
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
		cal.add(cal.WEEK_OF_MONTH, weeks);
		Date date1 = cal.getTime();
		String operationDate = df.format(date1);
		return operationDate;
	}
	
	public static String beforeOrAfterNumberDay(String operationDate, int day) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(operationDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 当前运营日期+PCD值
	 * 
	 * @Description:
	 * @param nextBillDate
	 * @param operationDate
	 * @param pcd
	 * @throws ParseException
	 */
	private String getNextBillDatePcd(String operationDate, String pcd,String billDay) throws ParseException {
		
		String nowDateStr = operationDate.substring(0,8);
		Integer operationBillDay = Integer.valueOf(operationDate.substring(8,10));
		Integer nowBillDay = Integer.valueOf(billDay);
		if (billDay.length()==1) {
			billDay = "0".concat(billDay);
		}
		nowDateStr = nowDateStr+billDay;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (-1==nowBillDay.compareTo(operationBillDay)) {
			// 增加一个月
			Date date = sdf.parse(nowDateStr);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.add(Calendar.MONTH, Integer.valueOf(pcd)); 
			String nextBillDate = sdf.format(rightNow.getTime());
			return nextBillDate;
		}
		return nowDateStr;
	}
	
	/**
	 * 判断当前日期是星期几
	 * @param pTime
	 * @return
	 * @throws ParseException 
	 * @throws Throwable
	 */
	@SuppressWarnings("deprecation")
	public static String dayForWeek(String pTime) throws ParseException   {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        Date tmpDate = format.parse(pTime);  
        Calendar cal = Calendar.getInstance(); 
        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        try {
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
	}
	
	public static void main(String[] args) throws Throwable {
	  String operationDate = dayForWeek("2019-06-02");
	  System.out.println(operationDate);
	}

}
