package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5996Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5345BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 最新周期号客户统一日期查询
 * 
 * @author gaozhennan
 */
@Service("X5996")
public class X5996ActionImpl implements ActionService {
	
    @Autowired
    private X5996Bus x5996Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5345BO x5345BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5345BO.class, Feature.DisableCircularReferenceDetect);
        return x5996Bus.busExecute(x5345BO);
	}
}
