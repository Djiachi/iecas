package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5510BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5510BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 客户封锁码查询
 * 
 * @author wt 2018年9月14日
 */
@Service("X5510")
public class X5510ActionImpl  implements ActionService {

    @Resource
    private X5510BusImpl x5510BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        X5510BO x5510BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5510BO.class, Feature.DisableCircularReferenceDetect);
        return x5510BusImpl.busExecute(x5510BO);
	}

}
