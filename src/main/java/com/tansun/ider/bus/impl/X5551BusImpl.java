package com.tansun.ider.bus.impl;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5551Bus;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5551BO;
import com.tansun.ider.model.vo.X5551VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class X5551BusImpl implements X5551Bus {
    private static Logger logger = LoggerFactory.getLogger(X5551BusImpl.class);
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;

    @Autowired
    private CommonInterfaceForArtService artService;

    @Autowired
    CardUtil cardUtil;

    @Override
    public Object busExecute(X5551BO x5551BO) throws Exception {

        X5551VO X5551VO=null;
        //查询业务项目表
        CoreCustomerBillDay customerBillDay = queryCoreCustomerBillDay(x5551BO.getCustomerNo(), x5551BO.getBusinessProgramNo());
        if(Objects.isNull(customerBillDay)){
            logger.info("客户业务项目信息为空!");
            return null;
        }
        //处理余额单元总金额
        X5551VO=this.authAmountFresh(customerBillDay,x5551BO);
        X5551VO.setAccountId(x5551BO.getAccountId());
        x5551BO.setCustomerNo(x5551BO.getCustomerNo());
        return X5551VO;
    }

    public Object busExecute2(X5551BO x5551BO) throws Exception {

        X5551VO X5551VO=null;
        //查询业务项目表
        CoreCustomerBillDay customerBillDay = queryCoreCustomerBillDay(x5551BO.getCustomerNo(), x5551BO.getBusinessProgramNo());
        if(Objects.isNull(customerBillDay)){
            logger.info("客户业务项目信息为空!");
            return null;
        }
        //处理余额单元总金额
        X5551VO=this.authAmountFresh(customerBillDay,x5551BO);
        X5551VO.setAccountId(x5551BO.getAccountId());
        x5551BO.setCustomerNo(x5551BO.getCustomerNo());
        return X5551VO;
    }

    /**
     * 查询账户
     *
     * @param customerNo
     * @return
     * @throws Exception
     */
    private CoreCustomerBillDay queryCoreCustomerBillDay(String customerNo, String businessProgramNo) throws Exception {
        CoreCustomerBillDay coreCustomerBillDay = new CoreCustomerBillDay();
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo).andBusinessProgramNoEqualTo(businessProgramNo)
                .andBillDayNotEqualTo(0);
        coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        return coreCustomerBillDay;
    }

    /**
     * 查询余额单元
     *
     * @param currency
     * @param accountNo
     * @return
     * @throws Exception
     */
    public List<CoreBalanceUnit> queryBalanceUnit(String currency, String accountNo) {
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        coreBalanceUnitSqlBuilder.orderByBalanceType(true);
        coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(currency).andAccountIdEqualTo(accountNo)
                .andBalanceGreaterThan(BigDecimal.ZERO);
        List<CoreBalanceUnit> list = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
        return list;
    }

    /**
     * @param coreCustomerBillDay
     * @throws Exception
     */
    private X5551VO authAmountFresh(CoreCustomerBillDay coreCustomerBillDay, X5551BO x5551BO)throws Exception {
        X5551VO x5551VO = new X5551VO();
        // 查询业务项目下业务类型
         CoreAccount coreAccount = queryAccountList(coreCustomerBillDay, x5551BO.getAccountId());
          String realTimeAccountRule = queryArt(x5551BO);
        if (StringUtil.isEmpty(realTimeAccountRule) || !Constant.CONCENTRATED_ACCOUNT.equals(realTimeAccountRule)) {
            // 查询账户余额单元的【首次结息周期号】不大于【当前周期号】的信息
            List<CoreBalanceUnit> balanceUnitList = queryBalanceUnit(coreAccount.getCurrencyCode(),
                    coreAccount.getAccountId());
            // 统计账单金额
             x5551VO = dealBalance(balanceUnitList, coreCustomerBillDay.getCurrentCycleNumber(),coreAccount.getCurrencyCode());
        }
        return x5551VO;
    }

    private String queryArt(X5551BO x5551BO){
        EventCommArea eventCommArea = new EventCommArea();
        eventCommArea.setEcommBusineseType(x5551BO.getBusinessTypeCode());
        eventCommArea.setEcommBusinessProgramCode(x5551BO.getBusinessProgramNo());
        eventCommArea.setEcommOperMode(x5551BO.getEcommOperMode());
        // 获取933-是否存在实时余额账户，933AAA0199-不进行实时余额刷新
        return queryRealTimeAccount(BSC.ARTIFACT_NO_933, eventCommArea);
    }

    // 客户号，业务项目
    private CoreAccount queryAccountList(CoreCustomerBillDay coreCustomerBillDay, String accountId) {
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId).andCustomerNoEqualTo(coreCustomerBillDay.getCustomerNo())
                .andBusinessProgramNoEqualTo(coreCustomerBillDay.getBusinessProgramNo());
        CoreAccount account = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        return account;
    }

    /**
     * @param artifactNo
     * @param eventCommArea
     * @return
     * @throws Exception
     */
    public String queryRealTimeAccount(String artifactNo, EventCommArea eventCommArea) {
        // 获取元件信息
        Map<String, String> resultMap = null;
        try {
            resultMap = artService.getElementByArtifact(artifactNo, eventCommArea);
            Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
            // 元件编码
            String realTimeAccountRule = null;
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String[] pcdKey = key.split("_");
                String currentKey = pcdKey[0];
                if (Constant.CONCENTRATED_ACCOUNT.equals(currentKey)) {
                    realTimeAccountRule = currentKey;
                }
                return realTimeAccountRule;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private X5551VO dealBalance(List<CoreBalanceUnit> balanceUnitList, int currentCycleNo,String currencyCode)throws  Exception {
        X5551VO x5551VO = new X5551VO();
        // 账单本金金额
        BigDecimal principalForBill = BigDecimal.ZERO;
        // 账单利息金额
        BigDecimal interestForBill = BigDecimal.ZERO;
        // 账单费用金额
        BigDecimal feeForBill = BigDecimal.ZERO;
        // 当期本金金额
        BigDecimal principalForCurrent = BigDecimal.ZERO;
        // 当期利息金额
        BigDecimal interestForCurrent = BigDecimal.ZERO;
        // 当期费用金额
        BigDecimal feeForCurrent = BigDecimal.ZERO;
        //总金额
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (CoreBalanceUnit coreBalanceUnit : balanceUnitList) {
            // 如果建立周期号=首次结息周期号，那就是当期;首次结息周期号大于当前周期号则为当期
            // 首次结息周期号小于等于当前周期号则为往期
            if ((coreBalanceUnit.getFirstBillingCycle() == coreBalanceUnit.getCycleNumber()
                    && coreBalanceUnit.getCycleNumber() == currentCycleNo)
                    || coreBalanceUnit.getFirstBillingCycle() > currentCycleNo) {
                if (Constant.BALANCE_TYPE_P.equals(coreBalanceUnit.getBalanceType())) {
                    principalForCurrent = principalForCurrent.add(coreBalanceUnit.getBalance());
                } else if (Constant.BALANCE_TYPE_I.equals(coreBalanceUnit.getBalanceType())) {
                    interestForCurrent = interestForCurrent.add(coreBalanceUnit.getBalance());
                } else if (Constant.BALANCE_TYPE_F.equals(coreBalanceUnit.getBalanceType())) {
                    feeForCurrent = feeForCurrent.add(coreBalanceUnit.getBalance());
                }
            } else if (coreBalanceUnit.getFirstBillingCycle() <= currentCycleNo) {
                //往期
                if (Constant.BALANCE_TYPE_P.equals(coreBalanceUnit.getBalanceType())) {
                    principalForBill = principalForBill.add(coreBalanceUnit.getBalance());
                } else if (Constant.BALANCE_TYPE_I.equals(coreBalanceUnit.getBalanceType())) {
                    interestForBill = interestForBill.add(coreBalanceUnit.getBalance());
                } else if (Constant.BALANCE_TYPE_F.equals(coreBalanceUnit.getBalanceType())) {
                    feeForBill = feeForBill.add(coreBalanceUnit.getBalance());
                }
            }
        }

        totalBalance = totalBalance.add(principalForCurrent).add(interestForCurrent).add(feeForCurrent).add(principalForBill)
                .add(interestForBill).add(feeForBill);

        int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
        x5551VO.setPrincipalForCurrent(CurrencyConversionUtil.reduce(principalForCurrent, currencyDecimal));
        x5551VO.setInterestForCurrent(CurrencyConversionUtil.reduce(interestForCurrent,currencyDecimal));
        x5551VO.setFeeForCurrent(CurrencyConversionUtil.reduce(feeForCurrent,currencyDecimal));
        x5551VO.setPrincipalForBill(CurrencyConversionUtil.reduce(principalForBill,currencyDecimal));
        x5551VO.setInterestForBill(CurrencyConversionUtil.reduce(interestForBill,currencyDecimal));
        x5551VO.setFeeForBill(CurrencyConversionUtil.reduce(feeForBill,currencyDecimal));
        x5551VO.setTotalBalance(CurrencyConversionUtil.reduce(totalBalance,currencyDecimal));
        return x5551VO;
    }
}