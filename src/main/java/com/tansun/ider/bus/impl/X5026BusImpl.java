package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5026Bus;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5026BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 客户增值服务费处理
 * 
 * @author admin
 *
 */
@Service
public class X5026BusImpl implements X5026Bus {

	@Autowired
	private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5026BO x5026bo) throws Exception {
		// 判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5026bo.getIsNew())) {
			if ("1".equals(x5026bo.getIsNew())) {
				return x5026bo;
			}
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5026bo, eventCommAreaNonFinance);
		String operatorId = x5026bo.getOperatorId();
		String customerNo = x5026bo.getCustomerNo();
		// operationMode
		String operationMode = x5026bo.getOperationMode();
		CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilderStr = new CoreCustomerWaiveFeeInfoSqlBuilder();
		coreCustomerWaiveFeeInfoSqlBuilderStr.andCustomerNoEqualTo(customerNo);
		List<CoreCustomerWaiveFeeInfo> coreCustomerWaiveFeeInfoList = coreCustomerWaiveFeeInfoDao
				.selectListBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilderStr);
		// 收费项目编号
		List<CoreFeeItem> coreFeeItemList = x5026bo.getCoreFeeItemList();
		if (null != coreFeeItemList && !coreFeeItemList.isEmpty()) {
			List<CoreCustomerWaiveFeeInfo> listCoreCustomerWaiveFeeInfos = new ArrayList<CoreCustomerWaiveFeeInfo>();
			for (CoreFeeItem coreFeeItem : coreFeeItemList) {
				String feeItemNo = coreFeeItem.getFeeItemNo();
				CoreFeeItem coreFeeItem1 = httpQueryService.queryCoreFeeItem(feeItemNo);
				String periodicFeeIdentifier = coreFeeItem1.getPeriodicFeeIdentifier();
				if (StringUtil.isBlank(periodicFeeIdentifier)
						|| (Constant.NO_CYCLE_FEE_FLAG.equals(periodicFeeIdentifier))) {
					continue;
				}
				CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo = new CoreCustomerWaiveFeeInfo();
				coreCustomerWaiveFeeInfo.setCustomerNo(customerNo);
				coreCustomerWaiveFeeInfo.setFeeItemNo(feeItemNo);
				// 获取运营币种
				CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
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
				coreCustomerWaiveFeeInfo.setNextExecutionDate("0000-00-00");
				coreCustomerWaiveFeeInfo.setId(RandomUtil.getUUID());
				coreCustomerWaiveFeeInfo.setVersion(1);
				//判断当前费用是否存在,如果已经存在，则不需要再次添加。
				if (null != coreCustomerWaiveFeeInfoList && !coreCustomerWaiveFeeInfoList.isEmpty()) {
					//判断当前是否存在
					for (CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo2 : coreCustomerWaiveFeeInfoList) {
						if (coreCustomerWaiveFeeInfo2.getFeeItemNo().equals(feeItemNo)) {
							throw new BusinessException("CUS-00148");
						}
					}
					
				}
					listCoreCustomerWaiveFeeInfos.add(coreCustomerWaiveFeeInfo);
			}
				coreCustomerWaiveFeeInfoDao.insertUseBatch(listCoreCustomerWaiveFeeInfos);
		}

		return eventCommAreaNonFinance;
	}
	
}
