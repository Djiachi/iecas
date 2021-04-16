package com.tansun.ider.bus.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5963Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.DataBackUp;

@Service
public class X5963BusImpl implements X5963Bus {
    
	private static Logger logger = LoggerFactory.getLogger(X5963BusImpl.class);
	
	@Autowired
    private HttpQueryService httpQueryService;
	
    
    @Value("${back.up.data.name.str.a}")
    private String dataNameStrA;
    
    @Value("${back.up.data.name.str.b}")
    private String dataNameStrB;
    
    @Value("${root.url}")
    private String rootUrl;
    
   
    @Override
    public Object busExecute() throws Exception {
    	DataBackUp classData = new DataBackUp();
        CoreSystemUnit coreSystemUnitList = httpQueryService.querySystemUnit("100");
        String nextLogFlag = coreSystemUnitList.getNextLogFlag();
        if ("A".equals(nextLogFlag)) {
        	//备份A表到历史表
        	classData.dataBackUp(rootUrl,dataNameStrA);
        	logger.debug("备份表A");
        } else if ("B".equals(nextLogFlag)) {
        	//备份B表到历史表
        	classData.dataBackUp(rootUrl,dataNameStrB);
        	logger.debug("备份表B");
        }
        return "ok";
    }
    
}
