package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.bus.X4030Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ResponseType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4030BO;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CardUtil;

/**
 * <p> Title: X4030BusImpl </p>
 * <p> Description: 单位公务卡额度授信</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
@Service
public class X4030BusImpl implements X4030Bus {
	@Resource
	private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
	@Resource
	private HttpQueryServiceByGns httpQueryServiceByGns;
	@Value("${global.target.service.url.auth}")
	private String authUrl;
	@Resource
	private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryServiceImpl httpQueryServiceImpl;
	
    @Override
    public Object busExecute(X4030BO x4030bo) throws Exception {
        
    	//验证授信金额
    	if(BigDecimal.ZERO.compareTo(x4030bo.getCreditLimit())>= 0){
    		//TODO 抛出异常，金额必须大于0
    		throw new BusinessException("CUS-12040");
    	}
    	//验证开始结束日期
    	if("T".equals(x4030bo.getCreditType())
    			&& x4030bo.getLimitEffectvDate().compareTo(x4030bo.getLimitExpireDate())>0){
    		//TODO 零时额度的生效时间必须小于截止时间
    		throw new BusinessException("CUS-12041");
    	}
    	CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerMedia(x4030bo.getExternalIdentificationNo());
    	CoreBusinessProgram coreBusinessProgram = queryBusinessProgram(coreMediaBasicInfo.getProductObjectCode(), coreMediaBasicInfo.getOperationMode());
    	if (ResponseType.RESPONSE_TYPE_PSN.getValue().equals(coreBusinessProgram.getResponseType())) {
    		//TODO 该公务卡为个人承责
    		throw new BusinessException("CUS-12046");
		}
    	//查询改预算编号下的所有额度总和
    	//BigDecimal amtQuota = queryAllQuota(x4030bo);
    	//查询该预算单位的总额度
    	CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = queryOrgAddInfo(x4030bo);
    	//查询该预算单位下可用额度
    	BigDecimal restQuota = coreBudgetOrgAddInfo.getOrgRestQuota();
    	//第一步，验证金额
    	CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
    	int currencyDecimal = cardUtil.getCurrencyDecimal(x4030bo.getCurrencyCode());
    	restQuota = CurrencyConversionUtil.reduce(restQuota, currencyDecimal);
		//BigDecimal reduceCreditLimit = CurrencyConversionUtil.reduce(x4030bo.getCreditLimit(), currencyDecimal);
    	if(restQuota.compareTo(x4030bo.getCreditLimit())<0){
    		//TODO 抛出异常，该预算单位下额度不足
    		throw new BusinessException("CUS-12042",restQuota.toString());
    	}
    	//更新可用额度
    	updateOrgRestQuota(coreBudgetOrgAddInfo,CurrencyConversionUtil.expand(x4030bo.getCreditLimit(), currencyDecimal));
    	//第二部调用授权接口
    	return triggerOtherEvent(x4030bo,coreMediaBasicInfo.getProductObjectCode(),coreMediaBasicInfo.getMainCustomerNo());
    	
    } 
    
    /**
     * 查询客户下所有有效媒介
     * 
     * @param customerNo
     * @return
     * @throws Exception
     */
    private CoreMediaBasicInfo queryCustomerMedia(String externalIdentificationNo) throws Exception {
        CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
        coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo(Constant.MEDIA_INVALID_FLAG);
        coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
        if (null == coreMediaBasicInfo) {
            throw new BusinessException("COR-00001");
        }
        return coreMediaBasicInfo;
    }
    
    //查询预算单位下已使用额度
