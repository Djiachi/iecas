package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.ider.bus.X5500Bus;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreAccountBalanceObjectDao;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceObject;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceObjectSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5500BO;
import com.tansun.ider.model.vo.X5500VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 账户余额对象查询
 * @author: lianhuan
 */
@Service
public class X5500BusImpl implements X5500Bus {

    @Autowired
    private CoreAccountBalanceObjectDao coreAccountBalanceObjectDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;

    @Override
    public Object busExecute(X5500BO x5500bo) throws Exception {

        String entrys = Constant.EMPTY_LIST;
        PageBean<X5500VO> page = new PageBean<>();
        // 身份证号
        String accountId = x5500bo.getAccountId();
        // 外部识别号
        String currencyCode = x5500bo.getCurrencyCode();

        CoreAccountBalanceObjectSqlBuilder coreAccountBalanceObjectSqlBuilder = new CoreAccountBalanceObjectSqlBuilder();
        coreAccountBalanceObjectSqlBuilder.andAccountIdEqualTo(accountId);
        coreAccountBalanceObjectSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        int totalCount = coreAccountBalanceObjectDao.countBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5500bo.getPageSize() && null != x5500bo.getIndexNo()) {
            coreAccountBalanceObjectSqlBuilder.setIndexNo(x5500bo.getIndexNo());
            coreAccountBalanceObjectSqlBuilder.setPageSize(x5500bo.getPageSize());
            page.setPageSize(x5500bo.getPageSize());
            page.setIndexNo(x5500bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<X5500VO> x5500vos = new ArrayList<X5500VO>();
            coreAccountBalanceObjectSqlBuilder.orderByAccountId(false);
            List<CoreAccountBalanceObject> listCoreAccount = coreAccountBalanceObjectDao
                    .selectListBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
            for (CoreAccountBalanceObject coreAccountBalanceObject : listCoreAccount) {
                X5500VO x5500vo = new X5500VO();
                CachedBeanCopy.copyProperties(coreAccountBalanceObject, x5500vo);
                // 金额转换
                amountConversion(coreAccountBalanceObject, currencyCode,x5500vo);
                String balanceObjectCode = x5500vo.getBalanceObjectCode();
                String balanceObjectDesc = queryBalObjDesc(x5500bo.getOperationMode(), balanceObjectCode);
                x5500vo.setBalanceObjectDesc(balanceObjectDesc);
                x5500vos.add(x5500vo);
            }
            page.setRows(x5500vos);
            if (null != listCoreAccount && !listCoreAccount.isEmpty()) {
                entrys = listCoreAccount.get(0).getId();
            }
            // 记录查询日志
            CoreEvent tempObject = new CoreEvent();
            paramsUtil.logNonInsert(x5500bo.getCoreEventActivityRel().getEventNo(),
                    x5500bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                    x5500bo.getOperatorId());
        }
        return page;
    }

    /**
     * 页面需要返显余额对象描述
     * 
     * @param balanceObjectCode
     * @return
     * @throws Exception
     */
    private String queryBalObjDesc(String operationMode, String balanceObjectCode) throws Exception {
        CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, balanceObjectCode);
        if (coreBalanceObject == null) {
            throw new BusinessException("PARAM-00002", "运营模式" + operationMode + "下的余额对象" + balanceObjectCode);
        }
        return coreBalanceObject.getObjectDesc();
    }

    /**
     * 金额转换:从数据库中查出来后需要缩小
     * 
     * @param coreAccountBalanceObject
     * @param currencyCode
     * @throws Exception
     */
    private void amountConversion(CoreAccountBalanceObject coreAccountBalanceObject, String currencyCode,X5500VO x5500vo)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if (coreAccountBalanceObject.getBalance() != null) {
            BigDecimal balance = CurrencyConversionUtil.reduce(coreAccountBalanceObject.getBalance(), decimalPlaces);
            coreAccountBalanceObject.setBalance(balance);
        }
        if (coreAccountBalanceObject.getAccumulatedInterest() != null) {
            BigDecimal accumulatedInterest = CurrencyConversionUtil
                    .reduce(coreAccountBalanceObject.getAccumulatedInterest(), decimalPlaces);
            coreAccountBalanceObject.setAccumulatedInterest(accumulatedInterest);
        }
        if (coreAccountBalanceObject.getCurrentDue() != null) {
            BigDecimal currentDue = CurrencyConversionUtil.reduce(coreAccountBalanceObject.getCurrentDue(),
                    decimalPlaces);
            coreAccountBalanceObject.setCurrentDue(currentDue);
        }
        if(null != coreCurrency){
        	x5500vo.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }

    }
}
