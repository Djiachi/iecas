package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5975Bus;
import com.tansun.ider.dao.beta.entity.*;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.enums.*;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.bo.X5975BO;
import com.tansun.ider.model.vo.X5115VO;
import com.tansun.ider.service.AccountingService;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询 参考5116
 */
@Service
public class X5975BusImpl implements X5975Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private ParamsUtil paramsUtil;
    public static final String ACC_CHILD = "childAcc";
    public static final String ACC_MAIN = "mainAcc";
    public static final String PAGE_MAIN = "mainPage";
    private static Logger logger = LoggerFactory.getLogger(X5975BusImpl.class);
    @Override
    public Object busExecute(X5975BO x5975BO) throws Exception {
        PageBean<X5115VO> page = new PageBean<>();
        if(StringUtils.isEmpty(x5975BO.getType())){
            throw new BusinessException("COR-13119");
        }

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

        CoreAccountSqlBuilder accountSqlBuilder = new CoreAccountSqlBuilder();
        accountSqlBuilder.andCapitalStageIsNull().orCapitalStageEqualTo("").orCapitalStageEqualTo(CapitalStageEnum.REPO.getValue());
        coreAccountSqlBuilder.and(accountSqlBuilder);

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
                // 是否显示操作按钮
                x5115VO.setOperation(judgeOperation(x5975BO.getAccountType(), coreAccount, operationMode, x5975BO.getType()));
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
     * 是否可操作封包
     * @param accountType
     * @param coreAccount 账户
     * @param operationMode 运营模式
     * @return
     * @throws Exception
     */
    private String judgeOperation(String accountType, CoreAccount coreAccount, String operationMode, String transType)throws Exception{

        //非未转让，非回购账户
        if(!(StringUtils.equals(CapitalStageEnum.REPO.getValue(),coreAccount.getCapitalStage()) || StringUtils.isEmpty(coreAccount.getCapitalStage()) )){
            return TureOrFalse.F.getValue();
        }
        //非本行资金
        if(StringUtils.isNotEmpty(coreAccount.getFundNum())){
            CoreCorporationEntity coreCorporationEntity = httpQueryService.queryFundInfo(coreAccount.getFundNum(),null, Constant.SIGN_YES);
            if (coreCorporationEntity == null) {
                logger.error("X5975BusImpl,coreAccount:{},FundNum:{},未找到资金信息", coreAccount.getAccountId(), coreAccount.getFundNum());
                throw new BusinessException("COR-13115");
            }
            //如果是租户法人，则“是否为本行资产”为Y
            if(StringUtils.equals(coreCorporationEntity.getPartnerCategory(),Constant.SIGN_NO)){
                return TureOrFalse.T.getValue();
            }

            if(StringUtils.equals(Constant.SIGN_YES,coreCorporationEntity.getIsBankFunds())){
                return TureOrFalse.F.getValue();
            }else {
                return TureOrFalse.T.getValue();
            }
        }
        if(StringUtils.equals(AccountTypeEnum.B.getValue(),accountType)){
            //不良资产
            if(!accountingService.judgeNonPerformingAssets(operationMode,coreAccount.getAccountingStatusCode())){
                return TureOrFalse.F.getValue();
            }
        }else {
            if(accountingService.judgeNonPerformingAssets(operationMode,coreAccount.getAccountingStatusCode())){
                return TureOrFalse.F.getValue();
            }
        }
        //循环账户 新资产转让
        if (StringUtils.equals(coreAccount.getAccountOrganForm(), Constant.ACCOUNT_ORGAN_FORM_R) && StringUtils.equals(Constant.TRANSFER_NEW,transType)){
            if(StringUtils.equals(CapitalStageEnum.REPO.getValue(),coreAccount.getCapitalStage()) && !StringUtils.equals(SubAccountIdentify.P.getValue(),coreAccount.getSubAccIdentify())){
                //非主账户
                return TureOrFalse.F.getValue();
            }
        }
        return TureOrFalse.T.getValue();
    }
}
