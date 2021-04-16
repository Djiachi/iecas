package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5275Bus;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerLifecycleDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomerLifecycle;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerLifecycleSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.LifeCycleNodeType;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5275BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * 客户生命周期展示
 * @author lianhuan
 * 2018年9月30日
 */
@Service
public class X5275BusImpl implements X5275Bus {
    @Autowired
    private CoreProductDao coreProductDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private CoreCustomerLifecycleDao coreCustomerLifecycleDao;

    @Override
    public Object busExecute(X5275BO x5275bo) throws Exception {
        // 事件公共公共区
    	EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5275bo, eventCommAreaNonFinance);
        String customerNo = eventCommAreaNonFinance.getCustomerNo();
        String lifecycleNodeType = eventCommAreaNonFinance.getLifecycleNodeType();
        PageBean<X5275BO> page = new PageBean<>();
        CoreCustomerLifecycleSqlBuilder lifecycleSqlBuilder = new CoreCustomerLifecycleSqlBuilder();
        lifecycleSqlBuilder.andCustomerNoEqualTo(customerNo);
        lifecycleSqlBuilder.orderByNodeOccurDate(false);
        if (lifecycleNodeType.equals(LifeCycleNodeType.PP.getValue())) {
            // 根据客户查询产品
            CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
            coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
            List<CoreProduct> productList = coreProductDao.selectListBySqlBuilder(coreProductSqlBuilder);
            CoreCustomerLifecycleSqlBuilder newCaseSQLBulider = new CoreCustomerLifecycleSqlBuilder();
            for (CoreProduct coreProduct : productList) {
                newCaseSQLBulider.orProductObjectCodeEqualTo(coreProduct.getProductObjectCode());
            }
            lifecycleSqlBuilder.and(newCaseSQLBulider);
        } else if (lifecycleNodeType.equals(LifeCycleNodeType.MP.getValue())) {
            // 根据客户查询媒介
            CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
            coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
            List<CoreMediaBasicInfo> mediaList = coreMediaBasicInfoDao
                    .selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
            CoreCustomerLifecycleSqlBuilder newCaseSQLBulider = new CoreCustomerLifecycleSqlBuilder();
            for (CoreMediaBasicInfo coreMediaBasicInfo : mediaList) {
                lifecycleSqlBuilder.orMediaObjectCodeEqualTo(coreMediaBasicInfo.getMediaObjectCode());
            }
            lifecycleSqlBuilder.and(newCaseSQLBulider);
        } else if (lifecycleNodeType.equals(LifeCycleNodeType.AP.getValue())) {
            // 根据客户查询账户
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
            List<CoreAccount> accList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            CoreCustomerLifecycleSqlBuilder newCaseSQLBulider = new CoreCustomerLifecycleSqlBuilder();
            for (CoreAccount coreAccount : accList) {
                lifecycleSqlBuilder.orAccountNoEqualTo(coreAccount.getAccountId());
            }
            lifecycleSqlBuilder.and(newCaseSQLBulider);
        }
        int rowsCount = coreCustomerLifecycleDao.countBySqlBuilder(lifecycleSqlBuilder);
        page.setPageSize(rowsCount);
        if (null != x5275bo.getPageSize() && null != x5275bo.getIndexNo()) {
            page.setPageSize(x5275bo.getPageSize());
            page.setIndexNo(x5275bo.getIndexNo());
        }
        if (rowsCount > 0) {
            List<X5275BO> x5275bos = new ArrayList<X5275BO>();
            List<CoreCustomerLifecycle> list = coreCustomerLifecycleDao.selectListBySqlBuilder(lifecycleSqlBuilder);
            for (CoreCustomerLifecycle coreCustomerLifecycle : list) {
                X5275BO bo = new X5275BO();
                CachedBeanCopy.copyProperties(coreCustomerLifecycle, bo);
                x5275bos.add(bo);
            }
            page.setRows(x5275bos);
        }
        return page;
    }
}
