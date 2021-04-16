package com.tansun.ider.bus.impl;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5980Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.InstallTransAndBaseAccountMapper;
import com.tansun.ider.enums.CapitalStageEnum;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5980BO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.NonFinancialLogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择+封包
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
public class X5980BusImpl implements X5980Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    public CoreNmnyLogDao coreNmnyLogDao;
    @Autowired
    public CoreNmnyLogBDao coreNmnyLogBDao;

    private static Logger logger = LoggerFactory.getLogger(X5980BusImpl.class);
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    InstallTransAndBaseAccountMapper installTransAndBaseAccountMapper;
    @Override
    public Object busExecute(X5980BO x5980BO) throws Exception {

        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5980BO, eventCommAreaNonFinance);
        //ABS计划号
        String planId = x5980BO.getPlanId();

        // 身份证号
        String idNumber = x5980BO.getIdNumber();
        //证件类型-
        String idType = x5980BO.getIdType();
        // 外部识别号
        String externalIdentificationNo = x5980BO.getExternalIdentificationNo();
        //判断卡号和证件号为空报错
        if (StringUtil.isBlank(externalIdentificationNo) && StringUtil.isBlank(idNumber)
                && StringUtil.isBlank(idType)) {
            throw new BusinessException("COR-10048");
        }
        //客户号
        String customerNo = x5980BO.getCustomerNo();
        CoreMediaBasicInfo coreMediaBasicInfo;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            CoreCustomer coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
        }

        // 当前操作员
        String operatorId = x5980BO.getOperatorId();

        //查询未更新前的账户信息
        List<CoreAccount> accountList = x5980BO.getAccountList();
        logger.info("accountList size:" +accountList.size());
        //避免前端重复传值 将 distinctByKey() 方法作为 filter() 的参数，过滤掉那些不能加入到 set 的元素
        accountList = accountList.stream().filter(distinctByKey(CoreAccount::getId)).collect(Collectors.toList());
        if (null != accountList && !accountList.isEmpty()) {
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
            coreAccountSqlBuilder.andBusinessDebitCreditCodeEqualTo(Constant.BOOKKEEPINGDIREC_D);
            List<CoreAccount> coreAccountsCus = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            CoreAccount accountAfter;
            for (CoreAccount coreAccount : accountList) {
                if (logger.isDebugEnabled()) {
                    logger.debug("coreAccount:" + coreAccount.toString());
                }
                String accountId = coreAccount.getAccountId();
                String currencyCode = coreAccount.getCurrencyCode();
                List<CoreAccount> coreAccounts = coreAccountsCus.stream().filter(x -> StringUtils.equals(x.getAccountId(), accountId) && x.getCurrencyCode().equals(currencyCode)).collect(Collectors.toList());
                if (coreAccounts.isEmpty()) {
                    logger.error("coreAccount不存在,accountId:{}，currencyCode:{}", accountId, currencyCode);
                    throw new BusinessException("未查到账户信息");
                }
                accountAfter = coreAccounts.get(0);
                CoreAccount coreAccountBefore = accountAfter.clone();
                accountAfter.setAbsPlanId(planId);
                //更新账户信息表资产转变阶段为“PACK-封包
                accountAfter.setCapitalStage(CapitalStageEnum.PACK.getValue());
                if(!StringUtils.equals(SubAccountIdentify.P.getValue(),coreAccount.getSubAccIdentify()) && !StringUtils.equals(SubAccountIdentify.S.getValue(),coreAccount.getSubAccIdentify())){
                    //非主账户 更新
                    updateAccountNonFinancialLog(x5980BO, eventCommAreaNonFinance, customerNo, operatorId, accountAfter, coreAccountBefore);
                    //跳过后续
                    continue;
                }
                //交易账户-分期 查询是否有子账户
                if (StringUtils.equals(coreAccount.getAccountOrganForm(), Constant.ACCOUNT_ORGAN_FORM_S)
                        && StringUtils.equals(coreAccount.getCycleModeFlag(), YesOrNo.YES.getValue())) {
                    InstallAccountBean installAccountBean = new InstallAccountBean();
                    if (StringUtil.isNotBlank(accountAfter.getGlobalTransSerialNo())) {
                        installAccountBean.setGlobalTransSerialNo(accountAfter.getGlobalTransSerialNo());
                        int count = installTransAndBaseAccountMapper.countBySqlBuilderForNotQuotaChildList(installAccountBean);
                        if (count < 1) {
                            updateAccountNonFinancialLog(x5980BO, eventCommAreaNonFinance, customerNo, operatorId, accountAfter, coreAccountBefore);
                            //无子账户 更新主账户
                            continue;
                        }
                    }
                }
                //循环账户 查询是否有子账户
                if (StringUtils.equals(coreAccountBefore.getAccountOrganForm(), Constant.ACCOUNT_ORGAN_FORM_R)){
                    updateAccountNonFinancialLog(x5980BO, eventCommAreaNonFinance, customerNo, operatorId, accountAfter, coreAccountBefore);
                    coreAccounts = coreAccountsCus.stream().filter(x ->(!StringUtils.equals(SubAccountIdentify.P.getValue(), x.getSubAccIdentify()) && !StringUtils.equals(SubAccountIdentify.S.getValue(), x.getSubAccIdentify()) && StringUtils.equals(x.getBusinessProgramNo(), coreAccountBefore.getBusinessProgramNo()) &&  StringUtils.equals(x.getProductObjectCode(), coreAccountBefore.getProductObjectCode()) && StringUtils.equals(x.getBusinessTypeCode(), coreAccountBefore.getBusinessTypeCode()) && x.getCurrencyCode().equals(coreAccountBefore.getCurrencyCode()))).collect(Collectors.toList());
                    if (coreAccounts.isEmpty()) {
                        if(StringUtils.equals(Constant.TRANSFER_NEW,x5980BO.getType()) && StringUtils.equals(CapitalStageEnum.REPO.getValue(),coreAccountBefore.getCapitalStage())){
                            throw new BusinessException("COR-12200", "该账户无新资产");
                        }
                        continue;
                    }
                    if(StringUtils.equals(Constant.TRANSFER_NEW,x5980BO.getType())){
                        //新资产转让 无新资产报错
                        coreAccounts = coreAccounts.stream().filter(x -> StringUtils.isEmpty(x.getCapitalStage())).collect(Collectors.toList());
                        if(coreAccounts.isEmpty()){
                            throw new BusinessException("COR-12200", "该账户无新资产");
                        }
                    }
                    for (CoreAccount accountS:coreAccounts) {
                        //更新账户信息表资产转变阶段为“PACK-封包
                        updateCoreAccount(accountS, planId , CapitalStageEnum.PACK.getValue());
                    }
                }
            }
        }
        return eventCommAreaNonFinance;
    }

    /**
     * 更新账户记录非金融日志
     * @param x5980BO
     * @param eventCommAreaNonFinance
     * @param customerNo
     * @param operatorId
     * @param accountAfter  afterObj
     * @param coreAccountBefore beforeObj
     * @throws Exception
     */
    private void updateAccountNonFinancialLog(X5980BO x5980BO, EventCommAreaNonFinance eventCommAreaNonFinance, String customerNo, String operatorId, CoreAccount accountAfter, CoreAccount coreAccountBefore) throws Exception {

        updateCoreAccount(coreAccountBefore,accountAfter.getAbsPlanId() , accountAfter.getCapitalStage());

        CachedBeanCopy.copyProperties(accountAfter, eventCommAreaNonFinance);
        //获取系统单元信息
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        // 当前操作员
        if (operatorId == null) {
            operatorId = "system";
        }
        //新增非金融活动日志
        nonFinancialLogUtil.createNonFinancialActivityLog(x5980BO.getEventNo(), x5980BO.getActivityNo(),
                ModificationType.UPD.getValue(), null, coreAccountBefore, accountAfter,
                accountAfter.getId(), coreSystemUnit.getCurrLogFlag(), operatorId,
                customerNo, coreAccountBefore.getProductObjectCode(), null, null);

    }

    /**
     * 乐观锁模式更新账户
     * @param coreAccount 原账户
     * @param absPlanId ABS计划号
     * @param capitalStage 资产阶段
     */
    public void updateCoreAccount(CoreAccount coreAccount, String absPlanId , String capitalStage)  {
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andIdEqualTo(coreAccount.getId()).andVersionEqualTo(coreAccount.getVersion());
        CoreAccount account = new CoreAccount();
        if(StringUtil.isNotEmpty(absPlanId)){
            account.setAbsPlanId(absPlanId);
        }
        if(StringUtil.isNotEmpty(capitalStage)){
            account.setCapitalStage(capitalStage);
        }
        account.setVersion(coreAccount.getVersion() + 1);
        int count = coreAccountDao.updateBySqlBuilderSelective(account, coreAccountSqlBuilder);
        if (count != 1) {
            logger.warn("5980,更新账户失败,accountId:{},account{}",coreAccount.getAccountId(),account.toString());
            throw new BusinessException("COR-00003");
        }
    }
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
