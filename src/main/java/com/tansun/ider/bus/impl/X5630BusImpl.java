package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5630Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreStageType;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.InstallTransAndBaseAccountMapper;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.bo.X5630BO;
import com.tansun.ider.model.vo.X5305VO;
import com.tansun.ider.model.vo.X5630VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.web.WSC;

/**
 * 信用卡分期账户信息查询
 *
 * @author cuiguangchao 2019年03月28日
 */
@Service
public class X5630BusImpl implements X5630Bus {
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
    public Object busExecute(X5630BO x5630bo) throws Exception {
        String idNumber = x5630bo.getIdNumber();
        String externalIdentificationNo = x5630bo.getExternalIdentificationNo();
        @SuppressWarnings("unused")
        String customerNo = null;
        String operationMode = null;
        PageBean<X5630VO> page = new PageBean<>();
        if (StringUtil.isNotBlank(idNumber)) {
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            if (coreCustomer != null) {
                customerNo = coreCustomer.getCustomerNo();
                operationMode = coreCustomer.getOperationMode();
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
                        operationMode = coreMediaBasicInfo.getOperationMode();
                        break;
                    }
                }
                // 第三步，查询分期账户
                Iterator<X5305VO> it = listX5305VO.iterator();
               /* CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilderNew =
                        new CoreInstallmentTransAcctSqlBuilder();*/
                List<String> externalIdentificationNos  = new ArrayList<String>();
                while (it.hasNext()) {
                    X5305VO x5305vo = it.next();
                    if (coreMediaBasicInfo.getProductObjectCode().equals(x5305vo.getProductObjectCode())) {
                    	externalIdentificationNos.add(x5305vo.getExternalIdentificationNo());
                    }
                }
                if(externalIdentificationNos.size()>0){
                	installAccountBean.setExternalIdentificationNos(externalIdentificationNos);
                }
                //coreInstallmentTransAcctSqlBuilder.and(coreInstallmentTransAcctSqlBuilderNew);
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
                        /* coreInstallmentTransAcctSqlBuilder.orderByLoanStartDate(false); */
                    	installAccountBean.setPageSize(x5630bo.getPageSize());
                    	installAccountBean.setIndexNo(x5630bo.getIndexNo());
                        page.setPageSize(x5630bo.getPageSize());
                        page.setIndexNo(x5630bo.getIndexNo());
                    }
                    List<X5630VO> list = queryInstallmentTransAcct(installAccountBean,operationMode);
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
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    private List<X5305VO> queryAllMedia(X5630BO x5630bo) throws Exception {
        List<X5305VO> listX5305VO = new ArrayList<>();
//        String params = JSON.toJSONString(x5630bo, SerializerFeature.DisableCircularReferenceDetect);
//        String triggerEventNo = EVENT.BSS_OP_01_0017;
//        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        HttpEntity<String> entity = new HttpEntity<String>(params, headers);
//        String response = restTemplate.postForObject(nofnUrl + triggerEventNo, entity, String.class);
//        ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
//        if (responseVO.getReturnData() != null) {
//            PageBean<X5305VO> page =
//                    JSON.parseObject(responseVO.getReturnData().toString(), PageBean.class, Feature.DisableCircularReferenceDetect);
//            if (page != null) {
//                listX5305VO = JSON.parseArray(page.getRows().toString(), X5305VO.class);
//                return listX5305VO;
//            }
//        }
        Map<String, Object> paramsMap = new HashMap<String, Object>(16);
        paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, JSON.toJSONString(x5630bo, SerializerFeature.DisableCircularReferenceDetect));
        // 全局事件流水号
        paramsMap.put(WSC.EVENT_ID, "BSS.IQ.01.0017-");
        ActionService coreService = (ActionService) SpringUtil.getBean("X5305");
        // 内部方法转换为JSON后进行传递
        PageBean<X5305VO> page = (PageBean<X5305VO>)coreService.execute(paramsMap);
        if (page != null) {
        	if (page.getRows() != null && page.getRows().size() > 0) {
				for (X5305VO x5305vo : page.getRows()) {
					listX5305VO.add(x5305vo);
				}
			}
        }
        return listX5305VO;
    }

    private List<X5630VO> queryInstallmentTransAcct(InstallAccountBean installAccountBean,String operationMode)
            throws Exception {
        List<X5630VO> list =
            		installTransAndBaseAccountMapper.selectCoreInstallmentTransAcctsForList(installAccountBean);
            for (X5630VO x5630VO : list) {
                amountConversionAndDesc(x5630VO, x5630VO.getCurrencyCode());
                //list.add(x5630vo);
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
