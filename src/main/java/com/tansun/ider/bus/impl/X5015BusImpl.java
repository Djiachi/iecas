package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5015Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.impl.CoreCustomerRemarksDaoImpl;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5015BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户备注信息新增活动设计
 * @author: admin
 */
@Service
public class X5015BusImpl implements X5015Bus {

	@Resource
	private CoreCustomerRemarksDaoImpl coreCustomerRemarksDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;

	@Override
	public Object busExecute(X5015BO x5015bo) throws Exception {
		
		//判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5015bo.getIsNew())) {
			if ("1".equals(x5015bo.getIsNew())) {
				return x5015bo;
			}
		}
				
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5015bo, eventCommAreaNonFinance);
		String operatorId = x5015bo.getOperatorId();
		List<CoreCustomerRemarks> listCoreCustomerRemarkss = x5015bo.getCoreCustomerRemarkss();
		if (listCoreCustomerRemarkss != null && listCoreCustomerRemarkss.size() > 0) {
			int i=1;
			for (CoreCustomerRemarks coreCustomerRemarks : listCoreCustomerRemarkss) {
				coreCustomerRemarks.setCustomerNo(x5015bo.getCustomerNo());
				coreCustomerRemarks.setId(RandomUtil.getUUID());
				coreCustomerRemarks.setVersion(1);
				coreCustomerRemarks.setSerialNo(i++);
			}
			@SuppressWarnings("unused")
			int result = coreCustomerRemarksDaoImpl.insertUseBatch(listCoreCustomerRemarkss);
		}
		if (operatorId == null) {
			operatorId = "system";
		}
		/**
		 * 生成非金融日志
		 */
		if (listCoreCustomerRemarkss != null && listCoreCustomerRemarkss.size() > 0) {
			for (CoreCustomerRemarks coreCustomerRemarks : listCoreCustomerRemarkss) {
				nonFinancialLogUtil.createNonFinancialActivityLog(x5015bo.getEventNo(), x5015bo.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreCustomerRemarks, coreCustomerRemarks.getId(),
						x5015bo.getCurrLogFlag(), operatorId, coreCustomerRemarks.getCustomerNo(),
						coreCustomerRemarks.getCustomerNo(), null,null);
			}
		}
		return eventCommAreaNonFinance;
	}

}
