package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5355Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreInstallmentPlanDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.IouStatus;
import com.tansun.ider.enums.ProductType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5355BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.DateConversionUtil;

/**
 * 修改账单日
 *
 * @ClassName X5355BusImpl
 * @Description (这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2018年12月10日 上午10:00:22
 * @version 1.0.0
 */
@Service
public class X5355BusImpl implements X5355Bus {
    private static Logger logger = LoggerFactory.getLogger(X5355BusImpl.class);
    public static final String MONTH = "month";
    public static final String WEEK = "week";
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;
    @Autowired
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;
    @Autowired
    private CoreInstallmentPlanDao coreInstallmentPlanDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5355BO x5355bo) throws Exception {
        // 判断输入的各字段是否为空
        SpringUtil.getBean(ValidatorUtil.class).validate(x5355bo);
        if (null == x5355bo.getBillDay()) {
            throw new BusinessException("COR-12103");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        List<CoreActivityArtifactRel> artifactList = x5355bo.getArtifactList();

        if (null == artifactList) {
            throw new BusinessException("COR-10021");
        }

        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        CachedBeanCopy.copyProperties(x5355bo, eventCommAreaNonFinance);
        CoreEventActivityRel coreEventActivityRel = x5355bo.getCoreEventActivityRel();
        eventCommAreaNonFinance.setEventNo(coreEventActivityRel.getEventNo());
        // 获取客户号和业务项目
        String customerNo = x5355bo.getCustomerNo();
        String businessProgramNo = x5355bo.getBusinessProgramNo();

        // 查询客户业务项目是否存在
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao
                .selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        if (null == coreCustomerBillDay) {
            throw new BusinessException("CUS-00014", "客户业务项目");
        }
        String oldBillDate = coreCustomerBillDay.getNextBillDate();
        String oldBillday = coreCustomerBillDay.getBillDay() + "";
        // 查询客户是否存在
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (null == coreCustomer) {
            throw new BusinessException("CUS-00014", "客户基本");
        }

        EventCommArea eventCommArea = new EventCommArea();
        eventCommArea.setEcommOperMode(coreCustomer.getOperationMode());
        eventCommArea.setEcommBusinessProgramCode(businessProgramNo);
        // 判断是否是公务卡业务，如果是公务卡业务，则不允许修改账单日
        judgeOfficialCard(eventCommArea, artService);
        // 获取业务项目506构件实例506AAA0299（不允许修改账单日）506AAA0200（允许修改账单日）
        String pcdVlaue = getElementArtifact(eventCommArea, artService, artifactList);
        if (StringUtil.isBlank(pcdVlaue)) {
            throw new BusinessException("COR-12101");// 该业务项目不允许修改账单日
        }
        // 如果【当前周期号】-【上一账单日修改周期号】<=506AAA02 PCD值，弹出错误信息‘账单日修改失败-修改过于频繁’
        // 当前周期号
        Integer currentCycleNumber = coreCustomerBillDay.getCurrentCycleNumber();
        if (null == currentCycleNumber) {
            throw new BusinessException("PARAM-00001", "客户业务项目周期号");
        }
        // 上一账单日修改周期号
        Integer lastupdateCycleNumber = coreCustomerBillDay.getLastBillDayUpdateCycle();
        if (null != lastupdateCycleNumber) {
            Integer differValue = currentCycleNumber - lastupdateCycleNumber;
            if (differValue < Integer.parseInt(pcdVlaue)) {
                throw new BusinessException("COR-12102");// 账单日修改失败-修改过于频繁
            }
        }

        // 比较新的下一账单日与当前周期宽限日
        CoreCustomerUnifyInfo coreCustomerUnifyInfo = null;
        CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
        coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerUnifyInfoSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(--currentCycleNumber);
        coreCustomerUnifyInfo = coreCustomerUnifyInfoDao.selectBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
        if (null == coreCustomerUnifyInfo) {
            throw new BusinessException("COR-12105");// 客户统一日期为空
        }
        String nextProcessDate = null;
        if (currentCycleNumber == 0) {// 根据客户建立日期算出新的下一账单日
            nextProcessDate = coreCustomer.getCreateDate();
            if (StringUtil.isBlank(nextProcessDate)) {
                throw new BusinessException("COR-12106");
            }
        } else {
            nextProcessDate = coreCustomerUnifyInfo.getStatementDate();
        }

        // 生成新的下一账单日
        Integer newbillDay = x5355bo.getBillDay();
        checkDateFormat(nextProcessDate);
        String nextBillDate = getNextBillDate(nextProcessDate, artifactList, newbillDay, eventCommArea);

        String graceDate = coreCustomerUnifyInfo.getGraceDate();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 新的下一账单日>当前周期宽限日
        // 判断日期格式是否正确
        checkDateFormat(graceDate);
        checkDateFormat(nextBillDate);
        if (sdf.parse(nextBillDate).after(sdf.parse(graceDate))) {
            // 同步新的下一账单日期到余额单元表
            syncBalanceUnit(customerNo, businessProgramNo, nextBillDate, coreCustomerBillDay.getNextBillDate());

            // 更新业务项目表中的下一账单日期和上次账单日修改周期号。
            coreCustomerBillDay.setNextBillDate(nextBillDate);
            coreCustomerBillDay.setLastBillDayUpdateCycle(coreCustomerBillDay.getCurrentCycleNumber());
            coreCustomerBillDay.setBillDay(newbillDay);
            coreCustomerBillDaySqlBuilder.andVersionEqualTo(coreCustomerBillDay.getVersion());
            coreCustomerBillDay.setVersion(coreCustomerBillDay.getVersion() + 1);
            coreCustomerBillDayDao.updateBySqlBuilder(coreCustomerBillDay, coreCustomerBillDaySqlBuilder);

        } else {// 新的下一账单日<=当前周日宽限日
            throw new BusinessException("COR-12104");// 账单日修改失败--新的下一账单日小于等于当前周期宽限日，请重新修改
        }
        // 更新分期账户
        if (this.checkCustBusinessType(eventCommArea, coreCustomer, coreCustomerUnifyInfo)) {
        	List<CoreInstallmentTransAcct> installAccts = this.getInstallAccts(customerNo, businessProgramNo);
        	if (installAccts != null && installAccts.size() > 0) {
        		this.syncInstallThrowDay(artifactList, eventCommArea, newbillDay, customerNo, businessProgramNo, nextBillDate,
        				oldBillDate, oldBillday,installAccts);
			}
		}
        // 外部识别号，媒介单元，法人，运营模式，客户号 产品对象
        eventCommAreaNonFinance.setCustomerNo(customerNo);
        eventCommAreaNonFinance.setSystemUnitNo(coreCustomer.getSystemUnitNo());
        eventCommAreaNonFinance.setOperationMode(coreCustomer.getOperationMode());
        eventCommAreaNonFinance.setCorporation(coreCustomer.getCorporationEntityNo());
        eventCommAreaNonFinance.setCorporationEntityNo(coreCustomer.getCorporationEntityNo());
        // 通过客户号和业务项目找卡号
        List<CoreMediaBasicInfo> mediaBasicInfoList = queryCustomerMediaList(customerNo);
        if (!mediaBasicInfoList.isEmpty()) {
            boolean b = false;
            for (CoreMediaBasicInfo coreMediaBasicInfo : mediaBasicInfoList) {
                List<CoreProductBusinessScope> list = httpQueryService.queryProductBusinessScope(
                        coreMediaBasicInfo.getProductObjectCode(), coreMediaBasicInfo.getOperationMode());
                if (null != list && !list.isEmpty()) {
                    for (CoreProductBusinessScope coreProductBusinessScope : list) {
                        if (businessProgramNo.equals(coreProductBusinessScope.getBusinessProgramNo())) {
                            eventCommAreaNonFinance
                                    .setExternalIdentificationNo(coreMediaBasicInfo.getExternalIdentificationNo());
                            eventCommAreaNonFinance.setMediaUnitCode(coreMediaBasicInfo.getMediaUnitCode());
                            eventCommAreaNonFinance.setProductObjectCode(coreMediaBasicInfo.getProductObjectCode());
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        break;
                    }
                }
            }
        }
        // 判断是否为独立账单日
        String independentBillDayFlag = getElementArtifact505(eventCommArea, artService, artifactList);
        if (YesOrNo.YES.getValue().equals(independentBillDayFlag)) {
            // 独立账单日费用层级为“P-产品级”，层级代码为产品代码
            eventCommAreaNonFinance.setFeeLevel(Constant.CONTROL_P);
            eventCommAreaNonFinance.setFeeLevelCode(eventCommAreaNonFinance.getProductObjectCode());
        } else {
            // 非独立账单日费用层级为“G-业务项目级”，层级代码为业务项目代码
            eventCommAreaNonFinance.setFeeLevel(Constant.CONTROL_C);
            eventCommAreaNonFinance.setFeeLevelCode(customerNo);
        }
        return eventCommAreaNonFinance;
    }

    /**
     * 客户下是否有分期业务
     * @param eventCommArea
     * @param coreCustomer
     * @param coreCustomerUnifyInfo
     * @return
     * @throws Exception
     */
    private boolean checkCustBusinessType(EventCommArea eventCommArea,CoreCustomer coreCustomer,CoreCustomerUnifyInfo coreCustomerUnifyInfo) throws Exception{
    	  CoreBusinessProgram businessProgram =
                  httpQueryService.queryBusinessProgram(eventCommArea.getEcommOperMode(), coreCustomerUnifyInfo.getBusinessProgramNo());
          if (businessProgram == null) {
              // TODO产品不存在
              throw new BusinessException("COR-10062");
          }
          if (ProductType.LOAN.getValue().equals(businessProgram.getProgramType())) {
        	  return false;
          }
          return true;
}
    
    
    /**
     * 根据505AAA01判断是否是独立账单日：505AAA0100非独立账单日，其他为独立账单日
     * 
     * @param eventCommArea
     * @param artService
     * @param artifactList
     * @return
     * @throws Exception
     */
    private String getElementArtifact505(EventCommArea eventCommArea, CommonInterfaceForArtService artService,
            List<CoreActivityArtifactRel> artifactList) throws Exception {
        String independentBillDayFlag = "";
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_505, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_505, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String[] pcdKey = key.split("_");
            String currentKey = pcdKey[0];
            if (Constant.ACC_DATE_GENERATE_N1.equals(currentKey)) {
                independentBillDayFlag = YesOrNo.NO.getValue();
            } else {
                independentBillDayFlag = YesOrNo.YES.getValue();
            }
        }
        return independentBillDayFlag;
    }

    /**
     * 查询客户下所有有效媒介
     *
     * @param customerNo
     * @return
     * @throws Exception
     */
    private List<CoreMediaBasicInfo> queryCustomerMediaList(String customerNo) throws Exception {
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
        List<CoreMediaBasicInfo> mediaList = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if (null == mediaList) {
            throw new BusinessException("COR-00001");
        }
        return mediaList;
    }

    /**
     * 检查是否是公务卡业务，如果是抛异常（syy改） @Description: TODO() @param: @param
     * eventCommArea @param: @param artService @param: @throws
     * Exception @return: void @throws
     */
    private void judgeOfficialCard(EventCommArea eventCommArea, CommonInterfaceForArtService artService)
            throws Exception {
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_501, eventCommArea);
        // 必要元件
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.BUSINESS_BY_BUDGETUNIT.equals(entry.getKey().substring(0, 10))) {// 501AAA0101
                // 预算单位业务，公务卡不允许修改账单日
                throw new BusinessException("COR-12101");
            }

        }
    }

    /**
     * 同步新的下一账单日期到余额单元表
     * 
     * @param customerNo
     * @param businessProgramNo
     * @param nextBillDate
     * 
     * @throws Exception
     */
    private void syncBalanceUnit(String customerNo, String businessProgramNo, String nextBillDate, String oldBillDate)
            throws Exception {
        // 查询账户list
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        List<CoreAccount> list = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = null;
        if (null != list && !list.isEmpty()) {
            for (CoreAccount coreAccount : list) {
                // 查询余额单元
                coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
                coreBalanceUnitSqlBuilder.andAccountIdEqualTo(coreAccount.getAccountId());
                List<CoreBalanceUnit> balanceList = coreBalanceUnitDao
                        .selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
                if (null != balanceList && !balanceList.isEmpty()) {
                    for (CoreBalanceUnit coreBalanceUnit : balanceList) {
                        // 【下一结息日期】=【修改前下一账单日】，将【下一结息日期】修改为当前客户业务项目中的【下一账单日期】
                        if (StringUtil.isNotBlank(oldBillDate)) {
                            if (oldBillDate.equals(coreBalanceUnit.getNextInterestBillingDate())) {
                                coreBalanceUnit.setNextInterestBillingDate(nextBillDate);
                            }
                            // 【余额结束日期】=【修改前下一账单日】，将【余额结束日期】修改为当前客户业务项目中的【下一账单日期】
                            if (oldBillDate.equals(coreBalanceUnit.getEndDate())) {
                                coreBalanceUnit.setEndDate(nextBillDate);
                            }
                            coreBalanceUnitSqlBuilder.clear();
                            coreBalanceUnitSqlBuilder.andIdEqualTo(coreBalanceUnit.getId());
                            coreBalanceUnitSqlBuilder.andVersionEqualTo(coreBalanceUnit.getVersion());
                            coreBalanceUnit.setVersion(coreBalanceUnit.getVersion() + 1);
                            coreBalanceUnitDao.updateBySqlBuilder(coreBalanceUnit, coreBalanceUnitSqlBuilder);
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * @param customerNo
     * @param businessProgramNo
     * @return
     * @throws Exception
     */
    private List<CoreInstallmentTransAcct> getInstallAccts(String customerNo, String businessProgramNo) throws Exception{
    	// 查询账户list
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreAccountSqlBuilder.andAccountOrganFormEqualTo("S");
        List<CoreAccount> list = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        List<CoreInstallmentTransAcct> installAccts = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            for (CoreAccount coreAccount : list) {
                // 查询交易账户
                CoreInstallmentTransAcct coreInstallmentTransAcct = queryCoreInstallmentTransAcct(
                        coreAccount.getAccountId(), coreAccount.getCurrencyCode());
                if (coreInstallmentTransAcct != null) {
                	installAccts.add(coreInstallmentTransAcct);
				}
            }
        }
        return installAccts;
    }
    
    /**
     * 同步新的下一账单日期到分期计划表
     * 
     * @param customerNo
     * @param businessProgramNo
     * @param nextBillDate
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
	private void syncInstallThrowDay(List<CoreActivityArtifactRel> artifactList, EventCommArea eventCommArea,
            Integer newbillDay, String customerNo, String businessProgramNo, String nextBillDate, String oldBillDate,
            String oldBillday,List<CoreInstallmentTransAcct> installAccts) throws Exception {
        for (CoreInstallmentTransAcct coreInstallmentTransAcct : installAccts) {
        	eventCommArea.setEcommInstallmentBusinessType(coreInstallmentTransAcct.getLoanType());
        	if (this.getThrowDateType(eventCommArea)) {
        		if (coreInstallmentTransAcct == null) {
                    continue;
                }
                // 新的跟以前的一样，不变
                if (String.valueOf(newbillDay).equals(coreInstallmentTransAcct.getRepayDay())) {
                    continue;
                }
                // 原先的跟账户里的不一样
                if (!String.valueOf(oldBillday).equals(coreInstallmentTransAcct.getRepayDay())) {
                    continue;
                }
                nextBillDate = reCalNextThrowDate(coreInstallmentTransAcct.getNextThrowDate(), nextBillDate,
                        newbillDay);
                // 更新下一次账单日
                coreInstallmentTransAcct.setNextThrowDate(nextBillDate);
                coreInstallmentTransAcct.setRepayDay(newbillDay);
                coreInstallmentTransAcct.setVersion(coreInstallmentTransAcct.getVersion() + 1);
          /*      CoreInstallmentPlanSqlBuilder coreInstallmentPlanSqlBuilder = new CoreInstallmentPlanSqlBuilder();
                coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(coreInstallmentTransAcct.getAccountId());
                coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(coreInstallmentTransAcct.getCurrencyCode());
                coreInstallmentPlanSqlBuilder.andTermNoGreaterThan(coreInstallmentTransAcct.getThrowedPeriod());
                coreInstallmentPlanSqlBuilder.orderByTermNo(false);
                List<CoreInstallmentPlan> lists = coreInstallmentPlanDao
                        .selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);*/
                // if(coreInstallmentTransAcct.getThrowedPeriod()>0){
                String startDate = "";
         /*       if (lists != null && !lists.isEmpty()) {

                    String endDate = nextBillDate;
                    boolean flag = true;
                    for (CoreInstallmentPlan coreInstallmentPlan : lists) {
                        if (flag) {
                            coreInstallmentPlan.setThrowDate(nextBillDate);
                        } else {
                            coreInstallmentPlan.setStartDate(startDate);
                            coreInstallmentPlan.setThrowDate(endDate);
                        }
                        startDate = endDate;
                        flag = false;
                        endDate = getNextBillDate(endDate, artifactList, newbillDay, eventCommArea);
                        coreInstallmentPlanSqlBuilder.clear();
                        coreInstallmentPlanSqlBuilder.andIdEqualTo(coreInstallmentPlan.getId());
                        coreInstallmentPlanSqlBuilder.andVersionEqualTo(coreInstallmentPlan.getVersion());
                        coreInstallmentPlan.setVersion(coreInstallmentPlan.getVersion() + 1);
                        coreInstallmentPlanDao.updateBySqlBuilderSelective(coreInstallmentPlan,
                                coreInstallmentPlanSqlBuilder);
                    }
                }*/
                String endDate = getEndBillDate(nextBillDate, artifactList, newbillDay, eventCommArea,coreInstallmentTransAcct.getRemainPeriod()-1);
                coreInstallmentTransAcct.setLoanEndDate(endDate);
                coreInstallmentTransAcct.setNextMakePlanDate(nextBillDate);
                coreInstallmentTransAcctDao.updateByPrimaryKey(coreInstallmentTransAcct);
			}
		}
        
    }

    /**
     * 计算下一账单日期到分期计划表
     * 
     * @param nextThrowDate
     * @param nextBillDate
     * @param newbillDay
     * @throws Exception
     */
    public String reCalNextThrowDate(String nextThrowDate, String nextBillDate, Integer newbillDay) {
        String repayDayString = "";
        if (newbillDay >= 10) {
            repayDayString = "" + newbillDay;
        } else {
            repayDayString = "0" + newbillDay;
        }
        String throwDate = nextThrowDate.substring(0, 8) + repayDayString;
        if (throwDate.compareTo(nextBillDate) < 0) {
            throwDate = nextBillDate;
        }
        return throwDate;
    }

    public boolean getThrowDateType(EventCommArea eventCommArea) throws Exception {
        // 抛帐日计算规则
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> throwDateTypeMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_605, eventCommArea);
        if (throwDateTypeMap == null) {
            return true;
        }
        Iterator<Map.Entry<String, String>> itThrow = throwDateTypeMap.entrySet().iterator();
        while (itThrow.hasNext()) {
            Map.Entry<String, String> entry = itThrow.next();
            String key = entry.getKey().split("_")[0];
            if (Constant.NEXT_605.equals(key) || Constant.NEXT_NEXT_605.equals(key)
                    || Constant.NEXT_BY_DAYS_605.equals(key)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 查询客户交易账户表
     *
     * @param accountId
     *            上次处理日期
     * @return CoreInstallmentTransAcct
     * @throws Exception
     */
    private CoreInstallmentTransAcct queryCoreInstallmentTransAcct(String accountId, String currencyCode)
            throws Exception {
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(accountId);
        coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreInstallmentTransAcct coreInstallmentTransAcct = coreInstallmentTransAcctDao
                .selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
        //查询是否为信贷账户
        
        if (coreInstallmentTransAcct == null
                || StringUtil.isBlank(coreInstallmentTransAcct.getNextThrowDate())
                || IouStatus.revoke.getValue().equals(coreInstallmentTransAcct.getStatus())
                || IouStatus.FRT.getValue().equals(coreInstallmentTransAcct.getStatus())
                || IouStatus.settle.getValue().equals(coreInstallmentTransAcct.getStatus())) {
            return null;
        }
        return coreInstallmentTransAcct;
    }

    /**
     * 505AAA0299（不允许修改账单日）505AAA0200（允许修改账单日）
     * 
     * @Description (TODO这里用一句话描述这个方法的作用)
     * @param eventCommArea
     * @return
     * @throws Exception
     */
    public String getElementArtifact(EventCommArea eventCommArea, CommonInterfaceForArtService artService,
            List<CoreActivityArtifactRel> artifactList) throws Exception {
        String pcdValue = null;
        for (CoreActivityArtifactRel dto : artifactList) {
            // 通过构件获取元件，公共程序处理CoreArtifactKey
            Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
            // 必要元件
            Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (Constant.CYCLE_BY_ALLOW.equals(entry.getKey().substring(0, 10))) {// 506AAA0200（允许修改账单日）
                    pcdValue = entry.getValue().toString().trim();
                } else if (Constant.CYCLE_BY_FORBID.equals(entry.getKey().substring(0, 10))) {// 506AAA0299（不允许修改账单日）
                    pcdValue = "";
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
                }
            }
        }
        if (null == pcdValue) {
            logger.debug("未查到505构件实例化信息");
            throw new BusinessException("CUS-00079", BSC.ARTIFACT_NO_505);
        }
        return pcdValue;
    }
    
    
    /**
     * 根据客户统一日期中本次账单日期生成日期1-YYYYMMDD； 日期1的YYYYMM与本次账单日期相同； 日期1的DD=修改后的DD。
     * 
     * @Description: 获取下一账单日
     * @param eventCommArea
     * @param artifactList
     * @param coreCustomer
     * @return
     * @throws Exception
     */
    private String getEndBillDate(String nextProcessDate, List<CoreActivityArtifactRel> artifactList,
            Integer newbillDay, EventCommArea eventCommArea,Integer cycle) throws Exception {
        // 506 构件
        // 按月 元件编号1 下一处理日 + PCD
        // 按周 元件编号2 下一处理日 +( PCD * 7 )
        String nextBillDate = null;
        String pcd = null;
        if(cycle<=0){
        	return nextProcessDate;
        }
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_506, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) { // 506AAA0100
                pcd = entry.getValue().toString().trim();
                pcd = String.valueOf(Integer.valueOf(pcd)*cycle);
                nextBillDate = getNextBillDatePcd(nextProcessDate, pcd, newbillDay.toString(), MONTH);
            } else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) { // 506AAA0101
                BigDecimal result = new BigDecimal(7*cycle).multiply(new BigDecimal(entry.getValue().toString().trim()));
                pcd = result.toString();
                nextBillDate = getNextBillDatePcd(nextProcessDate, pcd, newbillDay.toString(), WEEK);
            }
        }
        return nextBillDate;
    }

    /**
     * 根据客户统一日期中本次账单日期生成日期1-YYYYMMDD； 日期1的YYYYMM与本次账单日期相同； 日期1的DD=修改后的DD。
     * 
     * @Description: 获取下一账单日
     * @param eventCommArea
     * @param artifactList
     * @param coreCustomer
     * @return
     * @throws Exception
     */
    private String getNextBillDate(String nextProcessDate, List<CoreActivityArtifactRel> artifactList,
            Integer newbillDay, EventCommArea eventCommArea) throws Exception {
        // 506 构件
        // 按月 元件编号1 下一处理日 + PCD
        // 按周 元件编号2 下一处理日 +( PCD * 7 )
        String nextBillDate = null;
        String pcd = null;
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_506, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) { // 506AAA0100
                pcd = entry.getValue().toString().trim();
                nextBillDate = getNextBillDatePcd(nextProcessDate, pcd, newbillDay.toString(), MONTH);
            } else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) { // 506AAA0101
                BigDecimal result = new BigDecimal(7).multiply(new BigDecimal(entry.getValue().toString().trim()));
                pcd = result.toString();
                nextBillDate = getNextBillDatePcd(nextProcessDate, pcd, newbillDay.toString(), WEEK);
            }
        }
        return nextBillDate;
    }

    /**
     * 下一账单日计算
     * 
     * @param oldDate
     * @param pcd
     * @param billDay
     * @param flag
     * @return
     * @throws ParseException
     * 
     */
    private String getNextBillDatePcd(String oldDate, String pcd, String billDay, String flag) throws ParseException {

        String nowDateStr = oldDate.substring(0, 8);
        if (billDay.length() == 1) {
            billDay = "0".concat(billDay);
        }
        nowDateStr = nowDateStr + billDay;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 增加月或周的时间
        Date date = sdf.parse(nowDateStr);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        if (MONTH.equals(flag)) {
            rightNow.add(Calendar.MONTH, Integer.valueOf(pcd));
        } else if (WEEK.equals(flag)) {
            rightNow.add(Calendar.DAY_OF_MONTH, Integer.valueOf(pcd));
        }
        return sdf.format(rightNow.getTime());
    }

    /**
     * 1. 校验输入的日期是否合法  DD必须是1~31，根据大小月不同分别检查；  MM必须是1~12 //
     * 年只有两位时（YY），需拼接成四位YYYY；
     * 
     * @Description:
     * @param Date
     * @return
     */
    public void checkDateFormat(String date) throws Exception {
        if (10 != date.length()) {
            throw new Exception(date + "Date format type must be yyyy-MM-dd");
        }
        String yyyy = date.substring(0, 4);
        String mmStr = date.substring(5, 7);
        Integer dd = Integer.valueOf(date.substring(8, 10));
        Integer mm = Integer.valueOf(mmStr);
        //  MM必须是1~12
        if (mm < 0 || mm > 12) {
            throw new Exception(date + "The input month must be between 1-12.");
        }
        // 检验输入的天数比如是1 -31天内,满足大小月天数

        Integer year = Integer.valueOf(yyyy);
        int sum = DateConversionUtil.calculationMonth(year, mm);
        if ((dd <= 0) || (dd > sum)) {
            throw new Exception(date + "The date parameters you entered do not meet the requirements.");
        }
    }

}
