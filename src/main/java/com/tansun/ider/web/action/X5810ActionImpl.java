package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5810BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5810BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Desc:客户产品注销
 * @Author wangxi
 * @Date 2018年4月24日 下午4:41:53
 */
@Service("X5810")
public class X5810ActionImpl implements ActionService {

	@Autowired
	private X5810BusImpl x5810BusImpl;

	/**
	 * X5810 客户产品注销事件
	 */
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5810BO x5810BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5810BO.class, Feature.DisableCircularReferenceDetect);
        @SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> artifactList = (List<CoreActivityArtifactRel>) map.get(WSC.ACTION_ARTIFACT_KEY);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
    	// 全局事件流水6号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5810BO.setGlobalEventNo(globalEventNo);
        x5810BO.setCoreEventActivityRel(coreEventActivityRel);
        x5810BO.setEventNo(coreEventActivityRel.getEventNo());
        x5810BO.setActivityNo(coreEventActivityRel.getActivityNo());
        x5810BO.setActivityArtifactList(artifactList);
        
		return x5810BusImpl.busExecute(x5810BO);
	}

}
