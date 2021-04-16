package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5552Bus;
import com.tansun.ider.dao.issue.CoreAccountBalanceCheckDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceCheck;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.model.CoreAccountMode;
import com.tansun.ider.model.CoreAuthBalanceModel;
import com.tansun.ider.model.CoreBalanceModel;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5552BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class X5552BusImpl implements X5552Bus {
    private static Logger logger = LoggerFactory.getLogger(X5552BusImpl.class);
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreAccountBalanceCheckDao coreAccountBalanceCheckDao;
    @Autowired
    private CardUtil cardUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${global.target.service.url.auth}")
    private String authUrl;

    @Override
    public Object busExecute(X5552BO x5552BO) throws Exception {
        String customerNo = x5552BO.getEcommCustId();

        List<CoreCustomerBillDay> customerBillDay = queryCoreCustomerBillDay(customerNo);
        if (CollectionUtils.isEmpty(customerBillDay)) {
            logger.error("客户业务项目信息为空!");
            return null;
        }
        List<CoreAccountBalanceCheck> checkList = new ArrayList<>();
        for (CoreCustomerBillDay coreCustomerBillDay : customerBillDay) {
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
            List<CoreAccount> accountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            //查询发卡
            if (CollectionUtils.isEmpty(accountList)) {
                logger.info("{}客户在发卡没有账户记录....", customerNo);
            }
            // 查询授权
            List<CoreAccountMode> accountBalanceAuthList = queryCurrBalance(customerNo, coreCustomerBillDay.getBusinessProgramNo());
            //获取账户+余额单元
            List<CoreAccountMode> accountBalanceCardList = queryCardAccount(accountList, coreCustomerBillDay.getCurrentCycleNumber());
            if (CollectionUtils.isEmpty(accountBalanceAuthList) || CollectionUtils.isEmpty(accountBalanceCardList)) {
                builderBalanceCheck(accountBalanceCardList,accountBalanceAuthList);
                insertDB(checkList);
                return "ok";
            }else if(CollectionUtils.isEmpty(accountBalanceAuthList)&&CollectionUtils.isEmpty(accountBalanceCardList)){
                logger.info("{}客户在发卡和授权都没有账户记录....", customerNo);
                return null;
            }
            checkList = checkBalance(accountBalanceCardList, accountBalanceAuthList);
            insertDB(checkList);
        }
        return "ok";
    }
    private void insertDB(List<CoreAccountBalanceCheck>checkList){
        if (!CollectionUtils.isEmpty(checkList)) {
            List<CoreAccountBalanceCheck> volist = new ArrayList<>();
            for (CoreAccountBalanceCheck accountBalanceCheck : checkList) {
                volist.add(accountBalanceCheck);
            }

            try {
                coreAccountBalanceCheckDao.insertUseBatch(volist);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理发卡余额汇总
     *
     * @param accountList
     */
    private List<CoreAccountMode> queryCardAccount(List<CoreAccount> accountList, int currentCycleNo) {
        List<CoreAccountMode> modelList = new ArrayList<>();
        //计算余额单元
        List<CoreBalanceModel> balanceUnitList = accountList.stream().parallel().map(account -> {
            return  queryCardbalance(account.getAccountId(), currentCycleNo, account.getCurrencyCode());
        }).collect(Collectors.toList());

        //合并账户+金额
        CoreAccountMode accountMode = null;
        List<CoreAccountMode> accountModeList = new ArrayList<>();
        for (CoreAccount account : accountList) {
            accountMode = new CoreAccountMode();
            CachedBeanCopy.copyProperties(account, accountMode);
            for (CoreBalanceModel balanceModel : balanceUnitList) {
                if (account.getAccountId().equals(balanceModel.getAccountId())) {
                    accountMode.setFeeForBill(checkData(balanceModel.getFeeForBill()));
                    accountMode.setFeeForCurrent(checkData(balanceModel.getFeeForCurrent()));
                    accountMode.setInterestForBill(checkData(balanceModel.getInterestForBill()));
                    accountMode.setInterestForCurrent(checkData(balanceModel.getInterestForCurrent()));
                    accountMode.setPrincipalForBill(checkData(balanceModel.getPrincipalForBill()));
                    accountMode.setPrincipalForCurrent(checkData(balanceModel.getPrincipalForCurrent()));
                    accountMode.setTotalBalance(checkData(balanceModel.getTotalBalance()));
                    accountModeList.add(accountMode);
                }
            }
        }
        //根据账户表中：客户号、业务项目、产品对象、业务类型、币种、交易识别再累计上一步汇总的“余额”；
        Map<String, List<CoreAccountMode>> map = new HashMap<>();

        for (CoreAccountMode accountMode1 : accountModeList) {
            String key = accountMode1.getBusinessProgramNo() + accountMode1.getProductObjectCode() + accountMode1.getCurrencyCode() + accountMode1.getBusinessTypeCode();
            if (map.containsKey(key)) {//map中存在此id，将数据存放当前key的map中
                map.get(key).add(accountMode1);
            } else {//map中不存在，新建key，用来存放数据
                List<CoreAccountMode> tmpList = new ArrayList<>();
                tmpList.add(accountMode1);
                map.put(key, tmpList);
            }
        }

        CoreAccountMode accountBalanceCard = null;
        List<CoreAccountMode> modes = null;
        BigDecimal principalForBill = null;
        BigDecimal interestForBill = null;
        BigDecimal feeForBill = null;
        BigDecimal principalForCurrent = null;
        BigDecimal interestForCurrent = null;
        BigDecimal feeForCurrent = null;
        for (String s : map.keySet()) {
            modes = map.get(s);
            principalForBill = new BigDecimal("0.00");
            // 账单利息金额
            interestForBill = new BigDecimal("0.00");
            // 账单费用金额
            feeForBill = new BigDecimal("0.00");
            // 当期本金金额
            principalForCurrent = new BigDecimal("0.00");
            // 当期利息金额
            interestForCurrent = new BigDecimal("0.00");
            // 当期费用金额
            feeForCurrent = new BigDecimal("0.00");
//            modelList = new ArrayList<>();
            accountBalanceCard = new CoreAccountMode();

            for (CoreAccountMode accountMode1 : modes) {
                feeForBill = feeForBill.add(checkData(accountMode1.getFeeForBill()));
                feeForCurrent = feeForCurrent.add(checkData(accountMode1.getFeeForCurrent()));
                interestForBill = interestForBill.add(checkData(accountMode1.getInterestForBill()));
                interestForCurrent = interestForCurrent.add(checkData(accountMode1.getInterestForCurrent()));
                principalForBill = principalForBill.add(checkData(accountMode1.getPrincipalForBill()));
                principalForCurrent = principalForCurrent.add(checkData(accountMode1.getPrincipalForCurrent()));
                CachedBeanCopy.copyProperties(accountMode1, accountBalanceCard);
            }
            accountBalanceCard.setFeeForCurrent(feeForCurrent);
            accountBalanceCard.setInterestForBill(interestForBill);
            accountBalanceCard.setInterestForCurrent(interestForCurrent);
            accountBalanceCard.setPrincipalForBill(principalForBill);
            accountBalanceCard.setPrincipalForCurrent(principalForCurrent);
            accountBalanceCard.setFeeForBill(feeForBill);
            modelList.add(accountBalanceCard);
        }
        return modelList;
    }


    private BigDecimal checkData(BigDecimal str) {
        if (str != null) {
            return str;
        } else {
            return new BigDecimal(BigInteger.ZERO);
        }
    }

    private CoreBalanceModel queryCardbalance(String accountId, int currentCycleNo, String currencyCode) {
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        coreBalanceUnitSqlBuilder.andAccountIdEqualTo(accountId);
        List<CoreBalanceUnit> balanceUnitlist = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
        CoreBalanceModel model = new CoreBalanceModel();
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
        for (CoreBalanceUnit coreBalanceUnit : balanceUnitlist) {
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
            CachedBeanCopy.copyProperties(coreBalanceUnit, model);
        }

        totalBalance = totalBalance.add(principalForCurrent).add(interestForCurrent).add(feeForCurrent).add(principalForBill)
                .add(interestForBill).add(feeForBill);

        try {
            int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
            model.setPrincipalForCurrent(CurrencyConversionUtil.reduce(principalForCurrent, currencyDecimal));
            model.setInterestForCurrent(CurrencyConversionUtil.reduce(interestForCurrent, currencyDecimal));
            model.setFeeForCurrent(CurrencyConversionUtil.reduce(feeForCurrent, currencyDecimal));
            model.setPrincipalForBill(CurrencyConversionUtil.reduce(principalForBill, currencyDecimal));
            model.setInterestForBill(CurrencyConversionUtil.reduce(interestForBill, currencyDecimal));
            model.setFeeForBill(CurrencyConversionUtil.reduce(feeForBill, currencyDecimal));
            model.setTotalBalance(CurrencyConversionUtil.reduce(totalBalance, currencyDecimal));

        } catch (Exception e) {
            logger.error("处理余额单元异常,{}",e);
        }
        return model;
    }

    /**
     * 查询实时余额 授权
     *
     * @param customerNo
     */
    private List<CoreAccountMode> queryCurrBalance(String customerNo, String businessProgramNo) {
        Map map = new HashMap();
        map.put("customerNo", customerNo);
        map.put("businessProjectCode", businessProgramNo);
        map.put("authDataSynFlag", "1");
//        map.put("requestType", "1");
        String params = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
        List<CoreAccountMode> list = new ArrayList<>();
        CoreAccountMode accountModeAuth = null;
        String triggerEventNo = EVENT.AUS_IQ_01_0004;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);
        String response = restTemplate.postForObject(authUrl + triggerEventNo, entity, String.class);
        ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
        if (responseVO.getReturnData() != null) {
            JSONObject jsonObj = JSON.parseObject(responseVO.getReturnData().toString());
            Object rows = jsonObj.get("rows");
            if (rows != null) {
                List<CoreAuthBalanceModel> balanceModelList = new ArrayList<>();
                balanceModelList = JSON.parseArray(rows.toString(), CoreAuthBalanceModel.class);
                if (!CollectionUtils.isEmpty(balanceModelList)) {
                    for (CoreAuthBalanceModel authBalanceModel : balanceModelList) {
                        accountModeAuth = new CoreAccountMode();//businessProjectCode  businessProjectCode
                        CachedBeanCopy.copyProperties(authBalanceModel, accountModeAuth);
                        accountModeAuth.setFeeForBill(authBalanceModel.getBillCostBalance());
                        accountModeAuth.setFeeForCurrent(authBalanceModel.getCurrCostBalance());
                        accountModeAuth.setPrincipalForCurrent(authBalanceModel.getCurrPrincipalBalance());
                        accountModeAuth.setPrincipalForBill(authBalanceModel.getBillPrincipalBalance());
                        accountModeAuth.setInterestForCurrent(authBalanceModel.getCurrInterestBalance());
                        accountModeAuth.setInterestForBill(authBalanceModel.getBillInterestBalance());
                        accountModeAuth.setBusinessProgramNo(authBalanceModel.getBusinessProjectCode());
                        accountModeAuth.setAccountId(authBalanceModel.getAccountId());
                        list.add(accountModeAuth);
                    }
                }

            }
        }
        return list;
    }

    /**
     * 检查账户类型及金额不平
     */
    private List<CoreAccountBalanceCheck> checkBalance(List<CoreAccountMode> accountBalanceCards, List<CoreAccountMode> accountBalanceAuths) {
        List<CoreAccountBalanceCheck> objectAll = new ArrayList<>();
        List<CoreAccountMode> compCard = new ArrayList<>();
        List<CoreAccountMode> compAuth = new ArrayList<>();
        CoreAccountBalanceCheck balanceCheckAll = null;
       logger.info("cards.size={}，auths.size={}", accountBalanceCards.size(),accountBalanceAuths.size());
        ListIterator<CoreAccountMode> iteratorCard = accountBalanceCards.listIterator();
        while(iteratorCard.hasNext()){
            CoreAccountMode coreAccountMode = iteratorCard.next();
            for (CoreAccountMode authAccountBalanceModel : accountBalanceAuths) {
                    if (coreAccountMode.getBusinessProgramNo().equals(authAccountBalanceModel.getBusinessProgramNo())
                            && coreAccountMode.getProductObjectCode().equals(authAccountBalanceModel.getProductObjectCode())
                            && coreAccountMode.getBusinessTypeCode().equals(authAccountBalanceModel.getBusinessTypeCode())
                            && coreAccountMode.getCurrencyCode().equals(authAccountBalanceModel.getCurrencyCode())
//                            && coreAccountMode.getTransIdentifiNo().equals(authAccountBalanceModel.getTransIdentifiNo())
                    ) {
                        if ((coreAccountMode.getPrincipalForBill().equals(authAccountBalanceModel.getPrincipalForBill())
                                && coreAccountMode.getFeeForBill().equals(authAccountBalanceModel.getFeeForBill())
                                && coreAccountMode.getInterestForBill().equals(authAccountBalanceModel.getInterestForBill())
                                && coreAccountMode.getPrincipalForCurrent().equals(authAccountBalanceModel.getPrincipalForCurrent())
                                && coreAccountMode.getFeeForCurrent().equals(authAccountBalanceModel.getFeeForCurrent())
                                && coreAccountMode.getInterestForCurrent().equals(authAccountBalanceModel.getInterestForCurrent())
                        )) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("金额相等类型相等");
                            }
                        } else {
                            balanceCheckAll = new CoreAccountBalanceCheck();
                            balanceCheckAll = balanceCheckAllCardBuilder(coreAccountMode,balanceCheckAll);
                            balanceCheckAll.setBillCostBalCard(authAccountBalanceModel.getFeeForBill());
                            balanceCheckAll.setBillInterestBalCard(authAccountBalanceModel.getInterestForBill());
                            balanceCheckAll.setBillPrincipalBalCard(authAccountBalanceModel.getPrincipalForBill());
                            balanceCheckAll.setCurrCostBalCard(authAccountBalanceModel.getFeeForCurrent());
                            balanceCheckAll.setCurrInterestBalCard(authAccountBalanceModel.getInterestForCurrent());
                            balanceCheckAll.setCurrPrincipalBalCard(authAccountBalanceModel.getPrincipalForCurrent());
                            balanceCheckAll.setAccountIdAuth(authAccountBalanceModel.getAccountId());
                            balanceCheckAll.setAccountIdCard(coreAccountMode.getAccountId());
                            balanceCheckAll.setTransIdentifiNo(authAccountBalanceModel.getTransIdentifiNo());
                            balanceCheckAll.setId(randomId());
                            balanceCheckAll.setGmtCreate(DateUtil.format(new Date()));
                            balanceCheckAll.setVersion(1);
                            objectAll.add(balanceCheckAll);
                            if (logger.isDebugEnabled()) {
                                logger.debug("金额不相等类型相等 ");
                            }
                        }
                        //存储类型相同金额不同+类型相同金额相同
                        compCard.add(coreAccountMode);
                        compAuth.add(authAccountBalanceModel);
                        continue;
                    }
//                }
            }

        }
        List<CoreAccountMode> coreAccountModeCard =null;
        List<CoreAccountMode> coreAccountModeAuth =null;
        if(!CollectionUtils.isEmpty(compCard)){
            coreAccountModeCard = accountBalanceCards.stream().filter(item ->
                    !compCard.contains(item)
            ).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(coreAccountModeCard)){
                for (CoreAccountMode coreAccountMode : coreAccountModeCard) {
                    balanceCheckAll = new CoreAccountBalanceCheck();
                    balanceCheckAllCardBuilder(coreAccountMode,balanceCheckAll);
                    balanceCheckAll.setGmtCreate(DateUtil.format(new Date()));
                    balanceCheckAll.setAccountIdCard(coreAccountMode.getAccountId());
                    balanceCheckAll.setTransIdentifiNo(coreAccountMode.getTransIdentifiNo());
                    balanceCheckAll.setId(randomId());
                    balanceCheckAll.setVersion(1);
                    objectAll.add(balanceCheckAll);
                }
            }
        }
        if(!CollectionUtils.isEmpty(compAuth)){
            coreAccountModeAuth = accountBalanceAuths.stream().filter(item ->
                    !compAuth.contains(item)
            ).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(coreAccountModeAuth)){
                for (CoreAccountMode coreAccountMode : coreAccountModeAuth) {
                    balanceCheckAll = new CoreAccountBalanceCheck();
                    balanceCheckAllCardBuilder(coreAccountMode,balanceCheckAll);
                    balanceCheckAll.setGmtCreate(DateUtil.format(new Date()));
                    balanceCheckAll.setAccountIdAuth(coreAccountMode.getAccountId());
                    balanceCheckAll.setId(randomId());
                    balanceCheckAll.setVersion(1);
                    objectAll.add(balanceCheckAll);
                }
            }
        }
        return objectAll;
    }

    private List<CoreAccountBalanceCheck> builderBalanceCheck(List<CoreAccountMode>accountBalanceCards, List<CoreAccountMode>accountBalanceAuths){
        CoreAccountBalanceCheck balanceCheckAll= null;
        List<CoreAccountBalanceCheck>objectAll =new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountBalanceCards)) {
            balanceCheckAll = new CoreAccountBalanceCheck();
            for (CoreAccountMode coreAccountMode : accountBalanceCards) {
                balanceCheckAll = new CoreAccountBalanceCheck();
                balanceCheckAllCardBuilder(coreAccountMode,balanceCheckAll);
                balanceCheckAll.setGmtCreate(DateUtil.format(new Date()));
                balanceCheckAll.setId(randomId());
                balanceCheckAll.setVersion(1);
                objectAll.add(balanceCheckAll);
            }
        }
        //考虑authbalance 有的类型cardbalance没有的状况
        if (!CollectionUtils.isEmpty(accountBalanceAuths)) {
            balanceCheckAll = new CoreAccountBalanceCheck();
            for (CoreAccountMode authAccountBalance : accountBalanceAuths) {
                balanceCheckAll = new CoreAccountBalanceCheck();
                balanceCheckAllAuthBuilder(authAccountBalance,balanceCheckAll);
                balanceCheckAll.setGmtCreate(DateUtil.format(new Date()));
                balanceCheckAll.setId(randomId());
                balanceCheckAll.setVersion(1);
                balanceCheckAll.setAccountIdAuth(authAccountBalance.getAccountId());
                objectAll.add(balanceCheckAll);
            }
        }
        return  objectAll;
    }
    /**
     * 查询业务项目日期表
     *
     * @param customerNo
     * @return
     * @throws Exception
     */
    private List<CoreCustomerBillDay> queryCoreCustomerBillDay(String customerNo) throws Exception {
        List<CoreCustomerBillDay> coreCustomerBillDay = new ArrayList<>();
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerBillDay = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
        return coreCustomerBillDay;
    }

    /**
     * 生成随机id
     * @return
     */
    private String randomId() {
//        SecureRandom random = new SecureRandom();
//        Long id = random.nextLong();
        return RandomUtil.getUUID();
    }

    /**
     * 组装发卡金额
     * @param cardMode
     * @param balanceCheckCardAll
     * @return
     */
    private CoreAccountBalanceCheck balanceCheckAllCardBuilder (CoreAccountMode cardMode,CoreAccountBalanceCheck balanceCheckCardAll){
        CachedBeanCopy.copyProperties(cardMode, balanceCheckCardAll);
        balanceCheckCardAll.setBillCostBalCard(cardMode.getFeeForBill());
        balanceCheckCardAll.setBillInterestBalCard(cardMode.getInterestForBill());
        balanceCheckCardAll.setBillPrincipalBalCard(cardMode.getPrincipalForBill());
        balanceCheckCardAll.setCurrCostBalCard(cardMode.getFeeForCurrent());
        balanceCheckCardAll.setCurrInterestBalCard(cardMode.getInterestForCurrent());
        balanceCheckCardAll.setCurrPrincipalBalCard(cardMode.getPrincipalForCurrent());
        return balanceCheckCardAll;
    }

    /**
     * 组装授权金额
     * @param authMode
     * @param balanceCheckAuthAll
     * @return
     */
    private CoreAccountBalanceCheck balanceCheckAllAuthBuilder (CoreAccountMode authMode,CoreAccountBalanceCheck balanceCheckAuthAll){
        CachedBeanCopy.copyProperties(authMode, balanceCheckAuthAll);
        balanceCheckAuthAll.setBillCostBalAuth(authMode.getFeeForBill());
        balanceCheckAuthAll.setBillInterestBalAuth(authMode.getInterestForBill());
        balanceCheckAuthAll.setBillPrincipalBalAuth(authMode.getPrincipalForBill());
        balanceCheckAuthAll.setCurrCostBalAuth(authMode.getFeeForCurrent());
        balanceCheckAuthAll.setCurrInterestBalAuth(authMode.getInterestForCurrent());
        balanceCheckAuthAll.setCurrPrincipalBalAuth(authMode.getPrincipalForCurrent());
        balanceCheckAuthAll.setAccountIdAuth(authMode.getAccountId());
        return balanceCheckAuthAll;
    }

}