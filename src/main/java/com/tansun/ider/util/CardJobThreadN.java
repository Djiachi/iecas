package com.tansun.ider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.enums.TriggerNoType;

/**
 * trigger异步事件
 * 
 * @author dengjunwen
 * @date 2018年4月24日 下午3:21:03
 */
public class CardJobThreadN implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(CardJobThread.class);
	private String jobId;
	private Object eventJson;
	private String triggerTyp;

	public CardJobThreadN(String jobId, String triggerTyp, Object eventJson) {
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
			Object obj = eventJson;
			if (TriggerNoType.TRIGGER_TYP_POS.getValue().equals(triggerTyp)) {
				String result = restTemplate.postForObject("http://10.6.10.98:8081/authService/" + jobId, obj,
				String.class);
//				String result = restTemplate.postForObject("http://192.168.32.47:8081/authService/" + jobId, obj,
//						String.class);
				logger.info("授权返回结果---------result -  >  " + result );
			} else if (TriggerNoType.FINANCE_CARD.getValue().equals(triggerTyp)) {
				restTemplate.postForObject("http://10.6.80.164:8082/cardService/" + jobId, obj, String.class);
			} else if (TriggerNoType.QUICK_LOAN.getValue().equals(triggerTyp)) {
				restTemplate.postForObject("http://10.6.80.164:8020/nonfinanceService/" + jobId, obj, String.class);
			}
		} catch (Exception e) {
			logger.info("-------------启动子任务{}失败，异常信息为{}", jobId, e.getMessage());
			e.printStackTrace();
		}
	}
}
