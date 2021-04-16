package com.tansun.ider.service;

import java.math.BigDecimal;

import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.model.vo.X5982VO;

/**
 * @Author: PanQi
 * @Date: 2019/11/21
 * @updater:
 * @description: 查询账户信息公共方法
 */
public interface QueryAccountService {

    /**
     * 查询分期交易账户信息表
     * @param accountId 账户号
     * @param currencyCode 币种
     * @return
     * @throws Exception
     */
    CoreInstallmentTransAcct queryCoreInstallmentTransAcct(String accountId, String currencyCode) throws Exception;

    /**
     * 查询余额单元表信息和
     * @param accountId 账户Id
     * @param currencyCode 币种
     * @return
     * @throws Exception
     */
    BigDecimal queryCurrBalanceUnit(String accountId, String currencyCode) throws Exception;

    /**
     * 查询实时余额
     * @param currencyCode
     * @param remainPrincipalAmount
     * @return
     * @throws Exception
     */
    BigDecimal queryCurrBalance(String customerNo, String accountId,String currencyCode,String accountOrganForm,
                                String cycleModeFlag,BigDecimal remainPrincipalAmount)
            throws Exception;
}
