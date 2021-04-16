package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X4010Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreInstallmentPlanDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreInstallmentPlan;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerBillDayDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentPlanSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.MapBean;
import com.tansun.ider.model.bo.X4010BO;
import com.tansun.ider.model.vo.X4005VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.DateConversionUtil;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * <p> Title: X4010BusImpl </p>
 * <p> Description: 预算单位附件信息维护</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
@Service
public class X4010BusImpl implements X4010Bus {
    private static Logger logger = LoggerFactory.getLogger(X4010BusImpl.class);
    public static final String MONTH = "month";
    public static final String WEEK = "week";
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
    @Autowired
    private CoreInstallmentPlanDao coreInstallmentPlanDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Autowired
    private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private CoreCustomerBillDayDaoImpl coreCustomerBillDayDaoImpl;
    @Autowired
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;
    @Autowired
    private HttpQueryServiceByGns httpQueryServiceByGns;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;

    @Override
    public Object busExecute(X4010BO x4010bo) throws Exception {
        // 更新预算单位附加信息记录
        SpringUtil.getBean(ValidatorUtil.class).validate(x4010bo);
        // 获取客户号修改账单日
        String customNo = getCustomNo(x4010bo);
        CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
        coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(customNo);
        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
        // 更新
        if (coreBudgetOrgAddInfo != null) {
            CoreBudgetOrgAddInfo updateCoreBudgetOrgAddInfo = new CoreBudgetOrgAddInfo();
            updateCoreBudgetOrgAddInfo.setId(coreBudgetOrgAddInfo.getId());
            updateCoreBudgetOrgAddInfo.setVersion(coreBudgetOrgAddInfo.getVersion() + 1);
            BigDecimal expandOrgAllQuota = CurrencyConversionUtil.expand(x4010bo.getOrgAllQuota(), 2);
            if (expandOrgAllQuota.compareTo(coreBudgetOrgAddInfo.getOrgAllQuota().subtract(coreBudgetOrgAddInfo.getOrgRestQuota())) == -1) {
                throw new BusinessException("单位公务卡总授信额度要大于已使用额度!");
            }
            // 已使用额度
            BigDecimal oldUseQuota = coreBudgetOrgAddInfo.getOrgAllQuota().subtract(coreBudgetOrgAddInfo.getOrgRestQuota());
            // 剩余额度要使用新总额度减去已使用额度
            BigDecimal newOrgRestQuota = expandOrgAllQuota.subtract(oldUseQuota);
            updateCoreBudgetOrgAddInfo.setOrgRestQuota(newOrgRestQuota);
            if (x4010bo.getOrgAllQuota().compareTo(BigDecimal.ZERO) == 1) {
                updateCoreBudgetOrgAddInfo.setOrgAllQuota(CurrencyConversionUtil.expand(x4010bo.getOrgAllQuota(), 2));
            }
            if (x4010bo.getPersonMaxQuota().compareTo(BigDecimal.ZERO) == 1) {
                updateCoreBudgetOrgAddInfo.setPersonMaxQuota(CurrencyConversionUtil.expand(x4010bo.getPersonMaxQuota(), 2));
            }
            int updateResult = coreBudgetOrgAddInfoDao.updateByPrimaryKeySelective(updateCoreBudgetOrgAddInfo);
            if (updateResult != 1) {
                throw new BusinessException("", "更新预算单位附件信息失败!");
            }
        }
        else {
            throw new BusinessException("", "查询客户代码对应的预算单位附件信息失败!");
        }
        // 记录非金融交易日志
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(coreBudgetOrgAddInfo.getCustomerNo());
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        nonFinancialLogUtil.createNonFinancialActivityLog(x4010bo.getEventNo(), x4010bo.getActivityNo(), ModificationType.UPD.getValue(),
            null, coreBudgetOrgAddInfo, coreBudgetOrgAddInfo, coreBudgetOrgAddInfo.getId(), coreSystemUnit.getCurrLogFlag(),
            x4010bo.getOperatorId(), coreBudgetOrgAddInfo.getCustomerNo(), coreBudgetOrgAddInfo.getCustomerNo(), null, null);
        return null;
    }

