package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5075BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5075BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 转卡媒介资料查询
 * @author: admin
 */
@Service("X5075")
public class X5075ActionImpl implements ActionService {
	
	@Resource
	private X5075BusImpl x5075BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5075BO x5075BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5075BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5075BO.setGlobalEventNo(globalEventNo);
		x5075BO.setCoreEventActivityRel(coreEventActivityRel);
		x5075BO.setActivityArtifactList(activityArtifactList);
		
		return x5075BusImpl.busExecute(x5075BO);
	}

}
