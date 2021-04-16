package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5055BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5055BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
* @Description: 媒介制卡信息新建
* @author: admin  
 */
@Service("X5055")
public class X5055ActionImpl implements ActionService {
    
    @Autowired
    private X5055BusImpl x5055BusImpl;

    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5055BO x5055BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5055BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5055BO.setGlobalEventNo(globalEventNo);
		x5055BO.setCoreEventActivityRel(coreEventActivityRel);
		x5055BO.setEventNo(coreEventActivityRel.getEventNo());
		x5055BO.setActivityNo(coreEventActivityRel.getActivityNo());
    	
        return x5055BusImpl.busExecute(x5055BO);
    }

}
