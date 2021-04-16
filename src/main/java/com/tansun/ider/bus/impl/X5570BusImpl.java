package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5570Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreOccurrAmtTransRelDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreOccurrAmtTransRel;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOccurrAmtTransRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5570BO;
import com.tansun.ider.service.HttpQueryService;

/**
 * @version:1.0
 * @Description: 发生额关联交易查询
 * @author: admin
 */
@Service
public class X5570BusImpl implements X5570Bus {

	@Autowired
	private CoreOccurrAmtTransRelDao coreOccurrAmtTransRelDao;
	@Autowired
	private CoreTransHistDao coreTransHistDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreBalanceUnitDao coreBalanceUnitDao;

	@Override
	public Object busExecute(X5570BO x5570bo) throws Exception {
		PageBean<CoreTransHist> page = new PageBean<>();
		/** 余额单元代码 [18,0] Not NULL */
		String balanceUnitCode = x5570bo.getBalanceUnitCode();
		/** 起息日期 yyyy-MM-dd [10,0] Not NULL */
		String interestStartDate = x5570bo.getInterestStartDate();
		/** 节点类别 DR-借方节点 CR-贷方节点 PY-还款节点 NI-NETIN节点 [2,0] Not NULL */
		String nodeTyp = x5570bo.getNodeTyp();
		String serialNumber = x5570bo.getSerialNumber();
		CoreOccurrAmtTransRelSqlBuilder coreOccurrAmtTransRelSqlBuilder = new CoreOccurrAmtTransRelSqlBuilder();
		coreOccurrAmtTransRelSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
//		coreOccurrAmtTransRelSqlBuilder.andInterestStartDateEqualTo(interestStartDate);
//		coreOccurrAmtTransRelSqlBuilder.andNodeTypEqualTo(nodeTyp);
		if (StringUtil.isNotBlank(serialNumber)) {
			coreOccurrAmtTransRelSqlBuilder.andSerialNumberEqualTo(Integer.valueOf(serialNumber));
		}
		List<CoreOccurrAmtTransRel> listCoreOccurrAmtTransRels = coreOccurrAmtTransRelDao
				.selectListBySqlBuilder(coreOccurrAmtTransRelSqlBuilder);
		
		CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
		coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
		CoreBalanceUnit coreBalanceUnit = coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
		int totalCount = 0;
		for (CoreOccurrAmtTransRel coreOccurrAmtTransRel : listCoreOccurrAmtTransRels) {
			CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
			coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(coreOccurrAmtTransRel.getGlobalTransSerialNo())
					.and(new CoreTransHistSqlBuilder().andActivityNoEqualTo("X8040").orActivityNoEqualTo("X8050")
							.orActivityNoEqualTo("X8050"));
			// coreTransHistSqlBuilder.andLogLevelEqualTo("A");
			coreTransHistSqlBuilder.andBalanceObjectCodeEqualTo(coreBalanceUnit.getBalanceObjectCode());
			totalCount = totalCount + coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
		}
		page.setTotalCount(totalCount);
		List<CoreTransHist> listAll = new ArrayList<>();
		for (CoreOccurrAmtTransRel coreOccurrAmtTransRel : listCoreOccurrAmtTransRels) {
			int totalCountOne = 0;
			CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
			coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(coreOccurrAmtTransRel.getGlobalTransSerialNo())
					.and(new CoreTransHistSqlBuilder().andActivityNoEqualTo("X8040").orActivityNoEqualTo("X8050")
							.orActivityNoEqualTo("X8060"));
			// coreTransHistSqlBuilder.andActivityNoEqualTo("X8040");
			// coreTransHistSqlBuilder.andLogLevelEqualTo("A");
			coreTransHistSqlBuilder.andBalanceObjectCodeEqualTo(coreBalanceUnit.getBalanceObjectCode());
			totalCountOne = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
			if (totalCountOne > 0) {
				List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
				for (CoreTransHist coreTransHist : list) {
					// 金额转换
					amountConversion(coreTransHist);
				}
				listAll.addAll(list);
			}
		}
		page.setRows(listAll);
		return page;
	}

	/**
	 * 转换币种，交易金额
	 */
	private void amountConversion(CoreTransHist coreTransHist) throws Exception {
		// 实际入账金额
		CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getCurrencyCode());
		int decimalPlaces = coreCurrency.getDecimalPosition();
		if (coreTransHist.getActualPostingAmount() != null
				&& !coreTransHist.getActualPostingAmount().toString().equals("0")) {
			BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(),
					decimalPlaces);
			coreTransHist.setActualPostingAmount(actualPostingAmount);
		}
		// 清算分配金额
		if (coreTransHist.getSettleDistriAmount() != null
				&& !coreTransHist.getSettleDistriAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettleDistriAmount = httpQueryService
					.queryCurrency(coreTransHist.getSettleDistriCurrency());
			int decimalPlacesSettleDistriAmount = coreCurrencySettleDistriAmount.getDecimalPosition();
			BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettleDistriAmount(),
					decimalPlacesSettleDistriAmount);
			coreTransHist.setSettleDistriAmount(settleDistriAmount);
		}
		// 溢缴款冻结金额 overpay_frz_amount
		if (coreTransHist.getOverpayFrzAmount() != null
				&& !coreTransHist.getOverpayFrzAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettleDistriAmount = httpQueryService
					.queryCurrency(coreTransHist.getOverpayFrzCurrCode());
			int decimalPlacesOverpayFrzAmount = coreCurrencySettleDistriAmount.getDecimalPosition();
			BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayFrzAmount(),
					decimalPlacesOverpayFrzAmount);
			coreTransHist.setOverpayFrzAmount(overpayFrzAmount);
		}
		// 清算金额 settlement_amount
		if (coreTransHist.getSettlementAmount() != null
				&& !coreTransHist.getSettlementAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettlementAmount = httpQueryService
					.queryCurrency(coreTransHist.getSettlementCurrencyCode());
			int decimalPlacesSettlementAmountt = coreCurrencySettlementAmount.getDecimalPosition();
			BigDecimal settlementAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettlementAmount(),
					decimalPlacesSettlementAmountt);
			coreTransHist.setSettlementAmount(settlementAmount);
		}
		// 入账币种
		if (coreTransHist.getPostingAmount() != null && !coreTransHist.getPostingAmount().toString().equals("0")) {
			CoreCurrency coreCurrencyPostingAmount = httpQueryService
					.queryCurrency(coreTransHist.getPostingCurrencyCode());
			int decimalPlacesPostingAmount = coreCurrencyPostingAmount.getDecimalPosition();
			BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreTransHist.getPostingAmount(),
					decimalPlacesPostingAmount);
			coreTransHist.setPostingAmount(postingAmount);
		}
		// 交易金额
		if (coreTransHist.getTransAmount() != null && !coreTransHist.getTransAmount().toString().equals("0")) {
			CoreCurrency coreCurrencyTransAmount = httpQueryService.queryCurrency(coreTransHist.getTransCurrCde());
			int decimalPlacesTransAmount = coreCurrencyTransAmount.getDecimalPosition();
			BigDecimal transAmount = CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(),
					decimalPlacesTransAmount);
			coreTransHist.setTransAmount(transAmount);
		}
	}

}
