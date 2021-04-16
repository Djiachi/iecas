package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5130Bus;
import com.tansun.ider.dao.issue.entity.CoreMediaLifeCycleInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaLifeCycleInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLifeCycleInfoSqlBuilder;
import com.tansun.ider.enums.SortType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5130BO;

/**
 * @version:1.0
 * @Description: 媒介生命周期查询
 * @author: admin
 */
@Service
public class X5130BusImpl implements X5130Bus {

	@Autowired
	private CoreMediaLifeCycleInfoDaoImpl coreMediaLifeCycleInformationDaoImpl;

	@Override
	public Object busExecute(X5130BO x5130bo) throws Exception {

		if (null == x5130bo.getMediaUnitCode()) {
			throw new BusinessException("CUS-00013", "媒介单元代码");
		}
		CoreMediaLifeCycleInfoSqlBuilder coreMediaLifeCycleInformationSqlBuilder = new CoreMediaLifeCycleInfoSqlBuilder();
		if (null != x5130bo.getFlag() || SortType.D.getDesc().equals(x5130bo.getFlag())) {
			coreMediaLifeCycleInformationSqlBuilder.orderByLifecycleDate(true);
			coreMediaLifeCycleInformationSqlBuilder.orderByLifecycleTime(true);
		}

		coreMediaLifeCycleInformationSqlBuilder.andMediaUnitCodeEqualTo(x5130bo.getMediaUnitCode());
		List<CoreMediaLifeCycleInfo> coreMediaLifeCycleInformationLists = coreMediaLifeCycleInformationDaoImpl
				.selectListBySqlBuilder(coreMediaLifeCycleInformationSqlBuilder);

		return coreMediaLifeCycleInformationLists;
	}

}
