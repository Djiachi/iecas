package com.tansun.ider.web.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5555Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5555BO;
import com.tansun.ider.web.WSC;

/**
 * 利息交易查询
 * 
 * @author wt 2018年11月13日
 */
@Service("X5555")
public class X5555ActionImpl implements ActionService {

	@Autowired
	private X5555Bus x5555Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		 X5555BO x5555BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5555BO.class, Feature.DisableCircularReferenceDetect);
		 CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		 List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
					.get(WSC.ACTION_ARTIFACT_KEY);
		 x5555BO.setCoreEventActivityRel(coreEventActivityRel);
		 x5555BO.setActivityArtifactList(activityArtifactList);
		 return x5555Bus.busExecute(x5555BO);
	}
	
}
