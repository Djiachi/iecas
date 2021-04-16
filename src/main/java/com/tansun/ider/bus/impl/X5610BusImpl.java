package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5610Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ProductType;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5610BO;
import com.tansun.ider.service.BusinessTypeMatchService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 消贷分期账户信息查询
 *
 * @author huangyayun 2018年10月30日
 */
@Service
public class X5610BusImpl implements X5610Bus {

    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;// 分期交易账户表
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    private BusinessTypeMatchService businessTypeMatchService;

    @Override
    public Object busExecute(X5610BO x5610bo) throws Exception {
        String idNumber = x5610bo.getIdNumber();
        String externalIdentificationNo = x5610bo.getExternalIdentificationNo();
        String customerNo = null;
        PageBean<CoreInstallmentTransAcct> page = new PageBean<>();
        if (StringUtil.isNotBlank(idNumber)) {
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            if (coreCustomer != null) {
                customerNo = coreCustomer.getCustomerNo();
            }
        }
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        if (StringUtil.isNotBlank(externalIdentificationNo)) {
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
            if (coreMediaBasicInfo != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
            }
        }
        CoreEvent coreEvent = httpQueryService.queryEvent(x5610bo.getEventId());
        String transIdentificationCode = coreEvent.getTransIdentificationCode();
        if (StringUtil.isBlank(transIdentificationCode)) {
            transIdentificationCode = "X001";
        }
        boolean checkMark =
                checkProductType(transIdentificationCode, coreMediaBasicInfo.getProductObjectCode(), coreMediaBasicInfo.getOperationMode());
        if (checkMark) {
            CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
            if (StringUtil.isNotEmpty(customerNo) && StringUtil.isNotBlank(customerNo)) {
                coreInstallmentTransAcctSqlBuilder.andCustomerNoEqualTo(customerNo);
            }
            if (StringUtil.isNotBlank(externalIdentificationNo) && StringUtil.isNotEmpty(externalIdentificationNo)) {
                coreInstallmentTransAcctSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            }
            int totalCount = coreInstallmentTransAcctDao.countBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
            page.setTotalCount(totalCount);
            if (null != x5610bo.getPageSize() && null != x5610bo.getIndexNo()) {
                coreInstallmentTransAcctSqlBuilder.orderByLoanStartDate(false);
                coreInstallmentTransAcctSqlBuilder.setPageSize(x5610bo.getPageSize());
                coreInstallmentTransAcctSqlBuilder.setIndexNo(x5610bo.getIndexNo());
                page.setPageSize(x5610bo.getPageSize());
                page.setIndexNo(x5610bo.getIndexNo());
            }
            if (totalCount > 0) {
                List<CoreInstallmentTransAcct> list =
                        coreInstallmentTransAcctDao.selectListBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
                for (CoreInstallmentTransAcct coreInstallmentTransAcct : list) {
                    amountConversion(coreInstallmentTransAcct, coreInstallmentTransAcct.getCurrencyCode());
                }
                page.setRows(list);
            }
        }
        return page;
    }

    private void amountConversion(CoreInstallmentTransAcct coreInstallmentTransAcct, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        BigDecimal loanAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getLoanAmount(), decimalPlaces);
        coreInstallmentTransAcct.setLoanAmount(loanAmount);
        BigDecimal remainPrincipalAmount =
                CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainPrincipalAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainPrincipalAmount(remainPrincipalAmount);
        BigDecimal feeAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getFeeAmount(), decimalPlaces);
        coreInstallmentTransAcct.setFeeAmount(feeAmount);
        BigDecimal remainFeeAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainFeeAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainFeeAmount(remainFeeAmount);
        BigDecimal interAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getInterAmount(), decimalPlaces);
        coreInstallmentTransAcct.setInterAmount(interAmount);
        BigDecimal remainInterAmount = CurrencyConversionUtil.reduce(coreInstallmentTransAcct.getRemainInterAmount(), decimalPlaces);
        coreInstallmentTransAcct.setRemainInterAmount(remainInterAmount);
    }

    /**
     * 验证产品是否是信贷产品
     *
     * @param x8460Bo
     * @param artService
     * @return
     */
    public boolean checkProductType(String ecommTransIdentiNo, String ecommProdObjId, String ecommOperMode) throws Exception {
        EventCommArea eventCommArea = new EventCommArea();
        // ecommTransIdentiNo = "X001";
        boolean cheakMark = true;
        eventCommArea.setEcommTransIdentiNo(ecommTransIdentiNo);
        eventCommArea.setEcommProdObjId(ecommProdObjId);
        eventCommArea.setEcommOperMode(ecommOperMode);
        Map<String, String> map = businessTypeMatchService.getBusinessTypeByMatch(eventCommArea);
        if (map == null) {
            return cheakMark = false;
        }
        String businessTypeCode = map.get("businessTypeCode");
        String businessProgramCode = map.get("businessProgramCode");
        CoreBusinessProgram businessProgram = httpQueryService.queryBusinessProgram(eventCommArea.getEcommOperMode(), businessProgramCode);
        if (businessProgram == null) {
            // TODO产品不存在
            cheakMark = false;
        }
        CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(eventCommArea.getEcommOperMode(), businessTypeCode);
        if (coreBusinessType == null) {
            // TODO产品不存在
            cheakMark = false;
        }
        if (!ProductType.LOAN.getValue().equals(businessProgram.getProgramType())) {
            cheakMark = false;
        }
        String businessForm = coreBusinessType.getBusinessForm();
        if (!"S1".equals(businessForm) && !"S2".equals(businessForm)) {
            // todo 不是s1业务形态，不能做S1业务
            cheakMark = false;
        }
        return cheakMark;
    }

}
