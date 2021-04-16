package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5390Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5390BO;
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
@Service("X5390")
public class X5390ActionImpl implements ActionService {
    @Resource
    private X5390Bus x5390BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	X5390BO x5390BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5390BO.class, Feature.DisableCircularReferenceDetect);
        return x5390BusImpl.busExecute(x5390BO);
    }

}
