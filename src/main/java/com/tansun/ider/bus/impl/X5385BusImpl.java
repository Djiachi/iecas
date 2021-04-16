package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5385Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountCycleFiciDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreBusinessTypeBillSumDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDelqStaticDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreProductObjectBillSumDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelqStatic;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProductObjectBillSum;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelqStaticSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductObjectBillSumSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5385BO;
import com.tansun.ider.model.vo.X5385VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.util.CachedBeanCopy;

//import oracle.net.aso.q;


/**
 * 账单产品对象维度查询
 * 
 * @author huangyayun
 * @date 2018年8月13日
 */
@Service
public class X5385BusImpl implements X5385Bus {
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
//    @Resource
//    private CoreCustomerDao coreCustomerDao;// 客户号
//    @Resource
//    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;// 媒介基本信息
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    CoreCustomerDelqStaticDao coreCustomerDelqStaticDao;
    
    @Override
    public Object busExecute(X5385BO x5385Bo) throws Exception {

        // CoreBusinessTypeBillSumSqlBuilder
        // coreBusinessTypeBillSumSqlBuilder = new
        // CoreBusinessTypeBillSumSqlBuilder();
        CoreProductObjectBillSumSqlBuilder coreProductObjectBillSumSqlBuilder = new CoreProductObjectBillSumSqlBuilder();
        String customerNo = null;
        String operationMode = null;
       // List<CoreProductBusinessScope> coreProductBusinessScopes = null;
        String externalIdentificationNo = x5385Bo.getExternalIdentificationNo();
        Object object = queryCustomerService.queryCustomer(x5385Bo.getIdType(), x5385Bo.getIdNumber(), externalIdentificationNo);
        if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
			operationMode = coreMediaBasicInfo.getOperationMode();
		}
        
//        if (StringUtil.isNotEmpty(x5385Bo.getIdNumber())&&StringUtil.isNotEmpty(x5385Bo.getIdType())) {
//        	CoreCustomer coreCustomer = queryCustomerService.queryCoreCustomer(x5385Bo.getIdType(), x5385Bo.getIdNumber());
//            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//            coreCustomerSqlBuilder.
//            // andIdTypeEqualTo(x5385Bo.getIdType()).
//                    andIdNumberEqualTo(x5385Bo.getIdNumber());
//            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
//            if (coreCustomer != null) {
//                customerNo = coreCustomer.getCustomerNo();
//                operationMode = coreCustomer.getOperationMode();
//            }
//        	customerNo = coreCustomer.getCustomerNo();
//        	operationMode = coreCustomer.getOperationMode();
//        }
//        if (StringUtil.isNotBlank(externalIdentificationNo)) {
//        	CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
//            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//            coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//            CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
//                    .selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//            if (coreMediaBasicInfo != null) {
//                customerNo = coreMediaBasicInfo.getMainCustomerNo();
//                operationMode = coreMediaBasicInfo.getOperationMode();
//               // productObjectCode = coreMediaBasicInfo.getProductObjectCode();
//               // coreMediaBasicInfo.getOperationMode();
//               /* coreProductBusinessScopes = queryCoreProductBusinessScope(productObjectCode,
//                        coreMediaBasicInfo.getOperationMode());*/
//            }
//        	customerNo = coreMediaBasicInfo.getMainCustomerNo();
//        	operationMode = coreMediaBasicInfo.getOperationMode();
//        }

