package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5385Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5385BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 账单产品对象维度查询
 * @author huangyayun
 *
 * @date 2018年8月13日
 */
@Service("X5385")
public class X5385ActionImpl implements ActionService {
    @Resource
    private X5385Bus x5385BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	X5385BO x5385BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5385BO.class, Feature.DisableCircularReferenceDetect);
        return x5385BusImpl.busExecute(x5385BO);
    }

}
