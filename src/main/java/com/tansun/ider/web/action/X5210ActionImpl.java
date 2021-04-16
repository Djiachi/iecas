package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5210BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5210BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 媒介注销
 * @author: admin
 */
@Service("X5210")
public class X5210ActionImpl implements ActionService {

	@Resource
	private X5210BusImpl x5210BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5210BO x5210BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5210BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5210BO.setGlobalEventNo(globalEventNo);
		x5210BO.setCoreEventActivityRel(coreEventActivityRel);
		x5210BO.setEventNo(coreEventActivityRel.getEventNo());
		x5210BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5210BusImpl.busExecute(x5210BO);
	}

}
