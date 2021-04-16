package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5535Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5535BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 查询计息控制链表
 * 
 * @author lianhuan 2018年10月25日
 */
@Service("X5535")
public class X5535ActionImpl implements ActionService {

    @Autowired
    private X5535Bus x5535Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5535BO x5535BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5535BO.class, Feature.DisableCircularReferenceDetect);
        if("1".equals(x5535BO.getFlag())){
            return x5535Bus.executeForInterest(x5535BO);
        }else{
            return x5535Bus.busExecute(x5535BO);
        }
        
    }

}
