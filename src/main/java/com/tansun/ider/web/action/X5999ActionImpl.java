package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5999Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5999BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
* @ClassName: X5999ActionImpl 
* @Description:还款分配顺序查询
* @author by
* @date 2019年11月17日 下午4:47:25 
*
 */
@Service("X5999")
public class X5999ActionImpl implements ActionService {
	
    @Autowired
    private X5999Bus x5999Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5999BO x5999BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5999BO.class, Feature.DisableCircularReferenceDetect);
        return x5999Bus.busExecute(x5999BO);
	}
}
