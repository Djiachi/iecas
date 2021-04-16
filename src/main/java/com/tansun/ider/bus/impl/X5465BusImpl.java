package com.tansun.ider.bus.impl;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5465Bus;
import com.tansun.ider.dao.auth.entity.AuthAccountBalance;
import com.tansun.ider.dao.beta.entity.*;
import com.tansun.ider.dao.issue.CoreCustomerDelinquencyDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelinquency;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelinquencySqlBuilder;
import com.tansun.ider.enums.DelayLevel;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5465BO;
import com.tansun.ider.model.vo.X5465VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BaseConvert;
import com.tansun.ider.util.ParamsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户核算状态查询
 *
 * @author PanQi
 */
@Service
public class X5465BusImpl implements X5465Bus {
    private static Logger logger = LoggerFactory.getLogger(X5465BusImpl.class);
    private static final Integer DELAY_MAX_CYCLE_NO = 9999;

    @Resource
    private CoreCustomerDelinquencyDao coreCustomerDelinquencyDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private QueryCustomerService queryCustomerService;

    @Override
    public Object busExecute(X5465BO x5465bo) throws Exception {
        String idNumber = x5465bo.getIdNumber();
        String idType = x5465bo.getIdType();
        String externalIdentificationNo = x5465bo.getExternalIdentificationNo();
        String customerNo = null;
        String entrys = Constant.EMPTY_LIST;
        PageBean<X5465VO> page = new PageBean<>();
        CoreMediaBasicInfo coreMediaBasicInfo;
        //查询客户
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        CoreCustomer coreCustomer = null;
        String operationMode = "";
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
            operationMode = coreMediaBasicInfo.getOperationMode();
        }

