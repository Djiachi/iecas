package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5970Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreOccurrAmtNodeRelDao;
import com.tansun.ider.dao.issue.CoreOccurrAmtTransRelDao;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreOccurrAmtNodeRel;
import com.tansun.ider.dao.issue.entity.CoreOccurrAmtTransRel;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOccurrAmtNodeRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreOccurrAmtTransRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5970BO;
import com.tansun.ider.model.vo.X5435VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 交易级分配情况
 * @author qianyp
 */

@Service
public class X5970BusImpl implements X5970Bus {
    
    @Autowired
    private CoreTransHistDao coreTransHistDao;
    @Autowired
    private CoreOccurrAmtTransRelDao coreOccurrAmtTransRelDao;
    @Autowired
    private CoreOccurrAmtNodeRelDao coreOccurrAmtNodeRelDao;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private HttpQueryService httpQueryService;
    
    @Override
    public Object busExecute(X5970BO x5970bo) throws Exception {
        PageBean<X5435VO> page = new PageBean<>();
        String entrys = Constant.EMPTY_LIST;
        String balanceUnitCode = x5970bo.getBalanceUnitCode();
        String globalTransSerialNo = x5970bo.getGlobalTransSerialNo();
        CoreOccurrAmtTransRelSqlBuilder coreOccurrAmtTransRelSqlBuilder = new CoreOccurrAmtTransRelSqlBuilder();
        coreOccurrAmtTransRelSqlBuilder.andBalanceUnitCodeEqualTo(balanceUnitCode);
        coreOccurrAmtTransRelSqlBuilder.andGlobalTransSerialNoEqualTo(globalTransSerialNo);
        List<CoreOccurrAmtTransRel> occurrAmtTransRels = coreOccurrAmtTransRelDao.selectListBySqlBuilder(coreOccurrAmtTransRelSqlBuilder);
        List<CoreOccurrAmtNodeRel> occurrAmtNodeRels = new ArrayList<CoreOccurrAmtNodeRel>();
        for(CoreOccurrAmtTransRel occurrAmtTransRel : occurrAmtTransRels){
            List<CoreOccurrAmtNodeRel> list = coreOccurrAmtNodeRelDao.selectListBySqlBuilder(new CoreOccurrAmtNodeRelSqlBuilder()
                     .andBalanceUnitCodeEqualTo(occurrAmtTransRel.getBalanceUnitCode()).andRelSerialNumberEqualTo(occurrAmtTransRel.getSerialNumber()));
            if(list!=null && list.size()>0){
                occurrAmtNodeRels.addAll(list);
            }
        }
        List<CoreOccurrAmtTransRel> occurrAmtTransRelList = new ArrayList<CoreOccurrAmtTransRel>();
        if(occurrAmtNodeRels!=null && occurrAmtNodeRels.size()>0){
            for(CoreOccurrAmtNodeRel occurrAmtNodeRel : occurrAmtNodeRels){
                List<CoreOccurrAmtTransRel> list = coreOccurrAmtTransRelDao.selectListBySqlBuilder(new CoreOccurrAmtTransRelSqlBuilder()
                        .andBalanceUnitCodeEqualTo(occurrAmtNodeRel.getBalanceUnitCode()).andSerialNumberEqualTo(occurrAmtNodeRel.getSerialNumber()));
                if(list!=null && list.size()>0){
                    occurrAmtTransRelList.addAll(list);
                }
            }
        }
        CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
        coreTransHistSqlBuilder.andActivityNoEqualTo("X8010");
        coreTransHistSqlBuilder.andLogLevelEqualTo("A");
        coreTransHistSqlBuilder.andTransPropertyEqualTo("O");
        int totalCount = 0;
        if(occurrAmtTransRelList!=null && occurrAmtTransRelList.size()>0){
            CoreTransHistSqlBuilder coreTransHistSqlBuilderTemp = new CoreTransHistSqlBuilder();
            for(CoreOccurrAmtTransRel tran : occurrAmtTransRelList){
                coreTransHistSqlBuilderTemp.orGlobalSerialNumbrEqualTo(tran.getGlobalTransSerialNo());
            } 
            coreTransHistSqlBuilder.and(coreTransHistSqlBuilderTemp);
            totalCount = coreTransHistDao.countBySqlBuilder(coreTransHistSqlBuilder);
            page.setTotalCount(totalCount);
        }
        if (null != x5970bo.getPageSize() && null != x5970bo.getIndexNo()) {
            coreTransHistSqlBuilder.orderByOccurrDate(false);
            coreTransHistSqlBuilder.setPageSize(x5970bo.getPageSize());
            coreTransHistSqlBuilder.setIndexNo(x5970bo.getIndexNo());
            page.setPageSize(x5970bo.getPageSize());
            page.setIndexNo(x5970bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreTransHist> list = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
            List<X5435VO> listVO = new ArrayList<X5435VO>();
            X5435VO x5435VO = null;
            for (CoreTransHist coreTransHist : list) {
                x5435VO = new X5435VO();
                // 金额转换
                CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
                coreCustomerSqlBuilder.andCustomerNoEqualTo(coreTransHist.getCustomerNo());
                CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
                if (coreCustomer != null) {
                    String operationMode = coreCustomer.getOperationMode();
                    CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
                    amountConversion(coreTransHist, coreOperationMode.getAccountCurrency());
                }
                // 获取币种描述
                if (StringUtil.isNotEmpty(coreTransHist.getPostingCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getPostingCurrencyCode());
                    if (null != coreCurrency) {
                        x5435VO.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                CachedBeanCopy.copyProperties(coreTransHist, x5435VO);
                listVO.add(x5435VO);
            }
            page.setRows(listVO);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5970bo.getCoreEventActivityRel().getEventNo(),
                x5970bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5970bo.getOperatorId());
        return page;
    }
    
    private void amountConversion(CoreTransHist coreTransHist, String accountCurrency) throws Exception {
        if (coreTransHist.getTransAmount() != null && !BigDecimal.ZERO.equals(coreTransHist.getTransAmount())) {
            Integer transCurrPoint = coreTransHist.getTransCurrPoint();
            if (transCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getTransCurrCde())) {
                    queryCurrencyCode = coreTransHist.getTransCurrCde();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency transCurrCde = httpQueryService.queryCurrency(queryCurrencyCode);
                transCurrPoint = transCurrCde.getDecimalPosition();
            }
            BigDecimal transAmount = CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(), transCurrPoint);
            coreTransHist.setTransAmount(transAmount);
        }

        if (coreTransHist.getPostingAmount() != null && !BigDecimal.ZERO.equals(coreTransHist.getPostingAmount())) {
            Integer postingCurrencyPoint = coreTransHist.getPostingCurrencyPoint();
            if (postingCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getPostingCurrencyCode())) {
                    queryCurrencyCode = coreTransHist.getPostingCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency postingCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                postingCurrencyPoint = postingCurrencyCode.getDecimalPosition();
            }
            BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreTransHist.getPostingAmount(),
                    postingCurrencyPoint);
            coreTransHist.setPostingAmount(postingAmount);
        }

