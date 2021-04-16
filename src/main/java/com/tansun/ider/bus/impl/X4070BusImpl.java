package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4070Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreProductObjectBillSumDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProductObjectBillSum;
import com.tansun.ider.dao.issue.impl.CoreBudgetOrgCustRelDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductObjectBillSumDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductObjectBillSumSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4070BO;
import com.tansun.ider.model.vo.X4070BillInfo;
import com.tansun.ider.model.vo.X4070PeoCollectInfo;
import com.tansun.ider.model.vo.X4070UnitCollectInfo;
import com.tansun.ider.model.vo.X4070VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.util.DateConversionUtil;

/**
 * <p> Title: X4070BusImpl </p>
 * <p> Description: 预算单位已出账单查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月24日
 */
@Service
public class X4070BusImpl implements X4070Bus {
    private static Logger logger = LoggerFactory.getLogger(X4070BusImpl.class);
    @Autowired
    private CoreBudgetOrgCustRelDaoImpl coreBudgetOrgCustRelDaoImpl;
    @Autowired
    private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
    @Autowired
    private CoreProductObjectBillSumDao coreProductObjectBillSumDao;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${spring.application.name}")
    private String gnsInfo;
    @Value("${gns.global.target.service.url.nofn}")
    private String gnsnofnUrl;
    @Autowired
    private HttpQueryServiceByGns httpQueryServiceByGns;

