package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5180Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5180BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 客户封锁码删除
 * @author: wt
 */
@Service
public class X5180BusImpl implements X5180Bus {
	//管控来源
	static String contrlSource = "BLCK";
	private static final String system = "system";
	@Autowired
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Autowired
	private CoreCustomerDao CoreCustomerDao;
	@Autowired
	public OperationModeUtil operationModeUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreAccountDao coreAccountDao;
	
	@Value("${global.target.service.nofn.SP800050}")
    private String spEventNo80;
	@Value("${global.target.service.nofn.SP810050}")
    private String spEventNo81;
	@Value("${global.target.service.nofn.SP860050}")
    private String spEventNo86;
	@Value("${global.target.service.nofn.SP870050}")
    private String spEventNo87;
	
	@SuppressWarnings("null")
	@Override
	public Object busExecute(X5180BO x5180bo) throws Exception {
		//输入内容校验
		SpringUtil.getBean(ValidatorUtil.class).validate(x5180bo);
		// A. 通过客户号、封锁码类别、封锁码场景、管控层级（封锁码范围）从《客户封锁码信息》表中查询记录，如未找到则报错
		String customerNo = x5180bo.getCustomerNo();
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = CoreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		String corporation = coreCustomer.getCorporationEntityNo();
//		//运行模式           必输
		String operationMode = x5180bo.getOperationMode();
		// 事件编号         必输
		String spEventNo = x5180bo.getSpEventNo();
		//生效码类别
		String effectivenessCodeType = x5180bo.getEffectivenessCodeType();
		//生效码范围     
		String effectivenessCodeScene = x5180bo.getEffectivenessCodeScene();
		//场景触发对象         必输
		String sceneTriggerObject = x5180bo.getSceneTriggerObject();
		//层级代码        必输
		String levelCode = x5180bo.getLevelCode();
		//币种                非必输
		String currencyCode = x5180bo.getCurrencyCode();
		//操作员标识      必输
		String operatorId = x5180bo.getOperatorId(); 
		//业务项目代码
	    String businessProgramNo = x5180bo.getBusinessProgramNo();
		CoreEvent coreEvent = httpQueryService.queryEvent(spEventNo);
		String  evnetEffectivenessCodeType = "";
		if (null != coreEvent) {
			Integer eventEffectivenessCodeScene = coreEvent.getEffectivenessCodeScene();
			evnetEffectivenessCodeType  = coreEvent.getEffectivenessCodeType();
			String  evnetSceneTriggerObject = coreEvent.getSceneTriggerObject();
			String sceneTriggerObject_O = coreEvent.getSceneTriggerObject();
			if (StringUtil.isBlank(effectivenessCodeType)&&StringUtil.isNotBlank(sceneTriggerObject_O) && !"O".equals(sceneTriggerObject_O) ) {
				if (StringUtil.isBlank(evnetEffectivenessCodeType)) {
					throw new BusinessException("CUS-00109",spEventNo);
				}
				effectivenessCodeType = evnetEffectivenessCodeType;
			}
			if (StringUtil.isNotBlank(effectivenessCodeType)) {
				evnetEffectivenessCodeType = effectivenessCodeType;
			}
			if (StringUtil.isBlank(effectivenessCodeScene)) {
				if (null == eventEffectivenessCodeScene && StringUtil.isNotBlank(sceneTriggerObject_O) && !"O".equals(sceneTriggerObject_O)) {
					throw new BusinessException("CUS-00110",spEventNo);
				}
				if (null != eventEffectivenessCodeScene) {
					effectivenessCodeScene = eventEffectivenessCodeScene.toString();
				}
			}
			if (StringUtil.isBlank(sceneTriggerObject)) {
				sceneTriggerObject = evnetSceneTriggerObject;
			}
		} else {
			throw new BusinessException("CUS-00111", spEventNo);
		}
		CoreCustomerEffectiveCode coreCustomerEffectiveCode = null;
		CoreSystemUnit coreSystemUnit1 = operationModeUtil.getcoreOperationMode(customerNo);
		String operationDate = coreSystemUnit1.getNextProcessDate();
		
		//查询封锁码表,获取封锁码管控范围
		String effCodeScope = "";
		//对于延滞场景, 需要单独逻辑处理
		ArrayList<String> levelCodeList = new ArrayList<>();
		if (spEventNo80.equals(spEventNo)||spEventNo81.equals(spEventNo)||spEventNo86.equals(spEventNo)||spEventNo87.equals(spEventNo)) {
			List<CoreEffectivenessCode> coreEffectivenessCodeList = httpQueryService.queryEffectivenessCodeList(operationMode, evnetEffectivenessCodeType);
			if (null != coreEffectivenessCodeList && !coreEffectivenessCodeList.isEmpty()) {
				CoreEffectivenessCode coreEffectivenessCode = coreEffectivenessCodeList.get(0);
				effCodeScope = coreEffectivenessCode.getEffectivenessCodeScope();
			}
			//生效范围A
			if ("A".equals(effCodeScope)) {
				//场景触发对象 A/P/G
				if ("A".equals(sceneTriggerObject)) {
					levelCodeList.add(levelCode);
				}else if ("P".equals(sceneTriggerObject)) {
					//查询账户基本信息
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreAccountSqlBuilder.andProductObjectCodeEqualTo(levelCode);
					List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
					if (null != coreAccountList && !coreAccountList.isEmpty()) {
						for (CoreAccount coreAccount : coreAccountList) {
							//生效码范围A - 触发场景对象P - 层级代码:业务类型代码
							if (!levelCodeList.contains(coreAccount.getBusinessTypeCode())) {
								levelCodeList.add(coreAccount.getBusinessTypeCode());
							}
						}
					}
				}else if ("G".equals(sceneTriggerObject)) {
					//查询账户基本信息
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
					List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
					if (null != coreAccountList && !coreAccountList.equals(coreAccountList)) {
						for (CoreAccount coreAccount : coreAccountList) {
							//生效码范围A - 触发场景对象G - 层级代码:业务类型代码
							if (!levelCodeList.contains(coreAccount.getBusinessTypeCode())) {
								levelCodeList.add(coreAccount.getBusinessTypeCode());
							}
						}
					}
				}
			//生效范围P
			}else if ("P".equals(effCodeScope)) {
				//场景触发对象 A/P/G
				if ("A".equals(sceneTriggerObject)) {
					//生效码范围P - 触发场景对象A - 层级代码:产品代码
					levelCodeList.add(levelCode);
				}else if ("P".equals(sceneTriggerObject)) {
					//生效码范围P - 触发场景对象P - 层级代码:产品代码
					levelCodeList.add(levelCode);
				}else if ("G".equals(sceneTriggerObject)) {
					//查询账户基本信息
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
					List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
					if (null != coreAccountList && !coreAccountList.equals(coreAccountList)) {
						for (CoreAccount coreAccount : coreAccountList) {
							//生效码范围 - 触发场景对象G - 层级代码:业务项目代码
							if (!levelCodeList.contains(coreAccount.getBusinessTypeCode())) {
								levelCodeList.add(coreAccount.getBusinessTypeCode());
							}
						}
					}
				}
			//生效范围G
			}else if ("G".equals(effCodeScope)) {
				//场景触发对象 A/P/G
				if ("A".equals(sceneTriggerObject)) {
					//查询账户基本信息
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
					List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
					if (null != coreAccountList && !coreAccountList.equals(coreAccountList)) {
						for (CoreAccount coreAccount : coreAccountList) {
							//生效码范围G - 触发场景对象A - 层级代码:业务类型代码
							if (!levelCodeList.contains(coreAccount.getBusinessTypeCode())) {
								levelCodeList.add(coreAccount.getBusinessTypeCode());
							}
						}
					}
				}else if ("P".equals(sceneTriggerObject)) {
					CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
					coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreAccountSqlBuilder.andProductObjectCodeEqualTo(levelCode);
					List<CoreAccount> coreAccountList = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
					if (null != coreAccountList && !coreAccountList.isEmpty()) {
						for (CoreAccount coreAccount : coreAccountList) {
							// 生效码范围G - 触发场景对象P - 层级代码:业务项目代码
							if (!levelCodeList.contains(coreAccount.getBusinessProgramNo())) {
								levelCodeList.add(coreAccount.getBusinessProgramNo());
							}
						}
					}
				}else if ("G".equals(sceneTriggerObject)) {
					//生效码范围G  - 触发场景对象G - 层级代码:业务项目代码
					levelCodeList.add(levelCode);
				}
			}else if ("C".equals(effCodeScope)) {
				//生效码范围C - 任何场景触发对象 - 层级代码:客户号
				levelCodeList.add(customerNo);
			}
			x5180bo.setLevelCodeList(levelCodeList);
		}
		String effectivenessCodeTypeResult = "";
		String effectivenessCodeSceneResult = "";
		String SceneTriggerObjectLevelResult = "";
		String sceneTriggerObjectCodeResult = "";
		// 以往场景,不继续修改
		if (!spEventNo80.equals(spEventNo)&&!spEventNo81.equals(spEventNo)&&!spEventNo86.equals(spEventNo)&&!spEventNo87.equals(spEventNo)) {
			CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
			coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
			coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(spEventNo);
			coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectLevelEqualTo(sceneTriggerObject);
			coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectCodeEqualTo(levelCode);
			
			coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
			coreCustomerEffectiveCode = coreCustomerEffectiveCodeDao.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
			//查询封锁码场景，类别封锁码内容
			if (null != coreCustomerEffectiveCode) {
				
				effectivenessCodeTypeResult = coreCustomerEffectiveCode.getEffectivenessCodeType();
				effectivenessCodeSceneResult = coreCustomerEffectiveCode.getEffectivenessCodeScene();
				SceneTriggerObjectLevelResult = coreCustomerEffectiveCode.getSceneTriggerObjectLevel();
				sceneTriggerObjectCodeResult = coreCustomerEffectiveCode.getSceneTriggerObjectCode();
				coreCustomerEffectiveCode.setState("D");
				coreCustomerEffectiveCode.setRemovalUserid(operatorId);
				coreCustomerEffectiveCode.setRemoveDate(operationDate);
				coreCustomerEffectiveCode.setVersion(coreCustomerEffectiveCode.getVersion()+1);
				coreCustomerEffectiveCodeDao.updateBySqlBuilderSelective(coreCustomerEffectiveCode, coreCustomerEffectiveCodeSqlBuilder);
			}else {
				if (!system.equals(operatorId)) {
					throw new BusinessException("CUS-00119");
				}else {
					x5180bo.setOperationMode(operationMode);
					x5180bo.setCorporation(corporation);
					x5180bo.setBlockFlag(Constant.NOTHANDLE);
					return x5180bo;
				}
			}
		}else {
			//延滞删除封锁码
			if (null != levelCodeList && !levelCodeList.isEmpty()) {
				boolean flag = true;
				for (String seneTriggerObjectCode1 : levelCodeList) {
					CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
					coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(spEventNo);
					coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectLevelEqualTo(sceneTriggerObject);
					coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectCodeEqualTo(seneTriggerObjectCode1);
					if (spEventNo86.equals(spEventNo) || spEventNo87.equals(spEventNo)) {
						coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeTypeEqualTo(effectivenessCodeType);
						coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeSceneEqualTo(effectivenessCodeScene);
					}
					coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
					coreCustomerEffectiveCode = 
							coreCustomerEffectiveCodeDao.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
					if (null != coreCustomerEffectiveCode) {
						flag = false;
						effectivenessCodeTypeResult = coreCustomerEffectiveCode.getEffectivenessCodeType();
						effectivenessCodeSceneResult = coreCustomerEffectiveCode.getEffectivenessCodeScene();
						SceneTriggerObjectLevelResult = coreCustomerEffectiveCode.getSceneTriggerObjectLevel();
						sceneTriggerObjectCodeResult = coreCustomerEffectiveCode.getSceneTriggerObjectCode();
						coreCustomerEffectiveCode.setState("D");
						coreCustomerEffectiveCode.setRemovalUserid(operatorId);
						coreCustomerEffectiveCode.setRemoveDate(operationDate);
						coreCustomerEffectiveCode.setVersion(coreCustomerEffectiveCode.getVersion()+1);
						coreCustomerEffectiveCodeDao.updateBySqlBuilderSelective(coreCustomerEffectiveCode, coreCustomerEffectiveCodeSqlBuilder);
					}
				}
				// 延滞处理,没有查询到任何需要删除封锁码
				if (flag && system.equals(operatorId) ) {
					x5180bo.setOperationMode(operationMode);
					x5180bo.setCorporation(corporation);
					x5180bo.setBlockFlag(Constant.NOTHANDLE);
					return x5180bo;
				}
			}else {
				x5180bo.setOperationMode(operationMode);
				x5180bo.setCorporation(corporation);
				x5180bo.setBlockFlag(Constant.NOTHANDLE);
				return x5180bo;
			}
		}
		
		x5180bo.setOperationMode(operationMode);
		x5180bo.setEventNo(spEventNo);
		// 生效码类别
		x5180bo.setEffectivenessCodeType(effectivenessCodeTypeResult);
		// 生效码序号
		x5180bo.setEffectivenessCodeScene(effectivenessCodeSceneResult);
		// 场景触发对象
		x5180bo.setSceneTriggerObject(SceneTriggerObjectLevelResult);
		x5180bo.setLevelCode(sceneTriggerObjectCodeResult);
		//管控来源
		x5180bo.setContrlSource(contrlSource);
		// 生效码-范围
		x5180bo.setEffectivenessCodeScope(sceneTriggerObject);
//		sceneTriggerLevelCode
		x5180bo.setSceneTriggerObject(sceneTriggerObject);
		x5180bo.setSceneTriggerLevelCode(levelCode);
		//删除
		x5180bo.setBlockFlag(Constant.DEL);
		x5180bo.setOperationDate(operationDate);
		x5180bo.setCorporation(corporation);
		return x5180bo;
	}
	
}
