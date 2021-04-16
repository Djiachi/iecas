package com.tansun.ider.bus.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5655Bus;
import com.tansun.ider.dao.issue.CoreCardMakeRecordDao;
import com.tansun.ider.dao.issue.entity.CoreCardMakeRecord;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCardMakeRecordSqlBuilder;
import com.tansun.ider.model.bo.X5655BO;

/**
 * 生成制卡记录
 * @author qianyp
 */
@Service
public class X5655BusImpl implements X5655Bus {

	@Autowired
	private CoreCardMakeRecordDao coreCardMakeRecordDao;

	@Override
	public Object busExecute(X5655BO x5655BO) throws Exception {
		CoreCardMakeRecord coreCardMakeRecord = new CoreCardMakeRecord();
		coreCardMakeRecord.setId(RandomUtil.getUUID());
		coreCardMakeRecord.setExternalIdentificationNo(x5655BO.getExternalIdentificationNo());
		coreCardMakeRecord.setExpirationDate(x5655BO.getExpirationDate());
		coreCardMakeRecord.setCardAssociations("V");
		coreCardMakeRecord.setFormatCode(x5655BO.getFormatCode());
		coreCardMakeRecord.setMainSupplyIndicator("1");
		coreCardMakeRecord.setTimeStamp(new Date());
		coreCardMakeRecord.setVersion(1);
		CoreCardMakeRecordSqlBuilder coreCardMakeRecordSqlBuilder = new CoreCardMakeRecordSqlBuilder();
		coreCardMakeRecordSqlBuilder.orderByRecordNumber(true);
		List<CoreCardMakeRecord> cardMakeRecords = coreCardMakeRecordDao.selectListBySqlBuilder(coreCardMakeRecordSqlBuilder);
		if(cardMakeRecords!=null && cardMakeRecords.size()>0){
			coreCardMakeRecord.setRecordNumber(cardMakeRecords.get(0).getRecordNumber()+1);
		}else{
			coreCardMakeRecord.setRecordNumber(1);
		}
		int result =  coreCardMakeRecordDao.insert(coreCardMakeRecord);
		return result;
	}	
}
