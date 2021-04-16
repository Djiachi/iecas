package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5595Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5580BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 客户个性化元件删除
 * @author yanzhaofei 2019-01-23
 *
 */
@Service("X5595")
public class X5595ActionImpl implements ActionService {

	@Autowired
	private X5595Bus x5595Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5580BO x5580BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5580BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		// 全局事件流水号
		x5580BO.setEventNo(coreEventActivityRel.getEventNo());
		x5580BO.setActivityNo(coreEventActivityRel.getActivityNo());

		return x5595Bus.busExecute(x5580BO);
	}
}
