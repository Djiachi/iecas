package com.tansun.ider.web.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.impl.X5431BusImpl;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5430BO;
import com.tansun.ider.web.WSC;

/**
 * 客户定价视图查询
 * @author zhangte
 * @date 2019年8月7日
 */
@Service("X5431")
public class X5431ActionImpl implements ActionService {

	@Resource
	private X5431BusImpl x5431BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5430BO x5430BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5430BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5430BO.setGlobalEventNo(globalEventNo);
		x5430BO.setCoreEventActivityRel(coreEventActivityRel);
		if(StringUtil.isNotBlank(x5430BO.getFlag())){
			return x5431BusImpl.pricingBusExecute(x5430BO);
		}else{
			if(StringUtil.isBlank(x5430BO.getQueryFlag())){
				return x5431BusImpl.busExecute(x5430BO);
			}else{
				return x5431BusImpl.instanBusExecute(x5430BO);
			}
		}
	}
}
