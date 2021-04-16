package com.tansun.ider.bus.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5290Bus;
import com.tansun.ider.dao.issue.entity.CoreCardMakeRecord;
import com.tansun.ider.dao.issue.impl.CoreCardMakeRecordDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCardMakeRecordSqlBuilder;
import com.tansun.ider.model.bo.X5290BO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

@Service
public class X5290BusImpl implements X5290Bus {
	@Autowired
	private CoreCardMakeRecordDaoImpl coreCardMakeRecordDaoImpl;

	@Override
	public Object busX5290(X5290BO x5290Bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		
		// 将参数传递给事件公共区
		// // 将参数传递给事件公共区
		/*
		 * CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new
		 * CoreMediaBasicInfoSqlBuilder();
		 * coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		 * CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
		 * .selectBySqlBuilder(coreMediaBasicInfoSqlBuilder); if (null ==
		 * coreMediaBasicInfo) { throw new BusinessException("CUS-00014",
		 * "媒介单元基本信息");// 媒介单元基本信息 } // 查询产品对象表 CoreProductObjectSqlBuilder
		 * coreProductObjectSqlBuilder = new CoreProductObjectSqlBuilder();
		 * coreProductObjectSqlBuilder.andProductObjectCodeEqualTo(
		 * coreMediaBasicInfo.getProductObjectCode()); CoreProductObject
		 * coreProductObject = coreProductObjectDaoImpl.selectBySqlBuilder(
		 * coreProductObjectSqlBuilder); if (null == coreProductObject) { throw
		 * new BusinessException("CUS-00014", "产品对象表"); // 产品对象表 } // 发行卡bin
		 * CoreIssueCardBinSqlBuilder coreIssueCardBinSqlBuilder = new
		 * CoreIssueCardBinSqlBuilder();
		 * coreIssueCardBinSqlBuilder.andBinNoEqualTo(coreProductObject.getBinNo
		 * ()); CoreIssueCardBin coreIssueCardBin =
		 * coreIssueCardBinDaoImpl.selectBySqlBuilder(coreIssueCardBinSqlBuilder
		 * ); if (null == coreIssueCardBin) { throw new
		 * BusinessException("CUS-00014", "发行卡bin");// 发行卡bin } // 媒介制卡信息
		 * CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new
		 * CoreMediaCardInfoSqlBuilder();
		 * coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		 * CoreMediaCardInfo coreMediaCardInfo =
		 * coreMediaCardInfoDaoImpl.selectBySqlBuilder(
		 * coreMediaCardInfoSqlBuilder); if (null == coreMediaCardInfo) { throw
		 * new BusinessException("CUS-00014", "媒介制卡信息");// 媒介制卡信息 } // 获取三磁信息
		 * Map<String, Object> map =
		 * CardMakingUtil.getTrackInfo(coreMediaCardInfo, coreMediaBasicInfo,
		 * coreIssueCardBin);
		 */

		// 将生成的制卡数据放到制卡文件中
		CoreCardMakeRecord coreCardMakeRecord = new CoreCardMakeRecord();
		coreCardMakeRecord.setExternalIdentificationNo(x5290Bo.getExternalIdentificationNo());
		coreCardMakeRecord.setExpirationDate(x5290Bo.getExpirationDate()); // 修改类为字符串类型
		coreCardMakeRecord.setCardAssociations(x5290Bo.getCardAssociations());
		coreCardMakeRecord.setMagneticChannel1(x5290Bo.getMagneticChannel1());
		coreCardMakeRecord.setMagneticChannel2(x5290Bo.getMagneticChannel2());
		coreCardMakeRecord.setMagneticChannel3(x5290Bo.getMagneticChannel3());
		coreCardMakeRecord.setFormatCode(x5290Bo.getFormatCode());
		coreCardMakeRecord.setCardholderName(x5290Bo.getMediaUserName());
		coreCardMakeRecord.setMainSupplyIndicator(x5290Bo.getMainSupplyIndicator());
		coreCardMakeRecord.setId(RandomUtil.getUUID());
		coreCardMakeRecord.setVersion(1);
		CoreCardMakeRecordSqlBuilder coreCardMakeRecordSqlBuilder = new CoreCardMakeRecordSqlBuilder();
		coreCardMakeRecordSqlBuilder.andExternalIdentificationNoEqualTo(eventCommAreaNonFinance.getExternalIdentificationNo());
		List<CoreCardMakeRecord> listCoreCardMakeRecords = coreCardMakeRecordDaoImpl
				.selectListBySqlBuilder(coreCardMakeRecordSqlBuilder);
		CoreCardMakeRecord coreCardMakeRecordStr = null;
		if (listCoreCardMakeRecords.size() > 0) {
			Collections.sort(listCoreCardMakeRecords, new Comparator<CoreCardMakeRecord>() {
				@Override
				public int compare(CoreCardMakeRecord o1, CoreCardMakeRecord o2) {
					return o1.getRecordNumber() - o2.getRecordNumber();
				}
			});
			coreCardMakeRecordStr = listCoreCardMakeRecords.get(listCoreCardMakeRecords.size() - 1);
		}
		if (coreCardMakeRecordStr == null) {
			coreCardMakeRecord.setRecordNumber(1);
		} else {
			coreCardMakeRecord.setRecordNumber(coreCardMakeRecordStr.getRecordNumber() + 1);
		}
		@SuppressWarnings("unused")
		int result = coreCardMakeRecordDaoImpl.insert(coreCardMakeRecord);

		return x5290Bo;
	}
}
