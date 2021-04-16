package com.tansun.ider.util;

import java.util.Map;

import com.tansun.ider.web.controller.PCController;

/**
 * @Desc:非金融工程,调用授权工程
 * @Author wt
 * @Date 2018年5月24日 上午11:19:11
 */
public class NonFinanceRunnableAuth implements Runnable {

    private String eventId;
    private Map<String, Object> headerMap;
    private Map<String, Object> bodyMap;
    private PCController pCController;
    private String serviceAddr;

	public NonFinanceRunnableAuth(String eventId, Map<String, Object> headerMap, Map<String, Object> bodyMap,
			PCController pCController, String serviceAddr) {
		super();
		this.eventId = eventId;
		this.headerMap = headerMap;
		this.bodyMap = bodyMap;
		this.pCController = pCController;
		this.serviceAddr = serviceAddr;
	}

	@Override
    public void run() {
        
        pCController.authService(eventId,serviceAddr, headerMap, bodyMap);
        
    }

}
