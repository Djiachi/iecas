package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5100Bus;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5100BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 媒介资料维护
 * @author: admin
 */
@Service
public class X5100BusImpl implements X5100Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;

	@Override
	public Object busExecute(X5100BO x5100bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5100bo, eventCommAreaNonFinance);
		String id = eventCommAreaNonFinance.getId();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andIdEqualTo(id);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
		coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
		int result = coreMediaBasicInfoDaoImpl.updateBySqlBuilder(coreMediaBasicInfo, coreMediaBasicInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "");
		}

		return coreMediaBasicInfo;
	}

}
