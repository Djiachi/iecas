package com.tansun.ider.web.action;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5811Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5811BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("X5811")
public class X5811ActionImpl implements ActionService{

	@Autowired
	private X5811Bus x5811Bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5811BO x5811bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5811BO.class, Feature.DisableCircularReferenceDetect);
		return x5811Bus.busExecute(x5811bo);
	}

}
