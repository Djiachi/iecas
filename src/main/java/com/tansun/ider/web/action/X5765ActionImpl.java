package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5765Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5760BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * VISA拒付查询
 * 
 * @ClassName X5765ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月15日 下午5:09:37
 * @version 1.0.0
 */
@Service("X5765")
public class X5765ActionImpl implements ActionService {
	
	@Autowired
	private X5765Bus x5765BusImpl;
	
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X5760BO x5760bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5760BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5760bo.setGlobalEventNo(globalEventNo);
		x5760bo.setCoreEventActivityRel(coreEventActivityRel);
		
		return x5765BusImpl.busExecute(x5760bo);
	}

}