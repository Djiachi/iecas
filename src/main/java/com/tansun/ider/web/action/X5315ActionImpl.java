package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5315BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5315BO;
import com.tansun.ider.web.WSC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @version:1.0
* @Description: 媒介制卡信息修改(毁损补发)
* @author: admin  
 */
@Service("X5315")
public class X5315ActionImpl  implements ActionService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(X5315ActionImpl.class);
	
	@Autowired
	private X5315BusImpl x5315BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5315BO x5315Bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5315BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		@SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5315Bo.setActivityArtifactList(activityArtifactList);
		x5315Bo.setGlobalEventNo(globalEventNo);
		x5315Bo.setCoreEventActivityRel(coreEventActivityRel);
		return x5315BusImpl.busExecute(x5315Bo);
	}

}
