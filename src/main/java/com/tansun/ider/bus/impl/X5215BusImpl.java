package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5215Bus;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5215BO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 客户已有费用项目查询
 * 
 * @author admin
 *
 */
@Service
public class X5215BusImpl implements X5215Bus {

	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
	@Autowired
	private HttpQueryService httpQueryService;
	
	
	@Override
	public Object busExecute(X5215BO x5215bo) throws Exception {

		String idNumber = x5215bo.getIdNumber();
		String idType = x5215bo.getIdType();
		String externalIdentificationNo = x5215bo.getExternalIdentificationNo();
		String customerNo = "";
		// 查询客户号
		if (StringUtil.isNotBlank(idType) || StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null == coreCustomer) {
				throw new BusinessException("COR-10048");
			}
			customerNo = coreCustomer.getCustomerNo();
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("COR-10048");
			}
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
		} else {
			throw new BusinessException("COR-10048");
		}
		
		PageBean<CoreFeeItem> page = new PageBean<>();
		
		CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
		coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerWaiveFeeInfoSqlBuilder.andPeriodicFeeIdentifierEqualTo("C");
		int totalCount =  coreCustomerWaiveFeeInfoDao
				.countBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5215bo.getPageSize() && null != x5215bo.getIndexNo()) {
			coreCustomerWaiveFeeInfoSqlBuilder.setPageSize(x5215bo.getPageSize());
			coreCustomerWaiveFeeInfoSqlBuilder.setIndexNo(x5215bo.getIndexNo());
			page.setPageSize(x5215bo.getPageSize());
			page.setIndexNo(x5215bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreFeeItem> obj = new ArrayList<CoreFeeItem>();
			 List<CoreCustomerWaiveFeeInfo> coreCustomerWaiveFeeInfoList  = coreCustomerWaiveFeeInfoDao
					.selectListBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
			 if (null != coreCustomerWaiveFeeInfoList && !coreCustomerWaiveFeeInfoList.isEmpty()) {
				for (CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo : coreCustomerWaiveFeeInfoList) {
					String feeItemNo = coreCustomerWaiveFeeInfo.getFeeItemNo();
					CoreFeeItem coreFeeItem1 = httpQueryService.queryCoreFeeItem(feeItemNo);
					obj.add(coreFeeItem1);
				}
			 }
			 page.setRows(obj);
		}
		return page;
	}

}
