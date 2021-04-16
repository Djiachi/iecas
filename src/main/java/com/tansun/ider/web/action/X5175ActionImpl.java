package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5175BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5175BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 新媒介绑定
 * @author: admin
 */
@Service("X5175")
public class X5175ActionImpl implements ActionService {

	@Autowired
	private X5175BusImpl x5175BusImpl;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5175BO x5175BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5175BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);   //事件活动事件关联表
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);

		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5175BO.setGlobalEventNo(globalEventNo);
		x5175BO.setCoreEventActivityRel(coreEventActivityRel);
		x5175BO.setActivityArtifactList(activityArtifactList);
		x5175BO.setEventNo(coreEventActivityRel.getEventNo());
		x5175BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5175BusImpl.busExecute(x5175BO);
	}

}