    /**
     * 获取客户号
     * @Description: TODO()
     * @param: @param x4010bo
     * @param: @return
     * @param: @throws Exception
     * @return: String
     * @throws
     */
    private String getCustomNo(X4010BO x4010bo) throws Exception {
        String idNumber = x4010bo.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer == null) {
            throw new BusinessException("查询不到预算单位！");
        }
        if (coreCustomer != null && !coreCustomer.getBillDay().equals(x4010bo.getBillDay())) {
            coreCustomerSqlBuilder.andVersionEqualTo(coreCustomer.getVersion());
            coreCustomer.setBillDay(x4010bo.getBillDay());
            coreCustomer.setVersion(coreCustomer.getVersion() + 1);
            int i = coreCustomerDao.updateBySqlBuilder(coreCustomer, coreCustomerSqlBuilder);
            if (i != 1) {
                throw new BusinessException("修改账单日失败！");
            }
            // 同时修改该预算单位下所有客户的账单日
            updateCustomerBillDay(x4010bo);
        }
        return coreCustomer.getCustomerNo();
    }

    /**
     * 修改预算单位下所有客户的账单日
     *
     * @param x4010bo
     * @throws Exception
     */
    private void updateCustomerBillDay(X4010BO x4010bo) throws Exception {
        String budgetOrgNo = x4010bo.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = null;
        String customerNo = null;
        // 该预算单位编码所在片区
        String budgetOrgGnsCode = httpQueryServiceByGns.queryGnsCode(nofnUrl, budgetOrgNo);
        logger.debug("------预算单位所在片区:---" + budgetOrgGnsCode);
        if (budgetOrgGnsCode == null && StringUtil.isBlank(budgetOrgGnsCode)) {
            throw new BusinessException("COR-11007");
        }
        X4005VO x4005Vo = new X4005VO();
        x4005Vo = JSON.parseObject(budgetOrgGnsCode, X4005VO.class, Feature.DisableCircularReferenceDetect);
        String budgetGnsCode = x4005Vo.getGnsNumber();
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(budgetGnsCode);
        // 预算单位所在片区
        budgetGnsCode = m.replaceAll("").trim();
        CoreBudgetOrgCustRelSqlBuilder budgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
        budgetOrgCustRelSqlBuilder.andBudgetOrgCodeEqualTo(budgetOrgNo);
        List<CoreBudgetOrgCustRel> list = coreBudgetOrgCustRelDao.selectListBySqlBuilder(budgetOrgCustRelSqlBuilder);
        for (CoreBudgetOrgCustRel coreBudgetOrgCustRel : list) {
            String customerArea = coreBudgetOrgCustRel.getCustomerArea();
            if (StringUtil.isBlank(customerArea)) {
                throw new BusinessException("该片区下某一客户所在片区为空");
            }
            logger.debug("------该卡所在片区:---" + customerArea);
            if (budgetGnsCode.equals(customerArea)) {
                // 同一个片区下，直接查找修改就行
                updateSameBillDay(x4010bo, coreBudgetOrgCustRel, customerNo, coreCustomerSqlBuilder);
            }
            else {
                logger.debug("----开始修改不同片区的客户:---");
                // 不同片区下，查找修改相应的客户
                Map<String, String> params = new HashMap<String, String>();
                String eventNo = x4010bo.getEventNo();
                String idNumber = x4010bo.getIdNumber();
                String billDay = x4010bo.getBillDay();
                String activityNo = x4010bo.getActivityNo();
                String globalEventNo = x4010bo.getGlobalEventNo();
                BigDecimal orgAllQuota = x4010bo.getOrgAllQuota();
                BigDecimal personMaxQuota = x4010bo.getPersonMaxQuota();
                params.put("eventNo", eventNo);
                params.put("idNumber", idNumber);
                params.put("billDay", billDay);
                params.put("activityNo", activityNo);
                params.put("globalEventNo", globalEventNo);
                params.put("orgAllQuota", orgAllQuota + "");
                params.put("personMaxQuota", personMaxQuota + "");
                try {
                    httpQueryServiceByGns.updateGnsCustomer(nofnUrl, params);
                }
                catch (Exception e) {
                    throw new BusinessException("修改gns客户账单日失败！");
                }
            }
        }
    }

    /**
     * 修改同一预算单位下的账单日
     *
     * @param x4010bo
     * @param coreBudgetOrgCustRel
     * @param customerNo
     * @param coreCustomerSqlBuilder
     * @throws Exception
     */
    private void updateSameBillDay(X4010BO x4010bo, CoreBudgetOrgCustRel coreBudgetOrgCustRel, String customerNo,
            CoreCustomerSqlBuilder coreCustomerSqlBuilder) throws Exception {
        customerNo = coreBudgetOrgCustRel.getCustomerNo();
        String externalIdentificationNo = coreBudgetOrgCustRel.getExternalIdentificationNo();
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if (coreMediaBasicInfo == null) {
            throw new BusinessException("片区下媒介基本信息表不存在该信息");
        }
        coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer == null) {
            throw new BusinessException("关系表关联的客户表查询不到该客户：" + customerNo);
        }
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        List<CoreCustomerBillDay> coreCustomerBillDaylist =
                coreCustomerBillDayDaoImpl.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
        EventCommArea eventCommArea = new EventCommArea();
        eventCommArea.setEcommOperMode(coreMediaBasicInfo.getOperationMode());
        eventCommArea.setEcommProdObjId(coreMediaBasicInfo.getProductObjectCode());
        for (CoreCustomerBillDay coreCustomerBillDay : coreCustomerBillDaylist) {
            // 修改公务卡业务项目的账单日
            eventCommArea.setEcommBusinessProgramCode(coreCustomerBillDay.getBusinessProgramNo());
            CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
            Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_505, eventCommArea);
            Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (Constant.OFFICIAL_CARD.equals(entry.getKey())) { // 505AAA1003
                    // 公务卡将修改其业务项目账单日
                    logger.debug("------开始同步预算单位下客户账单日:---");
                    updateBillDay(coreCustomerBillDaySqlBuilder, coreCustomerBillDay, coreMediaBasicInfo, coreCustomer, x4010bo);
                    logger.debug("------同步预算单位下客户账单日完成:---");
                }
            }
        }
    }

    /**
     * 修改账单日同5355逻辑
     *
     * @param coreCustomerBillDaySqlBuilder
     * @param coreCustomerBillDay
     * @param coreMediaBasicInfo
     * @param coreCustomer
     * @param x4010bo
     * @throws Exception
     */
    private void updateBillDay(CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder, CoreCustomerBillDay coreCustomerBillDay,
            CoreMediaBasicInfo coreMediaBasicInfo, CoreCustomer coreCustomer, X4010BO x4010bo) throws Exception {
        List<CoreActivityArtifactRel> artifactList = x4010bo.getArtifactList();
        String customerNo = coreMediaBasicInfo.getMainCustomerNo();
        String businessProgramNo = coreCustomerBillDay.getBusinessProgramNo();
        String oldBillDate = coreCustomerBillDay.getNextBillDate();
        EventCommArea eventCommArea = new EventCommArea();
        eventCommArea.setEcommOperMode(coreCustomer.getOperationMode());
        eventCommArea.setEcommBusinessProgramCode(businessProgramNo);
        // 如果【当前周期号】-【上一账单日修改周期号】<=506AAA02 PCD值，弹出错误信息‘账单日修改失败-修改过于频繁’
        // 当前周期号
        Integer currentCycleNumber = coreCustomerBillDay.getCurrentCycleNumber();
        if (null == currentCycleNumber) {
            throw new BusinessException("PARAM-00001", "客户业务项目周期号");
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
        }
        else {
            nextProcessDate = coreCustomerUnifyInfo.getStatementDate();
        }
        // 生成新的下一账单日
        Integer newbillDay = Integer.parseInt(x4010bo.getBillDay());
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
            coreCustomerBillDayDaoImpl.updateBySqlBuilder(coreCustomerBillDay, coreCustomerBillDaySqlBuilder);
        }
        else {// 新的下一账单日<=当前周日宽限日
            throw new BusinessException("COR-12104");// 账单日修改失败--新的下一账单日小于等于当前周期宽限日，请重新修改
        }
        // 更新分期账户
        MapBean mapBean = getThrowDateType(eventCommArea);
        if (getThrowDateType(eventCommArea) != null) {
            syncInstallThrowDay(artifactList, eventCommArea, newbillDay, customerNo, businessProgramNo, nextBillDate, oldBillDate, mapBean);
        }
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
    private void syncInstallThrowDay(List<CoreActivityArtifactRel> artifactList, EventCommArea eventCommArea, Integer newbillDay,
            String customerNo, String businessProgramNo, String nextBillDate, String oldBillDate, MapBean mapBean) throws Exception {
        // 查询账户list
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        List<CoreAccount> list = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        if (null != list && !list.isEmpty()) {
            for (CoreAccount coreAccount : list) {
                // 查询交易账户
                CoreInstallmentTransAcct coreInstallmentTransAcct =
                        queryCoreInstallmentTransAcct(coreAccount.getAccountId(), coreAccount.getCurrencyCode());
                if (coreInstallmentTransAcct == null) {
                    continue;
                }
                CoreInstallmentPlanSqlBuilder coreInstallmentPlanSqlBuilder = new CoreInstallmentPlanSqlBuilder();
                coreInstallmentPlanSqlBuilder.andAccountIdEqualTo(coreAccount.getAccountId());
                coreInstallmentPlanSqlBuilder.andCurrencyCodeEqualTo(coreAccount.getCurrencyCode());
                coreInstallmentPlanSqlBuilder.andTermNoGreaterThan(coreInstallmentTransAcct.getThrowedPeriod());
                coreInstallmentPlanSqlBuilder.orderByTermNo(false);
                List<CoreInstallmentPlan> lists = coreInstallmentPlanDao.selectListBySqlBuilder(coreInstallmentPlanSqlBuilder);
                if (coreInstallmentTransAcct.getThrowedPeriod() > 0) {
                    if (lists != null && !lists.isEmpty()) {
                        String startDate = "";
                        String endDate = nextBillDate;
                        boolean flag = true;
                        for (CoreInstallmentPlan coreInstallmentPlan : lists) {
                            if (flag) {
                                coreInstallmentPlan.setThrowDate(nextBillDate);
                            }
                            else {
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
                            coreInstallmentPlanDao.updateBySqlBuilderSelective(coreInstallmentPlan, coreInstallmentPlanSqlBuilder);
                        }
                    }
                }
                else {
                    if (lists != null && !lists.isEmpty()) {
                        String startDate = "";
                        String endDate = nextBillDate;
                        boolean flag = true;
                        String repayDayString = "";
                        for (CoreInstallmentPlan coreInstallmentPlan : lists) {
                            if (flag) {
                                if (newbillDay >= 10) {
                                    repayDayString = "" + newbillDay;
                                }
                                else {
                                    repayDayString = "0" + newbillDay;
                                }
                                String throwDate = coreInstallmentPlan.getThrowDate().substring(0, 8) + repayDayString;
                                if (throwDate.compareTo(nextBillDate) < 0) {
                                    throwDate = nextBillDate;
                                }
                                coreInstallmentPlan.setThrowDate(throwDate);
                                endDate = throwDate;
                            }
                            else {
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
                            coreInstallmentPlanDao.updateBySqlBuilderSelective(coreInstallmentPlan, coreInstallmentPlanSqlBuilder);
                        }
                    }

                }
            }
        }
    }

    /**
     * 查询客户交易账户表
     *
     * @param accountId
     *            上次处理日期
     * @return CoreInstallmentTransAcct
     * @throws Exception
     */
    private CoreInstallmentTransAcct queryCoreInstallmentTransAcct(String accountId, String currencyCode) throws Exception {
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(accountId);
        coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreInstallmentTransAcct coreInstallmentTransAcct =
                coreInstallmentTransAcctDao.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
        if (coreInstallmentTransAcct == null || "LOAN".equals(coreInstallmentTransAcct.getLoanType())
                || StringUtil.isBlank(coreInstallmentTransAcct.getNextThrowDate())) {
            return null;
        }
        return coreInstallmentTransAcct;
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
    private String getNextBillDate(String nextProcessDate, List<CoreActivityArtifactRel> artifactList, Integer newbillDay,
            EventCommArea eventCommArea) throws Exception {
        // 506 构件
        // 按月 元件编号1 下一处理日 + PCD
        // 按周 元件编号2 下一处理日 +( PCD * 7 )
        String nextBillDate = null;
        String pcd = null;
        // 验证该活动是否配置构件信息
        // Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_506, artifactList);
        // if (!checkResult) {
        // throw new BusinessException("COR-10002");
        // }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) { // 506AAA0100
                pcd = entry.getValue().toString().trim();
                nextBillDate = getNextBillDatePcd(nextProcessDate, pcd, newbillDay.toString(), MONTH);
            }
            else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) { // 506AAA0101
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
        }
        else if (WEEK.equals(flag)) {
            rightNow.add(Calendar.DAY_OF_MONTH, Integer.valueOf(pcd));
        }
        return sdf.format(rightNow.getTime());
    }

    public MapBean getThrowDateType(EventCommArea eventCommArea) throws Exception {
        // 抛帐日计算规则
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> throwDateTypeMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_605, eventCommArea);
        if (throwDateTypeMap == null) {
            return null;
        }
        Iterator<Map.Entry<String, String>> itThrow = throwDateTypeMap.entrySet().iterator();
        MapBean mapBean = new MapBean();
        while (itThrow.hasNext()) {
            Map.Entry<String, String> entry = itThrow.next();
            String key = entry.getKey().split("_")[0];
            if (Constant.NEXT_605.equals(key) || Constant.NEXT_NEXT_605.equals(key) || Constant.NEXT_BY_DAYS_605.equals(key)) {
                mapBean.setKey(key);
                mapBean.setValue(entry.getValue());
                return mapBean;
            }

        }
        return null;
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
    private void syncBalanceUnit(String customerNo, String businessProgramNo, String nextBillDate, String oldBillDate) throws Exception {
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
                List<CoreBalanceUnit> balanceList = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
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
