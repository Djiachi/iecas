package com.tansun.ider.web.action;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5970Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5970BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service("X5970")
public class X5970ActionImpl implements ActionService{
	@Autowired
	private X5970Bus x5970bus;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
	    X5970BO x5970bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5970BO.class, Feature.DisableCircularReferenceDetect);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        x5970bo.setCoreEventActivityRel(coreEventActivityRel);
        return x5970bus.busExecute(x5970bo);
	}
}
