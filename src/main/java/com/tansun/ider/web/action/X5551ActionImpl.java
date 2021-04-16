package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5551Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5551BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service("X5551")
public class X5551ActionImpl implements ActionService {
    @Autowired
    private X5551Bus x5551Bus;
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5551BO x5551BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5551BO.class, Feature.DisableCircularReferenceDetect);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        return x5551Bus.busExecute(x5551BO);
    }
}
