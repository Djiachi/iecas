package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5625Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5625BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("X5625")
public class X5625ActionImpl implements ActionService{

	@Autowired
	private X5625Bus x5625bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5625BO x5625BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5625BO.class, Feature.DisableCircularReferenceDetect);
		return x5625bus.busExecute(x5625BO);
	}

}
