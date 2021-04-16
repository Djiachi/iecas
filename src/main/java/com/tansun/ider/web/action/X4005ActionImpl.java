package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4005Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4005BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 预算单位信息查询
 * @author: cuiguangchao
 */
@Service("X4005")
public class X4005ActionImpl implements ActionService {

    @Autowired
    private X4005Bus x4005Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4005BO x4005BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4005BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x4005BO.setGlobalEventNo(globalEventNo);
        x4005BO.setEventNo(coreEventActivityRel.getEventNo());
        x4005BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x4005Bus.busExecute(x4005BO);
    }

}
