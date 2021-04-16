package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5070BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5070BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 转卡列表查询
 * @author: admin
 */
@Service("X5070")
public class X5070ActionImpl implements ActionService {

	@Resource
	private X5070BusImpl x5070BusImpl;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5070BO x5070BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5070BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5070BO.setGlobalEventNo(globalEventNo);
		x5070BO.setCoreEventActivityRel(coreEventActivityRel);
		x5070BO.setActivityArtifactList(activityArtifactList);
		
		return x5070BusImpl.busExecute(x5070BO);
	}

}
