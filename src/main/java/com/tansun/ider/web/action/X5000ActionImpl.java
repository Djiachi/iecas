package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5000BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5000BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Desc:客户类信息建立
 * @Author admin
 * @Date 2018年4月24日 下午4:41:53
 */
@Service("X5000")
public class X5000ActionImpl implements ActionService {

	@Autowired
	private X5000BusImpl x5000BusImpl;

	/**
	 * X5000 客户基本信息新增活动设计
	 */
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5000BO x5000BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5000BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
    	// 全局事件流水6号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5000BO.setGlobalEventNo(globalEventNo);
        x5000BO.setCoreEventActivityRel(coreEventActivityRel);
        x5000BO.setEventNo(coreEventActivityRel.getEventNo());
        x5000BO.setActivityNo(coreEventActivityRel.getActivityNo());
        
		return x5000BusImpl.busExecute(x5000BO);
	}

}
