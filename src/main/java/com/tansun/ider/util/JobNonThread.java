package com.tansun.ider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.tansun.framework.util.SpringUtil;

/**
 * trigger异步事件
 * 
 * @author huangyayun
 * @date 2018年4月24日 下午3:21:03
 */
public class JobNonThread implements Runnable {
    
	private static Logger logger = LoggerFactory.getLogger(JobNonThread.class);
	private String jobId;
	private String eventJson;
	private String addr;

	public JobNonThread(String jobId, String eventJson,String addr) {
		this.jobId = jobId;
		this.eventJson = eventJson;
		this.addr = addr;
	}

	@Override
	public void run() {
		try {
			RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(type);
			HttpEntity<String> entity = new HttpEntity<String>(eventJson, headers);
			restTemplate.postForObject(addr + jobId, entity, String.class);
		} catch (Exception e) {
			logger.info("-------------启动子任务{}失败，异常信息为{}", jobId, e.getMessage());
			e.printStackTrace();
		}
	}
}
