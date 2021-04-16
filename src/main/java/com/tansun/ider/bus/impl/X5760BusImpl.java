package com.tansun.ider.bus.impl;

import java.math.BigDecimal;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5760Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingVisaDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingVisa;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingVisaSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5760BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CurrencyUtils;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;
/**
 * 
 * @ClassName X5760BusImpl
 * @Description TODO(VISA调单申请建立)
 * @author yanyingzhao
 * @Date 2019年2月14日 下午5:36:09
 * @version 1.0.0
 */
@Service
public class X5760BusImpl implements X5760Bus {
	
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private CurrencyUtils currencyUtils;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private ClrProcessingVisaDao clrProcessingVisaDao;
	
	@Override
	public String busExecute(X5760BO x5760bo) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(x5760bo);
		// 判断交易全局流水号是否存在
		this.existGlobalSerialNumbr(x5760bo);
		// 生成并放到缓存中
		this.saveClrProcessingVisa(x5760bo);
		return "OK";
	}
	
	private void saveClrProcessingVisa(X5760BO x5760bo) throws Exception {
		ClrProcessingVisa clrProcessingVisa = new ClrProcessingVisa();
		CachedBeanCopy.copyProperties(x5760bo, clrProcessingVisa);
		clrProcessingVisa.setId(RandomUtil.getUUID());
		clrProcessingVisa.setVersion(1);
		clrProcessingVisa.setProtestStatus("N");
		clrProcessingVisa.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		BigDecimal transAmt = currencyUtils.conversionAmount(x5760bo.getTransAmt(), x5760bo.getTransCurrCode(),
				CurrencyUtils.input);
		BigDecimal clearAmount = currencyUtils.conversionAmount(x5760bo.getClearAmount(), x5760bo.getClearCurrCode(),
				CurrencyUtils.input);
		BigDecimal authorizedAmount = currencyUtils.conversionAmount(x5760bo.getAuthorizedAmount(), x5760bo.getAuthorizationCurrencyCode(),
				CurrencyUtils.input);
		clrProcessingVisa.setTransAmt(transAmt);
		clrProcessingVisa.setClearAmount(clearAmount);
		clrProcessingVisa.setAuthorizedAmount(authorizedAmount);
		clrProcessingVisaDao.insert(clrProcessingVisa);

		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingVisa, clrProcessingVisa.getCustomerNo(), ClrProcessingVisa.class);
		
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingVisa.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5760bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		
		// 新增非金融参数日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5760bo.getEventNo(), x5760bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, null, clrProcessingVisa, clrProcessingVisa.getId(),
				coreSystemUnit.getCurrLogFlag(), operatorId, x5760bo.getCustomerNo(), x5760bo.getCustomerNo(), null, null);

	}
	
	/**
	 * 判断交易全局流水号是否存在
	 * 
	 * @param x5760bo
	 * @throws Exception
	 */
	private void existGlobalSerialNumbr(X5760BO x5760bo) throws Exception {
		ClrProcessingVisaSqlBuilder clrProcessingVisaSqlBuilder = new ClrProcessingVisaSqlBuilder();
		clrProcessingVisaSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5760bo.getOldGlobalSerialNumbr());
		ClrProcessingVisa clrProcessingVisa = clrProcessingVisaDao.selectBySqlBuilder(clrProcessingVisaSqlBuilder);
		if (clrProcessingVisa != null) {
			throw new BusinessException("PARAM-00003", "该拒付请求");
		}
	}

}