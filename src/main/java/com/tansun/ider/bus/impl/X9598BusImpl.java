package com.tansun.ider.bus.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X9598Bus;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLog;
import com.tansun.ider.model.bo.X9598BO;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * @version:1.0
 * @Description: 分期资产证券化未抛本金处理
 * @author: PanQi
 */
@Service
public class X9598BusImpl implements X9598Bus {

    @Override
    public Object busExecute(X9598BO x9598BO) throws Exception {

        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x9598BO, eventCommAreaNonFinance);

        List<CoreSpecialActivityLog> coreSpecialActivityLogLists = x9598BO.getCoreSpecialActivityLogList();
        // 是否有待处理S日志
        if (null == coreSpecialActivityLogLists || coreSpecialActivityLogLists.isEmpty()) {
            return eventCommAreaNonFinance;
        } else {
            List<Map<String, Object>> eventCommAreaTriggerEventList = new LinkedList<>();
            // 发卡
            EventCommArea eventCommArea = new EventCommArea();
            eventCommArea.setEcommCustId(x9598BO.getCustomerNo());
            eventCommArea.setEcommProdObjId(x9598BO.getProductObjectCode());
            eventCommArea.setEcommBusineseType(x9598BO.getBusinessTypeCode());
            eventCommArea.setEcommOperMode(x9598BO.getOperationMode());
            eventCommArea.setEcommCoreSpecialActivityLogs(coreSpecialActivityLogLists);
            Map<String, Object> triggerEventParams = new HashMap<String, Object>(2);
            triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, eventCommArea);
            eventCommAreaTriggerEventList.add(triggerEventParams);
            EventCommAreaNonFinance eventCommAreaNonFinanceNew = new EventCommAreaNonFinance();
            eventCommAreaNonFinanceNew.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
            return eventCommAreaNonFinanceNew;
        }

    }

}
