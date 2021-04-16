package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5976Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreStageType;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.InstallTransAndBaseAccountMapper;
import com.tansun.ider.enums.AccountTypeEnum;
import com.tansun.ider.enums.CapitalStageEnum;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.enums.TureOrFalse;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5630BO;
import com.tansun.ider.model.vo.X5305VO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.AccountingService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryAccountService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BigDecimalUtil;
import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询 BSS.IQ.01.0335
 * 参考5630 信用卡分期账户信息查询
 */
@Service
public class X5976BusImpl implements X5976Bus {
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    InstallTransAndBaseAccountMapper installTransAndBaseAccountMapper;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;
    @Autowired
    private AccountingService accountingService;
    @Resource
    private QueryAccountService queryAccountService;
    private static Logger logger = LoggerFactory.getLogger(X5976BusImpl.class);

    @Override
    public Object busExecute(X5630BO x5630bo) throws Exception {
        String idNumber = x5630bo.getIdNumber();
        String externalIdentificationNo = x5630bo.getExternalIdentificationNo();
        @SuppressWarnings("unused")
        String customerNo = null;
        PageBean<X5630VO> page = new PageBean<>();
        if (StringUtil.isNotBlank(idNumber)) {
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            if (coreCustomer != null) {
                customerNo = coreCustomer.getCustomerNo();
            }
        }
        if (StringUtil.isNotBlank(externalIdentificationNo)) {
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
            List<CoreMediaBasicInfo> list = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
            if (list == null || list.size() == 0) {
                throw new BusinessException("CUS-00018");
            }
        }
        // 第一步，查出该客户下所有的媒介信息
        if (StringUtil.isNotBlank(externalIdentificationNo)) {
            CoreMediaBasicInfo coreMediaBasicInfo = new CoreMediaBasicInfo();
            List<X5305VO> listX5305VO = this.queryAllMedia(x5630bo);
            // 第二步，筛选出与输入外部识别号的产品对象相同的外部识别号
            if (listX5305VO.size() != 0 && listX5305VO != null) {
                InstallAccountBean installAccountBean = new InstallAccountBean();
                for (X5305VO x5305vo : listX5305VO) {
                    if (x5305vo.getExternalIdentificationNo().equals(x5630bo.getExternalIdentificationNo())) {
                        CachedBeanCopy.copyProperties(x5305vo, coreMediaBasicInfo);
                        break;
                    }
                }
                // 第三步，查询分期账户
                Iterator<X5305VO> it = listX5305VO.iterator();
                List<String> externalIdentificationNos = new ArrayList<String>();
                while (it.hasNext()) {
                    X5305VO x5305vo = it.next();
                    if (coreMediaBasicInfo.getProductObjectCode().equals(x5305vo.getProductObjectCode())) {
                        externalIdentificationNos.add(x5305vo.getExternalIdentificationNo());
                    }
                }
                if (externalIdentificationNos.size() > 0) {
                    installAccountBean.setExternalIdentificationNos(externalIdentificationNos);
                }
                if (StringUtil.isNotEmpty(x5630bo.getOldGlobalSerialNumbr())) {
                    installAccountBean.setOldGlobalSerialNumbr(x5630bo.getOldGlobalSerialNumbr());
                }
                if (null != x5630bo.getBeginDate() && null != x5630bo.getEndDate()) {
                    if (x5630bo.getEndDate().compareTo(x5630bo.getBeginDate()) <= 0) {
                        throw new BusinessException("COR-12038");
                    }
                }
                if (null != x5630bo.getBeginDate()) {
                    installAccountBean.setLoanStartDate(x5630bo.getBeginDate());
                }
                if (null != x5630bo.getEndDate()) {
                    installAccountBean.setLoanEndDate(x5630bo.getEndDate());
                }
                int count = installTransAndBaseAccountMapper.countBySqlBuilderForList(installAccountBean);
                page.setTotalCount(count);
                if (count > 0) {
                    if (null != x5630bo.getPageSize() && null != x5630bo.getIndexNo()) {
                        installAccountBean.setPageSize(x5630bo.getPageSize());
                        installAccountBean.setIndexNo(x5630bo.getIndexNo());
                        page.setPageSize(x5630bo.getPageSize());
                        page.setIndexNo(x5630bo.getIndexNo());
                    }
                    List<X5630VO> list = queryInstallmentTransAcct(installAccountBean, x5630bo.getAccountType());
                    page.setRows(list);
                    page.setRowsCount(list.size());
                }
            }

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
        x5630VO.setHaveChild(true);
        descConversion(x5630VO);


    }

    /**
     * 查询客户下所有媒介信息
     *
     * @param x5630bo
     */
    @SuppressWarnings("unchecked")
    private List<X5305VO> queryAllMedia(X5630BO x5630bo) {
        List<X5305VO> listX5305VO = null;
        String params = JSON.toJSONString(x5630bo, SerializerFeature.DisableCircularReferenceDetect);
        String triggerEventNo = EVENT.BSS_OP_01_0017;
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);
        String response = restTemplate.postForObject(nofnUrl + triggerEventNo, entity, String.class);
        ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
        if (responseVO.getReturnData() != null) {
            PageBean<X5305VO> page =
                    JSON.parseObject(responseVO.getReturnData().toString(), PageBean.class, Feature.DisableCircularReferenceDetect);
            if (page != null) {
                listX5305VO = JSON.parseArray(page.getRows().toString(), X5305VO.class);
                return listX5305VO;
            }
        }
        return listX5305VO;
    }

