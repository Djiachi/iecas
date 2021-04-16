package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5005BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5005BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户序号规则表中创建
 * @author: admin
 */
@Service("X5005")
public class X5005ActionImpl implements ActionService {

	@Autowired
	private X5005BusImpl x5005BusImpl;

	/**
	 * 客户序号规则表活动创建
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5005BO x5005BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5005BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5005BO.setGlobalEventNo(globalEventNo);
		x5005BO.setCoreEventActivityRel(coreEventActivityRel);
		x5005BO.setEventNo(coreEventActivityRel.getEventNo());
		x5005BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5005BusImpl.busExecute(x5005BO);
	}

}
