package com.tansun.ider.bus.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5090Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5090BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户资料维护
 * @author: admin
 */
@Service
public class X5090BusImpl implements X5090Bus {

	@Autowired
	private CoreCustomerAddrDao coreCustomerAddrDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	
	@Override
	public Object busExecute(X5090BO x5090bo) throws Exception {
		String city = x5090bo.getCity();
		String contactPostCode = x5090bo.getContactPostCode();
		String contactMobilePhone = x5090bo.getContactMobilePhone();
		String contactAddress = x5090bo.getContactAddress();
	    //EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		//CachedBeanCopy.copyProperties(x5090bo, eventCommAreaNonFinance);
		CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
		coreCustomerAddrSqlBuilder.andIdEqualTo(x5090bo.getId());
		CoreCustomerAddr coreCustomerAddr = coreCustomerAddrDao.selectBySqlBuilder(coreCustomerAddrSqlBuilder);
		if(coreCustomerAddr==null){
		    throw new BusinessException("CUS-00014", "客户地址信息！"); 
		}
		if(StringUtil.isNotBlank(city)&&!city.equals(coreCustomerAddr.getCity())){
		    coreCustomerAddr.setCity(city); 
		}
		if(StringUtil.isNotBlank(contactPostCode)&&!contactPostCode.equals(coreCustomerAddr.getContactPostCode())){
		    coreCustomerAddr.setContactPostCode(contactPostCode);
		}
		if(StringUtil.isNotBlank(contactMobilePhone)&&!contactMobilePhone.equals(coreCustomerAddr.getContactMobilePhone())){
		    coreCustomerAddr.setContactMobilePhone(contactMobilePhone);
		}
		if(StringUtil.isNotBlank(contactAddress)&&!contactAddress.equals(coreCustomerAddr.getContactAddress())){
		    coreCustomerAddr.setContactAddress(contactAddress);
		}
		//CoreCustomerAddr coreCustomerAddrAfter = new CoreCustomerAddr();
		//CachedBeanCopy.copyProperties(coreCustomerAddr, coreCustomerAddrAfter);
		//CachedBeanCopy.copyProperties(coreCustomerAddr, eventCommAreaNonFinance);
		coreCustomerAddr.setVersion(coreCustomerAddr.getVersion() + 1);
		coreCustomerAddrDao.updateByPrimaryKey(coreCustomerAddr);
		int result = coreCustomerAddrDao.updateBySqlBuilderSelective(coreCustomerAddr, coreCustomerAddrSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "客户地址信息！");
		}
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreCustomerAddr.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5090bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		nonFinancialLogUtil.createNonFinancialActivityLog(x5090bo.getEventNo(), x5090bo.getActivityNo(), ModificationType.UPD.getValue(), null, coreCustomerAddr,
				coreCustomerAddr, coreCustomerAddr.getId(), coreSystemUnit.getCurrLogFlag(),operatorId , coreCustomerAddr.getCustomerNo(), coreCustomerAddr.getType().toString(), null,null);
		return "OK";
	}

}
