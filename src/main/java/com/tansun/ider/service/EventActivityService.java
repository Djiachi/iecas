package com.tansun.ider.service;

import java.util.List;
import java.util.Map;

import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * 
 * @author admin
 *
 */
public interface EventActivityService {
	
	/**
	 * 事件调用活动公共处理方法
	 */
	public Object businessHandler(String eventId, String globalEventNo, List<CoreEventActivityRel> actList) throws Exception;
	
	/**
	 * 非金融总控批量处理
	 */
	public ResponseVO customerCtrlHandle(CoreEvent coreEvent,Map<String, Object> bodyMap) throws Exception;
	
	/**
	 * 非金融非批量处理
	 */
	public ResponseVO nonBatchCtrlHandle(String eventId,CoreEvent coreEvent,Map<String, Object> bodyMap) throws Exception;
	
	public ResponseVO webService(String eventId,Map<String, Object> bodyMap,boolean someBatchFlag) throws Exception;
	
    /**
     * 公共service-事件公共区定义，所有参数全部从缓存中取值
     * 
     * @param eventId
     * @param map
     * @return EventCommArea
     * @throws Exception
     */
    public EventCommAreaNonFinance getEventCommAreaNonFinance(String eventId, Map<String, Object> map) throws Exception;
	
}
