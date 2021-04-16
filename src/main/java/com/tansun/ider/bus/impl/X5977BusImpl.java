package com.tansun.ider.bus.impl;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5977Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreStageType;
import com.tansun.ider.dao.nonfinance.mapper.InstallTransAndBaseAccountMapper;
import com.tansun.ider.enums.AccountTypeEnum;
import com.tansun.ider.enums.CapitalStageEnum;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.enums.TureOrFalse;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5631BO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.AccountingService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryAccountService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择子账户查询 参考5631 信用卡分期账户信息查询
 */
@Service
public class X5977BusImpl implements X5977Bus {
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    private QueryAccountService queryAccountService;
    @Resource
    InstallTransAndBaseAccountMapper installTransAndBaseAccountMapper;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;
    @Value("${global.target.service.url.auth}")
    private String authUrl;

    @Autowired
    private AccountingService accountingService;
    private static Logger logger = LoggerFactory.getLogger(X5977BusImpl.class);
    @Override
    public Object busExecute(X5631BO x5631bo) throws Exception {

        PageBean<X5630VO> page = new PageBean<>();
        InstallAccountBean installAccountBean = new InstallAccountBean();
        if (StringUtil.isBlank(x5631bo.getGlobalTransSerialNo())) {
            return page;
        }
        installAccountBean.setGlobalTransSerialNo(x5631bo.getGlobalTransSerialNo());
        int count = installTransAndBaseAccountMapper.countBySqlBuilderForNotQuotaChildList(installAccountBean);
        page.setTotalCount(count);
        if (count > 0) {
            if (null != x5631bo.getPageSize() && null != x5631bo.getIndexNo()) {
                installAccountBean.setPageSize(x5631bo.getPageSize());
                installAccountBean.setIndexNo(x5631bo.getIndexNo());
                page.setPageSize(x5631bo.getPageSize());
                page.setIndexNo(x5631bo.getIndexNo());
            }
            List<X5630VO> list = queryInstallmentTransAcct(installAccountBean);


            //是否可操作封包
            Iterator<X5630VO> it = list.iterator();
            while (it.hasNext()) {
                X5630VO vo = it.next();
                if (!judgeOperation(vo, x5631bo.getAccountType())) {
                    it.remove();
                    continue;
                }
            }
            page.setRows(list);
            page.setRowsCount(list.size());
        }
        return page;
    }

