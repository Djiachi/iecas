package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5615Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5615BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 分期账户信息+分期计划信息查询
 * ILS.IQ.01.0015
 * @author huangyayun 2018年10月29日
 */
@Service("X5615")
public class X5615ActionImpl implements ActionService {

    @Autowired
    private X5615Bus x5615Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5615BO x5615BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5615BO.class, Feature.DisableCircularReferenceDetect);
        return x5615Bus.busExecute(x5615BO);
    }

}
