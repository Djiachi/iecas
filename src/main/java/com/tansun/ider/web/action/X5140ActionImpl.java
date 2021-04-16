package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5140BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5140BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户备注信息维护
 * @author: admin
 */
@Service("X5140")
public class X5140ActionImpl implements ActionService {
	
	@Autowired
	private X5140BusImpl x5140BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5140BO x5140BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5140BO.class, Feature.DisableCircularReferenceDetect);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5140BO.setGlobalEventNo(globalEventNo);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		x5140BO.setEventNo(coreEventActivityRel.getEventNo());
		x5140BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5140BusImpl.busExecute(x5140BO);
	}

}
