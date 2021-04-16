package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5325Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.impl.CoreCustomerBillDayDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5325BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户业务项目修改
 * @author: admin
 */
@Service
public class X5325BusImpl implements X5325Bus {

	@Autowired
	private CoreCustomerBillDayDaoImpl coreCustomerBillDayDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;

	@Override
	public Object busExecute(X5325BO x5325bo) throws Exception {
	    EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5325bo, eventCommAreaNonFinance);
		String customerNo = x5325bo.getCustomerNo();
		String businessProgramNo = x5325bo.getBusinessProgramNo();
		// 公共区
		CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
		coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
		CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDaoImpl
				.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
		CoreCustomerBillDay coreCustomerBillDayAfter = new CoreCustomerBillDay();
		if (StringUtil.isNotBlank(x5325bo.getDirectDebitAccountNo())) {
			coreCustomerBillDay.setDirectDebitAccountNo(x5325bo.getDirectDebitAccountNo());
		}else {
			coreCustomerBillDay.setDirectDebitAccountNo("");
		}
		coreCustomerBillDay.setDirectDebitStatus(x5325bo.getDirectDebitStatus());
		coreCustomerBillDay.setDirectDebitMode(x5325bo.getDirectDebitMode());
		if (StringUtil.isNotBlank(x5325bo.getDirectDebitBankNo())) {
			coreCustomerBillDay.setDirectDebitBankNo(x5325bo.getDirectDebitBankNo());
		}else {
			coreCustomerBillDay.setDirectDebitBankNo("");
		}
		coreCustomerBillDay.setExchangePaymentFlag(x5325bo.getExchangePaymentFlag());
		CachedBeanCopy.copyProperties(coreCustomerBillDay, coreCustomerBillDayAfter);
		coreCustomerBillDay.setVersion(coreCustomerBillDay.getVersion()+1);
		int result = coreCustomerBillDayDaoImpl.updateBySqlBuilderSelective(coreCustomerBillDay,
				coreCustomerBillDaySqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "客户业务项目表");
		}
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreCustomerBillDay.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5325bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5325bo.getEventNo(),x5325bo.getActivityNo(),ModificationType.UPD.getValue(), null,
				coreCustomerBillDayAfter,coreCustomerBillDay,coreCustomerBillDay.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
				coreCustomerBillDay.getCustomerNo(),coreCustomerBillDay.getCustomerNo(),null,null);
		return eventCommAreaNonFinance;
	}

}
