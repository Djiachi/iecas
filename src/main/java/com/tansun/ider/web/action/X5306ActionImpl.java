package com.tansun.ider.web.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5306BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5306BO;
import com.tansun.ider.web.WSC;

/**
 * @version:1.0
 * @Description: 媒介基本信息查询（根据媒介单元查）
 * @author:wangxi
 */
@Service("X5306")
public class X5306ActionImpl implements ActionService {

	@Resource
	private X5306BusImpl x5306BusImpl;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5306BO x5306BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5306BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		@SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5306BO.setActivityArtifactList(activityArtifactList);
		x5306BO.setGlobalEventNo(globalEventNo);
		x5306BO.setCoreEventActivityRel(coreEventActivityRel);
		
		return x5306BusImpl.busExecute(x5306BO);
	}

}
