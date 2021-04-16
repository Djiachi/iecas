
package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5000Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreSequenceSerial;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreProductDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5000BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.ManagerCardNumService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.BetaCommonParamServiceImpl;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 *
 * Description:开立客户基本信息表
 *
 * @author admin
 * @date 2018年8月7日
 */
@Service
public class X5000BusImpl implements X5000Bus {

    // 需要存储的最大值
    private static final String CUS = "CUS";
    // 每次增加最大值次数
    private static final long ADDNUM = 500;
    private static Object lock = new Object();

	static final String ApplicationCardType_1 = "1";  //单申主卡
	static final String ApplicationCardType_2 = "2";  //单申附卡
	static final String ApplicationCardType_3 = "3";  //主附同申-主卡
	static final String ApplicationCardType_4 = "4";  //主附同申-附卡
    
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BetaCommonParamServiceImpl betaCommonParamServiceImpl;
    @Autowired
    private ManagerCardNumService managerCardNumService;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreProductDao coreProductDao;
	
	
    /**
     * 生成核心客户号 并给插入客户基本信息表中
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object busExecute(X5000BO x5000BO) throws Exception {
        // 判断输入的各字段是否为空
        SpringUtil.getBean(ValidatorUtil.class).validate(x5000BO);
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        CachedBeanCopy.copyProperties(x5000BO, eventCommAreaNonFinance);
        
        /**
         * 对港澳台居住证增加唯一性校验id_number_hmt
         * add by wangxi 2019/7/3 cyy提
         */
        String idNumberHmt = x5000BO.getIdNumberHmt();//港澳台居民居住证号码字段
        //当该字段为空时，跳过校验
        if(StringUtil.isNotBlank(idNumberHmt)){
	        List<CoreCustomer> coreCustomerList = null;
	        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
	        //根据港澳台居民居住证号码字段查询客户基本信息表
	    	coreCustomerSqlBuilder.andIdNumberHmtEqualTo(idNumberHmt);
	    	coreCustomerList = coreCustomerDao.selectListBySqlBuilder(coreCustomerSqlBuilder);
        	if(null != coreCustomerList && coreCustomerList.size() > 0){
        		throw new BusinessException("CUS-00125");//港澳台居民居住证已存在，请重新输入！
        	}
        }
        
        CoreOperationMode coreOperationMode = checkData(x5000BO);
        String operatorId = x5000BO.getOperatorId();
        // 机构编号
        String organNo = x5000BO.getInstitutionId();
        CoreOrgan coreOrgan = httpQueryService.queryOrgan(organNo);
        CoreCorporationEntity coreCorporationEntity = httpQueryService.queryCoreCorporationEntity(coreOrgan.getCorporationEntityNo());
        String systemUnitNo = coreCorporationEntity.getSystemUnitNo();
        Map<String, Object> map = MapTransformUtils.objectToMap(x5000BO);
        // 生成核心客户号

        if (StringUtil.isNotBlank(x5000BO.getIsNew())) {
            if ("1".equals(x5000BO.getIsNew())) {
                return x5000BO;
            }
        }

        /** 
         *  判断如果是信审开卡,并且申请附属卡，则校验以下内容:
         *  1. 主证件类型,主证件号码必须输入
         *  2. 主证件类型,主证件号码对应的对应的产品已经申请
         *  
         */
        if (StringUtil.isNotBlank(x5000BO.getApplicationCardType())) {
			if (ApplicationCardType_2.equals(x5000BO.getApplicationCardType()) || ApplicationCardType_4.equals(x5000BO.getApplicationCardType())) {
				if (StringUtil.isNotBlank(x5000BO.getSupIdType()) && StringUtil.isNotBlank(x5000BO.getSupIdNumber())) {
					CoreCustomer coreCustomerObj = (CoreCustomer)queryCustomerService.queryCustomer(x5000BO.getSupIdType(), x5000BO.getSupIdNumber(), null);
					if (null != coreCustomerObj) {
						// 产品对象代码
						String productObjectCode = x5000BO.getProductObjectCode();
						CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
						coreProductSqlBuilder.andCustomerNoEqualTo(coreCustomerObj.getCustomerNo());
						coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
						CoreProduct coreProduct = coreProductDao.selectBySqlBuilder(coreProductSqlBuilder);
						if (null != coreProduct) {
							
						}else {
							throw new BusinessException("CUS-00122",productObjectCode);
						}
					}else {
						throw new BusinessException("CUS-00121");
					}
				}else {
					throw new BusinessException("CUS-00120");
				}
			}
		}
        
        String customerNo = managerCardNumService.queryCustomerNo();

