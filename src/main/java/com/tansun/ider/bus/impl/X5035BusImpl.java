package com.tansun.ider.bus.impl;

import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5035Bus;
import com.tansun.ider.dao.issue.entity.CoreProductAdditionalInfo;
import com.tansun.ider.dao.issue.impl.CoreProductAdditionalInfoDaoImpl;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5035BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 新增媒介附加信息表
 * @author: admin
 */
@Service
public class X5035BusImpl implements X5035Bus {

	@Autowired
	private CoreProductAdditionalInfoDaoImpl coreProductAdditionalInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	
	@Override
	public Object busExecute(X5035BO x5035bo) throws Exception {
		
		//判断是是否新增产品
		if (StringUtil.isNotBlank(x5035bo.getIsNew())) {
			if ("2".equals(x5035bo.getIsNew())) {
				return x5035bo;
			}
		}
		
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5035bo, eventCommAreaNonFinance);
		String operatorId = x5035bo.getOperatorId();
		@SuppressWarnings("unused")
		Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
		CoreProductAdditionalInfo coreProductAdditionalInfo = new CoreProductAdditionalInfo();
		if (operatorId == null) {
			operatorId = "system";
		}
		if (StringUtil.isNotBlank(x5035bo.getCoBrandedNo()) && !"".equals(x5035bo.getCoBrandedNo())) {
			coreProductAdditionalInfo.setId(RandomUtil.getUUID());
			coreProductAdditionalInfo.setCoBrandedNo(x5035bo.getCoBrandedNo());
			coreProductAdditionalInfo.setVersion(1);
			coreProductAdditionalInfo.setProductObjectCode(eventCommAreaNonFinance.getProductObjectCode());
			coreProductAdditionalInfo.setCustomerNo(eventCommAreaNonFinance.getCustomerNo());
			coreProductAdditionalInfo.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
			@SuppressWarnings("unused")
			int result = coreProductAdditionalInfoDaoImpl.insert(coreProductAdditionalInfo);
			
			nonFinancialLogUtil.createNonFinancialActivityLog(x5035bo.getEventNo(), x5035bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreProductAdditionalInfo, coreProductAdditionalInfo.getId(),
					x5035bo.getCurrLogFlag(), operatorId, coreProductAdditionalInfo.getCustomerNo(),
					coreProductAdditionalInfo.getProductObjectCode(), null,null);
		}

		return eventCommAreaNonFinance;
	}

}
