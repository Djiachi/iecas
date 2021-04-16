package com.tansun.ider.util;

import java.math.BigDecimal;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.service.business.common.Constant;

/**
 * 
 * ClassName:CustomerUtil desc:客户创建公共方法
 * 
 * @author wt
 * @date 2018年4月10日 下午2:48:28
 */
public class CustomerUtil {

	private static CustomerUtil customerUtil;

	private static Object lock = new Object();

	private CustomerUtil() {
	}

	/**
	 * 生成全局唯一编号
	 * 
	 * @return
	 */
	public static CustomerUtil getInstance() {
		if (customerUtil == null) {
			synchronized (lock) {
				if (customerUtil == null) {
					customerUtil = new CustomerUtil();
				}
			}
		}
		return customerUtil;
	}

	/**
	 * 获取全局流水号
	 * 
	 * @return
	 */
	public String getCustomerNo() {
		StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);		
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_GLOBAL_INCREMENT_KEY+"POC").increment(1);  
		String strId = "";
		BigDecimal nowFlowNumber = new BigDecimal(id);
		Long temp = 99999999L;
        if (nowFlowNumber.compareTo(new BigDecimal(temp)) >0){
			strId = (id % 10000) + "";
		} else {
			strId = id.toString();
		}
		strId = "444"+String.format("%09d", Long.parseLong(strId));
		return strId;
	}
	
}
