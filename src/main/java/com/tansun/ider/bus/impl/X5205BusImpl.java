package com.tansun.ider.bus.impl;

import com.tansun.ider.bus.X5205Bus;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.impl.CoreMediaBindDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5205BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version:1.0
 * @Description: 媒介绑定信息修改
 * @author: admin
 */
@Service
public class X5205BusImpl implements X5205Bus {

	@Autowired
	private CoreMediaBindDaoImpl coreMediaBindDaoImpl;

	@Override
	public Object busExecute(X5205BO x5205bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5205bo, eventCommAreaNonFinance);
		String mediaObjectCode = eventCommAreaNonFinance.getMediaObjectCode();
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
		String id = eventCommAreaNonFinance.getId();
		CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
		coreMediaBindSqlBuilder.andIdEqualTo(id);
		coreMediaBindSqlBuilder.andMediaObjectCodeEqualTo(mediaObjectCode);
		coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaBind coreMediaBind = coreMediaBindDaoImpl.selectBySqlBuilder(coreMediaBindSqlBuilder);
		CachedBeanCopy.copyPropertieSwap(coreMediaBind, eventCommAreaNonFinance);
		int result = coreMediaBindDaoImpl.updateBySqlBuilderSelective(coreMediaBind, coreMediaBindSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介绑定信息表");
		}

		return "ok";
	}

}
