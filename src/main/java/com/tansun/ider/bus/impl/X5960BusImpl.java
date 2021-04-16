package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5960Bus;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerElementDao;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.CoreGeneralActivityLogBDao;
import com.tansun.ider.dao.issue.CoreGeneralActivityLogDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreOlTransPostLogBDao;
import com.tansun.ider.dao.issue.CoreOlTransPostLogDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.CoreSpecialActLogHistDao;
import com.tansun.ider.dao.issue.CoreSpecialActivityLogBDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.entity.CoreGeneralActivityLog;
import com.tansun.ider.dao.issue.entity.CoreGeneralActivityLogB;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreOlTransPostLog;
import com.tansun.ider.dao.issue.entity.CoreOlTransPostLogB;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.entity.CoreSpecialActLogHist;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLogB;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerElementSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreGeneralActivityLogBSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreGeneralActivityLogSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOlTransPostLogBSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOlTransPostLogSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreSpecialActLogHistSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreSpecialActivityLogBSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.model.bo.X5960BO;
import com.tansun.ider.service.business.common.Constant;

/**
 * publicservice工程查询发卡数据库统一接口
 * 
 * @author lianhuan 2019年3月12日
 */
@Service
public class X5960BusImpl implements X5960Bus {
    @Autowired
    private CoreOlTransPostLogDao coreOlTransPostLogDao;
    @Autowired
    private CoreOlTransPostLogBDao coreOlTransPostLogBDao;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Autowired
    private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
    @Autowired
    private CoreCustomerContrlViewDao coreCustomerContrlViewDao;
    @Autowired
    private CoreProductFormDao coreProductFormDao;
    @Autowired
    private CoreGeneralActivityLogDao coreGeneralActivityLogDao;
    @Autowired
    private CoreSpecialActivityLogBDao coreSpecialActivityLogBDao;
    @Autowired
    private CoreGeneralActivityLogBDao coreGeneralActivityLogBDao;
    @Autowired
    private CoreTransHistDao coreTransHistDao;
    @Autowired
    private CoreSpecialActLogHistDao coreSpecialActLogHistDao;
    @Autowired
    private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;
    @Autowired
    private CoreCustomerElementDao coreCustomerElementDao;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;

    @Override
    public Object queryOlTransPostLog(X5960BO x5960bo) throws Exception {
        Integer postingRetryNum = x5960bo.getPostingRetryNum();
        String transBillingState = x5960bo.getTransBillingState();
        CoreOlTransPostLogSqlBuilder coreOlTransPostLogSqlBuilder = new CoreOlTransPostLogSqlBuilder();
        if (StringUtil.isNotBlank(transBillingState)) {
            coreOlTransPostLogSqlBuilder.andTransBillingStateEqualTo(transBillingState);
        }
        if (null != postingRetryNum) {
            coreOlTransPostLogSqlBuilder.andPostingRetryNumGreaterThan(postingRetryNum);
        }

        List<CoreOlTransPostLog> list = coreOlTransPostLogDao.selectListBySqlBuilder(coreOlTransPostLogSqlBuilder);
        return list;

    }

    @Override
    public Object queryOlTransPostLogB(X5960BO x5960bo) throws Exception {
        Integer postingRetryNum = x5960bo.getPostingRetryNum();
        String transBillingState = x5960bo.getTransBillingState();
        CoreOlTransPostLogBSqlBuilder coreOlTransPostLogBSqlBuilder = new CoreOlTransPostLogBSqlBuilder();
        if (StringUtil.isNotBlank(transBillingState)) {
            coreOlTransPostLogBSqlBuilder.andTransBillingStateEqualTo(transBillingState);
        }
        if (null != postingRetryNum) {
            coreOlTransPostLogBSqlBuilder.andPostingRetryNumGreaterThan(postingRetryNum);
        }

        List<CoreOlTransPostLogB> list = coreOlTransPostLogBDao.selectListBySqlBuilder(coreOlTransPostLogBSqlBuilder);
        return list;

    }

