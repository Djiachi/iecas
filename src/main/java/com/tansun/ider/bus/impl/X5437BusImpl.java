package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5437Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountCycleFiciDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreBusinessTypeBillSumDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDelqStaticDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductObjectBillSumDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelqStatic;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProductObjectBillSum;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelqStaticSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductObjectBillSumSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5437BO;
import com.tansun.ider.model.vo.X5437VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CachedBeanCopy;

//import oracle.net.aso.q;


/**
 * 查询当期账单
 * @author liuyanxi
 *
 */
@Service
public class X5437BusImpl implements X5437Bus {
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
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;// 媒介基本信息
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    CoreCustomerDelqStaticDao coreCustomerDelqStaticDao;
	@Resource
    private HttpQueryServiceImpl httpQueryServiceImpl;
    
    @Override
    public Object busExecute(X5437BO x5437Bo) throws Exception {
        String externalIdentificationNo = x5437Bo.getExternalIdentificationNo();
        CoreMediaBasicInfo coreMedia = this.queryCustomerMedia(externalIdentificationNo);
        String customerNo = coreMedia.getMainCustomerNo();
        String operationMode = coreMedia.getOperationMode();
        String productObjectCode = coreMedia.getProductObjectCode();
        List<CoreProductBusinessScope> productBusinessScope = httpQueryServiceImpl.queryProductBusinessScope(productObjectCode, operationMode);
        String businessProgramNo = productBusinessScope.get(0).getBusinessProgramNo();
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        CoreProductObjectBillSumSqlBuilder coreProductObjectBillSumSqlBuilder = new CoreProductObjectBillSumSqlBuilder();
        coreProductObjectBillSumSqlBuilder.andCustomerNoEqualTo(customerNo);
        if (StringUtil.isNotEmpty(x5437Bo.getCurrencyCode())) {
            coreProductObjectBillSumSqlBuilder.andCurrencyCodeEqualTo(x5437Bo.getCurrencyCode());
        }
        /*if (StringUtil.isNotEmpty(x5437Bo.getBillDate())) {
            String dateString = DateUtil.format(DateUtil.parse(x5437Bo.getBillDate(), "yyyy-MM"), "yyyy-MM");
            coreProductObjectBillSumSqlBuilder.andBillDateLikeRigth(dateString);
        }*/
        coreProductObjectBillSumSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        coreProductObjectBillSumSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        coreProductObjectBillSumSqlBuilder.andCurrentCycleNumberEqualTo(coreCustomerBillDay.getCurrentCycleNumber()-1);
        PageBean<X5437VO> page = new PageBean<>();
        if (null != x5437Bo.getPageSize() && null != x5437Bo.getIndexNo()) {
            coreProductObjectBillSumSqlBuilder.setPageSize(x5437Bo.getPageSize());
            coreProductObjectBillSumSqlBuilder.setIndexNo(x5437Bo.getIndexNo());
            page.setPageSize(x5437Bo.getPageSize());
            page.setIndexNo(x5437Bo.getIndexNo());
        }
        int totalCount = coreProductObjectBillSumDao.countBySqlBuilder(coreProductObjectBillSumSqlBuilder);
        page.setTotalCount(totalCount);

        if (totalCount > 0) {
        	Integer decimalPlaces = null;
        	List<X5437VO> x5437VOList = new ArrayList<X5437VO>();
        	coreProductObjectBillSumSqlBuilder.orderByCurrentCycleNumber(false);
            List<CoreProductObjectBillSum> coreProductObjectBillSumList = coreProductObjectBillSumDao
                    .selectListBySqlBuilder(coreProductObjectBillSumSqlBuilder);
            Iterator<CoreProductObjectBillSum> it = coreProductObjectBillSumList.iterator();
            CoreProductObjectBillSum coreProductObjectBillSum = null;
            while(it.hasNext()){
            	coreProductObjectBillSum = it.next();
            	// 金额转换
            	if(decimalPlaces == null){
            		CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreProductObjectBillSum.getCurrencyCode());
            		decimalPlaces = coreCurrency.getDecimalPosition();
            	}
            	amountConversion(coreProductObjectBillSum, decimalPlaces);
            	X5437VO x5437VO = new X5437VO();
            	CachedBeanCopy.copyProperties(coreProductObjectBillSum, x5437VO);
            	List<CoreCustomerDelqStatic> staticList1 = getCoreCustomerDelqStaticList(coreProductObjectBillSum,true);
            	for(CoreCustomerDelqStatic  coreCustomerDelqStatic:staticList1){
            		if(coreCustomerDelqStatic.getCurrCyclePaymentMin()!=null){
            			BigDecimal currCyclePaymentMin = CurrencyConversionUtil.reduce(coreCustomerDelqStatic.getCurrCyclePaymentMin(),
            					decimalPlaces);
            			coreCustomerDelqStatic.setCurrCyclePaymentMin(currCyclePaymentMin);
            		}
            	}
            	BigDecimal minRepayAmt = getMinRepayAmt(staticList1);
            	List<CoreCustomerDelqStatic> staticList3 = getCoreCustomerDelqStaticList(coreProductObjectBillSum,false);
            	if (StringUtil.isNotBlank(coreProductObjectBillSum.getProductObjectCode())) {
            		CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,coreProductObjectBillSum.getProductObjectCode());
            		if (coreProductObject !=null) {
            			x5437VO.setProductDesc(coreProductObject.getProductDesc());
            		}
            	}
            	if (StringUtil.isNotBlank(coreProductObjectBillSum.getCustomerNo())) {
            		CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,coreProductObjectBillSum.getBusinessProgramNo());
            		if (coreBusinessProgram !=null) {
            			x5437VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
            		}
            	}           	
            	x5437VO.setMinRepayAmt(minRepayAmt);
            	x5437VOList.add(x5437VO);
            }
            page.setRows(x5437VOList);
        }
        return page;
    }
    
    
    private BigDecimal getMinRepayAmt(List<CoreCustomerDelqStatic> list){
    	BigDecimal minRepayAmt = BigDecimal.ZERO;
    	if(list!=null&&list.size()>0){
    		for(CoreCustomerDelqStatic coreCustomerDelqStatic : list){
    			minRepayAmt = minRepayAmt.add(coreCustomerDelqStatic.getCurrCyclePaymentMin());
    		}
    	}
    	return minRepayAmt;
    }
    
    private  List<CoreCustomerDelqStatic> getCoreCustomerDelqStaticList(CoreProductObjectBillSum coreProductObjectBillSum,boolean flag) throws Exception{
    	CoreCustomerDelqStaticSqlBuilder coreCustomerDelqStaticSqlBuilder = new CoreCustomerDelqStaticSqlBuilder();
    	coreCustomerDelqStaticSqlBuilder.andCustomerNoEqualTo(coreProductObjectBillSum.getCustomerNo());
    	coreCustomerDelqStaticSqlBuilder.andCurrencyCodeEqualTo(coreProductObjectBillSum.getCurrencyCode());
    	String productObjectCode = coreProductObjectBillSum.getProductObjectCode();
    	if(StringUtil.isBlank(productObjectCode)||"0".equals(productObjectCode)){
    	coreCustomerDelqStaticSqlBuilder.andDelinquencyLevelEqualTo("G");
    	}
    	else{
    		coreCustomerDelqStaticSqlBuilder.andDelinquencyLevelEqualTo("P");
    		coreCustomerDelqStaticSqlBuilder.andProductObjectNoEqualTo(productObjectCode);
    	}
    	if (flag) {
    		coreCustomerDelqStaticSqlBuilder.andCreateCycleNoEqualTo(9999);
		}else{
			coreCustomerDelqStaticSqlBuilder.andCreateCycleNoNotEqualTo(9999).andCreateCycleNoNotEqualTo(9998);
		}
    	List<CoreCustomerDelqStatic> lists = coreCustomerDelqStaticDao.selectListBySqlBuilder(coreCustomerDelqStaticSqlBuilder);
    	return lists;
    }

    private void amountConversion(CoreProductObjectBillSum coreProductObjectBillSum, Integer decimalPlaces)
            throws Exception {
        if(coreProductObjectBillSum.getAllGraceBalance()!=null){
        BigDecimal allGraceBalance = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getAllGraceBalance(),
                decimalPlaces);
        coreProductObjectBillSum.setAllGraceBalance(allGraceBalance);
        }
        if(coreProductObjectBillSum.getCreditAmount()!=null){
        BigDecimal creditAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getCreditAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setCreditAmount(creditAmount);
        }
        if(coreProductObjectBillSum.getDebitAmount()!=null){
        BigDecimal debitAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getDebitAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setDebitAmount(debitAmount);
        }
        if(coreProductObjectBillSum.getCurrentRepayAmount()!=null){
        BigDecimal currentRepayAmount = CurrencyConversionUtil
                .reduce(coreProductObjectBillSum.getCurrentRepayAmount(), decimalPlaces);
        coreProductObjectBillSum.setCurrentRepayAmount(currentRepayAmount);
        }
        if(coreProductObjectBillSum.getPostingAmount()!=null){
        BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPostingAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setPostingAmount(postingAmount);
        }
        if(coreProductObjectBillSum.getPrincipalAmount()!=null){
        BigDecimal principalAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getPrincipalAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setPrincipalAmount(principalAmount);
        }
        if(coreProductObjectBillSum.getFeeAmount()!=null){
        BigDecimal feeAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getFeeAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setFeeAmount(feeAmount);
        }
        if(coreProductObjectBillSum.getInterestAmount()!=null){
        BigDecimal interestAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getInterestAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setInterestAmount(interestAmount);
        }
        if(coreProductObjectBillSum.getCurrentAccountBalance()!=null){
        BigDecimal currentAccountBalance = CurrencyConversionUtil
                .reduce(coreProductObjectBillSum.getCurrentAccountBalance(), decimalPlaces);
        coreProductObjectBillSum.setCurrentAccountBalance(currentAccountBalance);
        }
        if(coreProductObjectBillSum.getCurrCyclePaymentMin()!=null){
        BigDecimal currCyclePaymentMin = CurrencyConversionUtil
                .reduce(coreProductObjectBillSum.getCurrCyclePaymentMin(), decimalPlaces);
        coreProductObjectBillSum.setCurrCyclePaymentMin(currCyclePaymentMin);
        }
        if(coreProductObjectBillSum.getDueAmount()!=null){
        BigDecimal dueAmount = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getDueAmount(),
                decimalPlaces);
        coreProductObjectBillSum.setDueAmount(dueAmount);
        }
        if(coreProductObjectBillSum.getBeginBalance()!=null){
        BigDecimal beginBalance = CurrencyConversionUtil.reduce(coreProductObjectBillSum.getBeginBalance(),
                decimalPlaces);
        coreProductObjectBillSum.setBeginBalance(beginBalance);
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
    
    /**
     * 查询客户下所有有效媒介
     * 
     * @param externalIdentificationNo
     * @return
     * @throws Exception
     */
    private CoreMediaBasicInfo queryCustomerMedia(String externalIdentificationNo) throws Exception {
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(Constant.MEDIA_INVALID_FLAG);
        coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if (null == coreMediaBasicInfo) {
            throw new BusinessException("COR-00001");
        }
        return coreMediaBasicInfo;
    }
}
