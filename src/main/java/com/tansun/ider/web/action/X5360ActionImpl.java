package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5360BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5360BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 更新媒介有效期
 * @author: admin
 */
@Service("X5360")
public class X5360ActionImpl implements ActionService {

	@Autowired
	private X5360BusImpl x5360BusImpl;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5360BO x5360BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5360BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5360BO.setGlobalEventNo(globalEventNo);
		x5360BO.setCoreEventActivityRel(coreEventActivityRel);
		x5360BO.setActivityArtifactList(activityArtifactList);
		x5360BO.setEventNo(coreEventActivityRel.getEventNo());
		x5360BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5360BusImpl.busExecute(x5360BO);
	}

}
