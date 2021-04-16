package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5552Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5551BO;
import com.tansun.ider.model.bo.X5552BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("X5552")
public class X5552ActionImpl implements ActionService {
    @Autowired
    private X5552Bus x5552Bus;
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5552BO x5552BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5552BO.class, Feature.DisableCircularReferenceDetect);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        return x5552Bus.busExecute(x5552BO);
    }
}