        CoreCustomerDelinquencySqlBuilder coreCustomerDelinquencySqlBuilder = new CoreCustomerDelinquencySqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreCustomerDelinquencySqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        //查询9999的Delinquency
        coreCustomerDelinquencySqlBuilder.andCycleNoEqualTo(DELAY_MAX_CYCLE_NO);
        int totalCount = coreCustomerDelinquencyDao.countBySqlBuilder(coreCustomerDelinquencySqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5465bo.getPageSize() && null != x5465bo.getIndexNo()) {
            coreCustomerDelinquencySqlBuilder.orderByDelinquencyLevel(false);
            coreCustomerDelinquencySqlBuilder.orderByLevelCode(false);
            coreCustomerDelinquencySqlBuilder.orderByProductObjectNo(false);
            coreCustomerDelinquencySqlBuilder.orderByCurrencyCode(false);
            coreCustomerDelinquencySqlBuilder.orderByCycleNo(false);
            coreCustomerDelinquencySqlBuilder.setPageSize(x5465bo.getPageSize());
            coreCustomerDelinquencySqlBuilder.setIndexNo(x5465bo.getIndexNo());
            page.setPageSize(x5465bo.getPageSize());
            page.setIndexNo(x5465bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreCustomerDelinquency> list = coreCustomerDelinquencyDao
                    .selectListBySqlBuilder(coreCustomerDelinquencySqlBuilder);
            List<X5465VO> listX5465VO = new ArrayList<>();
            //为了不在循环内查询，先将所有数据查询出来
            //读取授权实时余额文件 auth_account_balance
            List<AuthAccountBalance> authAccountBalanceList = httpQueryService.queryAuthAccountBalanceList(customerNo, null);
            if (logger.isDebugEnabled()) {
                logger.debug("=============authAccountBalanceList->{}",
                        authAccountBalanceList);
            }
            List<AuthAccountBalance> authAccountBalances = new ArrayList<>();
            List<CoreCurrency> coreCurrencys = httpQueryService.queryCurrencyList("all");
            List<CoreAccountingStatus> coreAccountingStatusList = httpQueryService.queryCoreAccountingStatusList(operationMode, "all");
            CoreCurrency coreCurrency = null;
            for (CoreCustomerDelinquency coreCustomerDelinquency : list) {
                X5465VO x5465VO = new X5465VO();
                // 金额转换
                Optional<CoreCurrency> first = coreCurrencys.stream().parallel()
                        .filter(x -> StringUtils.equals(x.getCurrencyCode(), coreCustomerDelinquency.getCurrencyCode())).findFirst();
                if (first.isPresent()) {
                    coreCurrency = first.get();
                }
                amountConversion(coreCustomerDelinquency, coreCurrency, x5465VO);
                x5465VO = BaseConvert.convert(coreCustomerDelinquency, X5465VO.class);
                x5465VO.setOperationMode(operationMode);
                String productObjectNo = coreCustomerDelinquency.getProductObjectNo();
                String delinquencyLevel = coreCustomerDelinquency.getDelinquencyLevel();
                if (StringUtil.isNotBlank(productObjectNo)) {
                    CoreProductObject coreProductObject = httpQueryService.queryProductObject(
                            operationMode, coreCustomerDelinquency.getProductObjectNo());
                    if (coreProductObject != null) {
                        x5465VO.setProductDesc(coreProductObject.getProductDesc());
                    }
                }
                //获取客户
                if (null == coreCustomer){
                    coreCustomer = httpQueryService.queryCustomer(idNumber, customerNo);
                }
                x5465VO.setCustomerName(coreCustomer.getCustomerName());
                x5465VO.setIdNumber(coreCustomer.getIdNumber());
                if (coreCustomer != null) {
                    operationMode = coreCustomer.getOperationMode();
                }
                if (StringUtil.isNotBlank(delinquencyLevel)) {
                    //延滞级别A
                    if (StringUtils.equals(DelayLevel.DELAY_LEVEL_A.getValue(), delinquencyLevel)) {
                        //对于账户级记录，要将账户下的所有账户的本金、利息、费用汇总
                        authAccountBalances = authAccountBalanceList.stream().filter(s -> (StringUtils.equals(s.getAccountId(),coreCustomerDelinquency.getLevelCode()) && StringUtils.equals(s.getCurrencyCode(),coreCustomerDelinquency.getCurrencyCode()))).collect(Collectors.toList());
                        CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode, coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessType) {
                            x5465VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                        }
                    } else if (StringUtils.equals(DelayLevel.DELAY_LEVEL_G.getValue(), delinquencyLevel)) {
                        //延滞级别 G 对于业务项目级记录，要将该业务项目下的所有账户的本金、利息、费用汇总
                        authAccountBalances = authAccountBalanceList.stream().filter(s -> (StringUtils.equals(coreCustomerDelinquency.getLevelCode(),s.getBusinessProjectCode()) && StringUtils.equals(s.getCurrencyCode(),coreCustomerDelinquency.getCurrencyCode()))).collect(Collectors.toList());
                        CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode, coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessProgram) {
                            x5465VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
                        }
                    } else if (StringUtils.equals(DelayLevel.DELAY_LEVEL_P.getValue(), delinquencyLevel)) {
                        //延滞级别 P  对于产品对象级记录，要将该产品对象下的所有账户的本金、利息、费用汇总
                        authAccountBalances = authAccountBalanceList.stream().filter(s -> (StringUtils.equals(s.getProductObjectCode(),coreCustomerDelinquency.getProductObjectNo()) && StringUtils.equals(s.getCurrencyCode(),coreCustomerDelinquency.getCurrencyCode()))).collect(Collectors.toList());
                        CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode, coreCustomerDelinquency.getLevelCode());
                        if (null != coreBusinessType) {
                            x5465VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                        }
                    }
                    getBalanceAmount(authAccountBalances, x5465VO);
                }
                //核算状态查询
                CoreAccountingStatus coreAccountingStatus = new CoreAccountingStatus();
                Optional<CoreAccountingStatus> firstCoreAccountingStatus = coreAccountingStatusList.stream().parallel()
                        .filter(x -> StringUtils.equals(x.getAccountingStatus(), coreCustomerDelinquency.getAccountingStatusCode())).findFirst();
                if (firstCoreAccountingStatus.isPresent()) {
                    coreAccountingStatus = firstCoreAccountingStatus.get();
                }

