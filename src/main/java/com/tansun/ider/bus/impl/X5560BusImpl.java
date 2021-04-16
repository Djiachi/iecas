package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5560Bus;
import com.tansun.ider.bus.X8125Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.sqlbuilder.CoreCurrencySqlBuilder;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreInterestContrlChain;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.CustomerContrlViewBean;
import com.tansun.ider.model.InterestProcessBean;
import com.tansun.ider.model.bo.X5560BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerBlockCode;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * @version:1.0
 * @Description: 计息过程
 * @author: admin
 */
@Service
public class X5560BusImpl implements X5560Bus {

    @Autowired
    private X8125Bus x8125Bus;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Autowired
    private QueryCustomerBlockCode queryCustomerBlockCode;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Value("${global.target.service.cease.interest}")
    private String controlProjectNo;
    @Resource
    private HttpQueryService httpQueryService;
    
    @SuppressWarnings("unused")
	@Override
    public Object busExecute(X5560BO x5560bo) throws Exception {
    	 SpringUtil.getBean(ValidatorUtil.class).validate(x5560bo);
        PageBean<InterestProcessBean> page = new PageBean<>();
        // 开始日期
        String interestStartDate = x5560bo.getInterestStartDate();
        // 结束日期
        String billingEndDate = x5560bo.getBillingEndDate();
        // 余额单元
        String balanceUnitCode = x5560bo.getBalanceUnitCode();
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        coreBalanceUnitSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
        CoreBalanceUnit coreBalanceUnit = coreBalanceUnitDao.selectBySqlBuilder(coreBalanceUnitSqlBuilder);
        String accountId = coreBalanceUnit.getAccountId();
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountSqlBuilder.andCurrencyCodeEqualTo(coreBalanceUnit.getCurrencyCode());
        CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
        String customerNo = coreAccount.getCustomerNo();
        List<CustomerContrlViewBean> listCustomerContrlViewBean = queryCustomerBlockCode
                .queryCoreCustomerContrlView(customerNo, controlProjectNo, interestStartDate, billingEndDate, "yyyy-MM-dd");
        CoreInterestContrlChain coreInterestContrlChain = new CoreInterestContrlChain();
        CachedBeanCopy.copyProperties(x5560bo, coreInterestContrlChain);
        List<InterestProcessBean> interestProcessBeanList = x8125Bus.calInterestForProcess(coreInterestContrlChain,
                listCustomerContrlViewBean);
        List<InterestProcessBean> interestProcessBeanListA  = new ArrayList<>();
        if (null != interestProcessBeanList && !interestProcessBeanList.isEmpty()) {
			for (InterestProcessBean interestProcessBean : interestProcessBeanList) {
				String startDate = interestProcessBean.getStartDate();
				String endDate  = interestProcessBean.getEndDate();
				if (StringUtil.isNotBlank(startDate) &&
						StringUtil.isNotBlank(endDate)) {
					if (startDate.compareTo(endDate) <=0) {
						interestProcessBeanListA.add(interestProcessBean);
					}
				}
			}
		}
        
        page.setRows(interestProcessBeanListA);
        page.setPageSize(interestProcessBeanListA.size());
        return page;
    }
    
    private void amountConversion(InterestProcessBean interestProcessBean,String currencyCode) throws Exception {
		CoreCurrencySqlBuilder coreCurrencySqlBuilder = new CoreCurrencySqlBuilder();
		coreCurrencySqlBuilder.andCurrencyCodeEqualTo(currencyCode);
		CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
		if (coreCurrency != null) {
			int decimalPlaces = coreCurrency.getDecimalPosition();
			BigDecimal pricipal = CurrencyConversionUtil.reduce(interestProcessBean.getPricipal(), decimalPlaces);
			interestProcessBean.setPricipal(pricipal);
		}
	}
    
    
    
}
