
package com.tansun.ider.bus.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5030Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.MapBean;
import com.tansun.ider.model.bo.X5030BO;
import com.tansun.ider.model.vo.X4005VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 产品单元基本信息建立
 * @author: admin
 */
@Service
public class X5030BusImpl implements X5030Bus {

    @Autowired
    private CoreProductDaoImpl coreProductDaoImpl;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
	private CardUtil cardUtil;
    @Autowired
    private HttpQueryServiceByGns httpQueryServiceByGns;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Object busExecute(X5030BO x5030BO) throws Exception {
        // 判断输入的各字段是否为空
        SpringUtil.getBean(ValidatorUtil.class).validate(x5030BO);
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x5030BO, eventCommAreaNonFinance);
        String operatorId = x5030BO.getOperatorId();
        Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
        // 检查产品线是否存在
        String customerNo = eventCommAreaNonFinance.getCustomerNo();
        String idType = eventCommAreaNonFinance.getIdType();
        String idNumber = eventCommAreaNonFinance.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
            coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
            coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
        }
        else {
            throw new BusinessException("COR-10047");
        }
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
        String productObjectCode = x5030BO.getProductObjectCode();
        eventCommAreaNonFinance.setOperationMode(coreCustomer.getOperationMode());
        CoreProductObject coreProductObject = checkDate(eventCommAreaNonFinance);
        CoreProduct coreProduct = new CoreProduct();
        if (operatorId == null) {
            operatorId = "system";
        }
        CoreProductSqlBuilder coreProductSqlBuilder = new CoreProductSqlBuilder();
        coreProductSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreProductSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        CoreProduct coreProductOld = coreProductDaoImpl.selectBySqlBuilder(coreProductSqlBuilder);
        if (coreProductOld != null) {
            if (!EVENT.BSS_AD_01_9001.equals(x5030BO.getEventNo())) {
                throw new BusinessException("CUS-00046");
            }
            else {
                CachedBeanCopy.copyProperties(coreProductObject, eventCommAreaNonFinance);
                eventCommAreaNonFinance.setIsNew("2");
                return eventCommAreaNonFinance;
            }
        }
        ReflexUtil.setFieldsValues(coreProduct, map);
        
        /**
         *  客户产品单元增加“申请日期”，赋值逻辑与媒介信息表中的新建日期一样
         *  add by wangxi	2019/7/19	cyy提
         */
        CoreCorporationEntity coreCorporationEntity = cardUtil
				.getSystemUnitNoCoreCorporationEntity(coreCustomer.getInstitutionId());
        String binNo = coreProductObject.getBinNo().toString();
		CoreSystemUnit systemUnit = httpQueryService.querySystemUnitForBinNo(binNo, coreCorporationEntity,
				operatorId);
        String operationDate = "";
		if (Constant.EOD.equals(systemUnit.getSystemOperateState())) {
			operationDate = systemUnit.getCurrProcessDate();
		} else {
			operationDate = systemUnit.getNextProcessDate();
		}
		coreProduct.setCreateDate(operationDate);//申请日期赋值
        
		/**
		 * 客户产品表增加“套卡对方产品产品对象代码[productCodeSet]”字段
		 * 新增客户时，后台从产品对象表中获取该字段并赋值,如果有值,则进行赋值操作,没值跳过
		 * add by wangxi   2019/9/5		POC平移183
		 */
		String productCodeSet = coreProductObject.getProductCodeSet();
		if(StringUtil.isNotBlank(productCodeSet)){
			coreProduct.setProductCodeSet(productCodeSet);
		}
		
        coreProduct.setId(RandomUtil.getUUID());
        coreProduct.setOperationMode(coreProductObject.getOperationMode());
        coreProduct.setStatusCode("1");
        coreProduct.setVersion(1);
        // 确定预算单位的片区号--by cgc-start
        // 查询产品对象对应的业务项目代码
        List<CoreProductBusinessScope> queryProductBusinessScopes =
                httpQueryService.queryProductBusinessScope(productObjectCode, coreProductObject.getOperationMode());
        if (CollectionUtils.isNotEmpty(queryProductBusinessScopes)) {
            // 查询业务项目对应的构件元件值,如果为预算单位业务,则预算单位编码必须有.
            EventCommArea eventCommArea = new EventCommArea();
            eventCommArea.setEcommOperMode(queryProductBusinessScopes.get(0).getOperationMode());// 运营模式
            eventCommArea.setEcommBusinessProgramCode(queryProductBusinessScopes.get(0).getBusinessProgramNo());// 业务项目代码
            CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
            Map<String, String> baseLoanRateMap = artService.getElementByArtifact(Constant.ARTIFACT_NO_501, eventCommArea);
            MapBean baseLoanRateMode = handleMap(baseLoanRateMap);
            if (Constant.BUSINESS_BY_BUDGETUNIT.equals(baseLoanRateMode.getKey())) {
                if (x5030BO.getBudgetOrgCode() != null) {
                    coreProduct.setBudgetOrgCode(x5030BO.getBudgetOrgCode());// 预算单位编码
                    // 查询预算单位所在的片区编号，判断预算单位和客户的片区编号是否相同
                    X4005VO x4005Vo = new X4005VO();
                    String queryGnsCode = httpQueryServiceByGns.queryGnsCode(nofnUrl, x5030BO.getBudgetOrgCode());
                    if (StringUtil.isNotBlank(queryGnsCode)) {
                        x4005Vo = JSON.parseObject(queryGnsCode, X4005VO.class, Feature.DisableCircularReferenceDetect);
                        String budgetGnsCode = x4005Vo.getGnsNumber();
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(budgetGnsCode);
                        coreProduct.setBudgetOrgArea(m.replaceAll("").trim());// 预算单位所在片区
                    }
                    else {
                        throw new BusinessException("COR-11004");
                    }
                }
                else {
                    throw new BusinessException("COR-11005");
                }
            }
        }
        coreProduct.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
        // by cgc-end
        @SuppressWarnings("unused")
        int i = coreProductDaoImpl.insert(coreProduct);
        eventCommAreaNonFinance.setOperationMode(coreProductObject.getOperationMode());
        // 获取当前系统下一处理日期
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        eventCommAreaNonFinance.setSystemUnitNo(coreCustomer.getSystemUnitNo());
        eventCommAreaNonFinance.setOperationDate(coreSystemUnit.getNextProcessDate());
        eventCommAreaNonFinance.setNextProcessDate(coreSystemUnit.getNextProcessDate());
        eventCommAreaNonFinance.setProductObjectCode(productObjectCode);
        eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
        nonFinancialLogUtil.createNonFinancialActivityLog(x5030BO.getEventNo(), x5030BO.getActivityNo(), ModificationType.ADD.getValue(),
            null, null, coreProduct, coreProduct.getId(), eventCommAreaNonFinance.getCurrLogFlag(), operatorId, coreProduct.getCustomerNo(),
            coreProduct.getProductObjectCode(), null, null);
        if (StringUtil.isBlank(eventCommAreaNonFinance.getBillDay())) {
            eventCommAreaNonFinance.setBillDay(coreSystemUnit.getNextProcessDate().substring(8, 10));
        }
        return eventCommAreaNonFinance;
    }

    /**
     *
     * @Description: 检查产品对象代码
     * @param eventCommAreaNonFinance
     * @throws Exception
     */
    private CoreProductObject checkDate(EventCommAreaNonFinance eventCommAreaNonFinance) throws Exception {
        CoreProductObject coreProductObject = httpQueryService.queryProductObject(eventCommAreaNonFinance.getOperationMode(),
            eventCommAreaNonFinance.getProductObjectCode());
        if (coreProductObject == null) {
            throw new BusinessException("CUS-00014", "产品对象表"); // 产品对象表
        }
        return coreProductObject;
    }

    public static MapBean handleMap(Map<String, String> paramMap) {
        Iterator<Map.Entry<String, String>> paramMapIterator = paramMap.entrySet().iterator();
        MapBean mapBean = new MapBean();
        while (paramMapIterator.hasNext()) {
            Map.Entry<String, String> entry = paramMapIterator.next();
            mapBean.setKey(entry.getKey().split("_")[0]);
            mapBean.setValue(entry.getValue());
        }
        if (StringUtil.isBlank(mapBean.getKey()) || StringUtil.isEmpty(mapBean.getKey())) {
            throw new BusinessException("COR-10003");
        }
        return mapBean;
    }

    public static void main(String[] args) {
        String a = "ider-nofn123";
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        System.out.println(m.replaceAll("").trim());
    }
}
