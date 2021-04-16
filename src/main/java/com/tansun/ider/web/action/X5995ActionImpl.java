package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5995BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5995BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 
* @ClassName: X5995ActionImpl 
* @Description: 强制结息（产品注销时实时结息）
* @author by
* @date 2019年8月20日 下午12:23:21 
*
 */
@Service("X5995")
public class X5995ActionImpl implements ActionService {

    @Resource
    private X5995BusImpl x5995BusImpl;
    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5995BO x5995BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5995BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5995BO.setGlobalEventNo(globalEventNo);
		x5995BO.setEventNo(coreEventActivityRel.getEventNo());
		x5995BO.setActivityNo(coreEventActivityRel.getActivityNo());
		x5995BO.setActivityArtifactList(activityArtifactList);
        return x5995BusImpl.busExecute(x5995BO);
    }
}
