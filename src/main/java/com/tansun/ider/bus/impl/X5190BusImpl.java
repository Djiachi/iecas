package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5190Bus;
import com.tansun.ider.dao.beta.entity.CoreBusTypeTransIdRel;
import com.tansun.ider.dao.beta.entity.CoreCntrlProgRelEvent;
import com.tansun.ider.dao.beta.entity.CoreControlProject;
import com.tansun.ider.dao.beta.entity.CoreEffectiveCdeCntrlProg;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.issue.CoreCustomerContrlEventDao;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlEvent;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlEventSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ControlFieldStatus;
import com.tansun.ider.enums.ProjectType;
import com.tansun.ider.enums.TriggerEventType;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthCardIssueExcptBean;
import com.tansun.ider.model.bo.X5190BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ChildTaskStartUtil;

/**
 * @version:1.0
 * @Description: 封锁码管控视图更新
 * @author: admin
 */
@Service
public class X5190BusImpl implements X5190Bus {
	// 客户级 > 产品级 = 业务项目级 = 媒介级 = 业务类型级

	private static int C = 50;
	// 产品级
	private static int P = 40;
	// 业务项目级
	private static int G = 30;
	// 媒介级
	private static int M = 20;
	// 业务类型级
	private static int A = 10;

	static String preControlCategory_A = "A";

