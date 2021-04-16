package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5985BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5980BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产证券化查询
 */
@Service("X5985")
public class X5985ActionImpl implements ActionService {

    @Resource
    private X5985BusImpl x5985BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5980BO x5982BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5980BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5982BO.setGlobalEventNo(globalEventNo);
        x5982BO.setEventNo(coreEventActivityRel.getEventNo());
        x5982BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x5985BusImpl.busExecute(x5982BO);
    }

}
