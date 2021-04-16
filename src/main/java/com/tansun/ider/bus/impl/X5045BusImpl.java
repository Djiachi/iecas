package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5045Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerUnifyInfoDaoImpl;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5045BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 产品线统一日期表
 * @author: admin
 */
@Service
public class X5045BusImpl implements X5045Bus {

	private static Logger logger = LoggerFactory.getLogger(X5045BusImpl.class);
	@Resource
	private CoreCustomerUnifyInfoDaoImpl coreCustomerUnifyInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	
	@SuppressWarnings("unused")
	@Override
	public Object busExecute(X5045BO x5045bo) throws Exception {
		//判断是是否新增产品
		if (StringUtil.isNotBlank(x5045bo.getIsNew())) {
			if ("2".equals(x5045bo.getIsNew())) {
				return x5045bo;
			}
		}
		
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5045bo, eventCommAreaNonFinance);
		Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
		String operatorId = x5045bo.getOperatorId();
		// 产品账单日，产品线构件编号
		List<CoreActivityArtifactRel> artifactList = x5045bo.getActivityArtifactList();
//		CoreOperationModeSqlBuilder coreOperationModeSqlBuilder = new CoreOperationModeSqlBuilder();
//		coreOperationModeSqlBuilder.andOperationModeEqualTo(eventCommAreaNonFinance.getOperationMode());
//		CoreOperationMode coreOperationMode = coreOperationModeDaoImpl.selectBySqlBuilder(coreOperationModeSqlBuilder);
		CoreOperationMode coreOperationMode =  httpQueryService.queryOperationMode(eventCommAreaNonFinance.getOperationMode());
		List<CoreCustomerBillDay> listCoreCustomerBillDays = eventCommAreaNonFinance.getListCoreCustomerBillDays();
		if (operatorId == null) {
			operatorId = "system";
		}
		if (listCoreCustomerBillDays != null && !listCoreCustomerBillDays.isEmpty()) {
			for (CoreCustomerBillDay coreCustomerBillDay : listCoreCustomerBillDays) {
				CoreCustomerUnifyInfo coreCustomerUnifyInfo = new CoreCustomerUnifyInfo();
				coreCustomerUnifyInfo.setId(RandomUtil.getUUID());
				coreCustomerUnifyInfo.setCustomerNo(eventCommAreaNonFinance.getCustomerNo());
				coreCustomerUnifyInfo.setCycleNumber(00);
				// 查询运营日期 + 账单日期 = 本次账单日期
				String billDay = coreCustomerBillDay.getBillDay().toString();
				String statementDate = coreCustomerBillDay.getNextBillDate();
				coreCustomerUnifyInfo.setStatementDate(statementDate);
				// 运营模式
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommOperMode(eventCommAreaNonFinance.getOperationMode());
				// 通过账单日计算最后还款日期
				eventCommArea.setEcommBusinessProgramCode(coreCustomerBillDay.getBusinessProgramNo());
				eventCommAreaNonFinance.setEventCommArea(eventCommArea);
				String paymentDueDate = getPaymentDueDate(eventCommAreaNonFinance, artifactList);
				coreCustomerUnifyInfo.setPaymentDueDate(paymentDueDate);
				coreCustomerUnifyInfo.setBusinessProgramNo(coreCustomerBillDay.getBusinessProgramNo());
				coreCustomerUnifyInfo.setVersion(1);
				eventCommArea.setEcommBusinessProgramCode(coreCustomerBillDay.getBusinessProgramNo());
				eventCommAreaNonFinance.setEventCommArea(eventCommArea);
				CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		        // 计算下一账单日
		        Map<String, String> nextBillDateMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommAreaNonFinance.getEventCommArea());
		        String currentBillDate = coreCustomerBillDay.getNextBillDate();
		        String nextBillDate = null;
		        String graceDate = null;
		        String lateDate = null;
		        String repayDate = null;
		        if("0".equals(currentBillDate)){
		        	  nextBillDate = "0";
		        	  graceDate = "0";
		        	  lateDate = "0";
		        	  repayDate = "0";
		        }else{
		        	nextBillDate = calNextBillDate(nextBillDateMap, currentBillDate, coreCustomerBillDay.getBillDay());
		        	
		        	// 查询502元件信息
		        	Map<String, String> repayDateMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_502, eventCommAreaNonFinance.getEventCommArea());
		        	// 计算还款日
		        	repayDate = calNextEndDate(repayDateMap, currentBillDate, nextBillDate, coreCustomerBillDay.getBillDay(),
		        			eventCommAreaNonFinance);
		        	// 查询503元件信息
		        	Map<String, String> graceDateMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_503, eventCommAreaNonFinance.getEventCommArea());
		        	// 计算宽限日
		        	graceDate = calNextGraceDate(graceDateMap, repayDate, currentBillDate);
		        	// 查询504元件信息
		        	Map<String, String> lastDateMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_504, eventCommAreaNonFinance.getEventCommArea());
		        	// 计算延滞日
		        	lateDate = calNextLateDate(lastDateMap, repayDate);
		        }
		        coreCustomerUnifyInfo.setPaymentDueDate(repayDate);
		        coreCustomerUnifyInfo.setGraceDate(graceDate);
		        coreCustomerUnifyInfo.setDelinquencyDate(lateDate);
				int result = coreCustomerUnifyInfoDaoImpl.insert(coreCustomerUnifyInfo);
				
				nonFinancialLogUtil.createNonFinancialActivityLog(x5045bo.getEventNo(), x5045bo.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreCustomerUnifyInfo, coreCustomerUnifyInfo.getId(),
						eventCommAreaNonFinance.getCurrLogFlag(), operatorId, coreCustomerUnifyInfo.getCustomerNo(),
						coreCustomerUnifyInfo.getBusinessProgramNo(), null,null);
			}
		}
		eventCommAreaNonFinance.setMainCustomerNo(eventCommAreaNonFinance.getCustomerNo());
		return eventCommAreaNonFinance;
	}
	
	 /**
     * 计算下一迟缴日
     * 
     * @param resultMap
     *            元件信息
     * @param nextEndDate
     *            下一到期日
     * @return
     * @throws Exception
     */
    public String calNextLateDate(Map<String, String> resultMap, String nextEndDate) throws Exception {
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String paymentDueDate = null;
        // 最低还款额
        Integer pcdValue = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.ACC_DATE_GENERATE_LATE.equals(entry.getKey())) {
                paymentDueDate = entry.getKey();
                if (StringUtil.isNotBlank(entry.getValue())) {
                	 pcdValue = Integer.parseInt(entry.getValue());
				}else {
					pcdValue =0;
				}
               
            }
        }
        if (StringUtil.isBlank(paymentDueDate) || StringUtil.isEmpty(paymentDueDate)) {
            throw new BusinessException("COR-10003", BSC.ARTIFACT_NO_504);
        }
        // 计算下一迟缴日
        String nextGraceDate = null;
        if (Constant.ACC_DATE_GENERATE_LATE.equals(paymentDueDate)) {
            nextGraceDate = DateUtil.offset(nextEndDate, DateUtil.FORMAT_DATE, Calendar.DAY_OF_YEAR, pcdValue);
        }
        return nextGraceDate;
    }
	
	
	 /**
     * 
     * 计算宽限日
     * 
     * @param resultMap
     *            元件信息
     * @param repayDate
     *            最后还款日
     * @param nextBillDate
     *            下一账单日
     * @return
     * @throws Exception
     */
    public String calNextGraceDate(Map<String, String> resultMap, String repayDate, String nextBillDate)
            throws Exception {
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String paymentDueDate = null;
        Integer pcdValue = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            // 503AAA0100：根据最后还款日计算，503AAA0101：下一账单日作为宽限日（新增）
            if (Constant.GRACE_LAST_DATE.equals(entry.getKey())) {
                paymentDueDate = entry.getKey();
                if (StringUtil.isNotBlank(entry.getValue())) {
                	pcdValue = Integer.parseInt(entry.getValue());
				}else {
					pcdValue = 0;
				}
            } else if (Constant.GRACE_NEXT_BILL_DATE.equals(entry.getKey())) {
            	if (StringUtil.isNotBlank(entry.getKey())) {
            		  paymentDueDate = entry.getKey();
				}else {
					paymentDueDate ="";
				}
              
            }
        }
        if (StringUtil.isBlank(paymentDueDate) || StringUtil.isEmpty(paymentDueDate)) {
            throw new BusinessException("COR-10003",BSC.ARTIFACT_NO_503);
        }
        // 计算下一到期日
        String nextGraceDate = null;
        if (Constant.GRACE_LAST_DATE.equals(paymentDueDate)) {
            nextGraceDate = DateUtil.offset(repayDate, DateUtil.FORMAT_DATE, Calendar.DAY_OF_YEAR, pcdValue);
        } else if (Constant.GRACE_NEXT_BILL_DATE.equals(paymentDueDate)) {
            nextGraceDate = nextBillDate;
        }
        return nextGraceDate;
    }
	
    /**
     * 计算到期日
     * 
     * @param resultMap
     *            元件信息
     * @param currentBillDate
     *            当前账单日
     * @param nextBillDate
     *            下一账单日
     * @param billDay
     *            账单日
     * @param eventCommArea
     *            公共区域信息
     * @return
     * @throws Exception
     */
    public String calNextEndDate(Map<String, String> resultMap, String currentBillDate, String nextBillDate,
            int billDay, EventCommAreaNonFinance eventCommAreaNonFinance) throws Exception {
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String paymentDueDate = null;
        // 最低还款额
        BigDecimal pcdValue = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            // 502AAA0100：根据账单日计算,502AAA0101：固定还款日,502AAA0102：下一账单日作为还款日（新增）
            if (Constant.DUE_DATE_BILL_DATE.equals(entry.getKey())) {
                paymentDueDate = entry.getKey();
                if (StringUtil.isNotBlank(entry.getValue())) {
                	 pcdValue = new BigDecimal(entry.getValue());
				}else {
					pcdValue = BigDecimal.ZERO;
				}
               
            } else if (Constant.DUE_DATE_GENERATION_FLAG.equals(entry.getKey())) {
            	if (StringUtil.isNotBlank(entry.getValue())) {
            		 paymentDueDate = entry.getKey();
            		 pcdValue = new BigDecimal(entry.getValue());
            	}else {
            		paymentDueDate = "";
            		pcdValue = BigDecimal.ZERO;
				}
               
            } else if (Constant.DUE_DATE_GENERATION.equals(entry.getKey())) {
            	 if (StringUtil.isNotBlank(entry.getValue())) {
            		 paymentDueDate = entry.getKey();
            	 }else {
            		 pcdValue = BigDecimal.ZERO;
				}
               
            }
        }
        if (StringUtil.isBlank(paymentDueDate) || StringUtil.isEmpty(paymentDueDate)) {
            throw new BusinessException("COR-10003", BSC.ARTIFACT_NO_502);
        }
        // 计算下一到期日
        String nextEndDate = null;
        if (Constant.DUE_DATE_BILL_DATE.equals(paymentDueDate)) {
            nextEndDate = DateUtil.offset(currentBillDate, DateUtil.FORMAT_DATE, Calendar.DAY_OF_YEAR,
                    pcdValue.intValue());
        } else if (Constant.DUE_DATE_GENERATION_FLAG.equals(paymentDueDate)) {
            // 计算还款日
            int day = (billDay + pcdValue.intValue()) / 31;
            if (29 == day) {
                // 若得到值为 29， 则设定固定还款日为1
                day = 01;
            } else if (30 == day) {
                // 若得到值为 30， 则设定固定还款日为2
                day = 02;
            } else if (0 == day) {
                // 若得到值为 00， 则设定固定还款日为3
                day = 03;
            }
            // 如果最后还款日大于等于账单日，则取本月对应日，如果小于则取下月对应日
            if (day < billDay) {
                nextEndDate = DateUtil.offset(currentBillDate, DateUtil.FORMAT_DATE, Calendar.MONTH, 1);
                nextEndDate = DateUtil.format(DateUtil.parse(nextEndDate), "yyyy-MM-");
                nextEndDate = nextEndDate + day;
                nextEndDate = DateUtil.format(DateUtil.parse(nextEndDate), DateUtil.FORMAT_DATE);
            } else {
                nextEndDate = DateUtil.format(DateUtil.parse(currentBillDate), "yyyy-MM-");
                nextEndDate = nextEndDate + day;
                nextEndDate = DateUtil.format(DateUtil.parse(nextEndDate), DateUtil.FORMAT_DATE);
            }
        } else if (Constant.DUE_DATE_GENERATION.equals(paymentDueDate)) {
            nextEndDate = currentBillDate;
        }
        // 如果最后还款日大于下一账单日，则取下一账单日
        double days = DateUtil.daysBetween(nextEndDate, nextBillDate, DateUtil.FORMAT_DATE);
        if (days < 0) {
            nextEndDate = nextBillDate;
        }
        return nextEndDate;
    }
	

	/**
	 * 
	 * @Description: 获取本次最后还款日期
	 * @param eventCommArea
	 * @param artifactList
	 * @return
	 * @throws Exception
	 */
	public String getPaymentDueDate(EventCommAreaNonFinance eventCommAreaNonFinance, List<CoreActivityArtifactRel> artifactList)
			throws Exception {
		// 本次最后还款日期
		String paymentDueDate = null;
		String pcd = null;
		// 验证该活动是否配置构件信息
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_502, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_502, eventCommAreaNonFinance.getEventCommArea());
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			// 查询运营日期 + 账单日期 + PCD = 本次最后还款日期
			Map.Entry<String, String> entry = it.next();
			if (Constant.DUE_DATE_GENERATION_FLAG.equals(entry.getKey())) { // 502AAA0100
				pcd = entry.getValue().toString();
				paymentDueDate = this.getPaymentDueDatePcd(eventCommAreaNonFinance, pcd);
			} else if (Constant.DUE_DATE_GENERATION.equals(entry.getKey())) { // 502AAA0101
				pcd = entry.getValue().toString();
				paymentDueDate = this.getPaymentDueDatePcd(eventCommAreaNonFinance, pcd);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
			}
		}
		return paymentDueDate;
	}
	
	 /**
     * 计算下一账单日
     * 
     * @param resultMap
     *            元件信息
     * @param lastBillDate
     *            上一账单日
     * @return
     * @throws Exception
     */
    public String calNextBillDate(Map<String, String> resultMap, String lastBillDate, int billDay) throws Exception {
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String cycleRequency = null;
        // 最低还款额
        BigDecimal pcdValue = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) {
                cycleRequency = entry.getKey();
                if (StringUtil.isNotBlank(entry.getValue())) {
                	  pcdValue = new BigDecimal(entry.getValue());
				}else {
					pcdValue = BigDecimal.ZERO;
				}
              
            } else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) {
                cycleRequency = entry.getKey();
                pcdValue = new BigDecimal(entry.getValue());
            }
        }
        if (StringUtil.isBlank(cycleRequency) || StringUtil.isEmpty(cycleRequency)) {
            throw new BusinessException("COR-10003", BSC.ARTIFACT_NO_506);
        }
        // 计算下一账单日
        String nextBillDate = null;
        if (Constant.CYCLE_BY_MONTH.equals(cycleRequency)) {
            String currNextBillDate = DateUtil.offset(lastBillDate, DateUtil.FORMAT_DATE, Calendar.MONTH,
                    pcdValue.intValue());
            String currentBillDay = DateUtil.format(DateUtil.parse(currNextBillDate, DateUtil.FORMAT_DATE), "dd");
            String nowBillDay = "" + billDay;
            if (!currentBillDay.equals(nowBillDay)) {
                if (billDay < 10) {
                    // 账单日小于10,前面补0
                    nowBillDay = "0" + nowBillDay;
                } else if (billDay > 28) {
                    // 获取当前月份的最后一天
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.parse(currNextBillDate));
                    int lastDay = cal.getActualMaximum(Calendar.DATE);
                    if (billDay > lastDay) {
                        // 账单日大于当月最后日，取当月最后日
                        nowBillDay = "" + lastDay;
                    }
                }
                nextBillDate = currNextBillDate.replace(currentBillDay, nowBillDay);
            } else {
                nextBillDate = currNextBillDate;
            }
        } else if (Constant.CYCLE_BY_WEEK.equals(cycleRequency)) {
            nextBillDate = DateUtil.offset(lastBillDate, DateUtil.FORMAT_DATE, Calendar.DAY_OF_YEAR,
                    pcdValue.intValue() * 7);
        }
        return nextBillDate;
    }

	/**
	 * 查询运营日期 + 账单日期 + PCD = 本次最后还款日期 2018-09 + 10 + 20 =
	 * 
	 * @Description:
	 * @param eventCommArea
	 * @param pcd
	 * @return
	 * @throws ParseException
	 */
	public String getPaymentDueDatePcd(EventCommAreaNonFinance eventCommAreaNonFinance, String pcd) throws ParseException {
		String operationDate = eventCommAreaNonFinance.getOperationDate().substring(0, 8) + eventCommAreaNonFinance.getBillDay();
		DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		Date dt = sdf.parse(operationDate);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_YEAR, Integer.valueOf(pcd));// 日期加10天
		return sdf.format(rightNow.getTime());
	}

	public static void main(String[] args) {
		String operationDate = "2018-08-06";
		String operationTime = operationDate.substring(0, 8);

		System.out.println(operationTime);
	}

}
