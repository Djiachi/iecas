package com.tansun.ider.bus.impl;

import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5230Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5230BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 客户媒介列表查询
 * @author: admin
 */
@Service
public class X5230BusImpl implements X5230Bus {

	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	
	@Override
	public Object busExecute(X5230BO x5230bo) throws Exception {
		// 外部识别号
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(x5230bo, eventCommAreaNonFinance);
		String externalIdentificationNo = x5230bo.getExternalIdentificationNo();
		String idType = x5230bo.getIdType();
		// 身份证号
		String idNumber = x5230bo.getIdNumber();
		
		if (StringUtil.isBlank(idType) && StringUtil.isBlank(idNumber) &&
				StringUtil.isBlank(externalIdentificationNo)) {
			throw new BusinessException("COR-10048");
		}
		String customerNo = null;
		if (StringUtil.isNotBlank(idNumber) && StringUtil.isNotBlank(idType)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (coreCustomer != null) {
				customerNo = coreCustomer.getCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		if (StringUtil.isNotBlank(customerNo)) {
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
		}
		coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		List<CoreMediaBasicInfo> listcoreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		eventCommAreaNonFinance.setListcoreMediaBasicInfo(listcoreMediaBasicInfo);
		eventCommAreaNonFinance.setCustomerNo(customerNo);
		return eventCommAreaNonFinance;
	}

}
