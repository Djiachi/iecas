package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5570Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5570BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 发生额关联交易查询
 * @author: admin
 */
@Service("X5570")
public class X5570ActionImpl implements ActionService {

	@Autowired
	private X5570Bus x5570Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5570BO x5570BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5570BO.class, Feature.DisableCircularReferenceDetect);
		return x5570Bus.busExecute(x5570BO);
	}

}
