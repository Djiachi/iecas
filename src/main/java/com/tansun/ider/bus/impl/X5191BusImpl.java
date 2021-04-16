package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5191Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectiveCdeCntrlProg;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CorePriceTag;
import com.tansun.ider.dao.issue.CoreCustomerBusinessTypeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBusinessTypeSqlBuilder;
import com.tansun.ider.enums.ProjectType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5191BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;

/**
 * 客户定价标签设置定价标签设置
 * 
 * @author wt
 *
 */
@Service
public class X5191BusImpl implements X5191Bus {

	private static String projectType = "1";

	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerBusinessTypeDao coreCustomerBusinessTypeDao;

	@Value("${global.target.service.nofn.SP800050}")
    private String spEventNo80;
	@Value("${global.target.service.nofn.SP810050}")
    private String spEventNo81;
	@Value("${global.target.service.nofn.SP860050}")
    private String spEventNo86;
	@Value("${global.target.service.nofn.SP870050}")
    private String spEventNo87;
	
	@Override
	public Object busExecute(X5191BO x5191BO) throws Exception {
		// 客户号 必输
		String customerNo = x5191BO.getCustomerNo();
		// 运营模式 必输
		String operationMode = x5191BO.getOperationMode();
		// 类别 必输
		String effectivenessCodeType = x5191BO.getEffectivenessCodeType();
		// 生效码层级
		String effectivenessCodeScene = x5191BO.getEffectivenessCodeScene();
		// 场景触发对象  必输
		String sceneTriggerObject = x5191BO.getSceneTriggerObject();
		// 生效码 层级
		String effectivenessCodeScope = x5191BO.getEffectivenessCodeScope();
		// 场景触发对象层级代码 必输
		String levelCode = x5191BO.getLevelCode();
		// 层级代码  
		String sceneTriggerLevelCode = x5191BO.getSceneTriggerLevelCode();
		// 封锁码标识 新增:A 删除 :D
		String blockFlag = x5191BO.getBlockFlag();
		//运营日期
		String operationDate = x5191BO.getOperationDate();
		// 操作员Id
		String operatorId = x5191BO.getOperatorId();
		// 事件编号 必输
		String spEventNo = x5191BO.getEventNo();
		//延滞新增管控封锁码
		ArrayList<String> levelCodeList = x5191BO.getLevelCodeList();
		// 查询封锁码表
		CoreEffectivenessCode coreEffectivenessCode = null;
		if (StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(effectivenessCodeType) &&StringUtil.isNotBlank(effectivenessCodeScene)) {
			coreEffectivenessCode = httpQueryService.queryEffectivenessCode(operationMode,
					effectivenessCodeType, effectivenessCodeScene);
		}
		
		if (null != coreEffectivenessCode) {
			effectivenessCodeScope = coreEffectivenessCode.getEffectivenessCodeScope();
		}
		//处理客户定价标签
		if (StringUtil.isNotBlank(blockFlag) && Constant.ADD.equals(blockFlag)) {
			List<CoreEffectiveCdeCntrlProg> coreEffectiveCdeCntrlProgList = httpQueryService
					.queryCoreEffectivenessCodeRelateControlItemList(operationMode, effectivenessCodeType,
							effectivenessCodeScene,ProjectType.PRICING_LABEL.getValue());
			if (null == coreEffectiveCdeCntrlProgList
					|| coreEffectiveCdeCntrlProgList.isEmpty()) {
				// 运营模式,类别,场景查询封锁码关联管控项目表不存在,则跳出,不更新
				return x5191BO;
			}
			
			if (!spEventNo80.equals(spEventNo)&&! spEventNo81.equals(spEventNo)&&!spEventNo86.equals(spEventNo)&&!spEventNo87.equals(spEventNo)) {
				// 生效码
				for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
					if (projectType.equals(coreEffectiveCdeCntrlProg.getProjectType().toString())) {
						// 查询的当前定价标签是否设置
						CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
						coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
						coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						coreCustomerBusinessTypeSqlBuilder.andPricingObjectEqualTo(sceneTriggerObject);
						coreCustomerBusinessTypeSqlBuilder.andPricingObjectCodeEqualTo(sceneTriggerLevelCode);
						CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao
								.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
						if (null == coreCustomerBusinessType) {
							String controlProjectNo = coreEffectiveCdeCntrlProg.getControlProjectNo();
							CorePriceTag corePriceTag = httpQueryService.queryPriceTag(operationMode, controlProjectNo, null);
							CoreCustomerBusinessType coreCustomerBusinessTypeAdd = new CoreCustomerBusinessType();
							coreCustomerBusinessTypeAdd.setId(RandomUtil.getUUID());
							coreCustomerBusinessTypeAdd.setCustomerNo(customerNo);
							coreCustomerBusinessTypeAdd.setPricingObject(corePriceTag.getPricingObject());
							coreCustomerBusinessTypeAdd.setPricingObjectCode(corePriceTag.getPricingObjectCode());
							coreCustomerBusinessTypeAdd.setPricingTag(controlProjectNo);
							coreCustomerBusinessTypeAdd.setPricingLevel(effectivenessCodeScope);
							coreCustomerBusinessTypeAdd.setPricingLevelCode(sceneTriggerLevelCode);
							coreCustomerBusinessTypeAdd.setGmtCreate(DateUtil.parse(DateUtil.offset(operationDate, 1)));
							coreCustomerBusinessTypeAdd.setSettingDate(DateUtil.offset(operationDate, 1));
							coreCustomerBusinessTypeAdd.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
							coreCustomerBusinessTypeAdd.setSettingUpUserid(operatorId);
							coreCustomerBusinessTypeAdd.setCustTagEffectiveDate(DateUtil.offset(operationDate, 1));
							coreCustomerBusinessTypeAdd.setCustTagExpirationDate("2099-12-31");
							coreCustomerBusinessTypeAdd.setState(YesOrNo.YES.getValue());
							coreCustomerBusinessTypeAdd.setVersion(1);
							coreCustomerBusinessTypeDao.insert(coreCustomerBusinessTypeAdd);
						}
					}
				}
			}else {
				// 生效码
				if (null != levelCodeList && !levelCodeList.isEmpty()) {
					for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
						if (projectType.equals(coreEffectiveCdeCntrlProg.getProjectType().toString())) {
								for (String sceneTriggerLevelCode1 : levelCodeList) {
									// 查询的当前定价标签是否设置
									CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
									coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
									coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
									coreCustomerBusinessTypeSqlBuilder.andPricingObjectEqualTo(sceneTriggerObject);
									coreCustomerBusinessTypeSqlBuilder.andPricingObjectCodeEqualTo(sceneTriggerLevelCode1);
									CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao
											.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
									if (null == coreCustomerBusinessType) {
										String controlProjectNo = coreEffectiveCdeCntrlProg.getControlProjectNo();
										CorePriceTag corePriceTag = httpQueryService.queryPriceTag(operationMode, controlProjectNo, null);
										CoreCustomerBusinessType coreCustomerBusinessTypeAdd = new CoreCustomerBusinessType();
										coreCustomerBusinessTypeAdd.setId(RandomUtil.getUUID());
										coreCustomerBusinessTypeAdd.setCustomerNo(customerNo);
										coreCustomerBusinessTypeAdd.setPricingObject(corePriceTag.getPricingObject());
										coreCustomerBusinessTypeAdd.setPricingObjectCode(corePriceTag.getPricingObjectCode());
										coreCustomerBusinessTypeAdd.setPricingTag(controlProjectNo);
										coreCustomerBusinessTypeAdd.setPricingLevel(effectivenessCodeScope);
										coreCustomerBusinessTypeAdd.setPricingLevelCode(sceneTriggerLevelCode1);
										coreCustomerBusinessTypeAdd.setGmtCreate(DateUtil.parse(DateUtil.offset(operationDate, 1)));
										coreCustomerBusinessTypeAdd.setSettingDate(DateUtil.offset(operationDate, 1));
										coreCustomerBusinessTypeAdd.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
										coreCustomerBusinessTypeAdd.setSettingUpUserid(operatorId);
										coreCustomerBusinessTypeAdd.setCustTagEffectiveDate(DateUtil.offset(operationDate, 1));
										coreCustomerBusinessTypeAdd.setCustTagExpirationDate("2099-12-31");
										coreCustomerBusinessTypeAdd.setState(YesOrNo.YES.getValue());
										coreCustomerBusinessTypeAdd.setVersion(1);
										coreCustomerBusinessTypeDao.insert(coreCustomerBusinessTypeAdd);
									}
								}
						}
					}
				}
			}
			
		}else if (StringUtil.isNotBlank(blockFlag) && Constant.DEL.equals(blockFlag)) {
			//生效码对应管控项目编号
			List<CoreEffectiveCdeCntrlProg> coreEffectiveCdeCntrlProgList = httpQueryService
					.queryCoreEffectivenessCodeRelateControlItemList(operationMode, effectivenessCodeType,
							effectivenessCodeScene,ProjectType.PRICING_LABEL.getValue());
			if (null == coreEffectiveCdeCntrlProgList
					|| coreEffectiveCdeCntrlProgList.isEmpty()) {
				// 运营模式,类别,场景查询封锁码关联管控项目表不存在,则跳出,不更新
				return x5191BO;
			}
			
			if (!spEventNo80.equals(spEventNo)&&! spEventNo81.equals(spEventNo)&&!spEventNo86.equals(spEventNo)&&!spEventNo87.equals(spEventNo)) {
				// 生效码
				for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
					if (projectType.equals(coreEffectiveCdeCntrlProg.getProjectType().toString())) {
						// 查询的当前定价标签是否设置
						CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
						coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
						coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						coreCustomerBusinessTypeSqlBuilder.andPricingLevelEqualTo(effectivenessCodeScope);
						coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(sceneTriggerLevelCode);
						CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao
								.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
						if (null != coreCustomerBusinessType) {
							coreCustomerBusinessType.setState("D");
							coreCustomerBusinessType.setRemovalUserid(operatorId);
							coreCustomerBusinessType.setRemoveDate(operationDate);
							coreCustomerBusinessType.setVersion(coreCustomerBusinessType.getVersion()+1);
							coreCustomerBusinessTypeDao.updateBySqlBuilderSelective(coreCustomerBusinessType,
									coreCustomerBusinessTypeSqlBuilder);
						}
					}
				}
			}else {
				if (null != levelCodeList && !levelCodeList.isEmpty()) {
					for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
						if (projectType.equals(coreEffectiveCdeCntrlProg.getProjectType().toString())) {
							for (String sceneTriggerLevelCode1 : levelCodeList) {
								// 查询的当前定价标签是否设置
								CoreCustomerBusinessTypeSqlBuilder coreCustomerBusinessTypeSqlBuilder = new CoreCustomerBusinessTypeSqlBuilder();
								coreCustomerBusinessTypeSqlBuilder.andCustomerNoEqualTo(customerNo);
								coreCustomerBusinessTypeSqlBuilder.andPricingTagEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
								coreCustomerBusinessTypeSqlBuilder.andPricingLevelEqualTo(effectivenessCodeScope);
								coreCustomerBusinessTypeSqlBuilder.andPricingLevelCodeEqualTo(sceneTriggerLevelCode1);
								CoreCustomerBusinessType coreCustomerBusinessType = coreCustomerBusinessTypeDao
										.selectBySqlBuilder(coreCustomerBusinessTypeSqlBuilder);
								if (null != coreCustomerBusinessType) {
									coreCustomerBusinessType.setState("D");
									coreCustomerBusinessType.setRemovalUserid(operatorId);
									coreCustomerBusinessType.setRemoveDate(operationDate);
									coreCustomerBusinessType.setVersion(coreCustomerBusinessType.getVersion()+1);
									coreCustomerBusinessTypeDao.updateBySqlBuilderSelective(coreCustomerBusinessType,
											coreCustomerBusinessTypeSqlBuilder);
								}
							}
						}
					}
				}
			}
			
		}else if (StringUtil.isNotBlank(blockFlag) && Constant.NOTHANDLE.equals(blockFlag)) {
			return x5191BO;
		} else {
			// 需要穿入操作内容,新增以及删除标识
			throw new BusinessException("CUS-00066");
		}
		return x5191BO;
	}
	
}
