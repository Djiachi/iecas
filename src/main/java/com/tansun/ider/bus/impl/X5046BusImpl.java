package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5046Bus;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductFeeItem;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.model.bo.X5046BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 产品增值服务费
 * 
 * @author admin
 *
 */
@Service
public class X5046BusImpl implements X5046Bus {

	@Autowired
	private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerBillDayDao coreCustomerBillDayDao;
	
	@Override
	public Object busExecute(X5046BO x5046bo) throws Exception {
		//判断是是否新增产品
		if (StringUtil.isNotBlank(x5046bo.getIsNew())) {
			if ("2".equals(x5046bo.getIsNew())) {
				return x5046bo;
			}
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5046bo, eventCommAreaNonFinance);
		String customerNo = x5046bo.getCustomerNo();
		String productObjectCode = x5046bo.getProductObjectCode();
		String operationMode = x5046bo.getOperationMode();
		// 1.产品增值服务费
		List<CoreProductFeeItem> coreProductFeeItemList =  httpQueryService.queryCoreProductFeeItem(productObjectCode, operationMode);
		if (null !=coreProductFeeItemList && !coreProductFeeItemList.isEmpty() ) {
			List<CoreCustomerWaiveFeeInfo> listCoreCustomerWaiveFeeInfos = new ArrayList<CoreCustomerWaiveFeeInfo>();
			for (CoreProductFeeItem coreProductFeeItem : coreProductFeeItemList) {
				String feeItemNo = coreProductFeeItem.getFeeItemNo();
				CoreFeeItem coreFeeItem1 = httpQueryService.queryCoreFeeItem(feeItemNo);
				String periodicFeeIdentifier = coreFeeItem1.getPeriodicFeeIdentifier();
				if (StringUtil.isBlank(periodicFeeIdentifier)|| (!Constant.FEE_fREQUENCY_P.equals(periodicFeeIdentifier))) {
        			continue;
        		}
				String defaultBusinessItem = coreFeeItem1.getDefaultBusinessItem();
				String nextBillDate = null;
				CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
				coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
				if (StringUtil.isNotBlank(defaultBusinessItem)) {
					coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(defaultBusinessItem);
					CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
					if (null != coreCustomerBillDay) {
						nextBillDate = coreCustomerBillDay.getNextBillDate();
					}else {
						CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilderNew = new CoreCustomerBillDaySqlBuilder();
						coreCustomerBillDaySqlBuilderNew.andCustomerNoEqualTo(customerNo);
						List<CoreCustomerBillDay> coreCustomerBillDayList = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilderNew);
						if (null != coreCustomerBillDayList && !coreCustomerBillDayList.isEmpty()) {
							CoreCustomerBillDay coreCustomerBillDay1 = coreCustomerBillDayList.get(0);
							nextBillDate = coreCustomerBillDay1.getNextBillDate();
						}
					}
				}else {
					List<CoreCustomerBillDay> coreCustomerBillDayList = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
					if (null != coreCustomerBillDayList && !coreCustomerBillDayList.isEmpty()) {
						CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayList.get(0);
						nextBillDate = coreCustomerBillDay.getNextBillDate();
					}
				}
        		CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo = new CoreCustomerWaiveFeeInfo();
        		coreCustomerWaiveFeeInfo.setCustomerNo(customerNo);
        		coreCustomerWaiveFeeInfo.setFeeItemNo(feeItemNo);
        		//获取运营币种
    			CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
        		//周期类费用标识
        		coreCustomerWaiveFeeInfo.setPeriodicFeeIdentifier(periodicFeeIdentifier);
        		if (Constant.FEE_fREQUENCY_P.equals(periodicFeeIdentifier)) {
        			coreCustomerWaiveFeeInfo.setCurrencyCode(coreOperationMode.getAccountCurrency());
        			coreCustomerWaiveFeeInfo.setWaiveCycleNo("0");
        			coreCustomerWaiveFeeInfo.setInstanCode1(productObjectCode);
            		coreCustomerWaiveFeeInfo.setInstanCode2(null);
            		coreCustomerWaiveFeeInfo.setInstanCode3(null);
            		coreCustomerWaiveFeeInfo.setInstanCode4(null);
            		coreCustomerWaiveFeeInfo.setInstanCode5(null);
        			coreCustomerWaiveFeeInfo.setChargingFrequency(coreFeeItem1.getChargingFrequency());
        			//失效日期
            		coreCustomerWaiveFeeInfo.setExpirationDate("");
				}
        		coreCustomerWaiveFeeInfo.setId(RandomUtil.getUUID());
        		coreCustomerWaiveFeeInfo.setNextExecutionDate(nextBillDate);
        		coreCustomerWaiveFeeInfo.setVersion(1);
				listCoreCustomerWaiveFeeInfos.add(coreCustomerWaiveFeeInfo);
			}
			if (null != listCoreCustomerWaiveFeeInfos && !listCoreCustomerWaiveFeeInfos.isEmpty()) {
				coreCustomerWaiveFeeInfoDao.insertUseBatch(listCoreCustomerWaiveFeeInfos);
			}
		}
		// 2.修改费用日期
		CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilderProd = new CoreCustomerWaiveFeeInfoSqlBuilder();
		coreCustomerWaiveFeeInfoSqlBuilderProd.andCustomerNoEqualTo(customerNo);
		coreCustomerWaiveFeeInfoSqlBuilderProd.andNextExecutionDateEqualTo("0000-00-00");
		List<CoreCustomerWaiveFeeInfo> coreCustomerWaiveFeeInfoList  = coreCustomerWaiveFeeInfoDao.selectListBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilderProd);
		if (null != coreCustomerWaiveFeeInfoList && !coreCustomerWaiveFeeInfoList.isEmpty()) {
			for (CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo : coreCustomerWaiveFeeInfoList) {
				String feeItemNo = coreCustomerWaiveFeeInfo.getFeeItemNo();
				CoreFeeItem coreFeeItem1 = httpQueryService.queryCoreFeeItem(feeItemNo);
				String defaultBusinessItem = coreFeeItem1.getDefaultBusinessItem();
				String nextBillDate = null;
				CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
				coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
				if (StringUtil.isNotBlank(defaultBusinessItem)) {
					coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(defaultBusinessItem);
					CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
					if (null != coreCustomerBillDay) {
						nextBillDate = coreCustomerBillDay.getNextBillDate();
					}else {
						CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilderNew = new CoreCustomerBillDaySqlBuilder();
						coreCustomerBillDaySqlBuilderNew.andCustomerNoEqualTo(customerNo);
						List<CoreCustomerBillDay> coreCustomerBillDayList = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilderNew);
						if (null != coreCustomerBillDayList && !coreCustomerBillDayList.isEmpty()) {
							CoreCustomerBillDay coreCustomerBillDay1 = coreCustomerBillDayList.get(0);
							nextBillDate = coreCustomerBillDay1.getNextBillDate();
						}
					}
				}else {
					List<CoreCustomerBillDay> coreCustomerBillDayList = coreCustomerBillDayDao.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
					if (null != coreCustomerBillDayList && !coreCustomerBillDayList.isEmpty()) {
						CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayList.get(0);
						nextBillDate = coreCustomerBillDay.getNextBillDate();
					}
				}
				CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
				coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
				coreCustomerWaiveFeeInfoSqlBuilder.andFeeItemNoEqualTo(coreCustomerWaiveFeeInfo.getFeeItemNo());
				coreCustomerWaiveFeeInfoSqlBuilder.andIdEqualTo(coreCustomerWaiveFeeInfo.getId());
				coreCustomerWaiveFeeInfo.setNextExecutionDate(nextBillDate);
				coreCustomerWaiveFeeInfoDao.updateBySqlBuilderSelective(coreCustomerWaiveFeeInfo, coreCustomerWaiveFeeInfoSqlBuilder);
			}
		}
		return eventCommAreaNonFinance;
	}

}
