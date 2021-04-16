package com.tansun.ider.bus.impl;

import javax.annotation.Resource;
import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5306Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaCardInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5306BO;
import com.tansun.ider.model.vo.X5306VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CardUtil;

/**
 * @Description: 媒介基本信息查询(根据媒介单元查询)
 * @author: wangxi
 */
@Service
public class X5306BusImpl implements X5306Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;//媒介单元基本信息
	@Resource
	private CoreCustomerDaoImpl coreCustomerDaoImpl;//客户基本信息
	@Resource
	private CoreMediaCardInfoDaoImpl coreMediaCardInfoDaoImpl;//媒介制卡信息
	@Autowired
	private CoreProductFormDao coreProductFormDao;//产品形式表
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CardUtil cardUtil;
	
	@Override
	public Object busExecute(X5306BO x5306bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5306bo, eventCommAreaNonFinance);
		//媒介单元代码-必输项
		String mediaUnitCode = x5306bo.getMediaUnitCode();
		//操作员Id
		String operatorId = x5306bo.getOperatorId();
		
		/**
		 * 0. 判断媒介单元代码是否有值
		 */
		if(StringUtil.isBlank(mediaUnitCode)){
			throw new BusinessException("CUS-00014", "媒介单元代码");
		}	
		
		/**
		 * 1. 根据媒介单元代码进行查询媒介单元基本信息
		 */
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		//得到媒介单元基本信息结果值
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		
		/**
		 * 2. 根据媒介单元代码进行查询媒介制卡信息
		 */
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		//得到媒介制卡信息结果值
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDaoImpl.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		
		/**
		 * 3. 根据产品形式代码查询产品形式表信息
		 */
		//产品形式代码
		String productForm = coreMediaBasicInfo.getProductForm();
		CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
		coreProductFormSqlBuilder.andProductFormEqualTo(productForm);
		//得到产品形式信息结果值
		CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
		
		/**
		 * 4. 根据运营模式+媒介对象代码查询媒介对象表
		 */
		//运营模式
		String operationMode = coreMediaBasicInfo.getOperationMode();
		//媒介对象代码
		String mediaObjectCode = coreMediaBasicInfo.getMediaObjectCode();
		//得到媒介对象信息
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(operationMode , mediaObjectCode);
		
		/**
		 * 5. 根据所属机构查询法人实体表
		 */
		//所属机构
		String institutionId = coreMediaBasicInfo.getInstitutionId();
		//得到法人实体信息
		CoreCorporationEntity coreCorporationEntity = cardUtil.getSystemUnitNoCoreCorporationEntity(institutionId);
		
		/**
		 * 6. 根据运营模式+产品对象代码查询产品对象表
		 */
		//产品对象代码
		String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
		//得到产品对象信息
		CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, productObjectCode);
		
		/**
		 * 7. 根据发行卡BIN+法人实体信息+操作员ID查询发行卡BIN表
		 */
		//发行卡BIN
		String binNo = coreProductObject.getBinNo().toString();
		//得到发行卡BIN信息
		CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(binNo,coreCorporationEntity,operatorId,null);
		
		/**
		 * 8. 将得到的结果返回给Vo
		 */
		X5306VO x5306VO = new X5306VO();
		x5306VO.setOperatorId(operatorId);
		x5306VO.setProductDesc(coreProductObject.getProductDesc());//产品描述
		
		if (coreProductForm != null) {
			CachedBeanCopy.copyProperties(coreProductForm, x5306VO);
			if ("1".equals(coreProductForm.getMainSupplyIndicator())) {
				x5306VO.setMediaHolderNo("");
			}
		}
		if (coreMediaObject != null) {
			CachedBeanCopy.copyProperties(coreMediaObject, x5306VO);
		}
		if (coreMediaBasicInfo != null) {
			CachedBeanCopy.copyProperties(coreMediaBasicInfo, x5306VO);
		}
		if (coreMediaCardInfo != null) {
			CachedBeanCopy.copyProperties(coreMediaCardInfo, x5306VO);
		}
		if (coreIssueCardBin != null) {
			CachedBeanCopy.copyProperties(coreIssueCardBin, x5306VO);
		}
		
		return x5306VO;

	}

}
