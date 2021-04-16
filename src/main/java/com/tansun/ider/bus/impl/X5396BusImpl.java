/**  

* @Title: X5396BusImpl.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年4月30日  

* @version R04.00 

*/
package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5396Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CorePcdInstan;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5396BO;
import com.tansun.ider.model.vo.X5396DateVO;
import com.tansun.ider.model.vo.X5396VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.DateConversionUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * 
 * @ClassName: X5396BusImpl
 * 
 * @Function:未出账单查询 :货币代码为空时，返回币种列表；反之返回汇总金额和交易详情.
 * 
 * @Description: 1.根据外部识别号（或证件类型+证件号）获取客户号。 2.根据客户号查询出产品列表
 *               3.根据客户号查询出产品产品对象代码和运营模式，放入公共区中，调用公用方法查询出对应项目下的币种pcd。
 *               4.汇总出客户号下所有币种（去重），获得币种列表。
 *               5.如果传出货币代码字段为空，则遍历币种币种列表，查询出货币代码对应的货币名称。返回数据；反之，通过客户号获取当前周期号，
 *               根据客户号、币种、周期号获取交易历史，并按借贷记方向和币种，计算汇总金额。
 * 
 * 
 * @author baiyu
 * 
 * @date 2019年4月30日
 * 
 * @version R04.00
 * 
 */
