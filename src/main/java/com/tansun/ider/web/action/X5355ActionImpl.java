package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5355Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5355BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 修改账单日
 * @ClassName X5355ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2018年12月10日 上午11:21:40
 * @version 1.0.0
 */
@Service("X5355")
public class X5355ActionImpl implements ActionService {

    @Autowired
    private X5355Bus x5355Bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5355BO x5355BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5355BO.class, Feature.DisableCircularReferenceDetect);
        @SuppressWarnings("unchecked")
		List<CoreActivityArtifactRel> artifactList = (List<CoreActivityArtifactRel>) map.get(WSC.ACTION_ARTIFACT_KEY);
        x5355BO.setArtifactList(artifactList);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        x5355BO.setCoreEventActivityRel(coreEventActivityRel);
        return x5355Bus.busExecute(x5355BO);
	}
	
}
