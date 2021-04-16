package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5785Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5775BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.ClassUtil;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;
/**
 * MC拒付维护
 * 
 * @ClassName X5785BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月20日 上午10:25:36
 * @version 1.0.0
 */
@Service
public class X5785BusImpl implements X5785Bus {

	@Autowired
	private ClrProcessingMcDao clrProcessingMcDao;
	
	@Autowired
	private ParamsUtil paramsUtil;
	
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public String busExecute(X5775BO x5775bo) throws Exception {
		// 更新
		ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
		clrProcessingMcSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5775bo.getOldGlobalSerialNumbr());
		ClrProcessingMc clrProcessingMcNew = clrProcessingMcDao.selectBySqlBuilder(clrProcessingMcSqlBuilder);
		ClrProcessingMc clrProcessingMcOld = new ClrProcessingMc();
		CachedBeanCopy.copyProperties(clrProcessingMcNew, clrProcessingMcOld);
		CachedBeanCopy.copyProperties(x5775bo, clrProcessingMcNew);
		
		clrProcessingMcSqlBuilder.andVersionEqualTo(clrProcessingMcNew.getVersion());
		clrProcessingMcNew.setVersion(clrProcessingMcNew.getVersion() + 1);
		clrProcessingMcNew = (ClrProcessingMc)ClassUtil.getReflectObjectTransString(clrProcessingMcNew);
		clrProcessingMcDao.updateBySqlBuilderSelective(clrProcessingMcNew,clrProcessingMcSqlBuilder);
		
		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingMcNew, clrProcessingMcNew.getCustomerNo(), CoreEvent.class);
		
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingMcNew.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5775bo.getOperatorId();    //获取操作员ID
		if (operatorId == null) {
			operatorId = "system";
		}
		String customerNo = clrProcessingMcNew.getCustomerNo();
		//新增非金融日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5775bo.getCoreEventActivityRel().getEventNo(), x5775bo.getCoreEventActivityRel().getActivityNo(), ModificationType.UPD.getValue(), null, 
				clrProcessingMcOld,clrProcessingMcNew, clrProcessingMcNew.getId(), coreSystemUnit.getCurrLogFlag(),operatorId , customerNo, customerNo, null,null);
		return "OK";
	}


}