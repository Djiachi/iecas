package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5115Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5115BO;
import com.tansun.ider.model.vo.X5115VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 客户账户列表
 * @author: admin
 */
@Service
public class X5115BusImpl implements X5115Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private ParamsUtil paramsUtil;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    static final String BSS_IQ_01_0113 = "BSS.IQ.01.0113";
    
    
	@Override
    public Object busExecute(X5115BO x5115bo) throws Exception {
        PageBean<X5115VO> page = new PageBean<>();
        // 身份证号
        String idNumber = x5115bo.getIdNumber();
        //证件类型
        String idType = x5115bo.getIdType();
        String flag = x5115bo.getFlag();
        // 外部识别号
        String externalIdentificationNo = x5115bo.getExternalIdentificationNo();
        String customerNo = null;
        String operationMode = null;
        String accountId = x5115bo.getAccountId();
        String accountOrganForm = x5115bo.getAccountOrganForm();
        //账户还款标志
        String payFlag = x5115bo.getPayFlag();
        x5115bo.getGlobalEventNo();
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        String productObjectCode = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
			operationMode = coreMediaBasicInfo.getOperationMode();
			productObjectCode = coreMediaBasicInfo.getProductObjectCode();
		}
		
		String globalEventNo = x5115bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		// 根据事件编号是否输入
		if (BSS_IQ_01_0113.equals(eventNo)&&Constant.FLAG_N.equals(flag)) {
			if (StringUtil.isNotBlank(externalIdentificationNo)) {
				EventCommArea eventCommArea = new EventCommArea();
			    eventCommArea.setEcommOperMode(operationMode);//运营模式
			    eventCommArea.setEcommProdObjId(productObjectCode);//产品对象代码
		        List<CoreActivityArtifactRel> artifactList = x5115bo.getActivityArtifactList();
				String reproductObjectCode = this.getProObjCode(eventCommArea, artifactList);
				//集中核算
				 if("0".equals(reproductObjectCode)){
					 coreAccountSqlBuilder.andProductObjectCodeEqualTo("0");
	             }else{//反之，正常进行赋值操作
	            	 coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
	             }
			}
        }
        //根据账号还款 ，查询出借记账户
        if (payFlag !=null && payFlag.equals("Y")) {
            coreAccountSqlBuilder.andBusinessDebitCreditCodeEqualTo("D");
            CoreAccountSqlBuilder orSqlBuilder = new CoreAccountSqlBuilder();
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT00001");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT00002");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT11111");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT11112");
            coreAccountSqlBuilder.and(orSqlBuilder);
            //coreAccountSqlBuilder.andBusinessDebitCreditCodeEqualTo("D").andBusinessTypeCodeEqualTo("MODT00001")
            //coreAccountSqlBuilder.andBusinessType
        }

        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        if (accountId != null && accountId.length() != 0) {
            coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        }
        if(StringUtil.isNotBlank(accountOrganForm)){
            coreAccountSqlBuilder.andAccountOrganFormEqualTo(accountOrganForm);
        }
        int totalCount = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5115bo.getPageSize() && null != x5115bo.getIndexNo()) {
            coreAccountSqlBuilder.setIndexNo(x5115bo.getIndexNo());
            coreAccountSqlBuilder.setPageSize(x5115bo.getPageSize());
            page.setPageSize(x5115bo.getPageSize());
            page.setIndexNo(x5115bo.getIndexNo());
        }
        List<X5115VO> listX5115VO = new ArrayList<>();
        String entrys = Constant.EMPTY_LIST;
        if (totalCount > 0) {
            coreAccountSqlBuilder.orderByAccountId(false);
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            for (CoreAccount coreAccount : listCoreAccount) {
                // 查询业务项目描述
                CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                        coreAccount.getBusinessTypeCode());
                X5115VO x5115VO = new X5115VO();
                CachedBeanCopy.copyProperties(coreAccount, x5115VO);
                if (StringUtil.isNotBlank(coreAccount.getProductObjectCode())) {
                    CoreProductObject coreProductObject = httpQueryService
                            .queryProductObject(coreAccount.getOperationMode(), coreAccount.getProductObjectCode());
                    if (coreProductObject != null) {
                        x5115VO.setProductDesc(coreProductObject.getProductDesc());
                    }
                }
                if (StringUtil.isNotBlank(coreAccount.getBusinessProgramNo())) {
                    CoreBusinessProgram coreBusinessProgram = httpQueryService
                    		.queryBusinessProgram(coreAccount.getOperationMode(), coreAccount.getBusinessProgramNo());
                    if (coreBusinessProgram != null) {
                        x5115VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
                    }
                }
                if (StringUtil.isNotBlank(coreAccount.getCustomerNo()) && StringUtil.isNotBlank(coreAccount.getBusinessProgramNo())) {
                	CoreCustomerBillDay coreCustomerBillDay = httpQueryService.queryAccountCustomerBillDay(coreAccount.getCustomerNo(), coreAccount.getBusinessProgramNo(), 
                			externalIdentificationNo, idType, idNumber);
                	if(null != coreCustomerBillDay){
                		x5115VO.setNextBillDate(coreCustomerBillDay.getNextBillDate());
                		x5115VO.setCurrentCycleNumber(coreCustomerBillDay.getCurrentCycleNumber());
                	}
                }
                if(StringUtil.isNotBlank(x5115VO.getCurrencyCode())){
                	CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5115VO.getCurrencyCode());
                	if(null != coreCurrency){
                		x5115VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                	}
                }
                //获取机构名称
                if(StringUtil.isNotBlank(x5115VO.getOrganNo())){
                	CoreOrgan coreOrgan = httpQueryService.queryOrgan(x5115VO.getOrganNo());
                	if(null != coreOrgan){
                		x5115VO.setOrganName(coreOrgan.getOrganName());
                	}
                }
                
                x5115VO.setAccountingStatusCode(coreAccount.getAccountingStatusCode());
                x5115VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                // 查询当前余额
                queryCurrBalance(x5115VO);
                listX5115VO.add(x5115VO);
            }
            page.setRows(listX5115VO);
            if (null != listX5115VO && !listX5115VO.isEmpty()) {
                entrys = listX5115VO.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5115bo.getCoreEventActivityRel().getEventNo(),
                x5115bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5115bo.getOperatorId());
        return page;
    }
    
    private String getProObjCode(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList) throws Exception {
        String productOjectCode = "";
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                // 单独核算
                productOjectCode = eventCommArea.getEcommProdObjId();
            } else {
                // 集中核算，产品对象置空
                productOjectCode = "0";
            }
        }
        return productOjectCode;
    }
    /**
     * 查询实时余额
     * 
     * @param x5115vo
     */
    private void queryCurrBalance(X5115VO x5115vo) {
        x5115vo.setAuthDataSynFlag("1");
        String params = JSON.toJSONString(x5115vo, SerializerFeature.DisableCircularReferenceDetect);
        String triggerEventNo = EVENT.AUS_IQ_01_0004;
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);
        String response = restTemplate.postForObject(authUrl + triggerEventNo, entity, String.class);
        ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
        if(responseVO.getReturnData() != null){
        	JSONObject jsonObj = JSON.parseObject(responseVO.getReturnData().toString());
            Object rows = jsonObj.get("rows");
            if (rows != null) {
                List<X5115VO> list = JSON.parseArray(rows.toString(), X5115VO.class);
                if (list != null && list.size() > 0) {
                    X5115VO obj = list.get(0);
                    if (obj != null) {
                        x5115vo.setTotalBalance(obj.getTotalBalance());
                    }
                }
            }	
        }
    }

}
