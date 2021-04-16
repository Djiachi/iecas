package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X4075Bus;
import com.tansun.ider.dao.issue.entity.CoreProductObjectBillSum;
import com.tansun.ider.dao.issue.impl.CoreProductObjectBillSumDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductObjectBillSumSqlBuilder;
import com.tansun.ider.model.bo.X4075BO;

/**
 * <p> Title: X4075BusImpl </p>
 * <p> Description: 通过gns查询产品对象账单摘要信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年6月15日
 */
@Service
public class X4075BusImpl implements X4075Bus {
    @Autowired
    private CoreProductObjectBillSumDaoImpl coreProductObjectBillSummaryDaoImpl;

    @Override
    public Object busExecute(X4075BO x4075bo) throws Exception {
        CoreProductObjectBillSumSqlBuilder coreProductObjectBillSummarySqlBuilder = new CoreProductObjectBillSumSqlBuilder();
        coreProductObjectBillSummarySqlBuilder.andCustomerNoEqualTo(x4075bo.getCustomerNo());
        coreProductObjectBillSummarySqlBuilder.andBillDateLikeRigth(x4075bo.getBillDate());
        coreProductObjectBillSummarySqlBuilder.andProductObjectCodeEqualTo(x4075bo.getProductObjectCode());
        List<CoreProductObjectBillSum> queryList =
                coreProductObjectBillSummaryDaoImpl.selectListBySqlBuilder(coreProductObjectBillSummarySqlBuilder);
        return queryList;
    }

}
