package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5240Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5240BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 媒介制卡信息查询
 * @author: admin
 */
@Service
public class X5240BusImpl implements X5240Bus {

	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;;

	@Autowired
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;

	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;

	@Override
	public Object busExecute(X5240BO x5240bo) throws Exception {

		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(x5240bo, eventCommAreaNonFinance);
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		PageBean<CoreMediaCardInfo> page = new PageBean<>();

		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();

		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			// 根据外部识别号查询，媒介信息
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
			coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (coreMediaBasicInfo != null) {
				coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
			}
		}
		if (StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (coreCustomer != null) {
				CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
				coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
				if (StringUtil.isNotBlank(externalIdentificationNo)) {
					coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
				}
				coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
				coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
				if (coreMediaBasicInfo != null) {
					coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
				}
			}
		}
		int totalCount = coreMediaCardInfoDaoImpl.countBySqlBuilder(coreMediaCardInfoSqlBuilder);
		page.setTotalCount(totalCount);

		if (null != x5240bo.getPageSize() && null != x5240bo.getIndexNo()) {
			coreMediaCardInfoSqlBuilder.orderByMediaUnitCode(false);
			coreMediaCardInfoSqlBuilder.setPageSize(x5240bo.getPageSize());
			coreMediaCardInfoSqlBuilder.setIndexNo(x5240bo.getIndexNo());
			page.setPageSize(x5240bo.getPageSize());
			page.setIndexNo(x5240bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreMediaCardInfo> list = coreMediaCardInfoDaoImpl.selectListBySqlBuilder(coreMediaCardInfoSqlBuilder);
			page.setRows(list);
		}

		return page;
	}

}
