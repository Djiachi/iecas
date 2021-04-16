package com.tansun.ider.bus.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X4020Bus;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.impl.CoreBudgetOrgCustRelDaoImpl;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4020BO;

/**
 * <p> Title: X4020BusImpl </p>
 * <p> Description: 处理预算单位片区号和客户的不同时，保存预算单位单位员工关系信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
@Service
public class X4020BusImpl implements X4020Bus {
    @Autowired
    private CoreBudgetOrgCustRelDaoImpl coreBudgetOrgCustRelDaoImpl;

    @Override
    public Object busExecute(X4020BO x4020bo) throws Exception {
        CoreBudgetOrgCustRel coreBudgetOrgCustRel = new CoreBudgetOrgCustRel();
        coreBudgetOrgCustRel.setId(RandomUtil.getUUID());
        coreBudgetOrgCustRel.setCustomerNo(x4020bo.getCustomerNo());
        coreBudgetOrgCustRel.setIdType(x4020bo.getIdType());
        coreBudgetOrgCustRel.setIdNumber(x4020bo.getIdNumber());
        coreBudgetOrgCustRel.setCustomerArea(x4020bo.getCustGnsInfo());// 持卡人信息所在片区
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(x4020bo.getBudgetOrgCode());
        coreBudgetOrgCustRel.setBudgetOrgCode(m.replaceAll("").trim());
        coreBudgetOrgCustRel.setExternalIdentificationNo(x4020bo.getExternalIdentificationNo());
        coreBudgetOrgCustRel.setVersion(1);
        int insert = coreBudgetOrgCustRelDaoImpl.insert(coreBudgetOrgCustRel);
        if (insert != 1) {
            throw new BusinessException("", "新增预算单位单位员工关系信息失败!");
        }
        return insert;
    }

}
