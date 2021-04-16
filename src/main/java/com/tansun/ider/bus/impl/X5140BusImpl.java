package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.ider.bus.X5140Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.impl.CoreCustomerRemarksDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerRemarksSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5140BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户备注信息维护
 * @author: admin
 */
@Service
public class X5140BusImpl implements X5140Bus {

	@Autowired
	private CoreCustomerRemarksDaoImpl coreCustomerRemarksDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Object busExecute(X5140BO x5140bo) throws Exception {
		String id = x5140bo.getId();
		String customerNo = x5140bo.getCustomerNo();
		CoreCustomerRemarksSqlBuilder coreCustomerRemarksSqlBuilder = new CoreCustomerRemarksSqlBuilder();
		if (null != id && "".equals(id)) {
			coreCustomerRemarksSqlBuilder.andIdEqualTo(id);
		}
		if (null != customerNo && "".equals(customerNo)) {
			coreCustomerRemarksSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		CoreCustomerRemarks coreCustomerRemarks = coreCustomerRemarksDaoImpl
				.selectBySqlBuilder(coreCustomerRemarksSqlBuilder);
		CoreCustomerRemarks coreCustomerRemarksAfter = new CoreCustomerRemarks();
		CachedBeanCopy.copyProperties(coreCustomerRemarks, x5140bo);
		CachedBeanCopy.copyProperties(coreCustomerRemarks, coreCustomerRemarksAfter);
		coreCustomerRemarks.setVersion(coreCustomerRemarks.getVersion() + 1);
		int result = coreCustomerRemarksDaoImpl.updateBySqlBuilderSelective(coreCustomerRemarks,
				coreCustomerRemarksSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "客户备注信息维护");
		}
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreCustomerRemarks.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5140bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5140bo.getEventNo(),x5140bo.getActivityNo(),ModificationType.UPD.getValue(), null,
				coreCustomerRemarksAfter,coreCustomerRemarks,coreCustomerRemarks.getId(),coreSystemUnit.getCurrLogFlag(),
				operatorId,coreCustomerRemarks.getCustomerNo(),coreCustomerRemarks.getCustomerNo(),null,null);
		return result;
	}

}
