package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5305Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5305BO;
import com.tansun.ider.model.vo.X5305VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;

/**
 * @version:1.0
 * @Description: 媒介基本信息查询
 * @author: admin
 */
@Service
public class X5305BusImpl implements X5305Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	// @Autowired
	// private CoreMediaObjectDaoImpl coreMediaObjectDaoImpl;
	@Resource
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Resource
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;
	// @Autowired
	// private CoreProductObjectDaoImpl coreProductObjectDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreProductFormDao coreProductFormDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CardUtil cardUtil;
	
	public static final String BSS_OP_01_0007 = "BSS.IQ.01.0007";
	public static final String BSS_OP_01_0017 = "BSS.IQ.01.0017";
	
	
	@Override
	public Object busExecute(X5305BO x5305bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5305bo,eventCommAreaNonFinance);
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		//证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		String externalIdentificationNo = x5305bo.getExternalIdentificationNo();
		
		Object object = null;
		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5305bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		//BSS_IQ_01_0017事件只支持无效卡查询	客户统一视图菜单下客户媒介信息查询时支持无效卡查询。
		if (BSS_OP_01_0017.equals(eventNo)){
			object = queryCustomerService.queryCustomerInvalid(idType, idNumber, externalIdentificationNo);//支持查询无效卡
		} else {//查询有效卡
			object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);//查询有效卡
		}
		
		if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
//		if (StringUtil.isNotBlank(idNumber)&&StringUtil.isNotBlank(idType)) {
//			CoreCustomer coreCustomer = queryCustomerService.queryCustomer(idType, idNumber);
//				customerNo = coreCustomer.getCustomerNo();
//		}
		String productObjectCode = eventCommAreaNonFinance.getProductObjectCode();
		String operatorId = x5305bo.getOperatorId();
