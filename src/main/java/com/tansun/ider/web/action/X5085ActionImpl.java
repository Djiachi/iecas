package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5085BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5085BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 客户资料维护
 * @author: admin
 */
@Service("X5085")
public class X5085ActionImpl implements ActionService {

	@Resource
	private X5085BusImpl x5085BusImpl;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5085BO x5085BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5085BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5085BO.setGlobalEventNo(globalEventNo);
		x5085BO.setCoreEventActivityRel(coreEventActivityRel);
		x5085BO.setEventNo(coreEventActivityRel.getEventNo());
		x5085BO.setActivityNo(coreEventActivityRel.getActivityNo());
		
		return x5085BusImpl.busExecute(x5085BO);
	}

}
