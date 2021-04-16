package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.ider.bus.X5150Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerPersonInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerPersonInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerPersonInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5150BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户个人信息维护
 * @author: admin
 */
@Service
public class X5150BusImpl implements X5150Bus {

	@Autowired
	private CoreCustomerPersonInfoDaoImpl coreCustomerPersonInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Object busExecute(X5150BO x5150bo) throws Exception {
		String id = x5150bo.getId();
		String customerNo = x5150bo.getCustomerNo();
		CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilder = new CoreCustomerPersonInfoSqlBuilder();

		if (null != id && "".equals(id)) {
			coreCustomerPersonInfoSqlBuilder.andIdEqualTo(id);
		}
		if (null != customerNo && "".equals(customerNo)) {
			coreCustomerPersonInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		CoreCustomerPersonInfo coreCustomerPersonInfo = coreCustomerPersonInfoDaoImpl
				.selectBySqlBuilder(coreCustomerPersonInfoSqlBuilder);
		CoreCustomerPersonInfo coreCustomerPersonInfoAfter = new CoreCustomerPersonInfo();
		CachedBeanCopy.copyProperties(coreCustomerPersonInfo, x5150bo);
		CachedBeanCopy.copyProperties(coreCustomerPersonInfo, coreCustomerPersonInfoAfter);
		coreCustomerPersonInfo.setVersion(coreCustomerPersonInfo.getVersion() + 1);
		
		int result = coreCustomerPersonInfoDaoImpl.updateBySqlBuilderSelective(coreCustomerPersonInfo,
				coreCustomerPersonInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", " 客户个人信息");
		}
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreCustomerPersonInfo.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5150bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5150bo.getEventNo(),x5150bo.getActivityNo(),ModificationType.UPD.getValue(), null,
				coreCustomerPersonInfoAfter,coreCustomerPersonInfo,coreCustomerPersonInfo.getId(),coreSystemUnit.getCurrLogFlag(),
				operatorId,coreCustomerPersonInfo.getCustomerNo(),coreCustomerPersonInfo.getCustomerNo(),null,null);
		return "ok";
	}

}