        if (null == customerNo) {
            for (int i = 0; i < 5; i++) {
                if (null == customerNo) {
                    customerNo = managerCardNumService.queryCustomerNo();
                }
                if (null != customerNo) {
                    break;
                }
            }
            if (null == customerNo) {
                throw new BusinessException("CUS-00065", "客户号");
            }
        }
        // 1.查询 Redis 获取 +1 值
        // 2.查询DB获取最大的参数值
        // 3.Redis 获取值跟DB获取值比较
        // 比较结果，如果是小于等于DB值，则可用
        // 4.如果不小于DB值，不可用。需要更新Redis值 = DB值，同时DB值增加5000
        // 5.更新DB成功后，再次查Redis 值，查询DB值，再次比较
        CoreCustomer coreCustomer = new CoreCustomer();
        ReflexUtil.setFieldsValues(coreCustomer, map);
        coreCustomer.setId(RandomUtil.getUUID());
        coreCustomer.setVersion(1);
        coreCustomer.setCustomerNo(customerNo);
        coreCustomer.setOperationMode(coreOperationMode.getOperationMode());
        coreCustomer.setSystemUnitNo(systemUnitNo);
        coreCustomer.setCorporationEntityNo(coreOrgan.getCorporationEntityNo());
        coreCustomer.setPaymentMark(x5000BO.getPaymentMark());
        if (StringUtil.isBlank(eventCommAreaNonFinance.getCustomerStatus())) {
            coreCustomer.setCustomerStatus("0");
            eventCommAreaNonFinance.setCustomerStatus(coreCustomer.getCustomerStatus());
        }
        else {
            coreCustomer.setCustomerStatus(eventCommAreaNonFinance.getCustomerStatus());
        }
        // 客户核算状态正常
        coreCustomer.setAccountingStatusCode("000");
        // 系统单元编号
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
        coreCustomer.setCreateDate(coreSystemUnit.getNextProcessDate());
        coreCustomer.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
        @SuppressWarnings("unused")
        int iresult = coreCustomerDao.insert(coreCustomer);
        eventCommAreaNonFinance.setCustomerNo(customerNo);
        eventCommAreaNonFinance.setOperationMode(coreOperationMode.getOperationMode());
        @SuppressWarnings("unused")
        String globalEventNo = x5000BO.getGlobalEventNo();
        eventCommAreaNonFinance.setSystemUnitNo(systemUnitNo);
        /** 核算状态码 */
        eventCommAreaNonFinance.setAccountingStatusCode(coreCustomer.getAccountingStatusCode());
        /** 新建客户日期 */
        eventCommAreaNonFinance.setCreateDate(coreCustomer.getCreateDate());
        eventCommAreaNonFinance.setSystemUnitNo(systemUnitNo);
        List<String> entrys = new ArrayList<String>();
        entrys.add("customer_no");
        if (operatorId == null) {
            operatorId = "system";
        }
        eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
        eventCommAreaNonFinance.setCorporationEntityNo(coreCorporationEntity.getCorporationEntityNo());
        eventCommAreaNonFinance.setCorporation(coreCorporationEntity.getCorporationEntityNo());
        nonFinancialLogUtil.createNonFinancialActivityLog(x5000BO.getEventNo(), x5000BO.getActivityNo(), ModificationType.ADD.getValue(),
            null, null, coreCustomer, coreCustomer.getId(), coreSystemUnit.getCurrLogFlag(), operatorId, customerNo, customerNo, null,
            null);
        return eventCommAreaNonFinance;
    }

    /**
     * 
     * @Description:
     * @return
     * @throws Exception
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String queryCustomerNo(Long redisIdNum, String operatorId) throws Exception {
        // 2.查询出DB存储的数值，顺序号参数表中存储的数据。
        String key = Constant.PARAMS_FLAG + CUS;
        Map<String, String> sequenceSerialParamsMap = new HashMap<String, String>();
        sequenceSerialParamsMap.put("serialType", CUS);
        sequenceSerialParamsMap.put("redisKey", key);
        // 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
        sequenceSerialParamsMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
        sequenceSerialParamsMap.put("operatorId", operatorId);
        CoreSequenceSerial coreSequenceSerial = betaCommonParamServiceImpl.querySequenceSerial(sequenceSerialParamsMap);
        if (null == coreSequenceSerial) {
            throw new BusinessException("CUS-00014", "顺序号表");// 机构表
        }
        long maxSerial = coreSequenceSerial.getNextSerialNo();
        long minSerial = maxSerial - ADDNUM;
        synchronized (lock) {
            if (redisIdNum > minSerial && redisIdNum <= maxSerial) {
                return String.format("%012d", redisIdNum);
            }
            long addStep = maxSerial - redisIdNum + 1;
            long maxSerialNew = maxSerial + ADDNUM;
            try {
                // 跟新DB数值
                // coreSequenceSerialSqlBuilder.andVersionEqualTo(coreSequenceSerial.getVersion());
                // coreSequenceSerial.setNextSerialNo(maxSerialNew);
                // coreSequenceSerial.setVersion(coreSequenceSerial.getVersion() + 1);
                // int rs = coreSequenceSerialDao.updateBySqlBuilderSelective(coreSequenceSerial,
                // coreSequenceSerialSqlBuilder);
                Map<String, String> sequenceSerialParamsUpMap = new HashMap<String, String>();
                sequenceSerialParamsUpMap.put("serialType", CUS);
                sequenceSerialParamsUpMap.put("redisKey", key);
                sequenceSerialParamsUpMap.put("nextSerialNo", String.valueOf(maxSerialNew));
                sequenceSerialParamsUpMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
                sequenceSerialParamsUpMap.put("operatorId", operatorId);
                // 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
                int rs = betaCommonParamServiceImpl.updateSequenceSerial(sequenceSerialParamsUpMap);
                if (rs == 1) {
                    if (redisIdNum > maxSerial && redisIdNum <= maxSerialNew) {
                        return String.format("%012d", redisIdNum);
                    }
                }
                else {
                    addStep = 1L;
                }
            }
            catch (Exception e) {
                return null;
            }
            long redisIdNumNew = stringRedisTemplate.opsForValue().increment(Constant.CORE_GLOBAL_INCREMENT_KEY + "POC5", addStep);
            if (redisIdNumNew > maxSerial && redisIdNumNew <= maxSerialNew) {
                return String.format("%012d", redisIdNumNew);
            }
        }
        return null;
    }

    /**
     * 
     * @Description: 检查输入条件，判断输入条件是否满足
     * @param x5000BO
     * @throws Exception
     */
    private CoreOperationMode checkData(X5000BO x5000BO) throws Exception {
        synchronized (lock) {
            CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
            if (StringUtil.isNotBlank(x5000BO.getIdType()) && StringUtil.isNotBlank(x5000BO.getIdNumber())) {
                // 检查卡类型与卡号是否匹配
                boolean checkIdTypeAndIdNum = this.checkIdTypeAndIdNum(x5000BO);
                if (!checkIdTypeAndIdNum) {
                    throw new BusinessException("CUS-00014", "卡类型");
                }
                coreCustomerSqlBuilder.andIdNumberEqualTo(x5000BO.getIdNumber());
                coreCustomerSqlBuilder.andIdTypeEqualTo(x5000BO.getIdType());
            }
            else {
                throw new BusinessException("COR-10047");
            }
            CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
            if (null != coreCustomer) {
                if (EVENT.BSS_AD_01_9001.equals(x5000BO.getEventNo()) || EVENT.BSS_AD_01_0001.equals(x5000BO.getEventNo())) {
                    CachedBeanCopy.copyProperties(coreCustomer, x5000BO);
                    x5000BO.setMainCustomerNo(coreCustomer.getCustomerNo());
                    // 该身份证号已经开立，需要判断产品编号是否已经开立
                    x5000BO.setIsNew("1");
                    if (EVENT.BSS_AD_01_0001.equals(x5000BO.getEventNo())) {
                        x5000BO.setWhetherProcess("1");
                    }
                }
                else {
                    throw new BusinessException("CUS-00044");
                }
            }
        }
        // 检查所属机构
        CoreOrgan coreOrgan = httpQueryService.queryOrgan(x5000BO.getInstitutionId());
        if (coreOrgan == null) {
            throw new BusinessException("CUS-00014", "机构表");// 机构表
        }
        x5000BO.setCorporationEntityNo(coreOrgan.getCorporationEntityNo());
        x5000BO.setCorporation(coreOrgan.getCorporationEntityNo());
        List<CoreCustomerAddr> listCoreCustomerAddrs = x5000BO.getCoreCoreCustomerAddrs();
        if (listCoreCustomerAddrs.size() > 4) {
            throw new BusinessException("CUS-00011");
        }
        CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(coreOrgan.getOperationMode());
        if (null == coreOperationMode) {
            throw new BusinessException("CUS-00014", "运营模式");// 机构表
        }
        // 校验客户类型与证件类型一致性--客户类型是“预算单位”时，证件类型只能是“预算单位”
        if ("3".equals(x5000BO.getCustomerType()) && !"7".equals(x5000BO.getIdType())) {
            throw new BusinessException("CUS-00015", "客户类型是预算单位时证件类型必须是预算单位!");
        }
        return coreOperationMode;
    }

    /**
     * 检查卡类型与卡是否符合
     * 
     * @param x5000BO
     * @return
     */
    private boolean checkIdTypeAndIdNum(X5000BO x5000BO) {
        String idType = x5000BO.getIdType();
        String idNumber = x5000BO.getIdNumber();
        boolean matche = false;
        String regex = "";
        switch (idType) {
        case "1":
            regex = "\\d{17}(\\d|[X])";
            matche = idNumber.matches(regex);
            break;
        case "2":
            regex = "([H]|[M])\\d{10}";
            matche = idNumber.matches(regex);
            break;
        case "3":
            regex = "(([a-z|A-Z])|\\d)\\d{7}";
            matche = idNumber.matches(regex);
            break;
        case "4":
            regex = "([a-z]|[A-Z])\\d{8}";
            matche = idNumber.matches(regex);
            break;
        case "5":
            regex = "([a-z]|[A-Z])\\d{8}";
            matche = idNumber.matches(regex);
            break;
        case "6":
            regex = "(([a-z|A-Z]){3})\\d{12}";
            matche = idNumber.matches(regex);
            break;
        case "7":
//        	regex = "";
//        	matche = idNumber.matches(regex);
        	matche = true;
        	break;
        default:
            matche = false;
        }
        return matche;
    }
}
