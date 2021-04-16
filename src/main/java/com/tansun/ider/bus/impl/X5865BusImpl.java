package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5865Bus;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.model.bo.X5865BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * 客户周期费用删除
 * 
 * @ClassName X5865BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年9月19日 上午11:06:35
 * @version 5.0.0
 */
@Service
public class X5865BusImpl implements X5865Bus {

    private static Logger logger = LoggerFactory.getLogger(X5865BusImpl.class);

    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreProductDao coreProductDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Autowired
    private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;

    @Override
    public Object busExecute(X5865BO x5865bo) throws Exception {
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5865bo, eventCommAreaNonFinance);
        String customerNo = x5865bo.getCustomerNo();
        String operationMode = x5865bo.getOperationMode();

        // 收费项目编号新收费项目list
        List<CoreFeeItem> coreFeeItemList = x5865bo.getCoreFeeItemList();
        List<String> list = new ArrayList<String>();
        Map<String, CoreFeeItem> map = new HashMap<String, CoreFeeItem>();
        if (null != coreFeeItemList && !coreFeeItemList.isEmpty()) {
            for (CoreFeeItem feeItem : coreFeeItemList) {
                if(StringUtil.isNotBlank(feeItem.getExpirationDate())){
                    continue;
                }
                list.add(feeItem.getFeeItemNo());
                map.put(feeItem.getFeeItemNo(), feeItem);
            }
        }
        String expirationDate = getExpirationDate(x5865bo);
        // 筛选删除部分
        CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
        coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreCustomerWaiveFeeInfoSqlBuilder.andExpirationDateIsNull();
        coreCustomerWaiveFeeInfoSqlBuilder.andPeriodicFeeIdentifierEqualTo(Constant.CUST_CYCLE_FEE_FLAG);
        List<CoreCustomerWaiveFeeInfo> feeList = coreCustomerWaiveFeeInfoDao
                .selectListBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
        if (null != feeList && !feeList.isEmpty()) {
            Iterator<CoreCustomerWaiveFeeInfo> it = feeList.iterator();
            while (it.hasNext()) {
                CoreCustomerWaiveFeeInfo obj = it.next();
                String feeItemNo = obj.getFeeItemNo();
                if (!list.contains(feeItemNo)) {
                    // 将指定记录的失效日期设置为系统下一处理日（即营业日）+失效时间
                    expirationDate = expirationDate +" " +DateUtil.format(new Date(), "HH:mm:ss.SSS");
                    deleteOldFeeItemList(obj, expirationDate);
                } else {
                    list.remove(feeItemNo);
                    map.remove(feeItemNo);
                }
            }
        }
        if (null != map && map.size() != 0) {
            // 新增list
            List<CoreCustomerWaiveFeeInfo> listCoreCustomerWaiveFeeInfos = new ArrayList<CoreCustomerWaiveFeeInfo>();
            for (Map.Entry<String, CoreFeeItem> entry : map.entrySet()) {
                insertFeeItemList(entry.getValue(), listCoreCustomerWaiveFeeInfos, x5865bo);
            }
            if (null != listCoreCustomerWaiveFeeInfos && !listCoreCustomerWaiveFeeInfos.isEmpty()) {
                coreCustomerWaiveFeeInfoDao.insertUseBatch(listCoreCustomerWaiveFeeInfos);
            }
        }

