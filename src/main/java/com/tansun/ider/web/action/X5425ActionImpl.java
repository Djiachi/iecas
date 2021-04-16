package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5425Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5425BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户业务项目建立
 * @author: admin
 */
@Service("X5425")
public class X5425ActionImpl  implements ActionService {
	
	@Resource
	private X5425Bus x5425Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		
		// 1. 将json反序列化成实体对象，事件公共区
		X5425BO x5425BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5425BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5425BO.setGlobalEventNo(globalEventNo);
		x5425BO.setCoreEventActivityRel(coreEventActivityRel);
		x5425BO.setEventNo(coreEventActivityRel.getEventNo());
		x5425BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5425Bus.busExecute(x5425BO);
	}
	
}
