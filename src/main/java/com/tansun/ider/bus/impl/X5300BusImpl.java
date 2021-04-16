package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5300Bus;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5300BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;

/**
 * @version:1.0
 * @Description: 更新媒介制卡信息
 * @author: admin
 */
@Service
public class X5300BusImpl implements X5300Bus {

	@Autowired
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;

	@Override
	public Object busExecute(X5300BO x5300bo) throws Exception {
		// 公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		if (StringUtil.isNotBlank(x5300bo.getMakeFlag()) && Constant.NOTHANDLE.equals(x5300bo.getMakeFlag())) {
			return x5300bo;
		}
		//将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5300bo, eventCommAreaNonFinance);
		// 媒介单元代码
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();

		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		if (null == coreMediaCardInfo) {
			throw new BusinessException("CUS-00015", "媒介制卡");
		}
		coreMediaCardInfo.setPreviousProductionCode(coreMediaCardInfo.getProductionCode());
		coreMediaCardInfo.setProductionCode("0");
		coreMediaCardInfo.setCvv(eventCommAreaNonFinance.getCvv());
		coreMediaCardInfo.setCvv2(eventCommAreaNonFinance.getCvv2());
		coreMediaCardInfo.setIcvv(eventCommAreaNonFinance.getIcvv());
		coreMediaCardInfo.setVersion(coreMediaCardInfo.getVersion() + 1);
		int result = coreMediaCardInfoDaoImpl.updateBySqlBuilderSelective(coreMediaCardInfo,
				coreMediaCardInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介制卡");
		}
		
		return eventCommAreaNonFinance;
	}

}
