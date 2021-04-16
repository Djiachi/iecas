package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5205BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5205BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 媒介绑定信息修改
 * @author: admin
 */
@Service("X5205")
public class X5205ActionImpl implements ActionService {

	@Autowired
	private X5205BusImpl x5205BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5205BO x5205BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5205BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5205BO.setGlobalEventNo(globalEventNo);
		x5205BO.setCoreEventActivityRel(coreEventActivityRel);
		x5205BO.setEventNo(coreEventActivityRel.getEventNo());
		x5205BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5205BusImpl.busExecute(x5205BO);
	}

}
