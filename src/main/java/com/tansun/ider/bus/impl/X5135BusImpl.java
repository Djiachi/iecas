package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5135Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5135BO;

/**
 * @version:1.0
 * @Description: 客户基本信息维护
 * @author: admin
 */
@Service
public class X5135BusImpl implements X5135Bus {

    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Object busExecute(X5135BO x5135bo) throws Exception {
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        String id = x5135bo.getId();
        String customerNo = x5135bo.getCustomerNo();
        if (StringUtil.isNotBlank(x5135bo.getId())) {
            coreCustomerSqlBuilder.andIdEqualTo(id);
        }
        if (StringUtil.isNotBlank(x5135bo.getCustomerNo())) {
            coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreCustomer updateCoreCustomer = new CoreCustomer();
        // 如果是公务卡建立预算单位,则客户类型与证件类型必须是预算单位,否则抛错。
        if ("3".equals(coreCustomer.getCustomerType()) && "7".equals(coreCustomer.getIdType())) {
            if (!"3".equals(x5135bo.getCustomerType()) || !"7".equals(x5135bo.getIdType())) {
                throw new BusinessException("", "预算单位维护时,客户类型与证件类型必须是预算单位!");
            }
        }
        CachedBeanCopy.copyProperties(x5135bo, updateCoreCustomer);
        // 设置版本号
        updateCoreCustomer.setId(coreCustomer.getId());
        updateCoreCustomer.setVersion(coreCustomer.getVersion() + 1);
        int result = coreCustomerDaoImpl.updateByPrimaryKeySelective(updateCoreCustomer);
        if (result != 1) {
            throw new BusinessException("CUS-00012", "客户基本信息维护");
        }
        // 如果客户类型是预算单位，并且修改了账单日;则需要要支持同时修改预算单位下所有个人/单位公务卡所属产品项目的账单日及下一账单日。
        if (!x5135bo.getBillDay().equals(coreCustomer.getBillDay())) {

        }
        return result;
    }

}
