package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X4002Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X4002BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * 
 * @Description:TODO(报销标识)   
 * @author: sunyaoyao
 * @date:   2019年5月9日 上午9:46:38   
 *
 */
@Service("X4002")
public class X4002ActionImpl implements ActionService{
	@Autowired
	private X4002Bus x4002bus;
	@Override
	public Object execute(Map<String, Object> map) throws Exception {
		X4002BO x4002bo = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X4002BO.class, Feature.DisableCircularReferenceDetect);
		return x4002bus.busExecute(x4002bo);
	}

}