    private void amountConversionAndDesc(X5630VO x5630VO, String currencyCode) throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        BigDecimal loanAmount = CurrencyConversionUtil.reduce(x5630VO.getLoanAmount(), decimalPlaces);
        x5630VO.setLoanAmount(loanAmount);
        BigDecimal remainPrincipalAmount = CurrencyConversionUtil.reduce(x5630VO.getRemainPrincipalAmount(), decimalPlaces);
        x5630VO.setRemainPrincipalAmount(remainPrincipalAmount);
        BigDecimal feeAmount = CurrencyConversionUtil.reduce(x5630VO.getFeeAmount(), decimalPlaces);
        x5630VO.setFeeAmount(feeAmount);
        BigDecimal remainFeeAmount = CurrencyConversionUtil.reduce(x5630VO.getRemainFeeAmount(), decimalPlaces);
        x5630VO.setRemainFeeAmount(remainFeeAmount);
        BigDecimal interAmount = CurrencyConversionUtil.reduce(x5630VO.getInterAmount(), decimalPlaces);
        x5630VO.setInterAmount(interAmount);
        BigDecimal remainInterAmount = CurrencyConversionUtil.reduce(x5630VO.getRemainInterAmount(), decimalPlaces);
        x5630VO.setRemainInterAmount(remainInterAmount);
        if (x5630VO.getPrepaidAmount() != null) {
            BigDecimal prePaidAmount = CurrencyConversionUtil.reduce(x5630VO.getPrepaidAmount(), decimalPlaces);
            x5630VO.setPrepaidAmount(prePaidAmount);
        }
        if (x5630VO.getNewBalance() != null) {
            BigDecimal newBalance = CurrencyConversionUtil.reduce(x5630VO.getNewBalance(), decimalPlaces);
            x5630VO.setNewBalance(newBalance);
        }
        if (x5630VO.getLoanFixedFee() != null) {
            BigDecimal loanFixedFee = CurrencyConversionUtil.reduce(x5630VO.getLoanFixedFee(), decimalPlaces);
            x5630VO.setLoanFixedFee(loanFixedFee);
        }
        if (x5630VO.getLoanFirstTermFee() != null) {
            BigDecimal loanFirstTermFee = CurrencyConversionUtil.reduce(x5630VO.getLoanFirstTermFee(), decimalPlaces);
            x5630VO.setLoanFirstTermFee(loanFirstTermFee);
        }
        if (x5630VO.getLoanFinalTermFee() != null) {
            BigDecimal loanFinalTermFee = CurrencyConversionUtil.reduce(x5630VO.getLoanFinalTermFee(), decimalPlaces);
            x5630VO.setLoanFinalTermFee(loanFinalTermFee);
        }
        if (x5630VO.getLoanFixedPrincipal() != null) {
            BigDecimal loanFixedPrincipal = CurrencyConversionUtil.reduce(x5630VO.getLoanFixedPrincipal(), decimalPlaces);
            x5630VO.setLoanFixedPrincipal(loanFixedPrincipal);
        }
        if (x5630VO.getLoanFirstTermPrincipal() != null) {
            BigDecimal loanFirstTermPrincipal = CurrencyConversionUtil.reduce(x5630VO.getLoanFirstTermPrincipal(), decimalPlaces);
            x5630VO.setLoanFirstTermPrincipal(loanFirstTermPrincipal);
        }
        if (x5630VO.getLoanFinalTermPrincipal() != null) {
            BigDecimal loanFinalTermPrincipal = CurrencyConversionUtil.reduce(x5630VO.getLoanFinalTermPrincipal(), decimalPlaces);
            x5630VO.setLoanFinalTermPrincipal(loanFinalTermPrincipal);
        }
        if (x5630VO.getLoanPrincipalDue() != null) {
            BigDecimal loanPrincipalDue = CurrencyConversionUtil.reduce(x5630VO.getLoanPrincipalDue(), decimalPlaces);
            x5630VO.setLoanPrincipalDue(loanPrincipalDue);
        }
        if (x5630VO.getLoanPrincipalUndue() != null) {
            BigDecimal loanPrincipalUndue = CurrencyConversionUtil.reduce(x5630VO.getLoanPrincipalUndue(), decimalPlaces);
            x5630VO.setLoanPrincipalUndue(loanPrincipalUndue);
        }
        if (x5630VO.getLoanFeeDue() != null) {
            BigDecimal loanFeeDue = CurrencyConversionUtil.reduce(x5630VO.getLoanFeeDue(), decimalPlaces);
            x5630VO.setLoanFeeDue(loanFeeDue);
        }
        if (x5630VO.getLoanFeeUndue() != null) {
            BigDecimal loanFeeUndue = CurrencyConversionUtil.reduce(x5630VO.getLoanFeeUndue(), decimalPlaces);
            x5630VO.setLoanFeeUndue(loanFeeUndue);
        }

        if (x5630VO.getLoanFeeUndue() != null) {
            BigDecimal loanFeeUndue = CurrencyConversionUtil.reduce(x5630VO.getLoanFeeUndue(), decimalPlaces);
            x5630VO.setLoanFeeUndue(loanFeeUndue);
        }

