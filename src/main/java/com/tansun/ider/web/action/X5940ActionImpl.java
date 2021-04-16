package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5940BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5940BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 交易二次识别参数删除
 * @author lianhuan
 * 2018年9月14日
 */
@Service("X5940")
public class X5940ActionImpl implements ActionService {
	
	@Resource
	private X5940BusImpl x5940BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5940BO x5940BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5940BO.class, Feature.DisableCircularReferenceDetect);
		return x5940BusImpl.busExecute(x5940BO);
	}

}
