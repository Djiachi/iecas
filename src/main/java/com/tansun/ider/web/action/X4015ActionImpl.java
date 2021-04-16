package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4015Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4015BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 查询个人公务卡最大限额
 * @author: cuiguangchao
 */
@Service("X4015")
public class X4015ActionImpl implements ActionService {

    @Autowired
    private X4015Bus x4015Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4015BO x4015BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4015BO.class, Feature.DisableCircularReferenceDetect);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x4015BO.setGlobalEventNo(globalEventNo);
        return x4015Bus.busExecute(x4015BO);
    }

}