        if (customerNo == null) {
            throw new BusinessException("CUS-00012", "客户号不存在");
        }
        coreProductObjectBillSumSqlBuilder.andCustomerNoEqualTo(customerNo);
        if (StringUtil.isNotEmpty(x5385Bo.getCurrencyCode())) {
            // coreBusinessTypeBillSumSqlBuilder.andCurrencyCodeEqualTo(x5385Bo.getCurrencyCode());
            coreProductObjectBillSumSqlBuilder.andCurrencyCodeEqualTo(x5385Bo.getCurrencyCode());
        }
        if (StringUtil.isNotEmpty(x5385Bo.getBillDate())) {
            String dateString = DateUtil.format(DateUtil.parse(x5385Bo.getBillDate(), "yyyy-MM"), "yyyy-MM");
            // coreBusinessTypeBillSumSqlBuilder.andCurrentCycleNumberEqualTo(x5385Bo.getCurrentCycleNumber());
            coreProductObjectBillSumSqlBuilder.andBillDateLikeRigth(dateString);
        }
        if (StringUtil.isNotEmpty(x5385Bo.getBusinessProgramNo())) {
            // coreBusinessTypeBillSumSqlBuilder.andBusinessProgramNoEqualTo(x5385Bo.getBusinessProgramNo());
            coreProductObjectBillSumSqlBuilder.andBusinessProgramNoEqualTo(x5385Bo.getBusinessProgramNo());
        } 
        /*else if (coreProductBusinessScopes != null && coreProductBusinessScopes.size() > 0) {
            CoreProductObjectBillSumSqlBuilder coreProductObjectBillSumSqlBuilderNew = new CoreProductObjectBillSumSqlBuilder();
            for (CoreProductBusinessScope coreProductBusinessScope : coreProductBusinessScopes) {
                coreProductObjectBillSumSqlBuilderNew
                        .orBusinessProgramNoEqualTo(coreProductBusinessScope.getBusinessProgramNo());
            }
            coreProductObjectBillSumSqlBuilder.and(coreProductObjectBillSumSqlBuilderNew);
        }*/
   /*     if (productObjectCode != null) {
            coreProductObjectBillSumSqlBuilder.andProductObjectCodeEqualTo(x5385Bo.getProductObjectCode());
        } else {*/
            if (StringUtil.isNotEmpty(x5385Bo.getProductObjectCode())) {
                // coreBusinessTypeBillSumSqlBuilder.andCurrencyCodeEqualTo(x5385Bo.getCurrencyCode());
                coreProductObjectBillSumSqlBuilder.andProductObjectCodeEqualTo(x5385Bo.getProductObjectCode());
            }
       // }
        PageBean<X5385VO> page = new PageBean<>();
        // List<CoreProductObjectBillSum> coreProductObjectBillSumList =
        // coreProductObjectBillSumDao.selectListBySqlBuilder(coreProductObjectBillSumSqlBuilder);
        if (null != x5385Bo.getPageSize() && null != x5385Bo.getIndexNo()) {
            coreProductObjectBillSumSqlBuilder.setPageSize(x5385Bo.getPageSize());
            coreProductObjectBillSumSqlBuilder.setIndexNo(x5385Bo.getIndexNo());
            page.setPageSize(x5385Bo.getPageSize());
            page.setIndexNo(x5385Bo.getIndexNo());
        }
        int totalCount = coreProductObjectBillSumDao.countBySqlBuilder(coreProductObjectBillSumSqlBuilder);
        page.setTotalCount(totalCount);

