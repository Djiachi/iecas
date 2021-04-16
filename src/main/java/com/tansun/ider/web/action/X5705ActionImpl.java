package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5705Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5705BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * VISA调单申请建立
 * 
 * @ClassName X5705ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年1月253日 下午5:36:09
 * @version 1.0.0
 */
@Service("X5705")
public class X5705ActionImpl implements ActionService {
	
	@Autowired
	private X5705Bus x5705BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5705BO x5705bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5705BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5705bo.setGlobalEventNo(globalEventNo);
		x5705bo.setCoreEventActivityRel(coreEventActivityRel);
		x5705bo.setEventNo(coreEventActivityRel.getEventNo());
		x5705bo.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5705BusImpl.busExecute(x5705bo);
	}

}