package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5600Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountBalanceObjectDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerDelinquencyDao;
import com.tansun.ider.dao.issue.CoreCustomerUnifyInfoDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceObject;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerDelinquency;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceObjectSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerDelinquencySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.BillAmountMapper;
import com.tansun.ider.enums.TransState;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.MapBean;
import com.tansun.ider.model.bo.X5600BO;
import com.tansun.ider.model.vo.X5600VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;

/**
 * 分期金额统计
 *
 * @author huangyayun 2018年10月30日
 */
@Service
public class X5600BusImpl implements X5600Bus {
    @Resource
    private CoreCustomerBillDayDao coreCustomerBillDayDao;
    @Resource
    private BillAmountMapper billAmountMapper;
    @Resource
    private CoreCustomerDelinquencyDao coreCustomerDelinquencyDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Resource
    private CoreAccountBalanceObjectDao coreAccountBalanceObjectDao;
    @Resource
    private CoreCustomerUnifyInfoDao coreCustomerUnifyInfoDao;
    @Resource
    private CoreAccountDao coreAccountDao;
    @Resource
    private CoreTransHistDao coreTransHistDao;
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CommonInterfaceForArtService artService;

    @Override
    public Object busExecute(X5600BO x5600bo) throws Exception {
        Integer cycleNumber = null;
        BigDecimal postingAmount = BigDecimal.ZERO;
        BigDecimal installmentAmount =  BigDecimal.ZERO;
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(x5600bo.getCustomerNo());
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (StringUtil.isNotBlank(x5600bo.getAccountId())) {
            CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
            coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(x5600bo.getEcommOrigGlobalSerialNumbr());
            coreTransHistSqlBuilder.andActivityNoEqualTo("X8010");
            coreTransHistSqlBuilder.andBalanceTypeEqualTo("P");
            List<CoreTransHist> coreTransHistList = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
            if (coreTransHistList != null && coreTransHistList.size() > 1) {
                cycleNumber = coreTransHistList.get(0).getCycleNumber();
                if (!(TransState.STATE_NOR.getValue().equals(coreTransHistList.get(0).getTransState()))
                		&&!(TransState.STATE_PIN.getValue().equals(coreTransHistList.get(0).getTransState()))) {
                    throw new BusinessException("CUS-00052");
                }
                x5600bo.setBusinessTypeCode(coreTransHistList.get(0).getBusinessTypeCode());
            }
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andAccountIdEqualTo(x5600bo.getAccountId());
            coreAccountSqlBuilder.andCurrencyCodeEqualTo(x5600bo.getPostingCurrencyCode());
            CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
            x5600bo.setBusinessProgramNo(coreAccount.getBusinessProgramNo());
            installmentAmount = coreTransHistList.get(0).getInstallmentAmount()==null?BigDecimal.ZERO:coreTransHistList.get(0).getInstallmentAmount();
            postingAmount = coreTransHistList.get(0).getPostingAmount().subtract(installmentAmount);
            if(BigDecimal.ZERO.compareTo(postingAmount)<0){
            CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5600bo.getPostingCurrencyCode());
            int decimalPlaces = coreCurrency.getDecimalPosition();
            postingAmount = CurrencyConversionUtil.reduce(postingAmount, decimalPlaces);
            if(BigDecimal.ZERO.compareTo(installmentAmount)<0){
            installmentAmount = CurrencyConversionUtil.reduce(installmentAmount, decimalPlaces);
            }
            }
            else{
            	// 该客户可分期金额为0
                throw new BusinessException("COR-12131");
            }
        }
        CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
        coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(x5600bo.getBusinessProgramNo());
        coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(x5600bo.getCustomerNo());
        CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        CoreCustomerDelinquencySqlBuilder coreCustomerDelinquencySqlBuilder = new CoreCustomerDelinquencySqlBuilder();
        coreCustomerDelinquencySqlBuilder.andCustomerNoEqualTo(x5600bo.getCustomerNo());
        coreCustomerDelinquencySqlBuilder.andCurrCyclePaymentMinGreaterThan(BigDecimal.ZERO);
        coreCustomerDelinquencySqlBuilder.andCycleNoLessThan(coreCustomerBillDay.getCurrentCycleNumber() - 1);
        List<CoreCustomerDelinquency> lists = coreCustomerDelinquencyDao.selectListBySqlBuilder(coreCustomerDelinquencySqlBuilder);
        X5600VO x5600VO = new X5600VO();
        x5600VO.setCustomerNo(x5600bo.getCustomerNo());
        if (lists != null && lists.size() > 0) {
            x5600VO.setBillAmt(BigDecimal.ZERO);
            // 该客户已逾期，不能分期
            throw new BusinessException("CUS-00049");
        }
        else if (StringUtil.isNotBlank(x5600bo.getAccountId())) {
        	EventCommArea eventCommArea = new EventCommArea();
        	eventCommArea.setEcommOperMode(coreCustomer.getOperationMode());
        	eventCommArea.setEcommBusineseType(x5600bo.getBusinessTypeCode());
            CoreAccountBalanceObjectSqlBuilder coreAccountBalanceObjectSqlBuilder = new CoreAccountBalanceObjectSqlBuilder();
            coreAccountBalanceObjectSqlBuilder.andAccountIdEqualTo(x5600bo.getAccountId());
            coreAccountBalanceObjectSqlBuilder.andCurrencyCodeEqualTo(x5600bo.getPostingCurrencyCode());
            coreAccountBalanceObjectSqlBuilder.andBalanceObjectCodeEqualTo(x5600bo.getBalanceObjectCode());
            CoreAccountBalanceObject coreAccountBalanceObject =
                    coreAccountBalanceObjectDao.selectBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
            eventCommArea.setEcommBcoCode(coreAccountBalanceObject.getBalanceObjectCode());
   		 	Map<String, String> resultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_810, eventCommArea);
   		 	MapBean checkLoanAmtmapBean = handleMap(resultMap);
   	        // 不可分期
   	        if (Constant.PERMIT_INSTALL_FLAG.equals(checkLoanAmtmapBean.getKey())) {
            //if (coreAccountBalanceObject != null && "1".equals(coreAccountBalanceObject.getInstallmentFlag())) {
                /*
                 * CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
                 * coreCustomerUnifyInfoSqlBuilder.andBusinessProgramNoEqualTo(coreCustomerBillDay.getBusinessProgramNo());
                 * coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(x5600bo.getCustomerNo());
                 * coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(coreCustomerBillDay.getCurrentCycleNumber()-1);
                 * CoreCustomerUnifyInfo coreCustomerUnifyInfo =
                 * coreCustomerUnifyInfoDao.selectBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
                 */
                // if(coreCustomerUnifyInfo.getStatementDate().compareTo(x5600bo.getOccurrDate())<0){
                if (cycleNumber == coreCustomerBillDay.getCurrentCycleNumber()) {
                    x5600VO.setBillAmt(postingAmount);
                    x5600VO.setInstallmentAmount(installmentAmount);
                }
                else {
                    // 当期交易才能分期
                    x5600VO.setBillAmt(BigDecimal.ZERO);
                    throw new BusinessException("CUS-00251");
                }

            }
            else {
                x5600VO.setBillAmt(BigDecimal.ZERO);
                throw new BusinessException("CUS-00050");
            }
        }
        else {
            x5600bo.setFirstBillingCycle(coreCustomerBillDay.getCurrentCycleNumber());
            List<X5600VO> x5600VONews = billAmountMapper.selectSumAmt(x5600bo);
            /**
             * 这个selectSumAmt sql查询中有一个字段是“installment_flag”是否可分期，对于不可分期的 x5600VONew为null
             * 有的业务类型不能做分期
             */
            if (null != x5600VONews && x5600VONews.size()>0) {
            	x5600VO = x5600VONews.get(0);
            	//x5600VO.setBillAmt(BigDecimal.ZERO);
            	String currencyCode = x5600VONews.get(0).getCurrencyCode();
                CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
                int decimalPlaces = coreCurrency.getDecimalPosition();
                BigDecimal billAmt = getBillAmt(x5600VONews,coreCustomer.getOperationMode(),x5600bo.getBusinessTypeCode());
                if(billAmt.compareTo(BigDecimal.ZERO)>0){
                	billAmt = CurrencyConversionUtil.reduce(billAmt, decimalPlaces);
                }
                    /**
                     * 1.在账单分期，增加一个判断，非本期账单的最大可分期金额为0
                     * 2.上期账单如果已经过了还款日做账单分期，最大可分期金额为0
                     * 
                     * @author mafuqiang
                     */
                    CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
                    coreCustomerUnifyInfoSqlBuilder.andBusinessProgramNoEqualTo(coreCustomerBillDay.getBusinessProgramNo());
                    coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(x5600bo.getCustomerNo());
                    coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(coreCustomerBillDay.getCurrentCycleNumber() - 1);
                    CoreCustomerUnifyInfo coreCustomerUnifyInfo =
                    coreCustomerUnifyInfoDao.selectBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
                    CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
                    if (x5600bo.getCurrentCycleNumber() == (coreCustomerBillDay.getCurrentCycleNumber() - 1)) {
                        if (coreCustomerUnifyInfo.getGraceDate().compareTo(coreSystemUnit.getNextProcessDate()) >= 0) {
                            x5600VO.setBillAmt(billAmt);
                        }
                        else {
                            throw new BusinessException("CUS-00128");
                        }
                    }
                    else {
                        throw new BusinessException("CUS-00127");
                    }

                    x5600VO.setCurrencyCode(x5600VONews.get(0).getCurrencyCode());
                    if (null != coreCurrency) {
                        x5600VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                //}
            }
            else {
                throw new BusinessException("CUS-00130");
            }
        }

        x5600VO.setCustomerNo(x5600bo.getCustomerNo());
        return x5600VO;

    }
    
    
    /**
     * 处理金额
     * 
     * @param artifactNo
     * @param eventCommArea
     * @param artifactList
     * @return
     * @throws Exception
     */
    public BigDecimal getBillAmt(List<X5600VO> x5600VOs,String ecommOperMode,String businessTypeCode) throws Exception{
    	EventCommArea eventCommArea = new EventCommArea();
    	eventCommArea.setEcommOperMode(ecommOperMode);
    	eventCommArea.setEcommBusineseType(businessTypeCode);
    	BigDecimal billAmt = BigDecimal.ZERO;
    	for(X5600VO x5600VO : x5600VOs){
    		 eventCommArea.setEcommBcoCode(x5600VO.getBalanceObjectCode());
    		 Map<String, String> resultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_810, eventCommArea);
    		 MapBean checkLoanAmtmapBean = handleMap(resultMap);
    	        // 不可分期
    	        if (Constant.PERMIT_INSTALL_FLAG.equals(checkLoanAmtmapBean.getKey())) {
    	        	billAmt = billAmt.add(x5600VO.getBillAmt()==null?BigDecimal.ZERO:x5600VO.getBillAmt());
    	        }
    	}
    	return billAmt;
    }
    
    /**
     * 处理元件构件
     *
     * @param para
     * @throws Exception
     */
    public static MapBean handleMap(Map<String, String> paramMap, String... match) {
        Iterator<Map.Entry<String, String>> paramMapIterator = paramMap.entrySet().iterator();
        MapBean mapBean = new MapBean();
        while (paramMapIterator.hasNext()) {
            Map.Entry<String, String> entry = paramMapIterator.next();
            if (match != null && match.length > 0) {
                for (int i = 0; i < match.length; i++) {
                    if (match[i].equals(entry.getKey().split("_")[0])) {
                        mapBean.setKey(entry.getKey().split("_")[0]);
                        mapBean.setValue(entry.getValue());
                        break;
                    }
                }
            }
            else {
                mapBean.setKey(entry.getKey().split("_")[0]);
                mapBean.setValue(entry.getValue());
            }
        }
        if (StringUtil.isBlank(mapBean.getKey()) || StringUtil.isEmpty(mapBean.getKey())) {
            throw new BusinessException("COR-10003");
        }
        return mapBean;
    }
    
}
