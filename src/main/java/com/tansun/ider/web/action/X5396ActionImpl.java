package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5396Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5396BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @ClassName: X5396ActionImpl
 * 
 * @Function: 未出账单查询
 * 
 * @Description:
 * 
 * @author baiyu
 * 
 * @date 2019年4月30日
 * 
 * @version R04.00
 */
@Service("X5396")
public class X5396ActionImpl implements ActionService {
    @Autowired
    private X5396Bus x5396Bus;

    /**
     * 给X5396Bo赋值，调用Bus
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5396BO x5396BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5396BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
                .get(WSC.ACTION_ARTIFACT_KEY);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5396BO.setGlobalEventNo(globalEventNo);
        x5396BO.setCoreEventActivityRel(coreEventActivityRel);
        x5396BO.setActivityArtifactList(activityArtifactList);
        return x5396Bus.busExecute(x5396BO);
    }

}
