package com.tansun.ider.bus.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5295Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreMediaCardInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerAddrDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5295BO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.BetaCommonParamServiceImpl;
import com.tansun.ider.util.CSVUtils;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardMakingUtil;

/**
 * @version:1.0
 * @Description: 实时制卡处理
 * @author: admin
 */
@Service
public class X5295BusImpl implements X5295Bus {

	static final String system = "system";
	// 算法标识，国密，国际加密
	static final String algorithmType = "1";

	@Resource
	private CoreMediaCardInfoDao coreMediaCardInfoDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Value("${webapp.service.card.make.path}")
	private String outPutPath;
	@Value("${webapp.service.card.make.fileName}")
	private String fileName;
	@Autowired
	private CoreCustomerAddrDaoImpl coreCustomerAddrDaoImpl;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private BetaCommonParamServiceImpl betaCommonParamServiceImpl;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Value("${global.target.service.url.auth}")
	private String authUrl;

	@SuppressWarnings("unchecked")
	@Override
	public Object busExecute(X5295BO x5295bo) throws Exception {
		// 公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5295bo, eventCommAreaNonFinance);
		// 媒介单元代码
		String mediaUnitCode = x5295bo.getMediaUnitCode();
		String operatorId = x5295bo.getOperatorId();
		//生成制卡文件名称
		String fileName3 = x5295bo.getFileName();
		CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
		coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDao.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
		if (null == coreMediaCardInfo) {
			if (!system.equals(operatorId)) {
				throw new BusinessException("CUS-00014", "媒介制卡");
			} else {
				// 批量程序单独处理，不抛出异常
				x5295bo.setMakeFlag(Constant.NOTHANDLE);
				return x5295bo;
			}
		}
		CoreProductObject coreProductObject = null;
		String operationMode = "";
		String mediaObjectCode = "";
		String externalIdentificationNo = "";
		String expirationDate = "";
		String mediaUserName = "";
		String mainCustomerNo = "";
		String mainSupplyIndicator = "";
		// 媒介单元信息表
		if (!coreMediaCardInfo.getProductionCode().equals("9")) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			// 产品对象信息表
			coreProductObject = httpQueryService.queryProductObject(coreMediaBasicInfo.getOperationMode(),
					coreMediaBasicInfo.getProductObjectCode());
			operationMode = coreMediaBasicInfo.getOperationMode();
			mediaObjectCode = coreMediaBasicInfo.getMediaObjectCode();
			externalIdentificationNo = coreMediaBasicInfo.getExternalIdentificationNo();
			expirationDate = coreMediaBasicInfo.getExpirationDate();
			mediaUserName = coreMediaBasicInfo.getMediaUserName();
			mainCustomerNo = coreMediaBasicInfo.getMainCustomerNo();
			mainSupplyIndicator = coreMediaBasicInfo.getMainSupplyIndicator();
		}else {
//			Map<String, String> paramMap = new HashMap<>();
//			paramMap.put("externalIdentificationNo", coreMediaCardInfo.getMediaUnitCode());
//			CorePrefabricatedCard corePrefabricatedCard = betaCommonParamServiceImpl.queryCorePrefabricatedCard(paramMap);
//			String corporationEntityNo = corePrefabricatedCard.getCorporationEntityNo();
//			CoreCorporationEntity coreCorporationEntity = httpQueryService.queryCoreCorporationEntity(corporationEntityNo);
//			coreProductObject = httpQueryService.queryProductObject(coreCorporationEntity.getOperationMode(),
//					corePrefabricatedCard.getProductObjectCode());
//			operationMode = coreCorporationEntity.getOperationMode();
//			mediaObjectCode = corePrefabricatedCard.getMediaObjectCode();
//			externalIdentificationNo = corePrefabricatedCard.getExternalIdentificationNo();
//			expirationDate = corePrefabricatedCard.getExpirationDate();
//			mediaUserName = "";
//			mainCustomerNo = "";
//			mainSupplyIndicator = "";
//			eventCommAreaNonFinance.setMediaUnitCode(externalIdentificationNo);
		}
		// binNo
		Integer binNo = coreProductObject.getBinNo();
		// 查询卡组织
		CoreCorporationEntity coreCorporationEntity = new CoreCorporationEntity();
		CoreIssueCardBin coreIssueCardBin = httpQueryService.queryCardBinNo(binNo + "", coreCorporationEntity,
				operatorId,null);
		CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(operationMode,
				mediaObjectCode);
		// 生成三磁信息
		Map<String, String> paramsMap = new HashMap<String, String>(10);
		paramsMap.put("externalIdentificationNo", externalIdentificationNo);
		paramsMap.put("expirationDate", expirationDate.substring(2, 4)+expirationDate.substring(0, 2));
		paramsMap.put("serviceCode", coreMediaObject.getServiceCode());
		paramsMap.put("operationMode", operationMode);
		paramsMap.put("algorithmType", algorithmType);
		paramsMap.put("mediaObjectType", coreMediaObject.getMediaObjectType());
		paramsMap.put("authDataSynFlag", "1");
		CoreMediaCardInfo coreMediaCardInfo1 = betaCommonParamService.queryCoreMediaCardInfo(paramsMap);
		if (null != coreMediaCardInfo1) {
			eventCommAreaNonFinance.setCvv(coreMediaCardInfo1.getCvv());
			eventCommAreaNonFinance.setCvv2(coreMediaCardInfo1.getCvv2());
			eventCommAreaNonFinance.setIcvv(coreMediaCardInfo1.getIcvv());
		}
		Map<String, Object> map = new HashMap<>();
		if (null != coreMediaCardInfo1) {
			// 生成三磁信息 一磁信息 magneticChannel1 二磁信息 magneticChannel2 三磁信息
			 map = CardMakingUtil.getTrackInfo(coreMediaCardInfo, externalIdentificationNo,expirationDate, coreIssueCardBin,coreMediaCardInfo1.getCvv(),coreMediaObject);
		}else {
			// 生成三磁信息 一磁信息 magneticChannel1 二磁信息 magneticChannel2 三磁信息
			 map = CardMakingUtil.getTrackInfo(coreMediaCardInfo, externalIdentificationNo,expirationDate, coreIssueCardBin,"999",coreMediaObject);
		}

