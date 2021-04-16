package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.ider.bus.X4015Bus;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.impl.CoreBudgetOrgAddInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.model.bo.X4015BO;
import com.tansun.ider.model.vo.X4015VO;

/**
 * <p> Title: X4015BusImpl </p>
 * <p> Description: 查询个人公务卡最大限额</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
@Service
public class X4015BusImpl implements X4015Bus {
    @Autowired
    private CoreBudgetOrgAddInfoDaoImpl coreBudgetOrgAddInfoDaoImpl;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;

    @Override
    public Object busExecute(X4015BO x4015bo) throws Exception {
        // CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
        // coreProductSqlBuilder.andProductObjectCodeEqualTo(eventCommAreaNonFinance.getProductObjectCode())
        // .andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo())
        // .andOperationModeEqualTo(eventCommAreaNonFinance.getOperationMode());
        // CoreProduct coreProduct = coreProductDaoImpl.selectBySqlBuilder(coreProductSqlBuilder);
        // 获取个人客户对应的预算单位编码
        // String budgetOrgCode = coreProduct.getBudgetOrgCode();

        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andIdNumberEqualTo(x4015bo.getBudgetOrgCode());
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
        // 根据预算单位编码查询预算单位附加信息
        CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
        coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDaoImpl.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
        // 将表中记录金额缩小一百倍
        BigDecimal ecommTransAmount = CurrencyConversionUtil.expand(coreBudgetOrgAddInfo.getPersonMaxQuota(), -2);
        Map<String, String> map = new HashMap<String, String>();
        X4015VO x4015Vo = new X4015VO();
        x4015Vo.setBudgetUnitCode(x4015bo.getBudgetOrgCode());
        x4015Vo.setPersonMaxQuota(ecommTransAmount);
        map.put("budgetUnitCode", x4015bo.getBudgetOrgCode());
        map.put("personMaxQuota", ecommTransAmount.toString());
        return map;
    }

    public static BigDecimal expand(BigDecimal amount, int decimal) {
        BigDecimal tenIndex = new BigDecimal(Math.pow(10.0D, decimal));
        return amount.multiply(tenIndex).setScale(0, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        BigDecimal amount = new BigDecimal("100000");
        int decimal = -2;
        BigDecimal bigDecimal = expand(amount, decimal);
        System.out.println(bigDecimal);
    }
}
