package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5066BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5065BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户额度调整
 * @author: admin
 */
@Service("X5066")
public class X5066ActionImpl implements ActionService {

	@Autowired
	private X5066BusImpl x5066BusImpl;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5065BO x5065BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5065BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5065BO.setGlobalEventNo(globalEventNo);
		x5065BO.setCoreEventActivityRel(coreEventActivityRel);
		x5065BO.setEventNo(coreEventActivityRel.getEventNo());
		x5065BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5066BusImpl.busExecute(x5065BO);
	}

}
