package com.tansun.ider.bus.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5441Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5440BO;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.OperationModeUtil;

/**
 * 客户定价标签删除
 * @author zhangte
 * @date 2019年8月7日
 */
@Service
public class X5441BusImpl implements X5441Bus {

	@Autowired
	private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;
	@Autowired
	public OperationModeUtil operationModeUtil;

	@Override
	public Object busExecute(X5440BO x5440bo) throws Exception {
		String customerNo = x5440bo.getCustomerNo();
		String pricingLevelCode = x5440bo.getPricingLevelCode();
		String pricingTag = x5440bo.getPricingTag();
		String id = x5440bo.getId();
		CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
		if (StringUtil.isNotBlank(id)) {
		    coreCustomerBusinessTypeSqlBuilder.andIdEqualTo(id);
		}
		if (StringUtil.isNotBlank(customerNo)) {
			coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
		}else{
		    throw new BusinessException("PARAM-00001","customerNo");
		}
		if (StringUtil.isNotBlank(pricingLevelCode)) {
			coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(pricingLevelCode);
		}else{
            throw new BusinessException("PARAM-00001","pricingLevelCode");
        }
		if (StringUtil.isNotBlank(pricingTag)) {
			coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(pricingTag);
		}else{
            throw new BusinessException("PARAM-00001","pricingTag");
        }
		CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
		coreCustomerBusinessType.setState("D");
		coreCustomerBusinessType.setRemovalUserid(x5440bo.getOperatorId());
		 CoreSystemUnit coreSystemUnit = operationModeUtil.getcoreOperationMode(coreCustomerBusinessType.getCustomerNo());
			String operationDate = "";
			if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
				operationDate = coreSystemUnit.getCurrProcessDate();
			} else {
				operationDate = coreSystemUnit.getNextProcessDate();
			}
		coreCustomerBusinessType.setRemoveDate(operationDate);
		coreCustomerBusinessTypeSqlBuilder.andVersionEqualTo(coreCustomerBusinessType.getVersion());
		coreCustomerBusinessType.setVersion(coreCustomerBusinessType.getVersion()+1);
		coreCustomerBusinessTypeDao.updateBySqlBuilder(coreCustomerBusinessType, coreCustomerBusinessTypeSqlBuilder);
		return "ok";
	}

}
