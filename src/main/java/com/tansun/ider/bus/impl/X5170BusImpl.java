package com.tansun.ider.bus.impl;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5170Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaLabelInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLabelInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5170BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 媒介标签信息维护
 * @author: admin
 */
@Service
public class X5170BusImpl implements X5170Bus {

	@Resource
	private CoreMediaLabelInfoDaoImpl coreMediaLabelInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	
	@Override
	public Object busExecute(X5170BO x5170bo) throws Exception {
		String mediaUnitCode = x5170bo.getMediaUnitCode();
		CoreMediaLabelInfoSqlBuilder coreMediaLabelInfoSqlBuilder = new CoreMediaLabelInfoSqlBuilder();
		coreMediaLabelInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaLabelInfo coreMediaLabelInfo = coreMediaLabelInfoDaoImpl
				.selectBySqlBuilder(coreMediaLabelInfoSqlBuilder);
		CoreMediaLabelInfo coreMediaLabelInfoAfter = new  CoreMediaLabelInfo();
		CachedBeanCopy.copyProperties(coreMediaLabelInfo, x5170bo);
		CachedBeanCopy.copyProperties(coreMediaLabelInfo, coreMediaLabelInfoAfter);
		coreMediaLabelInfo.setVersion(coreMediaLabelInfo.getVersion() + 1);
		int result = coreMediaLabelInfoDaoImpl.updateBySqlBuilderSelective(coreMediaLabelInfo,
				coreMediaLabelInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介标签信息");
		}
		//获取当前日志标识
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5170bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5170bo.getEventNo(),x5170bo.getActivityNo(),ModificationType.UPD.getValue(), null,
				coreMediaLabelInfoAfter,coreMediaLabelInfo,coreMediaLabelInfo.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
				coreMediaBasicInfo.getMainCustomerNo(),coreMediaLabelInfo.getMediaUnitCode(),null,null);
		
		return "ok";
	}

}
