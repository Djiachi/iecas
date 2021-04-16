package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5999Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.issue.CorePaymentAllocationSeqDao;
import com.tansun.ider.dao.issue.entity.CorePaymentAllocationSeq;
import com.tansun.ider.dao.issue.sqlbuilder.CorePaymentAllocationSeqSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5999BO;
import com.tansun.ider.model.vo.X5999VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;

/**
 * 
* @ClassName: X5999BusImpl 
* @Description: 还款分配顺序查询
* @author by
* @date 2019年11月17日 下午4:45:00 
*
 */
@Service
public class X5999BusImpl implements X5999Bus {

	@Autowired
	private CorePaymentAllocationSeqDao corePaymentAllocationSeqDao;
	@Autowired
    private HttpQueryService httpQueryService;
	@Override
	public Object busExecute(X5999BO x5999bo) throws Exception {
		PageBean<X5999VO> page = new PageBean<>();
	    String customerNo = x5999bo.getCustomerNo();
	    String globalSerialNumbr = x5999bo.getGlobalSerialNumbr();
	    String accountId = x5999bo.getAccountId(); 
	    String currencyCode = x5999bo.getCurrencyCode(); 
	    String balanceType = x5999bo.getBalanceType();
	    String operationMode = x5999bo.getOperationMode();
	    String ctdStmtFlag = x5999bo.getCtdStmtFlag();
	    CorePaymentAllocationSeqSqlBuilder corePaymentAllocationSeqSqlBuilder = new CorePaymentAllocationSeqSqlBuilder();
		if(StringUtil.isNotEmpty(customerNo)){
		    corePaymentAllocationSeqSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		if(StringUtil.isNotEmpty(globalSerialNumbr)){
		    corePaymentAllocationSeqSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
		}
		if(StringUtil.isNotEmpty(accountId)){
		    corePaymentAllocationSeqSqlBuilder.andAccountIdEqualTo(accountId);
		}
		if(StringUtil.isNotEmpty(currencyCode)){
		    corePaymentAllocationSeqSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
		}
		if(StringUtil.isNotEmpty(balanceType)){
		    corePaymentAllocationSeqSqlBuilder.andBalanceTypeEqualTo(balanceType);
		}
		if(StringUtil.isNotEmpty(ctdStmtFlag)){
			corePaymentAllocationSeqSqlBuilder.andCtdStmtFlagEqualTo(ctdStmtFlag);
		}
		int totalCount = corePaymentAllocationSeqDao.countBySqlBuilder(corePaymentAllocationSeqSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5999bo.getPageSize() && null != x5999bo.getIndexNo()) {
		    corePaymentAllocationSeqSqlBuilder.setPageSize(x5999bo.getPageSize());
		    corePaymentAllocationSeqSqlBuilder.setIndexNo(x5999bo.getIndexNo());
		    corePaymentAllocationSeqSqlBuilder.orderByDistributionOrder(false);
			page.setPageSize(x5999bo.getPageSize());
			page.setIndexNo(x5999bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CorePaymentAllocationSeq> list = corePaymentAllocationSeqDao
					.selectListBySqlBuilder(corePaymentAllocationSeqSqlBuilder);
			List<X5999VO> returnList = new ArrayList<>();
			for (CorePaymentAllocationSeq corePaymentAllocationSeq : list) {
			    X5999VO x5999VO = new X5999VO();
			    CachedBeanCopy.copyProperties(corePaymentAllocationSeq, x5999VO);
			    BigDecimal distributionAmount = amountConversion(x5999VO.getDistributionAmount(), x5999VO.getCurrencyCode());
			    x5999VO.setDistributionAmount(distributionAmount);
			    //获取余额单元描述
                CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, x5999VO.getBalanceObjectCode());
                if(coreBalanceObject!=null){
                    x5999VO.setBalanceObjectDesc(coreBalanceObject.getObjectDesc());
                }
                returnList.add(x5999VO);
			}
			page.setRows(returnList);
		}
		return page;
	}
	
	 /**
     * 
     *
     * @MethodName amountConversion
     * @Description: 查询交易信息中金额转换
     * @param currencyCode
     * @throws Exception
     * @return: void
     */
    private BigDecimal amountConversion(BigDecimal amount, String currencyCode) throws Exception {
        CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
        int currencyDecimal = cardUtil.getCurrencyDecimal(currencyCode);
        return  CurrencyConversionUtil.reduce(amount, currencyDecimal);

    }
    
}
