package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5135BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5135BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户基本信息维护
 * @author: admin
 */
@Service("X5135")
public class X5135ActionImpl implements ActionService{

	@Autowired
	private X5135BusImpl x5135BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5135BO x5135BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5135BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5135BO.setGlobalEventNo(globalEventNo);
		x5135BO.setCoreEventActivityRel(coreEventActivityRel);
		x5135BO.setEventNo(coreEventActivityRel.getEventNo());
		x5135BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		
		return x5135BusImpl.busExecute(x5135BO);
	}

}
