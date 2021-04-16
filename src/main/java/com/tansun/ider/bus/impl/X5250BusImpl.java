package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5250Bus;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.sqlbuilder.CoreProductBusinessScopeSqlBuilder;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreProductAdditionalInfo;
import com.tansun.ider.dao.issue.impl.CoreProductAdditionalInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductAdditionalInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.model.X5120VO;
import com.tansun.ider.model.bo.X5250BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * @version:1.0
 * @Description: 客户产品列表查询
 * @author: admin
 */
@Service
public class X5250BusImpl implements X5250Bus {

	@Autowired
	private CoreProductDaoImpl coreProductDaoImpl;
	@Autowired
	private CoreProductAdditionalInfoDaoImpl coreProductAdditionalInfoDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5250BO x5250bo) throws Exception {
		// 身份证号
		String idNumber = x5250bo.getIdNumber();
		// 外部识别号
		String externalIdentificationNo = x5250bo.getExternalIdentificationNo();
		//证件类型
		String idType = x5250bo.getIdType();
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		CachedBeanCopy.copyProperties(x5250bo, eventCommAreaNonFinance);
//		CoreCustomer coreCustomer = queryCustomerService.queryCoreCustomer(idNumber, externalIdentificationNo);
		
		//CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreCustomer);
		
		String customerNo = queryCustomerService.queryCustomerNo(idType, idNumber, externalIdentificationNo);
		
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		List<CoreProduct> listCoreProducts = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilder);
		eventCommAreaNonFinance.setCustomerNo(customerNo);
		eventCommAreaNonFinance.setListCoreProducts(listCoreProducts);
		List<X5120VO> listX5120VOs = new ArrayList<>();
		for (CoreProduct coreProduct2 : listCoreProducts) {
			X5120VO x5120VO = new X5120VO();
			CoreProductBusinessScopeSqlBuilder coreProductBusinessScopeSqlBuilder = new CoreProductBusinessScopeSqlBuilder();
			coreProductBusinessScopeSqlBuilder.andProductObjectCodeEqualTo(coreProduct2.getProductObjectCode());
			@SuppressWarnings("unused")
			// List<CoreProductBusinessScope> listCoreProductBusinessScope =
			// coreProductBusinessScopeDaoImpl
			// .selectListBySqlBuilder(coreProductBusinessScopeSqlBuilder);
			List<CoreProductBusinessScope> listCoreProductBusinessScope = httpQueryService
					.queryProductBusinessScope(coreProduct2.getProductObjectCode(), coreProduct2.getOperationMode());

			CoreProductAdditionalInfoSqlBuilder coreProductAdditionalInfoSqlBuilder = new CoreProductAdditionalInfoSqlBuilder();
			coreProductAdditionalInfoSqlBuilder.andProductObjectCodeEqualTo(coreProduct2.getProductObjectCode());
			coreProductAdditionalInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
			List<CoreProductAdditionalInfo> listCoreProductAdditionalInfos = coreProductAdditionalInfoDaoImpl
					.selectListBySqlBuilder(coreProductAdditionalInfoSqlBuilder);
			if (null != listCoreProductAdditionalInfos && !listCoreProductAdditionalInfos.isEmpty()) {
				x5120VO.setCoBrandedNo(listCoreProductAdditionalInfos.get(0).getCoBrandedNo());
			}
			CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreProduct2.getOperationMode(),
					coreProduct2.getProductObjectCode());
			CachedBeanCopy.copyProperties(coreProductObject, x5120VO);
			x5120VO.setCustomerNo(customerNo);
			listX5120VOs.add(x5120VO);
		}
		eventCommAreaNonFinance.setListX5120VOs(listX5120VOs);
		eventCommAreaNonFinance.setCustomerNo(customerNo);
		return eventCommAreaNonFinance;
	}

}
