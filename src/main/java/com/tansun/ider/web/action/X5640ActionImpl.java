package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5640Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5640BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 分期账户信息+支付计划信息查询
 * @author liuyanxi
 *
 */
@Service("X5640")
public class X5640ActionImpl implements ActionService {

    @Autowired
    private X5640Bus x5640Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5640BO x5640BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5640BO.class, Feature.DisableCircularReferenceDetect);
        return x5640Bus.busExecute(x5640BO);
    }

}
