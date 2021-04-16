package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5050BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5050BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 媒介单元基本信息新建
 * @author: admin
 */
@Service("X5050")
public class X5050ActionImpl implements ActionService {

	@Autowired
	private X5050BusImpl x5050BusImpl;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5050BO x5050BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5050BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5050BO.setGlobalEventNo(globalEventNo);
		x5050BO.setCoreEventActivityRel(coreEventActivityRel);
		x5050BO.setActivityArtifactList(activityArtifactList);
		x5050BO.setEventNo(coreEventActivityRel.getEventNo());
		x5050BO.setActivityNo(coreEventActivityRel.getActivityNo());

		return x5050BusImpl.busExecute(x5050BO);
	}

}
