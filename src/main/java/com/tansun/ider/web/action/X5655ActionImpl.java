package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5655Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5655BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 生成制卡记录
 * @author qianyp
 *
 */
@Service("X5655")
public class X5655ActionImpl implements ActionService {

    @Autowired
    private X5655Bus x5655Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        X5655BO x5655bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5655BO.class, Feature.DisableCircularReferenceDetect);
        return x5655Bus.busExecute(x5655bo);
    }

}
