package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5260BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5260BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Desc:统一利率
 * @Author yuanyanjiao
 * @Date 2018年9月28日 上午9:57:18
 */
@Service("X5260")
public class X5260ActionImpl implements ActionService {

	@Autowired
	private X5260BusImpl x5260BusImpl;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5260BO x5260BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5260BO.class, Feature.DisableCircularReferenceDetect);
		return x5260BusImpl.busExecute(x5260BO);
	}
}
