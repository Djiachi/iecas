package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5830Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5830BO;
import com.tansun.ider.web.WSC;

/**
 * 产品升降级
 * @author wangxi
 *
 */
@Service("X5830")
public class X5830ActionImpl implements ActionService {

    @Autowired
    private X5830Bus x5830Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5830BO x5830BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5830BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5830BO.setGlobalEventNo(globalEventNo);
        x5830BO.setCoreEventActivityRel(coreEventActivityRel);
        x5830BO.setEventNo(coreEventActivityRel.getEventNo());
        x5830BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x5830Bus.busExecute(x5830BO);
    }

}
