package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4050Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4050BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("X4050")
public class X4050ActionImpl implements ActionService{

	@Autowired
	private X4050Bus x4050bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X4050BO x4050BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4050BO.class, Feature.DisableCircularReferenceDetect);
		return x4050bus.busExecute(x4050BO);
	}

}
