package com.tansun.ider.bus.impl;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4095Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingAssetPlan;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4095BO;
import com.tansun.ider.service.HttpQueryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 资产证券化计划删除
 *
 * @author chenqilei
 */
@Service
public class X4095BusImpl implements X4095Bus {

    @Resource
    private CoreAccountDao coreAccountDao;
    @Resource
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X4095BO x4095bo) throws Exception {
        PageBean<CoreAccountingAssetPlan> page = new PageBean<>();
        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        if (StringUtil.isNotBlank(x4095bo.getPlanId())) {
            coreAccountSqlBuilder.andAbsPlanIdEqualTo(x4095bo.getPlanId());
        } else {
            throw new BusinessException("PARAM-00001","PlanId");
        }
        //根据planId查询改ABS计划有没有被用户使用    如果被使用不允许删除
        int accountNum = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        //删除该条记录
        if (accountNum < 1) {
            httpQueryService.deleteCoreAccountingAssetPlan(x4095bo.getPlanId());
        } else {
            throw new BusinessException("COR-14003");
        }
        page.setRows(null);
        page.setTotalCount(0);
        return page;
    }
}
