package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5901Bus;
import com.tansun.ider.dao.issue.CoreNmnyHistDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreNmnyHist;
import com.tansun.ider.dao.issue.entity.CoreNmnyLog;
import com.tansun.ider.dao.issue.entity.CoreNmnyLogB;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyHistSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyLogBSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyLogSqlBuilder;
import com.tansun.ider.model.bo.X5900BO;
import com.tansun.ider.util.SyncUtilNonFinTransHis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

/**
 * 非金融历史生成
 * 
 * @author admin
 *
 */
@Service
public class X5901BusImpl implements X5901Bus {

	private static Logger logger = LoggerFactory.getLogger(X5901BusImpl.class);

	@Autowired
	private SyncUtilNonFinTransHis syncUtilNonFinTransHis;
	@Autowired
	private CoreNmnyLogDao coreNmnyLogDao;
	@Autowired
	private CoreNmnyLogBDao coreNmnyLogBDao;
	@Autowired
	private CoreNmnyHistDao coreNmnyHistDao;

	@Override
	public Object busExecute(X5900BO x5900bo) throws Exception {
		Map<String, Object> mapResults = syncUtilNonFinTransHis.syncHisData(coreNmnyLogDao, coreNmnyLogBDao,
				coreNmnyHistDao);
		Set<Entry<String, Object>> setEntries = mapResults.entrySet();
		for (Entry<String, Object> entry : setEntries) {
			Object obj = entry.getValue();
			ArrayList<Object> list = (ArrayList<Object>) obj;
			if (entry.getKey().equals("LogNA")) {
				insertNALog(list);
			} else if (entry.getKey().equals("LogNB")) {
				insertNBLog(list);
			}
		}
		return "ok";
	}

