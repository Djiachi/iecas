package com.tansun.ider.bus.impl;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5994Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.*;
import com.tansun.ider.dao.issue.entity.*;
import com.tansun.ider.dao.issue.sqlbuilder.*;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5345BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.DateConversionUtil;
import com.tansun.ider.util.NonFinancialLogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 修改最后还款日和宽限日
 *
 * @author gaozhennan
 * @version 1.0.0
 * @ClassName X5994BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @Date 2019.9.10
 */
@Service
public class X5994BusImpl implements X5994Bus {
    private static Logger logger = LoggerFactory.getLogger(X5994BusImpl.class);

    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;
    @Autowired
    private HttpQueryServiceImpl httpQueryService;
    @Autowired
    private CoreAccountDao coreAccountDao;

    @Autowired
    private CoreAccountCycleFiciDao coreAccountCycleFiciDao;

    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;

    @Override
    public Object busExecute(X5345BO x5345bo) throws Exception {

        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5345bo,eventCommAreaNonFinance);

        //1.输入字段检查
        //获取本次账单日
        String statementDate = x5345bo.getStatementDate();

        // 获取客户号和业务项目
        String customerNo = x5345bo.getCustomerNo();
        String businessProgramNo = x5345bo.getBusinessProgramNo();

        // 查询客户业务项目是否存在  并获取下一账单日 和约定扣款状态
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao
                .selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        if (null == coreCustomerBillDay) {
            throw new BusinessException("CUS-00014", "客户业务项目");
        }
        String nextBillDate = coreCustomerBillDay.getNextBillDate();
        String directDebitStatus = coreCustomerBillDay.getDirectDebitStatus();

        // 当前周期号
        Integer currentCycleNumber = coreCustomerBillDay.getCurrentCycleNumber();
        if (null == currentCycleNumber) {
            throw new BusinessException("PARAM-00001", "客户业务项目周期号");
        }

        // 查询客户是否存在
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (null == coreCustomer) {
            throw new BusinessException("CUS-00014", "客户基本");
        }
        //获取之前的最后还款日
        CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
        coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerUnifyInfoSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(currentCycleNumber - 1);
        CoreCustomerUnifyInfo coreCustomerUnifyInfo = coreCustomerUnifyInfoDao.selectBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
        if (null == coreCustomerUnifyInfo) {
            throw new BusinessException("COR-12105", "客户统一日期");
        }
        String oldPaymentDueDate = coreCustomerUnifyInfo.getPaymentDueDate();

        //获取系统单元的下一处理日期
        String systemUnitNo = coreCustomer.getSystemUnitNo();
        CoreSystemUnit coreSystemUnitList = httpQueryService.querySystemUnit(systemUnitNo);
        String nextProcessDate = coreSystemUnitList.getNextProcessDate();

        // 判断输入的最后还款日和宽限日字段是否为空   并获取最后还款日和宽限日
        SpringUtil.getBean(ValidatorUtil.class).validate(x5345bo);
        if (null == x5345bo.getPaymentDueDate()) {
            throw new BusinessException("COR-12113");
        }
        //并获取更改后最后还款日
        String paymentDueDate = x5345bo.getPaymentDueDate();


        //检查系统单元下一处理日必须大于本次账单日期，小于等于之前的最后还款日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        checkDateFormat(nextProcessDate);
        checkDateFormat(statementDate);
        checkDateFormat(oldPaymentDueDate);
        checkDateFormat(paymentDueDate);

//        if (sdf.parse(nextProcessDate).after(sdf.parse(statementDate)) && sdf.parse(oldPaymentDueDate).after(sdf.parse(nextProcessDate))) {
        if (sdf.parse(nextProcessDate).after(sdf.parse(statementDate)) && !(sdf.parse(nextProcessDate).after(sdf.parse(oldPaymentDueDate)))) {
        }else{
            throw new BusinessException("COR-12111");
        }

