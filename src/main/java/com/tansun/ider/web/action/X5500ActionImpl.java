package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5500Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5500BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 查询账户余额对象
 * 
 * @author lianhuan 2018年9月14日
 */
@Service("X5500")
public class X5500ActionImpl implements ActionService {

    @Resource
    private X5500Bus x5500Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5500BO x5500BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5500BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5500BO.setGlobalEventNo(globalEventNo);
		x5500BO.setCoreEventActivityRel(coreEventActivityRel);
		
        return x5500Bus.busExecute(x5500BO);
    }

}
