package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5160Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreProductAdditionalInfo;
import com.tansun.ider.dao.issue.impl.CoreProductAdditionalInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductAdditionalInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5160BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 产品附加信息维护
 * @author: admin
 */
@Service
public class X5160BusImpl implements X5160Bus {

	@Autowired
	private CoreProductAdditionalInfoDaoImpl coreProductAdditionalInfoDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;

	@SuppressWarnings("null")
	@Override
	public Object busExecute(X5160BO x5160bo) throws Exception {

		// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(x5160bo, eventCommAreaNonFinance);
		String id = eventCommAreaNonFinance.getId();
		String productObjectCode  = eventCommAreaNonFinance.getProductObjectCode();
		String customerNo  = x5160bo.getCustomerNo();
		String coBrandedNo = x5160bo.getCoBrandedNo();
		CoreProductAdditionalInfoSqlBuilder coreProductAdditionalInfoSqlBuilder = new CoreProductAdditionalInfoSqlBuilder();
		if (null != id && !"".equals(id)) {
			coreProductAdditionalInfoSqlBuilder.andIdEqualTo(id);
		}
		if (StringUtil.isNotBlank(productObjectCode)) {
			coreProductAdditionalInfoSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		}
		if (StringUtil.isNotBlank(customerNo)) {
			coreProductAdditionalInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		CoreProductAdditionalInfo coreProductAdditionalInfo = coreProductAdditionalInfoDaoImpl
				.selectBySqlBuilder(coreProductAdditionalInfoSqlBuilder);

		String operatorId = x5160bo.getOperatorId();
		if (operatorId == null) {
			operatorId = "system";
		}
		if (null == coreProductAdditionalInfo) {
			CoreProductAdditionalInfo coreProductAdditionalInfoStr = new CoreProductAdditionalInfo();
			coreProductAdditionalInfoStr.setCoBrandedNo(coBrandedNo);
			coreProductAdditionalInfoStr.setCustomerNo(customerNo);
			coreProductAdditionalInfoStr.setProductObjectCode(productObjectCode);
			coreProductAdditionalInfoStr.setVersion(1);
			coreProductAdditionalInfoStr.setId(RandomUtil.getUUID());
			coreProductAdditionalInfoDaoImpl.insert(coreProductAdditionalInfoStr);
			//获取当前日志标识
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreProductAdditionalInfoStr.getCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			nonFinancialLogUtil.createNonFinancialActivityLog(x5160bo.getEventNo(),x5160bo.getActivityNo(),ModificationType.ADD.getValue(), null, null,
					coreProductAdditionalInfoStr,coreProductAdditionalInfoStr.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
					coreProductAdditionalInfoStr.getCustomerNo(),coreProductAdditionalInfoStr.getProductObjectCode(),null,null);
			
		}else {
			CoreProductAdditionalInfo coreProductAdditionalInfoAfter = new CoreProductAdditionalInfo();
			CachedBeanCopy.copyProperties(coreProductAdditionalInfo, coreProductAdditionalInfoAfter);
			if (StringUtil.isNotBlank(coBrandedNo)) {
				coreProductAdditionalInfo.setCoBrandedNo(coBrandedNo);
			}

			coreProductAdditionalInfo.setVersion(coreProductAdditionalInfo.getVersion() + 1);
			int result = coreProductAdditionalInfoDaoImpl.updateBySqlBuilderSelective(coreProductAdditionalInfo,
					coreProductAdditionalInfoSqlBuilder);
			if (result != 1) {
				throw new BusinessException("CUS-00012", " 产品附加信息");
			}
			//获取当前日志标识
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreProductAdditionalInfo.getCustomerNo());
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			nonFinancialLogUtil.createNonFinancialActivityLog(x5160bo.getEventNo(),x5160bo.getActivityNo(),ModificationType.UPD.getValue(), null,
					coreProductAdditionalInfoAfter,coreProductAdditionalInfo,coreProductAdditionalInfo.getId(),coreSystemUnit.getCurrLogFlag(), operatorId,
					coreProductAdditionalInfo.getCustomerNo(),coreProductAdditionalInfo.getProductObjectCode(),null,null);
			}
		
//		CachedBeanCopy.copyProperties(coreProductAdditionalInfo, eventCommArea);


		return eventCommAreaNonFinance;
	}

}
