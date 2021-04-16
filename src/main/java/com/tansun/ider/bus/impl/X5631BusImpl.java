package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5631Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreStageType;
import com.tansun.ider.dao.beta.entity.CoreTransIdNo;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.InstallTransAndBaseAccountMapper;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5631BO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 信用卡分期账户信息查询
 *
 * @author cuiguangchao 2019年03月28日
 */
@Service
public class X5631BusImpl implements X5631Bus {
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;// 分期交易账户表
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    private CoreAccountDao coreAccountDao;// 基本账户表
    @Resource
    InstallTransAndBaseAccountMapper installTransAndBaseAccountMapper;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;

    @Override
    public Object busExecute(X5631BO x5631bo) throws Exception {
        
    			PageBean<X5630VO> page = new PageBean<>();
                InstallAccountBean installAccountBean = new InstallAccountBean();
                if(StringUtil.isBlank(x5631bo.getGlobalTransSerialNo())){
                	return page;
                }
                installAccountBean.setGlobalTransSerialNo(x5631bo.getGlobalTransSerialNo());
                int count = installTransAndBaseAccountMapper.countBySqlBuilderForChildList(installAccountBean);
                page.setTotalCount(count);
                if (count > 0) {
                    if (null != x5631bo.getPageSize() && null != x5631bo.getIndexNo()) {
                        /* coreInstallmentTransAcctSqlBuilder.orderByLoanStartDate(false); */
                    	installAccountBean.setPageSize(x5631bo.getPageSize());
                    	installAccountBean.setIndexNo(x5631bo.getIndexNo());
                        page.setPageSize(x5631bo.getPageSize());
                        page.setIndexNo(x5631bo.getIndexNo());
                    }
                    List<X5630VO> list = queryInstallmentTransAcct(installAccountBean);
                    page.setRows(list);
                    page.setRowsCount(list.size());
                }
        return page;
    }

    private void amountConversionAndDesc(X5630VO x5630VO, String currencyCode,String operationMode) throws Exception {
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
        if (x5630VO.getFundNum() != null) {
            CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(x5630VO.getFundNum(), null, "1");
            x5630VO.setFundName(coreCorporationEntity.getCorporationEntityName());
        }
        if (x5630VO.getTransIdentifiNo() != null) {
            CoreTransIdNo transId = httpQueryService.queryTransIdNo(x5630VO.getOperationMode(), x5630VO.getTransIdentifiNo());
            x5630VO.setTransIdentifiDesc(transId.getTransIdentifiDesc());
        }
        descConversion(x5630VO);
    }


    private List<X5630VO> queryInstallmentTransAcct(InstallAccountBean installAccountBean)
            throws Exception {
        List<X5630VO> list =
            		installTransAndBaseAccountMapper.selectInstallTransAcctsForChildList(installAccountBean);
            for (X5630VO x5630VO : list) {
                amountConversionAndDesc(x5630VO, x5630VO.getCurrencyCode(),x5630VO.getOperationMode());
            }
        return list;
    }
    
    /**
    *
    * @param x5630VO
    * @throws Exception
    */
   private void descConversion(X5630VO x5630VO) throws Exception{
       if (StringUtil.isNotBlank(x5630VO.getLoanType())){
           CoreStageType coreStageType = httpQueryService.queryCoreStageType(x5630VO.getOperationMode(),x5630VO.getLoanType());
           if (coreStageType != null){
               x5630VO.setLoanTypeDesc(coreStageType.getStageTypeDesc());
               x5630VO.setStageType(coreStageType.getStageType());
           }
       }
   }

    /**
     * 查询账户
     *
     * @param accountId
     *            账号
     * @return
     * @throws Exception
     */
    public CoreAccount queryAccount(String accountId, String currencyCode) throws Exception {
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        return coreAccount;
    }
}
