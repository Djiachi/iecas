package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5987Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5975BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: PanQi
 * @Date: 2020/05/12
 * @updater:
 * @description: 资产证券化查询 循环账户
 */
@Service("X5987")
public class X5987ActionImpl implements ActionService {
    @Autowired
    private X5987Bus x5987Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5975BO x5975BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5975BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        x5975BO.setCoreEventActivityRel(coreEventActivityRel);
        List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
                .get(WSC.ACTION_ARTIFACT_KEY);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5975BO.setGlobalEventNo(globalEventNo);
        x5975BO.setCoreEventActivityRel(coreEventActivityRel);
        x5975BO.setActivityArtifactList(activityArtifactList);
        return x5987Bus.busExecute(x5975BO);
    }
}
