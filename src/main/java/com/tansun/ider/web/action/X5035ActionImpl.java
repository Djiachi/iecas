package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5035BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5035BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 产品附加信息新建
 * @author: admin
 */
@Service("X5035")
public class X5035ActionImpl implements ActionService {

	@Autowired
	private X5035BusImpl x5035BusImpl;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5035BO x5035BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5035BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5035BO.setGlobalEventNo(globalEventNo);
		x5035BO.setCoreEventActivityRel(coreEventActivityRel);
		//
		if(coreEventActivityRel!=null){
			x5035BO.setEventNo(coreEventActivityRel.getEventNo());
			x5035BO.setActivityNo(coreEventActivityRel.getActivityNo());
		}
		return x5035BusImpl.busExecute(x5035BO);
	}

}
