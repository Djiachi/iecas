package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5225Bus;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5225BO;

/**
 * 客户证件号码，证件类型查询
 * @author admin
 *
 */
@Service
public class X5225BusImpl implements X5225Bus{
	
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	
	@Override
	public Object busExecute(X5225BO x5225bo) throws Exception {
		
		String idNumber = x5225bo.getIdNumber();
		String idType = x5225bo.getIdType();
		String externalIdentificationNo = x5225bo.getExternalIdentificationNo();
		
		PageBean<CoreCustomer> page = new PageBean<>();
		String customerNo = null;
		//查询客户号
		if (StringUtil.isNotBlank(idType) || StringUtil.isNotBlank(idNumber) ) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			int totalCount = coreCustomerDao.countBySqlBuilder(coreCustomerSqlBuilder);
			page.setTotalCount(totalCount);
			if (null != x5225bo.getPageSize() && null != x5225bo.getIndexNo()) {
				coreCustomerSqlBuilder.setPageSize(x5225bo.getPageSize());
				coreCustomerSqlBuilder.setIndexNo(x5225bo.getIndexNo());
				page.setPageSize(x5225bo.getPageSize());
				page.setIndexNo(x5225bo.getIndexNo());
			}
			if (totalCount > 0) {
				List<CoreCustomer> list = coreCustomerDao.selectListBySqlBuilder(coreCustomerSqlBuilder);
				page.setRows(list);
			}
			return page;
		}else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			List<CoreMediaBasicInfo> coreMediaBasicInfoList = 
					coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null != coreMediaBasicInfoList && !coreMediaBasicInfoList.isEmpty()) {
				CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoList.get(0);
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}else {
			throw new BusinessException("COR-10048");
		}
		
		CoreCustomerSqlBuilder coreCustomerSqlBuilder1 = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder1.andCustomerNoEqualTo(customerNo);
		int totalCount = coreCustomerDao.countBySqlBuilder(coreCustomerSqlBuilder1);
		page.setTotalCount(totalCount);
		
		if (null != x5225bo.getPageSize() && null != x5225bo.getIndexNo()) {
			coreCustomerSqlBuilder1.setPageSize(x5225bo.getPageSize());
			coreCustomerSqlBuilder1.setIndexNo(x5225bo.getIndexNo());
			page.setPageSize(x5225bo.getPageSize());
			page.setIndexNo(x5225bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreCustomer> list = coreCustomerDao.selectListBySqlBuilder(coreCustomerSqlBuilder1);
			page.setRows(list);
		}
		return page;
	}
	
}
