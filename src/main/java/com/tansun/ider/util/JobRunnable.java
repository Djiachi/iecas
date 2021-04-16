package com.tansun.ider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.enums.TriggerNoType;

public class JobRunnable implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(JobRunnable.class);
	private String jobId;
	private String eventJson;
	private String triggerTyp;

	public JobRunnable(String jobId, String eventJson, String triggerTyp) {
		super();
		this.jobId = jobId;
		this.eventJson = eventJson;
		this.triggerTyp = triggerTyp;
	}

	@Override
	public void run() {
		try {
			RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(type);
			HttpEntity<String> entity = new HttpEntity<String>(eventJson, headers);
			if (TriggerNoType.TRIGGER_TYP_POS.getValue().equals(triggerTyp)) {
				restTemplate.postForObject("http://127.0.0.1:8081/authService/" + jobId, entity, String.class);
			} else if (TriggerNoType.FINANCE_CARD.getValue().equals(triggerTyp)) {
				restTemplate.postForObject("http://127.0.0.1:8082/cardService/" + jobId, entity, String.class);
			} else if (TriggerNoType.QUICK_LOAN.getValue().equals(triggerTyp)) {
				restTemplate.postForObject("http://127.0.0.1:8020/nonfinanceService/" + jobId, entity, String.class);
			}

		} catch (Exception e) {
			logger.info("-------------启动子任务{}失败，异常信息为{}", jobId, e.getMessage());
			e.printStackTrace();
		}

	}

}
