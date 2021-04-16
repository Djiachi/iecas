package com.tansun.ider.bus.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5995Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5995BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BatchUtil;
import com.tansun.ider.util.CardUtil;

/**
 * 
 * @ClassName: X5995BusImpl
 * @Description: 强制结息（产品注销时实时结息）
 * @author A18ccms a18ccms_gmail_com
 * @date 2019年8月19日 下午5:42:03
 *
 */
@Service
public class X5995BusImpl implements X5995Bus {
    @Value("${global.target.service.url.card}")
    private String cardUrl;
    @Autowired
    private CoreProductDao coreProductDao;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private BatchUtil batchUtil;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5995BO x5995bo) throws Exception {
        // 事件公共区
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        EventCommArea eventCommArea = new EventCommArea();// 调用403构建所需
        // 构件清单
        List<CoreActivityArtifactRel> artifactList = x5995bo.getActivityArtifactList();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5995bo,eventCommAreaNonFinance);
        // 客户代码
        String customerNo = x5995bo.getCustomerNo();
        if (StringUtil.isBlank(customerNo)) {
            throw new BusinessException("CUS-12060");
        }
        // 产品对象代码
        String productObjectCode = x5995bo.getProductObjectCode();
        if (StringUtil.isBlank(productObjectCode)) {
            throw new BusinessException("CUS-12060");
        }
        // 运营模式
        String operationMode = x5995bo.getOperationMode();
        if (StringUtil.isBlank(operationMode)) {
            throw new BusinessException("CUS-12060");
        }
        // 法人实体编号
        String corporation = x5995bo.getCorporation();
        if (StringUtil.isBlank(corporation)) {
            throw new BusinessException("CUS-12060");
        }
        boolean centralizedMark = false;
        centralizedMark = judgeCentralizedAccountingProduct(eventCommArea, artifactList, productObjectCode,
                operationMode);
        if (centralizedMark) {
            centralizedMark = false;
            CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
            coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
            // 客户状态 1-正常
            coreProductSqlBuilder.andStatusCodeEqualTo(Constant.NEED_INSPECTION_ACTIV);
            List<CoreProduct> coreProductList = coreProductDao.selectListBySqlBuilder(coreProductSqlBuilder);
            if (coreProductList != null && coreProductList.size() > 0) {
                for (CoreProduct coreProduct : coreProductList) {
                    String proc = coreProduct.getProductObjectCode();
                    if (productObjectCode.equals(proc)) {
                        continue;
                    }
                    centralizedMark = judgeCentralizedAccountingProduct(eventCommArea, artifactList, proc,
                            coreProduct.getOperationMode());
                    // 判断客户是否还有其他集中核算产品：如果有就直接结束程；否则调用BSS.RT.12.2002事件进行实时结计利息。
                    if (centralizedMark) {
                        return eventCommAreaNonFinance;
                    }
                }
                productObjectCode = Constant.DEFAULT_PRODUCT_OBJECT_CODE;
            } else {
                throw new BusinessException("CUS-00006");
            }
        }
        // 触发BSS.RT.12.2002事件进行实时结计利息.
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer != null) {
            String systemUnitNo = coreCustomer.getSystemUnitNo();
            eventCommArea.setEcommSystemUnitNo(systemUnitNo);
            CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
             if(coreSystemUnit!=null){
                 eventCommArea.setEcommImmediateProcessingDate(coreSystemUnit.getCurrProcessDate());
             }else{
                 throw new BusinessException("CUS-00061");
                
             }
        } else {
            throw new BusinessException("CUS-12000");      
        }
        eventCommArea.setEcommEventId(Constant.REAL_TIME_INTEREST_ACCRUA);
        eventCommArea.setEcommProdObjId(productObjectCode);
        eventCommArea.setEcommOperMode(operationMode);
        eventCommArea.setEcommCustId(customerNo);
        eventCommArea.setEcommLegalPersonNum(corporation);
        Map<String, Object> responseMap = batchUtil.triggerEvent(eventCommArea, cardUrl);
        if (!Constant.SUCCESS_CODE.equals(responseMap.get("returnCode"))) {
            throw new BusinessException("COR-12009",Constant.REAL_TIME_INTEREST_ACCRUA);
        }
        return eventCommAreaNonFinance;
    }

    /**
     * 根据403构件判断产品核算类型
     * 
     * @param eventCommArea
     * @param artifactList
     * @return 集中核算为true；单独核算为false
     * @throws Exception
     */
    private Boolean judgeCentralizedAccountingProduct(EventCommArea eventCommArea,
            List<CoreActivityArtifactRel> artifactList, String productObjectCode, String operationMode)
            throws Exception {
        eventCommArea.setEcommProdObjId(productObjectCode);// 产品对象代码
        eventCommArea.setEcommOperMode(operationMode);// 运营模式
        Boolean result = null;
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                result = false;
            } else if (Constant.BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                result = true;
            } else {
                throw new BusinessException("COR-12011");
            }
        }
        return result;
    }
}