        return eventCommAreaNonFinance;

    }

    private void insertFeeItemList(CoreFeeItem coreFeeItem,
            List<CoreCustomerWaiveFeeInfo> listCoreCustomerWaiveFeeInfos, X5865BO x5865bo) throws Exception {

        String feeItemNo = coreFeeItem.getFeeItemNo();
        CoreFeeItem coreFeeItem1 = httpQueryService.queryCoreFeeItem(feeItemNo);
        String periodicFeeIdentifier = coreFeeItem1.getPeriodicFeeIdentifier();
        CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo = new CoreCustomerWaiveFeeInfo();
        coreCustomerWaiveFeeInfo.setCustomerNo(x5865bo.getCustomerNo());
        coreCustomerWaiveFeeInfo.setFeeItemNo(feeItemNo);
        // 获取运营币种
        CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(x5865bo.getOperationMode());
        // 周期类费用标识
        coreCustomerWaiveFeeInfo.setPeriodicFeeIdentifier(periodicFeeIdentifier);
        if (Constant.CUST_CYCLE_FEE_FLAG.equals(periodicFeeIdentifier)) {
            coreCustomerWaiveFeeInfo.setCurrencyCode(coreOperationMode.getAccountCurrency());
            coreCustomerWaiveFeeInfo.setWaiveCycleNo("0");
            coreCustomerWaiveFeeInfo.setInstanCode1(coreOperationMode.getAccountCurrency());
            coreCustomerWaiveFeeInfo.setInstanCode2(null);
            coreCustomerWaiveFeeInfo.setInstanCode3(null);
            coreCustomerWaiveFeeInfo.setInstanCode4(null);
            coreCustomerWaiveFeeInfo.setInstanCode5(null);
            coreCustomerWaiveFeeInfo.setChargingFrequency(coreFeeItem1.getChargingFrequency());
            // 失效日期
            coreCustomerWaiveFeeInfo.setExpirationDate("");
        }

        String nextExecutionDate = "0000-00-00";
        // 查询产品
        CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
        coreProductSqlBuilder.andCustomerNoEqualTo(x5865bo.getCustomerNo());
        List<CoreProduct> proList = coreProductDao.selectListBySqlBuilder(coreProductSqlBuilder);
        if(null!=proList && !proList.isEmpty()){
            String  defaultBusPro = null;
                //判断默认业务项目是否存在客户业务项目表，并且是否存在产品关联业务项目
                defaultBusPro = getCusBusProList(proList,coreFeeItem.getDefaultBusinessItem());
            
            CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
            coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(x5865bo.getCustomerNo());
            coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(defaultBusPro);
            CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao
                    .selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
            if (null != coreCustomerBillDay) {
                nextExecutionDate = coreCustomerBillDay.getNextBillDate();
            }
        }
        if("0".equals(nextExecutionDate)){
            //取下一处理日
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andCustomerNoEqualTo(x5865bo.getCustomerNo());
            CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
            if(null!=coreCustomer){
                CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
                if(null!=coreSystemUnit){
                    nextExecutionDate = coreSystemUnit.getNextProcessDate();
                }
            }
        }
        coreCustomerWaiveFeeInfo.setNextExecutionDate(nextExecutionDate);
        coreCustomerWaiveFeeInfo.setId(RandomUtil.getUUID());
        coreCustomerWaiveFeeInfo.setVersion(1);
        listCoreCustomerWaiveFeeInfos.add(coreCustomerWaiveFeeInfo);

    }
    /**
     * 获取业务项目
     * 
     * @Description (TODO这里用一句话描述这个方法的作用)
     * @return
     * @throws Exception
     */
    public String getCusBusProList(List<CoreProduct> proList,String defaultBusPro) throws Exception {
        List<String> busList = new ArrayList<String>();
        List<CoreProductBusinessScope> busScoList =  new ArrayList<CoreProductBusinessScope>();
        for(CoreProduct coreProduct :proList){
            List<CoreProductBusinessScope> busProList = httpQueryService
                    .queryProductBusinessScope(coreProduct.getProductObjectCode(),coreProduct.getOperationMode());
            busScoList.addAll(busProList);
        }
        if(null!=busScoList && !busScoList.isEmpty()){
            for(CoreProductBusinessScope coreProductBusinessScope :busScoList){
                if(StringUtil.isNotBlank(coreProductBusinessScope.getBusinessProgramNo())){
                    busList.add(coreProductBusinessScope.getBusinessProgramNo());
                }
            }
        }
        
        if(null!=busList && !busList.isEmpty()){
             if(!busList.contains(defaultBusPro)){
                 defaultBusPro = busList.get(0);
             }
         }else{
             defaultBusPro = null;
         }
        return defaultBusPro;
    }
    private void deleteOldFeeItemList(CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo, String expirationDate)
            throws Exception {
        CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
        CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfoOld = new CoreCustomerWaiveFeeInfo();
        CachedBeanCopy.copyProperties(coreCustomerWaiveFeeInfo, coreCustomerWaiveFeeInfoOld);
        coreCustomerWaiveFeeInfo.setExpirationDate(expirationDate);
        coreCustomerWaiveFeeInfoSqlBuilder.andIdEqualTo(coreCustomerWaiveFeeInfo.getId());
        coreCustomerWaiveFeeInfoSqlBuilder.andVersionEqualTo(coreCustomerWaiveFeeInfo.getVersion());
        coreCustomerWaiveFeeInfo.setVersion(coreCustomerWaiveFeeInfo.getVersion() + 1);
        int i = coreCustomerWaiveFeeInfoDao.updateBySqlBuilder(coreCustomerWaiveFeeInfo,
                coreCustomerWaiveFeeInfoSqlBuilder);
    }

    private String getExpirationDate(X5865BO x5865bo) throws Exception {
        if (StringUtil.isNotBlank(x5865bo.getCustomerNo())) {
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            coreCustomerSqlBuilder.andCustomerNoEqualTo(x5865bo.getCustomerNo());
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            if (null != coreCustomer) {
                CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
                if (null != coreSystemUnit) {
                    return coreSystemUnit.getNextProcessDate();
                } else {
                    return null;
                }
            }
        }
        return null;

    }
}
