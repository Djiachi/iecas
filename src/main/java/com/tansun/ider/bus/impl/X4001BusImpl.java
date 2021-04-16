package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X4001Bus;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X4001BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * <p> Title: X4001BusImpl </p>
 * <p> Description: 预算单位附件信息新增接口实现类</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月23日
 */
@Service
public class X4001BusImpl implements X4001Bus {
    @Autowired
    private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;

    @Override
    public Object busExecute(X4001BO x4001bo) throws Exception {
        // 事件公共公共区
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x4001bo, eventCommAreaNonFinance);
        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = new CoreBudgetOrgAddInfo();
        coreBudgetOrgAddInfo.setId(RandomUtil.getUUID());
        coreBudgetOrgAddInfo.setCustomerNo(x4001bo.getCustomerNo());
        coreBudgetOrgAddInfo.setGmtCreate(DateUtil.getDate());
        coreBudgetOrgAddInfo.setManageLevelCode(x4001bo.getManageLevelCode());
        coreBudgetOrgAddInfo.setOrgAllQuota(CurrencyConversionUtil.expand(x4001bo.getOrgAllQuota(), 2));
        coreBudgetOrgAddInfo.setOrgRestQuota(CurrencyConversionUtil.expand(x4001bo.getOrgAllQuota(), 2));
        coreBudgetOrgAddInfo.setPersonMaxQuota(CurrencyConversionUtil.expand(x4001bo.getPersonMaxQuota(), 2));
        coreBudgetOrgAddInfo.setVersion(1);
        coreBudgetOrgAddInfoDao.insert(coreBudgetOrgAddInfo);
        String operatorId = x4001bo.getOperatorId();
        if (operatorId == null) {
            operatorId = "system";
        }
        /**
         * 生成非金融日志
         */
        nonFinancialLogUtil.createNonFinancialActivityLog(x4001bo.getEventNo(), x4001bo.getActivityNo(), ModificationType.ADD.getValue(),
            null, null, coreBudgetOrgAddInfo, coreBudgetOrgAddInfo.getId(), x4001bo.getCurrLogFlag(), operatorId,
            coreBudgetOrgAddInfo.getCustomerNo(), coreBudgetOrgAddInfo.getCustomerNo(), null, null);
        eventCommAreaNonFinance.setAuthDataSynFlag("1");
        return eventCommAreaNonFinance;
    }

}
