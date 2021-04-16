package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5060Bus;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaLabelInfoDaoImpl;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5060BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 媒介标签信息新建
 * @author: admin
 */
@Service
public class X5060BusImpl implements X5060Bus {

	@Autowired
	private CoreMediaLabelInfoDaoImpl coreMediaLabelInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	
	@Override
	public Object busExecute(X5060BO x5060BO) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5060BO, eventCommAreaNonFinance);
		String invalidReason = eventCommAreaNonFinance.getInvalidReason();
		String operatorId = x5060BO.getOperatorId();
		// 新发卡
		if (operatorId == null) {
			operatorId = "system";
		}
		List<CoreMediaLabelInfo> listCoreMediaLabelInfos = eventCommAreaNonFinance.getCoreMediaLabelInfos();
		if (StringUtil.isBlank(invalidReason)) {
			// 媒介标签信息新建
			if (null != listCoreMediaLabelInfos) {
				for (CoreMediaLabelInfo coreMediaLabelInfo : listCoreMediaLabelInfos) {
					coreMediaLabelInfo.setId(RandomUtil.getUUID());
					coreMediaLabelInfo.setMediaUnitCode(eventCommAreaNonFinance.getMediaUnitCode());
					coreMediaLabelInfo.setVersion(1);
				}
				@SuppressWarnings("unused")
				int result = coreMediaLabelInfoDaoImpl.insertUseBatch(listCoreMediaLabelInfos);
			}
		} else if (InvalidReasonStatus.TRF.getValue().equals(invalidReason)
				|| InvalidReasonStatus.BRK.getValue().equals(invalidReason)
				|| InvalidReasonStatus.EXP.getValue().equals(invalidReason)
				|| InvalidReasonStatus.PNA.getValue().equals(invalidReason)
				|| InvalidReasonStatus.CHP.getValue().equals(invalidReason)) {
			if (null != listCoreMediaLabelInfos && !listCoreMediaLabelInfos.isEmpty()) {
				for (CoreMediaLabelInfo coreMediaLabelInfo : listCoreMediaLabelInfos) {
					coreMediaLabelInfo.setMediaUnitCode(eventCommAreaNonFinance.getMediaUnitCode());
					coreMediaLabelInfo.setId(RandomUtil.getUUID());
					coreMediaLabelInfo.setVersion(1);
				}
				@SuppressWarnings("unused")
				int result = coreMediaLabelInfoDaoImpl.insertUseBatch(listCoreMediaLabelInfos);
			}
		}
		
		if (null != listCoreMediaLabelInfos && !listCoreMediaLabelInfos.isEmpty()) {
			for (CoreMediaLabelInfo coreMediaLabelInfo : listCoreMediaLabelInfos) {
				nonFinancialLogUtil.createNonFinancialActivityLog(x5060BO.getEventNo(), x5060BO.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreMediaLabelInfo, coreMediaLabelInfo.getId(),
						x5060BO.getCurrLogFlag(), operatorId, x5060BO.getCustomerNo(),
						coreMediaLabelInfo.getMediaUnitCode(), null,null);
			}
		}
		
		// 返回标签信息
		return eventCommAreaNonFinance;
	}
	
}
