package com.tansun.ider.model.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.model.BusinessProgramNoCycleDays;
import com.tansun.ider.model.ResultGns;
import com.tansun.ider.model.X5120VO;
import com.tansun.ider.service.business.EventCommArea;

/**
 * @version:1.0
 * @Description: 媒介单元基本信息创建
 * @author: wt
 */
public class X5051BO {
	
	/** cvv**/
	private String cvv;
	/** cvv2**/
	private String cvv2;
	/**ICVV**/
	private String icvv;
	/**主证件类型 */
	private String supIdType;
	/** 主证件号码 */
	private String supIdNumber;
	/** 申请类型  1-单申主卡(主副标识必须为主卡) 2-单申附卡(主副标识必须为附卡) 3-主副同申主卡(主副标识必须为主卡) 3-主副同申主卡(主副标识必须为附属卡) */
	protected String applicationCardType;
    /** 生效码类别 [1,0] */
    protected String effectivenessCodeType;
    /** 生效码序号 [10,0] */
    protected Integer effectivenessCodeScene;
    /** 描述 [50,0] */
    protected String effectivenessCodeDesc;
    /** 生效码范围 C-客户/G-业务项目/A-业务类型/P-产品/M-媒介/B-余额对象/O-按需指定； [50,0] Not NULL */
    protected String effectivenessCodeScope;
    /** 场景触发对象  C-客户,G-业务项目,A-业务类型,P-产品,M-媒介,O-按需指定 [1,0] */
    protected String sceneTriggerObject;
	private String corporation;
	private List<BusinessProgramNoCycleDays> businessProgramNoCycleDaysList;
    /** 购汇还款标志 */
    private String exchangePaymentFlag;
    private String businessProjectCode;
    private String isNew;
    /** 法人 */
    private String corporationEntityNo;
    private static final long serialVersionUID = 7584743553790639100L;
    /** 是否主控调起 1:不执行 */
    private String whetherProcess;
    /** 激活方式 */
    private String activationMode;
    /** 服务代码 */
    private String serviceCode;
    /** 当前日志标志 A/B [1,0] */
    protected String currLogFlag;
    /** 操作员Id */
    private String operatorId;
    /** 封锁码管控列表 */
    private List<CoreCustomerEffectiveCode> listCoreCustomerEffectiveCode;
    /** 新增标识 */
    private String blockFlag;
    // 触发事件编号和业务类型
    private List<Map<String, Object>> eventCommAreaTriggerEventList;
    private EventCommArea eventCommArea;
    /** 介质单元代码 */
    private String ecommMediaUnit;
    /** 产品形式代码 [18,0] Not NULL */
    protected String productForm;
    /** 产品持有人代码 [12,0] Not NULL */
    protected String productHolderNo;
    /** 媒介形式 R ： 实体 V：虚拟 [1,0] Not NULL */
    protected String mediaObjectForm;
    /** 媒介持有人代码 [12,0] Not NULL */
    protected String mediaHolderNo;
    /** 失效原因字段 TRF：转卡 EXP：到期 BRK：毁损 RNA:续卡未激活 CLS：关闭 */
    private String invalidReason;
    /** 失效原因字段 TRF：转卡 EXP：到期 BRK：毁损 CLS：关闭 */
    private String invalidReasonOld;
    /** 针对金融工程分页查询 第几页 */
    private int pageNum;
    /** 每页有多少条记录 */
    private int pageSize;
    /** 从第几天开始查询 */
    private int indexNo;
    // 授权相应
    private String authResp;
    /** 授权同步说明 */
    private String excptDesc;
    /** 无效标识 */
    private String invalidFlag;
    //上封锁码加的事件ID，X5185接值用
    private String spEventNo;
    // 固定参数
    private String eventNo; // 事件ID
    private String occurrDate; // 发生日期
    private String occurrTime; // 发生时间
    /** 客户号 */
    private String customerNo;
    /** 所属机构 */
    private String institutionId;
    /** 运行模式 */
    private String operationMode;
    /** 客户姓名 */
    private String customerName;
    /** 证件类型 */
    private String idType;
    /** 证件号码 */
    private String idNumber;
    /** 城市 */
    private String city;
    /** 个人公司标识 */
    private String customerType;
    /** 账单日 */
    private String billDay;
    /** 客户状态 */
    private String customerStatus;
    /** 地址信息 */
    private List<CoreCustomerAddr> coreCoreCustomerAddrs;
    /** 交易识别码 */
    private String transIdentifiNo;
    /** 业务项目 */
    private String businessProgram;
    /** 业务项目代码 */
    private String businessProgramNo;
    /** 标识是何场景下使用的地址 [10,0] */
    protected Integer type;
    /** 备注信息 */
    private String remarkInfo;
    /** 新增备注人员 */
    private String lastUpdateUserid;
    /** 序号 */
    private Integer serialNo;
    /** 新增备注日期 */
    private String lastUpdateDate;
    /** 币种 */
    private String currencyCode;
    /** 业务类型 */
    private String businessType;
    /** 产品对象代码 */
    private String productObjectCode;
    /** 定价区域 */
    private String pricingType;
    /** 标签号 */
    private String pricingTag;
    /** 标签生效日期 */
    private Date effectiveDate;
    /** 标签失效日期 */
    private Date tagExpirationDate;
    /** 余额类型 */
    private String balanceType;
    /** 分行号 */
    private String branchNumber;
    /** 生日 yyyy-MM-dd */
    private String dateOfBirth;
    /** 手机号码 */
    private String mobilePhone;
    /** 邮编 */
    private String zipCode;
    /** 家庭电话 */
    private String homePhone;
    /** 公司电话 */
    private String companyPhone;
    /** 联络人电话 */
    private String contactPhone;
    /** 联络人姓名 */
    private String contactName;
    /** 成为会员年份 */
    private Integer memberSince;
    /** 客户来源码 */
    private String customerSource;
    /** 行为得分 */
    private Integer behaviorScore;
    /** 客户等级 */
    private String customerLevel;
    /** 年收入 */
    private Integer annualIncome;
    /** 性别 */
    private String sexCode;
    /** 住宅性质 */
    private String residencyStatus;
    /** 婚姻状况 */
    private String maritalStatus;
    /** 工作行业代码 */
    private String occupationCode;
    /** 职务级别代码 */
    private String postRankCode;
    /** 职称代码 */
    private String titleCode;
    /** 工作年限 */
    private String periodOfOccupation;
    /** 兴趣爱好 */
    private String hobby;
    /** 担保人标识 */
    private String guarantorFlag;
    /** 营销员代码 */
    private String marketerCode;
    /** 持卡人的社会保障号类型 */
    private String socialSecurityId;
    /** 社保账号 */
    private String socialSecurityNumber;
    /** 备注信息 */
    private List<CoreCustomerRemarks> coreCustomerRemarkss;
    /** 定价标签信息 */
    private List<CoreCustomerBusinessType> coreCustomerBusinessTypes;
    /** 产品单元代码 [18,0] Not NULL */
    private String productUnitCode;
    /** 产品线代码 [9,0] */
    private String productLineCode;
    /** 入账币种1 */
    private String postingCurrency1;
    /** 入账币种2 */
    private String postingCurrency2;
    /** 入账币种3 */
    private String postingCurrency3;
    /** 入账币种4 */
    private String postingCurrency4;
    /** 入账币种5 */
    private String postingCurrency5;
    /** 入账币种6 */
    private String postingCurrency6;
    /** 入账币种7 */
    private String postingCurrency7;
    /** 入账币种8 */
    private String postingCurrency8;
    /** 入账币种9 */
    private String postingCurrency9;
    /** 入账币种10 */
    private String postingCurrency10;
    /** 联名号 */
    private String coBrandedNo;
    /** 媒介单元代码 */
    private String mediaUnitCode;
    /** 媒介对象 */
    private String mediaObjectCode;
    /** 外部识别号 */
    private String externalIdentificationNo;
    /** 主客户代码 */
    private String mainCustomerNo;
    /** 副客户代码 */
    private String subCustomerNo;
    /** 主附标识 1：主卡 2：附属卡 */
    private String mainSupplyIndicator;
    /** 媒介使用者姓名 */
    private String mediaUserName;
    /** 进件人员编号 */
    private String applicationStaffNo;
    /** 申请书编号 */
    private String applicationNumber;
    /** 有效期 MMYY */
    private String expirationDate;
    /** 上一有效期 MMYY */
    private String previousExpirationDate;
    /** 密码函领取标志 */
    private String pinDispatchMethod;
    /** 媒介领取标志 */
    private String mediaDispatchMethod;
    /** 状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
    private String statusCode;
    /** 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活 [1,0] */
    private String activationFlag;
    /** 激活日期 */
    private String activationDate;
    /** 新建日期 */
    private String createDate;
    // 媒介制卡信息
    /** 制卡信息一 */
    private String embosserName1;
    /** 制卡信息二 */
    private String embosserName2;
    /** 制卡信息三 */
    private String embosserName3;
    /** 制卡信息四 */
    private String embosserName4;
    /** 制卡请求 0：无请求 1：新发卡制卡 2：到期续卡制卡 3：毁损补发制卡 4：挂失换卡制卡 [1,0] Not NULL */
    private String productionCode;
    /** 制卡请求日期 yyyy-MM-dd */
    private String productionDate;
    /** 上一制卡请求 */
    private String previousProductionCode;
    /** 上一制卡请求日期 */
    private String previousProductionDate;
    /** 卡版的样式代码 */
    private String formatCode;
    /** 运营核算币种 */
    private String accountCurrency;
    /** 模式名称 */
    private String modeName;
    /** 上一处理日期 */
    private String lastProcessDate;
    /** 当前处理日期 */
    private String currProcessDate;
    /** 下一处理日期 */
    private String nextProcessDate;
    /** 营业日期 */
    private String operationDate;
    /** 额度树编号 */
    private String creditTreeId;
    /** 溢缴款业务类型 */
    private String excessPymtBusinessType;
    /** 定价标签信息 */
    private List<CoreMediaLabelInfo> coreMediaLabelInfos;
    /** 绑定识别号 */
    private String bindId;
    /** 绑定日期 yyyy-MM-dd */
    private String bindDate;
    /** 解绑日期 yyyy-MM-dd */
    private String unbindDate;
    /** 产品线账单日表中 */
    private String nextBillDate;
    // 是否转卡
    private String isTransferCard;
    // 非金融日志记录 主键
    private String nonLogId;
    // Id 主键
    private String id;
    /** 账户代码 */
    private String accountId;
    /** 所属业务类型 */
    private String businessTypeCode;
    /** 所属机构号 */
    private String organNo;
    /** 账户组织形式 R：循环类账户 T：交易 */
    private String accountOrganizeTyp;
    /** 账户性质 D：借记账户 C ： 贷记账户 */
    private String businessDebitCreditCode;
    /** 周期模式标志 Y：周期模式 N：非周期模式 */
    private String cycleModeFlag;
    /** 状态更新日期 */
    private String statusUpdateDate;
    /** 关闭日期 [10,0] */
    private String closedDate;
    /** 计息处理日 */
    private String interestProcessDate;
    /** 最后还款日 */
    private String paymentDueDate;
    /** 滞纳金产生日 */
    private String delinquencyDate;
    /** 核算状态码 */
    private String accountingStatusCode;
    /** 核算状态日期 */
    private String accountingStatusDate;
    /** 上一核算状态码 */
    private String prevAccountingStatusCode;
    /** 上一核算状态日期 */
    private String prevAccountingStatusDate;
    /** 约定扣款方式 1：账单余额 2：最低还款 */
    private String directDebitType;
    /** 逾期状态 */
    private String cycleDue;
    /** 上一逾期状态 */
    private String prevCycleDue;
    /** ABS状态，资产担保证券或资产支撑证券 */
    private String absStatus;
    /** 当前周期号 */
    private Integer currentCycleNumber;
    /** 最低还款总额 */
    private BigDecimal totalAmtDue;
    /** 升序，降序标签 */
    private String flag;
    /** 余额单元代码 [18,0] Not NULL */
    protected String balanceUnitCode;
    /** 建立周期号 [10,0] */
    protected Integer cycleNumber;
    /** 余额 [18,0] */
    protected BigDecimal balance;
    /** 计息年利率 [8,7] */
    protected BigDecimal annualInterestRate;
    /** 累计利息 [19,0] */
    protected BigDecimal accumulatedInterest;
    /** 当期最低还款 [18,0] */
    protected BigDecimal currentMinPayment;
    /** 余额对象 [9,0] */
    protected String balanceObjectCode;
    /** 下一结息日期 yyyy-MM-dd [10,0] */
    protected String nextInterestBillingDate;
    /** 上一结息处理日 yyyy-MM-dd [10,0] */
    protected String prevInterestBillingDate;
    /** 资产属性 [2,0] */
    protected String assetProperties;
    /** 余额结束日期 yyyy-MM-dd [10,0] */
    protected String endDate;
    /** 上一计息年利率 [8,7] */
    protected BigDecimal lastInterestRate;
    /** 上一年利率失效日期 [10,0] */
    protected String lastInterestRateExpirDate;
    /** 首次结息周期号 [10,0] */
    protected Integer firstBillingCycle;
    /** 记录序号 [10,0] Not NULL */
    private Integer recordNumber;
    /** 卡组织 [1,0] Not NULL */
    private String cardAssociations;
    /** 磁道1信息 [79,0] */
    private String magneticChannel1;
    /** 磁道2信息 [40,0] */
    private String magneticChannel2;
    /** 磁道3信息 [113,0] */
    private String magneticChannel3;
    /** 持卡人姓名 [30,0] */
    private String cardholderName;
    /** 邮编 : 邮编 [9,0] */
    private String postCode;
    /** 联系电话 : 联系电话 [18,0] */
    private String phoneNumber;
    /** 媒介形式 [1,0] Not NULL */
    private String mediaObjectType;
    /** 预留Buffer域 [50,0] */
    private String extBuffer;
    /** 联系地址 [90,0] */
    private String contactAddress;
    /** 联系邮编 [9,0] */
    private String contactPostCode;
    /** 指定电话 [18,0] */
    private String contactMobilePhone;
    /** 客户业务项目 */
    private List<CoreCustomerBillDay> listCoreCustomerBillDays;
    /** 产品线统一日期 */
    private List<CoreCustomerUnifyInfo> listCoreCustomerUnifyInfos;
    /** 媒介绑定参数 */
    private List<CoreMediaBind> listCoreMediaBinds;
    /** 封锁码类别 [1,0] Not NULL */
    protected String blockCodeType;
    /** 封锁码场景序号 [10,0] Not NULL */
    protected Integer blockCodeScene;
    /** 优先级 [10,0] */
    protected Integer blockCodePrioriy;
    /** 封锁码场景描述 [50,0] */
    protected String blockCodeDesc;
    /** 封封锁范围 C：客户级A：业务类型级P：产品级M：媒介级 [50,0] */
    protected String blockCodeScope;
    protected List<CoreProduct> listCoreProducts;
    protected List<CoreMediaBasicInfo> listcoreMediaBasicInfo;
    private List<CoreAccount> listCoreAccount;
    /** 元件代码 */
    private String elementNo;
    /** 管控序号 */
    private String contrlSerialNo;
    /** 管控层级 C-客户级 A-业务类型级 P-产品级 M-媒介级 */
    private String contrlLevel;
    /** 层级代码 账户代码/产品单元代码/媒介单元代码 */
    private String levelCode;
    /** 管控来源 BLCK-封锁码 */
    private String contrlSource;
    /** 管控开始日期 */
    private String contrlStartDate;
    /** 管控结束日期 */
    private String contrlEndDate;
    /** 开始日期 */
    private String startDate;
    private List<X5120VO> listX5120VOs;
    /** 生命周期节点类型 */
    private String lifecycleNodeType;
    /** 约定扣款状态 0-未设置1-已设置 [1,0] */
    private String directDebitStatus;
    /** 约定扣款方式 0-最小还款1-全额还款 [1,0] */
    private String directDebitMode;
    /** 约定还款银行号 [7,0] */
    private String directDebitBankNo;
    /** 约定还款账户号 [25,0] */
    private String directDebitAccountNo;
    /** 系统单元 */
    private String systemUnitNo;
    /** 额度节点编号 */
    private String creditNodeNo;
    /** 授信金额 */
    private String creditLimit;
    /** 转出媒介代码 */
    private String transferMediaCode;
    /** 同步授权标志 */
    private String authDataSynFlag;
    /** 授信金额 */
    private BigDecimal permLimit;
    /** 消费验密标志 1：需要验密2：不需要验密 [1,0] */
    private String retailVerifyPswFlag;
    /** 消费免密金额 [18,0] */
    private BigDecimal retailVerifyPswAmount;
    /** 国家码 */
    private String country;
    /** 日志层级 C-客户级 A-账户级 P-产品级 M-媒介级 R-参数级 D-客户业务项目级 [4,0] Not NULL */
    private String logLevel;
    /** 还款选项 */
    private String paymentMark;
    /** 触发事件 */
    /** 修改字段名 [64,0] Not NULL */
    private String modifyFieldName;
    /** 活动编号 [8,0] Not NULL */
    private String activityNo;
    /** 登录名 */
    private String loginName;
    /** 访问类别 */
    private String modifyType;
    /** 管理员标识 */
    private String adminFlag;

