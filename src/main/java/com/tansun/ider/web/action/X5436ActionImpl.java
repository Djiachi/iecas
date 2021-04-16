package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5436Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5436BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 关联交易历史查询
 * 
 * @author huangyayun 2018年10月15日
 */
@Service("X5436")
public class X5436ActionImpl implements ActionService {

    @Autowired
    private X5436Bus x5436Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5436BO x5436BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5436BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5436BO.setGlobalEventNo(globalEventNo);
		x5436BO.setCoreEventActivityRel(coreEventActivityRel);
		
        return x5436Bus.busExecute(x5436BO);
    }

}
