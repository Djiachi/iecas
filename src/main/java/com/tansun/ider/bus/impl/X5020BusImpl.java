package com.tansun.ider.bus.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5020Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.impl.CoreCustomerBusinessTypeDaoImpl;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.model.bo.X5020BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 新增客户类业务标签新增活动设计
 * @author: admin
 */
@Service
public class X5020BusImpl implements X5020Bus {

	@Autowired
	private CoreCustomerBusinessTypeDaoImpl coreCustomerBusinessTypeDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;

	@Override
	public Object busExecute(X5020BO x5020bo) throws Exception {

		//判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5020bo.getIsNew())) {
			if ("1".equals(x5020bo.getIsNew())) {
				return x5020bo;
			}
		}
		
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5020bo, eventCommAreaNonFinance);
		String operatorId = x5020bo.getOperatorId();
		List<CoreCustomerBusinessType> listCoreCustomerBusinessTypes = x5020bo.getCoreCustomerBusinessTypes();
		if (listCoreCustomerBusinessTypes != null && listCoreCustomerBusinessTypes.size() > 0) {
			for (CoreCustomerBusinessType coreCustomerBusinessType : listCoreCustomerBusinessTypes) {
				coreCustomerBusinessType.setCustomerNo(x5020bo.getCustomerNo());
				coreCustomerBusinessType.setId(RandomUtil.getUUID());
				coreCustomerBusinessType.setSettingDate(DateUtil.format(new Date(), "yyyy:MM:dd"));
				coreCustomerBusinessType.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
				coreCustomerBusinessType.setSettingUpUserid(operatorId);
				coreCustomerBusinessType.setVersion(1);
				coreCustomerBusinessType.setState(YesOrNo.YES.getValue());
			}
			@SuppressWarnings("unused")
			int result = coreCustomerBusinessTypeDaoImpl.insertUseBatch(listCoreCustomerBusinessTypes);
		}
		if (operatorId == null) {
			operatorId = "system";
		}
		if (listCoreCustomerBusinessTypes != null && listCoreCustomerBusinessTypes.size() > 0) {
			for (CoreCustomerBusinessType coreCustomerBusinessType : listCoreCustomerBusinessTypes) {
				nonFinancialLogUtil.createNonFinancialActivityLog(x5020bo.getEventNo(), x5020bo.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreCustomerBusinessType,
						coreCustomerBusinessType.getId(), x5020bo.getCurrLogFlag(), operatorId,
						coreCustomerBusinessType.getCustomerNo(), coreCustomerBusinessType.getCustomerNo(), null,null);
			}
		}
		return eventCommAreaNonFinance;
	}

}
