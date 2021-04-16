package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5235Bus;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.BatchDateProcessType;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.model.bo.X5235BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 旧卡媒介有效期更新,并且同步授权
 * @author admin
 *
 */
@Service
public class X5235BusImpl implements X5235Bus {

	@Autowired	
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	
	@Override
	public Object busExecute(X5235BO x5235bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5235bo, eventCommAreaNonFinance);
		
		String mediaUnitCode = x5235bo.getMediaUnitCode();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if (null != coreMediaBasicInfo) {
			String invaliddateOldcard = coreMediaBasicInfo.getInvaliddateOldcard();
			if (StringUtil.isNotBlank(invaliddateOldcard)) {
//				旧卡失效日期在本次批量处理区间内
//				FRW 上一处理日 < 旧卡失效日期 <=当前处理日期
//				BCK 当前处理日期<= 旧卡失效日期 < 下一处理日
				CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
				coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
				CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
				String systemUnitNo = coreCustomer.getSystemUnitNo();
				CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
				String operationMode = coreMediaBasicInfo.getOperationMode();
				CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
				String batchDateProcessType = coreOperationMode.getBatchDateProcessType();
				boolean flag =  false;
				if (BatchDateProcessType.BWD.getValue().equals(batchDateProcessType)) {
					double sumTotal1 = DateUtil.daysBetween(invaliddateOldcard, coreSystemUnit.getCurrProcessDate(), "yyyy-MM-dd");
					double sumTotal2 = DateUtil.daysBetween(invaliddateOldcard, coreSystemUnit.getLastProcessDate(), "yyyy-MM-dd");
					if ((sumTotal1 > 0 || sumTotal1 == 0) && (sumTotal2 < 0)) {
						flag  = true;
					}else {
						flag  = false; 
					}
				}else if (BatchDateProcessType.FWD.getValue().equals(batchDateProcessType)) {
					double sumTotal1 = DateUtil.daysBetween(invaliddateOldcard, coreSystemUnit.getCurrProcessDate(), "yyyy-MM-dd");
					double sumTotal2 = DateUtil.daysBetween(invaliddateOldcard, coreSystemUnit.getNextProcessDate(), "yyyy-MM-dd");
					if ((sumTotal1 > 0 || sumTotal1 ==0 ) && (sumTotal2 < 0 )) {
						flag  = true;
					}else {
						flag  = false; 
					}
				}
				List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
				// 判断是否需要处理
				if (flag) {
					// 同步授权
					coreMediaBasicInfo.setInvalidFlag("N");
					coreMediaBasicInfo.setInvalidReason(InvalidReasonStatus.CHP.getValue());
					coreMediaBasicInfoDao.updateBySqlBuilderSelective(coreMediaBasicInfo, coreMediaBasicInfoSqlBuilder);
					Map<String, Object> triggerEventParams = new HashMap<String, Object>();
					EventCommAreaNonFinance eventCommAreaNonFinance1= new EventCommAreaNonFinance();
					eventCommAreaNonFinance1.setAuthDataSynFlag("1");
					CachedBeanCopy.copyProperties(coreMediaBasicInfo, eventCommAreaNonFinance1);
					eventCommAreaNonFinance1.setInvalidReasonOld(coreMediaBasicInfo.getInvalidReason());
					triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, eventCommAreaNonFinance1);
					eventCommAreaTriggerEventList.add(triggerEventParams);
				}
				if (flag) {
					eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
				} else {
					eventCommAreaNonFinance.setWhetherProcess("1");
				}
			}
		}
		
		return eventCommAreaNonFinance;
	}

}
