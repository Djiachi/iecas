package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5420Bus;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5420BO;

/**
 * @version:1.0
 * @Description: 产品形式信息查询
 * @author: admin
 */
@Service
public class X5420BusImpl implements X5420Bus{

	@Autowired
	private CoreProductFormDao coreProductFormDao;
	
	@Override
	public Object busExecute(X5420BO x5420bo) throws Exception {
		PageBean<CoreProductForm> page = new PageBean<>();
		String productForm = x5420bo.getProductForm();
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		coreProductFormSqlBuilder.andProductFormEqualTo(productForm);
		int totalCount = coreProductFormDao.countBySqlBuilder(coreProductFormSqlBuilder);
		page.setTotalCount(totalCount);
		if (totalCount > 0) {
			List<CoreProductForm> list = coreProductFormDao.selectListBySqlBuilder(coreProductFormSqlBuilder);
			page.setRows(list);
		}
		return page;
	}

}
