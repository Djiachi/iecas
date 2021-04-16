package com.tansun.ider.bus.impl;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5518Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerDelinquencyDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelinquency;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelinquencySqlBuilder;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5115BO;
import com.tansun.ider.model.vo.X5515VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @version:1.0
 * @Description: 客户延滞状况查询(子账户）
 * @author: gaozhennan 2020年4月1日
 */
@Service
public class X5518BusImpl implements X5518Bus {

    private static final Integer DELAY_MAX_CYCLE_NO = 9999;

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Resource
    private CoreCustomerDelinquencyDao coreCustomerDelinquencyDao;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    static final String BSS_IQ_01_0118 = "BSS.IQ.01.0118";


    @Override
    public Object busExecute(X5115BO x5115bo) throws Exception {
        PageBean<X5515VO> page = new PageBean<>();
        // 身份证号
        String idNumber = x5115bo.getIdNumber();
        //证件类型
        String idType = x5115bo.getIdType();
        String flag = x5115bo.getFlag();
        // 外部识别号
        String externalIdentificationNo = x5115bo.getExternalIdentificationNo();
        String customerNo = null;
        String operationMode = null;
        String accountId = x5115bo.getAccountId();
        String accountOrganForm = x5115bo.getAccountOrganForm();

        //账户形式
        String accFlag = x5115bo.getAccFlag();

        String currencyCode = x5115bo.getCurrencyCode();
        String businessProgramNo = x5115bo.getBusinessProgramNo();
        //全局流水号
        String globalTransSerialNo = x5115bo.getGlobalTransSerialNo();
        //业务类型
        String businessTypeCode = x5115bo.getBusinessTypeCode();
        x5115bo.getGlobalEventNo();
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        String productObjectCode = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            CoreCustomer coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
            operationMode = coreMediaBasicInfo.getOperationMode();
            productObjectCode = coreMediaBasicInfo.getProductObjectCode();
        }

        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        if (StringUtil.isNotBlank(accountOrganForm)) {
            coreAccountSqlBuilder.andAccountOrganFormEqualTo(accountOrganForm);
        }

        if (accFlag != null && accFlag.equals("mainAcc")) {
            //查询主账户信息。和循环类子账户
            //对于账户组织形式为R的，根据主账户业务类型查询“交易识别码”或“资金方代码”不为空的账户；
            if (accountOrganForm != null && accountOrganForm.equals(Constant.ACCOUNT_ORGAN_FORM_R) && businessTypeCode != null) {
                coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
                coreAccountSqlBuilder.andBusinessTypeCodeEqualTo(businessTypeCode);
                coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.P.getValue());
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.S.getValue());
            } else if (accountOrganForm != null && accountOrganForm.equals(Constant.ACCOUNT_ORGAN_FORM_S) && globalTransSerialNo != null) {
                coreAccountSqlBuilder.andGlobalTransSerialNoEqualTo(globalTransSerialNo);
                //查询展示“交易识别码”或“资金方代码”不为空的账户
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.P.getValue());
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.S.getValue());
                coreAccountSqlBuilder.orderByTransIdentifiNo(true);
                coreAccountSqlBuilder.orderByFundNum(true);
                coreAccountSqlBuilder.orderByAccountId(true);
            }
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (accountId != null && accountId.length() != 0) {
            coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        }

        int totalCount = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5115bo.getPageSize() && null != x5115bo.getIndexNo()) {
            coreAccountSqlBuilder.setIndexNo(x5115bo.getIndexNo());
            coreAccountSqlBuilder.setPageSize(x5115bo.getPageSize());
            page.setPageSize(x5115bo.getPageSize());
            page.setIndexNo(x5115bo.getIndexNo());
        }
        List<X5515VO> listX5515VO = new ArrayList<X5515VO>();
        if (totalCount > 0) {
            coreAccountSqlBuilder.orderByAccountId(false);
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            for (CoreAccount coreAccount : listCoreAccount) {
                X5515VO x5515VO = new X5515VO();
                x5515VO.setCoreAccount(coreAccount);
                CoreCustomerDelinquencySqlBuilder coreCustomerDelinquencySqlBuilder =
                        new CoreCustomerDelinquencySqlBuilder();

                coreCustomerDelinquencySqlBuilder.andCurrencyCodeEqualTo(coreAccount.getCurrencyCode());
                coreCustomerDelinquencySqlBuilder.andLevelCodeEqualTo(coreAccount.getAccountId());
                coreCustomerDelinquencySqlBuilder.andCycleNoEqualTo(DELAY_MAX_CYCLE_NO);
                int count = coreCustomerDelinquencyDao.countBySqlBuilder(coreCustomerDelinquencySqlBuilder);
                CoreCustomerDelinquency coreCustomerDelinquency = null;
                if (count>0) {
                    coreCustomerDelinquency =
                            coreCustomerDelinquencyDao.selectBySqlBuilder(coreCustomerDelinquencySqlBuilder);
                    CachedBeanCopy.copyProperties(coreCustomerDelinquency, x5515VO);
                    if (StringUtil.isNotBlank(coreCustomerDelinquency.getProductObjectNo())) {
                        CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,
                                coreCustomerDelinquency.getProductObjectNo());

                        if (coreProductObject != null) {
                            x5515VO.setProductDesc(coreProductObject.getProductDesc());
                        }
                    }
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                            coreCustomerDelinquency.getLevelCode());
                    if (null != coreBusinessType) {
                        x5515VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                    }

                    //金额转换
                    amountConversion(coreCustomerDelinquency, coreCustomerDelinquency.getCurrencyCode(), x5515VO);
                }
                if (accFlag != null && accFlag.equals("mainAcc")) {
                    x5515VO.setFlag("childAcc");
                }
                listX5515VO.add(x5515VO);
            }
            page.setRows(listX5515VO);

        }
        return page;

    }
    private void amountConversion(CoreCustomerDelinquency coreCustomerDelinquency, String currencyCode, X5515VO x5515VO)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreCustomerDelinquency.getCurrCyclePaymentMin() != null) {
            BigDecimal occurrAmount = CurrencyConversionUtil.reduce(coreCustomerDelinquency.getCurrCyclePaymentMin(),
                    decimalPlaces);
            x5515VO.setCurrCyclePaymentMin(occurrAmount);
        }
        if (coreCustomerDelinquency.getCurrCycleBeginPaymentMin() != null) {
            BigDecimal currCycleBeginPaymentMin = CurrencyConversionUtil
                    .reduce(coreCustomerDelinquency.getCurrCycleBeginPaymentMin(), decimalPlaces);
            coreCustomerDelinquency.setCurrCycleBeginPaymentMin(currCycleBeginPaymentMin);
        }
        if (null != coreCurrency) {
            x5515VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }

    }

}
