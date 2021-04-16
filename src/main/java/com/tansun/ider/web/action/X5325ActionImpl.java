package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5325BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5325BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @version:1.0
* @Description: 客户业务项目修改
* @author: admin  
 */
@Service("X5325")
public class X5325ActionImpl implements ActionService {

	@Autowired
	private X5325BusImpl x5325BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5325BO x5325Bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5325BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		@SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5325Bo.setActivityArtifactList(activityArtifactList);
		x5325Bo.setGlobalEventNo(globalEventNo);
		x5325Bo.setCoreEventActivityRel(coreEventActivityRel);
		x5325Bo.setEventNo(coreEventActivityRel.getEventNo());
		x5325Bo.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5325BusImpl.busExecute(x5325Bo);
	}
	
}
