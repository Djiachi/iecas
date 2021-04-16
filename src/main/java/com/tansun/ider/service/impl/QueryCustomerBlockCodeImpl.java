package com.tansun.ider.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BlockCodeBean;
import com.tansun.ider.model.CustomerContrlViewBean;
import com.tansun.ider.service.QueryCustomerBlockCode;
import com.tansun.ider.util.CachedBeanCopy;

@Service
public class QueryCustomerBlockCodeImpl implements QueryCustomerBlockCode {

	@Autowired
	private CoreCustomerContrlViewDao coreCustomerContrlViewDao;
	@Autowired
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;

	@Override
	public List<CustomerContrlViewBean> queryCoreCustomerContrlView(String customerNo, String controlProjectNo,
			String startDate, String endDate, String dateFormat) throws Exception {
		if ((dateFormat == null) || ("".equals(dateFormat))){
			dateFormat = "yyyy-MM-dd";
		}
		List<CustomerContrlViewBean> listContrlViewBean = new ArrayList<CustomerContrlViewBean>();
		if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(controlProjectNo)
				&& StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
			coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
			coreCustomerContrlViewSqlBuilder.andControlProjectNoEqualTo(controlProjectNo);
			// 按照结束时间排序
			coreCustomerContrlViewSqlBuilder.orderByContrlStartDate(false);
			List<CoreCustomerContrlView> listCoreCustomerContrlView = coreCustomerContrlViewDao
					.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
			if (null != listCoreCustomerContrlView && !listCoreCustomerContrlView.isEmpty()) {
				for (CoreCustomerContrlView coreCustomerContrlView : listCoreCustomerContrlView) {
					CustomerContrlViewBean customerContrlViewBean = new CustomerContrlViewBean();
					CachedBeanCopy.copyProperties(coreCustomerContrlView, customerContrlViewBean);
					String contrlStartDateStr = "";
					String contrlEndDateStr = "";
					String contrlEndDate = coreCustomerContrlView.getContrlEndDate();
					String contrlStartDate = coreCustomerContrlView.getContrlStartDate();
					if ((null == contrlEndDate) || ("".equals(contrlEndDate))) {
						// 1.则将区间结束日期，最为结束日期返回
						// 2.比较区间开始日期,管控开始日期
						double results = DateUtil.daysBetween(startDate, contrlStartDate, dateFormat);
						if (results <= 0) {
							contrlStartDateStr = startDate;
							contrlEndDateStr = contrlEndDate;
							customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
							customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
						} else {
							contrlStartDateStr = contrlStartDate;
							contrlEndDateStr = contrlEndDate;
							customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
							customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
						}
					} else {
						double results1 = DateUtil.daysBetween(startDate, contrlStartDate, dateFormat);
						double results2 = DateUtil.daysBetween(endDate, contrlStartDate, dateFormat);
						double results3 = DateUtil.daysBetween(startDate, contrlEndDate, dateFormat);
						double results4 = DateUtil.daysBetween(endDate, contrlEndDate, dateFormat);
						if (results1 <= 0) {
							if (results3 >= 0) {
								if (results4 >= 0) {
									contrlStartDateStr = startDate;
									contrlEndDateStr = endDate;
									customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
									customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
								} else {
									contrlStartDateStr = startDate;
									contrlEndDateStr = contrlEndDate;
									customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
									customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
								}
							} else {
								continue;
							}
						} else {
							if (results2 >= 0) {
								continue;
							} else {
								if (results4 >= 0) {
									contrlStartDateStr = contrlStartDate;
									contrlEndDateStr = endDate;
									customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
									customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
								} else {
									contrlStartDateStr = contrlStartDate;
									contrlEndDateStr = contrlEndDate;
									customerContrlViewBean.setContrlStartDate(contrlStartDateStr);
									customerContrlViewBean.setContrlEndDate(contrlEndDateStr);
								}
							}
						}
					}
					listContrlViewBean.add(customerContrlViewBean);
				}
			}
		} else {
			throw new BusinessException("CUS-00070");
		}
		return listContrlViewBean;
	}

	/**
	 * 
	 * @param customerNo
	 * @return
	 * @throws Exception
	 */
	public BlockCodeBean queryCustomerBlockCode(String customerNo, String eventNo) throws Exception {
		CoreCustomerEffectiveCode coreCustomerEffectiveCode = null;
		if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(eventNo)) {
			CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
			coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
			coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(eventNo);
			coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo("Y");
			coreCustomerEffectiveCode = coreCustomerEffectiveCodeDao.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
		} else {
			throw new BusinessException("CUS-00069");
		}
		BlockCodeBean blockCodeBean = new BlockCodeBean();
		if (null != coreCustomerEffectiveCode) {
			blockCodeBean.setBlockCodeType(coreCustomerEffectiveCode.getEffectivenessCodeType());
			blockCodeBean.setBlockCodeScene(coreCustomerEffectiveCode.getEffectivenessCodeScene());
		}
		return blockCodeBean;
	}

}
