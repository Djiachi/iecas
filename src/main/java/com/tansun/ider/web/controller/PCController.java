package com.tansun.ider.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 代理服务接入入口
 * 
 * @author pg
 * @since JDK 1.8
 */
@Component
public class PCController {
	private static Logger logger = LoggerFactory.getLogger(PCController.class);

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 发送请求,调用restful请求
	 * 
	 * @param eventId
	 * @param headerMap
	 * @param bodyMap
	 * @return
	 */
	public Object authService(String eventId, String serviceAddr, Map<String, Object> headerMap,
			Map<String, Object> bodyMap) {
		String requestJson = JSON.toJSONString(bodyMap, SerializerFeature.DisableCircularReferenceDetect);
		logger.debug("TCP->HTTP " + "[requestJson>" + requestJson);
		HttpHeaders headers = new HttpHeaders();
		MediaType httpType = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(httpType);
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		String responsJson = restTemplate.postForObject(serviceAddr, entity, String.class);
		@SuppressWarnings("unused")
		Map<String, Object> responseData = new HashMap<>();
		responseData = JSON.parseObject(responsJson, Feature.DisableCircularReferenceDetect);
		logger.debug("TCP->HTTP [responsJson>" + responsJson);
		return responsJson;
	}

}
