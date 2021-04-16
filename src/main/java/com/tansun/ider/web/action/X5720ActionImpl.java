package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5720Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5720BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * MC调单申请建立
 * @ClassName X5720ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年1月25日 上午11:07:46
 * @version 1.0.0
 */
@Service("X5720")
public class X5720ActionImpl implements ActionService {

    @Autowired
    private X5720Bus x5720Bus;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object execute(Map<String, Object> map) throws Exception {
        X5720BO x5720BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5720BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5720BO.setGlobalEventNo(globalEventNo);
		x5720BO.setCoreEventActivityRel(coreEventActivityRel);
        x5720BO.setEventNo(coreEventActivityRel.getEventNo());
        x5720BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return x5720Bus.busExecute(x5720BO);
    }

}