	@SuppressWarnings({ "unchecked", "null", "unused","unused" })
	private void insertNBLog(ArrayList<Object> list) throws Exception {
		if (null != list && !list.isEmpty()) {
			for (Object obj : list) {
				List<CoreNmnyHist> listCoreNmnyHists = new ArrayList<CoreNmnyHist>();
				CoreNmnyLogB coreNmnyLogB = new CoreNmnyLogB();
				CachedBeanCopy.copyProperties(obj, coreNmnyLogB);
				String activityNo = coreNmnyLogB.getActivityNo();
				String logLeve = coreNmnyLogB.getLogLevel();
				String relativeTableName = coreNmnyLogB.getRelativeTableName();
				String modifyType = coreNmnyLogB.getModifyType();
				// 比较日志中的内容更新
				String modifyAfter = coreNmnyLogB.getModifyAfter();
				String modifyBefore = coreNmnyLogB.getModifyBefore();
				Map<String, Object> mapAfter = JSON.parseObject(modifyAfter, Map.class, Feature.DisableCircularReferenceDetect);
				Map<String, Object> mapBefore = JSON.parseObject(modifyBefore, Map.class,Feature.DisableCircularReferenceDetect);
				if ((mapAfter == null || mapAfter.isEmpty()) && (mapBefore != null || !mapBefore.isEmpty())) {
					Iterator<Map.Entry<String, Object>> entries = mapBefore.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLogB.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLogB.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLogB.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLogB.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLogB.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLogB.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLogB.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLogB.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLogB.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLogB.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLogB.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLogB.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLogB.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLogB.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLogB.getCurrencyCode());
							coreNmnyHistAdd.setModifyAfter(null);
							if (StringUtil.isNotBlank(entry.getValue() + "") && !"null".equals(entry.getValue() + "")) {
								coreNmnyHistAdd.setModifyBefore(entry.getValue() + "");
							}else {
								coreNmnyHistAdd.setModifyBefore("");
							}
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				} else if ((mapAfter != null || !mapAfter.isEmpty()) && (mapBefore == null || mapBefore.isEmpty())) {
					Iterator<Map.Entry<String, Object>> entries = mapAfter.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLogB.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLogB.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLogB.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLogB.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLogB.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLogB.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLogB.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLogB.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLogB.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLogB.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLogB.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLogB.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLogB.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLogB.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLogB.getCurrencyCode());
							if (StringUtil.isNotBlank(entry.getValue() + "") && !"null".equals(entry.getValue() + "")) {
								coreNmnyHistAdd.setModifyAfter(entry.getValue() + "");
							}else {
								coreNmnyHistAdd.setModifyAfter("");
							}
							coreNmnyHistAdd.setModifyBefore(null);
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				} else if ((mapAfter != null || !mapAfter.isEmpty()) && (mapBefore != null || !mapBefore.isEmpty())) {
					Map<String, Object> mapNew = new HashMap<String, Object>();
					mapNew.putAll(mapAfter);
					mapNew.putAll(mapBefore);
					Iterator<Map.Entry<String, Object>> entries = mapNew.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLogB.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLogB.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLogB.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLogB.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLogB.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLogB.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLogB.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLogB.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLogB.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLogB.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLogB.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLogB.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLogB.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLogB.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLogB.getCurrencyCode());
							if (StringUtil.isNotBlank(mapAfter.get(entry.getKey()) + "") && !"null".equals(mapAfter.get(entry.getKey()) + "")) {
								coreNmnyHistAdd.setModifyAfter(mapAfter.get(entry.getKey()) + "");
							}else {
								coreNmnyHistAdd.setModifyAfter("");
							}
							if (StringUtil.isNotBlank(mapBefore.get(entry.getKey()) + "") && !"null".equals(mapBefore.get(entry.getKey()) + "")) {
								coreNmnyHistAdd.setModifyBefore(mapBefore.get(entry.getKey()) + "");
							}else {
								coreNmnyHistAdd.setModifyBefore("");
							}
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				}
				CoreNmnyLogBSqlBuilder coreNmnyLogBSqlBuilder = new CoreNmnyLogBSqlBuilder();
				coreNmnyLogBSqlBuilder.andIdEqualTo(coreNmnyLogB.getId());
				coreNmnyLogBSqlBuilder.andVersionEqualTo(coreNmnyLogB.getVersion());
				CoreNmnyLogB coreNmnyLogB1 = coreNmnyLogBDao.selectBySqlBuilder(coreNmnyLogBSqlBuilder);
				coreNmnyLogB1.setTransHisFlag("Y");
				coreNmnyLogB1.setVersion(coreNmnyLogB.getVersion()+1);
				int result = coreNmnyLogBDao.updateBySqlBuilderSelective(coreNmnyLogB1, coreNmnyLogBSqlBuilder);
				if (logger.isDebugEnabled()) {
					logger.debug("处理B表日志id >> " + coreNmnyLogB.getId() + " >> end");
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "null", "unused","unused" })
	private void insertNALog(ArrayList<Object> list) throws Exception {
		if (null != list && !list.isEmpty()) {
			for (Object obj : list) {
				List<CoreNmnyHist> coreNmnyHistList = new ArrayList<CoreNmnyHist>();
				CoreNmnyLog coreNmnyLog = new CoreNmnyLog();
				CachedBeanCopy.copyProperties(obj, coreNmnyLog);
				String relativeTableName = coreNmnyLog.getRelativeTableName();
				String modifyType = coreNmnyLog.getModifyType();
				// 比较日志中的内容更新
				String modifyAfter = coreNmnyLog.getModifyAfter();
				String modifyBefore = coreNmnyLog.getModifyBefore();
				Map<String, Object> mapAfter = JSON.parseObject(modifyAfter, Map.class,Feature.DisableCircularReferenceDetect);
				Map<String, Object> mapBefore = JSON.parseObject(modifyBefore, Map.class,Feature.DisableCircularReferenceDetect);
				if ((mapAfter == null || mapAfter.isEmpty()) && (mapBefore != null || !mapBefore.isEmpty())) {
					Iterator<Map.Entry<String, Object>> entries = mapBefore.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLog.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLog.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLog.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLog.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLog.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLog.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLog.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLog.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLog.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLog.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLog.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLog.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLog.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLog.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLog.getCurrencyCode());
							coreNmnyHistAdd.setModifyAfter(null);
							if (StringUtil.isNotBlank("" + entry.getValue()) && !"null".equals("" + entry.getValue())) {
								coreNmnyHistAdd.setModifyBefore("" + entry.getValue());
							}else {
								coreNmnyHistAdd.setModifyBefore("");
							}
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistAdd.setGmtCreate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				} else if ((mapAfter != null || !mapAfter.isEmpty()) && (mapBefore == null || mapBefore.isEmpty())) {
					Iterator<Map.Entry<String, Object>> entries = mapAfter.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLog.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLog.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLog.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLog.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLog.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLog.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLog.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLog.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLog.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLog.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLog.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLog.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLog.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLog.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLog.getCurrencyCode());
							if (StringUtil.isNotBlank("" + entry.getValue()) && !"null".equals("" + entry.getValue())) {
								coreNmnyHistAdd.setModifyAfter("" + entry.getValue());
							}else {
								coreNmnyHistAdd.setModifyAfter("");
							}
							coreNmnyHistAdd.setModifyBefore(null);
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistAdd.setGmtCreate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				} else if ((mapAfter != null || !mapAfter.isEmpty()) && (mapBefore != null || !mapBefore.isEmpty())) {
					Map<String, Object> mapNew = new HashMap<String, Object>();
					mapNew.putAll(mapAfter);
					mapNew.putAll(mapBefore);
					Iterator<Map.Entry<String, Object>> entries = mapNew.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, Object> entry = entries.next();
						if (logger.isDebugEnabled()) {
							logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
						}
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.andEventNoEqualTo(coreNmnyLog.getEventNo());
						coreNmnyHistSqlBuilder.andActivityNoEqualTo(coreNmnyLog.getActivityNo());
						coreNmnyHistSqlBuilder.andOccurrDateEqualTo(coreNmnyLog.getOccurrDate());
						coreNmnyHistSqlBuilder.andOccurrTimeEqualTo(coreNmnyLog.getOccurrTime());
						coreNmnyHistSqlBuilder.andRelativeTableIdEqualTo(coreNmnyLog.getRelativeTableId());
						coreNmnyHistSqlBuilder.andModifyFieldNameEqualTo(entry.getKey());
						CoreNmnyHist coreNmnyHist = coreNmnyHistDao.selectBySqlBuilder(coreNmnyHistSqlBuilder);
						if (coreNmnyHist == null) {
							CoreNmnyHist coreNmnyHistAdd = new CoreNmnyHist();
							coreNmnyHistAdd.setActivityNo(coreNmnyLog.getActivityNo());
							coreNmnyHistAdd.setEventNo(coreNmnyLog.getEventNo());
							coreNmnyHistAdd.setId(RandomUtil.getUUID());
							coreNmnyHistAdd.setLogLevel(coreNmnyLog.getLogLevel());
							coreNmnyHistAdd.setModifyType(modifyType);
							coreNmnyHistAdd.setOccurrDate(coreNmnyLog.getOccurrDate());
							coreNmnyHistAdd.setOccurrTime(coreNmnyLog.getOccurrTime());
							coreNmnyHistAdd.setRelativeTableName(relativeTableName);
							coreNmnyHistAdd.setOperatorId(coreNmnyLog.getOperatorId());
							coreNmnyHistAdd.setRelativeTableId(coreNmnyLog.getRelativeTableId());
							coreNmnyHistAdd.setCustomerNo(coreNmnyLog.getCustomerNo());
							coreNmnyHistAdd.setEntityKey(coreNmnyLog.getEntityKey());
							coreNmnyHistAdd.setCurrencyCode(coreNmnyLog.getCurrencyCode());
							if (StringUtil.isNotBlank("" + mapAfter.get(entry.getKey())) && !"null".equals("" + mapAfter.get(entry.getKey()))) {
								coreNmnyHistAdd.setModifyAfter("" + mapAfter.get(entry.getKey()));
							}else {
								coreNmnyHistAdd.setModifyAfter("");
							}
							if (StringUtil.isNotBlank("" + mapBefore.get(entry.getKey())) && !"null".equals("" + mapBefore.get(entry.getKey()))) {
								coreNmnyHistAdd.setModifyBefore("" + mapBefore.get(entry.getKey()));
							}else {
								coreNmnyHistAdd.setModifyBefore("");
							}
							coreNmnyHistAdd.setModifyFieldName(entry.getKey());
							coreNmnyHistAdd.setVersion(1);
							coreNmnyHistAdd.setGmtCreate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
							coreNmnyHistDao.insert(coreNmnyHistAdd);
						}
					}
				}
				CoreNmnyLogSqlBuilder coreNmnyLogSqlBuilder = new CoreNmnyLogSqlBuilder();
				coreNmnyLogSqlBuilder.andIdEqualTo(coreNmnyLog.getId());
				coreNmnyLogSqlBuilder.andVersionEqualTo(coreNmnyLog.getVersion());
				CoreNmnyLog coreNmnyLog1 = coreNmnyLogDao.selectBySqlBuilder(coreNmnyLogSqlBuilder);
				coreNmnyLog1.setTransHisFlag("Y");
				coreNmnyLog1.setVersion(coreNmnyLog1.getVersion()+1);
				int result = coreNmnyLogDao.updateBySqlBuilderSelective(coreNmnyLog1, coreNmnyLogSqlBuilder);
				if (logger.isDebugEnabled()) {
					logger.debug("处理A表日志id >> " + coreNmnyLog.getId() + " >> end");
				}
			}
		}
	}

}
