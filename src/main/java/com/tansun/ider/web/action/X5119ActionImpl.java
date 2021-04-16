package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5119BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5115BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 账户余额平衡查询
 * @author: gaozhennan
 */
@Service("X5119")
public class X5119ActionImpl implements ActionService {

    @Resource
    private X5119BusImpl x5119BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5115BO x5115BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5115BO.class, Feature.DisableCircularReferenceDetect);
//		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
//		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
//				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5115BO.setGlobalEventNo(globalEventNo);
//		x5115BO.setCoreEventActivityRel(coreEventActivityRel);
//		x5115BO.setActivityArtifactList(activityArtifactList);
        return x5119BusImpl.busExecute(x5115BO);
    }

}