        if (totalCount > 0) {
        	Integer decimalPlaces = null;
        	List<X5385VO> x5385VOList = new ArrayList<X5385VO>();
        	coreProductObjectBillSumSqlBuilder.orderByCurrentCycleNumber(false);
            List<CoreProductObjectBillSum> coreProductObjectBillSumList = coreProductObjectBillSumDao
                    .selectListBySqlBuilder(coreProductObjectBillSumSqlBuilder);
//            List<CoreCustomerDelqStatic> staticList2 = new ArrayList<>();
            for (CoreProductObjectBillSum coreProductObjectBillSum : coreProductObjectBillSumList) {
                // 金额转换
            	if(decimalPlaces == null){
            		 CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreProductObjectBillSum.getCurrencyCode());
                     decimalPlaces = coreCurrency.getDecimalPosition();
            	}
                amountConversion(coreProductObjectBillSum, decimalPlaces);
            	X5385VO x5385VO = new X5385VO();
            	CachedBeanCopy.copyProperties(coreProductObjectBillSum, x5385VO);
            	List<CoreCustomerDelqStatic> staticList1 = getCoreCustomerDelqStaticList(coreProductObjectBillSum,true);
            	for(CoreCustomerDelqStatic  coreCustomerDelqStatic:staticList1){
            		 if(coreCustomerDelqStatic.getCurrCyclePaymentMin()!=null){
            		        BigDecimal currCyclePaymentMin = CurrencyConversionUtil.reduce(coreCustomerDelqStatic.getCurrCyclePaymentMin(),
            		                decimalPlaces);
            		        coreCustomerDelqStatic.setCurrCyclePaymentMin(currCyclePaymentMin);
            		        }
            	}
            	BigDecimal minRepayAmt = getMinRepayAmt(staticList1);
            /*	 if(minRepayAmt!=null){
            		 minRepayAmt = CurrencyConversionUtil.reduce(minRepayAmt,
            	                decimalPlaces);
            	 }*/
            	List<CoreCustomerDelqStatic> staticList3 = getCoreCustomerDelqStaticList(coreProductObjectBillSum,false);
            	for(CoreCustomerDelqStatic  coreCustomerDelqStatic:staticList3){
           		 if(coreCustomerDelqStatic.getCurrCyclePaymentMin()!=null){
           		        //BigDecimal currCyclePaymentMin = CurrencyConversionUtil.reduce(coreCustomerDelqStatic.getCurrCyclePaymentMin(),decimalPlaces);
           		        //coreCustomerDelqStatic.setCurrCyclePaymentMin(currCyclePaymentMin);
           		        }
            	}
            	if (StringUtil.isNotBlank(coreProductObjectBillSum.getProductObjectCode())) {
            		CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,coreProductObjectBillSum.getProductObjectCode());
                	if (coreProductObject !=null) {
                		x5385VO.setProductDesc(coreProductObject.getProductDesc());
					}
				}
            	if (StringUtil.isNotBlank(coreProductObjectBillSum.getCustomerNo())) {
            		CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,coreProductObjectBillSum.getBusinessProgramNo());
                	if (coreBusinessProgram !=null) {
                		x5385VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
					}
				}           	
//            	staticList2.addAll(staticList3);
            	x5385VO.setMinRepayAmt(minRepayAmt);
//            	x5385VO.getPage().setRows(staticList2);
            	x5385VO.getPage().setRows(staticList3);
            	x5385VOList.add(x5385VO);
            }
//            page.setObj(staticList2);
            page.setRows(x5385VOList);
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
    	//黄亚运修改20191010增加周期号
    	coreCustomerDelqStaticSqlBuilder.andCreateCycleNoEqualTo(coreProductObjectBillSum.getCurrentCycleNumber());
//    	coreCustomerDelqStaticSqlBuilder.andLevelCodeEqualTo(coreProductObjectBillSum.getBusinessProgramNo());
//    	coreCustomerDelqStaticSqlBuilder.andCreateCycleNoEqualTo(coreProductObjectBillSum.getCurrentCycleNumber());
    	String productObjectCode = coreProductObjectBillSum.getProductObjectCode();
    	if(StringUtil.isBlank(productObjectCode)||"0".equals(productObjectCode)){
    	coreCustomerDelqStaticSqlBuilder.andDelinquencyLevelEqualTo("G");
    	}
    	else{
    		coreCustomerDelqStaticSqlBuilder.andDelinquencyLevelEqualTo("P");
    		coreCustomerDelqStaticSqlBuilder.andProductObjectNoEqualTo(productObjectCode);
    	}
    	if (flag) {
    		//黄亚运修改20191010 CreateCycleNo修改为cycleNo
    		//coreCustomerDelqStaticSqlBuilder.andCreateCycleNoEqualTo(9999);
    		coreCustomerDelqStaticSqlBuilder.andCycleNoEqualTo(9999);
		}else{
			//黄亚运修改20191010 CreateCycleNo修改为cycleNo
			/*coreCustomerDelqStaticSqlBuilder.and(new CoreCustomerDelqStaticSqlBuilder().orCycleNoEqualTo(9999)
					.orCycleNoEqualTo(9998));*/
			coreCustomerDelqStaticSqlBuilder.andCycleNoEqualTo(9999);
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
}