@Service
public class X5396BusImpl implements X5396Bus {
    private static Logger logger = LoggerFactory.getLogger(X5396BusImpl.class);
    //默认产品对象代码0
    private static final String PRODUCT_OBJECT_CODE_ZERO = "0";
    //还款未拆分之前的默认币种
    private static final String DEFAULT_CURRENCY_CODE = "999";
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreTransHistDao coreTransHistDao;
    @Autowired
    private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;
    @Autowired
    private ParamsUtil paramsUtil;
    @Override
    public Object busExecute(X5396BO x5396BO) throws Exception {
        // 外部识别号
        String externalIdentificationNo = x5396BO.getExternalIdentificationNo();
        logger.debug("------externalIdentificationNo:" + externalIdentificationNo);
        // 货币代码
        String currencyCode = x5396BO.getCurrencyCode();
        logger.debug("------currencyCode:" + currencyCode);
        // 查询业务项目代码标识
        boolean queryBusinessProgramNo = x5396BO.getQueryBusinessProgramNo();
        logger.debug("------queryBusinessProgramNo:" + queryBusinessProgramNo);
        // 业务项目代码
        String businessProgramNo = x5396BO.getBusinessProgramNo();
        logger.debug("------businessProgramNo:" + businessProgramNo);
        //起始日期
        String startDate = x5396BO.getStartDate();
        logger.debug("------startDate:" + startDate);
        //结束日期
        String endDate = x5396BO.getEndDate();
        logger.debug("------endDate:" + endDate);
        boolean queryDate = x5396BO.getQueryDate();
        logger.debug("------queryDate:" + queryDate);
        CoreMediaBasicInfo coreMediaBasicInfo = getCoreMediaBasicInfo(externalIdentificationNo);
        logger.debug("------coreMediaBasicInfo:" + coreMediaBasicInfo);
        // 调用公共方法查询客户号
        String customerNo = coreMediaBasicInfo.getMainCustomerNo();
        logger.debug("------customerNo:" + customerNo);
        // 产品对象代码
        String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
        logger.debug("------productObjectCode:" + productObjectCode);
        //运营模式
        String operationMode = coreMediaBasicInfo.getOperationMode();
        logger.debug("------operationMode:" + operationMode);
        String entrys = Constant.EMPTY_LIST;
        X5396VO x5396VO = null;
        PageBean<X5396BO> page = null;
        if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(productObjectCode)
                && StringUtil.isNotBlank(operationMode)) {
            EventCommArea eventCommArea = new EventCommArea();
            eventCommArea.setEcommOperMode(operationMode);
            List<CoreActivityArtifactRel> artifactList = x5396BO.getActivityArtifactList();
            logger.debug("------artifactList:" + artifactList);
            // 调用获得客户币种列表方法获得客户币种列表
            Set<String> pcdSet = this.getCurrCodeList(eventCommArea, productObjectCode, artifactList);
            logger.debug("------pcdSet:" + pcdSet);
            if (StringUtil.isBlank(currencyCode) && !queryBusinessProgramNo && StringUtil.isBlank(businessProgramNo)) {
                List<X5396VO> currencyCodeList = new ArrayList<>();
                for (String pcd : pcdSet) {
                    x5396VO = new X5396VO();
                    // 根据货币代码获取货币名称
                    x5396VO.setCurrencyCode(pcd);
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(pcd);
                    String currencyName = coreCurrency.getCurrencyDesc();
                    x5396VO.setCurrencyName(currencyName);
                    currencyCodeList.add(x5396VO);
                }
                PageBean<X5396VO> currencyCodePage = new PageBean<>();
                currencyCodePage.setRowsCount(currencyCodeList.size());
                currencyCodePage.setRows(currencyCodeList);
                logger.debug("------currencyCodeList:" + currencyCodeList);
                return currencyCodePage;
            } else if (queryBusinessProgramNo) {
                List<CoreCustomerBillDay> CoreCustomerBillDayList = getbusinessProgramNo(customerNo);
                PageBean<CoreCustomerBillDay> CoreCustomerBillDayPage = new PageBean<>();
                CoreCustomerBillDayPage.setRowsCount(CoreCustomerBillDayList.size());
                CoreCustomerBillDayPage.setRows(CoreCustomerBillDayList);
                return CoreCustomerBillDayPage;
            } else if (StringUtil.isNotBlank(businessProgramNo)) {
                // 通过客户号获取当前周期号和下一账单日期
                CoreCustomerBillDay coreCustomerBillDay = this.getCoreCustomerBillDay(customerNo, businessProgramNo);
                // 当前周期号
                Integer currentCycleNum = coreCustomerBillDay.getCurrentCycleNumber();
                logger.debug("------currentCycleNum:" + currentCycleNum);
                if (queryDate) {
                    // 结束日期
                     endDate = coreCustomerBillDay.getNextBillDate();
                    logger.debug("------endDate:" + endDate);
                    // 开始日期
                    startDate = getStartDate(customerNo, eventCommArea, currentCycleNum, businessProgramNo,
                            x5396BO.getActivityArtifactList(), endDate);
                    logger.debug("------startDate:" + startDate);
                    PageBean<X5396DateVO> datePage = new PageBean<>();
                    datePage.setObj(new X5396DateVO(startDate, endDate));
                    logger.debug("------datePage:" + datePage);
                    return datePage;
                } else if (StringUtil.isNotBlank(currencyCode)&&StringUtil.isNotBlank(productObjectCode)) {
                    page = new PageBean<>();
                    x5396VO = new X5396VO();
                    // 根据客户号、币种、周期号查询交易历史表
                    CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
                    coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
                    coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().andCurrencyCodeEqualTo(currencyCode)
                            .or(new CoreTransHistSqlBuilder().andCurrencyCodeEqualTo(DEFAULT_CURRENCY_CODE)
                                    .andTransCurrCdeEqualTo(currencyCode)));
                    coreTransHistSqlBuilder.andBusinessProgramCodeEqualTo(businessProgramNo);
                    coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(startDate);
                    coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(endDate);
                    boolean  manyAccount = manyAccount(eventCommArea,artifactList);
                    if(manyAccount){
                        CoreTransHistSqlBuilder secondaryCoreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
                        secondaryCoreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                        productObjectCode=PRODUCT_OBJECT_CODE_ZERO;
                        secondaryCoreTransHistSqlBuilder.orProductObjectCodeEqualTo(productObjectCode);
                        coreTransHistSqlBuilder.and(secondaryCoreTransHistSqlBuilder);
                        coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().andExternalIdentificationNoEqualTo(externalIdentificationNo).orExternalIdentificationNoIsNull());
                    }else{
                        externalIdentificationNo=null;
                        coreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                    }
                    // 金额汇总
                    BigDecimal actualPostingAmount = this.getSumActualPostAmt(customerNo, currencyCode, startDate, endDate, businessProgramNo, productObjectCode, externalIdentificationNo);
                    logger.debug("------actualPostingAmount:" + actualPostingAmount);
                    coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder()
                            .andActivityNoEqualTo(Constant.BALANCE_UNIT_ENTRY_ACTIVITYNO)
                            .orActivityNoEqualTo(Constant.BALANCE_UNIT_DISTRIBUTION_ACTIVITYNO)
                            .orActivityNoEqualTo(Constant.BALANCE_UNIT_RESUME_ACTIVITYNO)
                            .or(new CoreTransHistSqlBuilder().orActivityNoEqualTo(Constant.ACCOUNT_LOCATION_ACTIVITYNO)
                                    .and(new CoreTransHistSqlBuilder()
                                            .orEventNoEqualTo(Constant.TOTAL_EVENTS_OF_REPAYMENT)
                                            .orEventNoEqualTo(Constant.REPAYMENT_RESTORATION_TOTAL_EVENT)
                                            .orEventNoEqualTo(Constant.EXPENSE_ENTRY_EVENT))));
                    int totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
                    page.setTotalCount(totalCount);
                    if (null != x5396BO.getPageSize() && null != x5396BO.getIndexNo()) {
                        coreTransHistSqlBuilder.orderByOccurrDate(false);
                        coreTransHistSqlBuilder.setPageSize(x5396BO.getPageSize());
                        coreTransHistSqlBuilder.setIndexNo(x5396BO.getIndexNo());
                        page.setPageSize(x5396BO.getPageSize());
                        page.setIndexNo(x5396BO.getIndexNo());
                    }
                    if (totalCount > 0) {
                    	List<X5396BO> x5396BOList = new ArrayList<>();
                        List<CoreTransHist> coreTransHistList = coreTransHistDao
                                .selectListBySqlBuilder(coreTransHistSqlBuilder);
                        for (CoreTransHist coreTransHist : coreTransHistList) {
                        	X5396BO x5396bo = new X5396BO();
                        	CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
                            int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
                            coreTransHist
                                    .setTransAmount(CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(), currencyDecimal));
                            coreTransHist
                                    .setActualPostingAmount(CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(), currencyDecimal));
                        	CachedBeanCopy.copyProperties(coreTransHist,x5396bo);
                        	//获取业务项目描述
                        	if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(businessProgramNo)){
                        		CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode, businessProgramNo);
                        		if(null != coreBusinessProgram){
                        			x5396bo.setProgramDesc(coreBusinessProgram.getProgramDesc());
                        		}
                        	}
                        	//获取币种描述
                        	if(StringUtil.isNotBlank(x5396bo.getTransCurrCde())){
                        		CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5396bo.getTransCurrCde());
                        		if(null != coreCurrency){
                        			x5396bo.setTransCurrDesc(coreCurrency.getCurrencyDesc());
                        		}
                        	}
                        	if(StringUtil.isNotBlank(x5396bo.getPostingCurrencyCode())){
                        		CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5396bo.getPostingCurrencyCode());
                        		if(null != coreCurrency){
                        			x5396bo.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                        		}
                        	}
                        	if(StringUtil.isNotBlank(x5396bo.getSettlementCurrencyCode())){
                        		CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5396bo.getSettlementCurrencyCode());
                        		if(null != coreCurrency){
                        			x5396bo.setSettlementCurrencyDesc(coreCurrency.getCurrencyDesc());
                        		}
                        	}
                        	if(StringUtil.isNotBlank(x5396bo.getCurrencyCode())){
                        		CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5396bo.getCurrencyCode());
                        		if(null != coreCurrency){
                        			x5396bo.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                        		}
                        	}
                        	if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5396bo.getBusinessTypeCode())){
                        		CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode, x5396bo.getBusinessTypeCode());
                        		if(null != coreBusinessType){
                        			x5396bo.setBusinessDesc(coreBusinessType.getBusinessDesc());
                        		}
                        	}
                        	if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5396bo.getBalanceObjectCode())){
                        		CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, x5396bo.getBalanceObjectCode());
                        		if(null != coreBalanceObject){
                        			x5396bo.setObjectDesc(coreBalanceObject.getObjectDesc());
                        		}
                        	}
                        	x5396bo.setPostingAmount(amountConversion(x5396bo.getPostingAmount(), x5396bo.getPostingCurrencyCode()));
                        	x5396bo.setSettlementAmount(amountConversion(x5396bo.getSettlementAmount(), x5396bo.getSettlementCurrencyCode()));
                        	x5396BOList.add(x5396bo);
                        }
