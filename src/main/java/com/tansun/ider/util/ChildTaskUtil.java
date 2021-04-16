package com.tansun.ider.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;

/**
 * Description: 非金融工程异步工程
 * 
 * @author wt
 * @date 2018年8月10日
 */

public class ChildTaskUtil {

	private static Logger logger = LoggerFactory.getLogger(ChildTaskUtil.class);
	private static ChildTaskUtil childTaskUtil;
	private static Object synclock = new Object();

	private ChildTaskUtil() {
	};

	/**
	 * 
	 * @Description: 保证全局唯一实例变量
	 * @return
	 */
	@SuppressWarnings("unused")
	private static ChildTaskUtil getInstance() {
		if (childTaskUtil == null) {
			synchronized (synclock) {
				if (childTaskUtil == null) {
					childTaskUtil = new ChildTaskUtil();
				}
			}
		}
		return childTaskUtil;
	}

	/**
	 * 异步调起事件
	 * 
	 * @Description:
	 * @param eventId
	 * @param dto
	 * @param result
	 */
	public void startChildTask(String eventId, CoreEventActivityRel dto, Object result) {
		if (logger.isDebugEnabled()) {
			logger.debug("-----------------启动子任务，当前事件===== > [{}]下活动 [{}]异步调起 [{}]", eventId, dto.getActivityNo(),
					dto.getTriggerNo());
		}
		// 声明：兼容该值为空的结果，该结果必须是JSON，在事件公共区会通过反射赋值
		String resultMsg = (null == result) ? "" : result.toString();
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("task-pool-%d").build();
		ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		singleThreadPool.execute(new JobRunnable(dto.getTriggerNo(), resultMsg, dto.getTriggerTyp()));
		singleThreadPool.shutdown();
		if (logger.isDebugEnabled()) {
			logger.info("-------------启动子任务---------------结束");
		}
	}

}
