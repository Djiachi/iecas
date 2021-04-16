package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.impl.X5395BusImpl;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5395BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 账单产品对象+业务类型维度查询
 * @author huangyayun
 *
 * @date 2018年8月13日
 */
@Service("X5395")
public class X5395ActionImpl implements ActionService {
    @Resource
    private X5395BusImpl x5395BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	X5395BO x5395BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5395BO.class, Feature.DisableCircularReferenceDetect);
        return x5395BusImpl.busExecute(x5395BO);
    }

}
