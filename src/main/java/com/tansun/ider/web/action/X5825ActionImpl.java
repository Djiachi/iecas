package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5825Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5825BO;
import com.tansun.ider.web.WSC;

/**
 * 媒介升降级
 * @author wangxi
 *
 */
@Service("X5825")
public class X5825ActionImpl implements ActionService {

    @Autowired
    private X5825Bus x5825Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5825BO x5825BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5825BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5825BO.setGlobalEventNo(globalEventNo);
        x5825BO.setCoreEventActivityRel(coreEventActivityRel);
        x5825BO.setEventNo(coreEventActivityRel.getEventNo());
        x5825BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x5825Bus.busExecute(x5825BO);
    }

}
