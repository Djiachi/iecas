package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5994Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5345BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 修改最后还款日和宽限日
 * @ClassName X5994ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author ga0zhennan
 * @Date 2019.9.10
 * @version 1.0.0
 */
@Service("X5994")
public class X5994ActionImpl implements ActionService {

    @Autowired
    private X5994Bus x5994Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5345BO x5345BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5345BO.class, Feature.DisableCircularReferenceDetect);

		List<CoreActivityArtifactRel> artifactList = (List<CoreActivityArtifactRel>) map.get(WSC.ACTION_ARTIFACT_KEY);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        x5345BO.setArtifactList(artifactList);

        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5345BO.setGlobalEventNo(globalEventNo);
        x5345BO.setEventNo(coreEventActivityRel.getEventNo());
        x5345BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x5994Bus.busExecute(x5345BO);
	}
	
}