    private List<X5630VO> queryInstallmentTransAcct(InstallAccountBean installAccountBean, String accountType)
            throws Exception {
        List<X5630VO> list =
                installTransAndBaseAccountMapper.selectCoreInstallmentTransAcctsForList(installAccountBean);
        //是否可操作封包
        Iterator<X5630VO> it = list.iterator();
        while (it.hasNext()) {
            X5630VO vo = it.next();
            if (!judgeOperation(vo, accountType)) {
                it.remove();
                continue;
            }
            amountConversionAndDesc(vo, vo.getCurrencyCode());
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
        InstallAccountBean installAccountBean = new InstallAccountBean();
        installAccountBean.setGlobalTransSerialNo(x5630VO.getGlobalTransSerialNo());
        //查询是否有子账户
        int count = installTransAndBaseAccountMapper.countBySqlBuilderForNotQuotaChildList(installAccountBean);

        if (count == 0) {
            return judgeSubOperation(x5630VO, accountType);
        } else {
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andCustomerNoEqualTo(x5630VO.getCustomerNo());
            coreAccountSqlBuilder.andGlobalTransSerialNoEqualTo(x5630VO.getGlobalTransSerialNo());
            coreAccountSqlBuilder.andSubAccIdentifyEqualTo(SubAccountIdentify.L.getValue());
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            if (judgeTranOperation(x5630VO)) {
                return false;
            }
            Boolean judge = false;
            for (CoreAccount coreAccount : listCoreAccount) {
                judge = judge || judgeSubOperation(coreAccount, accountType);
            }
            return judge;
        }
    }

    private Boolean judgeSubOperation(X5630VO x5630VO, String accountType) throws Exception {
        if (judgeTranOperation(x5630VO)) {
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
                logger.error("X5976BusImpl,coreAccount:{},FundNum:{},未找到资金信息", x5630VO.getAccountId(), x5630VO.getFundNum());
                throw new BusinessException("COR-13115");
            }

            //如果是租户法人，则“是否为本行资产”为Y
            if(StringUtils.equals(coreCorporationEntity.getPartnerCategory(),Constant.SIGN_NO)){
                return true;
            }
            if (StringUtils.equals(Constant.SIGN_NO, coreCorporationEntity.getIsBankFunds())) {
                x5630VO.setOperation(TureOrFalse.F.getValue());
                return false;
            }
        }
        if (StringUtils.equals(AccountTypeEnum.B.getValue(), accountType)) {
            //不良
            if (!accountingService.judgeNonPerformingAssets(x5630VO.getOperationMode(), x5630VO.getAccountingStatusCode())) {
                return false;
            }
        }
        //额度子账户
        if (StringUtils.equals(SubAccountIdentify.Q.getValue(), x5630VO.getSubAccIdentify())) {
            return false;
        }
        x5630VO.setOperation(TureOrFalse.T.getValue());
        return true;
    }

    /**
     * 是否可操作封包 此方法值为反
     * @param x5630VO
     * @return
     * @throws Exception
     */
    private boolean judgeTranOperation(X5630VO x5630VO) throws Exception {
        //退货或者撤销
        if (Constant.SIGN_NO.equals(x5630VO.getStatus()) || Constant.SIGN_FOUR.equals(x5630VO.getStatus())) {
            return true;
        }
        //已经全部还清的   未抛金额都为0且已抛帐未还金额为0
        BigDecimal cuurBalance = queryAccountService.queryCurrBalanceUnit(x5630VO.getAccountId(), x5630VO.getCurrencyCode());
        cuurBalance = cuurBalance.add(x5630VO.getRemainPrincipalAmount()).add(x5630VO.getRemainFeeAmount()).add(x5630VO.getRemainInterAmount());
        if (BigDecimalUtil.compareTo(cuurBalance, BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    private Boolean judgeSubOperation(CoreAccount coreAccount, String accountType) throws Exception {

        //非未转让，非回购账户
        if (!(StringUtils.equals(CapitalStageEnum.REPO.getValue(), coreAccount.getCapitalStage()) || StringUtils.isEmpty(coreAccount.getCapitalStage()))) {
            return false;
        }
        //非本行资金
        if (StringUtils.isNotEmpty(coreAccount.getFundNum())) {
            CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(coreAccount.getFundNum(), null, Constant.SIGN_YES);
            if (coreCorporationEntity == null) {
                logger.error("X5976BusImpl,coreAccount:{},FundNum:{},未找到资金信息", coreAccount.getAccountId(), coreAccount.getFundNum());
                throw new BusinessException("COR-13115");
            }
            //如果是租户法人，则“是否为本行资产”为Y
            if(StringUtils.equals(coreCorporationEntity.getPartnerCategory(),Constant.SIGN_NO)){
                return true;
            }

            if (StringUtils.equals(Constant.SIGN_NO, coreCorporationEntity.getIsBankFunds())) {
                return true;
            }else {
                return false;
            }
        }
        if (StringUtils.equals(AccountTypeEnum.B.getValue(), accountType)) {
            //不良
            if (!accountingService.judgeNonPerformingAssets(coreAccount.getOperationMode(), coreAccount.getAccountingStatusCode())) {
                return false;
            }
        }
        //额度子账户
        if (StringUtils.equals(SubAccountIdentify.Q.getValue(), coreAccount.getSubAccIdentify())) {
            return false;
        }
        return true;
    }

}
