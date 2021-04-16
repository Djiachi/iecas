package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5268Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5268BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 查询统一利率
 * @author qianyp
 */
@Service("X5268")
public class X5268ActionImpl implements ActionService {
    
    @Resource
    private X5268Bus x5268Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5268BO x5268bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5268BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		x5268bo.setCoreEventActivityRel(coreEventActivityRel);
        return x5268Bus.busExecute(x5268bo);
    }
}
