package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5435Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5435BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 交易历史查询
 * 
 * @author lianhuan 2018年10月15日
 */
@Service("X5435")
public class X5435ActionImpl implements ActionService {

    @Autowired
    private X5435Bus x5435Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5435BO x5435BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5435BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5435BO.setGlobalEventNo(globalEventNo);
		x5435BO.setCoreEventActivityRel(coreEventActivityRel);
		
        return x5435Bus.busExecute(x5435BO);
    }

}