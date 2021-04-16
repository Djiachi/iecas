package com.tansun.ider.bus.impl;

import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5075Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.issue.CoreMediaLifeCycleInfoDao;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLifeCycleInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLifeCycleInfoSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.model.bo.X5075BO;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.CardUtils;

/**
 * @version:1.0
 * @Description: 转卡媒介资料查询
 * @author: admin
 */
@Service
public class X5075BusImpl implements X5075Bus {

	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private CoreMediaLifeCycleInfoDao coreMediaLifeCycleInfoDao;
	@Autowired
	private QueryCustomerService queryCustomerService;

	@Override
	public Object busExecute(X5075BO x5075bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5075bo, eventCommAreaNonFinance);
		List<CoreActivityArtifactRel> artifactList = x5075bo.getActivityArtifactList();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		List<CoreMediaBasicInfo> listCoreMediaBasicInfo = queryCustomerService
				.queryCoreMediaBasicInfoList(externalIdentificationNo, "Y");
		for (CoreMediaBasicInfo coreMediaBasicInfo2 : listCoreMediaBasicInfo) {
			CachedBeanCopy.copyProperties(coreMediaBasicInfo2, eventCommAreaNonFinance);
			EventCommArea eventCommArea = new EventCommArea();
			eventCommArea.setEcommMediaObjId(coreMediaBasicInfo2.getMediaObjectCode());
			eventCommAreaNonFinance.setEventCommArea(eventCommArea);
			String transferCard = CardUtils.getTransferCard(eventCommAreaNonFinance.getEventCommArea(), artifactList,
					coreMediaBasicInfo2);
			x5075bo.setIsTransferCard(transferCard);
			CachedBeanCopy.copyProperties(coreMediaBasicInfo2, x5075bo);
			// 查询封锁码
			CoreMediaBasicInfo coreMediaBasicInfoNew = null;
			// 查询媒介资料
			if (coreMediaBasicInfo2.getInvalidFlag().equals("N") && coreMediaBasicInfo2.getInvalidReason().equals(InvalidReasonStatus.TRF.getValue())) {
				CoreMediaLifeCycleInfoSqlBuilder coreMediaLifeCycleInfoSqlBuilder = new CoreMediaLifeCycleInfoSqlBuilder();
				coreMediaLifeCycleInfoSqlBuilder.andTransferMediaCodeEqualTo(coreMediaBasicInfo2.getMediaUnitCode());
				CoreMediaLifeCycleInfo coreMediaLifeCycleInfo = coreMediaLifeCycleInfoDao
						.selectBySqlBuilder(coreMediaLifeCycleInfoSqlBuilder);
				// 查询新媒介资料
				CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderNew = new CoreMediaBasicInfoSqlBuilder();
				coreMediaBasicInfoSqlBuilderNew.andMediaUnitCodeEqualTo(coreMediaLifeCycleInfo.getMediaUnitCode());
				coreMediaBasicInfoNew = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderNew);
				x5075bo.setExternalIdentificationNoNew(coreMediaBasicInfoNew.getExternalIdentificationNo());
				x5075bo.setMediaObjectCodeNew(coreMediaBasicInfoNew.getMediaObjectCode());
				x5075bo.setProductObjectCodeNew(coreMediaBasicInfoNew.getProductObjectCode());
				x5075bo.setMainCustomerCodeNew(coreMediaBasicInfoNew.getMainCustomerNo());
				x5075bo.setMediaUserNameNew(coreMediaBasicInfoNew.getMediaUserName());
				x5075bo.setTermValidityNew(coreMediaBasicInfoNew.getExpirationDate());
			}
		}
		
		return x5075bo;
	}

}
