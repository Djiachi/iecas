package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5770Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingVisaDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingVisa;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingVisaSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5760BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.ClassUtil;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * Visa拒付维护
 * 
 * @ClassName X5770BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月15日 上午10:25:36
 * @version 1.0.0
 */
@Service
public class X5770BusImpl implements X5770Bus {
	
	@Autowired
	private ClrProcessingVisaDao clrProcessingVisaDao;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public String busExecute(X5760BO x5760bo) throws Exception {
		// 更新
		ClrProcessingVisaSqlBuilder clrProcessingVisaSqlBuilder = new ClrProcessingVisaSqlBuilder();
		clrProcessingVisaSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5760bo.getOldGlobalSerialNumbr());
		ClrProcessingVisa clrProcessingVisaNew = clrProcessingVisaDao.selectBySqlBuilder(clrProcessingVisaSqlBuilder);
		ClrProcessingVisa clrProcessingVisaOld = new ClrProcessingVisa();
		CachedBeanCopy.copyProperties(clrProcessingVisaNew, clrProcessingVisaOld);
		CachedBeanCopy.copyProperties(x5760bo, clrProcessingVisaNew);
		
		clrProcessingVisaSqlBuilder.andVersionEqualTo(clrProcessingVisaNew.getVersion());
		clrProcessingVisaNew.setVersion(clrProcessingVisaNew.getVersion() + 1);
		clrProcessingVisaNew = (ClrProcessingVisa)ClassUtil.getReflectObjectTransString(clrProcessingVisaNew);
		clrProcessingVisaDao.updateBySqlBuilderSelective(clrProcessingVisaNew,clrProcessingVisaSqlBuilder);
		
		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingVisaNew, clrProcessingVisaNew.getCustomerNo(), CoreEvent.class);
		
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingVisaNew.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5760bo.getOperatorId();    //获取操作员ID
		if (operatorId == null) {
			operatorId = "system";
		}
		String customerNo = clrProcessingVisaNew.getCustomerNo();
		//新增非金融日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5760bo.getCoreEventActivityRel().getEventNo(), x5760bo.getCoreEventActivityRel().getActivityNo(), ModificationType.UPD.getValue(), null, 
				clrProcessingVisaOld,clrProcessingVisaNew, clrProcessingVisaNew.getId(), coreSystemUnit.getCurrLogFlag(),operatorId , customerNo, customerNo, null,null);
		return "OK";
	}


}