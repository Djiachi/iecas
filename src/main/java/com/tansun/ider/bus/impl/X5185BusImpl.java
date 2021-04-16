package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5185Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5185BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.OperationModeUtil;

/**
 * @version:1.0
 * @Description: 封锁码新增
 * @author: wt
 */
@Service
public class X5185BusImpl implements X5185Bus {

	private static String A = "A";
	private static String C= "C";
	private static String P = "P";
	private static String M = "M";
	private static String G = "G";
	
	// 管控来源
	private static final String contrlSource = "BLCK";
	private static final String system = "system";

	@Autowired
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Autowired
	public OperationModeUtil operationModeUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
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
	public Object busExecute(X5185BO x5185bo) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(x5185bo);
		// 通过证件类型 证 件号码 查询客户号 或者外部识别号查询客户号
		String customerNo = x5185bo.getCustomerNo();
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5185bo, eventCommAreaNonFinance);
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("CUS-00013", "客户号");
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if (null == coreCustomer) {
			throw new BusinessException("CUS-00105");
		}
		String corporation = coreCustomer.getCorporationEntityNo();
		// 运营模式 必输
		String operationMode = x5185bo.getOperationMode();
		// 事件编号 必输
		String spEventNo = x5185bo.getSpEventNo();
		// 生效码类别 非必输
		String effectivenessCodeScene = x5185bo.getEffectivenessCodeScene();
		// 生效码序号 非必输
		String effectivenessCodeType = x5185bo.getEffectivenessCodeType();
		// 管控层级 非必输
		String sceneTriggerObject = x5185bo.getSceneTriggerObject();
		// 场景触发对象代码   必输     //业务类型代码
		String levelCode = x5185bo.getLevelCode();
		// 业务项目代码
		String businessProgramNo = x5185bo.getBusinessProgramNo();
		// 层级代码 
		String sceneTriggerLevelCode = x5185bo.getSceneTriggerLevelCode();
		// 币种 非必输 账户级别的币种 必须输入
		String currencyCode = x5185bo.getCurrencyCode();
		// 操作员标识 必输
		String operatorId = x5185bo.getOperatorId();
		CoreEvent coreEvent = httpQueryService.queryEvent(spEventNo);
		if (null != coreEvent) {
			Integer eventEffectivenessCodeScene = coreEvent.getEffectivenessCodeScene();
			String eventEffectivenessCodeType = coreEvent.getEffectivenessCodeType();
			if (StringUtil.isBlank(sceneTriggerObject)) {
				sceneTriggerObject = coreEvent.getSceneTriggerObject();
			}
			if (StringUtil.isBlank(effectivenessCodeScene)) {
				if (null == eventEffectivenessCodeScene) {
					throw new BusinessException("CUS-00110",spEventNo);
				}
				effectivenessCodeScene = eventEffectivenessCodeScene.toString();
				eventCommAreaNonFinance.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
			}
			if (StringUtil.isBlank(effectivenessCodeType)) {
				if (StringUtil.isBlank(eventEffectivenessCodeType)) {
					throw new BusinessException("CUS-00109",spEventNo);
				}
				effectivenessCodeType = eventEffectivenessCodeType;
				eventCommAreaNonFinance.setEffectivenessCodeType(eventEffectivenessCodeType);
			}
		} else {
			throw new BusinessException("CUS-00111", spEventNo);
		}
		
		//层级代码 list
		ArrayList<String> levelCodeList = new ArrayList<>();
		// 查询封锁码表
		CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(operationMode,
				effectivenessCodeType, effectivenessCodeScene);
		//生效码范围
		String effectivenessCodeScope = coreEffectivenessCode.getEffectivenessCodeScope();
		//触发场景对象
		eventCommAreaNonFinance.setSceneTriggerObject(sceneTriggerObject);
