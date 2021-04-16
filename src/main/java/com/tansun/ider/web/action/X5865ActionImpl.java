package com.tansun.ider.web.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5865Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5865BO;
import com.tansun.ider.web.WSC;


/**
 * 客户周期费用删除
 * @ClassName X5865ActionImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年9月19日 上午11:10:06
 * @version 5.0.0
 */
@Service("X5865")
public class X5865ActionImpl implements ActionService {

	@Autowired
	private X5865Bus x5865Bus;

	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		// 1. 将json反序列化成实体对象，事件公共区
		X5865BO x5865BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5865BO.class, Feature.DisableCircularReferenceDetect);
		CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
		List<CoreActivityArtifactRel> activityArtifactList = (List<CoreActivityArtifactRel>) map
				.get(WSC.ACTION_ARTIFACT_KEY);
		// 全局事件流水号
		String globalEventNo = (String) map.get(WSC.EVENT_ID);
		x5865BO.setGlobalEventNo(globalEventNo);
		x5865BO.setCoreEventActivityRel(coreEventActivityRel);
		x5865BO.setActivityArtifactList(activityArtifactList);
		x5865BO.setEventNo(coreEventActivityRel.getEventNo());
		x5865BO.setActivityNo(coreEventActivityRel.getActivityNo());
		return x5865Bus.busExecute(x5865BO);
	}

}
