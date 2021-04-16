package com.tansun.ider.bus.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5580Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerElementDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerElementSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5580BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @Desc:X5580客户个性化元件新增
 * @Author yanzhaofei
 * @Date 2019年1月24日14:25:34
 */
@Service
public class X5580BusImpl implements X5580Bus {

    @Autowired
    private CoreCustomerElementDao coreCustomerElementDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private HttpQueryService httpQueryService;

    @Override
    public Object busExecute(X5580BO x5580Bo) throws Exception {
        String customerNo = x5580Bo.getCustomerNo();
        if (customerNo == null) {
            throw new BusinessException("客户号为空！");
        }
        List<CoreCustomerElement> elementList = x5580Bo.getList();
        if (elementList.size() < 1) {
            throw new BusinessException("未选择元件！");
        }
        // 获取当前日志标识
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        for (CoreCustomerElement coreCustomerElement : elementList) {
            String effectDate = coreCustomerElement.getEffectDate();
            String uneffectDate = coreCustomerElement.getUneffectDate();
            if (StringUtil.isBlank(effectDate) || StringUtil.isBlank(uneffectDate)
                    || effectDate.compareTo(uneffectDate) > 0) {
                throw new Exception("生效日期或者失效日期不符合规范！");
            }
            coreCustomerElement.setCustomerNo(x5580Bo.getCustomerNo());
            coreCustomerElement.setId(RandomUtil.getUUID());
            coreCustomerElement.setVersion(1);
            coreCustomerElement.setGmtCreate(DateUtil.format(new Date(), "yyyy-MM-dd"));
            if (!checkElement(coreCustomerElement)) {
                throw new BusinessException("该客户已存在个性化元件！");
            } 
            int result = coreCustomerElementDao.insert(coreCustomerElement);
            if (result != 1) {
                throw new BusinessException("客户个性化元件生成错误！");
            }
            // 新增非金融日志
            nonFinancialLogUtil.createNonFinancialActivityLog(x5580Bo.getEventNo(), x5580Bo.getActivityNo(),
                    ModificationType.ADD.getValue(), null, null, coreCustomerElement, coreCustomerElement.getId(),
                    coreSystemUnit.getCurrLogFlag(), x5580Bo.getOperatorId(), x5580Bo.getCustomerNo(),
                    x5580Bo.getCustomerNo(), null, x5580Bo.getLogLevel());
        }
        return "OK";
    }
    /**
     * 
    
     * @MethodName: checkElement
    
     * @Description: 新增元件验证
    
     * @param coreCustomerElement
     * @return
     * @throws Exception
     */
    private Boolean checkElement(CoreCustomerElement coreCustomerElement) throws Exception {
        CoreCustomerElementSqlBuilder coreCustomerElementSqlBuilder = new CoreCustomerElementSqlBuilder();
        coreCustomerElementSqlBuilder.andCustomerNoEqualTo(coreCustomerElement.getCustomerNo());
        coreCustomerElementSqlBuilder.andElementNoEqualTo(coreCustomerElement.getElementNo());
        List<CoreCustomerElement> cceList = coreCustomerElementDao
                .selectListBySqlBuilder(coreCustomerElementSqlBuilder);
        if (cceList != null && cceList.size() > 0) {
            for (CoreCustomerElement cce : cceList) {
                String effectDate = cce.getEffectDate();
                String uneffectDate = cce.getUneffectDate();
                if (!(coreCustomerElement.getEffectDate().compareTo(uneffectDate) > 0
                        || coreCustomerElement.getUneffectDate().compareTo(effectDate) < 0)) {
                    return false;
                }
            }
        }
        return true;
    }
}
