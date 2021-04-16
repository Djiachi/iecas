package com.tansun.ider.web.controller;

import java.util.Map;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.framwork.exception.ExternalSystemException;
import com.tansun.ider.service.EventActivityService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.web.WSC;

/**
 * 
 * ClassName: EntranceController <br/>
 * Function: 卡服务接入入口. <br/>
 * Reason: ADD REASON(可选). <br/>
 * date: 2018年3月13日 下午5:30:12 <br/>
 * 
 * @author dengjunwen
 * @since JDK 1.8
 */

@Controller
@ResponseBody
public class EntranceController {
	private static final ResponseVO OK = null;
	private static Logger logger = LoggerFactory.getLogger(EntranceController.class);
	public static boolean liVaild = false;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private EventActivityService eventActivityService;
	
	/**
	 * 
	 * Description: <br/>
	 * 
	 * @param transCode
	 *            交易码 用于链路跟踪
	 * @param header
	 *            request header域信息
	 * @param body
	 *            以map对象方式进行传递
	 * @return
	 * @author dengjunwen
	 * @since JDK 1.8
	 */
	/* @SuppressWarnings("unused") */
	@RequestMapping(value = "/nonfinanceService/{eventId:.+}", method = RequestMethod.POST, produces = "application/json;charset=utf-8;")
	public ResponseVO betaService(@PathVariable("eventId") String eventId, @RequestHeader Map<String, Object> headerMap,
			@RequestBody(required = true) Map<String, Object> bodyMap) {
		if (logger.isDebugEnabled()) {
			logger.debug("Request [{}] Message =>{}", eventId, bodyMap);
		}
		long start = System.currentTimeMillis();
		if (eventId == null) {
			return null;
		}
		ResponseVO responseVO = new ResponseVO();
		responseVO.setTransCode(eventId);
		try {
			// 用户登录验证。clientToken不传则跳过验证
            /*if(null!=bodyMap && bodyMap.containsKey(Constant.CLIENT_TOKEN)){
                LoginFilter loginFilter =  SpringUtil.getBean(LoginFilter.class);
                loginFilter.masterLoginAuth((String) bodyMap.get(Constant.CLIENT_TOKEN));
            }*/
            CoreEvent coreEvent = httpQueryService.queryEvent(eventId);
            if (coreEvent == null) {
                throw new BusinessException("CUS-00102",eventId);
            }
            String temp = eventId.substring(4, 6);
            if (("BN".equals(temp) && StringUtil.isNotBlank(coreEvent.getCycleType())) && (!coreEvent.getEventNo().equals("BSS.BN.00.0000")) && (!coreEvent.getCycleType().equals("O"))) {
                // 如果是批量事件，并且循环类型不为空，才进行客户管控，否则正常执行事件
            	responseVO = eventActivityService.customerCtrlHandle(coreEvent, bodyMap);
            }else if((!"BN".equals(temp) && StringUtil.isNotBlank(coreEvent.getCycleType())) && (coreEvent.getCycleType().equals("O"))){
            	//非批量事件且循环类型不为空且等于O
            	responseVO = eventActivityService.nonBatchCtrlHandle(eventId,coreEvent,bodyMap);
            }else{
            	responseVO =  eventActivityService.webService(eventId, bodyMap,true);
            }
        } catch (Exception e) {
            if (e instanceof ExternalSystemException) {
                responseVO.setReturnCode(((ExternalSystemException) e).getErrCode());
                responseVO.setReturnMsg(((ExternalSystemException) e).toString());
            } else if (e instanceof BusinessException) {
                responseVO.setReturnCode(((BusinessException) e).getErrCode());
                responseVO.setReturnMsg(((BusinessException) e).toString());
            } else if (e instanceof TooManyResultsException) {
                responseVO.setReturnCode(WSC.TOOMANY_EXCEPTION_CODE);
                responseVO.setReturnMsg(WSC.TOOMANY_EXCEPTION_MSG);
            } else {
                e.printStackTrace();
                logger.error(WSC.DEFAULT_ERROR_RETURN_CODE + "==>" + e.getMessage(), e);
                responseVO.setReturnCode(WSC.DEFAULT_ERROR_RETURN_CODE);
                responseVO.setReturnMsg(e.getMessage());
            }
        } finally {
            long end = System.currentTimeMillis();
            responseVO.setExpendMillisecond(String.valueOf(end - start));
            if (logger.isDebugEnabled()) {
                logger.debug("Response [{}] Message =>{}", eventId, responseVO);
            }
        }
		return responseVO;
	}
	
}
