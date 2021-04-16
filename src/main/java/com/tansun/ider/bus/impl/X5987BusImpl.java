package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5987Bus;
import com.tansun.ider.dao.beta.entity.*;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5975BO;
import com.tansun.ider.model.vo.X5115VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.ParamsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询 参考5116
 * BSS.IQ.01.0350
 */
@Service
public class X5987BusImpl implements X5987Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    @Autowired
    private ParamsUtil paramsUtil;
    public static final String ACC_CHILD = "childAcc";
    public static final String ACC_MAIN = "mainAcc";
    public static final String PAGE_MAIN = "mainPage";
    private static Logger logger = LoggerFactory.getLogger(X5987BusImpl.class);

    @Override
    public Object busExecute(X5975BO x5975BO) throws Exception {
        PageBean<X5115VO> page = new PageBean<>();
        // 身份证号
        String idNumber = x5975BO.getIdNumber();
        //证件类型
        String idType = x5975BO.getIdType();
        String flag = x5975BO.getFlag();
        // 外部识别号
        String externalIdentificationNo = x5975BO.getExternalIdentificationNo();
        String customerNo = null;
        String operationMode = null;
        String accountId = x5975BO.getAccountId();
        String accountOrganForm = x5975BO.getAccountOrganForm();
        //账户还款标志
        String payFlag = x5975BO.getPayFlag();
        //账户形式
        String accFlag = x5975BO.getAccFlag();
        String pageFlag = x5975BO.getPageFlag();
        String currencyCode = x5975BO.getCurrencyCode();
        String businessProgramNo = x5975BO.getBusinessProgramNo();
        //全局流水号
        String globalTransSerialNo = x5975BO.getGlobalTransSerialNo();
        //业务类型
        String businessTypeCode = x5975BO.getBusinessTypeCode();
        CoreMediaBasicInfo coreMediaBasicInfo;
        String productObjectCode = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if (object instanceof CoreCustomer) {
            CoreCustomer coreCustomer = (CoreCustomer) object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        } else if (object instanceof CoreMediaBasicInfo) {
            coreMediaBasicInfo = (CoreMediaBasicInfo) object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
            operationMode = coreMediaBasicInfo.getOperationMode();
            productObjectCode = coreMediaBasicInfo.getProductObjectCode();
        }

        CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
        if (Constant.FLAG_N.equals(flag)) {
            if (StringUtil.isNotBlank(externalIdentificationNo)) {
                EventCommArea eventCommArea = new EventCommArea();
                //运营模式
                eventCommArea.setEcommOperMode(operationMode);
                //产品对象代码
                eventCommArea.setEcommProdObjId(productObjectCode);
                List<CoreActivityArtifactRel> artifactList = x5975BO.getActivityArtifactList();
                String reproductObjectCode = this.getProObjCode(eventCommArea, artifactList);
                //集中核算
                if ("0".equals(reproductObjectCode)) {
                    coreAccountSqlBuilder.andProductObjectCodeEqualTo("0");
                } else {//反之，正常进行赋值操作
                    coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                }
            }
        }
        //根据账号还款 ，查询出借记账户
        if (payFlag != null && payFlag.equals(YesOrNo.YES.getValue())) {
            coreAccountSqlBuilder.andBusinessDebitCreditCodeEqualTo(Constant.BOOKKEEPINGDIREC_D);
            CoreAccountSqlBuilder orSqlBuilder = new CoreAccountSqlBuilder();
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT00001");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT00002");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT11111");
            orSqlBuilder.orBusinessTypeCodeEqualTo("MODT11112");
            coreAccountSqlBuilder.and(orSqlBuilder);
        } else {
            if (StringUtil.isNotBlank(accountOrganForm)) {
                coreAccountSqlBuilder.andAccountOrganFormEqualTo(accountOrganForm);
            }
        }
        if (pageFlag != null && PAGE_MAIN.equals(pageFlag)) {
            coreAccountSqlBuilder.andAbsPlanIdIsNotNull();
            CoreAccountSqlBuilder sqlBuilder = new CoreAccountSqlBuilder();
            sqlBuilder.andSubAccIdentifyEqualTo(SubAccountIdentify.P.getValue()).orSubAccIdentifyEqualTo(SubAccountIdentify.S.getValue());
            coreAccountSqlBuilder.and(sqlBuilder);
        }
        if (accFlag != null && ACC_MAIN.equals(accFlag)) {
            //查询主账户信息。和循环类子账户
            //对于账户组织形式为R的，根据主账户业务类型查询“交易识别码”或“资金方代码”不为空的账户；
            if (accountOrganForm != null && accountOrganForm.equals(Constant.ACCOUNT_ORGAN_FORM_R) && businessTypeCode != null) {
                coreAccountSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
                coreAccountSqlBuilder.andBusinessTypeCodeEqualTo(businessTypeCode);
                coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
                coreAccountSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.P.getValue());
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.S.getValue());
            } else if (accountOrganForm != null && accountOrganForm.equals(Constant.ACCOUNT_ORGAN_FORM_S) && globalTransSerialNo != null) {
                coreAccountSqlBuilder.andGlobalTransSerialNoEqualTo(globalTransSerialNo);
                //查询展示“交易识别码”或“资金方代码”不为空的账户
                coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.P.getValue());
                coreAccountSqlBuilder.orderByTransIdentifiNo(true);
                coreAccountSqlBuilder.orderByFundNum(true);
                coreAccountSqlBuilder.orderByAccountId(true);
                //未实现用交易识别码、资金方代码分组后按账户代码排序
            }
        }
        if (StringUtil.isNotBlank(customerNo)) {
            coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (accountId != null && accountId.length() != 0) {
            coreAccountSqlBuilder.andAccountIdEqualTo(accountId);
        }
        //排除额度子账户
        coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.Q.getValue());

        int totalCount = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5975BO.getPageSize() && null != x5975BO.getIndexNo()) {
            coreAccountSqlBuilder.setIndexNo(x5975BO.getIndexNo());
            coreAccountSqlBuilder.setPageSize(x5975BO.getPageSize());
            page.setPageSize(x5975BO.getPageSize());
            page.setIndexNo(x5975BO.getIndexNo());
        }
        List<X5115VO> listX5115VO = new ArrayList<>();
        String entrys = Constant.EMPTY_LIST;
        if (totalCount > 0) {
            coreAccountSqlBuilder.orderByAccountId(false);
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
            //2020-5-29 显示ABS计划的阶段
            List<CoreAccountingAssetPlan> accountingAssetPlanList = httpQueryService.queryCoreAccountingAssetPlanList(operationMode, null, null);
            if (accountingAssetPlanList.isEmpty()) {
                throw new BusinessException("");
            }
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
                    CoreCustomerBillDay coreCustomerBillDay =
                            httpQueryService.queryAccountCustomerBillDay(coreAccount.getCustomerNo(),
                                    coreAccount.getBusinessProgramNo(),
                                    externalIdentificationNo, idType, idNumber);
                    if (null != coreCustomerBillDay) {
                        x5115VO.setNextBillDate(coreCustomerBillDay.getNextBillDate());
                        x5115VO.setCurrentCycleNumber(coreCustomerBillDay.getCurrentCycleNumber());
                    }
                }
                if (StringUtil.isNotBlank(x5115VO.getCurrencyCode())) {
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5115VO.getCurrencyCode());
                    if (null != coreCurrency) {
                        x5115VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
                //获取机构名称
                if (StringUtil.isNotBlank(x5115VO.getOrganNo())) {
                    CoreOrgan coreOrgan = httpQueryService.queryOrgan(x5115VO.getOrganNo());
                    if (null != coreOrgan) {
                        x5115VO.setOrganName(coreOrgan.getOrganName());
                    }
                }
                x5115VO.setAccountingStatusCode(coreAccount.getAccountingStatusCode());
                x5115VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                if (accFlag != null && ACC_MAIN.equals(accFlag)) {
                    x5115VO.setFlag(ACC_CHILD);
                }
                if (pageFlag != null && PAGE_MAIN.equals(pageFlag)) {
                    x5115VO.setHaveChild(true);
                }
                //子账户标识为L时，不触发授权查询额度
                if (!(SubAccountIdentify.L.getValue().equals(coreAccount.getSubAccIdentify()))) {
                    // 查询当前余额
                    queryCurrBalance(x5115VO);
                }
                queryPlanDetails(x5115VO, accountingAssetPlanList);
                queryCapitalOrganizationName(x5115VO);
                listX5115VO.add(x5115VO);
            }
            page.setRows(listX5115VO);
            if (null != listX5115VO && !listX5115VO.isEmpty()) {
                entrys = listX5115VO.get(0).getId();
            }
        }
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5975BO.getCoreEventActivityRel().getEventNo(),
                x5975BO.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
                x5975BO.getOperatorId());
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
        x5115vo.setRequestType("1");
        String params = JSON.toJSONString(x5115vo, SerializerFeature.DisableCircularReferenceDetect);
        String triggerEventNo = EVENT.AUS_IQ_01_0004;
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);
        String response = restTemplate.postForObject(authUrl + triggerEventNo, entity, String.class);
        ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class, Feature.DisableCircularReferenceDetect);
        if (responseVO.getReturnData() != null) {
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

    /**
     * 查询abs计划内容
     *
     * @param x5115vo
     * @param accountingAssetPlanList
     */
    private void queryPlanDetails(X5115VO x5115vo, List<CoreAccountingAssetPlan> accountingAssetPlanList) {
        CoreAccountingAssetPlan plan;
        Optional<CoreAccountingAssetPlan> first = accountingAssetPlanList.stream().filter(x -> StringUtils.equals(x.getPlanId(), x5115vo.getAbsPlanId())).findFirst();

        if (first.isPresent()) {
            plan = first.get();
            x5115vo.setAccountType(plan.getAccountType());
        }
    }

    /**
     * 查询资金法人名称
     *
     * @param vo
     */
    private void queryCapitalOrganizationName(X5115VO vo) throws Exception {
        String fundNum;
        if (StringUtils.isEmpty(vo.getFundNum())) {
            //客户上
            CoreCustomer coreCustomer = queryCustomerService.queryCustomer(vo.getCustomerNo());
            fundNum = coreCustomer.getCorporationEntityNo();
            vo.setFundNum(fundNum);
        } else {
            fundNum = vo.getFundNum();
        }

        CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(fundNum, null, "1");
        if (coreCorporationEntity == null) {
            logger.error("X5987BusImpl.,查询coreFundInfo不存在,FundNum:{}", vo.getFundNum());
            throw new BusinessException("COR-13115");
        }
        vo.setCapitalOrganizationName(coreCorporationEntity.getCorporationEntityName());
    }

}
