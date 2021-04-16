package com.tansun.ider.bus.impl;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5715Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5720BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.ClassUtil;
import com.tansun.ider.util.LoggerUtil;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * MC调单申请维护
 * 
 * @ClassName X5715BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年1月25日 下午2:19:13
 * @version 1.0.0
 */
@Service
public class X5715BusImpl implements X5715Bus {
	@Resource
	private ClrProcessingMcDao clrProcessingMcDao;
	@Resource
	private LoggerUtil loggerUtil;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5720BO x5720bo) throws Exception {
		// 更新事件
		ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
		clrProcessingMcSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5720bo.getOldGlobalSerialNumbr());
		ClrProcessingMc clrProcessingMcNew = clrProcessingMcDao.selectBySqlBuilder(clrProcessingMcSqlBuilder);
		ClrProcessingMc clrProcessingMcOld = new ClrProcessingMc();
		CachedBeanCopy.copyProperties(clrProcessingMcNew, clrProcessingMcOld);
		CachedBeanCopy.copyProperties(clrProcessingMcNew, x5720bo);
		clrProcessingMcSqlBuilder.andVersionEqualTo(clrProcessingMcNew.getVersion());
		clrProcessingMcNew.setVersion(clrProcessingMcNew.getVersion() + 1);
		clrProcessingMcNew = (ClrProcessingMc) ClassUtil.getReflectObjectTransString(clrProcessingMcNew);
		clrProcessingMcDao.updateBySqlBuilderSelective(clrProcessingMcNew, clrProcessingMcSqlBuilder);

		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingMcNew.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);

		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());

		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingMcNew, clrProcessingMcNew.getCustomerNo(), CoreEvent.class);

		String operatorId = "";
		if (StringUtil.isNotBlank(x5720bo.getOperatorId())) {
			operatorId = "system";
		}
		
		nonFinancialLogUtil.createNonFinancialActivityLog(x5720bo.getEventNo(), x5720bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, clrProcessingMcOld, clrProcessingMcNew, clrProcessingMcNew.getId(),
				coreSystemUnit.getCurrLogFlag(), operatorId, coreCustomer.getCustomerNo(),
				clrProcessingMcNew.getExternalIdentificationNo(), null, null);

		return "OK";
	}

}
