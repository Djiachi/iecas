package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5976Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5630BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author: PanQi
 * @Date: 2020/04/07
 * @updater:
 * @description: 资产选择账户查询
 * 信用卡分期账户信息查询 BSS.IQ.01.0335
 */
@Service("X5976")
public class X5976ActionImpl implements ActionService {

    @Autowired
    private X5976Bus x5976Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5630BO x5630BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5630BO.class, Feature.DisableCircularReferenceDetect);
        return x5976Bus.busExecute(x5630BO);
    }

}
