package com.tansun.ider.web.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5051Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5051BO;
import com.tansun.ider.web.WSC;

/**
 * 按照产品币种授信
 * @author admin
 *
 */
@Service("X5051")
public class X5051ActionImpl implements ActionService {

	@Autowired
	private X5051Bus X5051Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5051BO x5051BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5051BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5051BO.setGlobalEventNo(globalEventNo);	
		x5051BO.setEventNo(coreEventActivityRel.getEventNo());
		x5051BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return X5051Bus.busExecute(x5051BO);
	}
	
}
