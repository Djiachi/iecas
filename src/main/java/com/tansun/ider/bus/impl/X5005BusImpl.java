package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5005Bus;
import com.tansun.ider.dao.issue.CoreCustomerNumberRuleDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerNumberRule;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5005BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户序号规则表活动设计
 * @author: admin
 * @date: date{time}
 */
@Service
public class X5005BusImpl implements X5005Bus {

	@Autowired
	private CoreCustomerNumberRuleDao coreCustomerNumberRuleDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;

	@Override
	public Object busExecute(X5005BO x5005BO) throws Exception {
		//判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5005BO.getIsNew())) {
			if ("1".equals(x5005BO.getIsNew())) {
				return x5005BO;
			}
		}
		
		// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5005BO, eventCommAreaNonFinance);
		String operatorId = x5005BO.getOperatorId();
		CoreCustomerNumberRule coreCustomerNumberRuleA = new CoreCustomerNumberRule();
		coreCustomerNumberRuleA.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleA.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleA.setNextSeqNo(1);
		coreCustomerNumberRuleA.setSeqType("A");
		coreCustomerNumberRuleA.setVersion(1);
		coreCustomerNumberRuleA.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleB = new CoreCustomerNumberRule();
		coreCustomerNumberRuleB.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleB.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleB.setNextSeqNo(1);
		coreCustomerNumberRuleB.setSeqType("B");
		coreCustomerNumberRuleB.setVersion(1);
		coreCustomerNumberRuleB.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleM = new CoreCustomerNumberRule();
		coreCustomerNumberRuleM.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleM.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleM.setNextSeqNo(1);
		coreCustomerNumberRuleM.setSeqType("M");
		coreCustomerNumberRuleM.setVersion(1);
		coreCustomerNumberRuleM.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleP = new CoreCustomerNumberRule();
		coreCustomerNumberRuleP.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleP.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleP.setNextSeqNo(1);
		coreCustomerNumberRuleP.setVersion(1);
		coreCustomerNumberRuleP.setSeqType("P");
		coreCustomerNumberRuleP.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleL = new CoreCustomerNumberRule();
		coreCustomerNumberRuleL.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleL.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleL.setNextSeqNo(1);
		coreCustomerNumberRuleL.setSeqType("L");
		coreCustomerNumberRuleL.setVersion(1);
		coreCustomerNumberRuleL.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleF = new CoreCustomerNumberRule();
		coreCustomerNumberRuleF.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleF.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleF.setNextSeqNo(1);
		coreCustomerNumberRuleF.setSeqType("F");
		coreCustomerNumberRuleF.setVersion(1);
		coreCustomerNumberRuleF.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		CoreCustomerNumberRule coreCustomerNumberRuleS = new CoreCustomerNumberRule();
		coreCustomerNumberRuleS.setCustomerNo(x5005BO.getCustomerNo());
		coreCustomerNumberRuleS.setId(RandomUtil.getUUID());
		coreCustomerNumberRuleS.setNextSeqNo(1);
		coreCustomerNumberRuleS.setSeqType("S");
		coreCustomerNumberRuleS.setVersion(1);
		coreCustomerNumberRuleS.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		List<CoreCustomerNumberRule> listCoreCustomerNumberRules = new ArrayList<CoreCustomerNumberRule>();
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleA);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleB);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleM);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleP);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleL);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleF);
		listCoreCustomerNumberRules.add(coreCustomerNumberRuleS);
		@SuppressWarnings("unused")
		int result = coreCustomerNumberRuleDao.insertUseBatch(listCoreCustomerNumberRules);
		if (operatorId == null) {
			operatorId = "system";
		}
		for (CoreCustomerNumberRule coreCustomerNumberRule : listCoreCustomerNumberRules) {
			nonFinancialLogUtil.createNonFinancialActivityLog(x5005BO.getEventNo(), x5005BO.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreCustomerNumberRule, coreCustomerNumberRule.getId(),
					x5005BO.getCurrLogFlag(), operatorId, coreCustomerNumberRule.getCustomerNo(),
					coreCustomerNumberRule.getCustomerNo(), null,null);
		}
		return eventCommAreaNonFinance;
	}

}
