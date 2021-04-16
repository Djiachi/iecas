/**  

* @Title: X5820ActionImpl.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/  
package com.tansun.ider.web.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5820Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5820BO;
import com.tansun.ider.web.WSC;

/**  

* @ClassName: X5820ActionImpl  

* @Function:

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/
@Service("X5820")
public class X5820ActionImpl implements ActionService {
    @Autowired
    private X5820Bus x5820Bus;
    
    /* (non-Javadoc)  
    
     * <p>Title: execute</p>  
    
     * <p>Description: </p>  
    
     * @param arg0
     * @return
     * @throws Exception  
    
     * @see com.tansun.ider.framwork.api.ActionService#execute(java.util.Map)  
    
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        // 1. 将json反序列化成实体对象，事件公共区
        X5820BO x5820BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5820BO.class, Feature.DisableCircularReferenceDetect);
        List<CoreActivityArtifactRel> artifactList = (List<CoreActivityArtifactRel>) map.get(WSC.ACTION_ARTIFACT_KEY);
        CoreEventActivityRel coreEventActivityRel = (CoreEventActivityRel) map.get(WSC.ACTIVITY_INFO);
        // 全局事件流水号
        String globalEventNo = (String) map.get(WSC.EVENT_ID);
        x5820BO.setGlobalEventNo(globalEventNo);
        x5820BO.setCoreEventActivityRel(coreEventActivityRel);
        x5820BO.setEventNo(coreEventActivityRel.getEventNo());
        x5820BO.setActivityNo(coreEventActivityRel.getActivityNo());
        return  x5820Bus.busExecute(x5820BO) ;
    }

}
