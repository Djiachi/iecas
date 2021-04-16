package com.tansun.ider.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.service.HttpQueryService;

@Service
public class OperationModeUtil {

	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private HttpQueryService httpQueryService;
	
	/**
	 * 
	* @Description: 
	* @param operationMode
	* @return
	* @throws Exception
	 */
	public  CoreSystemUnit getcoreOperationMode(String customerNo) throws Exception{
		if (StringUtil.isNotBlank(customerNo)) {
			
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//		CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new CoreSystemUnitSqlBuilder();
//		coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo(coreCustomer.getSystemUnitNo());
//		CoreSystemUnit coreSystemUnit = coreSystemUnitDaoImpl.selectBySqlBuilder(coreSystemUnitSqlBuilder);
		CoreSystemUnit coreSystemUnit =httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		
		return coreSystemUnit;
	}
	
}
