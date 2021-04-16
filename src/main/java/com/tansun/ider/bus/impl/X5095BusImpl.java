package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5095Bus;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5095BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 产品资料维护
 * @author: admin
 */
@Service
public class X5095BusImpl implements X5095Bus {

	@Autowired
	private CoreProductDaoImpl coreProductDaoImpl;

	@Override
	public Object busExecute(X5095BO x5095bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5095bo, eventCommAreaNonFinance);
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andIdEqualTo(eventCommAreaNonFinance.getId());
		CoreProduct coreProduct = coreProductDaoImpl.selectBySqlBuilder(coreProductSqlBuilder);
		CachedBeanCopy.copyProperties(coreProduct, eventCommAreaNonFinance);
		coreProduct.setVersion(coreProduct.getVersion() + 1);
		int result = coreProductDaoImpl.updateBySqlBuilder(coreProduct, coreProductSqlBuilder);
		if (result != 1) {
			throw new BusinessException("CUS-00012", "产品单元基本信息");
		}

		return coreProduct;
	}

}
