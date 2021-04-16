package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5350Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5350BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 媒介生命周期表创建
 * 
 * @author wt 2018年10月15日
 */
@Service("X5350")
public class X5350ActionImpl implements ActionService {

    @Autowired
    private X5350Bus x5350Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5350BO x5350BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5350BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5350BO.setGlobalEventNo(globalEventNo);
		x5350BO.setCoreEventActivityRel(coreEventActivityRel);
		x5350BO.setEventNo(coreEventActivityRel.getEventNo());
		x5350BO.setActivityNo(coreEventActivityRel.getActivityNo());
        
        return x5350Bus.busExecute(x5350BO);
	}
	
}
