package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5120Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.entity.CoreProductAdditionalInfo;
import com.tansun.ider.dao.issue.impl.CoreProductAdditionalInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductAdditionalInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5120BO;
import com.tansun.ider.model.vo.X5120VO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 产品信息查询
 * @author: admin
 */
@Service
public class X5120BusImpl implements X5120Bus {

	@Resource
	private CoreProductDaoImpl coreProductDaoImpl;
	@Autowired
	private CoreProductAdditionalInfoDaoImpl coreProductAdditionalInfoDaoImpl;
//	@Autowired
//	private CoreCustomerDaoImpl coreCustomerDaoImpl;
//	@Autowired
//	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	
	public static final String BSS_OP_01_0114 = "BSS.IQ.01.0004";

	@Override
	public Object busExecute(X5120BO x5120bo) throws Exception {
		// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5120bo, eventCommAreaNonFinance);

		PageBean<X5120VO> page = new PageBean<>();

		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		// 证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 客户号
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		String productObjectCode = "";
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreCustomer coreCustomer = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			 if(coreMediaBasicInfo.getMainCustomerNo() != null && coreMediaBasicInfo.getProductObjectCode() != null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
				//外部识别号只查询自己所属产品，需要得到产品对象的信息  	add by wangxi 2019/6/18
				productObjectCode = coreMediaBasicInfo.getProductObjectCode();
			}
		}
//		if (StringUtil.isNotBlank(idNumber)&&StringUtil.isNotBlank(idType)) {
//			CoreCustomer coreCustomer = queryCustomerService.queryCustomer(idType, idNumber);
//			customerNo = coreCustomer.getCustomerNo();
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null != coreCustomer) {
//				customerNo = coreCustomer.getCustomerNo();
//			} else {
//				throw new BusinessException("CUS-00014", "客户基本");
//			}
//		}

//		if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
//			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
//					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//			if (null != coreMediaBasicInfo) {
//				customerNo = coreMediaBasicInfo.getMainCustomerNo();
//			}
//		}

		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5120bo.getGlobalEventNo();
		String eventNo = globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
//		System.out.println(eventNo);
		
		CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
		coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
		//根据事件号判断查询结果
		if(BSS_OP_01_0114.equals(eventNo)){
			//外部识别号只查询自己所属产品，所以在此加一个产品对象代码的关联查询信息，用来只显示该外部识别号所属的产品  	add by wangxi 2019/6/18
			if(StringUtil.isNotBlank(externalIdentificationNo)){
				coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
			}
		}
		List<CoreProduct> listcoreProduct = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilder);

		List<X5120VO> listX5120VO = new ArrayList<>();
		String entrys = Constant.EMPTY_LIST;
		for (CoreProduct coreProduct2 : listcoreProduct) {
			X5120VO x5120VO = new X5120VO();
//			CoreProductBusinessScopeSqlBuilder coreProductBusinessScopeSqlBuilder = new CoreProductBusinessScopeSqlBuilder();
//			coreProductBusinessScopeSqlBuilder.andProductObjectCodeEqualTo(coreProduct2.getProductObjectCode());
//			@SuppressWarnings("unused")
//			List<CoreProductBusinessScope> listCoreProductBusinessScope = coreProductBusinessScopeDaoImpl
//					.selectListBySqlBuilder(coreProductBusinessScopeSqlBuilder);
			
			String key = Constant.PARAMS_FLAG + coreProduct2.getOperationMode()+ eventCommAreaNonFinance.getProductObjectCode();
			Map<String, String> ProductBusinessScopesMap = new HashMap<String, String>();
			ProductBusinessScopesMap.put("productObjectCode", coreProduct2.getProductObjectCode());
			ProductBusinessScopesMap.put("operationMode", coreProduct2.getOperationMode());
			ProductBusinessScopesMap.put("redisKey", key);
			// 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
			ProductBusinessScopesMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
			List<CoreProductBusinessScope> listCoreProductBusinessScope = betaCommonParamService.queryProductBusinessScope(ProductBusinessScopesMap);
			
			CoreProductAdditionalInfoSqlBuilder coreProductAdditionalInfoSqlBuilder = new CoreProductAdditionalInfoSqlBuilder();
			coreProductAdditionalInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
			coreProductAdditionalInfoSqlBuilder.andProductObjectCodeEqualTo(coreProduct2.getProductObjectCode());
			List<CoreProductAdditionalInfo> listCoreProductAdditionalInfo = coreProductAdditionalInfoDaoImpl
					.selectListBySqlBuilder(coreProductAdditionalInfoSqlBuilder);
			if (null != listCoreProductAdditionalInfo && !listCoreProductAdditionalInfo.isEmpty()) {
				x5120VO.setCoBrandedNo(listCoreProductAdditionalInfo.get(0).getCoBrandedNo());
			}
			CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreProduct2.getOperationMode(),
					coreProduct2.getProductObjectCode());
			CachedBeanCopy.copyProperties(coreProductObject, x5120VO);
			x5120VO.setCustomerNo(customerNo);
			x5120VO.setStatusCode(coreProduct2.getStatusCode());
			x5120VO.setCreateDate(coreProduct2.getCreateDate());//申请日期
			x5120VO.setProductCancelDate(coreProduct2.getProductCancelDate());//注销日期
			listX5120VO.add(x5120VO);
		}
		page.setTotalCount(listcoreProduct.size());
		if (null != x5120bo.getPageSize() && null != x5120bo.getIndexNo()) {
			page.setPageSize(x5120bo.getPageSize());
			page.setIndexNo(x5120bo.getIndexNo());
		}
		if (listcoreProduct.size() > 0) {
			page.setRows(listX5120VO);
			if (null != listX5120VO && !listX5120VO.isEmpty()) {
				entrys = listX5120VO.get(0).getId();
			}
		}
		// 记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5120bo.getCoreEventActivityRel().getEventNo(),
				x5120bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
				x5120bo.getOperatorId());
		return page;
	}

}
