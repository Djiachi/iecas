package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5988Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingAssetPlan;
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
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5630BO;
import com.tansun.ider.model.vo.X5305VO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
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
import java.util.Optional;


/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询 BSS.IQ.01.0355
 * 参考5630 信用卡分期账户信息查询
 */
@Service
public class X5988BusImpl implements X5988Bus {
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    InstallTransAndBaseAccountMapper installTransAndBaseAccountMapper;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;

    private static Logger logger = LoggerFactory.getLogger(X5988BusImpl.class);

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
                    List<X5630VO> list = queryInstallmentTransAcct(installAccountBean);
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

    private List<X5630VO> queryInstallmentTransAcct(InstallAccountBean installAccountBean)
            throws Exception {
        List<X5630VO> list =
                installTransAndBaseAccountMapper.selectCoreInstallmentTransAcctsForList(installAccountBean);
        List<CoreAccountingAssetPlan> accountingAssetPlanList = httpQueryService.queryCoreAccountingAssetPlanList(null, null, null);
        if (accountingAssetPlanList.isEmpty()) {
            throw new BusinessException("");
        }
        //是否可操作封包
        Iterator<X5630VO> it = list.iterator();
        while (it.hasNext()) {
            X5630VO vo = it.next();
            if (!judgeOperation(vo)) {
                it.remove();
                continue;
            }
            queryPlanDetails(vo, accountingAssetPlanList);
            queryCapitalOrganizationName(vo);
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
            CoreStageType coreStageType = httpQueryService.queryCoreStageType(x5630VO.getOperationMode(), x5630VO.getLoanType());
            if (coreStageType != null) {
                x5630VO.setLoanTypeDesc(coreStageType.getStageTypeDesc());
            }
        }
    }

    /**
     * 未封包不显示
     *
     * @throws Exception
     */
    private Boolean judgeOperation(X5630VO x5630VO) throws Exception {
        InstallAccountBean installAccountBean = new InstallAccountBean();
        installAccountBean.setGlobalTransSerialNo(x5630VO.getGlobalTransSerialNo());
        //查询是否有子账户
        int count = installTransAndBaseAccountMapper.countBySqlBuilderForNotQuotaChildList(installAccountBean);

        if (count == 0) {
            return judgeSubOperation(x5630VO);
        } else {
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andCustomerNoEqualTo(x5630VO.getCustomerNo());
            coreAccountSqlBuilder.andGlobalTransSerialNoEqualTo(x5630VO.getGlobalTransSerialNo());
            coreAccountSqlBuilder.andSubAccIdentifyEqualTo(SubAccountIdentify.L.getValue());
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            Boolean judge = false;
            for (CoreAccount coreAccount : listCoreAccount) {
                judge = judge || judgeSubOperation(coreAccount);
            }
            return judge;
        }
    }

    private Boolean judgeSubOperation(X5630VO x5630VO) {

        if (StringUtils.isEmpty(x5630VO.getAbsPlanId())) {
            return false;
        } else {
            return true;
        }

    }

    private Boolean judgeSubOperation(CoreAccount coreAccount) {

        if (StringUtils.isEmpty(coreAccount.getAbsPlanId())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询abs计划内容
     *
     * @param x5630VO
     * @param accountingAssetPlanList
     */
    private void queryPlanDetails(X5630VO x5630VO, List<CoreAccountingAssetPlan> accountingAssetPlanList) {
        CoreAccountingAssetPlan plan;
        Optional<CoreAccountingAssetPlan> first = accountingAssetPlanList.stream().filter(x -> StringUtils.equals(x.getPlanId(), x5630VO.getAbsPlanId())).findFirst();

        if (first.isPresent()) {
            plan = first.get();
            x5630VO.setAccountType(plan.getAccountType());
        }
    }

    /**
     * 查询资金法人名称
     *
     * @param vo
     */
    private void queryCapitalOrganizationName(X5630VO vo) throws Exception {
        String fundNum;
        if (StringUtils.isEmpty(vo.getFundNum())) {
            //客户上
            CoreCustomer coreCustomer = queryCustomerService.queryCustomer(vo.getCustomerNo());
            fundNum = coreCustomer.getCorporationEntityNo();
            vo.setFundNum(fundNum);
        } else {
            fundNum = vo.getFundNum();
        }

        CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(fundNum, null, "1");
        if (coreCorporationEntity == null) {
            logger.error("X5987BusImpl.,查询coreFundInfo不存在,FundNum:{}", vo.getFundNum());
            throw new BusinessException("COR-13115");
        }
        vo.setCapitalOrganizationName(coreCorporationEntity.getCorporationEntityName());
    }

}