        //检查输入的最后还款日必须大于系统单元下一处理日
        /*if (sdf.parse(nextProcessDate).after(sdf.parse(paymentDueDate))) {
            throw new BusinessException("COR-12115");
        }*/
        if (sdf.parse(paymentDueDate).after(sdf.parse(nextProcessDate))) {
        } else {
            throw new BusinessException("COR-12115");
        }

        //获取活动相关连的构件
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        List<CoreActivityArtifactRel> artifactList = x5345bo.getArtifactList();
        if (null == artifactList) {
            throw new BusinessException("COR-10021");
        }


        //往事件公共区设置运营模式和业务类型
        EventCommArea eventCommArea = new EventCommArea();
        eventCommArea.setEcommOperMode(coreCustomer.getOperationMode());
        eventCommArea.setEcommBusinessProgramCode(businessProgramNo);

        //2.检查是否允许修改
        //502AAA0200  修改次数大于pcd值   显示统一日期修改过于频繁
        //502AAA0299   不允许修改最后还款日
        String pcdVlaue = getElementArtifact(eventCommArea, artService, artifactList);
        if (StringUtil.isBlank(pcdVlaue)) {
            throw new BusinessException("COR-12116");// 该业务项目不允许修改最后还款日和宽限日
//            throw new BusinessException("COR-12116" + "该业务项目不允许修改最后还款日和宽限日");// 该业务项目不允许修改最后还款日和宽限日
        }
        //获取客户统一日期表的本周期修改次数   为空可以修改
        Integer cycleModifyNo = coreCustomerUnifyInfo.getCycleModifyNo();

        if (!"null".equals(cycleModifyNo + "")) {

            if (cycleModifyNo >= Integer.parseInt(pcdVlaue)) {
                throw new BusinessException("COR-12112");// 统一日期修改失败-修改过于频繁
//                throw new BusinessException("COR-12112" + "统一日期修改失败-修改过于频繁");// 统一日期修改失败-修改过于频繁
            }
        }

        //3.生成最后还款日期、宽限日期、迟缴费收取日期、约定扣款日期
        String newPaymentDueDate = null;
        String newGraceDate = null;
        String newDelinquencyDate = null;
        String newDirectDebitDate = null;

        if (!oldPaymentDueDate.equals(paymentDueDate)) {
            newPaymentDueDate = paymentDueDate;
            //获取宽限日
            newGraceDate = getGraceDate(sdf, artService, artifactList, eventCommArea, newPaymentDueDate);

            //获取迟缴费收取日期
            newDelinquencyDate = getDelinquencyDate(sdf, artService, artifactList, eventCommArea, newPaymentDueDate);

            //获取约定扣款日期
            newDirectDebitDate = getDirectDebitDate(x5345bo, sdf, artService, artifactList, directDebitStatus, eventCommArea, newPaymentDueDate);

        } else {
            newPaymentDueDate = x5345bo.getPaymentDueDate();
            newGraceDate = x5345bo.getGraceDate();
            newDelinquencyDate = x5345bo.getDelinquencyDate();
            newDirectDebitDate = x5345bo.getDirectDebitDate();
        }

