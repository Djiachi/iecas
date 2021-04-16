package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4080Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4080BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * @version:1.0
 * @Description: 会计接口文件查询
 * @author: yanyingzhao
 */
@Service("X4080")
public class X4080ActionImpl implements ActionService {

    @Autowired
    private X4080Bus x4080BusImpl;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X4080BO x4080bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4080BO.class, Feature.DisableCircularReferenceDetect);
        if (!Constant.REQUEST_TYPE.equals(x4080bo.getRequestType())) {
        	return x4080BusImpl.busExecute(x4080bo);
        }else{
        	return x4080BusImpl.requestQueryList(x4080bo);
        }
    }

}
