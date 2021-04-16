package com.tansun.ider.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tansun.ider.dao.issue.CoreNmnyHistDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreNmnyLog;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyLogSqlBuilder;

public class NonFinTransHisSyncThreadA implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(NonFinTransHisSyncThreadA.class);
	private SyncUtilNonFinTransHis syncUtil;
	private CoreNmnyLogDao coreNmnyLogDao;
	private CoreNmnyHistDao coreNmnyHistDao;

	public NonFinTransHisSyncThreadA(SyncUtilNonFinTransHis syncUtil, CoreNmnyLogDao coreNmnyLogDao,
			CoreNmnyHistDao coreNmnyHistDao) {
		super();
		this.syncUtil = syncUtil;
		this.coreNmnyLogDao = coreNmnyLogDao;
		this.coreNmnyHistDao = coreNmnyHistDao;
	}

	@Override
	public void run() {
		try {
			// 筛选非金融类日志
			CoreNmnyLogSqlBuilder coreNmnyLogSqlBuilder = new CoreNmnyLogSqlBuilder();
			coreNmnyLogSqlBuilder.andTransHisFlagEqualTo("N");
			List<CoreNmnyLog> list = coreNmnyLogDao.selectListBySqlBuilder(coreNmnyLogSqlBuilder);
			List<CoreNmnyLog> returnListNA = new ArrayList<>();
			if (null != list && !list.isEmpty()) {
				for (CoreNmnyLog coreNmnyLog : list) {
					if (logger.isDebugEnabled()) {
						logger.debug("处理A表日志id >> " + coreNmnyLog.getId() + " >> start");
					}
					returnListNA.add(coreNmnyLog);
				}
			}
			if (returnListNA.size() > 0) {
				syncUtil.putOne("LogNA", returnListNA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
