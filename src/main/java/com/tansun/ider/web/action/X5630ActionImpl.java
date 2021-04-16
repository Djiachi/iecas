package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5630Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5630BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 信用卡分期账户信息查询
 * ILS.IQ.01.0020
 *
 * @author cuiguangchao 2018年10月29日
 */
@Service("X5630")
public class X5630ActionImpl implements ActionService {

    @Autowired
    private X5630Bus x5630Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5630BO x5630BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5630BO.class, Feature.DisableCircularReferenceDetect);
        return x5630Bus.busExecute(x5630BO);
    }

}
