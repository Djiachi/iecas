package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5434Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5435BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 延滞冲减查询
 * @author qianyp
 */
@Service("X5434")
public class X5434ActionImpl implements ActionService {

    @Autowired
    private X5434Bus x5434bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5435BO x5435BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5435BO.class, Feature.DisableCircularReferenceDetect);
        return x5434bus.busExecute(x5435BO);
    }
}
