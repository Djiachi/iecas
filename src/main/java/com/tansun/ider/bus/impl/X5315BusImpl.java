package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5315Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.beta.impl.CoreSystemUnitDaoImpl;
import com.tansun.ider.dao.beta.sqlbuilder.CoreSystemUnitSqlBuilder;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5315BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 媒介制卡信息修改(毁损补发)
 * @author: admin
 */
@Service
public class X5315BusImpl implements X5315Bus {

	@Autowired
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
	private HttpQueryService httpQueryService;
	
	
	@Override
	public Object busExecute(X5315BO x5315bo) throws Exception {
		// 公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5315bo, eventCommAreaNonFinance);
		// 媒介单元代码
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
		if (StringUtil.isBlank(eventCommAreaNonFinance.getOperationDate())) {
			String operationDate = getOperationDate(mediaUnitCode);
			eventCommAreaNonFinance.setOperationDate(operationDate);
		}
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		if (null == coreMediaCardInfo) {
			throw new BusinessException("CUS-00015", "媒介制卡信息");
		}
		coreMediaCardInfo.setPreviousProductionCode(coreMediaCardInfo.getProductionCode());
		coreMediaCardInfo.setProductionCode("3");
		coreMediaCardInfo.setPreviousProductionDate(coreMediaCardInfo.getProductionDate());
		coreMediaCardInfo.setProductionDate(eventCommAreaNonFinance.getOperationDate());
		coreMediaCardInfo.setVersion(coreMediaCardInfo.getVersion() + 1);
		int result = coreMediaCardInfoDaoImpl.updateBySqlBuilderSelective(coreMediaCardInfo,
				coreMediaCardInfoSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "媒介制卡信息");
		}
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		return eventCommAreaNonFinance;
	}

	public String getOperationDate(String mediaUnitCode) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
				.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		String mainCustomerNo = coreMediaBasicInfo.getMainCustomerNo();
		
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(mainCustomerNo);
		CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder); 
//		CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new CoreSystemUnitSqlBuilder();
//		coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo(coreCustomer.getSystemUnitNo());
//		CoreSystemUnit coreSystemUnit =coreSystemUnitDaoImpl.selectBySqlBuilder(coreSystemUnitSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		
		return coreSystemUnit.getNextProcessDate();

	}

}