    @Override
    public Object busExecute(X4070BO x4070BO) throws Exception {
        // 获取片区编号
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(gnsInfo);
        String code = m.replaceAll("").trim();
        if (StringUtil.isEmpty(x4070BO.getBillDay())) {
            throw new BusinessException("COR-11009");
        }
        X4070VO x4070Vo = new X4070VO();
        List<X4070BillInfo> x4070BillInfos = new ArrayList<>();
        PageBean<X4070BillInfo> page = new PageBean<>();
        X4070UnitCollectInfo unitCollectInfo = new X4070UnitCollectInfo();// 单位公务卡汇总信息
        X4070PeoCollectInfo peoCollectInfo = new X4070PeoCollectInfo();// 个人公务卡汇总信息
        List<CoreProductObjectBillSum> totalCoreProductObjectBillSumList = new ArrayList<>();
        // 查询预算单位下的所有关联客户
        CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
        coreBudgetOrgCustRelSqlBuilder.andBudgetOrgCodeEqualTo(x4070BO.getIdNumber());
        List<CoreBudgetOrgCustRel> coreBudgetOrgCustRels =
                coreBudgetOrgCustRelDaoImpl.selectListBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
        if (CollectionUtils.isNotEmpty(coreBudgetOrgCustRels)) {
            for (CoreBudgetOrgCustRel coreBudgetOrgCustRel : coreBudgetOrgCustRels) {
                CoreMediaBasicInfo queryCoreMediaBasicInfo = queryCoreMediaBasicInfo(coreBudgetOrgCustRel.getExternalIdentificationNo());
                String productObjectCode = queryCoreMediaBasicInfo.getProductObjectCode();
                String dateString = DateUtil.format(DateUtil.parse(x4070BO.getBillDay(), "yyyy-MM"), "yyyy-MM");
                // 如果片区编号和客户所在片区一直则直接查询产品对象账单摘要信息
                List<CoreProductObjectBillSum> coreProductObjectBillSumList = new ArrayList<>();
                if (code.equals(coreBudgetOrgCustRel.getCustomerArea())) {
                    CoreProductObjectBillSumSqlBuilder coreProductObjectBillSumSqlBuilder =
                            new CoreProductObjectBillSumSqlBuilder();
                    coreProductObjectBillSumSqlBuilder.andCustomerNoEqualTo(coreBudgetOrgCustRel.getCustomerNo());
                    coreProductObjectBillSumSqlBuilder.andBillDateLikeRigth(dateString);
                    coreProductObjectBillSumSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                    List<CoreProductObjectBillSum> queryList =
                            coreProductObjectBillSumDao.selectListBySqlBuilder(coreProductObjectBillSumSqlBuilder);
                    coreProductObjectBillSumList.addAll(queryList);
                }
                // 如果片区编号和客户所在片区编号不一致，则通过接口查询产品对象账单摘要信息
                else {
                    List<CoreProductObjectBillSum> queryCoreProductObjectBillSumByGns = httpQueryServiceByGns
                        .getCoreProductObjectBillSummary(gnsnofnUrl, coreBudgetOrgCustRel.getCustomerNo(), dateString, productObjectCode);
                    if (CollectionUtils.isNotEmpty(queryCoreProductObjectBillSumByGns)) {
                        coreProductObjectBillSumList.addAll(queryCoreProductObjectBillSumByGns);
                    }
                }
                if (CollectionUtils.isNotEmpty(coreProductObjectBillSumList)) {
                    for (CoreProductObjectBillSum coreProductObjectBillSum : coreProductObjectBillSumList) {
                        X4070BillInfo x4070BillInfo = new X4070BillInfo();
                        x4070BillInfo.setExternalIdentificationNo(coreBudgetOrgCustRel.getExternalIdentificationNo());
                        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
                        coreCustomerSqlBuilder.andCustomerNoEqualTo(coreProductObjectBillSum.getCustomerNo());
                        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
                        CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreCustomer.getOperationMode(),
                            coreProductObjectBillSum.getProductObjectCode());
                        if (coreProductObject == null) {
                            throw new BusinessException("COR-11010");
                        }
                        CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreProductObjectBillSum.getCurrencyCode());
                        Integer decimalPlaces = coreCurrency.getDecimalPosition();
                        x4070BillInfo.setCustomerName(coreCustomer.getCustomerName());
                        x4070BillInfo.setCurrencyCode(coreProductObjectBillSum.getCurrencyCode());
                        x4070BillInfo.setCurrentCycleNumber(coreProductObjectBillSum.getCurrentCycleNumber());
                        x4070BillInfo.setPostingAmount(
                            CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPostingAmount(), decimalPlaces));
                        x4070BillInfo.setProductDesc(coreProductObject.getProductDesc());
                        x4070BillInfo.setGmtCreate(coreProductObjectBillSum.getGmtCreate());
                        x4070BillInfos.add(x4070BillInfo);
                    }
                    totalCoreProductObjectBillSumList.addAll(coreProductObjectBillSumList);
                }
            }
            // 按照创建时间对list中的数据进行排序
            Collections.sort(x4070BillInfos, new Comparator<X4070BillInfo>() {
                @Override
                public int compare(X4070BillInfo o1, X4070BillInfo o2) {
                    int daysBetween =
                            daysBetween(DateUtil.parse(o1.getGmtCreate(), "yyyy-MM-dd"), DateUtil.parse(o2.getGmtCreate(), "yyyy-MM-dd"));
                    if (daysBetween > 0) {
                        return 1;
                    }
                    else if (daysBetween < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
            logger.debug("x4070BillInfos---->:", x4070BillInfos);
            // 对list中的数据进行分页处理
            List<X4070BillInfo> pageX4070BillInfos = new ArrayList<>();
            if (null != x4070BO.getPageSize() && x4070BO.getPageNum() > 0) {
                int pageNo = x4070BO.getPageNum();// 相当于pageNo
                int count = x4070BO.getPageSize();// 相当于pageSize
                int size = x4070BillInfos.size();
                int pageCount = size / count;
                int fromIndex = count * (pageNo - 1);
                int toIndex = fromIndex + count;
                if (toIndex >= size) {
                    toIndex = size;
                }
                if (pageNo > pageCount + 1) {
                    fromIndex = 0;
                    toIndex = 0;
                }
                pageX4070BillInfos = x4070BillInfos.subList(fromIndex, toIndex);
            }
            page.setTotalCount(x4070BillInfos.size());
            if (pageX4070BillInfos.size() > 0) {
                page.setRows(pageX4070BillInfos);
            }
            // 计算单位公务卡及个人公务卡汇总信息
            List<CoreProductObjectBillSum> peoCoreProductObjectBillSums = new ArrayList<>();
            List<CoreProductObjectBillSum> unitCoreProductObjectBillSums = new ArrayList<>();
            for (CoreProductObjectBillSum coreProductObjectBillSum : totalCoreProductObjectBillSumList) {
                CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
                coreCustomerSqlBuilder.andCustomerNoEqualTo(coreProductObjectBillSum.getCustomerNo());
                CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
                CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(coreCustomer.getOperationMode(),
                    coreProductObjectBillSum.getBusinessProgramNo());
                if ("PSN".equals(coreBusinessProgram.getResponseType()) || StringUtil.isEmpty(coreBusinessProgram.getResponseType())) {
                    peoCoreProductObjectBillSums.add(coreProductObjectBillSum);
                }
                else if ("CMP".equals(coreBusinessProgram.getResponseType())) {
                    unitCoreProductObjectBillSums.add(coreProductObjectBillSum);
                }
            }
            /** 个人公务卡账单金额 */
            BigDecimal peoPostingAmount = BigDecimal.ZERO;
            /** 个人公务卡本金金额 */
            BigDecimal peoPrincipalAmount = BigDecimal.ZERO;
            /** 个人公务卡利息金额 */
            BigDecimal peoInterestAamount = BigDecimal.ZERO;
            /** 个人公务卡费用金额 */
            BigDecimal peoFeeAmount = BigDecimal.ZERO;
            for (CoreProductObjectBillSum coreProductObjectBillSum : peoCoreProductObjectBillSums) {
                CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreProductObjectBillSum.getCurrencyCode());
                Integer decimalPlaces = coreCurrency.getDecimalPosition();
                peoPostingAmount =
                        peoPostingAmount.add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPostingAmount(), decimalPlaces));
                peoPrincipalAmount = peoPrincipalAmount
                    .add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPrincipalAmount(), decimalPlaces));
                peoInterestAamount = peoInterestAamount
                    .add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getInterestAmount(), decimalPlaces));
                peoFeeAmount = peoFeeAmount.add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getFeeAmount(), decimalPlaces));
            }
            peoCollectInfo.setFeeAmount(peoFeeAmount);
            peoCollectInfo.setInterestAamount(peoInterestAamount);
            peoCollectInfo.setPostingAmount(peoPostingAmount);
            peoCollectInfo.setPrincipalAmount(peoPrincipalAmount);
            /** 单位公务卡账单金额 */
            BigDecimal unitPostingAmount = BigDecimal.ZERO;
            /** 单位公务卡本金金额 */
            BigDecimal unitPrincipalAmount = BigDecimal.ZERO;
            /** 单位公务卡利息金额 */
            BigDecimal unitInterestAamount = BigDecimal.ZERO;
            /** 单位公务卡费用金额 */
            BigDecimal unitFeeAmount = BigDecimal.ZERO;
            for (CoreProductObjectBillSum coreProductObjectBillSum : unitCoreProductObjectBillSums) {
                CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreProductObjectBillSum.getCurrencyCode());
                Integer decimalPlaces = coreCurrency.getDecimalPosition();
                unitPostingAmount = unitPostingAmount
                    .add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPostingAmount(), decimalPlaces));
                unitPrincipalAmount = unitPrincipalAmount
                    .add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPrincipalAmount(), decimalPlaces));
                unitInterestAamount = unitInterestAamount
                    .add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getInterestAmount(), decimalPlaces));
                unitFeeAmount =
                        unitFeeAmount.add(CurrencyConversionUtil.reduce(coreProductObjectBillSum.getFeeAmount(), decimalPlaces));
            }
            unitCollectInfo.setFeeAmount(unitFeeAmount);
            unitCollectInfo.setInterestAmount(unitInterestAamount);
            unitCollectInfo.setPostingAmount(unitPostingAmount);
            unitCollectInfo.setPrincipalAmount(unitPrincipalAmount);
        }
        else {
            throw new BusinessException("COR-11008");
        }
        x4070Vo.setPeoCollectInfo(peoCollectInfo);
        x4070Vo.setUnitCollectInfo(unitCollectInfo);
        page.setObj(x4070Vo);
        return page;
    }

    public CoreMediaBasicInfo queryCoreMediaBasicInfo(String externalIdentificationNo) throws Exception {
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
        CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        return coreMediaBasicInfo;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @return
     */
    public static int daysBetween(Date start, Date end) {
        int days = DateConversionUtil.daysBetween(start, end);
        return days;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        // int page = 2;// 相当于pageNo
        // int count = 20;// 相当于pageSize
        // int size = list.size();// 5
        // int pageCount = size / count;// 0.25
        // int fromIndex = count * (page - 1);// 20
        // int toIndex = fromIndex + count;// 40
        // if (toIndex >= size) {
        // toIndex = size;
        // }
        // if (page > pageCount + 1) {
        // fromIndex = 0;
        // toIndex = 0;
        // }
        Date start = DateUtil.parse("2019-01-01", "yyyy-MM-dd");
        Date end = DateUtil.parse("2019-01-01", "yyyy-MM-dd");
        System.out.println(daysBetween(start, end));
        // System.out.println(list.subList(0, 5));
    }
}
