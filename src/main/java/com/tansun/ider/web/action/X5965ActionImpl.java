package com.tansun.ider.web.action;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5965Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5965BO;
import com.tansun.ider.web.WSC;

@Service("X5965")
public class X5965ActionImpl implements ActionService {
    
    @Autowired
    private X5965Bus x5965Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5965BO x5965bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5965BO.class, Feature.DisableCircularReferenceDetect);
        return x5965Bus.busExecute(x5965bo);
    }
}