//		if (StringUtil.isNotBlank(idNumber)) {
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null != coreCustomer) {
//				customerNo = coreCustomer.getCustomerNo();
//			} else {
//				throw new BusinessException("CUS-00014", "客户基本");
//			}
//		}
		
		//得到事件号   根据全局事件号进行截取操作
//		String globalEventNo = x5305bo.getGlobalEventNo();
//		String eventNo = globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串 
//		System.out.println(eventNo);
		
		PageBean<X5305VO> page = new PageBean<>();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
		}
		if (StringUtil.isNotBlank(productObjectCode)) {
			coreMediaBasicInfoSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
		}
		//只有“基础业务受理”菜单下的媒介相关查询媒介基本信息的，传外部识别号查询该媒介下的信息。    add by wangxi 2019/6/21
		if(BSS_OP_01_0007.equals(eventNo)){
			if (StringUtil.isNotBlank(externalIdentificationNo)) {
				coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			}
		}
		if (StringUtil.isBlank(x5305bo.getFlag())) {
			//注释掉原因：客户媒介视图需要改成查所有的，这行代码只是查出来有效卡片，所以注释。         add by wangxi 2019/5/22
//			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		} else if ("2".equals(x5305bo.getFlag())) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderStr.orInvalidFlagEqualTo("N");
			coreMediaBasicInfoSqlBuilderStr.orInvalidFlagEqualTo("Y");
			coreMediaBasicInfoSqlBuilderStr.orInvalidReasonEqualTo(InvalidReasonStatus.RNA.getValue());
			coreMediaBasicInfoSqlBuilder.and(coreMediaBasicInfoSqlBuilderStr);
		} /*else if("3".equals(x5305bo.getFlag()) || "4".equals(x5305bo.getFlag())){
		    coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		}*/

		int rowsCount = coreMediaBasicInfoDaoImpl.countBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		page.setPageSize(rowsCount);
		if (null != x5305bo.getPageSize() && null != x5305bo.getIndexNo()) {
			page.setPageSize(x5305bo.getPageSize());
			page.setIndexNo(x5305bo.getIndexNo());
		}

		if (rowsCount > 0) {
			List<X5305VO> listX5305VO = new ArrayList<X5305VO>();
			List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDaoImpl
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			for (CoreMediaBasicInfo coreMediaBasicInfo : listCoreMediaBasicInfos) {
				X5305VO x5305VO = new X5305VO();
				x5305VO.setOperatorId(operatorId);
				// CoreMediaObjectSqlBuilder coreMediaObjectSqlBuilder = new
				// CoreMediaObjectSqlBuilder();
				// coreMediaObjectSqlBuilder.andMediaObjectCodeEqualTo(coreMediaBasicInfo.getMediaObjectCode());
				// CoreMediaObject coreMediaObject =
				// coreMediaObjectDaoImpl.selectBySqlBuilder(coreMediaObjectSqlBuilder);
				CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(
						coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getMediaObjectCode());
				CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
				coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
				CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl
						.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
				// CoreProductObjectSqlBuilder coreProductObjectSqlBuilder = new
				// CoreProductObjectSqlBuilder();
				// coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
				// CoreProductObject coreProductObject =
				// coreProductObjectDaoImpl
				// .selectBySqlBuilder(coreProductObjectSqlBuilder);
				
				CoreCorporationEntity coreCorporationEntity = cardUtil
						.getSystemUnitNoCoreCorporationEntity(coreMediaBasicInfo.getInstitutionId());
				
				CoreProductObject coreProductObject = httpQueryService.queryProductObject(
						coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
				x5305VO.setProductDesc(coreProductObject.getProductDesc());
				CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(coreProductObject.getBinNo().toString(),coreCorporationEntity,operatorId,null);
				CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
				coreProductFormSqlBuilder.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
				CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
				if (coreProductForm != null) {
					CachedBeanCopy.copyProperties(coreProductForm,x5305VO);
					if ("1".equals(coreProductForm.getMainSupplyIndicator())) {
						x5305VO.setMediaHolderNo("");
					}
				}
				if (coreMediaObject != null) {
					CachedBeanCopy.copyProperties(coreMediaObject,x5305VO);
				}
				if (coreMediaBasicInfo != null) {
					CachedBeanCopy.copyProperties(coreMediaBasicInfo,x5305VO);
				}
				if (coreMediaCardInfo != null) {
					CachedBeanCopy.copyProperties(coreMediaCardInfo,x5305VO);
				}
				if (coreIssueCardBin != null) {
					CachedBeanCopy.copyProperties(coreIssueCardBin,x5305VO);
				}
				if("3".equals(x5305bo.getFlag())){//DPAN信息:查询为3的标识，筛选有效的，媒介形式为R,V的
				    if(coreProductForm != null){
				    	if (StringUtil.isNotBlank(coreProductForm.getMediaObjectForm())) {
				    		if(!coreProductForm.getMediaObjectForm().equals("T")){
					            CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreMediaBasicInfo.getInstitutionId());
					            x5305VO.setOrganName(coreOrgan.getOrganName());
					            listX5305VO.add(x5305VO);
					        }
						}else {
							listX5305VO.add(x5305VO);
						}
				    }
				}else if("4".equals(x5305bo.getFlag())){
				    if(coreProductForm != null){
                        if(StringUtil.isNotBlank(coreProductForm.getMediaObjectForm()) && coreProductForm.getMediaObjectForm().equals("T")){
                            listX5305VO.add(x5305VO);
                        }
                    }
				}else{
				    listX5305VO.add(x5305VO);
				}
			}
			//查询改客户是否别人的附属卡
			if (BSS_OP_01_0017.equals(eventNo) && StringUtil.isNotBlank(customerNo)){
				CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
				coreProductFormSqlBuilder.andMediaHolderNoEqualTo(customerNo);
				coreProductFormSqlBuilder.andMainSupplyIndicatorEqualTo("2");
				List<CoreProductForm> coreProductFormList = coreProductFormDao.selectListBySqlBuilder(coreProductFormSqlBuilder);
				if (null != coreProductFormList && !coreProductFormList.isEmpty()) {
					CoreProductForm coreProductForm = coreProductFormList.get(0);
					String productHolderNo = coreProductForm.getProductHolderNo();
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilder1.andMainCustomerNoEqualTo(productHolderNo);
					coreMediaBasicInfoSqlBuilder1.andMainSupplyIndicatorEqualTo("2");
					List<CoreMediaBasicInfo> listCoreMediaBasicInfos1 = coreMediaBasicInfoDaoImpl
							.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder1);
					if (null != listCoreMediaBasicInfos1 && !listCoreMediaBasicInfos1.isEmpty()) {
						for (CoreMediaBasicInfo coreMediaBasicInfo : listCoreMediaBasicInfos1) {
							X5305VO x5305VO = new X5305VO();
							x5305VO.setOperatorId(operatorId);
							CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(
									coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getMediaObjectCode());
							CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
							coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
							CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl
									.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
							CoreCorporationEntity coreCorporationEntity = cardUtil
									.getSystemUnitNoCoreCorporationEntity(coreMediaBasicInfo.getInstitutionId());
							CoreProductObject coreProductObject = httpQueryService.queryProductObject(
									coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
							x5305VO.setProductDesc(coreProductObject.getProductDesc());
							CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(coreProductObject.getBinNo().toString(),coreCorporationEntity,operatorId,null);
							CoreProductFormSqlBuilder coreProductFormSqlBuilder1 = new CoreProductFormSqlBuilder();
							coreProductFormSqlBuilder1.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
							CoreProductForm coreProductForm1 = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder1);
							if (coreProductForm1 != null) {
								CachedBeanCopy.copyProperties(coreProductForm1,x5305VO);
								if ("1".equals(coreProductForm1.getMainSupplyIndicator())) {
									x5305VO.setMediaHolderNo("");
								}
							}
							if (coreMediaObject != null) {
								CachedBeanCopy.copyProperties(coreMediaObject,x5305VO);
							}
							if (coreMediaBasicInfo != null) {
								CachedBeanCopy.copyProperties(coreMediaBasicInfo,x5305VO);
							}
							if (coreMediaCardInfo != null) {
								CachedBeanCopy.copyProperties(coreMediaCardInfo,x5305VO);
							}
							if (coreIssueCardBin != null) {
								CachedBeanCopy.copyProperties(coreIssueCardBin,x5305VO);
							}
							if("3".equals(x5305bo.getFlag())){//DPAN信息:查询为3的标识，筛选有效的，媒介形式为R,V的
							    if(coreProductForm != null){
							    	if (StringUtil.isNotBlank(coreProductForm.getMediaObjectForm())) {
							    		if(!coreProductForm.getMediaObjectForm().equals("T")){
								            CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreMediaBasicInfo.getInstitutionId());
								            x5305VO.setOrganName(coreOrgan.getOrganName());
								            listX5305VO.add(x5305VO);
								        }
									}else {
										listX5305VO.add(x5305VO);
									}
							    }
							}else if("4".equals(x5305bo.getFlag())){
							    if(coreProductForm != null){
			                        if(StringUtil.isNotBlank(coreProductForm.getMediaObjectForm()) && coreProductForm.getMediaObjectForm().equals("T")){
			                            listX5305VO.add(x5305VO);
			                        }
			                    }
							}else{
							    listX5305VO.add(x5305VO);
							}
						}
				    }
					page.setRows(listX5305VO);
					page.setTotalCount(listX5305VO.size());
				}
			}
			
			page.setRows(listX5305VO);
			page.setTotalCount(listX5305VO.size());
		}else {
			if (BSS_OP_01_0017.equals(eventNo) && StringUtil.isNotBlank(customerNo)){
				CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
				coreProductFormSqlBuilder.andMediaHolderNoEqualTo(customerNo);
				coreProductFormSqlBuilder.andMainSupplyIndicatorEqualTo("2");
				List<CoreProductForm> coreProductFormList = coreProductFormDao.selectListBySqlBuilder(coreProductFormSqlBuilder);
				if (null != coreProductFormList && !coreProductFormList.isEmpty()) {
					CoreProductForm coreProductForm = coreProductFormList.get(0);
					String productHolderNo = coreProductForm.getProductHolderNo();
					List<X5305VO> listX5305VO = new ArrayList<X5305VO>();
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder1 = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilder1.andMainCustomerNoEqualTo(productHolderNo);
					coreMediaBasicInfoSqlBuilder1.andMainSupplyIndicatorEqualTo("2");
					List<CoreMediaBasicInfo> listCoreMediaBasicInfos = coreMediaBasicInfoDaoImpl
							.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder1);
					if (null != listCoreMediaBasicInfos && !listCoreMediaBasicInfos.isEmpty()) {
						for (CoreMediaBasicInfo coreMediaBasicInfo : listCoreMediaBasicInfos) {
							X5305VO x5305VO = new X5305VO();
							x5305VO.setOperatorId(operatorId);
							// CoreMediaObjectSqlBuilder coreMediaObjectSqlBuilder = new
							// CoreMediaObjectSqlBuilder();
							// coreMediaObjectSqlBuilder.andMediaObjectCodeEqualTo(coreMediaBasicInfo.getMediaObjectCode());
							// CoreMediaObject coreMediaObject =
							// coreMediaObjectDaoImpl.selectBySqlBuilder(coreMediaObjectSqlBuilder);
							CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(
									coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getMediaObjectCode());
							CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
							coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
							CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl
									.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
							// CoreProductObjectSqlBuilder coreProductObjectSqlBuilder = new
							// CoreProductObjectSqlBuilder();
							// coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(coreMediaBasicInfo.getProductObjectCode());
							// CoreProductObject coreProductObject =
							// coreProductObjectDaoImpl
							// .selectBySqlBuilder(coreProductObjectSqlBuilder);
							
							CoreCorporationEntity coreCorporationEntity = cardUtil
									.getSystemUnitNoCoreCorporationEntity(coreMediaBasicInfo.getInstitutionId());
							
							CoreProductObject coreProductObject = httpQueryService.queryProductObject(
									coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
							x5305VO.setProductDesc(coreProductObject.getProductDesc());
							CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(coreProductObject.getBinNo().toString(),coreCorporationEntity,operatorId,null);
							CoreProductFormSqlBuilder coreProductFormSqlBuilder1 = new CoreProductFormSqlBuilder();
							coreProductFormSqlBuilder1.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
							CoreProductForm coreProductForm1 = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder1);
							if (coreProductForm1 != null) {
								CachedBeanCopy.copyProperties(coreProductForm1,x5305VO);
								if ("1".equals(coreProductForm1.getMainSupplyIndicator())) {
									x5305VO.setMediaHolderNo("");
								}
							}
							if (coreMediaObject != null) {
								CachedBeanCopy.copyProperties(coreMediaObject,x5305VO);
							}
							if (coreMediaBasicInfo != null) {
								CachedBeanCopy.copyProperties(coreMediaBasicInfo,x5305VO);
							}
							if (coreMediaCardInfo != null) {
								CachedBeanCopy.copyProperties(coreMediaCardInfo,x5305VO);
							}
							if (coreIssueCardBin != null) {
								CachedBeanCopy.copyProperties(coreIssueCardBin,x5305VO);
							}
							if("3".equals(x5305bo.getFlag())){//DPAN信息:查询为3的标识，筛选有效的，媒介形式为R,V的
							    if(coreProductForm != null){
							    	if (StringUtil.isNotBlank(coreProductForm.getMediaObjectForm())) {
							    		if(!coreProductForm.getMediaObjectForm().equals("T")){
								            CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreMediaBasicInfo.getInstitutionId());
								            x5305VO.setOrganName(coreOrgan.getOrganName());
								            listX5305VO.add(x5305VO);
								        }
									}else {
										listX5305VO.add(x5305VO);
									}
							    }
							}else if("4".equals(x5305bo.getFlag())){
							    if(coreProductForm != null){
			                        if(StringUtil.isNotBlank(coreProductForm.getMediaObjectForm()) && coreProductForm.getMediaObjectForm().equals("T")){
			                            listX5305VO.add(x5305VO);
			                        }
			                    }
							}else{
							    listX5305VO.add(x5305VO);
							}
						}
				    }
					page.setRows(listX5305VO);
					page.setTotalCount(listX5305VO.size());
				}
			}
		}

		return page;
	}

}
