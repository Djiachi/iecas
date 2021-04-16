package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5710Bus;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5720BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.LoggerUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * MC调单申请查询
 * @ClassName X5710BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年1月25日 下午2:09:32
 * @version 1.0.0
 */
@Service
public class X5710BusImpl implements X5710Bus {
	@Resource
    private ClrProcessingMcDao clrProcessingMcDao;
	@Resource
    private ParamsUtil paramsUtil;

    @Override
    public Object busExecute(X5720BO x5720bo) throws Exception {
        
    	ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
         if (StringUtil.isNotEmpty(x5720bo.getExternalIdentificationNo())) {
        	 clrProcessingMcSqlBuilder.andExternalIdentificationNoEqualTo(x5720bo.getExternalIdentificationNo());
         }
         PageBean<ClrProcessingMc> page = new PageBean<>();
         int totalCount = clrProcessingMcDao.countBySqlBuilder(clrProcessingMcSqlBuilder);
         page.setTotalCount(totalCount);
         if (null != x5720bo.getPageSize() && null != x5720bo.getIndexNo()) {
        	 clrProcessingMcSqlBuilder.setPageSize(x5720bo.getPageSize());
        	 clrProcessingMcSqlBuilder.setIndexNo(x5720bo.getIndexNo());
             page.setPageSize(x5720bo.getPageSize());
             page.setIndexNo(x5720bo.getIndexNo());
         }
         String entrys =Constant.EMPTY_LIST;
         if (totalCount > 0) {
             List<ClrProcessingMc> list = clrProcessingMcDao
                     .selectListBySqlBuilder(clrProcessingMcSqlBuilder);
             page.setRows(list);
             if(null != list && !list.isEmpty()){
 				entrys = list.get(0).getId();
 			}
         }
        //新增非金融日志
 		paramsUtil.logNonInsert(x5720bo.getEventNo(), x5720bo.getActivityNo(), 
 				new ClrProcessingMc(), new ClrProcessingMc(), entrys, x5720bo.getOperatorId());
         return page;
     }
    
}
