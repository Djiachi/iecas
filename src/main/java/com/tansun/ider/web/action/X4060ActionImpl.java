package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4060Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4060BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service("X4060")
public class X4060ActionImpl implements ActionService{
	@Autowired
	private X4060Bus x4060bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X4060BO x4060BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4060BO.class, Feature.DisableCircularReferenceDetect);
		return x4060bus.busExecute(x4060BO);
	}
}