//		c对c  p对c和p g对g和c  a对 c g p a  m对c p m
//		如果事件表,配置O则跳过不检查，如果不是O则继续检查
		String sceneTriggerObject_O = coreEvent.getSceneTriggerObject();
		if (StringUtil.isNotBlank(sceneTriggerObject_O) && !"O".equals(sceneTriggerObject_O) ) {
			if (C.equals(sceneTriggerObject)) {
				//触发对象层级C
				sceneTriggerLevelCode = levelCode;
			}else if (P.equals(sceneTriggerObject)) {
				if (C.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = customerNo;
				}else if (P.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = levelCode;
				}else {
					throw new BusinessException("CUS-00149", sceneTriggerObject,effectivenessCodeScope);
				}
			}else if (M.equals(sceneTriggerObject)) {
				if (C.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = customerNo;
				}else if (P.equals(effectivenessCodeScope)) {
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
					coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(levelCode);
					CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
					sceneTriggerLevelCode = coreMediaBasicInfo.getProductObjectCode();
				}else if (M.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = levelCode;
				}else {
					throw new BusinessException("CUS-00149", sceneTriggerObject,effectivenessCodeScope);
				}
			}else if (A.equals(sceneTriggerObject)) {
				CoreAccountSqlBuilder coreAccountSqlBuilder =  new CoreAccountSqlBuilder();
				coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
				coreAccountSqlBuilder.andAccountIdEqualTo(levelCode);
				coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
				CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
				if (null != coreAccount) {
					if (G.equals(effectivenessCodeScope)) {
						sceneTriggerLevelCode = coreAccount.getBusinessProgramNo();
					}else if (P.equals(effectivenessCodeScope)) {
						sceneTriggerLevelCode = coreAccount.getProductObjectCode();
					}else if (C.equals(effectivenessCodeScope)) {
						sceneTriggerLevelCode = customerNo;
					}else if (A.equals(effectivenessCodeScope)) {
						sceneTriggerLevelCode = levelCode;
					}else {
						throw new BusinessException("CUS-00149", sceneTriggerObject,effectivenessCodeScope);
					}
				}else {
					sceneTriggerLevelCode = levelCode;
				}
			}else if (G.equals(sceneTriggerObject)) {
				if (G.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = levelCode;
				}else if (C.equals(effectivenessCodeScope)) {
					sceneTriggerLevelCode = customerNo;
				}else {
					throw new BusinessException("CUS-00149", sceneTriggerObject,effectivenessCodeScope);
				}
			}
		}else {
			// 此处添加 延滞新增对于封锁码内容修改  - wt
			//生效码范围
			if (spEventNo80.equals(spEventNo)||spEventNo81.equals(spEventNo)||spEventNo86.equals(spEventNo)||spEventNo87.equals(spEventNo)) {
				//范围A
				if ("A".equals(effectivenessCodeScope)) {
					//触发场景对象 A\P\G -  sceneTriggerObject
					if ("A".equals(sceneTriggerObject)) {
						//生效码范围A - 触发场景对象A - 层级代码:业务类型代码
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
						if (StringUtil.isNotBlank(businessProgramNo)) {
							coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
						}
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
				}else if ("P".equals(effectivenessCodeScope)) {
				//范围P
					//触发场景对象 A\P\G -  sceneTriggerObject  
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
						if (StringUtil.isNotBlank(businessProgramNo)) {
							coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
						}
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
				}else if ("G".equals(effectivenessCodeScope)) {
				//范围G
					//触发场景对象 A\P\G -  sceneTriggerObject
					if ("A".equals(sceneTriggerObject)) {
						//查询账户基本信息
						CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
						coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
						if (StringUtil.isNotBlank(businessProgramNo)) {
							coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
						}
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
				}else if ("C".equals(effectivenessCodeScope)) {
				//生效码范围C - 任何场景触发对象 - 层级代码:客户号
					levelCodeList.add(customerNo);
				}
			}else {
				sceneTriggerLevelCode = levelCode;
			}
		}
		//生效码层级
		eventCommAreaNonFinance.setEffectivenessCodeScope(effectivenessCodeScope);
		if (sceneTriggerObject.equals(A)) {
			if (StringUtil.isBlank(currencyCode)) {
				throw new BusinessException("CUS-00067");
			}
		}
		
		if (null == coreEffectivenessCode) {
			throw new BusinessException("CUS-00014", "生效码表");
		}
		X5190BusImpl x5190BusImpl = SpringUtil.getBean(X5190BusImpl.class);
		int blockCodeScopeIn = x5190BusImpl.blockCodeScopeToInt(sceneTriggerObject);
		int blockCodeScopeOut = x5190BusImpl
				.blockCodeScopeToInt(coreEffectivenessCode.getEffectivenessCodeScope().toString());
		// 比较封锁码管控层级
		/*if (blockCodeScopeIn < blockCodeScopeOut) {
			throw new BusinessException("CUS-00084", effectivenessCodeType + effectivenessCodeScene, levelCode);
		}*/
		String operationDate = "";
		CoreSystemUnit coreSystemUnit = operationModeUtil.getcoreOperationMode(customerNo);
		
		if (Constant.EOD.equals(coreSystemUnit.getSystemOperateState())) {
			operationDate = coreSystemUnit.getCurrProcessDate();
		} else {
			operationDate = coreSystemUnit.getNextProcessDate();
		}
		if (!spEventNo80.equals(spEventNo)&&! spEventNo81.equals(spEventNo)&&!spEventNo86.equals(spEventNo)&&!spEventNo87.equals(spEventNo)) {
			CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
			if (StringUtil.isNotBlank(customerNo)) {
				coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
			}
			if (StringUtil.isNotBlank(spEventNo)) {
				coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(spEventNo);
			}
			//层级
			coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectLevelEqualTo(sceneTriggerObject);
			//层级代码
			coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectCodeEqualTo(sceneTriggerLevelCode);
			coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
			if (StringUtil.isNotBlank(currencyCode)) {
				coreCustomerEffectiveCodeSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
			}
			// 类别
			CoreCustomerEffectiveCode coreCustomerEffectiveCode = coreCustomerEffectiveCodeDao
					.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
			if (null != coreCustomerEffectiveCode) {
				if (!system.equals(operatorId)) {
					throw new BusinessException("CUS-00119");
				} else {
					// 批量程序单独处理，不抛出异常
					// 如果 生效码类别、生效码序号一致则不做处理，如果不一致，需要先删除，然后新增
					String effectivenessCodeTypeOld = coreCustomerEffectiveCode.getEffectivenessCodeType();
					String effectivenessCodeSceneOld = coreCustomerEffectiveCode.getEffectivenessCodeScene();
					if (effectivenessCodeType.equals(effectivenessCodeTypeOld)
							&& effectivenessCodeScene.equals(effectivenessCodeSceneOld)) {
						x5185bo.setBlockFlag(Constant.NOTHANDLE);
						return x5185bo;
					}else {
						coreCustomerEffectiveCode.setState("D");
						coreCustomerEffectiveCode.setRemovalUserid(operatorId);
						coreCustomerEffectiveCode.setRemoveDate(operationDate);
						coreCustomerEffectiveCode.setVersion(coreCustomerEffectiveCode.getVersion()+1);
						coreCustomerEffectiveCodeDao.updateBySqlBuilderSelective(coreCustomerEffectiveCode,
								coreCustomerEffectiveCodeSqlBuilder);
						//调用删除重新计算视图
						x5190BusImpl.delControlProjectNo(operationMode, effectivenessCodeTypeOld, effectivenessCodeScene, sceneTriggerObject, 
								sceneTriggerLevelCode, currencyCode, customerNo, corporation, eventCommAreaNonFinance, operationDate, contrlSource,spEventNo);
						coreCustomerEffectiveCode = null;
					}
				}
			}
			if (null == coreCustomerEffectiveCode) {
				CoreCustomerEffectiveCode coreCustomerEffectiveCodeNew = new CoreCustomerEffectiveCode();
				coreCustomerEffectiveCodeNew.setId(RandomUtil.getUUID());
				coreCustomerEffectiveCodeNew.setCurrencyCode(currencyCode);
				coreCustomerEffectiveCodeNew.setCustomerNo(customerNo);
				coreCustomerEffectiveCodeNew.setEffectivenessCodeType(effectivenessCodeType);
				coreCustomerEffectiveCodeNew.setEffectivenessCodeScene(effectivenessCodeScene);
				coreCustomerEffectiveCodeNew.setEventNo(spEventNo);
				coreCustomerEffectiveCodeNew.setSceneTriggerObjectLevel(sceneTriggerObject);
				coreCustomerEffectiveCodeNew.setSceneTriggerObjectCode(sceneTriggerLevelCode);
				coreCustomerEffectiveCodeNew.setSettingDate(operationDate);
				coreCustomerEffectiveCodeNew.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
				coreCustomerEffectiveCodeNew.setSettingUpUserid(operatorId);
				coreCustomerEffectiveCodeNew.setState(YesOrNo.YES.getValue());
				coreCustomerEffectiveCodeNew.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
				coreCustomerEffectiveCodeNew.setVersion(1);
				coreCustomerEffectiveCodeDao.insert(coreCustomerEffectiveCodeNew);
			}
		}else {
			//延滞处理，需要重新修改业务逻辑
			if (null != levelCodeList && !levelCodeList.isEmpty()) {
				for (String seneTriggerObjectCode1 : levelCodeList) {
					CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
					if (StringUtil.isNotBlank(customerNo)) {
						coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
					}
					if (StringUtil.isNotBlank(spEventNo)) {
						coreCustomerEffectiveCodeSqlBuilder.andEventNoEqualTo(spEventNo);
					}
					//场景触发对象层级
					coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectLevelEqualTo(sceneTriggerObject);
					//场景触发对象代码
					coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectCodeEqualTo(seneTriggerObjectCode1);
					if (spEventNo86.equals(spEventNo) || spEventNo87.equals(spEventNo)) {
						coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeTypeEqualTo(effectivenessCodeType);
						coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeSceneEqualTo(effectivenessCodeScene);
					}
					coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
					CoreCustomerEffectiveCode coreCustomerEffectiveCode = 
							coreCustomerEffectiveCodeDao.selectBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
					if (null == coreCustomerEffectiveCode) {
						CoreCustomerEffectiveCode coreCustomerEffectiveCodeNew = new CoreCustomerEffectiveCode();
						coreCustomerEffectiveCodeNew.setId(RandomUtil.getUUID());
						coreCustomerEffectiveCodeNew.setCurrencyCode(currencyCode);
						coreCustomerEffectiveCodeNew.setCustomerNo(customerNo);
						coreCustomerEffectiveCodeNew.setEffectivenessCodeType(effectivenessCodeType);
						coreCustomerEffectiveCodeNew.setEffectivenessCodeScene(effectivenessCodeScene);
						coreCustomerEffectiveCodeNew.setEventNo(spEventNo);
						coreCustomerEffectiveCodeNew.setSceneTriggerObjectLevel(sceneTriggerObject);
						coreCustomerEffectiveCodeNew.setSceneTriggerObjectCode(seneTriggerObjectCode1);
						coreCustomerEffectiveCodeNew.setSettingDate(operationDate);
						coreCustomerEffectiveCodeNew.setSettingTime(DateUtil.format(new Date(), "HH:mm:ss:SSS"));
						coreCustomerEffectiveCodeNew.setSettingUpUserid(operatorId);
						coreCustomerEffectiveCodeNew.setState(YesOrNo.YES.getValue());
						coreCustomerEffectiveCodeNew.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
						coreCustomerEffectiveCodeNew.setVersion(1);
						coreCustomerEffectiveCodeDao.insert(coreCustomerEffectiveCodeNew);
					}
				}
			}else {
				//批量逻辑,不进行处理
				x5185bo.setBlockFlag(Constant.NOTHANDLE);
				return x5185bo;
			}
		}
		// 管控来源
		x5185bo.setContrlSource(contrlSource);
		x5185bo.setEventNo(spEventNo);
		// 生效码类别
		x5185bo.setEffectivenessCodeType(effectivenessCodeType);
		// 生效码场景
		x5185bo.setEffectivenessCodeScene(effectivenessCodeScene);
		// 场景触发对象
		x5185bo.setSceneTriggerObject(sceneTriggerObject);
		// 生效码范围
		x5185bo.setEffectivenessCodeScope(effectivenessCodeScope);
		// 新增
		x5185bo.setBlockFlag(Constant.ADD);
		x5185bo.setOperationDate(operationDate);
		x5185bo.setCustomerNo(customerNo);
		x5185bo.setCorporation(corporation);
		//延滞处理,修改逻辑
		x5185bo.setLevelCodeList(levelCodeList);
		// 层级代码 
		if (StringUtil.isNotBlank(sceneTriggerLevelCode)) {
			x5185bo.setSceneTriggerLevelCode(sceneTriggerLevelCode);
		}else {
			x5185bo.setSceneTriggerLevelCode(levelCode);
		}
		return x5185bo;
	}
	
}
