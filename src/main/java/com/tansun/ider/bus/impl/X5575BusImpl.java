package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5575Bus;
import com.tansun.ider.dao.issue.CoreCustomerElementDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerElementSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5580BO;

/**
 * X5575 客户个性化元件修改
 * 
 * @author admin
 * @Date 2019年1月24日14:56:07
 */
@Service
public class X5575BusImpl implements X5575Bus {
    @Autowired
    private CoreCustomerElementDao coreCustomerElementDao;

    @Override
    public Object busExecute(X5580BO x5580Bo) throws Exception {
        String effectDate = x5580Bo.getEffectDate();
        String uneffectDate = x5580Bo.getUneffectDate();
        CoreCustomerElementSqlBuilder coreCustomerElementSqlBuilder = new CoreCustomerElementSqlBuilder();
        coreCustomerElementSqlBuilder.andIdEqualTo(x5580Bo.getId());
        CoreCustomerElement coreCustomerElement = coreCustomerElementDao
                .selectBySqlBuilder(coreCustomerElementSqlBuilder);
        if (effectDate.equals(coreCustomerElement.getEffectDate())
                && uneffectDate.equals(coreCustomerElement.getUneffectDate())) {
            throw new Exception("生效日期与失效日期未修改！");
        } else if (effectDate.compareTo(uneffectDate) > 0) {
            throw new Exception("生效日期不能大于失效日期！");
 
        } else {
            coreCustomerElement.setEffectDate(effectDate);
            coreCustomerElement.setUneffectDate(uneffectDate);
            if(!checkElement(coreCustomerElement)){
                throw new BusinessException("相同个性化元件有效期重叠！");
            }
        }
        coreCustomerElement.setVersion(coreCustomerElement.getVersion() + 1);
        int result = coreCustomerElementDao.updateByPrimaryKey(coreCustomerElement);
        if (result == 0) {
            throw new BusinessException("客户个性化元件修改失败！");
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
        coreCustomerElementSqlBuilder.andIdNotEqualTo(coreCustomerElement.getId());
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
