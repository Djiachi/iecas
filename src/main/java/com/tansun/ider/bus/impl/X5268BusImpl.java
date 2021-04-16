package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5268Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreAccountBalanceObjectDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceObject;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceObjectSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5268BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * 统一利率查询
 * @author qianyp
 */

@Service
public class X5268BusImpl implements X5268Bus {
    
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreAccountBalanceObjectDao coreAccountBalanceObjectDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;

    public Object busExecute(X5268BO x5268bo) throws Exception {
       String entrys =Constant.EMPTY_LIST;
       PageBean<CoreAccountBalanceObject> page = new PageBean<>();
       String accountId = x5268bo.getAccountId();
       String currencyCode = x5268bo.getCurrencyCode();
       CoreAccountBalanceObjectSqlBuilder coreAccountBalanceObjectSqlBuilder = new CoreAccountBalanceObjectSqlBuilder();
       if(StringUtil.isNotBlank(accountId)){
           coreAccountBalanceObjectSqlBuilder.andAccountIdEqualTo(accountId);
       }
       if(StringUtil.isNotBlank(currencyCode)){
           coreAccountBalanceObjectSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
       }
       int totalCount = coreAccountBalanceObjectDao.countBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
       if (null != x5268bo.getPageSize() && null != x5268bo.getIndexNo()) {
           coreAccountBalanceObjectSqlBuilder.setIndexNo(x5268bo.getIndexNo());
           coreAccountBalanceObjectSqlBuilder.setPageSize(x5268bo.getPageSize());
           page.setPageSize(x5268bo.getPageSize());
           page.setIndexNo(x5268bo.getIndexNo());
       }
       if (totalCount > 0) {
           //查询账户基本信息获取业务类型
           CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
           coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
           coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
           CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
           
           coreAccountBalanceObjectSqlBuilder.orderByAccountId(false);
           List<CoreAccountBalanceObject> accountBalances = new ArrayList<CoreAccountBalanceObject>();
           List<CoreAccountBalanceObject> listCoreAccount = coreAccountBalanceObjectDao.selectListBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
           for (CoreAccountBalanceObject coreAccountBalanceObject : listCoreAccount) {
               //余额对象代码
               EventCommArea eventCommArea=new EventCommArea();
               eventCommArea.setEcommOperMode(coreAccount.getOperationMode());
               eventCommArea.setEcommBcoCode(coreAccountBalanceObject.getBalanceObjectCode());
               eventCommArea.setEcommBusineseType(coreAccount.getBusinessTypeCode());
               String bcuDimension = queryBcuDimension(BSC.ARTIFACT_NO_808, eventCommArea);
               if(StringUtil.isNotBlank(bcuDimension)){
                   // 金额转换
                   amountConversion(coreAccountBalanceObject, currencyCode);
                   accountBalances.add(coreAccountBalanceObject);
               }
           }
           page.setRows(accountBalances);
           if(null != accountBalances && !accountBalances.isEmpty()){
               entrys = accountBalances.get(0).getId();
               page.setTotalCount(accountBalances.size());
           }
           //记录查询日志
           CoreEvent tempObject = new CoreEvent();
           paramsUtil.logNonInsert(x5268bo.getCoreEventActivityRel().getEventNo(), x5268bo.getCoreEventActivityRel().getActivityNo(),
                   tempObject, tempObject, entrys, x5268bo.getOperatorId());
       }
       return page;
    }
    
    /**
     * 查询808元件
     */
    public String queryBcuDimension(String artifactNo, EventCommArea eventCommArea) throws Exception {
        // 获取元件信息
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> resultMap = artService.getElementByArtifact(artifactNo, eventCommArea);
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String bcuDimension = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String[] pcdKey = key.split("_");
            String currentKey = pcdKey[0];
            if(Constant.BCU_CYCLE_RATE.equals(currentKey)){
                bcuDimension = currentKey;
            }
        }
        return bcuDimension;
    }
    
    private void amountConversion(CoreAccountBalanceObject coreAccountBalanceObject, String currencyCode)
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
    }
}
