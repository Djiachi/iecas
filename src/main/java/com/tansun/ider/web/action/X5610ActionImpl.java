package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5610Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5610BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 消贷分期账户信息查询
 * ILS.IQ.01.0010
 * 
 * @author huangyayun 2018年10月29日
 */
@Service("X5610")
public class X5610ActionImpl implements ActionService {

    @Autowired
    private X5610Bus x5610Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5610BO x5610BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5610BO.class, Feature.DisableCircularReferenceDetect);
        String eventId = map.get(WSC.EVENT_ID).toString().split("-")[0];
        x5610BO.setEventId(eventId);
        return x5610Bus.busExecute(x5610BO);
    }

}
