package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5780Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5775BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * MC拒付查询
 * 
 * @ClassName X5780ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月20日 下午5:09:37
 * @version 1.0.0
 */
@Service("X5780")
public class X5780ActionImpl implements ActionService {
	
	@Autowired
	private X5780Bus x5780BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5775BO x5775bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5775BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5775bo.setGlobalEventNo(globalEventNo);
		x5775bo.setCoreEventActivityRel(coreEventActivityRel);
		
		return x5780BusImpl.busExecute(x5775bo);
	}

}