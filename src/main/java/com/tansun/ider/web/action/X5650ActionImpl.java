package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5650Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5650BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 消贷分期账户查询
 * @author liuyanxi
 *
 */
@Service("X5650")
public class X5650ActionImpl implements ActionService {

    @Autowired
    private X5650Bus x5650Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5650BO x5650BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5650BO.class, Feature.DisableCircularReferenceDetect);
        return x5650Bus.busExecute(x5650BO);
    }

}
