package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5165Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5165BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 媒介基本信息维护
 * @author: admin
 */
@Service
public class X5165BusImpl implements X5165Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;

	@Override
	public Object busExecute(X5165BO x5165bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5165bo, eventCommAreaNonFinance);
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		if (null != mediaUnitCode && !"".equals(mediaUnitCode)) {
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		}
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(x5165bo.getMainCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5165bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		CoreMediaBasicInfo coreMediaBasicInfoAfter = new CoreMediaBasicInfo();
		eventCommAreaNonFinance.setId(coreMediaBasicInfo.getId());
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, coreMediaBasicInfoAfter);
		CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance);
		coreMediaBasicInfo.setVersion(coreMediaBasicInfo.getVersion() + 1);
		coreMediaBasicInfo.setInvalidFlag("Y");
		int result = coreMediaBasicInfoDaoImpl.updateBySqlBuilder(coreMediaBasicInfo, coreMediaBasicInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介基本信息");
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5165bo.getEventNo(),x5165bo.getActivityNo(),ModificationType.UPD.getValue(), null,
				coreMediaBasicInfoAfter,coreMediaBasicInfo,coreMediaBasicInfo.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
				coreMediaBasicInfo.getMainCustomerNo(),coreMediaBasicInfo.getMediaUnitCode(),null,null);
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		if (null != coreMediaCardInfo) {
			CoreMediaCardInfo coreMediaCardInfoAfter = new CoreMediaCardInfo();
			CachedBeanCopy.copyProperties(coreMediaCardInfo, eventCommAreaNonFinance);
			CachedBeanCopy.copyProperties(coreMediaCardInfo, coreMediaCardInfoAfter);
			coreMediaCardInfo.setVersion(coreMediaCardInfo.getVersion() + 1);
			int i = coreMediaCardInfoDaoImpl.updateBySqlBuilderSelective(coreMediaCardInfo, coreMediaCardInfoSqlBuilder);
			if (i != 1) {
				throw new BusinessException("CUS-00012", "媒介制卡信息");
			}
			nonFinancialLogUtil.createNonFinancialActivityLog(x5165bo.getEventNo(),x5165bo.getActivityNo(),ModificationType.UPD.getValue(), null,
					coreMediaCardInfoAfter,coreMediaCardInfo,coreMediaCardInfo.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
					x5165bo.getMainCustomerNo(),coreMediaCardInfo.getMediaUnitCode(),null,null);
		}
		
		return "ok";
	}

}
