package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5235BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5235BO;
import com.tansun.ider.web.WSC;


/**
 * 旧卡有效期是否更新判断
 * @author wt
 *
 */
@Service("X5235")
public class X5235ActionImpl implements ActionService {

	@Autowired
	private X5235BusImpl x5235BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5235BO x5235BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5235BO.class, Feature.DisableCircularReferenceDetect);
		return x5235BusImpl.busExecute(x5235BO);
	}
	
}
