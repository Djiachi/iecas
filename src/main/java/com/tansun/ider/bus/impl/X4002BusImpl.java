package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X4002Bus;
import com.tansun.ider.dao.issue.CoreTransHistDao;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4002BO;

@Service
public class X4002BusImpl implements X4002Bus {
	
	@Autowired
	private CoreTransHistDao coreTransHistDao;

	@Override
	public Object busExecute(X4002BO x4002bo) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(x4002bo);
		String loanSign = x4002bo.getLoanSign();
		if("D".equals(loanSign)){
			String reimbursementStatus = x4002bo.getReimbursementStatus();
			if("Y".equals(reimbursementStatus)){
				throw new BusinessException("该交易已报销");
			}
			CoreTransHistSqlBuilder coreTransHistSqlBuilder = equalsTo(x4002bo);
			List<CoreTransHist> listBySqlBuilder = coreTransHistDao.selectListBySqlBuilder(coreTransHistSqlBuilder);
			for (CoreTransHist coreTransHist : listBySqlBuilder) {
				CoreTransHistSqlBuilder coreTransHistSqlBuilderNew = new CoreTransHistSqlBuilder();
				coreTransHistSqlBuilderNew.andCustomerNoEqualTo(coreTransHist.getCustomerNo());
				coreTransHistSqlBuilderNew.andEntityKeyEqualTo(coreTransHist.getEntityKey());
				coreTransHistSqlBuilderNew.andGlobalSerialNumbrEqualTo(coreTransHist.getGlobalSerialNumbr());
				coreTransHistSqlBuilderNew.andLogLevelEqualTo(coreTransHist.getLogLevel());
				coreTransHistSqlBuilderNew.andOccurrTimeEqualTo(coreTransHist.getOccurrTime());
				coreTransHistSqlBuilderNew.andCurrencyCodeEqualTo(coreTransHist.getCurrencyCode());
				coreTransHistSqlBuilderNew.andBalanceTypeEqualTo(coreTransHist.getBalanceType());
				coreTransHistSqlBuilderNew.andVersionEqualTo(coreTransHist.getVersion());
				if("Y".equals(coreTransHist.getReimburseStatus())){
					throw new BusinessException("该交易已报销");
				}
				coreTransHist.setReimburseStatus("Y");
				coreTransHist.setVersion(coreTransHist.getVersion()+1);
				int result = coreTransHistDao.updateBySqlBuilder(coreTransHist, coreTransHistSqlBuilderNew);
				if (result != 1) {
					throw new BusinessException("CUS-00012", "报销状态");
				}
			}
			return "ok";
		}else{
			throw new BusinessException("非借记不可报销！");
		}
		
	}
	/**
	 * 
	 * @Description: TODO()   
	 * @param: @param x4002bo
	 * @return: CoreTransHistSqlBuilder      
	 * @throws
	 */
	private CoreTransHistSqlBuilder equalsTo(X4002BO x4002bo){
		String customerNo = x4002bo.getCustomerNo();
		String entityKey = x4002bo.getEntityKey();
		String globalSerialNumbr = x4002bo.getGlobalSerialNumbr();
		String logLevel = x4002bo.getLogLevel();
		String occurrTime = x4002bo.getOccurrTime();
		String currencyCode = x4002bo.getCurrencyCode();
		String balanceType = x4002bo.getBalanceType();
		CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
		if(StringUtil.isNotBlank(customerNo)){
			coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
//		if(StringUtil.isNotBlank(entityKey)){
//			coreTransHistSqlBuilder.andEntityKeyEqualTo(entityKey);
//		}
		if(StringUtil.isNotBlank(globalSerialNumbr)){
			coreTransHistSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
		}
//		if(StringUtil.isNotBlank(logLevel)){
//			coreTransHistSqlBuilder.andLogLevelEqualTo(logLevel);
//		}
//		if(StringUtil.isNotBlank(occurrTime)){
//			coreTransHistSqlBuilder.andOccurrTimeEqualTo(occurrTime);
//		}
		if(StringUtil.isNotBlank(currencyCode)){
			coreTransHistSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
		}
		if(StringUtil.isNotBlank(balanceType)){
			coreTransHistSqlBuilder.andBalanceTypeEqualTo(balanceType);
		}
		return coreTransHistSqlBuilder;
	}

}
