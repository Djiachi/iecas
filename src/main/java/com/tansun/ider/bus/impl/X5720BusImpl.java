package com.tansun.ider.bus.impl;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5720Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5720BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.LoggerUtil;
import com.tansun.ider.util.NonFinancialLogUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * MC调单申请建立
 * @ClassName X5720BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年1月25日 上午11:02:18
 * @version 1.0.0
 */
@Service
public class X5720BusImpl implements X5720Bus {
	@Resource
    private ClrProcessingMcDao clrProcessingMcDao;
	@Resource
    private LoggerUtil loggerUtil;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;

    @Override
    public Object busExecute(X5720BO x5720bo) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(x5720bo);
		// 判断交易全局流水号是否存在
		this.existGlobalSerialNumbr(x5720bo);
		// 生成并放到缓存中
		this.saveClrRtrqVisa(x5720bo);
		return "OK";
    }
    
    
	private void saveClrRtrqVisa(X5720BO x5720bo) throws Exception {
		ClrProcessingMc clrProcessingMc = new ClrProcessingMc();
		CachedBeanCopy.copyProperties(x5720bo, clrProcessingMc);
		clrProcessingMc.setId(RandomUtil.getUUID());
		clrProcessingMc.setVersion(1);
		clrProcessingMc.setRetrievalStatus("N");
		clrProcessingMc.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
		clrProcessingMcDao.insert(clrProcessingMc);
    	
		// 更新redis缓存
		paramsUtil.redisCommit(clrProcessingMc, clrProcessingMc.getCustomerNo(), ClrProcessingMc.class);
		
		//获取当前日志标识
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(clrProcessingMc.getCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		String operatorId = x5720bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		
		// 新增非金融参数日志
		nonFinancialLogUtil.createNonFinancialActivityLog(x5720bo.getEventNo(), x5720bo.getActivityNo(),
				ModificationType.ADD.getValue(), null, null, clrProcessingMc, clrProcessingMc.getId(),
				coreSystemUnit.getCurrLogFlag(), operatorId, x5720bo.getCustomerNo(), x5720bo.getCustomerNo(), null, null);
		
	}
    	
    	
	/**
	 * 判断交易全局流水号是否存在
	 * 
	 * @param x5705bo
	 * @throws Exception
	 */
	private void existGlobalSerialNumbr(X5720BO x5720bo) throws Exception {
		ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
		clrProcessingMcSqlBuilder.andOldGlobalSerialNumbrEqualTo(x5720bo.getOldGlobalSerialNumbr());
		ClrProcessingMc clrProcessingMc = clrProcessingMcDao.selectBySqlBuilder(clrProcessingMcSqlBuilder);
		if (clrProcessingMc != null) {
			throw new BusinessException("PARAM-00003", "该调单申请");
		}
	}	
    	
    	
/*    	//校验是否已申请
    	
    	
    	ClrRtrqMc clrRtrqMc = new ClrRtrqMc();
    	CachedBeanCopy.copyProperties(clrRtrqMc, x5720bo);
    	clrRtrqMc.setId(RandomUtil.getUUID());
    	clrRtrqMc.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
    	clrRtrqMcDao.insert(clrRtrqMc);
    	//新增非金融日志
    	loggerUtil.logNonParamInsert(x5720bo.getEventNo(), x5720bo.getActivityNo(), 
    			ModificationType.ADD.getValue(), clrRtrqMc, new ClrRtrqMc(), clrRtrqMc.getId(), x5720bo.getOperatorId(), null);
        return "OK";*/
   
   
}
