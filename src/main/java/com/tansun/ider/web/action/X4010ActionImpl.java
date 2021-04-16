package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4010Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4010BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 预算单位附件信息维护
 * @author: cuiguangchao
 */
@Service("X4010")
public class X4010ActionImpl implements ActionService {

    @Autowired
    private X4010Bus x4010Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4010BO x4010BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4010BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x4010BO.setGlobalEventNo(globalEventNo);
        x4010BO.setEventNo(coreEventActivityRel.getEventNo());
        x4010BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x4010Bus.busExecute(x4010BO);
    }

}
