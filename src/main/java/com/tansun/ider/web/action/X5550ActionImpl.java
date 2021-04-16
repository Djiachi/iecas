package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5550Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5550BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 查询客户交易统计信息
 * 
 * @author lianhuan 2018年10月25日
 */
@Service("X5550")
public class X5550ActionImpl  implements ActionService {
	
	@Autowired
	private X5550Bus x5550Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        X5550BO x5550BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5550BO.class, Feature.DisableCircularReferenceDetect);
        return x5550Bus.busExecute(x5550BO);
	}
	
}
