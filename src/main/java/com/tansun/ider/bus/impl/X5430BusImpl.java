package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5430Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CorePcd;
import com.tansun.ider.dao.beta.entity.CorePriceTag;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5430BO;
import com.tansun.ider.model.vo.X5430VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 客户定价标签查询
 * @author: admin
 */
@Service
public class X5430BusImpl implements X5430Bus {
    private static Logger logger = LoggerFactory.getLogger(X5430BusImpl.class);
    @Autowired
    private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5430BO x5430bo) throws Exception {

        String idNumber = x5430bo.getIdNumber();
        String idType = x5430bo.getIdType();
        String externalIdentificationNo = x5430bo.getExternalIdentificationNo();
        String customerNo = "";
        String operationMode = "";
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        CoreCustomer coreCustomer = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            if (coreMediaBasicInfo.getMainCustomerNo() != null) {
                customerNo = coreMediaBasicInfo.getMainCustomerNo();
                operationMode = coreMediaBasicInfo.getOperationMode();
            }
        }
        String entrys = Constant.EMPTY_LIST;
        PageBean<X5430VO> page = new PageBean<>();

        CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
        coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
        int totalCount = coreCustomerBusinessTypeDao.countBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5430bo.getPageSize() && null != x5430bo.getIndexNo()) {
            coreCustomerBusinessTypeSqlBuilder.setPageSize(x5430bo.getPageSize());
            coreCustomerBusinessTypeSqlBuilder.setIndexNo(x5430bo.getIndexNo());
            page.setPageSize(x5430bo.getPageSize());
            page.setIndexNo(x5430bo.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreCustomerBusinessType> listCoreCustomerBusinessType = coreCustomerBusinessTypeDao
                    .selectListBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
            List<X5430VO> list = new ArrayList<X5430VO>();
            for (CoreCustomerBusinessType coreCustomerBusinessType : listCoreCustomerBusinessType) {
                X5430VO x5430VO = new X5430VO();
                // 查询定价标签信息
                CorePriceTag priceTag = httpQueryService.queryPriceTag(operationMode,
                        coreCustomerBusinessType.getPricingTag(), null);
                if (priceTag != null) {
                    addPricingObjectDesc(priceTag,x5430VO);
                    CachedBeanCopy.copyProperties(priceTag, x5430VO);
                }
                CachedBeanCopy.copyProperties(coreCustomerBusinessType, x5430VO);
              //标签应用范围（币种）
                String pricingScope =coreCustomerBusinessType.getPricingScope();
                //获取币种名称
                if (StringUtil.isNotEmpty(pricingScope)) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(pricingScope);
                    String currencyDesc= coreCurrency.getCurrencyDesc();
                    x5430VO.setCurrencyDesc(currencyDesc);  
                }
                list.add(x5430VO);
            }
            page.setRows(list);
            if (null != list && !list.isEmpty()) {
                entrys = list.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5430bo.getCoreEventActivityRel().getEventNo(),
                x5430bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5430bo.getOperatorId());
        return page;
    }

    /**
     * 
     *
     * @MethodName addProductDesc
     * @Description: 返回对象添加产品描述字段
     * @param list
     * @return: List<X0390VO>
     * @throws Exception
     */
    private X5430VO addPricingObjectDesc(CorePriceTag corePriceTag,X5430VO x5430VO) throws Exception {
        logger.debug("---------X5430BusImpl.addProductDesc    start");
        logger.debug("---------X5430BusImpl.addProductDesc    corePriceTag: " + corePriceTag);
        String pricingObject = corePriceTag.getPricingObject();
        logger.debug("---------X5430BusImpl.addProductDesc    pricingObject: " + pricingObject);
        String pricingObjectCode = corePriceTag.getPricingObjectCode();
        logger.debug("---------X5430BusImpl.addProductDesc    pricingObjectCode: " + pricingObjectCode);
        if (StringUtil.isNotBlank(pricingObjectCode)) {
            String pricingObjectDesc = null;
            if (Constant.TAG_TYPE_P.equals(pricingObject)) {
             CorePcd corePcd = httpQueryService.queryPcdNo(pricingObjectCode);
                logger.debug("---------X5430BusImpl.addProductDesc   corePcd: " + corePcd);
                if (corePcd != null) {
                    pricingObjectDesc = corePcd.getPcdDesc();
                    logger.debug("---------X5430BusImpl.addProductDesc     pricingObjectDesc: " + pricingObjectDesc);
                }
            } else if (Constant.TAG_TYPE_F.equals(pricingObject)) {
                CoreFeeItem coreFeeItem = httpQueryService.queryCoreFeeItem(pricingObjectCode);
                logger.debug("---------X5430BusImpl.addProductDesc   coreFeeItem: " + coreFeeItem);
                if (coreFeeItem != null) {
                    pricingObjectDesc = coreFeeItem.getFeeDesc();
                    logger.debug("---------X5430BusImpl.addProductDesc     pricingObjectDesc: " + pricingObjectDesc);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("---------X5430BusImpl.addProductDesc   pricingObject unmatch  id: "
                            + corePriceTag.getId());
                }
            }
            x5430VO.setPricingObjectDesc(pricingObjectDesc);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("---------X5430BusImpl.addProductDesc   pricingObjectCode is null   id: "
                        + corePriceTag.getId());
            }
        }
        return x5430VO;

    }
}
