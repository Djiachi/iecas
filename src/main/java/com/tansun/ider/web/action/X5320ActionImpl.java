package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5320BusImpl;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5320BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @version:1.0
* @Description: 客户业务项目查询
* @author: admin
 */
@Service("X5320")
public class X5320ActionImpl implements ActionService {
	
	@Resource
	private X5320BusImpl x5320BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5320BO x5320BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5320BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		@SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5320BO.setActivityArtifactList(activityArtifactList);
		x5320BO.setGlobalEventNo(globalEventNo);
		x5320BO.setCoreEventActivityRel(coreEventActivityRel);
		
		if (CoreCustomerBillDay.class.getName().equals(x5320BO.getQueryTableName())) {
			return x5320BusImpl.queryCustomerBillDay(x5320BO);
		}else{
			return x5320BusImpl.busExecute(x5320BO);
		}
	}
	
}
