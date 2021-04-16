package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5310Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5310BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 判断产品对象对应的，业务项目是否需要输入账单日
 * @author wt
 *
 */
@Service("X5310")
public class X5310ActionImpl implements ActionService{

	@Autowired
	private X5310Bus x5310Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5310BO x5310BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5310BO.class, Feature.DisableCircularReferenceDetect);
		return x5310Bus.busExecute(x5310BO);
	}

}