	@Autowired
	private CoreCustomerContrlViewDao coreCustomerContrlViewDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Resource
	private CoreCustomerContrlEventDao coreCustomerContrlEventDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Value("${global.target.service.url.auth}")
	private String authUrl;
	@Value("${global.target.service.url.card}")
	private String cardUrl;
	@Value("${global.target.service.url.nofn}")
	private String nonFinUrl;
	@Value("${global.target.service.nofn.SP800050}")
    private String spEventNo80;
	@Value("${global.target.service.nofn.SP810050}")
    private String spEventNo81;
	@Value("${global.target.service.nofn.SP860050}")
    private String spEventNo86;
	@Value("${global.target.service.nofn.SP870050}")
    private String spEventNo87;
	
	
	@SuppressWarnings("unused")
	@Override
	public Object busExecute(X5190BO x5190bo) throws Exception {

		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5190bo,eventCommAreaNonFinance);
		CoreEventActivityRel dto = x5190bo.getCoreEventActivityRel();
		// 运营模式 必输
		String operationMode = x5190bo.getOperationMode();
		// 客户号 必输
		String customerNo = x5190bo.getCustomerNo();
		// 类别 必输
		String effectivenessCodeType = x5190bo.getEffectivenessCodeType();
		// 生效码场景
		String effectivenessCodeScene = x5190bo.getEffectivenessCodeScene();
		// 管控层级 必输
		String sceneTriggerObject = x5190bo.getSceneTriggerObject();
		// 生效码范围
		String effectivenessCodeScope = x5190bo.getEffectivenessCodeScope();
		// 场景触发对象层级代码 必输
//		String levelCode = x5190bo.getLevelCode();
		// 层级代码 
		String sceneTriggerLevelCode = x5190bo.getSceneTriggerLevelCode();
		// 管控来源 必输
		String contrlSource = x5190bo.getContrlSource();
		// 币种 非必输
		String currencyCode = x5190bo.getCurrencyCode();
		// 封锁码标识 新增:A 删除 :D
		String blockFlag = x5190bo.getBlockFlag();
		// 当前营业日期
		String operationDate = x5190bo.getOperationDate();
		// 法人实体编号
		String corporation = x5190bo.getCorporation();
		//层级代码 保存list参数
		ArrayList<String> levelCodeList = x5190bo.getLevelCodeList();
		//当前事件编号
		String spEventNo = x5190bo.getEventNo();
		if (StringUtil.isNotBlank(blockFlag) && Constant.ADD.equals(blockFlag)) {
			// 查询封锁码关联管控视图表
			List<CoreEffectiveCdeCntrlProg> coreEffectiveCdeCntrlProgList = httpQueryService
					.queryCoreEffectivenessCodeRelateControlItemList(operationMode, effectivenessCodeType,
							effectivenessCodeScene,ProjectType.CONTROL_PROJECT.getValue());
			if (null == coreEffectiveCdeCntrlProgList
					|| coreEffectiveCdeCntrlProgList.isEmpty()) {
				// 运营模式,类别,场景查询封锁码关联管控项目表不存在,则跳出,不更新
				return x5190bo;
			}
			// 判断是否同步授权封锁码
			boolean flag = false;
			// 新增管控
			// 更新封锁码管控项目
			// 同步授权
			List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
			//正常情况
			if (!spEventNo80.equals(spEventNo)&&! spEventNo81.equals(spEventNo)&&!spEventNo86.equals(spEventNo)&&!spEventNo87.equals(spEventNo)) {
				for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
					// 查询管控项目表
					List<CoreControlProject> coreControlProjectList = httpQueryService
							.queryCoreControlProjectList(operationMode, preControlCategory_A);
					String controlProjectNo = coreEffectiveCdeCntrlProg.getControlProjectNo();
					// 管控项目
					if (null != coreControlProjectList && !coreControlProjectList.isEmpty()) {
						for (CoreControlProject coreControlProjectList2 : coreControlProjectList) {
							if (controlProjectNo.equals(coreControlProjectList2.getControlProjectNo())) {
								flag = true;
								String authDenyIdentify = coreControlProjectList2.getAuthDenyIdentify();
								if (StringUtil.isBlank(authDenyIdentify)) {
									throw new BusinessException("CUS-00104", coreControlProjectList2.getControlProjectNo());
								}
								// 发卡例外同步封锁码
								Map<String, Object> triggerEventParams = addAuthCardIssueExcptBean(effectivenessCodeScope,
										sceneTriggerLevelCode, currencyCode, customerNo, corporation, authDenyIdentify, operationMode ,spEventNo);
								eventCommAreaTriggerEventList.add(triggerEventParams);
							}
						}
					}
					// ********************************* 同步授权封锁码
					CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
					coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreCustomerContrlViewSqlBuilder
							.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
					List<CoreCustomerContrlView> coreCustomerContrlViewList = coreCustomerContrlViewDao
							.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
					int sequenceNum = coreCustomerContrlViewList.size();
					if (null != coreCustomerContrlViewList && !coreCustomerContrlViewList.isEmpty()) {
						// 比较优先级
						for (CoreCustomerContrlView coreCustomerContrlView : coreCustomerContrlViewList) {
							if (coreCustomerContrlView.getContrlSerialNo() == 000) {
								if ("C".equals(coreCustomerContrlView.getContrlLevel())) {
									//已经是最高层级,不需要在更高的层级
									continue;
								}else {
									//查询当前管控视图是否存在
									CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder1 = new CoreCustomerContrlViewSqlBuilder();
									coreCustomerContrlViewSqlBuilder1.andCustomerNoEqualTo(customerNo);
									coreCustomerContrlViewSqlBuilder1
											.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
									coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
									coreCustomerContrlViewSqlBuilder1.andContrlSerialNoEqualTo(000);
									List<CoreCustomerContrlView> coreCustomerContrlViewList1 = coreCustomerContrlViewDao
											.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder1);
									if (null == coreCustomerContrlViewList1 || coreCustomerContrlViewList1.isEmpty()) {
										// 新增
										CoreCustomerContrlView coreCustomerContrlViewFirstAdd = new CoreCustomerContrlView();
										coreCustomerContrlViewFirstAdd.setId(RandomUtil.getUUID());
										coreCustomerContrlViewFirstAdd.setCustomerNo(customerNo);
										// 管控项目编号
										coreCustomerContrlViewFirstAdd
												.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
										// 管控序号
										coreCustomerContrlViewFirstAdd.setContrlSerialNo(000);
										// 管控层级
										coreCustomerContrlViewFirstAdd.setContrlLevel(effectivenessCodeScope);
										// 层级代码
										coreCustomerContrlViewFirstAdd.setLevelCode(sceneTriggerLevelCode);
										// 币种
										if (StringUtil.isNotBlank(currencyCode)) {
											coreCustomerContrlViewFirstAdd.setCurrencyCode(currencyCode);
										}
										// 生效码类别
										coreCustomerContrlViewFirstAdd.setEffectivenessCodeType(effectivenessCodeType);
										// 生效码序号
										coreCustomerContrlViewFirstAdd.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
										// 管控来源
										coreCustomerContrlViewFirstAdd.setContrlSource(contrlSource);
										// 管控开始日期
										coreCustomerContrlViewFirstAdd.setContrlStartDate(operationDate);
										// 建立日期
										coreCustomerContrlViewFirstAdd.setGmtCreate(DateUtil.getDate());
										// 版本号
										coreCustomerContrlViewFirstAdd.setVersion(1);
										coreCustomerContrlViewDao.insert(coreCustomerContrlViewFirstAdd);
									}
									
								}
							} else {
								//查询当前管控视图是否存在
								CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder1 = new CoreCustomerContrlViewSqlBuilder();
								coreCustomerContrlViewSqlBuilder1.andCustomerNoEqualTo(customerNo);
								coreCustomerContrlViewSqlBuilder1
										.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
								coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
								coreCustomerContrlViewSqlBuilder1.andContrlSerialNoEqualTo(000);
								List<CoreCustomerContrlView> coreCustomerContrlViewList1 = coreCustomerContrlViewDao
										.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder1);
								if (null == coreCustomerContrlViewList1 || coreCustomerContrlViewList1.isEmpty()) {
									// 新增
									CoreCustomerContrlView coreCustomerContrlViewAdd = new CoreCustomerContrlView();
									coreCustomerContrlViewAdd.setId(RandomUtil.getUUID());
									coreCustomerContrlViewAdd.setCustomerNo(customerNo);
									// 管控项目编号
									coreCustomerContrlViewAdd
											.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
									// 管控序号
									coreCustomerContrlViewAdd.setContrlSerialNo(000);
									// 管控层级
									coreCustomerContrlViewAdd.setContrlLevel(effectivenessCodeScope);
									// 层级代码
									coreCustomerContrlViewAdd.setLevelCode(sceneTriggerLevelCode);
									// 币种
									if (StringUtil.isNotBlank(currencyCode)) {
										coreCustomerContrlViewAdd.setCurrencyCode(currencyCode);
									}
									// 生效码类别
									coreCustomerContrlViewAdd.setEffectivenessCodeType(effectivenessCodeType);
									// 生效码序号
									coreCustomerContrlViewAdd
											.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
									// 管控来源
									coreCustomerContrlViewAdd.setContrlSource(contrlSource);
									// 管控开始日期
									coreCustomerContrlViewAdd.setContrlStartDate(operationDate);
									// 建立日期
									coreCustomerContrlViewAdd.setGmtCreate(DateUtil.getDate());
									// 版本号
									coreCustomerContrlViewAdd.setVersion(1);
									int i = coreCustomerContrlViewDao.insert(coreCustomerContrlViewAdd);
								}
							}
						}
					} else {
						// 新增
						CoreCustomerContrlView coreCustomerContrlViewFirstAdd = new CoreCustomerContrlView();
						coreCustomerContrlViewFirstAdd.setId(RandomUtil.getUUID());
						coreCustomerContrlViewFirstAdd.setCustomerNo(customerNo);
						// 管控项目编号
						coreCustomerContrlViewFirstAdd
								.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						// 管控序号
						coreCustomerContrlViewFirstAdd.setContrlSerialNo(000);
						// 管控层级
						coreCustomerContrlViewFirstAdd.setContrlLevel(effectivenessCodeScope);
						// 层级代码
						coreCustomerContrlViewFirstAdd.setLevelCode(sceneTriggerLevelCode);
						// 币种
						if (StringUtil.isNotBlank(currencyCode)) {
							coreCustomerContrlViewFirstAdd.setCurrencyCode(currencyCode);
						}
						// 生效码类别
						coreCustomerContrlViewFirstAdd.setEffectivenessCodeType(effectivenessCodeType);
						// 生效码序号
						coreCustomerContrlViewFirstAdd.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
						// 管控来源
						coreCustomerContrlViewFirstAdd.setContrlSource(contrlSource);
						// 管控开始日期
						coreCustomerContrlViewFirstAdd.setContrlStartDate(operationDate);
						// 建立日期
						coreCustomerContrlViewFirstAdd.setGmtCreate(DateUtil.getDate());
						// 版本号
						coreCustomerContrlViewFirstAdd.setVersion(1);
						coreCustomerContrlViewDao.insert(coreCustomerContrlViewFirstAdd);
					}
				}
			}else {
				//延滞情况,管控视图逻辑处理
				if (null != levelCodeList && !levelCodeList.isEmpty()) {
					for (String seneTriggerObjectCode1 : levelCodeList) {
					sceneTriggerLevelCode =seneTriggerObjectCode1;
					for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
						// 查询管控项目表
						List<CoreControlProject> coreControlProjectList = httpQueryService
								.queryCoreControlProjectList(operationMode, preControlCategory_A);
						String controlProjectNo = coreEffectiveCdeCntrlProg.getControlProjectNo();
						// 管控项目
						if (null != coreControlProjectList && !coreControlProjectList.isEmpty()) {
							for (CoreControlProject coreControlProjectList2 : coreControlProjectList) {
								if (controlProjectNo.equals(coreControlProjectList2.getControlProjectNo())) {
									flag = true;
									String authDenyIdentify = coreControlProjectList2.getAuthDenyIdentify();
									if (StringUtil.isBlank(authDenyIdentify)) {
										throw new BusinessException("CUS-00104", coreControlProjectList2.getControlProjectNo());
									}
									// 发卡例外同步封锁码
									Map<String, Object> triggerEventParams = addAuthCardIssueExcptBean(effectivenessCodeScope,
											sceneTriggerLevelCode, currencyCode, customerNo, corporation, authDenyIdentify, operationMode ,spEventNo);
									eventCommAreaTriggerEventList.add(triggerEventParams);
								}
							}
						}
						// ********************************* 同步授权封锁码
						CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
						coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
						coreCustomerContrlViewSqlBuilder
								.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
							coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
						List<CoreCustomerContrlView> coreCustomerContrlViewList = coreCustomerContrlViewDao
								.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
						int sequenceNum = coreCustomerContrlViewList.size();
						if (null != coreCustomerContrlViewList && !coreCustomerContrlViewList.isEmpty()) {
							// 比较优先级
							for (CoreCustomerContrlView coreCustomerContrlView : coreCustomerContrlViewList) {
								if (coreCustomerContrlView.getContrlSerialNo() == 000) {
									if ("C".equals(coreCustomerContrlView.getContrlLevel())) {
										//已经是最高层级,不需要在更高的层级
										continue;
									}else {
										//查询当前管控视图是否存在
										CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder1 = new CoreCustomerContrlViewSqlBuilder();
										coreCustomerContrlViewSqlBuilder1.andCustomerNoEqualTo(customerNo);
										coreCustomerContrlViewSqlBuilder1
												.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
										coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
										coreCustomerContrlViewSqlBuilder1.andContrlSerialNoEqualTo(000);
										List<CoreCustomerContrlView> coreCustomerContrlViewList1 = coreCustomerContrlViewDao
												.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder1);
										if (null == coreCustomerContrlViewList1 || coreCustomerContrlViewList1.isEmpty()) {
											// 新增
											CoreCustomerContrlView coreCustomerContrlViewFirstAdd = new CoreCustomerContrlView();
											coreCustomerContrlViewFirstAdd.setId(RandomUtil.getUUID());
											coreCustomerContrlViewFirstAdd.setCustomerNo(customerNo);
											// 管控项目编号
											coreCustomerContrlViewFirstAdd
													.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
											// 管控序号
											coreCustomerContrlViewFirstAdd.setContrlSerialNo(000);
											// 管控层级
											coreCustomerContrlViewFirstAdd.setContrlLevel(effectivenessCodeScope);
											// 层级代码
											coreCustomerContrlViewFirstAdd.setLevelCode(sceneTriggerLevelCode);
											// 币种
											if (StringUtil.isNotBlank(currencyCode)) {
												coreCustomerContrlViewFirstAdd.setCurrencyCode(currencyCode);
											}
											// 生效码类别
											coreCustomerContrlViewFirstAdd.setEffectivenessCodeType(effectivenessCodeType);
											// 生效码序号
											coreCustomerContrlViewFirstAdd.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
											// 管控来源
											coreCustomerContrlViewFirstAdd.setContrlSource(contrlSource);
											// 管控开始日期
											coreCustomerContrlViewFirstAdd.setContrlStartDate(operationDate);
											// 建立日期
											coreCustomerContrlViewFirstAdd.setGmtCreate(DateUtil.getDate());
											// 版本号
											coreCustomerContrlViewFirstAdd.setVersion(1);
											coreCustomerContrlViewDao.insert(coreCustomerContrlViewFirstAdd);
										}
									}
								} else {
									//查询当前管控视图是否存在
									CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder1 = new CoreCustomerContrlViewSqlBuilder();
									coreCustomerContrlViewSqlBuilder1.andCustomerNoEqualTo(customerNo);
									coreCustomerContrlViewSqlBuilder1
											.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
									coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(sceneTriggerLevelCode);
									coreCustomerContrlViewSqlBuilder1.andContrlSerialNoEqualTo(000);
									List<CoreCustomerContrlView> coreCustomerContrlViewList1 = coreCustomerContrlViewDao
											.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder1);
									if (null == coreCustomerContrlViewList1 || coreCustomerContrlViewList1.isEmpty()) {
										// 新增
										CoreCustomerContrlView coreCustomerContrlViewAdd = new CoreCustomerContrlView();
										coreCustomerContrlViewAdd.setId(RandomUtil.getUUID());
										coreCustomerContrlViewAdd.setCustomerNo(customerNo);
										// 管控项目编号
										coreCustomerContrlViewAdd
												.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
										// 管控序号
										coreCustomerContrlViewAdd.setContrlSerialNo(000);
										// 管控层级
										coreCustomerContrlViewAdd.setContrlLevel(effectivenessCodeScope);
										// 层级代码
										coreCustomerContrlViewAdd.setLevelCode(sceneTriggerLevelCode);
										// 币种
										if (StringUtil.isNotBlank(currencyCode)) {
											coreCustomerContrlViewAdd.setCurrencyCode(currencyCode);
										}
										// 生效码类别
										coreCustomerContrlViewAdd.setEffectivenessCodeType(effectivenessCodeType);
										// 生效码序号
										coreCustomerContrlViewAdd
												.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
										// 管控来源
										coreCustomerContrlViewAdd.setContrlSource(contrlSource);
										// 管控开始日期
										coreCustomerContrlViewAdd.setContrlStartDate(operationDate);
										// 建立日期
										coreCustomerContrlViewAdd.setGmtCreate(DateUtil.getDate());
										// 版本号
										coreCustomerContrlViewAdd.setVersion(1);
										int i = coreCustomerContrlViewDao.insert(coreCustomerContrlViewAdd);
									}
								}
							}
						} else {
							// 新增
							CoreCustomerContrlView coreCustomerContrlViewFirstAdd = new CoreCustomerContrlView();
							coreCustomerContrlViewFirstAdd.setId(RandomUtil.getUUID());
							coreCustomerContrlViewFirstAdd.setCustomerNo(customerNo);
							// 管控项目编号
							coreCustomerContrlViewFirstAdd
									.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
							// 管控序号
							coreCustomerContrlViewFirstAdd.setContrlSerialNo(000);
							// 管控层级
							coreCustomerContrlViewFirstAdd.setContrlLevel(effectivenessCodeScope);
							// 层级代码
							coreCustomerContrlViewFirstAdd.setLevelCode(sceneTriggerLevelCode);
							// 币种
							if (StringUtil.isNotBlank(currencyCode)) {
								coreCustomerContrlViewFirstAdd.setCurrencyCode(currencyCode);
							}
							// 生效码类别
							coreCustomerContrlViewFirstAdd.setEffectivenessCodeType(effectivenessCodeType);
							// 生效码序号
							coreCustomerContrlViewFirstAdd.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
							// 管控来源
							coreCustomerContrlViewFirstAdd.setContrlSource(contrlSource);
							// 管控开始日期
							coreCustomerContrlViewFirstAdd.setContrlStartDate(operationDate);
							// 建立日期
							coreCustomerContrlViewFirstAdd.setGmtCreate(DateUtil.getDate());
							// 版本号
							coreCustomerContrlViewFirstAdd.setVersion(1);
							coreCustomerContrlViewDao.insert(coreCustomerContrlViewFirstAdd);
						}
					 }
					}
				}
				
			}
			if (!flag) {
				eventCommAreaNonFinance.setWhetherProcess("1");
			} else {
				eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
			}
			// 提前结清
			if (null != coreEffectiveCdeCntrlProgList
					&& !coreEffectiveCdeCntrlProgList.isEmpty()) {
				if (null == levelCodeList || levelCodeList.isEmpty()) {
					levelCodeList.add(sceneTriggerLevelCode);
				}
				for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
					CoreControlProject coreControlProject = httpQueryService.queryCoreControlProject(operationMode, null,coreEffectiveCdeCntrlProg.getControlProjectNo());
					if (null != coreControlProject) {
						if (StringUtil.isNotBlank(coreControlProject.getControlField())
								&& StringUtil.isNotBlank(coreEffectiveCdeCntrlProg.getControlProjectNo())
								&& ControlFieldStatus.IE.getValue().equals(coreControlProject.getControlField())) {
							// 需要触发交易
							List<CoreCntrlProgRelEvent> coreCntrlProgRelEventList = httpQueryService
									.queryCoreControlProjectRelatedEvent(operationMode,
											coreControlProject.getControlProjectNo());
							if (null != coreCntrlProgRelEventList && !coreCntrlProgRelEventList.isEmpty()) {
								for (CoreCntrlProgRelEvent coreCntrlProgRelEvent : coreCntrlProgRelEventList) {
									//发起同步交易
									if (StringUtil.isNotBlank(coreCntrlProgRelEvent.getEventNo())) {
										this.triggerEventMode(coreCntrlProgRelEvent.getEventNo(), effectivenessCodeScope, customerNo,
												levelCodeList.get(0), currencyCode, dto,operationMode,coreCntrlProgRelEvent.getControlProjectNo());
									}
								}
							}
						}
					}
				}
			}
			
			if (StringUtil.isNotBlank(effectivenessCodeScene)) {
				eventCommAreaNonFinance.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
			}
		} else if (StringUtil.isNotBlank(blockFlag) && Constant.DEL.equals(blockFlag)) {
			if (StringUtil.isNotBlank(effectivenessCodeScene)) {
				eventCommAreaNonFinance.setEffectivenessCodeScene(Integer.valueOf(effectivenessCodeScene));
			}
			return delControlProjectNo(operationMode, effectivenessCodeType, effectivenessCodeScene, effectivenessCodeScope,
					sceneTriggerLevelCode, currencyCode, customerNo, corporation, eventCommAreaNonFinance, operationDate,
					contrlSource,spEventNo);

		} else if (StringUtil.isNotBlank(blockFlag) && Constant.NOTHANDLE.equals(blockFlag)) {
			return eventCommAreaNonFinance;
		} else {
			// 需要穿入操作内容,新增以及删除标识
			throw new BusinessException("CUS-00066");
		}
		this.createCustControlEvent(customerNo, operationMode);
		return eventCommAreaNonFinance;
	}

	/**
	 * 重新计算生成管控项目关联管控事件
	 * 
	 * @param customerNo
	 *            客户号
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void createCustControlEvent(String customerNo, String operationMode) throws Exception {
		// 删除管控事件
		CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
		coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerContrlEventDao.deleteBySqlBuilder(coreCustomerContrlEventSqlBuilder);
		// 重新生成管控事件关联表
		CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
		coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerContrlViewSqlBuilder.andContrlSerialNoEqualTo(000);
		List<CoreCustomerContrlView> listCoreCustomerContrlViewList = coreCustomerContrlViewDao
				.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
		List<CoreCustomerContrlEvent> coreCustomerContrlEventList = new ArrayList<>();

		if (null != listCoreCustomerContrlViewList && !listCoreCustomerContrlViewList.isEmpty()) {
			for (CoreCustomerContrlView coreCustomerContrlView : listCoreCustomerContrlViewList) {
				List<CoreCntrlProgRelEvent> coreCntrlProgRelEventList1 = httpQueryService
						.queryCoreControlProjectRelatedEvent(operationMode,
								coreCustomerContrlView.getControlProjectNo());
				if (null != coreCntrlProgRelEventList1 && !coreCntrlProgRelEventList1.isEmpty()) {
					List<CoreCustomerContrlEvent> coreCustomerContrlEventList1 = new ArrayList<>();
					for (CoreCntrlProgRelEvent coreControlProjectRelatedEvent : coreCntrlProgRelEventList1) {
						CoreCustomerContrlEvent coreCustomerContrlEvent = new CoreCustomerContrlEvent();
						CachedBeanCopy.copyProperties(coreCustomerContrlView,coreCustomerContrlEvent);
						coreCustomerContrlEvent.setEventNo(coreControlProjectRelatedEvent.getEventNo());
						coreCustomerContrlEvent.setId(RandomUtil.getUUID());
						coreCustomerContrlEventList1.add(coreCustomerContrlEvent);
					}
					coreCustomerContrlEventList.addAll(coreCustomerContrlEventList1);
				}
			}
		}

		if (null != coreCustomerContrlEventList && !coreCustomerContrlEventList.isEmpty()) {
			coreCustomerContrlEventDao.insertUseBatch(coreCustomerContrlEventList);
		}

	}

	/**
	 * 去重重复参数内容
	 * 
	 * @param coreControlProjectRelatedEventList
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<CoreCustomerContrlEvent> getEventNo(List<CoreCustomerContrlEvent> coreCustomerContrlEventlist) {
		List<CoreCustomerContrlEvent> list = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(coreCustomerContrlEventlist)) {
			for (CoreCustomerContrlEvent coreCustomerContrlEvent : coreCustomerContrlEventlist) {
				boolean flag = true;
				// list去重复，内部重写equals
				if (null != list && !list.isEmpty()) {
					for (CoreCustomerContrlEvent coreCustomerContrlEvent1 : list) {
						if (coreCustomerContrlEvent.getEventNo().equals(coreCustomerContrlEvent1.getEventNo())) {
							flag = false;
						}
					}
				}
				if (flag) {
					list.add(coreCustomerContrlEvent);
				}
			}
		}
		return list;
	}

	/**
	 * 将管控层级,对应的取值更新
	 * 
	 * @param blockCodeScope
	 * @return
	 * @throws Exception
	 */
	public int blockCodeScopeToInt(String blockCodeScope) throws Exception {
		int blockCodeScopeInt = 0;
		// 前封锁码比较值
		if (blockCodeScope.equals("C")) {
			blockCodeScopeInt = C;
		} else if (blockCodeScope.equals("P")) {
			blockCodeScopeInt = P;
		} else if (blockCodeScope.equals("G")) {
			blockCodeScopeInt = G;
		} else if (blockCodeScope.equals("M")) {
			blockCodeScopeInt = M;
		} else if (blockCodeScope.equals("A")) {
			blockCodeScopeInt = A;
		}
		return blockCodeScopeInt;
	}

	/**
	 * 
	 * @param blockCodeScope
	 * @param levelCode
	 * @param currencyCode
	 * @param customerNo
	 * @param corporation
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addAuthCardIssueExcptBean(String blockCodeScope, String levelCode, String currencyCode,
			String customerNo, String corporation, String authDenyIdentify, String operationMode,String spEventNo) throws Exception {

		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
		// 业务类型 - A
		if (Constant.CONTROL_A.equals(blockCodeScope)) {
			// 可以获取交易识别码，业务项目
			List<CoreBusTypeTransIdRel> ListCoreBusTypeTransIdRel = httpQueryService
					.queryBusinessTypeTransIden(operationMode, levelCode);
			if (null != ListCoreBusTypeTransIdRel && !ListCoreBusTypeTransIdRel.isEmpty()) {
				for (CoreBusTypeTransIdRel coreBusTypeTransIdRel : ListCoreBusTypeTransIdRel) {
					AuthCardIssueExcptBean authCardIssueExcptBean = new AuthCardIssueExcptBean();
					authCardIssueExcptBean.setCustomerNo(customerNo);
					// 业务项目
					authCardIssueExcptBean.setCurrencyCode(currencyCode);
					authCardIssueExcptBean.setTransIdentifiNo(coreBusTypeTransIdRel.getTransIdentifiNo());
					authCardIssueExcptBean.setCorporation(corporation);
					authCardIssueExcptBean.setAuthDataSynFlag("1");
					authCardIssueExcptBean.setAuthResp(authDenyIdentify);
					if(spEventNo.contains("SP.01") || spEventNo.contains("SP.80") || spEventNo.contains("SP.86")){
						authCardIssueExcptBean.setModifyType("ADD");
					}else if(spEventNo.contains("SP.11") || spEventNo.contains("SP.81") || spEventNo.contains("SP.87")){
						authCardIssueExcptBean.setModifyType("DEL");
					}else{
						authCardIssueExcptBean.setModifyType("");
					}
					triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authCardIssueExcptBean);
				}
			}
		} else if (Constant.CONTROL_M.equals(blockCodeScope)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(levelCode);
			coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(customerNo);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00014", "媒介基本");
			}
			AuthCardIssueExcptBean authCardIssueExcptBean = new AuthCardIssueExcptBean();
			authCardIssueExcptBean.setCustomerNo(customerNo);
			authCardIssueExcptBean.setCorporation(corporation);
			authCardIssueExcptBean.setExternalIdentificationNo(coreMediaBasicInfo.getExternalIdentificationNo());
			authCardIssueExcptBean.setAuthResp(authDenyIdentify);
			authCardIssueExcptBean.setAuthDataSynFlag("1");
			if(spEventNo.contains("SP.01") || spEventNo.contains("SP.80") || spEventNo.contains("SP.86")){
				authCardIssueExcptBean.setModifyType("ADD");
			}else if(spEventNo.contains("SP.11") || spEventNo.contains("SP.81") || spEventNo.contains("SP.87")){
				authCardIssueExcptBean.setModifyType("DEL");
			}else{
				authCardIssueExcptBean.setModifyType("");
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authCardIssueExcptBean);
		} else if (Constant.CONTROL_C.equals(blockCodeScope)) {
			AuthCardIssueExcptBean authCardIssueExcptBean = new AuthCardIssueExcptBean();
			authCardIssueExcptBean.setCustomerNo(customerNo);
			authCardIssueExcptBean.setCorporation(corporation);
			authCardIssueExcptBean.setAuthResp(authDenyIdentify);
			authCardIssueExcptBean.setAuthDataSynFlag("1");
			if(spEventNo.contains("SP.01") || spEventNo.contains("SP.80") || spEventNo.contains("SP.86")){
				authCardIssueExcptBean.setModifyType("ADD");
			}else if(spEventNo.contains("SP.11") || spEventNo.contains("SP.81") || spEventNo.contains("SP.87")){
				authCardIssueExcptBean.setModifyType("DEL");
			}else{
				authCardIssueExcptBean.setModifyType("");
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authCardIssueExcptBean);
		} else if (Constant.CONTROL_P.equals(blockCodeScope)) {
			AuthCardIssueExcptBean authCardIssueExcptBean = new AuthCardIssueExcptBean();
			authCardIssueExcptBean.setCustomerNo(customerNo);
			authCardIssueExcptBean.setCorporation(corporation);
			authCardIssueExcptBean.setProductObjectCode(levelCode);
			authCardIssueExcptBean.setAuthResp(authDenyIdentify);
			authCardIssueExcptBean.setAuthDataSynFlag("1");
			if(spEventNo.contains("SP.01") || spEventNo.contains("SP.80") || spEventNo.contains("SP.86")){
				authCardIssueExcptBean.setModifyType("ADD");
			}else if(spEventNo.contains("SP.11") || spEventNo.contains("SP.81") || spEventNo.contains("SP.87")){
				authCardIssueExcptBean.setModifyType("DEL");
			}else{
				authCardIssueExcptBean.setModifyType("");
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authCardIssueExcptBean);
		} else if (Constant.CONTROL_G.equals(blockCodeScope)) {
			AuthCardIssueExcptBean authCardIssueExcptBean = new AuthCardIssueExcptBean();
			authCardIssueExcptBean.setCustomerNo(customerNo);
			authCardIssueExcptBean.setCorporation(corporation);
			authCardIssueExcptBean.setAuthResp(authDenyIdentify);
			authCardIssueExcptBean.setAuthDataSynFlag("1");
			if(spEventNo.contains("SP.01") || spEventNo.contains("SP.80") || spEventNo.contains("SP.86")){
				authCardIssueExcptBean.setModifyType("ADD");
			}else if(spEventNo.contains("SP.11") || spEventNo.contains("SP.81") || spEventNo.contains("SP.87")){
				authCardIssueExcptBean.setModifyType("DEL");
			}else{
				authCardIssueExcptBean.setModifyType("");
			}
			triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authCardIssueExcptBean);
		}
		return triggerEventParams;
	}

	/**
	 * 重新计算生成管控项目
	 * 
	 * @param operationMode
	 * @param effectivenessCodeType
	 * @param effectivenessCodeScene
	 * @param effectivenessCodeScope
	 * @param levelCode
	 * @param currencyCode
	 * @param customerNo
	 * @param corporation
	 * @param eventCommAreaNonFinance
	 * @param operationDate
	 * @param contrlSource
	 * @return
	 * @throws Exception
	 */
	public Object delControlProjectNo(String operationMode, String effectivenessCodeType, String effectivenessCodeScene,
			String effectivenessCodeScope, String levelCode, String currencyCode, String customerNo, String corporation,
			EventCommAreaNonFinance eventCommAreaNonFinance, String operationDate, String contrlSource,String spEventNo)
			throws Exception {
		// ********************************* 同步授权封锁码
		// 查询封锁码关联管控视图表
		List<CoreEffectiveCdeCntrlProg> coreEffectiveCdeCntrlProgList1 = httpQueryService
				.queryCoreEffectivenessCodeRelateControlItemList(operationMode, effectivenessCodeType,
						effectivenessCodeScene,ProjectType.CONTROL_PROJECT.getValue());
		// 同步授权
		List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
		// 判断是否同步授权封锁码
		boolean flag = false;
		if (null != coreEffectiveCdeCntrlProgList1
				&& !coreEffectiveCdeCntrlProgList1.isEmpty()) {
			for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList1) {
				// 管控项目
				String controlProjectNo = coreEffectiveCdeCntrlProg.getControlProjectNo();
				// 查询管控项目表
				List<CoreControlProject> coreControlProjectList = httpQueryService
						.queryCoreControlProjectList(operationMode, preControlCategory_A);
				// 管控项目
				if (null != coreControlProjectList && !coreControlProjectList.isEmpty()) {
					for (CoreControlProject coreControlProjectList2 : coreControlProjectList) {
						if (controlProjectNo.equals(coreControlProjectList2.getControlProjectNo())) {
							flag = true;
							String authDenyIdentify = coreControlProjectList2.getAuthDenyIdentify();
							// 发卡例外同步封锁码
							Map<String, Object> triggerEventParams = this.addAuthCardIssueExcptBean(effectivenessCodeScope,
									levelCode, currencyCode, customerNo, corporation, "", operationMode,spEventNo);
							eventCommAreaTriggerEventList.add(triggerEventParams);
						}
					}
				}
			}
		}
		if (!flag) {
			eventCommAreaNonFinance.setWhetherProcess("1");
		} else {
			eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		}
		// ********************************* 同步授权封锁码
		// 删除
		CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
		coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerContrlViewSqlBuilder.andContrlSerialNoEqualTo(000);
		List<CoreCustomerContrlView> coreCustomerContrlViewList = coreCustomerContrlViewDao
				.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
		if (null != coreCustomerContrlViewList && !coreCustomerContrlViewList.isEmpty()) {
			// 将管控层级增加 +1 处理
			for (CoreCustomerContrlView coreCustomerContrlView : coreCustomerContrlViewList) {
				// 查询最大数
				CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderSize = new CoreCustomerContrlViewSqlBuilder();
				coreCustomerContrlViewSqlBuilderSize.andCustomerNoEqualTo(customerNo);
				coreCustomerContrlViewSqlBuilderSize
						.andControlProjectNoEqualTo(coreCustomerContrlView.getControlProjectNo());
				coreCustomerContrlViewSqlBuilderSize.orderByContrlSerialNo(true);
				List<CoreCustomerContrlView> coreCustomerContrlViewListSize = coreCustomerContrlViewDao
						.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilderSize);
				int contrlSerialNoSize = coreCustomerContrlViewListSize.get(0).getContrlSerialNo();
				coreCustomerContrlView.setContrlSerialNo(contrlSerialNoSize + 1);
				coreCustomerContrlView.setVersion(coreCustomerContrlView.getVersion() + 1);
				CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderOld = new CoreCustomerContrlViewSqlBuilder();
				coreCustomerContrlViewSqlBuilderOld.andCustomerNoEqualTo(customerNo);
				coreCustomerContrlViewSqlBuilderOld.andContrlSerialNoEqualTo(000);
				coreCustomerContrlViewSqlBuilderOld
						.andControlProjectNoEqualTo(coreCustomerContrlView.getControlProjectNo());
				coreCustomerContrlView.setContrlEndDate(operationDate);
				coreCustomerContrlView.setGmtCreate(DateUtil.getDate());
				coreCustomerContrlViewSqlBuilderOld.andIdEqualTo(coreCustomerContrlView.getId());
				int result = coreCustomerContrlViewDao.updateBySqlBuilderSelective(coreCustomerContrlView,
						coreCustomerContrlViewSqlBuilderOld);
				if (result != 1) {
					throw new BusinessException("CUS-00012", "客户管控视图");
				}
			}
		}
		// 从新计算视图
		CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
		coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
		List<CoreCustomerEffectiveCode> coreCustomerEffectiveCodeList = coreCustomerEffectiveCodeDao
				.selectListBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
		// 查询管控视图
		if (null != coreCustomerEffectiveCodeList && !coreCustomerEffectiveCodeList.isEmpty()) {
			for (CoreCustomerEffectiveCode CoreCustomerEffectiveCode : coreCustomerEffectiveCodeList) {
				// 查询封锁码关联管控项目表
				List<CoreEffectiveCdeCntrlProg> coreEffectiveCdeCntrlProgList = httpQueryService
						.queryCoreEffectivenessCodeRelateControlItemList(operationMode,
								CoreCustomerEffectiveCode.getEffectivenessCodeType(),
								CoreCustomerEffectiveCode.getEffectivenessCodeScene(),ProjectType.CONTROL_PROJECT.getValue());
				// 查询封锁码表
				CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(operationMode,
						CoreCustomerEffectiveCode.getEffectivenessCodeType(), CoreCustomerEffectiveCode.getEffectivenessCodeScene());
				
				// 不存在管控项目
				if (null == coreEffectiveCdeCntrlProgList
						|| coreEffectiveCdeCntrlProgList.isEmpty()) {
					// 运营模式,类别,场景查询封锁码关联管控项目表不存在,则跳出,不更新
					continue;
				}
				for (CoreEffectiveCdeCntrlProg coreEffectiveCdeCntrlProg : coreEffectiveCdeCntrlProgList) {
					CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderAdd = new CoreCustomerContrlViewSqlBuilder();
					coreCustomerContrlViewSqlBuilderAdd.andCustomerNoEqualTo(customerNo);
					coreCustomerContrlViewSqlBuilderAdd.andContrlSerialNoEqualTo(000);
					coreCustomerContrlViewSqlBuilderAdd
							.andControlProjectNoEqualTo(coreEffectiveCdeCntrlProg.getControlProjectNo());
					coreCustomerContrlViewSqlBuilderAdd
							.andLevelCodeEqualTo(CoreCustomerEffectiveCode.getSceneTriggerObjectCode());
					CoreCustomerContrlView coreCustomerContrlView = coreCustomerContrlViewDao
							.selectBySqlBuilder(coreCustomerContrlViewSqlBuilderAdd);
					if (null == coreCustomerContrlView) {
						// 新增，增加一个
						CoreCustomerContrlView coreCustomerContrlViewInsert = new CoreCustomerContrlView();
						coreCustomerContrlViewInsert.setCustomerNo(customerNo);
						coreCustomerContrlViewInsert.setContrlSource(contrlSource);
						// 管控序号
						coreCustomerContrlViewInsert.setContrlSerialNo(000);
						coreCustomerContrlViewInsert.setEffectivenessCodeScene(
								Integer.valueOf(CoreCustomerEffectiveCode.getEffectivenessCodeScene()));
						coreCustomerContrlViewInsert
								.setEffectivenessCodeType(CoreCustomerEffectiveCode.getEffectivenessCodeType());
						coreCustomerContrlViewInsert
								.setContrlLevel(coreEffectivenessCode.getEffectivenessCodeScope());
						coreCustomerContrlViewInsert.setContrlStartDate(CoreCustomerEffectiveCode.getSettingDate());
						coreCustomerContrlViewInsert.setCurrencyCode(CoreCustomerEffectiveCode.getCurrencyCode());
						coreCustomerContrlViewInsert.setGmtCreate(DateUtil.getDate());
						coreCustomerContrlViewInsert.setId(RandomUtil.getUUID());
						coreCustomerContrlViewInsert
								.setLevelCode(CoreCustomerEffectiveCode.getSceneTriggerObjectCode());
						coreCustomerContrlViewInsert.setVersion(1);
						// 管控项目
						coreCustomerContrlViewInsert
								.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						coreCustomerContrlViewDao.insert(coreCustomerContrlViewInsert);
					} else {
						CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderSize = new CoreCustomerContrlViewSqlBuilder();
						coreCustomerContrlViewSqlBuilderSize.andCustomerNoEqualTo(customerNo);
						coreCustomerContrlViewSqlBuilderSize
								.andControlProjectNoEqualTo(coreCustomerContrlView.getControlProjectNo());
						coreCustomerContrlViewSqlBuilderSize
								.andLevelCodeEqualTo(CoreCustomerEffectiveCode.getSceneTriggerObjectCode());
						coreCustomerContrlViewSqlBuilderSize.orderByContrlSerialNo(true);
						List<CoreCustomerContrlView> coreCustomerContrlViewListSize = coreCustomerContrlViewDao
								.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilderSize);
						int contrlSerialNo = coreCustomerContrlViewListSize.get(0).getContrlSerialNo();
						coreCustomerContrlView.setVersion(coreCustomerContrlView.getVersion() + 1);
						coreCustomerContrlView
								.setEffectivenessCodeType(CoreCustomerEffectiveCode.getEffectivenessCodeType());
						coreCustomerContrlView.setEffectivenessCodeScene(
								Integer.valueOf(CoreCustomerEffectiveCode.getEffectivenessCodeScene()));
						coreCustomerContrlView
								.setContrlLevel(coreEffectivenessCode.getEffectivenessCodeScope());
						coreCustomerContrlView.setContrlSerialNo(contrlSerialNo + 1);
						CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilderUp = new CoreCustomerContrlViewSqlBuilder();
						coreCustomerContrlViewSqlBuilderUp.andCustomerNoEqualTo(customerNo);
						coreCustomerContrlViewSqlBuilderUp
								.andControlProjectNoEqualTo(coreCustomerContrlView.getControlProjectNo());
						coreCustomerContrlViewSqlBuilderUp.andIdEqualTo(coreCustomerContrlView.getId());
						int result = coreCustomerContrlViewDao.updateBySqlBuilderSelective(coreCustomerContrlView,
								coreCustomerContrlViewSqlBuilderUp);
						if (result != 1) {
							throw new BusinessException("CUS-00012", "客户封锁码表");
						}
						// 管控序号000,新增一条
						CoreCustomerContrlView coreCustomerContrlViewInsert = new CoreCustomerContrlView();
						coreCustomerContrlViewInsert.setCustomerNo(customerNo);
						coreCustomerContrlViewInsert.setContrlSource(contrlSource);
						// 管控序号
						coreCustomerContrlViewInsert.setContrlSerialNo(000);
						coreCustomerContrlViewInsert.setEffectivenessCodeScene(
								Integer.valueOf(CoreCustomerEffectiveCode.getEffectivenessCodeScene()));
						coreCustomerContrlViewInsert
								.setEffectivenessCodeType(CoreCustomerEffectiveCode.getEffectivenessCodeType());
						coreCustomerContrlViewInsert
								.setContrlLevel(coreEffectivenessCode.getEffectivenessCodeScope());
						coreCustomerContrlViewInsert.setContrlStartDate(CoreCustomerEffectiveCode.getSettingDate());
						coreCustomerContrlViewInsert.setCurrencyCode(CoreCustomerEffectiveCode.getCurrencyCode());
						coreCustomerContrlViewInsert.setGmtCreate(DateUtil.getDate());
						coreCustomerContrlViewInsert.setId(RandomUtil.getUUID());
						coreCustomerContrlViewInsert
								.setLevelCode(CoreCustomerEffectiveCode.getSceneTriggerObjectCode());
						coreCustomerContrlViewInsert.setVersion(1);
						// 管控项目
						coreCustomerContrlViewInsert
								.setControlProjectNo(coreEffectiveCdeCntrlProg.getControlProjectNo());
						coreCustomerContrlViewDao.insert(coreCustomerContrlViewInsert);
					}
				}
			}
			// 重新计算,重新生成客户管控事件
			this.createCustControlEvent(customerNo, operationMode);
			return eventCommAreaNonFinance;
		} else {
			// 重新计算,重新生成客户管控事件
			this.createCustControlEvent(customerNo, operationMode);
			return eventCommAreaNonFinance;
		}
	}
	
	@SuppressWarnings("unused")
	private void triggerEventMode(String triggerEventNo,String effectivenessCodeScope,String customerNo,String levelCode,String currencyCode,
			CoreEventActivityRel dto,String operationMode,String controlProjectNo)throws Exception {
			CoreEvent coreEvent = httpQueryService.queryEvent(triggerEventNo);
			String eventType = coreEvent.getEventType();
			String url = "";
			String params = "";
	        if (TriggerEventType.MONY.getValue().equals(eventType)) {
				url = cardUrl;
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommCustId(customerNo);
				eventCommArea.setEcommOperMode(operationMode);
				eventCommArea.setEcommControlProjectNo(controlProjectNo);
				eventCommArea.setEcommLevelCode(levelCode);
				eventCommArea.setEcommSceneTriggerObject(effectivenessCodeScope);
				if ("A".equals(effectivenessCodeScope)) {
					eventCommArea.setEcommTransPostingCurr(currencyCode);
				} 
				params = JSON.toJSONString(eventCommArea, SerializerFeature.DisableCircularReferenceDetect);
			} else if (TriggerEventType.AUTH.getValue().equals(eventType)) {
				url = authUrl;
				Map<String, Object> authDataSynMap = new HashMap<String, Object>(9);
				authDataSynMap.put("customerNo", customerNo);
					if ("P".equals(effectivenessCodeScope)) {
						authDataSynMap.put("productObjectCode", levelCode);
					} else if ("M".equals(effectivenessCodeScope)) {
						authDataSynMap.put("mediaUnitCode", levelCode);
					} else if ("A".equals(effectivenessCodeScope)) {
						 authDataSynMap.put("accountId", levelCode);
					     authDataSynMap.put("currencyCode", currencyCode);
					} else if ("G".equals(effectivenessCodeScope)) {
						authDataSynMap.put("businessProgramNo", levelCode);
					}
		        authDataSynMap.put("authDataSynFlag", "1");
				params = JSON.toJSONString(authDataSynMap, SerializerFeature.DisableCircularReferenceDetect);
			} else if (TriggerEventType.NMNY.getValue().equals(eventType)) {
				url = nonFinUrl;
				EventCommAreaNonFinance eventCommAreaNonFinance= new EventCommAreaNonFinance();
				eventCommAreaNonFinance.setCustomerNo(customerNo);
				if ("P".equals(effectivenessCodeScope)) {
					eventCommAreaNonFinance.setProductObjectCode(levelCode);
				} else if ("M".equals(effectivenessCodeScope)) {
					eventCommAreaNonFinance.setMediaUnitCode(levelCode);
				} else if ("A".equals(effectivenessCodeScope)) {
					eventCommAreaNonFinance.setAccountId(levelCode);
					eventCommAreaNonFinance.setCurrencyCode(currencyCode);
				} else if ("G".equals(effectivenessCodeScope)) {
					eventCommAreaNonFinance.setBusinessProgramNo(levelCode);
				}
				params = JSON.toJSONString(eventCommAreaNonFinance, SerializerFeature.DisableCircularReferenceDetect);
			}
	        String triggerEventInteractMode = dto.getTriggerEventInteractMode();
	        if (StringUtil.isNotBlank(triggerEventInteractMode)) {
				if ("SYNC".equals(triggerEventInteractMode)) {
					// 同步触发，需等待触发事件处理结束
					RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
					HttpHeaders headers = new HttpHeaders();
					MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
					headers.setContentType(type);
					HttpEntity<String> entity = new HttpEntity<String>(params, headers);
					String response = restTemplate.postForObject(url + triggerEventNo, entity, String.class);
					ResponseVO returnVO = JSON.parseObject(response, ResponseVO.class,
							Feature.DisableCircularReferenceDetect);
					if (!Constant.SUCCESS_CODE.equals(returnVO.getReturnCode())) {
						throw new BusinessException(returnVO.getReturnMsg());
					}
				} else if ("ASYNC".equals(triggerEventInteractMode)) {
					// 异步触发，无需等待触发事件处理结束
					dto.setTriggerNo(triggerEventNo);
					ChildTaskStartUtil.getInstance().startChildTask(dto.getEventNo(), dto, params, url);
				}
			}
	    }

}
