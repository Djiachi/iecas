package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5046Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5026BO;
import com.tansun.ider.model.bo.X5046BO;
import com.tansun.ider.web.WSC;

/**
 * 产品增值服务费
 * @author admin
 *
 */
@Service("X5046")
public class X5046ActionImpl implements ActionService {

	@Autowired
	private X5046Bus x5046Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5046BO x5046BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5046BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5046BO.setGlobalEventNo(globalEventNo);
		x5046BO.setCoreEventActivityRel(coreEventActivityRel);
		x5046BO.setEventNo(coreEventActivityRel.getEventNo());
		x5046BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5046Bus.busExecute(x5046BO);
	}
	
}
