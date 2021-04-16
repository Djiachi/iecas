package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5645Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5645BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 最近一期未支付计划信息查询
 * @author liuyanxi
 *
 */
@Service("X5645")
public class X5645ActionImpl implements ActionService {

    @Autowired
    private X5645Bus x5645Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5645BO x5645BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5645BO.class, Feature.DisableCircularReferenceDetect);
        return x5645Bus.busExecute(x5645BO);
    }

}
