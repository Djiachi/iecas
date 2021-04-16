package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5595Bus;
import com.tansun.ider.dao.issue.CoreCustomerElementDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5580BO;

/**
 * X5595 客户个性化元件删除
 * 
 * @author yanzhaofei
 * @Date 2019年1月24日15:03:31
 */
@Service
public class X5595BusImpl implements X5595Bus {
//	@Autowired
//	private CoreCustomerElement coreCustomerElement;
	@Autowired
	private CoreCustomerElementDao coreCustomerElementDao;

	@Override
	public Object busExecute(X5580BO x5580Bo) throws Exception {
		CoreCustomerElement coreCustomerElement = new CoreCustomerElement();
		coreCustomerElement.setId(x5580Bo.getId());
		int result = coreCustomerElementDao.deleteByPrimaryKey(coreCustomerElement);
		if (result ==0) {
			throw new BusinessException("客户个性化元件删除失败！");
		}
		return "OK";
	}
}
