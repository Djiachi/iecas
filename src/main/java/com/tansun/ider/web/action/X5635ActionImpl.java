package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5635Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5635BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 累计利息查询
 * 
 * @author admin
 *
 */
@Service("X5635")
public class X5635ActionImpl implements ActionService {
	
	@Autowired
	private X5635Bus x5635Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		 X5635BO x5635BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5635BO.class, Feature.DisableCircularReferenceDetect);
	     return x5635Bus.busExecute(x5635BO);
	}
	
}
