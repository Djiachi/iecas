package com.tansun.ider.web.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5437Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5437BO;
import com.tansun.ider.web.WSC;

/**
 * 可分期账单查询
 * @author liuyanxi
 *
 */
@Service("X5437")
public class X5437ActionImpl implements ActionService {
    @Resource
    private X5437Bus x5437BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	X5437BO x5437BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5437BO.class, Feature.DisableCircularReferenceDetect);
        return x5437BusImpl.busExecute(x5437BO);
    }

}
