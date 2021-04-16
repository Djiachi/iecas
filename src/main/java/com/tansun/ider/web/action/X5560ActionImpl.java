package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5560Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5560BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 利息计算详情查询
 * 
 * @author wt 2018年12月14日
 */
@Service("X5560")
public class X5560ActionImpl implements ActionService {

	@Autowired
	private X5560Bus x5560Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5560BO x5560BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5560BO.class, Feature.DisableCircularReferenceDetect);
		return x5560Bus.busExecute(x5560BO);
	}

}