        BigDecimal currentTotalBalance = queryAccountService.queryCurrBalance(x5630VO.getCustomerNo(), x5630VO.getAccountId(), x5630VO.getCurrencyCode(), x5630VO.getAccountOrganForm(), x5630VO.getCycleModeFlag(), x5630VO.getRemainPrincipalAmount());
        x5630VO.setCurrentTotalBalance(currentTotalBalance);
        descConversion(x5630VO);
    }


    private List<X5630VO> queryInstallmentTransAcct(InstallAccountBean installAccountBean)
            throws Exception {
        List<X5630VO> list =
                installTransAndBaseAccountMapper.selectInstallTransAcctsForNotQuotaChildList(installAccountBean);
        for (X5630VO x5630VO : list) {
            amountConversionAndDesc(x5630VO, x5630VO.getCurrencyCode());
        }
        return list;
    }

    /**
     * @param x5630VO
     * @throws Exception
     */
    private void descConversion(X5630VO x5630VO) throws Exception {
        if (StringUtil.isNotBlank(x5630VO.getLoanType())) {
            CoreStageType coreStageType = httpQueryService.queryCoreStageType(x5630VO.getOperationMode(),x5630VO.getLoanType());
            if (coreStageType != null) {
                x5630VO.setLoanTypeDesc(coreStageType.getStageTypeDesc());
            }
        }
    }

    /**
     * 是否可操作封包
     *
     * @throws Exception
     */
    private Boolean judgeOperation(X5630VO x5630VO, String accountType) throws Exception {
        //退货或者撤销
        if (Constant.SIGN_NO.equals(x5630VO.getStatus()) || Constant.SIGN_FOUR.equals(x5630VO.getStatus())) {
            return false;
        }
        //已经全部还清的   未抛金额都为0且已抛帐未还金额为0
        BigDecimal cuurBalance = queryAccountService.queryCurrBalanceUnit(x5630VO.getAccountId(),x5630VO.getCurrencyCode());
        cuurBalance = cuurBalance.add(x5630VO.getRemainPrincipalAmount()).add(x5630VO.getRemainFeeAmount()).add(x5630VO.getRemainInterAmount());

        if (BigDecimalUtil.compareTo(cuurBalance, BigDecimal.ZERO) == 0) {
            return false;
        }

        //非未转让，非回购账户
        if (!(StringUtils.equals(CapitalStageEnum.REPO.getValue(), x5630VO.getCapitalStage()) || StringUtils.isEmpty(x5630VO.getCapitalStage()))) {
            x5630VO.setOperation(TureOrFalse.F.getValue());
            return false;
        }
        //非本行资金
        if (StringUtils.isNotEmpty(x5630VO.getFundNum())) {
            CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(x5630VO.getFundNum(), null, Constant.SIGN_YES);
            if (coreCorporationEntity == null) {
                logger.error("X5977BusImpl,coreAccount:{},FundNum:{},,未找到资金信息",x5630VO.getAccountId(),x5630VO.getFundNum());
                throw new BusinessException("COR-13115");
            }
            //如果是租户法人，则“是否为本行资产”为Y
            if(StringUtils.equals(coreCorporationEntity.getPartnerCategory(),Constant.SIGN_NO)){
                return true;
            }

            if (StringUtils.equals(Constant.SIGN_YES, coreCorporationEntity.getIsBankFunds())) {
                x5630VO.setOperation(TureOrFalse.F.getValue());
                return false;
            }else {
                x5630VO.setOperation(TureOrFalse.T.getValue());
                return true;
            }
        }
        if (StringUtils.equals(AccountTypeEnum.B.getValue(), accountType)) {
            //不良资产
            if (!accountingService.judgeNonPerformingAssets(x5630VO.getOperationMode(), x5630VO.getAccountingStatusCode())) {
                return false;
            }
        }else {
            if (accountingService.judgeNonPerformingAssets(x5630VO.getOperationMode(), x5630VO.getAccountingStatusCode())) {
                return false;
            }
        }
        //额度子账户
        if(StringUtils.equals(SubAccountIdentify.Q.getValue(),x5630VO.getSubAccIdentify())){
            return false;
        }
        x5630VO.setOperation(TureOrFalse.T.getValue());
        return true;
    }
}