//    public BigDecimal queryAllQuota(X4030BO x4030bo) throws Exception{
//    	CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
//    	coreBudgetOrgCustRelSqlBuilder.andBudgetOrgCodeEqualTo(x4030bo.getBudgetOrgCode());
//    	List<CoreBudgetOrgCustRel> lists = coreBudgetOrgCustRelDao.selectListBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
//    	BigDecimal amtQuota = BigDecimal.ZERO;
//    	if(lists!=null && lists.size()>0){
//    		for(CoreBudgetOrgCustRel coreBudgetOrgCustRel : lists){
//    			amtQuota = amtQuota.add(httpQueryServiceByGns.queryGnsQuota(authUrl, coreBudgetOrgCustRel.getExternalIdentificationNo()));
//    		}
//    	}
//    	return amtQuota;
//    }
    
    public CoreBudgetOrgAddInfo queryOrgAddInfo(X4030BO x4030bo) throws Exception{
    	CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
    	coreBudgetOrgCustRelSqlBuilder.andExternalIdentificationNoEqualTo(x4030bo.getExternalIdentificationNo());
    	CoreBudgetOrgCustRel coreBudgetOrgCustRel = coreBudgetOrgCustRelDao.selectBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
    	String budgetOrgCode = coreBudgetOrgCustRel.getBudgetOrgCode();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder  = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andIdNumberEqualTo(budgetOrgCode);
        coreCustomerSqlBuilder.andIdTypeEqualTo("7");
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if(coreCustomer == null){
        	//TODO 预算单位不存在
        	throw new BusinessException("CUS-12043");
        }
        //查询预算单位信息
        CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
        coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
        if(coreBudgetOrgAddInfo == null){
        	//TODO 预算单位信息不存在
        	throw new BusinessException("CUS-12043");
        }
        return coreBudgetOrgAddInfo;
    }
    
    /**
     * 更新预算单位可用额度
     * 
     * @param customerNo
     * @return
     * @throws Exception
     */
    public void updateOrgRestQuota(CoreBudgetOrgAddInfo coreBudgetOrgAddInfo,BigDecimal creditLimit) throws Exception{
    	CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
    	coreBudgetOrgAddInfoSqlBuilder.andIdEqualTo(coreBudgetOrgAddInfo.getId());
    	coreBudgetOrgAddInfoSqlBuilder.andVersionEqualTo(coreBudgetOrgAddInfo.getVersion());
    	coreBudgetOrgAddInfo.setOrgRestQuota(coreBudgetOrgAddInfo.getOrgRestQuota().subtract(creditLimit));
    	coreBudgetOrgAddInfo.setVersion(coreBudgetOrgAddInfo.getVersion()+1);
    	int i = coreBudgetOrgAddInfoDao.updateBySqlBuilder(coreBudgetOrgAddInfo, coreBudgetOrgAddInfoSqlBuilder);
    	if (i < 1) {
			//TODO 额度更新失败
    		throw new BusinessException("CUS-12047");
		}
    }
    
    /**
     * 触发其他事件
     *
     * @param eventCommArea
     * @param x4030vo
     * @throws Exception
     */
    private EventCommAreaNonFinance triggerOtherEvent(X4030BO x4030bo,String productObjectCode,String customerNo) throws Exception {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        Map<String, Object> paramsMap2 = new HashMap<String, Object>();
        //运营模式
        paramsMap.put(Constant.PARAM_OPER_MODE, x4030bo.getOperationMode());
        // 客户号
        paramsMap.put(Constant.CUSTOMER_NO, customerNo);
        // 入账币种：ecommField51
        paramsMap.put(Constant.PARAM_CURRENCY_CODE, x4030bo.getCurrencyCode());
        // 外部识别号
        paramsMap.put(Constant.EXTER_IDENTIFI_NO, x4030bo.getExternalIdentificationNo());
        // 产品对象
        paramsMap.put(Constant.PARAM_PRODUCT_OBJ, productObjectCode);
        // 授信类型
        paramsMap.put(Constant.CREDIT_TYPE, x4030bo.getCreditType());
        // 额度节点
        paramsMap.put(Constant.CREDIT_NODE_NO, x4030bo.getCreditNodeNo());
        // 授信金额
        paramsMap.put(Constant.CREDIT_LIMIT, x4030bo.getCreditLimit());
        // 操作员id
        paramsMap.put(Constant.OPERATOR_ID, x4030bo.getOperatorId());
        // 额度生效日期
        paramsMap.put(Constant.LIMIT_EFFECTV_DATE, x4030bo.getLimitEffectvDate());
        // 额度失效日期
        paramsMap.put(Constant.LIMIT_EXPIRE_DATE, x4030bo.getLimitExpireDate());
        // 法人实体
        paramsMap.put(Constant.C0RPORATION, x4030bo.getCorporation());
        // "authDataSynFlag": "1"
        paramsMap.put(Constant.AUTH_SYN_FLAG, "1");
        paramsMap2.put(Constant.KEY_TRIGGER_PARAMS, paramsMap);
        list.add(paramsMap2);
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        eventCommAreaNonFinance.setEventCommAreaTriggerEventList(list);
        return eventCommAreaNonFinance;
    }
    
    /**
     * 查找客户业务项目
     *
     * @param productObjectCode,operationMode
     * @throws Exception
     */
    private CoreBusinessProgram queryBusinessProgram(String productObjectCode,String operationMode) throws Exception{
    	List<CoreProductBusinessScope> coreProductBusinessScope = httpQueryServiceImpl.queryProductBusinessScope(productObjectCode, operationMode);
    	if (null == coreProductBusinessScope || coreProductBusinessScope.size() == 0) {
			//TODO 客户业务类型不存在
    		throw new BusinessException("CUS-00059");
		}
    	CoreBusinessProgram coreBusinessProgram = httpQueryServiceImpl.queryBusinessProgram(operationMode, coreProductBusinessScope.get(0).getBusinessProgramNo());
    	if (null == coreBusinessProgram) {
    		//TODO 客户业务项目不存在
    		throw new BusinessException("CUS-00063");
		}
    	return coreBusinessProgram;
    }    
}
