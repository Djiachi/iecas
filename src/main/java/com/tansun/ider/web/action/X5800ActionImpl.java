package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5800Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5800BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @version:1.0
 * @Description: 提前续卡
 */
@Service("X5800")
public class X5800ActionImpl implements ActionService {

	@Autowired
	private X5800Bus x5800Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5800BO x5800BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5800BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5800BO.setGlobalEventNo(globalEventNo);
		x5800BO.setCoreEventActivityRel(coreEventActivityRel);
		x5800BO.setActivityArtifactList(activityArtifactList);
		x5800BO.setEventNo(coreEventActivityRel.getEventNo());
		x5800BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5800Bus.busExecute(x5800BO);
	}

}