        //4.判断检查修改后的最后还款日期、宽限日期、迟缴费收取日期、约定扣款日期均不大于下一账单日期
        if (!StringUtil.isBlank(newDirectDebitDate)) {
            checkDateFormat(newPaymentDueDate);
            checkDateFormat(newGraceDate);
            checkDateFormat(newDelinquencyDate);
            checkDateFormat(newDirectDebitDate);
            /*if (sdf.parse(nextBillDate).after(sdf.parse(newPaymentDueDate)) || nextBillDate.equals(newPaymentDueDate)) {
            } else {
                throw new BusinessException("COR-12117");
            }
            if (sdf.parse(nextBillDate).after(sdf.parse(newGraceDate))|| nextBillDate.equals(newGraceDate)) {
            } else {
                throw new BusinessException("COR-12118");
            }
            if (sdf.parse(nextBillDate).after(sdf.parse(newDelinquencyDate)) || nextBillDate.equals(newDelinquencyDate)) {
            } else {
                throw new BusinessException("COR-12119");
            }
            if (sdf.parse(nextBillDate).after(sdf.parse(newDirectDebitDate)) || nextBillDate.equals(newDirectDebitDate)) {
            } else {
                throw new BusinessException("COR-12120");
            }*/
            if (!sdf.parse(newPaymentDueDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12117");
            }
            if (!sdf.parse(newGraceDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12118");
            }
            if (!sdf.parse(newDelinquencyDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12119");
            }
            if (!sdf.parse(newDirectDebitDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12120");
            }
        } else {
            checkDateFormat(newPaymentDueDate);
            checkDateFormat(newGraceDate);
            checkDateFormat(newDelinquencyDate);
            /*if (sdf.parse(nextBillDate).after(sdf.parse(newPaymentDueDate))|| nextBillDate.equals(newPaymentDueDate)) {
            } else {
                throw new BusinessException("COR-12117");
            }
            if (sdf.parse(nextBillDate).after(sdf.parse(newGraceDate)) || nextBillDate.equals(newGraceDate)) {
            } else {
                throw new BusinessException("COR-12118");
            }
            if (sdf.parse(nextBillDate).after(sdf.parse(newDelinquencyDate)) || nextBillDate.equals(newDelinquencyDate)) {
            } else {
                throw new BusinessException("COR-12119");
            }*/
            if (!sdf.parse(newPaymentDueDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12117");
            }
            if (!sdf.parse(newGraceDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12118");
            }
            if (!sdf.parse(newDelinquencyDate).after(sdf.parse(nextBillDate))) {
            } else {
                throw new BusinessException("COR-12119");
            }

        }

        //5.更新数据库表

        updateTables(x5345bo, customerNo, businessProgramNo, coreCustomerBillDay, currentCycleNumber, coreCustomer, coreCustomerUnifyInfoSqlBuilder, coreCustomerUnifyInfo, newPaymentDueDate, newGraceDate, newDelinquencyDate, newDirectDebitDate);
        return null;
    }

    /**
     * 更新客户统一日期表，客户表，账户周期金融信息表，并新增活动日志
     * @param x5345bo
     * @param customerNo
     * @param businessProgramNo
     * @param coreCustomerBillDay
     * @param currentCycleNumber
     * @param coreCustomer
     * @param coreCustomerUnifyInfoSqlBuilder
     * @param coreCustomerUnifyInfo
     * @param newPaymentDueDate
     * @param newGraceDate
     * @param newDelinquencyDate
     * @param newDirectDebitDate
     * @throws Exception
     */
    private void updateTables(X5345BO x5345bo, String customerNo, String businessProgramNo, CoreCustomerBillDay coreCustomerBillDay, Integer currentCycleNumber, CoreCustomer coreCustomer, CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder, CoreCustomerUnifyInfo coreCustomerUnifyInfo, String newPaymentDueDate, String newGraceDate, String newDelinquencyDate, String newDirectDebitDate) throws Exception {
        String operatorId = x5345bo.getOperatorId();
        // 新发卡
        if (operatorId == null) {
            operatorId = "system";
        }
        // 更新客户统一日期表
        CoreCustomerUnifyInfo coreCustomerUnifyInfoAfter = new CoreCustomerUnifyInfo();
        CachedBeanCopy.copyProperties(coreCustomerUnifyInfo,coreCustomerUnifyInfoAfter);
        coreCustomerUnifyInfo.setPaymentDueDate(newPaymentDueDate);
        coreCustomerUnifyInfo.setGraceDate(newGraceDate);
        coreCustomerUnifyInfo.setDelinquencyDate(newDelinquencyDate);
        coreCustomerUnifyInfo.setDirectDebitDate(newDirectDebitDate);
        coreCustomerUnifyInfo.setLastPaymentDueDate(coreCustomerUnifyInfo.getPaymentDueDate());
        coreCustomerUnifyInfo.setLastGraceDate(coreCustomerUnifyInfo.getGraceDate());
        coreCustomerUnifyInfo.setLastDelinquencyDate(coreCustomerUnifyInfo.getDelinquencyDate());
        coreCustomerUnifyInfo.setLastDirectDebitDate(coreCustomerUnifyInfo.getDirectDebitDate());
        Integer cycleModifyNo1 = coreCustomerUnifyInfo.getCycleModifyNo();
        if ("null".equals(cycleModifyNo1 + "")) {

            coreCustomerUnifyInfo.setCycleModifyNo(1);
        } else {
            coreCustomerUnifyInfo.setCycleModifyNo(coreCustomerUnifyInfo.getCycleModifyNo() + 1);
        }
        coreCustomerUnifyInfo.setVersion(coreCustomerUnifyInfo.getVersion() + 1);

        coreCustomerUnifyInfoDao.updateBySqlBuilder(coreCustomerUnifyInfo, coreCustomerUnifyInfoSqlBuilder);


        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        //获取全局事件编号
        String globalEventNo = x5345bo.getGlobalEventNo();
        //获取事件号
        String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
//        获取活动号
        String activityNo = x5345bo.getActivityNo();
        //新增非金融活动日志

        nonFinancialLogUtil.createNonFinancialActivityLog(eventNo,activityNo,
                ModificationType.UPD.getValue(), null, coreCustomerUnifyInfoAfter, coreCustomerUnifyInfo,
                coreCustomerBillDay.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
                customerNo, coreCustomerUnifyInfo.getBusinessProgramNo(), null, null);

        //更新表信息
        //更新账户基本信息
        //先查账户号和币种信息
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        String accountNo = null;
        String currencyCode = null;
        List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        if (coreAccountList != null && coreAccountList.size() > 0) {

            for (CoreAccount coreAccount : coreAccountList) {
                accountNo = coreAccount.getAccountId();
                currencyCode = coreAccount.getCurrencyCode();
                CoreAccount coreAccountAfter = new CoreAccount();
                CachedBeanCopy.copyProperties(coreAccount,coreAccountAfter);
                //更新账户表
                CoreAccountSqlBuilder coreAccountSqlBuilder1 = new CoreAccountSqlBuilder();
                coreAccountSqlBuilder1.andAccountIdEqualTo(accountNo);
                coreAccountSqlBuilder1.andCurrencyCodeEqualTo(currencyCode);
                coreAccount.setPaymentDueDate(newPaymentDueDate);
                coreAccount.setGraceDate(newGraceDate);
                coreAccount.setDelinquencyDate(newDelinquencyDate);
                coreAccountDao.updateBySqlBuilder(coreAccount, coreAccountSqlBuilder1);

                //新增非金融活动日志
                nonFinancialLogUtil.createNonFinancialActivityLog(eventNo,activityNo,
                        ModificationType.UPD.getValue(), null, coreAccountAfter, coreAccount,
                        coreCustomerBillDay.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
                        customerNo, coreCustomerUnifyInfo.getBusinessProgramNo(), null, null);


                //更新账户周期金融信息表
                CoreAccountCycleFiciSqlBuilder coreAccountCycleFiciSqlBuilder = new CoreAccountCycleFiciSqlBuilder();
                coreAccountCycleFiciSqlBuilder.andAccountIdEqualTo(accountNo);
                coreAccountCycleFiciSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
                coreAccountCycleFiciSqlBuilder.andCurrentCycleNumberEqualTo(currentCycleNumber - 1);
                CoreAccountCycleFici coreAccountCycleFici = coreAccountCycleFiciDao.selectBySqlBuilder(coreAccountCycleFiciSqlBuilder);
                if (coreAccountCycleFici != null) {
                    CoreAccountCycleFici coreAccountCycleFiciAfter = new CoreAccountCycleFici();
                    CachedBeanCopy.copyProperties(coreAccountCycleFici,coreAccountCycleFiciAfter);
                    coreAccountCycleFici.setPaymentDueDate(newPaymentDueDate);
                    coreAccountCycleFici.setGraceDate(newGraceDate);
                    coreAccountCycleFiciDao.updateBySqlBuilder(coreAccountCycleFici, coreAccountCycleFiciSqlBuilder);

                    //新增非金融活动日志
                    nonFinancialLogUtil.createNonFinancialActivityLog(eventNo,activityNo,
                            ModificationType.UPD.getValue(), null, coreAccountCycleFiciAfter, coreAccountCycleFici,
                            coreCustomerBillDay.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
                            customerNo, coreCustomerUnifyInfo.getBusinessProgramNo(), null, null);
                }

            }
        }
    }


    /**
     * 获取约定扣款日期
     *
     * @param x5345bo
     * @param sdf
     * @param artService
     * @param artifactList
     * @param directDebitStatus
     * @param eventCommArea
     * @param newPaymentDueDate
     * @return
     * @throws Exception
     */
    private String getDirectDebitDate(X5345BO x5345bo, SimpleDateFormat sdf, CommonInterfaceForArtService artService, List<CoreActivityArtifactRel> artifactList, String directDebitStatus, EventCommArea eventCommArea, String newPaymentDueDate) throws Exception {
        String directDebitDate = null;
        String pcdValue = null;

        if (!StringUtil.isBlank(directDebitStatus) && 1 == Integer.parseInt(directDebitStatus)) {
            for (CoreActivityArtifactRel dto : artifactList) {
                // 通过构件获取元件，公共程序处理CoreArtifactKey
                Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
                // 必要元件
                Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (Constant.DIRECT_DEBIT_DUE_DAY.equals(entry.getKey().substring(0, 10))) {//508AAA0200	最后还款日产生约定扣款
                        pcdValue = entry.getValue().toString().trim();
                    } else if (Constant.DIRECT_DEBIT_STATEMENT_DAY.equals(entry.getKey().substring(0, 10))) {//508AAA0201	账单日产生约定扣款
                        //pcdValue = "";
                        throw new BusinessException("COR-12123");
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
                    }
                }
            }
            if (null == pcdValue) {
                logger.debug("COR-12124");
                throw new BusinessException("CUS-00079", BSC.ARTIFACT_NO_508);
            }
            int realPcdValue = Integer.valueOf(pcdValue);
            Calendar calendar = Calendar.getInstance();
            Date date = sdf.parse(newPaymentDueDate);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, realPcdValue);
            directDebitDate = sdf.format(calendar.getTime());

        } else if (StringUtil.isBlank(directDebitStatus)) {
            directDebitDate = x5345bo.getDirectDebitDate();
        } else if (0 == Integer.parseInt(directDebitStatus)) {
            directDebitDate = x5345bo.getDirectDebitDate();
        }
        return directDebitDate;
    }

    /**
     * 获取迟缴费收取日期
     *
     * @param sdf
     * @param artService
     * @param artifactList
     * @param eventCommArea
     * @param newPaymentDueDate
     * @return
     * @throws Exception
     */
    private String getDelinquencyDate(SimpleDateFormat sdf, CommonInterfaceForArtService artService, List<CoreActivityArtifactRel> artifactList, EventCommArea eventCommArea, String newPaymentDueDate) throws Exception {
        String delinquencyDate;
        String pcdValue = null;
        for (CoreActivityArtifactRel dto : artifactList) {
            // 通过构件获取元件，公共程序处理CoreArtifactKey
            Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
            // 必要元件
            Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (Constant.ACC_DATE_GENERATE_LATE.equals(entry.getKey().substring(0, 10))) {// 504AAA0100	根据最后还款日计算迟缴费收取日期
                    pcdValue = entry.getValue().toString().trim();
                } else if (Constant.ACC_DATE_EQUALS_REPAYMENT_DATE.equals(entry.getKey().substring(0, 10))) {// 504AAA0199	不计算滞纳金日期
                    pcdValue = "0";
                    throw new BusinessException("COR-12125");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
                }
            }
        }
        if (null == pcdValue) {
            logger.debug("COR-12126");
            throw new BusinessException("CUS-00079", BSC.ARTIFACT_NO_504);
        }
        int realPcdValue = Integer.valueOf(pcdValue);
        Calendar calendar = Calendar.getInstance();
        Date date = sdf.parse(newPaymentDueDate);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, realPcdValue);
        delinquencyDate = sdf.format(calendar.getTime());
        return delinquencyDate;
    }

    /**
     * 获取宽限日
     *
     * @param sdf
     * @param artService
     * @param artifactList
     * @param eventCommArea
     * @param newPaymentDueDate
     * @return
     * @throws Exception
     */
    private String getGraceDate(SimpleDateFormat sdf, CommonInterfaceForArtService artService, List<CoreActivityArtifactRel> artifactList, EventCommArea eventCommArea, String newPaymentDueDate) throws Exception {
        String graceDate;
        String pcdValue = null;
        for (CoreActivityArtifactRel dto : artifactList) {
            // 通过构件获取元件，公共程序处理CoreArtifactKey
            Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
            // 必要元件
            Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                if (Constant.GRACE_LAST_DATE.equals(entry.getKey().substring(0, 10))) {// 503AAA0100（根据最后还款日计算）
                    pcdValue = entry.getValue().toString().trim();
                } else if (Constant.GRACE_EQUALS_LAST_DATE.equals(entry.getKey().substring(0, 10))) {// 503AAA0199（不计算宽限日期）
                    //pcdValue = "0";
                    throw new BusinessException("COR-12127");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
                }
            }
        }
        if (null == pcdValue) {
            logger.debug("COR-12128");
            throw new BusinessException("CUS-00079", BSC.ARTIFACT_NO_503);
        }
        int realPcdValue = Integer.valueOf(pcdValue);

        Calendar calendar = Calendar.getInstance();
        Date date = sdf.parse(newPaymentDueDate);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, realPcdValue);
        graceDate = sdf.format(calendar.getTime());
        return graceDate;
    }





    /**
     * //502AAA0200  修改次数大于pcd值   显示统一日期修改过于频繁
     * //502AAA0299   不允许修改最后还款日
     *
     * @param eventCommArea
     * @return
     * @throws Exception
     * @Description (TODO这里用一句话描述这个方法的作用)
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
                if (Constant.LAST_REPAYMENT_AND_GRACE_DATE_BY_ALLOW.equals(entry.getKey().substring(0, 10))) {// 502AAA0200  修改次数大于pcd值   显示统一日期修改过于频繁
                    pcdValue = entry.getValue().toString().trim();
                } else if (Constant.LAST_REPAYMENT_DATE_BY_FORBID.equals(entry.getKey().substring(0, 10))) {// 502AAA0299   不允许修改最后还款日
                    //pcdValue = "";
                    throw new BusinessException("COR-12129");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
                }
            }
        }
        if (null == pcdValue) {
            logger.debug("COR-12130");
            throw new BusinessException("CUS-00079", BSC.ARTIFACT_NO_502);
        }
        return pcdValue;
    }


    /**
     * 1. 校验输入的日期是否合法  DD必须是1~31，根据大小月不同分别检查；  MM必须是1~12 //
     * 年只有两位时（YY），需拼接成四位YYYY；
     *
     * @param date
     * @return
     * @Description:
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
