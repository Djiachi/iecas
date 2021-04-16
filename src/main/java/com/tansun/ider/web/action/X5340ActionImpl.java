package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5340Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5340BO;
import com.tansun.ider.web.WSC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 争议账户信息查询
 * @author huangyayun
 *
 * @date 2018年8月13日
 */
@Service("X5340")
public class X5340ActionImpl implements ActionService {
    @Resource
    private X5340Bus x5340BusImpl;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
    	X5340BO x5340BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5340BO.class, Feature.DisableCircularReferenceDetect);
        return x5340BusImpl.busExecute(x5340BO);
    }

}
