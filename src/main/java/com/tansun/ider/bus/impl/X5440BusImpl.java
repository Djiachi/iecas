package com.tansun.ider.bus.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5440Bus;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5440BO;

/**
 * @version:1.0
 * @Description: 客户定价标签修改
 * @author: admin
 */
@Service
public class X5440BusImpl implements X5440Bus {

	@Autowired
	private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;

	@Override
	public Object busExecute(X5440BO x5440bo) throws Exception {
		String customerNo = x5440bo.getCustomerNo();
		String pricingLevelCode = x5440bo.getPricingLevelCode();
		String pricingTag = x5440bo.getPricingTag();
		CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		if (StringUtil.isNotBlank(pricingLevelCode)) {
			coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(pricingLevelCode);
		}
		if (StringUtil.isNotBlank(pricingTag)) {
			coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(pricingTag);
		}
		CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao
				.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
		if (coreCustomerBusinessType != null) {
			coreCustomerBusinessType.setCustTagEffectiveDate(x5440bo.getCustTagEffectiveDate());
			coreCustomerBusinessType.setCustTagExpirationDate(x5440bo.getCustTagExpirationDate());
			coreCustomerBusinessType.setPricingObject(x5440bo.getPricingObject());
			coreCustomerBusinessType.setPricingObjectCode(x5440bo.getPricingObjectCode());
			coreCustomerBusinessType.setVersion(coreCustomerBusinessType.getVersion() + 1);
			int result = coreCustomerBusinessTypeDao.updateBySqlBuilderSelective(coreCustomerBusinessType,
					coreCustomerBusinessTypeSqlBuilder);
			if (result != 1) {
				throw new BusinessException("CUS-00012", "客户业务类型标签");
			}
		}
		return "ok";
	}

}
