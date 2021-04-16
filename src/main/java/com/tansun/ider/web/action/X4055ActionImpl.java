package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4055Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4055BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service("X4055")
public class X4055ActionImpl implements ActionService{
	@Autowired
	private X4055Bus x4055bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X4055BO x4055BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4055BO.class, Feature.DisableCircularReferenceDetect);
		return x4055bus.busExecute(x4055BO);
	}
}
