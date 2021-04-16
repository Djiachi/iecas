package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4020Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4020BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 保存预算单位单位员工关系信息
 * @author: cuiguangchao
 */
@Service("X4020")
public class X4020ActionImpl implements ActionService {

    @Autowired
    private X4020Bus x4020Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4020BO x4020BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4020BO.class, Feature.DisableCircularReferenceDetect);
        return x4020Bus.busExecute(x4020BO);
    }

}
