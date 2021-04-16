package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5565Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5565BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 历史试算详情
 * @author wt
 *
 */
@Service("X5565")
public class X5565ActionImpl implements ActionService {
	
	@Autowired
	private X5565Bus x5565Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5565BO x5565BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5565BO.class, Feature.DisableCircularReferenceDetect);
		return x5565Bus.busExecute(x5565BO);
	}

}
