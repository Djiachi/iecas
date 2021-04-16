package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5962Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5962BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * 
* @ClassName: X5962ActionImpl  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author Administrator  
* @date 2019年3月13日  
*
 */
@Service("X5962")
public class X5962ActionImpl implements ActionService {
    public static String tableName = "Core";
    @Autowired
    private X5962Bus x5962Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	// 1. 将json反序列化成实体对象，事件公共区
		X5962BO x5962BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5962BO.class, Feature.DisableCircularReferenceDetect);
		return x5962Bus.updateCoreCustomerWaiveFeeInfo(x5962BO);
    }

}
