package com.tansun.ider.bus.impl;

import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5145Bus;
import com.tansun.ider.model.bo.X5145BO;

/**
 * @version:1.0
 * @Description: 产品线账单日维护
 * @author: admin
 */
@Service
public class X5145BusImpl implements X5145Bus {

//	@Resource
//	private CoreProductLineBillDayDaoImpl coreProductLineBillDayDaoImpl;
//	@Autowired
//	private ParamServiceImpl paramServiceImpl ;
//	@Autowired
//	private CoreCustomerDao coreCustomerDao;
//	@Autowired
//	private NonFinancialLogUtil nonFinancialLogUtil;
//	@Autowired
//	private ParamServiceImpl paramServiceImpl ;
//	@Autowired
//	private CoreCustomerDao coreCustomerDao;

	@Override
	public Object busExecute(X5145BO x5145bo) throws Exception {
		String id = x5145bo.getId();
		String customerNo = x5145bo.getCustomerNo();
//		CoreProductLineBillDaySqlBuilder coreProductLineBillDaySqlBuilder = new CoreProductLineBillDaySqlBuilder();
		if (null != id || "".equals(id)) {
//			coreProductLineBillDaySqlBuilder.andIdEqualTo(id);
		}
		if (null != customerNo || "".equals(customerNo)) {
//			coreProductLineBillDaySqlBuilder.andIdEqualTo(customerNo);
		}
 //		CoreProductLineBillDay coreProductLineBillDay = coreProductLineBillDayDaoImpl
//				.selectBySqlBuilder(coreProductLineBillDaySqlBuilder);
//		CoreProductLineBillDay coreProductLineBillDayAfter = new CoreProductLineBillDay();
//		CachedBeanCopy.copyProperties(coreProductLineBillDay, x5145bo);
//		CachedBeanCopy.copyProperties(coreProductLineBillDayAfter, coreProductLineBillDay);
//		coreProductLineBillDay.setVersion(coreProductLineBillDay.getVersion() + 1);
//		int result = coreProductLineBillDayDaoImpl.updateBySqlBuilderSelective(coreProductLineBillDay,
//				coreProductLineBillDaySqlBuilder);
//		if (result != 1) {
//			throw new BusinessException("CUS-00012", "产品线账单日信息维护");
//		}
		//获取当前日志标识
//		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreProductLineBillDay.getCustomerNo());
//		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
//		CoreSystemUnit coreSystemUnit = paramServiceImpl.querySystemUnit(coreCustomer.getSystemUnitNo());
//		String operatorId = x5145bo.getOperatorId();
//		if (operatorId == null) {
//			operatorId = "system";
//		}
//		nonFinancialLogUtil.createNonFinancialActivityLog(x5145bo.getEventNo(),x5145bo.getActivityNo(),ModificationType.UPD.getValue(), null,
//				coreProductLineBillDayAfter,coreProductLineBillDay,coreProductLineBillDay.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
//				coreProductLineBillDay.getCustomerNo(),coreProductLineBillDay.getCustomerNo(),null);
		return "";
	}

}
