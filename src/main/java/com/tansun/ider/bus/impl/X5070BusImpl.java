package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5070Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5070BO;
import com.tansun.ider.model.vo.X5070VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CardUtils;
import com.tansun.ider.util.MapTransformUtils;

/**
 * @version:1.0
 * @Description: 转卡列表查询
 * @author: admin
 */
@Service
public class X5070BusImpl implements X5070Bus {

	private static Logger logger = LoggerFactory.getLogger(X5070BusImpl.class);
	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Autowired
    private QueryCustomerService queryCustomerService;
	@Autowired
	private HttpQueryServiceImpl httpQueryServiceImpl;
	@Override
	public Object busExecute(X5070BO x5070bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		//  将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5070bo, eventCommAreaNonFinance);
		List<CoreActivityArtifactRel> artifactList = x5070bo.getActivityArtifactList();
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		
		String idType = eventCommAreaNonFinance.getIdType();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		PageBean<X5070VO> page = new PageBean<>();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		CoreCustomer coreCustomer = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			 coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
		} else if (object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
		}
//        else if(object instanceof CoreMediaBasicInfo){
//			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
//			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
//			
//		}
        
//		if (StringUtil.isNotEmpty(idNumber)&&StringUtil.isNotEmpty(idType)) {
//			coreCustomer = queryCustomerService.queryCoreCustomer(idType, idNumber);
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null != coreCustomer) {
//				coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
//			}else {
//				throw new BusinessException("CUS-00014", "客户基本信息");
//			}
//		}
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		if (StringUtil.isBlank(x5070bo.getFlag())) {
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		}

		int totalCount = coreMediaBasicInfoDaoImpl.countBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5070bo.getPageSize() && null != x5070bo.getIndexNo()) {
			coreMediaBasicInfoSqlBuilder.setPageSize(x5070bo.getPageSize());
			coreMediaBasicInfoSqlBuilder.setIndexNo(x5070bo.getIndexNo());
			page.setPageSize(x5070bo.getPageSize());
			page.setIndexNo(x5070bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreMediaBasicInfo> list = coreMediaBasicInfoDaoImpl
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			List<X5070VO> listVo = new ArrayList<X5070VO>();
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : list) {
				X5070VO x5070VO = new X5070VO();
				Map<String, Object> map = MapTransformUtils.objectToMap(coreMediaBasicInfo2);
				ReflexUtil.setFieldsValues(x5070VO, map);
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommMediaObjId(coreMediaBasicInfo2.getMediaObjectCode());
				eventCommAreaNonFinance.setEventCommArea(eventCommArea);
				String transferCard = CardUtils.getTransferCard(eventCommAreaNonFinance.getEventCommArea(), artifactList,
						coreMediaBasicInfo2);
				if (coreMediaBasicInfo2.getInvalidFlag().equals("N")) {
					transferCard ="N";
				}
				x5070VO.setTransferCard(transferCard);
				x5070VO.setInvalidFlag(coreMediaBasicInfo2.getInvalidFlag());
				x5070VO.setInvalidReason(coreMediaBasicInfo2.getInvalidReason());
				//当传入外部识别号的时候，当coreCustomer为空时，会有直接取空值，从而报空指针的情况出现，因此加上非空判断	add by wangxi 2019/6/11
				if(null != coreCustomer){
					x5070VO.setIdType(coreCustomer.getIdType());
					x5070VO.setIdNumber(coreCustomer.getIdNumber());
				}
				if (StringUtil.isNotBlank(coreMediaBasicInfo2.getOperationMode()) && StringUtil.isNotBlank(x5070VO.getProductObjectCode())) {
					CoreProductObject coreProductObject = httpQueryServiceImpl.queryProductObject(coreMediaBasicInfo2.getOperationMode(), x5070VO.getProductObjectCode());
					x5070VO.setProductDesc(coreProductObject.getProductDesc());
				}
				if (StringUtil.isNotBlank(coreMediaBasicInfo2.getOperationMode()) && StringUtil.isNotBlank(x5070VO.getMediaObjectCode())) {
					CoreMediaObject coreMediaObject = httpQueryServiceImpl.queryMediaObject(coreMediaBasicInfo2.getOperationMode(), x5070VO.getMediaObjectCode());
					x5070VO.setMediaObjectDesc(coreMediaObject.getMediaObjectDesc());
				}				
				listVo.add(x5070VO);
			}
			page.setRows(listVo);
		}
		return page;
	}

	/**
	 * 
	 * @Description: 查询是否可以转卡
	 * @param x5070bo
	 * @param coreMediaBasicInfo2
	 * @return
	 * @throws Exception
	 */
	// 根据媒介对象号调用通用程序“活动获取元件”，取得当前媒介生效元件
	// 若元件编号为“302AAA0100”，则将“媒介类型是否可转卡”赋值为Y，控制挂失可转卡，转卡媒介查询“是否转卡”不允许选“是”，选择“是”报错；
	// 若元件编号为“302AAA0101”，则将“媒介类型是否可转卡”赋值为N ，控制挂失不可转卡，转卡媒介查询“是否转卡”可选“是”。
	@SuppressWarnings("unused")
	private String getTransferCard(EventCommAreaNonFinance eventCommAreaNonFinance,
			List<CoreActivityArtifactRel> artifactList, CoreMediaBasicInfo coreMediaBasicInfo2) throws Exception {
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		EventCommArea eventCommArea = new EventCommArea();
		// 运营模式
		eventCommArea.setEcommOperMode(coreMediaBasicInfo2.getOperationMode());
		eventCommArea.setEcommMediaObjId(coreMediaBasicInfo2.getMediaObjectCode());
		String transferCard = "";
		for (CoreActivityArtifactRel dto : artifactList) {
			// 6.1 通过构件获取元件，公共程序处理CoreArtifactKey
			Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
			// 6.2 应对POC演示选取必要元件
			Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (Constant.TRANSFER_CARD_FLAG.equals(entry.getKey())) {
					// 是否可转卡标志
					transferCard = "Y";
				} else if (Constant.TRANSFER_CARD_TYPE.equals(entry.getKey())) {
					// 是否可转卡标志
					transferCard = "N";
				}
				if (logger.isDebugEnabled()) {
					logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
				}
			}
		}

		return transferCard;
	}

}
