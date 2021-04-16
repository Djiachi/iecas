package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5565Bus;
import com.tansun.ider.bus.X8125Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.CoreAccountBean;
import com.tansun.ider.model.CoreBalanceBean;
import com.tansun.ider.model.bo.X5565BO;
import com.tansun.ider.model.vo.X5565VO;
import com.tansun.ider.service.HttpQueryService;

/**
 * 调用利息试算
 * 
 * @author wt
 *
 */
@Service
public class X5565BusImpl implements X5565Bus {

	@Autowired
	private CoreBalanceUnitDao coreBalanceUnitDao;
	@Autowired
	private X8125Bus x8125Bus;
	@Autowired
	private CoreAccountDao coreAccountDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5565BO x5565bo) throws Exception {
		// 试算日期
		String interestStartDate = x5565bo.getInterestStartDate();
		// 账户代码
		String accountId = x5565bo.getAccountId();
		// 币种
		String accountCurrency = x5565bo.getAccountCurrency();
		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
		coreAccountSqlBuilder.andCurrencyCodeEqualTo(accountCurrency);
		CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
		//获取运营模式
		String operationMode = "";
		X5565VO x5565VO = new X5565VO();
		if (null != coreAccount) {
			operationMode = coreAccount.getOperationMode();
			CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
			coreBalanceUnitSqlBuilder.andAccountIdEqualTo(accountId);
			coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(accountCurrency);
			coreBalanceUnitSqlBuilder.andBalanceIsNotNull();
			coreBalanceUnitSqlBuilder.andBalanceNotEqualTo(BigDecimal.ZERO);
			coreBalanceUnitSqlBuilder.orderByCycleNumber(false);
			coreBalanceUnitSqlBuilder.orderByBalanceUnitCode(false);
			List<CoreBalanceUnit> listCoreBalanceUnit = coreBalanceUnitDao
					.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
			BigDecimal balanceTotal = BigDecimal.ZERO;
			List<CoreBalanceBean> listcoreBalanceBean = new ArrayList<CoreBalanceBean>();
			if (null != listCoreBalanceUnit && !listCoreBalanceUnit.isEmpty()) {
				for (CoreBalanceUnit coreBalanceUnit : listCoreBalanceUnit) {
					// 获取币种对应小数位
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreBalanceUnit.getCurrencyCode());
					int currencyDecimal = coreCurrency.getDecimalPosition();
					// 获取币种对应小数位
					BigDecimal interest = x8125Bus.calIntForLoan(coreBalanceUnit, coreAccount, currencyDecimal,
							interestStartDate);
					balanceTotal = balanceTotal.add(interest);
					CoreBalanceBean coreBalanceBean = new CoreBalanceBean();
					CachedBeanCopy.copyProperties(coreBalanceUnit, coreBalanceBean);
					BigDecimal setScale1 = interest.setScale(2,BigDecimal.ROUND_HALF_UP);
					int decimalPlaces  = coreCurrency.getDecimalPosition();
					BigDecimal postingInterest = CurrencyConversionUtil.reduce(setScale1,
							 decimalPlaces);
					coreBalanceBean.setInterest(postingInterest);
					BigDecimal balance = coreBalanceBean.getBalance();
					BigDecimal postingBalance = CurrencyConversionUtil.reduce(balance,
							 decimalPlaces);
					coreBalanceBean.setBalance(postingBalance);
					if(StringUtil.isNotBlank(coreBalanceBean.getBalanceObjectCode()) && StringUtil.isNotBlank(operationMode)){
						CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, coreBalanceBean.getBalanceObjectCode());
						if(null != coreBalanceObject){
							coreBalanceBean.setObjectDesc(coreBalanceObject.getObjectDesc());
						}
					}
					if(null != coreCurrency){
						coreBalanceBean.setCurrencyDesc(coreCurrency.getCurrencyDesc());
					}
					listcoreBalanceBean.add(coreBalanceBean);
				}
				CoreAccountBean coreAccountBean = new CoreAccountBean();
				coreAccountBean.setAccountId(accountId);
				BigDecimal balanceTota2 = balanceTotal.setScale(2,BigDecimal.ROUND_HALF_UP);
				CoreCurrency coreCurrencyAccount = httpQueryService.queryCurrency(coreAccount.getCurrencyCode());
				//获取币种描述
				String currencyDesc = "";
				if(null != coreCurrencyAccount){
					currencyDesc = coreCurrencyAccount.getCurrencyDesc();
					coreAccountBean.setCurrencyDesc(currencyDesc);
				}
				int decimalPlaces  = coreCurrencyAccount.getDecimalPosition();
				BigDecimal unPostingInterest = CurrencyConversionUtil.reduce(balanceTota2,
						 decimalPlaces);
				coreAccountBean.setBalanceTotal(unPostingInterest);
				coreAccountBean.setCurrencyCode(accountCurrency);
				coreAccountBean.setInterestStartDate(interestStartDate);
				x5565VO.setCoreAccountBean(coreAccountBean);
				x5565VO.setListcoreBalanceBean(listcoreBalanceBean);
			} else {
				throw new BusinessException("CUS-00071");
			}
		}else {
			throw new BusinessException("CUS-00014","账户基本");
		}
		return x5565VO;
	}

}
