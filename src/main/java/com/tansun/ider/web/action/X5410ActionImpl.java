package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5410BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5410BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
* @Description: 非金融維護歷史查詢
* @author: admin
 */
@Service("X5410")
public class X5410ActionImpl implements ActionService{
	

	@Autowired
	private X5410BusImpl x5410BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5410BO x5410bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5410BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5410bo.setGlobalEventNo(globalEventNo);
		x5410bo.setCoreEventActivityRel(coreEventActivityRel);
		
		return x5410BusImpl.busExecute(x5410bo);
	}

}