                x5465VO.setNextAccountingStatus(coreAccountingStatus.getNextAccountingStatus());
                x5465VO.setPrevAccountingStatus(coreAccountingStatus.getPrevAccountingStatus());
                String next = coreAccountingStatus.getNextAccountingStatus();
                String prev = coreAccountingStatus.getPrevAccountingStatus();
                if(StringUtils.isNotEmpty(coreAccountingStatus.getNextAccountingStatus())){
                    //核算状态查询
                    firstCoreAccountingStatus = coreAccountingStatusList.stream().parallel()
                            .filter(x -> StringUtils.equals(x.getAccountingStatus(),next)).findFirst();
                    if (firstCoreAccountingStatus.isPresent()) {
                        coreAccountingStatus = firstCoreAccountingStatus.get();
                    }
                    x5465VO.setNextAccountingStatusDesc(coreAccountingStatus.getAccountingDesc());
                }
                if(StringUtils.isNotEmpty(coreAccountingStatus.getPrevAccountingStatus())){
                    //核算状态查询
                    firstCoreAccountingStatus = coreAccountingStatusList.stream().parallel()
                            .filter(x -> StringUtils.equals(x.getAccountingStatus(),prev)).findFirst();
                    if (firstCoreAccountingStatus.isPresent()) {
                        coreAccountingStatus = firstCoreAccountingStatus.get();
                    }
                    x5465VO.setPrevAccountingStatusDesc(coreAccountingStatus.getAccountingDesc());
                }
                listX5465VO.add(x5465VO);
            }
            page.setRows(listX5465VO);
            if (!listX5465VO.isEmpty()) {
                entrys = listX5465VO.get(0).getId();
            }
            // 记录查询日志
            CoreEvent tempObject = new CoreEvent();
            paramsUtil.logNonInsert(x5465bo.getCoreEventActivityRel().getEventNo(),
                    x5465bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                    x5465bo.getOperatorId());
        }
        return page;
    }

    /**
     /**
     * 金额转换
     *
     * @param coreCustomerDelinquency 延滞信息
     * @param coreCurrency            币种信息
     * @param x5465VO                 返回值
     * @throws Exception
     */
    private void amountConversion(CoreCustomerDelinquency coreCustomerDelinquency,  CoreCurrency coreCurrency, X5465VO x5465VO)
            throws Exception {

        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreCustomerDelinquency.getCurrCyclePaymentMin() != null) {
            BigDecimal occurrAmount = CurrencyConversionUtil.reduce(coreCustomerDelinquency.getCurrCyclePaymentMin(),
                    decimalPlaces);
            coreCustomerDelinquency.setCurrCyclePaymentMin(occurrAmount);
        }
        if (coreCustomerDelinquency.getCurrCycleBeginPaymentMin() != null) {
            BigDecimal currCycleBeginPaymentMin = CurrencyConversionUtil.reduce(coreCustomerDelinquency.getCurrCycleBeginPaymentMin(),
                    decimalPlaces);
            coreCustomerDelinquency.setCurrCycleBeginPaymentMin(currCycleBeginPaymentMin);
        }
        x5465VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
    }

    /**
     * 将账户的当期本金+账单本金 累计出本金
     * 将账户的当期利息+账单利息累计出利息
     * 将账户的当期费用+账单费用 累计出费用
     */
    private void getBalanceAmount(List<AuthAccountBalance> authAccountBalances, X5465VO x5465VO) throws Exception {

        if (authAccountBalances.isEmpty()) {
            //"未找到实时余额"
            throw new BusinessException("COR-10005");
        }
        //本金
        BigDecimal prinAmount = authAccountBalances.stream().map(AuthAccountBalance::getBillPrincipalBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        prinAmount = authAccountBalances.stream().map(AuthAccountBalance::getCurrPrincipalBalance).reduce(prinAmount, BigDecimal::add);
        //利息
        BigDecimal intAmount = authAccountBalances.stream().map(AuthAccountBalance::getBillInterestBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        intAmount = authAccountBalances.stream().map(AuthAccountBalance::getCurrInterestBalance).reduce(intAmount, BigDecimal::add);
        //费用
        BigDecimal feeAmount = authAccountBalances.stream().map(AuthAccountBalance::getBillCostBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        feeAmount = authAccountBalances.stream().map(AuthAccountBalance::getCurrCostBalance).reduce(feeAmount, BigDecimal::add);
        x5465VO.setPrinAmount(prinAmount);
        x5465VO.setIntAmount(intAmount);
        x5465VO.setFeeAmount(feeAmount);
    }
}
