package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5550Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.sqlbuilder.CoreCurrencySqlBuilder;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreDepositInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreDepositInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreDepositInfoSqlBuilder;
import com.tansun.ider.enums.LendingD;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5550BO;
import com.tansun.ider.model.vo.X5550VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 存款账户信息查询
 * 
 * @author wt 2018年11月05日
 */
@Service
public class X5550BusImpl implements X5550Bus {

	@Autowired
	private CoreDepositInfoDaoImpl coreDepositInfoDaoImpl;
//	@Autowired
//	private CoreCustomerDao coreCustomerDao;
//	@Autowired
//	private CoreMediaBasicInfoDao coreMediaB	asicInfoDao;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreAccountDao coreAccountDao;
    @Resource
    private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5550BO x5550bo) throws Exception {
		PageBean<X5550VO> page = new PageBean<>();
		String customerNo = x5550bo.getCustomerNo();
		String idNumber = x5550bo.getIdNumber();
		String externalIdentificationNo = x5550bo.getExternalIdentificationNo();
		String idType = x5550bo.getIdType();
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			//预算单位溢缴款转出需要
			page.setObj(coreCustomer);
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
		}
//		if (StringUtil.isNotBlank(idNumber)&&StringUtil.isNotBlank(idType)) {
//			// 查询客户基本信息
//			CoreCustomer coreCustomer = queryCustomerService.queryCoreCustomer(idType, idNumber);
////			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
////			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
////			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
////			if (coreCustomer == null) {
////				throw new BusinessException("CUS-00005");
////			}
//			customerNo = coreCustomer.getCustomerNo();
//		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			// 查询媒介基本信息
//			CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
////			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
////			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
////			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
////					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
////			if (coreMediaBasicInfo == null) {
////				throw new BusinessException("CUS-00005");
////			}
//			customerNo = coreMediaBasicInfo.getMainCustomerNo();
//		}
		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreAccountSqlBuilder.andBusinessDebitCreditCodeEqualTo(LendingD.C.getValue());
		List<CoreAccount> listCoreAccounts = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
		
		List<X5550VO> x5550VOList = new ArrayList<>();
		int totalCountAll = 0;
		for (CoreAccount coreAccount : listCoreAccounts) {
			CoreDepositInfoSqlBuilder coreDepositInfoSqlBuilder = new CoreDepositInfoSqlBuilder();
			if (StringUtil.isNotBlank(coreAccount.getAccountId())) {
				coreDepositInfoSqlBuilder.andAccountIdEqualTo(coreAccount.getAccountId());
			}
		    int totalCount = coreDepositInfoDaoImpl.countBySqlBuilder(coreDepositInfoSqlBuilder);
			if (totalCount > 0) {
				totalCountAll+=totalCount;
				List<CoreDepositInfo> list = coreDepositInfoDaoImpl.selectListBySqlBuilder(coreDepositInfoSqlBuilder);
				for (CoreDepositInfo coreDepositInfo : list) {
					X5550VO x5550VO = new X5550VO();
					// 金额转换
					amountConversion(coreDepositInfo,x5550VO);
					CachedBeanCopy.copyProperties(coreDepositInfo, x5550VO);
					x5550VOList.add(x5550VO);
				}
			}
		}
		page.setTotalCount(totalCountAll);
		page.setRows(x5550VOList);
		return page;
	}

	private void amountConversion(CoreDepositInfo coreDepositInfo,X5550VO x5550VO) throws Exception {
		CoreCurrencySqlBuilder coreCurrencySqlBuilder = new CoreCurrencySqlBuilder();
		coreCurrencySqlBuilder.andCurrencyCodeEqualTo(coreDepositInfo.getCurrencyCode());
		CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreDepositInfo.getCurrencyCode());
		if (coreCurrency != null) {
			int decimalPlaces = coreCurrency.getDecimalPosition();
			BigDecimal currBalance = CurrencyConversionUtil.reduce(coreDepositInfo.getCurrBalance(), decimalPlaces);
			BigDecimal interestBillingAmt = CurrencyConversionUtil.reduce(coreDepositInfo.getInterestBillingAmt(),
					decimalPlaces);
			BigDecimal frozenBalance = CurrencyConversionUtil.reduce(coreDepositInfo.getFrozenBalance(),
					decimalPlaces);
			BigDecimal currAccumultBalance = CurrencyConversionUtil.reduce(coreDepositInfo.getCurrAccumultBalance(),
					decimalPlaces);
			 BigDecimal unPostingInterest = CurrencyConversionUtil.reduce(coreDepositInfo.getUnPostingInterest(),
						decimalPlaces);
			coreDepositInfo.setCurrBalance(currBalance);
			coreDepositInfo.setInterestBillingAmt(interestBillingAmt);
			coreDepositInfo.setFrozenBalance(frozenBalance);
			coreDepositInfo.setCurrAccumultBalance(currAccumultBalance);
			coreDepositInfo.setUnPostingInterest(unPostingInterest);
			//获取交易币种
			x5550VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
		}
	}
	
}
