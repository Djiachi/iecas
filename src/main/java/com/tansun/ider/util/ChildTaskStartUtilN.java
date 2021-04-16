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
 * 任务启动工具类
 * 
 * @author dengjunwen
 * @date 2018年4月25日 上午11:02:17
 */
public class ChildTaskStartUtilN {
	private static Logger logger = LoggerFactory.getLogger(ChildTaskStartUtilN.class);
	private static ChildTaskStartUtilN childTaskStartUtil;
	private static Object synclock = new Object();

	private ChildTaskStartUtilN() {
	}

	/**
	 * 返回全局唯一实例
	 * 
	 * @return ChildTaskStartUtil
	 * @author dengjunwen
	 * @since JDK 1.8
	 */
	public static ChildTaskStartUtilN getInstance() {
		if (childTaskStartUtil == null) {
			synchronized (synclock) {
				// 考虑到高并发的情况
				if (childTaskStartUtil == null) {
					childTaskStartUtil = new ChildTaskStartUtilN();
				}
			}
		}
		return childTaskStartUtil;
	}

	/**
	 * 异步调起事件
	 * 
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
		Object resultMsg = (null == result) ? "" : result;
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("task-pool-%d").build();
		ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		singleThreadPool.execute(new CardJobThreadN(dto.getTriggerNo(), dto.getTriggerTyp(), resultMsg));
		singleThreadPool.shutdown();
		if (logger.isDebugEnabled()) {
			logger.info("-------------启动子任务---------------结束");
		}
	}
}