    /** 贷款金额 */
    private String loanAmount;
    /** 贷款币种 */
    private String loanCurrencyCode;
    /** 还款方式 */
    private String repayMode;
    /** 贷款编号 */
    private String loanNumber;
    /** 贷款期限 */
    private String monthSupplyPeriod;
    /** 贷款期限单位 */
    private String loanTermUnit;
    /** 贷款起始日 */
    private String payLoanDate;
    /** 贷款到期日 */
    private String loanExpira;
    /** 收款账户号 */
    private String receiptAccount;
    /** 收款方行号 */
    private String ReceiptBranchNUM;
    /** 放款利率 */
    private String loanRate;
    /** 还款周期 */
    private String repayPrincipal;
    /** 还款日类型 */
    private String repaymentDateType;
    /** 指定还款日 */
    private String repayDay;
    /** 80 免息天数 */
    private String freeDays;
    /** 授信类型 */
    private String creditType;
    /** 媒介 */
    private Map<String, Object> coreMediaBasicInfoMap;
    /** 媒介记录表 */
    private List<ResultGns> coreMediaBasicInfoList;
    /** 额度生效日期 [10,0] */
    private String limitEffectvDate;
    /** 额度失效日期 [10,0] */
    private String limitExpireDate;
    // 预算单位信息建立--附加信息
    /** 预算管理层级 */
    private String manageLevelCode;
    /** 单位公务卡总授信额度 */
    private BigDecimal orgAllQuota;
    /** 个人公务卡最大授信额度 */
    private BigDecimal personMaxQuota;
    /** 预算单位编码--实际对应的是证件号码 */
    private String budgetOrgCode;
    /**
     * 账户信息表
     */
    /** 全局事件编号 */
    private String globalEventNo;
    private String credentialNumber;
    /** 账户组织形式 R：循环 [1,0] */
    protected String accountOrganForm;
    /** 资产属性，资产担保证券或资产支撑证券（Asset-backed security） 00-表内01-表外 [2,0] */
    //protected String absStatus;
    /** 宽限日 [10,0] */
    protected String graceDate;
    /** 当前余额 [18,0] */
    protected BigDecimal newBalance;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 资产转变方式:ABS-证券化 [3,0] */
    protected String capitalType;
    /** 资产转变阶段:PACK-封包,TRSF-转让,REPO-回购 [4,0] */
    protected String capitalStage;
    /** 资产转出机构代码 [25,0] */
    protected String capitalOrganizationCode;
    /** 资产转出机构名称 [120,0] */
    protected String capitalOrganizationName;
    /** 产品对象代码 [9,0] Not NULL 产品升降级所选新的产品对象*/
    private String productObjectCodeNew;
    /** 主产品形式代码 */
	private String mainProductForm;
	/** 设备编号 */
	private String deviceNumber;
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	public String getIcvv() {
		return icvv;
	}
	public void setIcvv(String icvv) {
		this.icvv = icvv;
	}
	public String getSupIdType() {
		return supIdType;
	}
	public void setSupIdType(String supIdType) {
		this.supIdType = supIdType;
	}
	public String getSupIdNumber() {
		return supIdNumber;
	}
	public void setSupIdNumber(String supIdNumber) {
		this.supIdNumber = supIdNumber;
	}
	public String getApplicationCardType() {
		return applicationCardType;
	}
	public void setApplicationCardType(String applicationCardType) {
		this.applicationCardType = applicationCardType;
	}
	public String getEffectivenessCodeType() {
		return effectivenessCodeType;
	}
	public void setEffectivenessCodeType(String effectivenessCodeType) {
		this.effectivenessCodeType = effectivenessCodeType;
	}
	public Integer getEffectivenessCodeScene() {
		return effectivenessCodeScene;
	}
	public void setEffectivenessCodeScene(Integer effectivenessCodeScene) {
		this.effectivenessCodeScene = effectivenessCodeScene;
	}
	public String getEffectivenessCodeDesc() {
		return effectivenessCodeDesc;
	}
	public void setEffectivenessCodeDesc(String effectivenessCodeDesc) {
		this.effectivenessCodeDesc = effectivenessCodeDesc;
	}
	public String getEffectivenessCodeScope() {
		return effectivenessCodeScope;
	}
	public void setEffectivenessCodeScope(String effectivenessCodeScope) {
		this.effectivenessCodeScope = effectivenessCodeScope;
	}
	public String getSceneTriggerObject() {
		return sceneTriggerObject;
	}
	public void setSceneTriggerObject(String sceneTriggerObject) {
		this.sceneTriggerObject = sceneTriggerObject;
	}
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public List<BusinessProgramNoCycleDays> getBusinessProgramNoCycleDaysList() {
		return businessProgramNoCycleDaysList;
	}
	public void setBusinessProgramNoCycleDaysList(List<BusinessProgramNoCycleDays> businessProgramNoCycleDaysList) {
		this.businessProgramNoCycleDaysList = businessProgramNoCycleDaysList;
	}
	public String getExchangePaymentFlag() {
		return exchangePaymentFlag;
	}
	public void setExchangePaymentFlag(String exchangePaymentFlag) {
		this.exchangePaymentFlag = exchangePaymentFlag;
	}
	public String getBusinessProjectCode() {
		return businessProjectCode;
	}
	public void setBusinessProjectCode(String businessProjectCode) {
		this.businessProjectCode = businessProjectCode;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getCorporationEntityNo() {
		return corporationEntityNo;
	}
	public void setCorporationEntityNo(String corporationEntityNo) {
		this.corporationEntityNo = corporationEntityNo;
	}
	public String getWhetherProcess() {
		return whetherProcess;
	}
	public void setWhetherProcess(String whetherProcess) {
		this.whetherProcess = whetherProcess;
	}
	public String getActivationMode() {
		return activationMode;
	}
	public void setActivationMode(String activationMode) {
		this.activationMode = activationMode;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getCurrLogFlag() {
		return currLogFlag;
	}
	public void setCurrLogFlag(String currLogFlag) {
		this.currLogFlag = currLogFlag;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	
	public List<CoreCustomerEffectiveCode> getListCoreCustomerEffectiveCode() {
		return listCoreCustomerEffectiveCode;
	}
	public void setListCoreCustomerEffectiveCode(List<CoreCustomerEffectiveCode> listCoreCustomerEffectiveCode) {
		this.listCoreCustomerEffectiveCode = listCoreCustomerEffectiveCode;
	}
	public String getBlockFlag() {
		return blockFlag;
	}
	public void setBlockFlag(String blockFlag) {
		this.blockFlag = blockFlag;
	}
	public List<Map<String, Object>> getEventCommAreaTriggerEventList() {
		return eventCommAreaTriggerEventList;
	}
	public void setEventCommAreaTriggerEventList(List<Map<String, Object>> eventCommAreaTriggerEventList) {
		this.eventCommAreaTriggerEventList = eventCommAreaTriggerEventList;
	}
	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}
	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}
	public String getEcommMediaUnit() {
		return ecommMediaUnit;
	}
	public void setEcommMediaUnit(String ecommMediaUnit) {
		this.ecommMediaUnit = ecommMediaUnit;
	}
	public String getProductForm() {
		return productForm;
	}
	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}
	public String getProductHolderNo() {
		return productHolderNo;
	}
	public void setProductHolderNo(String productHolderNo) {
		this.productHolderNo = productHolderNo;
	}
	public String getMediaObjectForm() {
		return mediaObjectForm;
	}
	public void setMediaObjectForm(String mediaObjectForm) {
		this.mediaObjectForm = mediaObjectForm;
	}
	public String getMediaHolderNo() {
		return mediaHolderNo;
	}
	public void setMediaHolderNo(String mediaHolderNo) {
		this.mediaHolderNo = mediaHolderNo;
	}
	public String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public String getInvalidReasonOld() {
		return invalidReasonOld;
	}
	public void setInvalidReasonOld(String invalidReasonOld) {
		this.invalidReasonOld = invalidReasonOld;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}
	public String getAuthResp() {
		return authResp;
	}
	public void setAuthResp(String authResp) {
		this.authResp = authResp;
	}
	public String getExcptDesc() {
		return excptDesc;
	}
	public void setExcptDesc(String excptDesc) {
		this.excptDesc = excptDesc;
	}
	public String getInvalidFlag() {
		return invalidFlag;
	}
	public void setInvalidFlag(String invalidFlag) {
		this.invalidFlag = invalidFlag;
	}
	public String getSpEventNo() {
		return spEventNo;
	}
	public void setSpEventNo(String spEventNo) {
		this.spEventNo = spEventNo;
	}
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getOccurrDate() {
		return occurrDate;
	}
	public void setOccurrDate(String occurrDate) {
		this.occurrDate = occurrDate;
	}
	public String getOccurrTime() {
		return occurrTime;
	}
	public void setOccurrTime(String occurrTime) {
		this.occurrTime = occurrTime;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getBillDay() {
		return billDay;
	}
	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}
	public String getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}
	public List<CoreCustomerAddr> getCoreCoreCustomerAddrs() {
		return coreCoreCustomerAddrs;
	}
	public void setCoreCoreCustomerAddrs(List<CoreCustomerAddr> coreCoreCustomerAddrs) {
		this.coreCoreCustomerAddrs = coreCoreCustomerAddrs;
	}
	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}
	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}
	public String getBusinessProgram() {
		return businessProgram;
	}
	public void setBusinessProgram(String businessProgram) {
		this.businessProgram = businessProgram;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRemarkInfo() {
		return remarkInfo;
	}
	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
	}
	public String getLastUpdateUserid() {
		return lastUpdateUserid;
	}
	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getPricingType() {
		return pricingType;
	}
	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}
	public String getPricingTag() {
		return pricingTag;
	}
	public void setPricingTag(String pricingTag) {
		this.pricingTag = pricingTag;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getTagExpirationDate() {
		return tagExpirationDate;
	}
	public void setTagExpirationDate(Date tagExpirationDate) {
		this.tagExpirationDate = tagExpirationDate;
	}
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public String getBranchNumber() {
		return branchNumber;
	}
	public void setBranchNumber(String branchNumber) {
		this.branchNumber = branchNumber;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Integer getMemberSince() {
		return memberSince;
	}
	public void setMemberSince(Integer memberSince) {
		this.memberSince = memberSince;
	}
	public String getCustomerSource() {
		return customerSource;
	}
	public void setCustomerSource(String customerSource) {
		this.customerSource = customerSource;
	}
	public Integer getBehaviorScore() {
		return behaviorScore;
	}
	public void setBehaviorScore(Integer behaviorScore) {
		this.behaviorScore = behaviorScore;
	}
	public String getCustomerLevel() {
		return customerLevel;
	}
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	public Integer getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(Integer annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public String getResidencyStatus() {
		return residencyStatus;
	}
	public void setResidencyStatus(String residencyStatus) {
		this.residencyStatus = residencyStatus;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getOccupationCode() {
		return occupationCode;
	}
	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}
	public String getPostRankCode() {
		return postRankCode;
	}
	public void setPostRankCode(String postRankCode) {
		this.postRankCode = postRankCode;
	}
	public String getTitleCode() {
		return titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	public String getPeriodOfOccupation() {
		return periodOfOccupation;
	}
	public void setPeriodOfOccupation(String periodOfOccupation) {
		this.periodOfOccupation = periodOfOccupation;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getGuarantorFlag() {
		return guarantorFlag;
	}
	public void setGuarantorFlag(String guarantorFlag) {
		this.guarantorFlag = guarantorFlag;
	}
	public String getMarketerCode() {
		return marketerCode;
	}
	public void setMarketerCode(String marketerCode) {
		this.marketerCode = marketerCode;
	}
	public String getSocialSecurityId() {
		return socialSecurityId;
	}
	public void setSocialSecurityId(String socialSecurityId) {
		this.socialSecurityId = socialSecurityId;
	}
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	public List<CoreCustomerRemarks> getCoreCustomerRemarkss() {
		return coreCustomerRemarkss;
	}
	public void setCoreCustomerRemarkss(List<CoreCustomerRemarks> coreCustomerRemarkss) {
		this.coreCustomerRemarkss = coreCustomerRemarkss;
	}
	public List<CoreCustomerBusinessType> getCoreCustomerBusinessTypes() {
		return coreCustomerBusinessTypes;
	}
	public void setCoreCustomerBusinessTypes(List<CoreCustomerBusinessType> coreCustomerBusinessTypes) {
		this.coreCustomerBusinessTypes = coreCustomerBusinessTypes;
	}
	public String getProductUnitCode() {
		return productUnitCode;
	}
	public void setProductUnitCode(String productUnitCode) {
		this.productUnitCode = productUnitCode;
	}
	public String getProductLineCode() {
		return productLineCode;
	}
	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}
	public String getPostingCurrency1() {
		return postingCurrency1;
	}
	public void setPostingCurrency1(String postingCurrency1) {
		this.postingCurrency1 = postingCurrency1;
	}
	public String getPostingCurrency2() {
		return postingCurrency2;
	}
	public void setPostingCurrency2(String postingCurrency2) {
		this.postingCurrency2 = postingCurrency2;
	}
	public String getPostingCurrency3() {
		return postingCurrency3;
	}
	public void setPostingCurrency3(String postingCurrency3) {
		this.postingCurrency3 = postingCurrency3;
	}
	public String getPostingCurrency4() {
		return postingCurrency4;
	}
	public void setPostingCurrency4(String postingCurrency4) {
		this.postingCurrency4 = postingCurrency4;
	}
	public String getPostingCurrency5() {
		return postingCurrency5;
	}
	public void setPostingCurrency5(String postingCurrency5) {
		this.postingCurrency5 = postingCurrency5;
	}
	public String getPostingCurrency6() {
		return postingCurrency6;
	}
	public void setPostingCurrency6(String postingCurrency6) {
		this.postingCurrency6 = postingCurrency6;
	}
	public String getPostingCurrency7() {
		return postingCurrency7;
	}
	public void setPostingCurrency7(String postingCurrency7) {
		this.postingCurrency7 = postingCurrency7;
	}
	public String getPostingCurrency8() {
		return postingCurrency8;
	}
	public void setPostingCurrency8(String postingCurrency8) {
		this.postingCurrency8 = postingCurrency8;
	}
	public String getPostingCurrency9() {
		return postingCurrency9;
	}
	public void setPostingCurrency9(String postingCurrency9) {
		this.postingCurrency9 = postingCurrency9;
	}
	public String getPostingCurrency10() {
		return postingCurrency10;
	}
	public void setPostingCurrency10(String postingCurrency10) {
		this.postingCurrency10 = postingCurrency10;
	}
	public String getCoBrandedNo() {
		return coBrandedNo;
	}
	public void setCoBrandedNo(String coBrandedNo) {
		this.coBrandedNo = coBrandedNo;
	}
	public String getMediaUnitCode() {
		return mediaUnitCode;
	}
	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}
	public String getMediaObjectCode() {
		return mediaObjectCode;
	}
	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getMainCustomerNo() {
		return mainCustomerNo;
	}
	public void setMainCustomerNo(String mainCustomerNo) {
		this.mainCustomerNo = mainCustomerNo;
	}
	public String getSubCustomerNo() {
		return subCustomerNo;
	}
	public void setSubCustomerNo(String subCustomerNo) {
		this.subCustomerNo = subCustomerNo;
	}
	public String getMainSupplyIndicator() {
		return mainSupplyIndicator;
	}
	public void setMainSupplyIndicator(String mainSupplyIndicator) {
		this.mainSupplyIndicator = mainSupplyIndicator;
	}
	public String getMediaUserName() {
		return mediaUserName;
	}
	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}
	public String getApplicationStaffNo() {
		return applicationStaffNo;
	}
	public void setApplicationStaffNo(String applicationStaffNo) {
		this.applicationStaffNo = applicationStaffNo;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getPreviousExpirationDate() {
		return previousExpirationDate;
	}
	public void setPreviousExpirationDate(String previousExpirationDate) {
		this.previousExpirationDate = previousExpirationDate;
	}
	public String getPinDispatchMethod() {
		return pinDispatchMethod;
	}
	public void setPinDispatchMethod(String pinDispatchMethod) {
		this.pinDispatchMethod = pinDispatchMethod;
	}
	public String getMediaDispatchMethod() {
		return mediaDispatchMethod;
	}
	public void setMediaDispatchMethod(String mediaDispatchMethod) {
		this.mediaDispatchMethod = mediaDispatchMethod;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getActivationFlag() {
		return activationFlag;
	}
	public void setActivationFlag(String activationFlag) {
		this.activationFlag = activationFlag;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getEmbosserName1() {
		return embosserName1;
	}
	public void setEmbosserName1(String embosserName1) {
		this.embosserName1 = embosserName1;
	}
	public String getEmbosserName2() {
		return embosserName2;
	}
	public void setEmbosserName2(String embosserName2) {
		this.embosserName2 = embosserName2;
	}
	public String getEmbosserName3() {
		return embosserName3;
	}
	public void setEmbosserName3(String embosserName3) {
		this.embosserName3 = embosserName3;
	}
	public String getEmbosserName4() {
		return embosserName4;
	}
	public void setEmbosserName4(String embosserName4) {
		this.embosserName4 = embosserName4;
	}
	public String getProductionCode() {
		return productionCode;
	}
	public void setProductionCode(String productionCode) {
		this.productionCode = productionCode;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getPreviousProductionCode() {
		return previousProductionCode;
	}
	public void setPreviousProductionCode(String previousProductionCode) {
		this.previousProductionCode = previousProductionCode;
	}
	public String getPreviousProductionDate() {
		return previousProductionDate;
	}
	public void setPreviousProductionDate(String previousProductionDate) {
		this.previousProductionDate = previousProductionDate;
	}
	public String getFormatCode() {
		return formatCode;
	}
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	public String getAccountCurrency() {
		return accountCurrency;
	}
	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getLastProcessDate() {
		return lastProcessDate;
	}
	public void setLastProcessDate(String lastProcessDate) {
		this.lastProcessDate = lastProcessDate;
	}
	public String getCurrProcessDate() {
		return currProcessDate;
	}
	public void setCurrProcessDate(String currProcessDate) {
		this.currProcessDate = currProcessDate;
	}
	public String getNextProcessDate() {
		return nextProcessDate;
	}
	public void setNextProcessDate(String nextProcessDate) {
		this.nextProcessDate = nextProcessDate;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public String getCreditTreeId() {
		return creditTreeId;
	}
	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}
	public String getExcessPymtBusinessType() {
		return excessPymtBusinessType;
	}
	public void setExcessPymtBusinessType(String excessPymtBusinessType) {
		this.excessPymtBusinessType = excessPymtBusinessType;
	}
	public List<CoreMediaLabelInfo> getCoreMediaLabelInfos() {
		return coreMediaLabelInfos;
	}
	public void setCoreMediaLabelInfos(List<CoreMediaLabelInfo> coreMediaLabelInfos) {
		this.coreMediaLabelInfos = coreMediaLabelInfos;
	}
	public String getBindId() {
		return bindId;
	}
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	public String getBindDate() {
		return bindDate;
	}
	public void setBindDate(String bindDate) {
		this.bindDate = bindDate;
	}
	public String getUnbindDate() {
		return unbindDate;
	}
	public void setUnbindDate(String unbindDate) {
		this.unbindDate = unbindDate;
	}
	public String getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}
	public String getIsTransferCard() {
		return isTransferCard;
	}
	public void setIsTransferCard(String isTransferCard) {
		this.isTransferCard = isTransferCard;
	}
	public String getNonLogId() {
		return nonLogId;
	}
	public void setNonLogId(String nonLogId) {
		this.nonLogId = nonLogId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getOrganNo() {
		return organNo;
	}
	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}
	public String getAccountOrganizeTyp() {
		return accountOrganizeTyp;
	}
	public void setAccountOrganizeTyp(String accountOrganizeTyp) {
		this.accountOrganizeTyp = accountOrganizeTyp;
	}
	public String getBusinessDebitCreditCode() {
		return businessDebitCreditCode;
	}
	public void setBusinessDebitCreditCode(String businessDebitCreditCode) {
		this.businessDebitCreditCode = businessDebitCreditCode;
	}
	public String getCycleModeFlag() {
		return cycleModeFlag;
	}
	public void setCycleModeFlag(String cycleModeFlag) {
		this.cycleModeFlag = cycleModeFlag;
	}
	public String getStatusUpdateDate() {
		return statusUpdateDate;
	}
	public void setStatusUpdateDate(String statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public String getInterestProcessDate() {
		return interestProcessDate;
	}
	public void setInterestProcessDate(String interestProcessDate) {
		this.interestProcessDate = interestProcessDate;
	}
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public String getDelinquencyDate() {
		return delinquencyDate;
	}
	public void setDelinquencyDate(String delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}
	public String getAccountingStatusCode() {
		return accountingStatusCode;
	}
	public void setAccountingStatusCode(String accountingStatusCode) {
		this.accountingStatusCode = accountingStatusCode;
	}
	public String getAccountingStatusDate() {
		return accountingStatusDate;
	}
	public void setAccountingStatusDate(String accountingStatusDate) {
		this.accountingStatusDate = accountingStatusDate;
	}
	public String getPrevAccountingStatusCode() {
		return prevAccountingStatusCode;
	}
	public void setPrevAccountingStatusCode(String prevAccountingStatusCode) {
		this.prevAccountingStatusCode = prevAccountingStatusCode;
	}
	public String getPrevAccountingStatusDate() {
		return prevAccountingStatusDate;
	}
	public void setPrevAccountingStatusDate(String prevAccountingStatusDate) {
		this.prevAccountingStatusDate = prevAccountingStatusDate;
	}
	public String getDirectDebitType() {
		return directDebitType;
	}
	public void setDirectDebitType(String directDebitType) {
		this.directDebitType = directDebitType;
	}
	public String getCycleDue() {
		return cycleDue;
	}
	public void setCycleDue(String cycleDue) {
		this.cycleDue = cycleDue;
	}
	public String getPrevCycleDue() {
		return prevCycleDue;
	}
	public void setPrevCycleDue(String prevCycleDue) {
		this.prevCycleDue = prevCycleDue;
	}
	public String getAbsStatus() {
		return absStatus;
	}
	public void setAbsStatus(String absStatus) {
		this.absStatus = absStatus;
	}
	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public BigDecimal getTotalAmtDue() {
		return totalAmtDue;
	}
	public void setTotalAmtDue(BigDecimal totalAmtDue) {
		this.totalAmtDue = totalAmtDue;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getBalanceUnitCode() {
		return balanceUnitCode;
	}
	public void setBalanceUnitCode(String balanceUnitCode) {
		this.balanceUnitCode = balanceUnitCode;
	}
	public Integer getCycleNumber() {
		return cycleNumber;
	}
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAnnualInterestRate() {
		return annualInterestRate;
	}
	public void setAnnualInterestRate(BigDecimal annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}
	public BigDecimal getAccumulatedInterest() {
		return accumulatedInterest;
	}
	public void setAccumulatedInterest(BigDecimal accumulatedInterest) {
		this.accumulatedInterest = accumulatedInterest;
	}
	public BigDecimal getCurrentMinPayment() {
		return currentMinPayment;
	}
	public void setCurrentMinPayment(BigDecimal currentMinPayment) {
		this.currentMinPayment = currentMinPayment;
	}
	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}
	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}
	public String getNextInterestBillingDate() {
		return nextInterestBillingDate;
	}
	public void setNextInterestBillingDate(String nextInterestBillingDate) {
		this.nextInterestBillingDate = nextInterestBillingDate;
	}
	public String getPrevInterestBillingDate() {
		return prevInterestBillingDate;
	}
	public void setPrevInterestBillingDate(String prevInterestBillingDate) {
		this.prevInterestBillingDate = prevInterestBillingDate;
	}
	public String getAssetProperties() {
		return assetProperties;
	}
	public void setAssetProperties(String assetProperties) {
		this.assetProperties = assetProperties;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getLastInterestRate() {
		return lastInterestRate;
	}
	public void setLastInterestRate(BigDecimal lastInterestRate) {
		this.lastInterestRate = lastInterestRate;
	}
	public String getLastInterestRateExpirDate() {
		return lastInterestRateExpirDate;
	}
	public void setLastInterestRateExpirDate(String lastInterestRateExpirDate) {
		this.lastInterestRateExpirDate = lastInterestRateExpirDate;
	}
	public Integer getFirstBillingCycle() {
		return firstBillingCycle;
	}
	public void setFirstBillingCycle(Integer firstBillingCycle) {
		this.firstBillingCycle = firstBillingCycle;
	}
	public Integer getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}
	public String getCardAssociations() {
		return cardAssociations;
	}
	public void setCardAssociations(String cardAssociations) {
		this.cardAssociations = cardAssociations;
	}
	public String getMagneticChannel1() {
		return magneticChannel1;
	}
	public void setMagneticChannel1(String magneticChannel1) {
		this.magneticChannel1 = magneticChannel1;
	}
	public String getMagneticChannel2() {
		return magneticChannel2;
	}
	public void setMagneticChannel2(String magneticChannel2) {
		this.magneticChannel2 = magneticChannel2;
	}
	public String getMagneticChannel3() {
		return magneticChannel3;
	}
	public void setMagneticChannel3(String magneticChannel3) {
		this.magneticChannel3 = magneticChannel3;
	}
	public String getCardholderName() {
		return cardholderName;
	}
	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMediaObjectType() {
		return mediaObjectType;
	}
	public void setMediaObjectType(String mediaObjectType) {
		this.mediaObjectType = mediaObjectType;
	}
	public String getExtBuffer() {
		return extBuffer;
	}
	public void setExtBuffer(String extBuffer) {
		this.extBuffer = extBuffer;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public String getContactPostCode() {
		return contactPostCode;
	}
	public void setContactPostCode(String contactPostCode) {
		this.contactPostCode = contactPostCode;
	}
	public String getContactMobilePhone() {
		return contactMobilePhone;
	}
	public void setContactMobilePhone(String contactMobilePhone) {
		this.contactMobilePhone = contactMobilePhone;
	}
	public List<CoreCustomerBillDay> getListCoreCustomerBillDays() {
		return listCoreCustomerBillDays;
	}
	public void setListCoreCustomerBillDays(List<CoreCustomerBillDay> listCoreCustomerBillDays) {
		this.listCoreCustomerBillDays = listCoreCustomerBillDays;
	}
	public List<CoreCustomerUnifyInfo> getListCoreCustomerUnifyInfos() {
		return listCoreCustomerUnifyInfos;
	}
	public void setListCoreCustomerUnifyInfos(List<CoreCustomerUnifyInfo> listCoreCustomerUnifyInfos) {
		this.listCoreCustomerUnifyInfos = listCoreCustomerUnifyInfos;
	}
	public List<CoreMediaBind> getListCoreMediaBinds() {
		return listCoreMediaBinds;
	}
	public void setListCoreMediaBinds(List<CoreMediaBind> listCoreMediaBinds) {
		this.listCoreMediaBinds = listCoreMediaBinds;
	}
	public String getBlockCodeType() {
		return blockCodeType;
	}
	public void setBlockCodeType(String blockCodeType) {
		this.blockCodeType = blockCodeType;
	}
	public Integer getBlockCodeScene() {
		return blockCodeScene;
	}
	public void setBlockCodeScene(Integer blockCodeScene) {
		this.blockCodeScene = blockCodeScene;
	}
	public Integer getBlockCodePrioriy() {
		return blockCodePrioriy;
	}
	public void setBlockCodePrioriy(Integer blockCodePrioriy) {
		this.blockCodePrioriy = blockCodePrioriy;
	}
	public String getBlockCodeDesc() {
		return blockCodeDesc;
	}
	public void setBlockCodeDesc(String blockCodeDesc) {
		this.blockCodeDesc = blockCodeDesc;
	}
	public String getBlockCodeScope() {
		return blockCodeScope;
	}
	public void setBlockCodeScope(String blockCodeScope) {
		this.blockCodeScope = blockCodeScope;
	}
	public List<CoreProduct> getListCoreProducts() {
		return listCoreProducts;
	}
	public void setListCoreProducts(List<CoreProduct> listCoreProducts) {
		this.listCoreProducts = listCoreProducts;
	}
	public List<CoreMediaBasicInfo> getListcoreMediaBasicInfo() {
		return listcoreMediaBasicInfo;
	}
	public void setListcoreMediaBasicInfo(List<CoreMediaBasicInfo> listcoreMediaBasicInfo) {
		this.listcoreMediaBasicInfo = listcoreMediaBasicInfo;
	}
	public List<CoreAccount> getListCoreAccount() {
		return listCoreAccount;
	}
	public void setListCoreAccount(List<CoreAccount> listCoreAccount) {
		this.listCoreAccount = listCoreAccount;
	}
	public String getElementNo() {
		return elementNo;
	}
	public void setElementNo(String elementNo) {
		this.elementNo = elementNo;
	}
	public String getContrlSerialNo() {
		return contrlSerialNo;
	}
	public void setContrlSerialNo(String contrlSerialNo) {
		this.contrlSerialNo = contrlSerialNo;
	}
	public String getContrlLevel() {
		return contrlLevel;
	}
	public void setContrlLevel(String contrlLevel) {
		this.contrlLevel = contrlLevel;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getContrlSource() {
		return contrlSource;
	}
	public void setContrlSource(String contrlSource) {
		this.contrlSource = contrlSource;
	}
	public String getContrlStartDate() {
		return contrlStartDate;
	}
	public void setContrlStartDate(String contrlStartDate) {
		this.contrlStartDate = contrlStartDate;
	}
	public String getContrlEndDate() {
		return contrlEndDate;
	}
	public void setContrlEndDate(String contrlEndDate) {
		this.contrlEndDate = contrlEndDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public List<X5120VO> getListX5120VOs() {
		return listX5120VOs;
	}
	public void setListX5120VOs(List<X5120VO> listX5120VOs) {
		this.listX5120VOs = listX5120VOs;
	}
	public String getLifecycleNodeType() {
		return lifecycleNodeType;
	}
	public void setLifecycleNodeType(String lifecycleNodeType) {
		this.lifecycleNodeType = lifecycleNodeType;
	}
	public String getDirectDebitStatus() {
		return directDebitStatus;
	}
	public void setDirectDebitStatus(String directDebitStatus) {
		this.directDebitStatus = directDebitStatus;
	}
	public String getDirectDebitMode() {
		return directDebitMode;
	}
	public void setDirectDebitMode(String directDebitMode) {
		this.directDebitMode = directDebitMode;
	}
	public String getDirectDebitBankNo() {
		return directDebitBankNo;
	}
	public void setDirectDebitBankNo(String directDebitBankNo) {
		this.directDebitBankNo = directDebitBankNo;
	}
	public String getDirectDebitAccountNo() {
		return directDebitAccountNo;
	}
	public void setDirectDebitAccountNo(String directDebitAccountNo) {
		this.directDebitAccountNo = directDebitAccountNo;
	}
	public String getSystemUnitNo() {
		return systemUnitNo;
	}
	public void setSystemUnitNo(String systemUnitNo) {
		this.systemUnitNo = systemUnitNo;
	}
	public String getCreditNodeNo() {
		return creditNodeNo;
	}
	public void setCreditNodeNo(String creditNodeNo) {
		this.creditNodeNo = creditNodeNo;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getTransferMediaCode() {
		return transferMediaCode;
	}
	public void setTransferMediaCode(String transferMediaCode) {
		this.transferMediaCode = transferMediaCode;
	}
	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}
	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
	}
	public BigDecimal getPermLimit() {
		return permLimit;
	}
	public void setPermLimit(BigDecimal permLimit) {
		this.permLimit = permLimit;
	}
	public String getRetailVerifyPswFlag() {
		return retailVerifyPswFlag;
	}
	public void setRetailVerifyPswFlag(String retailVerifyPswFlag) {
		this.retailVerifyPswFlag = retailVerifyPswFlag;
	}
	public BigDecimal getRetailVerifyPswAmount() {
		return retailVerifyPswAmount;
	}
	public void setRetailVerifyPswAmount(BigDecimal retailVerifyPswAmount) {
		this.retailVerifyPswAmount = retailVerifyPswAmount;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getPaymentMark() {
		return paymentMark;
	}
	public void setPaymentMark(String paymentMark) {
		this.paymentMark = paymentMark;
	}
	public String getModifyFieldName() {
		return modifyFieldName;
	}
	public void setModifyFieldName(String modifyFieldName) {
		this.modifyFieldName = modifyFieldName;
	}
	public String getActivityNo() {
		return activityNo;
	}
	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getAdminFlag() {
		return adminFlag;
	}
	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getLoanCurrencyCode() {
		return loanCurrencyCode;
	}
	public void setLoanCurrencyCode(String loanCurrencyCode) {
		this.loanCurrencyCode = loanCurrencyCode;
	}
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getLoanNumber() {
		return loanNumber;
	}
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}
	public String getMonthSupplyPeriod() {
		return monthSupplyPeriod;
	}
	public void setMonthSupplyPeriod(String monthSupplyPeriod) {
		this.monthSupplyPeriod = monthSupplyPeriod;
	}
	public String getLoanTermUnit() {
		return loanTermUnit;
	}
	public void setLoanTermUnit(String loanTermUnit) {
		this.loanTermUnit = loanTermUnit;
	}
	public String getPayLoanDate() {
		return payLoanDate;
	}
	public void setPayLoanDate(String payLoanDate) {
		this.payLoanDate = payLoanDate;
	}
	public String getLoanExpira() {
		return loanExpira;
	}
	public void setLoanExpira(String loanExpira) {
		this.loanExpira = loanExpira;
	}
	public String getReceiptAccount() {
		return receiptAccount;
	}
	public void setReceiptAccount(String receiptAccount) {
		this.receiptAccount = receiptAccount;
	}
	public String getReceiptBranchNUM() {
		return ReceiptBranchNUM;
	}
	public void setReceiptBranchNUM(String receiptBranchNUM) {
		ReceiptBranchNUM = receiptBranchNUM;
	}
	public String getLoanRate() {
		return loanRate;
	}
	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
	}
	public String getRepayPrincipal() {
		return repayPrincipal;
	}
	public void setRepayPrincipal(String repayPrincipal) {
		this.repayPrincipal = repayPrincipal;
	}
	public String getRepaymentDateType() {
		return repaymentDateType;
	}
	public void setRepaymentDateType(String repaymentDateType) {
		this.repaymentDateType = repaymentDateType;
	}
	public String getRepayDay() {
		return repayDay;
	}
	public void setRepayDay(String repayDay) {
		this.repayDay = repayDay;
	}
	public String getFreeDays() {
		return freeDays;
	}
	public void setFreeDays(String freeDays) {
		this.freeDays = freeDays;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public Map<String, Object> getCoreMediaBasicInfoMap() {
		return coreMediaBasicInfoMap;
	}
	public void setCoreMediaBasicInfoMap(Map<String, Object> coreMediaBasicInfoMap) {
		this.coreMediaBasicInfoMap = coreMediaBasicInfoMap;
	}
	public List<ResultGns> getCoreMediaBasicInfoList() {
		return coreMediaBasicInfoList;
	}
	public void setCoreMediaBasicInfoList(List<ResultGns> coreMediaBasicInfoList) {
		this.coreMediaBasicInfoList = coreMediaBasicInfoList;
	}
	public String getLimitEffectvDate() {
		return limitEffectvDate;
	}
	public void setLimitEffectvDate(String limitEffectvDate) {
		this.limitEffectvDate = limitEffectvDate;
	}
	public String getLimitExpireDate() {
		return limitExpireDate;
	}
	public void setLimitExpireDate(String limitExpireDate) {
		this.limitExpireDate = limitExpireDate;
	}
	public String getManageLevelCode() {
		return manageLevelCode;
	}
	public void setManageLevelCode(String manageLevelCode) {
		this.manageLevelCode = manageLevelCode;
	}
	public BigDecimal getOrgAllQuota() {
		return orgAllQuota;
	}
	public void setOrgAllQuota(BigDecimal orgAllQuota) {
		this.orgAllQuota = orgAllQuota;
	}
	public BigDecimal getPersonMaxQuota() {
		return personMaxQuota;
	}
	public void setPersonMaxQuota(BigDecimal personMaxQuota) {
		this.personMaxQuota = personMaxQuota;
	}
	public String getBudgetOrgCode() {
		return budgetOrgCode;
	}
	public void setBudgetOrgCode(String budgetOrgCode) {
		this.budgetOrgCode = budgetOrgCode;
	}
	public String getGlobalEventNo() {
		return globalEventNo;
	}
	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}
	public String getCredentialNumber() {
		return credentialNumber;
	}
	public void setCredentialNumber(String credentialNumber) {
		this.credentialNumber = credentialNumber;
	}
	public String getAccountOrganForm() {
		return accountOrganForm;
	}
	public void setAccountOrganForm(String accountOrganForm) {
		this.accountOrganForm = accountOrganForm;
	}
	public String getGraceDate() {
		return graceDate;
	}
	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}
	public BigDecimal getNewBalance() {
		return newBalance;
	}
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getCapitalType() {
		return capitalType;
	}
	public void setCapitalType(String capitalType) {
		this.capitalType = capitalType;
	}
	public String getCapitalStage() {
		return capitalStage;
	}
	public void setCapitalStage(String capitalStage) {
		this.capitalStage = capitalStage;
	}
	public String getCapitalOrganizationCode() {
		return capitalOrganizationCode;
	}
	public void setCapitalOrganizationCode(String capitalOrganizationCode) {
		this.capitalOrganizationCode = capitalOrganizationCode;
	}
	public String getCapitalOrganizationName() {
		return capitalOrganizationName;
	}
	public void setCapitalOrganizationName(String capitalOrganizationName) {
		this.capitalOrganizationName = capitalOrganizationName;
	}
	public String getProductObjectCodeNew() {
		return productObjectCodeNew;
	}
	public void setProductObjectCodeNew(String productObjectCodeNew) {
		this.productObjectCodeNew = productObjectCodeNew;
	}
	public String getMainProductForm() {
		return mainProductForm;
	}
	public void setMainProductForm(String mainProductForm) {
		this.mainProductForm = mainProductForm;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	
}
