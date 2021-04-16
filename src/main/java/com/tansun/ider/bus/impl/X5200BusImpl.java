package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5200Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBindDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5200BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 媒介绑定信息查询
 * @author: admin
 */
@Service
public class X5200BusImpl implements X5200Bus {

	@Resource
	private CoreMediaBindDaoImpl coreMediaBindDaoImpl;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;

	@Override
	public Object busExecute(X5200BO x5200bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5200bo, eventCommAreaNonFinance);
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 媒介单元代码
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
		PageBean<CoreMediaBind> page = new PageBean<>();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
			}
		}
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		//因为客户媒介视图支持无效卡查询，所以媒介绑定信息也需支持无效卡查询，所以注释掉这行代码   add by wangxi 2019/8/19   cyy提
//		coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");    
		if (StringUtil.isNotBlank(mediaUnitCode)) {
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		}
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
		coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());

		int totalCount = coreMediaBindDaoImpl.countBySqlBuilder(coreMediaBindSqlBuilder);
		page.setTotalCount(totalCount);

		if (null != x5200bo.getPageSize() && null != x5200bo.getIndexNo()) {
			coreMediaBindSqlBuilder.orderByMediaUnitCode(false);
			coreMediaBindSqlBuilder.setPageSize(x5200bo.getPageSize());
			coreMediaBindSqlBuilder.setIndexNo(x5200bo.getIndexNo());
			page.setPageSize(x5200bo.getPageSize());
			page.setIndexNo(x5200bo.getIndexNo());
		}

		if (totalCount > 0) {
			List<CoreMediaBind> list = coreMediaBindDaoImpl.selectListBySqlBuilder(coreMediaBindSqlBuilder);
			page.setRows(list);
		}

		return page;
	}

}
