package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5330Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreExcptTransInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreExcptTransInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreExcptTransInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5330BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 异常交易查询
 * 
 * @author yanzhaofei 2018年10月15日
 */
@Service
public class X5330BusImpl implements X5330Bus {

	@Autowired
	private CoreExcptTransInfoDao coreExcptTransInfoDao;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private ParamsUtil paramsUtil;
	@Override
	public Object busExecute(X5330BO x5330bo) throws Exception {
		String idNumber = x5330bo.getIdNumber();
		String externalIdentificationNo = x5330bo.getExternalIdentificationNo();
		String startDate = x5330bo.getStartDate();
		String endDate = x5330bo.getEndDate();
		String transBillingState = x5330bo.getTransBillingState();
		String failureReason = x5330bo.getFailureReason();
		String customerNo = "";

		if (StringUtil.isNotBlank(idNumber)) {
			// 通过证件号来获取客户号
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (coreCustomer != null) {
				customerNo = coreCustomer.getCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			// 通过外部识别号来获取客户号
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(Constant.MEDIA_INVALID_FLAG);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (coreMediaBasicInfo != null) {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "媒介单元基本");
			}
		}

		if (StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)) {
			if (endDate.compareTo(startDate) < 0) {
				throw new Exception("交易起始日期不能大于交易结束日期");
			}
		}
		String entrys = Constant.EMPTY_LIST;
		PageBean<CoreExcptTransInfo> page = new PageBean<>();
		CoreExcptTransInfoSqlBuilder coreExcptTransInfoSqlBuilder = new CoreExcptTransInfoSqlBuilder();
		if (StringUtil.isNotEmpty(customerNo)) {
			coreExcptTransInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		if (StringUtil.isNotEmpty(externalIdentificationNo)) {
			coreExcptTransInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		if (StringUtil.isNotEmpty(startDate)) {
			coreExcptTransInfoSqlBuilder.andTransDateGreaterThanOrEqualTo(startDate);
		}
		if (StringUtil.isNotEmpty(endDate)) {
			coreExcptTransInfoSqlBuilder.andTransDateLessThanOrEqualTo(endDate);
		}
		if (StringUtil.isNotEmpty(transBillingState)) {
			coreExcptTransInfoSqlBuilder.andTransBillingStateEqualTo(transBillingState);
		}
		if (StringUtil.isNotEmpty(failureReason)) {
			coreExcptTransInfoSqlBuilder.andFailureReasonEqualTo(failureReason);
		}
		// logger.info("failureReason={} ", failureReason);
		int totalCount = coreExcptTransInfoDao.countBySqlBuilder(coreExcptTransInfoSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5330bo.getPageSize() && null != x5330bo.getIndexNo()) {
			coreExcptTransInfoSqlBuilder.orderById(false);
			coreExcptTransInfoSqlBuilder.setPageSize(x5330bo.getPageSize());
			coreExcptTransInfoSqlBuilder.setIndexNo(x5330bo.getIndexNo());
			page.setPageSize(x5330bo.getPageSize());
			page.setIndexNo(x5330bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreExcptTransInfo> list = coreExcptTransInfoDao.selectListBySqlBuilder(coreExcptTransInfoSqlBuilder);
			for (CoreExcptTransInfo tmp : list) {
				amountConversion(tmp);
			}
			page.setRows(list);
			if (null != list && !list.isEmpty()) {
				entrys = list.get(0).getId();
			}
		}
		// 记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5330bo.getCoreEventActivityRel().getEventNo(),
				x5330bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
				x5330bo.getOperatorId());
		return page;
	}

	/**
	 * 金额转换
	 * 
	 * @param coreCustomerWaiveFeeInfo
	 * @param currencyCode
	 * @throws Exception
	 */
	private void amountConversion(CoreExcptTransInfo coreExcptTransInfo) throws Exception {
		//交易币种小数位
		int decimalPlaces = coreExcptTransInfo.getTransCurrDecimal();
		if (coreExcptTransInfo.getTransAmount() != null) {
			BigDecimal transAmount = CurrencyConversionUtil.reduce(coreExcptTransInfo.getTransAmount(),
					decimalPlaces);
			coreExcptTransInfo.setTransAmount(transAmount);
		}
	    /** 实时余额当期本金入账金额 [18,0] */
		if (coreExcptTransInfo.getCurrPrincipalBalance() != null && coreExcptTransInfo.getCurrDeciaml()!=null) {
			//实时余额货币小数位
			decimalPlaces = coreExcptTransInfo.getCurrDeciaml();
			
			BigDecimal currPrincipalBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getCurrPrincipalBalance(),
					decimalPlaces);
			coreExcptTransInfo.setCurrPrincipalBalance(currPrincipalBalance);
		}
	    /** 实时余额账单本金入账金额 [18,0] */
		if (coreExcptTransInfo.getBillPrincipalBalance() != null) {
			BigDecimal billPrincipalBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getBillPrincipalBalance(),
					decimalPlaces);
			coreExcptTransInfo.setBillPrincipalBalance(billPrincipalBalance);
		}
	    /** 实时余额当期利息入账金额 [18,0] */
		if (coreExcptTransInfo.getCurrInterestBalance() != null) {
			BigDecimal currInterestBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getCurrInterestBalance(),
					decimalPlaces);
			coreExcptTransInfo.setCurrInterestBalance(currInterestBalance);
		}
	    /** 实时余额账单利息入账金额  [18,0] */
		if (coreExcptTransInfo.getBillInterestBalance() != null) {
			BigDecimal billInterestBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getBillInterestBalance(),
					decimalPlaces);
			coreExcptTransInfo.setBillInterestBalance(billInterestBalance);
		}
	    /** 实时余额当期费用入账金额 [18,0] */
		if (coreExcptTransInfo.getCurrCostBalance() != null) {
			BigDecimal currCostBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getCurrCostBalance(),
					decimalPlaces);
			coreExcptTransInfo.setCurrCostBalance(currCostBalance);
		}
	    /** 实时余额账单费用入账金额 [18,0] */
		if (coreExcptTransInfo.getBillCostBalance() != null) {
			BigDecimal billCostBalance = CurrencyConversionUtil.reduce(coreExcptTransInfo.getBillCostBalance(),
					decimalPlaces);
			coreExcptTransInfo.setBillCostBalance(billCostBalance);
		}
		if (coreExcptTransInfo.getPostingAmt() != null && coreExcptTransInfo.getCurrDeciaml()!=null) {
			//入账货币小数位
			decimalPlaces = coreExcptTransInfo.getCurrDeciaml();
			
			BigDecimal postingAmt = CurrencyConversionUtil.reduce(coreExcptTransInfo.getPostingAmt(),
					decimalPlaces);
			coreExcptTransInfo.setPostingAmt(postingAmt);
		}
		if (coreExcptTransInfo.getClearAmount() != null && coreExcptTransInfo.getCurrDeciaml()!=null) {
			//清算货币小数位
			decimalPlaces = coreExcptTransInfo.getCurrDeciaml();
			
			BigDecimal clearAmount = CurrencyConversionUtil.reduce(coreExcptTransInfo.getClearAmount(),
					decimalPlaces);
			coreExcptTransInfo.setClearAmount(clearAmount);
		}
	}
}
