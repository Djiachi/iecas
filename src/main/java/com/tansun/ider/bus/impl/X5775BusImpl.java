package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5775Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5775BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;
/**
 * 
 * @ClassName X5775BusImpl
 * @Description TODO(MC调单申请建立)
 * @author yanyingzhao
 * @Date 2019年2月20日 下午5:36:09
 * @version 1.0.0
 */
@Service
public class X5775BusImpl implements X5775Bus {
	
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private ClrProcessingMcDao clrProcessingMcDao;
	
	@Override
	public String busExecute(X5775BO x5775bo) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(x5775bo);
		// 判断交易全局流水号是否存在
		this.existGlobalSerialNumbr(x5775bo);
		// 生成并放到缓存中
		this.saveClrProcessingMc(x5775bo);
		return "OK";
	}
	
	private void saveClrProcessingMc(X5775BO x5775bo) throws Exception {
		ClrProcessingMc clrProcessingMc = new ClrProcessingMc();
		CachedBeanCopy.copyProperties(x5775bo, clrProcessingMc);
		clrProcessingMc.setId(RandomUtil.getUUID());
		clrProcessingMc.setVersion(1);
		clrProcessingMc.setProtestStatus("N");
		clrProcessingMc.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		clrProcessingMcDao.insert(clrProcessingMc);

		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingMc, clrProcessingMc.getCustomerNo(), ClrProcessingMc.class);
		
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingMc.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5775bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		
		// 新增非金融参数日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5775bo.getEventNo(), x5775bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, null, clrProcessingMc, clrProcessingMc.getId(),
				coreSystemUnit.getCurrLogFlag(), operatorId, x5775bo.getCustomerNo(), x5775bo.getCustomerNo(), null, null);

	}
	
	/**
	 * 判断交易全局流水号是否存在
	 * 
	 * @param x5775bo
	 * @throws Exception
	 */
	private void existGlobalSerialNumbr(X5775BO x5775bo) throws Exception {
		ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
		clrProcessingMcSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5775bo.getOldGlobalSerialNumbr());
		ClrProcessingMc clrProcessingMc = clrProcessingMcDao.selectBySqlBuilder(clrProcessingMcSqlBuilder);
		if (clrProcessingMc != null) {
			throw new BusinessException("PARAM-00003", "该拒付请求");
		}
	}

}