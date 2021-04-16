package com.tansun.ider.bus.impl;

import java.util.Date;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5350Bus;
import com.tansun.ider.dao.issue.CoreMediaLifeCycleInfoDao;
import com.tansun.ider.dao.issue.entity.CoreMediaLifeCycleInfo;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5350BO;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 生命周期表信息创建
 * @author: admin
 */
@Service
public class X5350BusImpl implements X5350Bus {

	@Autowired
	private CoreMediaLifeCycleInfoDao coreMediaLifeCycleInfoDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	
	@Override
	public Object busExecute(X5350BO x5350bo) throws Exception {
		EventCommArea eventCommArea = new EventCommArea();
		CachedBeanCopy.copyProperties(x5350bo, eventCommArea);
		String mediaUnitCode = x5350bo.getMediaUnitCode();
		String transferMediaCode = x5350bo.getTransferMediaCode();
		String operatorId  =  x5350bo.getOperatorId();
		CoreMediaLifeCycleInfo coreMediaLifeCycleInfo = new CoreMediaLifeCycleInfo();
		coreMediaLifeCycleInfo.setId(RandomUtil.getUUID());
		coreMediaLifeCycleInfo.setLifecycleDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
		coreMediaLifeCycleInfo.setLifecycleTime(DateUtil.format(new Date(), "HH:mm:ss"));
		coreMediaLifeCycleInfo.setMediaUnitCode(mediaUnitCode);
		coreMediaLifeCycleInfo.setStageType("T");
		coreMediaLifeCycleInfo.setTransferMediaCode(transferMediaCode);
		coreMediaLifeCycleInfo.setVersion(1);
		coreMediaLifeCycleInfoDao.insert(coreMediaLifeCycleInfo);
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5350bo.getEventNo(), x5350bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, null, coreMediaLifeCycleInfo, coreMediaLifeCycleInfo.getId(),
				x5350bo.getCurrLogFlag(), operatorId, x5350bo.getCustomerNo(),
				coreMediaLifeCycleInfo.getMediaUnitCode(), null,null);
		
		x5350bo.setTransferMediaCode(transferMediaCode);
		CachedBeanCopy.copyProperties(x5350bo, eventCommArea);
		return x5350bo;
	}

}
