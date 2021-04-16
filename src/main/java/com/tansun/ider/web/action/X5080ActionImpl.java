package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5080BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5080BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 转卡处理
 * @author: admin
 */
@Service("X5080")
public class X5080ActionImpl implements ActionService {

	@Resource
	private X5080BusImpl x5080BusImpl;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5080BO x5080BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5080BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5080BO.setGlobalEventNo(globalEventNo);
		x5080BO.setCoreEventActivityRel(coreEventActivityRel);
		x5080BO.setActivityArtifactList(activityArtifactList);
		x5080BO.setEventNo(coreEventActivityRel.getEventNo());
		x5080BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5080BusImpl.busExecute(x5080BO);
	}

}