//                        amountConversion(coreTransHistList, currencyCode);
                        x5396VO.setActualPostingAmount(actualPostingAmount);
                        page.setRows(x5396BOList);
                        page.setObj(x5396VO);
                        if (null != coreTransHistList && !coreTransHistList.isEmpty()) {
                            entrys = coreTransHistList.get(0).getId();
                        }
                    }
                }

            } else {
                // 抛出异常CUS-00005客户信息查询失败
                throw new BusinessException("CUS-00107");
            }

        } else {
            // 抛出异常CUS-00005客户信息查询失败
            throw new BusinessException("CUS-00005");
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5396BO.getCoreEventActivityRel().getEventNo(),
                x5396BO.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5396BO.getOperatorId());
        return page;
    }

    /**
     * 
     *
     * @MethodName getCoreMediaBasicInfo
     * @Description: 根据外部识别号获取媒介基本信息
     * @param externalIdentificationNo
     * @return
     * @throws Exception
     * @return: CoreMediaBasicInfo
     */
    private CoreMediaBasicInfo getCoreMediaBasicInfo(String externalIdentificationNo) throws Exception {
        CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService
                .queryCoreMediaBasicInfoForExt(externalIdentificationNo);
        if (coreMediaBasicInfo == null) {
            throw new BusinessException("COR-00001");
        }
        return coreMediaBasicInfo;
    }

    /**
     * 
     *
     * @MethodName manyAccount
     * @Description: 判断产品对象是否为归户（多账户）
     * @param coreTransHistSqlBuilder
     * @return
     * @return: CoreTransHistSqlBuilder
     * @throws Exception
     */
    private boolean manyAccount(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList)
            throws Exception {
        logger.debug("singleOrManyAccount    eventCommArea:" + eventCommArea + "  artifactList:" + artifactList);
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        Map.Entry<String, String> entry = it.next();
        String entryKey = entry.getKey();
        logger.debug("singleOrManyAccount    entryKey:" + entryKey);
        if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entryKey)) {
            return false;
        } else if (Constant.BUSINESS_ACCOUNTS.equals(entryKey)) {
            return true;
        } else {
            logger.error("singleOrManyAccount    未匹配上构件");
            throw new BusinessException("COR-10002");
        }
    }

    /**
     * 根据401构件元件查询产品对象对应的币种列表
     * 
     * @param eventCommArea
     * @param artifactList
     * @return
     * @throws Exception
     */
    private Set<String> getCurrCodeList(EventCommArea eventCommArea, String productObjectCode,
            List<CoreActivityArtifactRel> artifactList) {
        Set<String> pcdSet = new TreeSet<>();
        eventCommArea.setEcommProdObjId(productObjectCode);
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_401, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap;
        try {
            elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_401, eventCommArea);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.debug("[getCurrCodeList]  无法匹配401构件      eventCommArea:" + eventCommArea);
            return pcdSet;
        }
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String entryKey = entry.getKey();
            // 401AAA01
            if (Constant.POSTING_CURRENCY_OPTION.equals(entryKey.substring(0, 8))) {
                List<CorePcdInstan> corePcdInstanList = JSON.parseArray(entry.getValue(), CorePcdInstan.class);
                if (logger.isDebugEnabled()) {
                    logger.debug("PCD信息={} ", entry.getKey(), entry.getValue());
                }
                for (CorePcdInstan corePcdInstan : corePcdInstanList) {
                    pcdSet.add(corePcdInstan.getPcdValue());
                }
            }
        }
        return pcdSet;
    }

    /**
     * 查询客户下业务项目信息
     * 
     * @param customerNo
     *            客户编码
     * @return
     * @throws Exception
     */
    private List<CoreCustomerBillDay> getbusinessProgramNo(String customerNo) throws Exception {
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        List<CoreCustomerBillDay> coreCustomerBillDayList = coreCustomerBillDayDao
                .selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
        return coreCustomerBillDayList;
    }

    /**
     * @Description: 根据客户号和业务项目代码获取客户业务项目（主要是获取周期号和下一账单日期）
     * 
     * @MethodName: getCurrentCycleNum
     * 
     * 
     * @param customerNo
     * @return
     * @throws Exception
     */
    private CoreCustomerBillDay getCoreCustomerBillDay(String customerNo, String businessProgramNo) throws Exception {
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao
                .selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        return coreCustomerBillDay;
    }

    /**
     * @Description: 对查询出来的交易信息进行汇总
     * @MethodName: getSumActualPostAmt
     * @param list
     * @return
     * @throws Exception
     */

    private BigDecimal getSumActualPostAmt(String customerNo, String currencyCode, String startDate, String endDate,
            String businessProgramNo, String productObjectCode, String externalIdentificationNo) throws Exception {
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder().andCurrencyCodeEqualTo(currencyCode)
                .or(new CoreTransHistSqlBuilder().andCurrencyCodeEqualTo(DEFAULT_CURRENCY_CODE)
                        .andTransCurrCdeEqualTo(currencyCode)));
        coreTransHistSqlBuilder.andBusinessProgramCodeEqualTo(businessProgramNo);
        coreTransHistSqlBuilder.andOccurrDateGreaterThanOrEqualTo(startDate);
        coreTransHistSqlBuilder.andOccurrDateLessThanOrEqualTo(endDate);
        if (externalIdentificationNo != null) {
            CoreTransHistSqlBuilder secondaryCoreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
            secondaryCoreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
            productObjectCode=PRODUCT_OBJECT_CODE_ZERO;
            secondaryCoreTransHistSqlBuilder.orProductObjectCodeEqualTo(productObjectCode);
            coreTransHistSqlBuilder.and(secondaryCoreTransHistSqlBuilder);
            coreTransHistSqlBuilder.and(new CoreTransHistSqlBuilder()
                    .andExternalIdentificationNoEqualTo(externalIdentificationNo).orExternalIdentificationNoIsNull());
        }else{
            coreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        }
        coreTransHistSqlBuilder
                .and(new CoreTransHistSqlBuilder().orActivityNoEqualTo(Constant.BALANCE_UNIT_ENTRY_ACTIVITYNO)
                        .orActivityNoEqualTo(Constant.BALANCE_UNIT_DISTRIBUTION_ACTIVITYNO)
                        .orActivityNoEqualTo(Constant.BALANCE_UNIT_RESUME_ACTIVITYNO));
        List<CoreTransHist> coreTransHistList = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
        BigDecimal actualPostingAmount = BigDecimal.ZERO;
        if (coreTransHistList != null && coreTransHistList.size() > 0) {
            for (CoreTransHist coreTransHist : coreTransHistList) {
                if ("D".equals(coreTransHist.getLoanSign())
                        && (coreTransHist.getActualPostingAmount().compareTo(BigDecimal.ZERO) > 0)) {
                    actualPostingAmount = actualPostingAmount.add(coreTransHist.getActualPostingAmount());
                } else if (("C".equals(coreTransHist.getLoanSign())
                        && (coreTransHist.getActualPostingAmount().compareTo(BigDecimal.ZERO) > 0))) {
                    actualPostingAmount = actualPostingAmount.subtract(coreTransHist.getActualPostingAmount());
                }
            }
            // 以分为单位转换成以元为单位
            CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
            int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
            actualPostingAmount = CurrencyConversionUtil.reduce(actualPostingAmount, currencyDecimal);
        }
        return actualPostingAmount;
    }

    /**
     * @Description: 获取开始日期
     * 
     * @MethodName: getStartDate
     * 
     * 
     * @return
     * @throws Exception
     */
    private String getStartDate(String customerNo, EventCommArea eventCommArea, Integer currentCycleNum,
            String businessProgramNo, List<CoreActivityArtifactRel> artifactList, String nextBillDate)
            throws Exception {
        Date startDate=null;
        if (currentCycleNum == 1) {
            String lastBillDateStr = getLastBillDate(eventCommArea, businessProgramNo, artifactList, nextBillDate);
            logger.debug("------[X5396BusImpl.getStartDate]: lastBillDateStr=" + lastBillDateStr);
            Date lastBillDate = DateConversionUtil.getDate(lastBillDateStr);
            logger.debug("------[X5396BusImpl.getStartDate]: lastBillDate=" + lastBillDate);
            startDate = DateConversionUtil.addDays(lastBillDate, 1);
        } else {
            CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
            coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
            coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(currentCycleNum - 1);
            CoreCustomerUnifyInfo coreCustomerUnifyInfo = coreCustomerUnifyInfoDao
                    .selectBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
            String statementDate = coreCustomerUnifyInfo.getStatementDate();
            logger.debug("------[X5396BusImpl.getStartDate]: statementDate=" + statementDate);
            startDate = DateConversionUtil.getDate(statementDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        logger.debug("------[X5396BusImpl.getStartDate]: startDate=" + startDate);
        return DateConversionUtil.getDateStr(startDate);
    }

    /**
     * 
     * @Description: 获取上一账单日（当前周期号为1时使用）
     * @param eventCommArea
     * @param artifactList
     * @return
     * @throws Exception
     */
    private String getLastBillDate(EventCommArea eventCommArea, String businessProgramNo,
            List<CoreActivityArtifactRel> artifactList, String nextBillDate) throws Exception {
        // 506 构件
        String lastBillDate = null;
        String pcd = null;
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_506, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        eventCommArea.setEcommBusinessProgramCode(businessProgramNo);
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_506, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String entryKey = entry.getKey();
            if (Constant.CYCLE_BY_MONTH.equals(entryKey) || Constant.CYCLE_BY_WEEK.equals(entryKey)) { // 506AAA0100
                pcd = entry.getValue().toString().trim();
                lastBillDate = getLastBillDateByPcd(entryKey, nextBillDate, pcd);
            }
        }
        return lastBillDate;
    }

    /**
     * @Description: 通过pcd获取开始日期
     * 
     * @MethodName: getLastBillDateByPcd
     * @return
     * @throws ParseException
     */
    private String getLastBillDateByPcd(String entryKey, String nextBillDate, String pcd) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Date sdfDate = DateConversionUtil.getDate(nextBillDate);
        calendar.setTime(sdfDate);
        if (Constant.CYCLE_BY_MONTH.equals(entryKey)) {
            calendar.add(Calendar.MONTH, -(Integer.valueOf(pcd)));
        } else if (Constant.CYCLE_BY_WEEK.equals(entryKey)) {
            calendar.add(Calendar.WEEK_OF_MONTH, -(Integer.valueOf(pcd)));
        } else {
            throw new BusinessException("COR-10031");
        }
        sdfDate = calendar.getTime();
        return DateConversionUtil.getDateStr(sdfDate);
    }

    /**
     * 
     *
     * @MethodName amountConversion
     * @Description: 查询交易信息中金额转换
     * @param list
     * @param currencyCode
     * @throws Exception
     * @return: void
     */
    private BigDecimal amountConversion(BigDecimal amount, String currencyCode) throws Exception {
        CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
        int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
        return  CurrencyConversionUtil.reduce(amount, currencyDecimal);

    }
}
