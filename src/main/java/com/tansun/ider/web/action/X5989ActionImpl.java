package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5977Bus;
import com.tansun.ider.bus.X5989Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5631BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author: PanQi
 * @Date: 2020/05/12
 * @updater:
 * @description: 资产证券化查询 分期子账户
 *
 */
@Service("X5989")
public class X5989ActionImpl implements ActionService {

    @Autowired
    private X5989Bus x5977Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5631BO x5631BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5631BO.class, Feature.DisableCircularReferenceDetect);
        return x5977Bus.busExecute(x5631BO);
    }

}