		// 生成csv文件报文内容
		@SuppressWarnings("rawtypes")
		LinkedHashMap linkHashMap = new LinkedHashMap();
		linkHashMap.put("1", "外部识别号");
		linkHashMap.put("2", "有效期");
		linkHashMap.put("3", "卡组织");
		linkHashMap.put("4", "一磁信息");
		linkHashMap.put("5", "二磁信息");
		linkHashMap.put("6", "三磁信息");
		linkHashMap.put("7", "卡号");
		linkHashMap.put("8", "卡版代号");
		linkHashMap.put("9", "持卡人姓名");
		linkHashMap.put("10", "联系地址");
		linkHashMap.put("11", "邮编");
		linkHashMap.put("12", "联系电话");
		linkHashMap.put("13", "卡介质");
		linkHashMap.put("14", "预留Buffer域");

		@SuppressWarnings("rawtypes")
		List exportData = new ArrayList<Map>();
		@SuppressWarnings("rawtypes")
		Map row1 = new LinkedHashMap<String, String>();
		row1.put("1", externalIdentificationNo);
		row1.put("2", expirationDate);
		row1.put("3", coreIssueCardBin.getCardScheme().substring(0, 1));
		row1.put("4", map.get("magneticChannel1"));
		row1.put("5", map.get("magneticChannel2"));
		row1.put("6", map.get("magneticChannel3"));
		row1.put("7", externalIdentificationNo);
		row1.put("8", coreMediaCardInfo.getFormatCode()==null?"":coreMediaCardInfo.getFormatCode());
		row1.put("9", mediaUserName);

		CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
		coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(mainCustomerNo);
		List<CoreCustomerAddr> coreCustomerAddrList = coreCustomerAddrDaoImpl
				.selectListBySqlBuilder(coreCustomerAddrSqlBuilder);
		// 联系地址
		if (null != coreCustomerAddrList && !coreCustomerAddrList.isEmpty()) {
			CoreCustomerAddr coreCustomerAddr = null;
			for (CoreCustomerAddr coreCustomerAddr1 : coreCustomerAddrList) {
				int getType = coreCustomerAddr1.getType();
				if (1 == getType) {
					coreCustomerAddr = coreCustomerAddr1;
				}
			}
			if (null == coreCustomerAddr) {
				coreCustomerAddr = coreCustomerAddrList.get(0);
			}
			row1.put("10", coreCustomerAddr.getContactAddress());
			row1.put("11", coreCustomerAddr.getContactPostCode());
			row1.put("12", coreCustomerAddr.getContactMobilePhone());
		}else {
			row1.put("10", "");
			row1.put("11", "");
			row1.put("12", "");
		}
		// 媒介对象表中的媒介类型
		row1.put("13", coreMediaObject.getMediaObjectType());
		row1.put("14", externalIdentificationNo);
		exportData.add(row1);
		String dateStr = DateUtil.format(new Date(), "yyyy-MM-dd");
		String fileName1 = "";
		if (StringUtil.isBlank(fileName3)) {
			// 生成制卡文件信息
			fileName1 = fileName+"_"+dateStr+"_001";
			//调整文件名称
//			fileName1 = checkUpFileName(fileName1, outPutPath);
		}else {
			fileName1 = fileName3;
		}
		CSVUtils.createCSVFile(exportData, linkHashMap, outPutPath, fileName1);
		eventCommAreaNonFinance.setExternalIdentificationNo(externalIdentificationNo);
		eventCommAreaNonFinance.setExpirationDate(expirationDate);
		eventCommAreaNonFinance.setCardAssociations(coreIssueCardBin.getCardScheme());
		eventCommAreaNonFinance.setMagneticChannel1(map.get("magneticChannel1").toString());
		eventCommAreaNonFinance.setMagneticChannel2(map.get("magneticChannel2").toString());
		eventCommAreaNonFinance.setMagneticChannel3(map.get("magneticChannel3").toString());
		eventCommAreaNonFinance.setCardholderName(coreMediaCardInfo.getFormatCode());
		eventCommAreaNonFinance.setMediaUserName(mediaUserName);
		eventCommAreaNonFinance.setMainSupplyIndicator(mainSupplyIndicator);
		eventCommAreaNonFinance.setMediaObjectType(coreMediaObject.getMediaObjectType());
		eventCommAreaNonFinance.setFormatCode(coreMediaCardInfo.getFormatCode());
		
		return eventCommAreaNonFinance;
	}
	
	/**
	 * 检查当前目录 是否成功文件，如果没有成功文件名称+1
	 * @param fileName1
	 * @param outPutPath
	 * @return
	 */
	public String checkUpFileName(String fileName,String outPutPath) {
		File csvFile = new File(outPutPath + fileName + ".csv");
		boolean flag = csvFile.exists();
		while (flag) {
			String[] str = fileName.split("_");
			Integer str1 = Integer.valueOf(str[2])+1;
			String str2 = "";
			if (str1<10) {
				str2 = "00"+str1.toString();
			}else if(str1<100){
				str2 = "0"+str1.toString();
			}else {
				str2 = str1.toString();
			}
			fileName = str[0]+"_"+str[1];
			fileName = fileName+"_"+str2.toString();
			File csvFile1 = new File(outPutPath + fileName + ".csv");
			flag = csvFile1.exists();
		}
		return fileName;
	}
	
}