        if (coreTransHist.getSettlementAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getSettlementAmount())) {
            Integer settlementCurrencyPoint = coreTransHist.getSettlementCurrencyPoint();
            if (settlementCurrencyPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getSettlementCurrencyCode())) {
                    queryCurrencyCode = coreTransHist.getSettlementCurrencyCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settlementCurrencyCode = httpQueryService.queryCurrency(queryCurrencyCode);
                settlementCurrencyPoint = settlementCurrencyCode.getDecimalPosition();
            }
            BigDecimal settlementAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettlementAmount(),
                    settlementCurrencyPoint);
            coreTransHist.setSettlementAmount(settlementAmount);
        }

        if (coreTransHist.getActualPostingAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getActualPostingAmount())
                && StringUtil.isNotBlank(coreTransHist.getCurrencyCode())) {
            CoreCurrency currencyCode = httpQueryService.queryCurrency(coreTransHist.getCurrencyCode());
            int currencyCodePoint = currencyCode.getDecimalPosition();
            BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(),
                    currencyCodePoint);
            coreTransHist.setActualPostingAmount(actualPostingAmount);
        }

        if (coreTransHist.getSettleDistriAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getSettleDistriAmount())) {
            Integer settleDistriCurrPoint = coreTransHist.getSettleDistriCurrPoint();
            if (settleDistriCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getSettleDistriCurrency())) {
                    queryCurrencyCode = coreTransHist.getSettleDistriCurrency();
                } else {
                    queryCurrencyCode = accountCurrency;
                }
                CoreCurrency settleDistriCurrency = httpQueryService.queryCurrency(queryCurrencyCode);
                settleDistriCurrPoint = settleDistriCurrency.getDecimalPosition();
            }
            BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettleDistriAmount(),
                    settleDistriCurrPoint);
            coreTransHist.setSettleDistriAmount(settleDistriAmount);
        }

        if (coreTransHist.getOverpayFrzAmount() != null
                && !BigDecimal.ZERO.equals(coreTransHist.getOverpayFrzAmount())) {
            Integer overpayFrzCurrPoint = coreTransHist.getOverpayFrzCurrPoint();
            if (overpayFrzCurrPoint == null) {
                String queryCurrencyCode = "";
                if (StringUtil.isNotBlank(coreTransHist.getOverpayFrzCurrCode())) {
                    queryCurrencyCode = coreTransHist.getOverpayFrzCurrCode();
                } else {
                    queryCurrencyCode = accountCurrency;
                }

                CoreCurrency overpayFrzCurrCode = httpQueryService.queryCurrency(queryCurrencyCode);
                overpayFrzCurrPoint = overpayFrzCurrCode.getDecimalPosition();
            }
            BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayFrzAmount(),
                    overpayFrzCurrPoint);
            coreTransHist.setOverpayFrzAmount(overpayFrzAmount);
        }

    }

}
