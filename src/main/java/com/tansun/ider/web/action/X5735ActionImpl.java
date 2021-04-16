package com.tansun.ider.web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5735Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5735BO;
import com.tansun.ider.web.WSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
/**
 * 	附加表信息查询	
 * 
 * @author yanyingzhao 2019年1月24日
 */
@Service("X5735")
public class X5735ActionImpl implements ActionService {
	
	   @Autowired
	    private  X5735Bus x5735Bus;
	   
	    @Override
	    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	    public Object execute(Map<String, Object> map) throws Exception {
	    	X5735BO x5735BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5735BO.class, Feature.DisableCircularReferenceDetect);
	        return x5735Bus.busExecute(x5735BO);
	    }

}
