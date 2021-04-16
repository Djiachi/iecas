package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5280Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5280BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 查询产品是否支持自动配号
 * @author admin
 *
 */
@Service("X5280")
public class X5280ActionImpl implements ActionService {

	@Autowired
	private X5280Bus x5280Bus;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5280BO x5280BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5280BO.class, Feature.DisableCircularReferenceDetect);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		x5280BO.setActivityArtifactList(activityArtifactList);
		return x5280Bus.busExecute(x5280BO);
	}
	
}
