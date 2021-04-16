package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5590Bus;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5590BO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 查询客户机构运营模式下，产品
 * 
 * @author admin
 *
 */
@Service
public class X5590BusImpl implements X5590Bus {

	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@SuppressWarnings("null")
	@Override
	public Object busExecute(X5590BO x5590Bo) throws Exception {
		PageBean<CoreProductObject> page = new PageBean<>();
		String customerNo = x5590Bo.getCustomerNo();
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if (null == coreCustomer) {
			throw new BusinessException("CUS-00014", "客户基本信息");// 客户信息表
		}
		String institutionId = coreCustomer.getInstitutionId();// 所属机构
		// 根据机构号获取法人编号
		CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);
		if (null == coreOrgan) {
			throw new BusinessException("CUS-00014", "机构表");// 机构表
		}
		List<CoreProductObject> list = new ArrayList<CoreProductObject>();
		// 获取所属法人实体
		String corporationEntityNo = coreOrgan.getCorporationEntityNo();
		List<CoreOperationMode> coreOperationModeList = httpQueryService.queryOperationModeList(corporationEntityNo);
		if (null != coreOperationModeList || !coreOperationModeList.isEmpty()) {
			for (CoreOperationMode coreOperationMode : coreOperationModeList) {
				List<CoreProductObject> coreProductObjectList = httpQueryService
						.queryProductObjectList(coreOperationMode.getOperationMode());
				if (null != coreProductObjectList && !coreProductObjectList.isEmpty()) {
					list.addAll(coreProductObjectList);
				}
			}
		}
		int totalCount = list.size();
		page.setTotalCount(totalCount);
		if (null != x5590Bo.getPageSize() && null != x5590Bo.getIndexNo()) {
			page.setPageSize(x5590Bo.getPageSize());
			page.setIndexNo(x5590Bo.getIndexNo());
		}
		if (totalCount > 0) {
			page.setRows(list);
		}
		return page;
	}

}
