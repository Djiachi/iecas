package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4095Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4095BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("X4095")
public class X4095ActionImpl implements ActionService {

	@Autowired
	private X4095Bus X4095BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		
		X4095BO x4095BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4095BO.class, Feature.DisableCircularReferenceDetect);
		return X4095BusImpl.busExecute(x4095BO);
	}

}
