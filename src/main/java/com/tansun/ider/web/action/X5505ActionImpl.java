package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5505Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5505BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 查询余额单元
 * 
 * @author lianhuan 2018年9月14日
 */
@Service("X5505")
public class X5505ActionImpl implements ActionService {

    @Resource
    private X5505Bus x5505Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5505BO x5505BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5505BO.class, Feature.DisableCircularReferenceDetect);
        return x5505Bus.busExecute(x5505BO);
    }

}
