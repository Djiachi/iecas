package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5335Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5330BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 异常交易操作
 * 
 * @author yanzhaofei 2019年1月10日
 * 
 */
@Service("X5335")
public class X5335ActionImpl implements ActionService{
    @Autowired
    private X5335Bus x5335Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5330BO x5330BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5330BO.class, Feature.DisableCircularReferenceDetect);
        return x5335Bus.busExecute(x5330BO);
    }

}