    @Override
    public Object queryAccount(X5960BO x5960bo) throws Exception {
        String accountId = x5960bo.getAccountId();
        String businessProgramNo = x5960bo.getBusinessProgramNo();
        String businessTypeCode = x5960bo.getBusinessTypeCode();
        String currencyCode = x5960bo.getCurrencyCode();
        String customerNo = x5960bo.getCustomerNo();
        String productObjectCode = x5960bo.getProductObjectCode();
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        if (StringUtil.isNotBlank(accountId)) {
            coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        }
        if (StringUtil.isNotBlank(businessProgramNo)) {
            coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        }
        if (StringUtil.isNotBlank(businessTypeCode)) {
            coreAccountSqlBuilder.andBusinessTypeCodeEqualTo(businessTypeCode);
        }
        if (StringUtil.isNotBlank(currencyCode)) {
            coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(productObjectCode)) {
            coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        }
        List<CoreAccount> list = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return list;
    }

    @Override
    public Object queryCustomer(X5960BO x5960bo) throws Exception {
        String customerNo = x5960bo.getCustomerNo();
        String idNumber = x5960bo.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(idNumber)) {
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
        }
        CoreCustomer customer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        return customer;
    }

    @Override
    public Object queryMediaBasicInfo(X5960BO x5960bo) throws Exception {
        String externalIdentificationNo = x5960bo.getExternalIdentificationNo();
        String invalidFlag = x5960bo.getInvalidFlag();
        String mediaUnitCode = x5960bo.getMediaUnitCode();
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        if (StringUtil.isNotBlank(externalIdentificationNo)) {
            coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        }
        if (StringUtil.isNotBlank(invalidFlag)) {
            coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(invalidFlag);
        }
        if (StringUtil.isNotBlank(mediaUnitCode)) {
            coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
        }
        List<CoreMediaBasicInfo> list = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        return list;
    }

    @Override
    public Object queryCustomerWaiveFeeInfo(X5960BO x5960bo) throws Exception {
        String customerNo = x5960bo.getCustomerNo();
        String feeItemNo = x5960bo.getFeeItemNo();
        String waiveCycleNo = x5960bo.getWaiveCycleNo();
        String currencyCode = x5960bo.getCurrencyCode();
        CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(feeItemNo)) {
            coreCustomerWaiveFeeInfoSqlBuilder.andFeeItemNoEqualTo(feeItemNo);
        }
        if (StringUtil.isNotBlank(waiveCycleNo)) {
            coreCustomerWaiveFeeInfoSqlBuilder.andWaiveCycleNoEqualTo(waiveCycleNo);
        }
        if (StringUtil.isNotBlank(currencyCode)) {
            coreCustomerWaiveFeeInfoSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        CoreCustomerWaiveFeeInfoSqlBuilder tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
        if (StringUtil.isNotBlank(x5960bo.getInstanCode1())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode1EqualTo(x5960bo.getInstanCode1());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode1EqualTo("");
            tempSqlBui.orInstanCode1IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5960bo.getInstanCode2())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode2EqualTo(x5960bo.getInstanCode2());
        }else{
        	tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode2EqualTo("");
            tempSqlBui.orInstanCode2IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5960bo.getInstanCode3())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode3EqualTo(x5960bo.getInstanCode3());
        }else{
        	tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode3EqualTo("");
            tempSqlBui.orInstanCode3IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5960bo.getInstanCode4())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode4EqualTo(x5960bo.getInstanCode4());
        }else{
        	tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode4EqualTo("");
            tempSqlBui.orInstanCode4IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5960bo.getInstanCode5())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode5EqualTo(x5960bo.getInstanCode5());
        }else{
        	tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode5EqualTo("");
            tempSqlBui.orInstanCode5IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo = coreCustomerWaiveFeeInfoDao
                .selectBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
        return coreCustomerWaiveFeeInfo;
    }

    @Override
    public Object queryCustomerContrlView(X5960BO x5960bo) throws Exception {
        String customerNo = x5960bo.getCustomerNo();
        CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        List<CoreCustomerContrlView> list = coreCustomerContrlViewDao
                .selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
        return list;
    }

    @Override
    public Object queryProductForm(X5960BO x5960bo) throws Exception {
        String productHolderNo = x5960bo.getProductHolderNo();
        String productForm = x5960bo.getProductForm();
        CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
        if (StringUtil.isNotBlank(productForm)) {
            coreProductFormSqlBuilder.andProductFormEqualTo(productForm);
        }
        if (StringUtil.isNotBlank(productHolderNo)) {
            coreProductFormSqlBuilder.andProductHolderNoEqualTo(productHolderNo);
        }
        CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
        return coreProductForm;
    }

    @Override
    public Object queryGeneralActivityLog(X5960BO x5960bo) throws Exception {
        String occurrDate = x5960bo.getOccurrDate();
        String customerNo = x5960bo.getCustomerNo();
        String transHisFlag = x5960bo.getTransHisFlag();
        CoreGeneralActivityLogSqlBuilder coreGeneralActivityLogSqlBuilder = new CoreGeneralActivityLogSqlBuilder();
        if (StringUtil.isNotBlank(occurrDate)) {
            coreGeneralActivityLogSqlBuilder.andLifecycleNodeIsNotNull();
            coreGeneralActivityLogSqlBuilder.andOccurrDateEqualTo(occurrDate);
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreGeneralActivityLogSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(transHisFlag)) {
            coreGeneralActivityLogSqlBuilder.andTransHisFlagEqualTo(transHisFlag);
        }
        List<CoreGeneralActivityLog> list = coreGeneralActivityLogDao
                .selectListBySqlBuilder(coreGeneralActivityLogSqlBuilder);
        return list;
    }

    @Override
    public Object querySpecialActivityLogB(X5960BO x5960bo) throws Exception {
        String globalSerialNumbr = x5960bo.getGlobalSerialNumbr();
        CoreSpecialActivityLogBSqlBuilder coreSpecialActivityLogBSqlBuilder = new CoreSpecialActivityLogBSqlBuilder();
        if (StringUtil.isNotBlank(globalSerialNumbr)) {
            coreSpecialActivityLogBSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
        }
        List<CoreSpecialActivityLogB> list = coreSpecialActivityLogBDao
                .selectListBySqlBuilder(coreSpecialActivityLogBSqlBuilder);
        return list;
    }

    @Override
    public Object queryGeneralActivityLogB(X5960BO x5960bo) throws Exception {
        String transHisFlag = x5960bo.getTransHisFlag();
        CoreGeneralActivityLogBSqlBuilder coreGeneralActivityLogBSqlBuilder = new CoreGeneralActivityLogBSqlBuilder();
        if (StringUtil.isNotBlank(transHisFlag)) {
            coreGeneralActivityLogBSqlBuilder.andTransHisFlagEqualTo(transHisFlag);
        }
        List<CoreGeneralActivityLogB> list = coreGeneralActivityLogBDao
                .selectListBySqlBuilder(coreGeneralActivityLogBSqlBuilder);
        return list;
    }

    @Override
    public Object queryTransHist(X5960BO x5960bo) throws Exception {
        String customerNo = x5960bo.getCustomerNo();
        String entityKey = x5960bo.getEntityKey();
        String currencyCode = x5960bo.getCurrencyCode();
        String globalSerialNumbr = x5960bo.getGlobalSerialNumbr();
        String occurrDate = x5960bo.getOccurrDate();
        String occurrTime = x5960bo.getOccurrTime();
        String logLevel = x5960bo.getLogLevel();
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(entityKey)) {
            coreTransHistSqlBuilder.andEntityKeyEqualTo(entityKey);
        }
        if (StringUtil.isNotBlank(currencyCode)) {
            coreTransHistSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        if (StringUtil.isNotBlank(globalSerialNumbr)) {
            coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
        }
        if (StringUtil.isNotBlank(occurrDate)) {
            coreTransHistSqlBuilder.andOccurrDateEqualTo(occurrDate);
        }
        if (StringUtil.isNotBlank(occurrTime)) {
            coreTransHistSqlBuilder.andOccurrTimeEqualTo(occurrTime);
        }
        if (StringUtil.isNotBlank(logLevel)) {
            coreTransHistSqlBuilder.andLogLevelEqualTo(logLevel);
        }
        List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
        return list;
    }

    @Override
    public Object querySpecialActLogHist(X5960BO x5960bo) throws Exception {
        String eventNo = x5960bo.getEventNo();
        String activityNo = x5960bo.getActivityNo();
        String accountId = x5960bo.getAccountId();
        String globalSerialNumbr = x5960bo.getGlobalSerialNumbr();
        String customerNo = x5960bo.getCustomerNo();
        String currencyCode = x5960bo.getCurrencyCode();
        String cycleNumber = x5960bo.getCycleNumber();
        CoreSpecialActLogHistSqlBuilder coreSpecialActLogHistSqlBuilder = new CoreSpecialActLogHistSqlBuilder();
        if (StringUtil.isNotBlank(eventNo)) {
            coreSpecialActLogHistSqlBuilder.andEventNoEqualTo(eventNo);
        }
        if (StringUtil.isNotBlank(activityNo)) {
            coreSpecialActLogHistSqlBuilder.andActivityNoEqualTo(activityNo);
        }
        if (StringUtil.isNotBlank(accountId)) {
            coreSpecialActLogHistSqlBuilder.andAccountIdEqualTo(accountId);
        }
        if (StringUtil.isNotBlank(globalSerialNumbr)) {
            coreSpecialActLogHistSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreSpecialActLogHistSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(currencyCode)) {
            coreSpecialActLogHistSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        if (StringUtil.isNotBlank(cycleNumber)) {
            coreSpecialActLogHistSqlBuilder.andCycleNumberEqualTo(Integer.parseInt(cycleNumber));
        }
        CoreSpecialActLogHist coreSpecialActLogHist = coreSpecialActLogHistDao
                .selectBySqlBuilder(coreSpecialActLogHistSqlBuilder);
        return coreSpecialActLogHist;
    }

    @Override
    public Object queryCustomerBusinessType(X5960BO x5960bo) throws Exception {
        CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
        // 客户号
        coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(x5960bo.getCustomerNo());
        // 定价层级代码
        if(StringUtil.isNotBlank(x5960bo.getPricingLevelCode())){
        	coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(x5960bo.getPricingLevelCode());
        }
        
        // 元件前8位或者是收费项目编号
        CoreCustomerBusinessTypeSqlBuilder tempSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
        tempSqlBuilder.orStateEqualTo(Constant.FLAG_Y);
        tempSqlBuilder.orStateEqualTo(Constant.FLAG_S);
        coreCustomerBusinessTypeSqlBuilder.and(tempSqlBuilder);
        String pricingObjectCode = x5960bo.getPricingObjectCode();
        if(StringUtil.isNotEmpty(pricingObjectCode)){
            coreCustomerBusinessTypeSqlBuilder.andPricingObjectCodeEqualTo(pricingObjectCode);
        }
        String pricingLevel = x5960bo.getPricingLevel();
        if(StringUtil.isNotEmpty(pricingLevel)){
            coreCustomerBusinessTypeSqlBuilder.andPricingLevelEqualTo(pricingLevel);
        }
        String pricingScope = x5960bo.getPricingScope();
        if(StringUtil.isNotEmpty(pricingScope)){
            coreCustomerBusinessTypeSqlBuilder.andPricingScopeEqualTo(pricingScope);
        }
        coreCustomerBusinessTypeSqlBuilder.andCustTagEffectiveDateLessThanOrEqualTo(x5960bo.getProcessingDate());
        coreCustomerBusinessTypeSqlBuilder.andCustTagExpirationDateGreaterThanOrEqualTo(x5960bo.getProcessingDate());
        return coreCustomerBusinessTypeDao.selectListBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
    }

    @Override
    public Object queryCustomerElement(X5960BO x5960bo) throws Exception {
        String elementNoPre = x5960bo.getElementNoPre();
        String customerNo = x5960bo.getCustomerNo();
        String processingDate = x5960bo.getProcessingDate();
        CoreCustomerElementSqlBuilder coreCustomerElementSqlBuilder = new CoreCustomerElementSqlBuilder();
        if (StringUtil.isNotBlank(processingDate)) {
            coreCustomerElementSqlBuilder.andEffectDateLessThanOrEqualTo(processingDate);
            coreCustomerElementSqlBuilder.andUneffectDateGreaterThanOrEqualTo(processingDate);
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerElementSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotBlank(elementNoPre)) {
            coreCustomerElementSqlBuilder.andElementNoLikeRigth(elementNoPre);
        }
        List<CoreCustomerElement> list = coreCustomerElementDao.selectListBySqlBuilder(coreCustomerElementSqlBuilder);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @Override
	public Object queryCustomerBillDay(X5960BO x5960bo) throws Exception {
		String eventNo = x5960bo.getEventNo();
        String activityNo = x5960bo.getActivityNo();
        String accountId = x5960bo.getAccountId();
        String globalSerialNumbr = x5960bo.getGlobalSerialNumbr();
        String customerNo = x5960bo.getCustomerNo();
        String currencyCode = x5960bo.getCurrencyCode();
        String cycleNumber = x5960bo.getCycleNumber();
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
        	coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        List<CoreCustomerBillDay> list = coreCustomerBillDayDao
                .selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
        return list;
	}

}
