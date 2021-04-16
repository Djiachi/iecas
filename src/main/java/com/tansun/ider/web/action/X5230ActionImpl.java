package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5230BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5230BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户媒介列表查询
 * @author: admin
 */
@Service("X5230")
public class X5230ActionImpl implements ActionService {

	@Autowired
	private X5230BusImpl x5230BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5230BO x5230BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5230BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5230BO.setGlobalEventNo(globalEventNo);
		x5230BO.setCoreEventActivityRel(coreEventActivityRel);
		
		return x5230BusImpl.busExecute(x5230BO);
	}

}