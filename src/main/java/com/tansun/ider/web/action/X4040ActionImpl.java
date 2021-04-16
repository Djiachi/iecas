package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4040Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4040BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @version:1.0
 * @Description: 个人公务卡最大额度查询
 * @author: cuiguangchao
 */
@Service("X4040")
public class X4040ActionImpl implements ActionService {

    @Autowired
    private X4040Bus x4040Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4040BO x4040BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4040BO.class, Feature.DisableCircularReferenceDetect);
        return x4040Bus.busExecute(x4040BO);
    }

}
