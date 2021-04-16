package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5390Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountCycleFiciDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreBusinessTypeBillSumDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerDelqStaticDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductObjectBillSumDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBusinessTypeBillSum;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelqStatic;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBusinessTypeBillSumSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelqStaticSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5390BO;
import com.tansun.ider.model.vo.X5390VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 账单产品对象+业务类型维度查询
 *
 * @author huangyayun
 * @date 2018年8月13日
 */
@Service
public class X5390BusImpl implements X5390Bus {
	@Resource
	private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;//客户统一日期表
	@Resource
	private CoreAccountDao coreAccountDao;//客户基本账户表
	@Resource
	private CoreCustomerBillDayDao coreCustomerBillDayDao;//客户业务项目表
	@Resource
	private CoreAccountCycleFiciDao coreAccountCycleFiciDao;//客户周期余额信息表
	@Resource
	private CoreBalanceUnitDao coreBalanceUnitDao;//余额单元表
	@Resource
	private CoreProductObjectBillSumDao coreProductObjectBillSumDao;//产品对象账单摘要表
	@Resource
	private CoreBusinessTypeBillSumDao coreBusinessTypeBillSumDao;//业务类型账单摘要表
	@Resource
	private CoreCustomerDao coreCustomerDao;//客户号
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;//媒介基本信息
	@Resource
	private CoreCustomerDelqStaticDao coreCustomerDelqStaticDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Override
	public Object busExecute(X5390BO x5390Bo) throws Exception {

		CoreBusinessTypeBillSumSqlBuilder coreBusinessTypeBillSumSqlBuilder = new CoreBusinessTypeBillSumSqlBuilder();
		if(StringUtil.isNotEmpty(x5390Bo.getCurrencyCode())){
			coreBusinessTypeBillSumSqlBuilder.andCurrencyCodeEqualTo(x5390Bo.getCurrencyCode());
		}
		if(StringUtil.isNotEmpty(x5390Bo.getBillDate())){
			coreBusinessTypeBillSumSqlBuilder.andBillDateEqualTo(x5390Bo.getBillDate());
		}
		if(StringUtil.isNotEmpty(x5390Bo.getBusinessProgramNo())){
			coreBusinessTypeBillSumSqlBuilder.andBusinessProgramNoEqualTo(x5390Bo.getBusinessProgramNo());
		}

		if(StringUtil.isNotEmpty(x5390Bo.getProductObjectCode())){
			coreBusinessTypeBillSumSqlBuilder.andProductObjectCodeEqualTo(x5390Bo.getProductObjectCode());
		}
		if(StringUtil.isNotEmpty(x5390Bo.getCustomerNo())){
			coreBusinessTypeBillSumSqlBuilder.andCustomerNoEqualTo(x5390Bo.getCustomerNo());
		}
		if(StringUtil.isNotEmpty(x5390Bo.getBusinessTypeCode())){
			coreBusinessTypeBillSumSqlBuilder.andBusinessTypeCodeEqualTo(x5390Bo.getBusinessTypeCode());
		}
		PageBean<X5390VO> page = new PageBean<>();
		if (null != x5390Bo.getPageSize() && null != x5390Bo.getIndexNo()) {
			coreBusinessTypeBillSumSqlBuilder.setPageSize(x5390Bo.getPageSize());
			coreBusinessTypeBillSumSqlBuilder.setIndexNo(x5390Bo.getIndexNo());
			page.setPageSize(x5390Bo.getPageSize());
			page.setIndexNo(x5390Bo.getIndexNo());
		}
		int totalCount = coreBusinessTypeBillSumDao.countBySqlBuilder(coreBusinessTypeBillSumSqlBuilder);
		page.setTotalCount(totalCount);

		if (totalCount > 0) {
			Integer decimalPlaces = null;
			List<X5390VO> x5390VOList = new ArrayList<X5390VO>();
			coreBusinessTypeBillSumSqlBuilder.orderByCurrentCycleNumber(false);
			List<CoreBusinessTypeBillSum> coreBusinessTypeBillSumList = coreBusinessTypeBillSumDao
					.selectListBySqlBuilder(coreBusinessTypeBillSumSqlBuilder);
			for (CoreBusinessTypeBillSum coreBusinessTypeBillSum : coreBusinessTypeBillSumList) {
				X5390VO x5390VO = new X5390VO();
				String currencyDesc = "";
				// 金额转换
				if(decimalPlaces == null){
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreBusinessTypeBillSum.getCurrencyCode());
					//获取币种描述
					if(null != coreCurrency){
						currencyDesc = coreCurrency.getCurrencyDesc();
					}
					decimalPlaces = coreCurrency.getDecimalPosition();
				}
				amountConversion(coreBusinessTypeBillSum, decimalPlaces);
				CachedBeanCopy.copyProperties(coreBusinessTypeBillSum, x5390VO);
				x5390VO.setCurrencyDesc(currencyDesc);
				List<CoreCustomerDelqStatic> staticList = getCoreCustomerDelqStaticList(coreBusinessTypeBillSum);
				for(CoreCustomerDelqStatic  coreCustomerDelqStatic:staticList){
					if(coreCustomerDelqStatic.getCurrCyclePaymentMin()!=null){
						BigDecimal currCyclePaymentMin = CurrencyConversionUtil.reduce(coreCustomerDelqStatic.getCurrCyclePaymentMin(),
								decimalPlaces);
						coreCustomerDelqStatic.setCurrCyclePaymentMin(currCyclePaymentMin);
					}
				}
				BigDecimal minRepayAmt = getMinRepayAmt(staticList);
     /*  	 if(minRepayAmt!=null){
       		 minRepayAmt = CurrencyConversionUtil.reduce(minRepayAmt,
       	                decimalPlaces);
       	 }*/
				x5390VO.setMinRepayAmt(minRepayAmt);
				x5390VO.getPage().setRows(staticList);
				String operationMode = "";
				//获取运营模式
				if(StringUtil.isNotEmpty(coreBusinessTypeBillSum.getCustomerNo())){
					CoreCustomer coreCustomer = httpQueryService.queryCustomer(null,coreBusinessTypeBillSum.getCustomerNo());
					if(null != coreCustomer){
						operationMode = coreCustomer.getOperationMode();
					}
				}
				//获取业务项目描述
				if(StringUtil.isNotEmpty(coreBusinessTypeBillSum.getBusinessProgramNo()) && operationMode != ""){
					CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,coreBusinessTypeBillSum.getBusinessProgramNo());
					if(coreBusinessProgram != null){
						String programDesc = coreBusinessProgram.getProgramDesc();
						x5390VO.setProgramDesc(programDesc);
					}
				}
				//获取业务类型描述
				if(StringUtil.isNotEmpty(coreBusinessTypeBillSum.getBusinessTypeCode()) && operationMode != ""){
					CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,coreBusinessTypeBillSum.getBusinessTypeCode());
					if(coreBusinessType != null){
						String businessDesc = coreBusinessType.getBusinessDesc();
						x5390VO.setBusinessDesc(businessDesc);
					}
				}
				//获取产品对象描述
				if(StringUtil.isNotEmpty(coreBusinessTypeBillSum.getProductObjectCode()) && operationMode != ""){
					CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, coreBusinessTypeBillSum.getProductObjectCode());
					if(coreProductObject != null){
						String productDesc = coreProductObject.getProductDesc();
						x5390VO.setProductDesc(productDesc);
					}
				}
				//查询账户号
				/*String accoutNo = getAccoutNo(x5390VO);
				x5390VO.setAccountId(accoutNo);*/
				x5390VOList.add(x5390VO);
			}
			page.setRows(x5390VOList);
		}
		return page;
	}

	private BigDecimal getMinRepayAmt(List<CoreCustomerDelqStatic> list){
		BigDecimal minRepayAmt = BigDecimal.ZERO;
		if(list!=null&&list.size()>0){
			for(CoreCustomerDelqStatic coreCustomerDelqStatic : list){
				if(coreCustomerDelqStatic.getCycleNo()!=9999 && coreCustomerDelqStatic.getCycleNo()!=9998){
					minRepayAmt = minRepayAmt.add(coreCustomerDelqStatic.getCurrCyclePaymentMin());
				}
			}
		}
		return minRepayAmt;
	}

	private  List<CoreCustomerDelqStatic> getCoreCustomerDelqStaticList(CoreBusinessTypeBillSum coreBusinessTypeBillSum) throws Exception{
		List<CoreAccount> coreAccounts = getAccountList(coreBusinessTypeBillSum);
		if(coreAccounts == null||coreAccounts.size()==0){
			return null;
		}
		CoreCustomerDelqStaticSqlBuilder coreCustomerDelqStaticSqlBuilder = new CoreCustomerDelqStaticSqlBuilder();
		CoreCustomerDelqStaticSqlBuilder coreCustomerDelqStaticSqlBuilder1 = new CoreCustomerDelqStaticSqlBuilder();
		coreCustomerDelqStaticSqlBuilder.andCustomerNoEqualTo(coreBusinessTypeBillSum.getCustomerNo());
		coreCustomerDelqStaticSqlBuilder.andCurrencyCodeEqualTo(coreBusinessTypeBillSum.getCurrencyCode());
		coreCustomerDelqStaticSqlBuilder.andCreateCycleNoEqualTo(coreBusinessTypeBillSum.getCurrentCycleNumber());
    /*   	coreCustomerDelqStaticSqlBuilder.andCycleNoNotEqualTo(9999);
    	coreCustomerDelqStaticSqlBuilder.andCycleNoNotEqualTo(9998);*/
		for(CoreAccount coreAccount:coreAccounts){
			coreCustomerDelqStaticSqlBuilder1.orLevelCodeEqualTo(coreAccount.getAccountId());
		}
		coreCustomerDelqStaticSqlBuilder.and(coreCustomerDelqStaticSqlBuilder1);
		coreCustomerDelqStaticSqlBuilder.orderByLevelCode(true);
		List<CoreCustomerDelqStatic> lists = coreCustomerDelqStaticDao.selectListBySqlBuilder(coreCustomerDelqStaticSqlBuilder);
		return lists;
	}

	private void amountConversion(CoreBusinessTypeBillSum coreBusinessTypeBillSum, Integer decimalPlaces)
			throws Exception {
		if(coreBusinessTypeBillSum.getGraceBalance()!=null){
			BigDecimal graceBalance = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getGraceBalance(),
					decimalPlaces);
			coreBusinessTypeBillSum.setGraceBalance(graceBalance);
		}
		if(coreBusinessTypeBillSum.getCreditAmount()!=null){
			BigDecimal creditAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getCreditAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setCreditAmount(creditAmount);
		}
		if(coreBusinessTypeBillSum.getDebitAmount()!=null){
			BigDecimal debitAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getDebitAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setDebitAmount(debitAmount);
		}
		if(coreBusinessTypeBillSum.getCurrentRepayAmount()!=null){
			BigDecimal currentRepayAmount = CurrencyConversionUtil
					.reduce(coreBusinessTypeBillSum.getCurrentRepayAmount(), decimalPlaces);
			coreBusinessTypeBillSum.setCurrentRepayAmount(currentRepayAmount);
		}
		if(coreBusinessTypeBillSum.getPostingAmount()!=null){
			BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getPostingAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setPostingAmount(postingAmount);
		}
		if(coreBusinessTypeBillSum.getPrincipalAmount()!=null){
			BigDecimal principalAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getPrincipalAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setPrincipalAmount(principalAmount);
		}
		if(coreBusinessTypeBillSum.getFeeAmount()!=null){
			BigDecimal feeAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getFeeAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setFeeAmount(feeAmount);
		}
		if(coreBusinessTypeBillSum.getInterestAmount()!=null){
			BigDecimal interestAmount = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getInterestAmount(),
					decimalPlaces);
			coreBusinessTypeBillSum.setInterestAmount(interestAmount);
		}
		if(coreBusinessTypeBillSum.getCurrentAccountBalance()!=null){
			BigDecimal currentAccountBalance = CurrencyConversionUtil
					.reduce(coreBusinessTypeBillSum.getCurrentAccountBalance(), decimalPlaces);
			coreBusinessTypeBillSum.setCurrentAccountBalance(currentAccountBalance);
		}
		if(coreBusinessTypeBillSum.getBeginBalance()!=null){
			BigDecimal beginBalance = CurrencyConversionUtil.reduce(coreBusinessTypeBillSum.getBeginBalance(),
					decimalPlaces);
			coreBusinessTypeBillSum.setBeginBalance(beginBalance);
		}
	}

	private  List<CoreAccount> getAccountList(CoreBusinessTypeBillSum coreBusinessTypeBillSum) throws Exception{
		CoreAccountSqlBuilder accountSqlBuilder = new CoreAccountSqlBuilder();
		accountSqlBuilder.andCustomerNoEqualTo(coreBusinessTypeBillSum.getCustomerNo());
		accountSqlBuilder.andCurrencyCodeEqualTo(coreBusinessTypeBillSum.getCurrencyCode());
		accountSqlBuilder.andBusinessProgramNoEqualTo(coreBusinessTypeBillSum.getBusinessProgramNo());
		accountSqlBuilder.andBusinessTypeCodeEqualTo(coreBusinessTypeBillSum.getBusinessTypeCode());
		accountSqlBuilder.andProductObjectCodeEqualTo(coreBusinessTypeBillSum.getProductObjectCode());
		List<CoreAccount> lists = coreAccountDao.selectListBySqlBuilder(accountSqlBuilder);
		return lists;
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
	private List<CoreCustomerUnifyInfo> queryCoreCustomerUnifyInfo(String lastDealDate,String currentDealDate) throws Exception {
		CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
		coreCustomerUnifyInfoSqlBuilder.andStatementDateGreaterThan(lastDealDate).andStatementDateLessThanOrEqualTo(currentDealDate);
		List<CoreCustomerUnifyInfo> coreCustomerUnifyInfoList = coreCustomerUnifyInfoDao.selectListBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
		return coreCustomerUnifyInfoList;
	}

	//查询账号
	private String getAccoutNo(X5390VO x5390VO){
		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		coreAccountSqlBuilder.andCurrencyCodeEqualTo(x5390VO.getCurrencyCode());
		coreAccountSqlBuilder.andBusinessTypeCodeEqualTo(x5390VO.getBusinessTypeCode());
		coreAccountSqlBuilder.andCustomerNoEqualTo(x5390VO.getCustomerNo());
		coreAccountSqlBuilder.andProductObjectCodeEqualTo(x5390VO.getProductObjectCode());
		coreAccountSqlBuilder.andBusinessProgramNoEqualTo(x5390VO.getBusinessProgramNo());
		coreAccountSqlBuilder.and(new CoreAccountSqlBuilder().andSubAccIdentifyEqualTo(SubAccountIdentify.P.getValue())
                .orSubAccIdentifyEqualTo(SubAccountIdentify.S.getValue()));
		CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
		String accountNo=null;
		if(null!=coreAccount){
			accountNo=coreAccount.getAccountId();
		}
		return accountNo;
	}

}
