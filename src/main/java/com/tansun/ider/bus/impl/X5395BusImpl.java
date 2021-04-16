package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5395Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreAccountCycleFiciDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreBusinessTypeBillSumDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductObjectBillSumDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5395BO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 账单查询-交易明细查询
 * 
 * @author huangyayun
 * @date 2018年8月13日
 */
@Service
public class X5395BusImpl implements X5395Bus {
	@Resource
	private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;// 客户统一日期表
	@Resource
	private CoreAccountDao coreAccountDao;// 客户基本账户表
	@Resource
	private CoreCustomerBillDayDao coreCustomerBillDayDao;// 客户业务项目表
	@Resource
	private CoreAccountCycleFiciDao coreAccountCycleFiciDao;// 客户周期余额信息表
	@Resource
	private CoreBalanceUnitDao coreBalanceUnitDao;// 余额单元表
	@Resource
	private CoreProductObjectBillSumDao coreProductObjectBillSumDao;// 产品对象账单摘要表
	@Resource
	private CoreBusinessTypeBillSumDao coreBusinessTypeBillSumDao;// 业务类型账单摘要表
	@Resource
	private CoreCustomerDao coreCustomerDao;// 客户号
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;// 媒介基本信息
	@Resource
	private CoreTransHistDao coreTransHistDao;// 交易历史表
	@Resource
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5395BO x5395Bo) throws Exception {

		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
		coreTransHistSqlBuilder.andActivityNoEqualTo("X8010");
		coreTransHistSqlBuilder.andLogLevelEqualTo("A");
		if (StringUtil.isNotEmpty(x5395Bo.getCurrencyCode())) {
			coreAccountSqlBuilder.andCurrencyCodeEqualTo(x5395Bo.getCurrencyCode());
			coreTransHistSqlBuilder.andCurrencyCodeEqualTo(x5395Bo.getCurrencyCode());
		}
		if (x5395Bo.getCurrentCycleNumber() != null && x5395Bo.getCurrentCycleNumber() >= 0) {
			// coreAccountSqlBuilder.andCurrentCycleNumberEqualTo(x5395Bo.getCurrentCycleNumber());
			coreTransHistSqlBuilder.andCycleNumberEqualTo(x5395Bo.getCurrentCycleNumber());
		}
		if (StringUtil.isNotEmpty(x5395Bo.getBusinessProgramNo())) {
			coreAccountSqlBuilder.andBusinessProgramNoEqualTo(x5395Bo.getBusinessProgramNo());
		}

		if (StringUtil.isNotEmpty(x5395Bo.getProductObjectCode())) {
			coreAccountSqlBuilder.andProductObjectCodeEqualTo(x5395Bo.getProductObjectCode());
		}
		if (StringUtil.isNotEmpty(x5395Bo.getCustomerNo())) {
			coreAccountSqlBuilder.andCustomerNoEqualTo(x5395Bo.getCustomerNo());
			coreTransHistSqlBuilder.andCustomerNoEqualTo(x5395Bo.getCustomerNo());
		}
		if (StringUtil.isNotEmpty(x5395Bo.getBusinessTypeCode())) {
			coreAccountSqlBuilder.andBusinessTypeCodeEqualTo(x5395Bo.getBusinessTypeCode());
			coreTransHistSqlBuilder.andBusinessTypeCodeEqualTo(x5395Bo.getBusinessTypeCode());

		}
		List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
		if (coreAccountList != null && coreAccountList.size() > 0) {
			CoreTransHistSqlBuilder coreTransHistSqlBuilderNew = new CoreTransHistSqlBuilder();
			for (CoreAccount coreAccount : coreAccountList) {
				coreTransHistSqlBuilderNew.orAccountIdEqualTo(coreAccount.getAccountId());
			}
			coreTransHistSqlBuilder.and(coreTransHistSqlBuilderNew);
		}
		PageBean<CoreTransHist> page = new PageBean<>();
		if (null != x5395Bo.getPageSize() && null != x5395Bo.getIndexNo()) {
			coreTransHistSqlBuilder.setPageSize(x5395Bo.getPageSize());
			coreTransHistSqlBuilder.setIndexNo(x5395Bo.getIndexNo());
			page.setPageSize(x5395Bo.getPageSize());
			page.setIndexNo(x5395Bo.getIndexNo());
		}
		int totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
		page.setTotalCount(totalCount);
		Integer decimalPlaces = null;
		if (totalCount > 0) {
			coreTransHistSqlBuilder.orderByOccurrDate(false);
			List<CoreTransHist> coreTransHistList = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
			for (CoreTransHist coreTransHist : coreTransHistList) {
				// 金额转换
				if (decimalPlaces == null) {
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getCurrencyCode());
					decimalPlaces = coreCurrency.getDecimalPosition();
				}
				amountConversion(coreTransHist, decimalPlaces);
			}
			page.setRows(coreTransHistList);
		}
		return page;
	}

	private void amountConversion(CoreTransHist coreTransHist, Integer decimalPlaces) throws Exception {
		if (coreTransHist.getTransAmount() != null) {
			BigDecimal transAmount = CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(), decimalPlaces);
			coreTransHist.setTransAmount(transAmount);
		}
		if (coreTransHist.getPostingAmount() != null) {
			BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreTransHist.getPostingAmount(), decimalPlaces);
			coreTransHist.setPostingAmount(postingAmount);
		}
		if (coreTransHist.getSettlementAmount() != null) {
			BigDecimal settlementAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettlementAmount(),
					decimalPlaces);
			coreTransHist.setSettlementAmount(settlementAmount);
		}
		if (coreTransHist.getActualPostingAmount() != null) {
			BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(),
					decimalPlaces);
			coreTransHist.setActualPostingAmount(actualPostingAmount);
		}
		if (coreTransHist.getSettleDistriAmount() != null) {
			BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettleDistriAmount(),
					decimalPlaces);
			coreTransHist.setSettleDistriAmount(settleDistriAmount);
		}
		if (coreTransHist.getOverpayFrzAmount() != null) {
			BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayFrzAmount(),
					decimalPlaces);
			coreTransHist.setOverpayFrzAmount(overpayFrzAmount);
		}
		if (coreTransHist.getOverpayAmount() != null) {
			BigDecimal overpayAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayAmount(), decimalPlaces);
			coreTransHist.setOverpayAmount(overpayAmount);
		}
	}

	/**
	 * 查询客户统一日期
	 * 
	 * @param lastDealDate
	 *            上次处理日期
	 * @param currentDealDate
	 *            当前处理日期
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private List<CoreCustomerUnifyInfo> queryCoreCustomerUnifyInfo(String lastDealDate, String currentDealDate)
			throws Exception {
		CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
		coreCustomerUnifyInfoSqlBuilder.andStatementDateGreaterThan(lastDealDate)
				.andStatementDateLessThanOrEqualTo(currentDealDate);
		List<CoreCustomerUnifyInfo> coreCustomerUnifyInfoList = coreCustomerUnifyInfoDao
				.selectListBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
		return coreCustomerUnifyInfoList;
	}

}
