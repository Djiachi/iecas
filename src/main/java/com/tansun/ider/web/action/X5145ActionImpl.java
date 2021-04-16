package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5145BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5145BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
* @Description:产品线账单日信息维护
* @author: admin
 */
@Service("X5145")
public class X5145ActionImpl implements ActionService{

	@Autowired
	private X5145BusImpl x5145BusImpl;
	
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5145BO x5145BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5145BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		x5145BO.setEventNo(coreEventActivityRel.getEventNo());
		x5145BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5145BusImpl.busExecute(x5145BO);
	}
	
}
