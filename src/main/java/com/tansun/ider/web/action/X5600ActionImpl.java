package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5600Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5600BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 可分期金额统计
 * ILS.IQ.01.0001
 * @author huangyayun 2018年10月29日
 */
@Service("X5600")
public class X5600ActionImpl implements ActionService {

    @Autowired
    private X5600Bus x5600Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5600BO x5600BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5600BO.class, Feature.DisableCircularReferenceDetect);
        return x5600Bus.busExecute(x5600BO);
    }

}
