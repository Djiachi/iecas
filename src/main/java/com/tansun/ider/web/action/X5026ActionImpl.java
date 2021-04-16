package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5026Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5026BO;
import com.tansun.ider.web.WSC;

/**
 * 客户增值服务费新增
 * @author admin
 *
 */
@Service("X5026")
public class X5026ActionImpl implements ActionService{

	@Autowired
	private X5026Bus x5026Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5026BO x5026BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5026BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5026BO.setGlobalEventNo(globalEventNo);
		x5026BO.setCoreEventActivityRel(coreEventActivityRel);
		x5026BO.setEventNo(coreEventActivityRel.getEventNo());
		x5026BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5026Bus.busExecute(x5026BO);
	}

}
