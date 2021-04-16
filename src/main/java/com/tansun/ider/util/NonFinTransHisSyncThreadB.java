package com.tansun.ider.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tansun.ider.dao.issue.CoreNmnyHistDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.entity.CoreNmnyLogB;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyLogBSqlBuilder;

public class NonFinTransHisSyncThreadB implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(NonFinTransHisSyncThreadB.class);
	private SyncUtilNonFinTransHis syncUtil;
	private CoreNmnyLogBDao coreNmnyLogBDao;
	private CoreNmnyHistDao coreNmnyHistDao;

	public NonFinTransHisSyncThreadB(SyncUtilNonFinTransHis syncUtil, CoreNmnyLogBDao coreNmnyLogBDao,
			CoreNmnyHistDao coreNmnyHistDao) {
		super();
		this.syncUtil = syncUtil;
		this.coreNmnyLogBDao = coreNmnyLogBDao;
		this.coreNmnyHistDao = coreNmnyHistDao;
	}

	@Override
	public void run() {
		try {
			// 筛选非金融类日志
			CoreNmnyLogBSqlBuilder coreNmnyLogBSqlBuilder = new CoreNmnyLogBSqlBuilder();
			coreNmnyLogBSqlBuilder.andTransHisFlagEqualTo("N");
			List<CoreNmnyLogB> list = coreNmnyLogBDao.selectListBySqlBuilder(coreNmnyLogBSqlBuilder);
			// 处理执行
			List<CoreNmnyLogB> returnListNB = new ArrayList<>();
			if (null != list && !list.isEmpty()) {
				for (CoreNmnyLogB coreNmnyLogB : list) {
					if (logger.isDebugEnabled()) {
						logger.debug("处理B表日志id>> " + coreNmnyLogB.getId() + " >> start");
					}
					returnListNB.add(coreNmnyLogB);
				}
			}
			if (returnListNB.size() > 0) {
				syncUtil.putOne("LogNB", returnListNB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
