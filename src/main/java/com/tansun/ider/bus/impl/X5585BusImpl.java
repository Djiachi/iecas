package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5585Bus;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerElementDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerElementSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5580BO;
import com.tansun.ider.model.vo.X5580VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * X5585 客户个性化元件 查询
 * 
 * @author yanzhaofei
 * @Date 2019年1月24日14:48:50
 */
@Service
public class X5585BusImpl implements X5585Bus {

    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreCustomerElementDao coreCustomerElementDao;
    @Resource
    private HttpQueryService httpQueryService;
    @Autowired
    private ParamsUtil paramsUtil;

    @Override
    public Object busExecute(X5580BO x5580Bo) throws Exception {
        // 证件号
        String idNumber = x5580Bo.getIdNumber();
        // 外部识别号
        String externalIdentificationNo = x5580Bo.getExternalIdentificationNo();
        // 元件编号
        String elementNo = x5580Bo.getElementNo();
        // 调用公共方法查询客户信息
        CoreCustomer customer = queryCustomerService.queryCoreCustomer(idNumber, externalIdentificationNo);
        if (customer == null) {
            // 抛出异常CUS-00005客户信息查询失败
            throw new BusinessException("CUS-00107");
        }
        String customerNo = customer.getCustomerNo();
        String systemUnitNo = customer.getSystemUnitNo();
        CoreCustomerElementSqlBuilder coreCustomerElementSqlBuilder = new CoreCustomerElementSqlBuilder();
        if (StringUtil.isNotBlank(elementNo)) {
            coreCustomerElementSqlBuilder.andElementNoEqualTo(elementNo);
        }
        if (StringUtil.isNotBlank(idNumber) || StringUtil.isNotBlank(externalIdentificationNo)) {
            coreCustomerElementSqlBuilder.andCustomerNoEqualTo(customerNo);
        } else {
            throw new BusinessException("CUS-00025");
        }
        String entrys = Constant.EMPTY_LIST;
        PageBean<X5580VO> page = new PageBean<>();
        int totalCount = coreCustomerElementDao.countBySqlBuilder(coreCustomerElementSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5580Bo.getPageSize() && null != x5580Bo.getIndexNo()) {
            coreCustomerElementSqlBuilder.orderByGmtCreate(true);
            coreCustomerElementSqlBuilder.setPageSize(x5580Bo.getPageSize());
            coreCustomerElementSqlBuilder.setIndexNo(x5580Bo.getIndexNo());
            page.setPageSize(x5580Bo.getPageSize());
            page.setIndexNo(x5580Bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreCustomerElement> list = coreCustomerElementDao
                    .selectListBySqlBuilder(coreCustomerElementSqlBuilder);
            List<X5580VO> returnList = new ArrayList<>();
            for (CoreCustomerElement tmp : list) {
                X5580VO x5580VO = new X5580VO();
                CachedBeanCopy.copyProperties(tmp, x5580VO);
                x5580VO.setCustomerNo(customerNo);
                x5580VO.setElementDesc(httpQueryService.queryElementDesc(tmp.getElementNo()));
                CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
                if(coreSystemUnit==null){
                    throw new BusinessException("  CUS-00061");
                }
                x5580VO.setSetDate(coreSystemUnit.getNextProcessDate());
                returnList.add(x5580VO);
            }
            page.setRows(returnList);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5580Bo.getEventNo(), x5580Bo.getActivityNo(), tempObject, tempObject, entrys,
                x5580Bo.getOperatorId());
        return page;
    }
}
