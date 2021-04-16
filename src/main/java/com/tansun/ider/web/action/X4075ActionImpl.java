package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tansun.ider.bus.X4075Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4075BO;
import com.tansun.ider.web.WSC;

@Service("X4075")
public class X4075ActionImpl implements ActionService {

    @Autowired
    private X4075Bus x4075bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X4075BO x4075BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4075BO.class);
        return x4075bus.busExecute(x4075BO);
    }

}
