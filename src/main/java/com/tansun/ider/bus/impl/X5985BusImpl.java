package com.tansun.ider.bus.impl;

import com.tansun.ider.bus.X5985Bus;
import com.tansun.ider.dao.beta.entity.CoreAccountingAssetPlan;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5980BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: PanQi
 * @Date: 2020/04/10
 * @updater:
 * @description: 资产证券化查询 初始版本 后已废弃
 * BSS.IQ.01.0345
 */
@Service
public class X5985BusImpl implements X5985Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    @Autowired
    public CoreNmnyLogDao coreNmnyLogDao;
    @Autowired
    public CoreNmnyLogBDao coreNmnyLogBDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private ParamsUtil paramsUtil;

    private static Logger logger = LoggerFactory.getLogger(X5985BusImpl.class);

    @Override
    public Object busExecute(X5980BO x5980BO) throws Exception {
        PageBean<CoreAccountingAssetPlan> page = new PageBean<>();
        // 身份证号
        String idNumber = x5980BO.getIdNumber();
        //证件类型
        String idType = x5980BO.getIdType();
        // 外部识别号
        String externalIdentificationNo = x5980BO.getExternalIdentificationNo();
        String customerNo = null;
        String operationMode = null;
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

        coreAccountSqlBuilder.andCustomerNoEqualTo(customerNo);
        coreAccountSqlBuilder.andOperationModeEqualTo(operationMode);
        if (StringUtils.isNotEmpty(productObjectCode)) {
            coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        }
        coreAccountSqlBuilder.andAbsPlanIdIsNotNull();

        int totalCount = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        if (totalCount > 0) {

        }

        List<CoreAccountingAssetPlan> voList = new ArrayList<>();

        List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);
        if (!listCoreAccount.isEmpty()) {
            Set<String> planIdList = listCoreAccount.stream().map(x -> x.getAbsPlanId()).collect(Collectors.toSet());

            List<CoreAccountingAssetPlan> accountingAssetPlanList = httpQueryService.queryCoreAccountingAssetPlanList(operationMode, null, null);
            if (accountingAssetPlanList.isEmpty()) {
                throw new BusinessException("");
            }
            voList = accountingAssetPlanList.stream().filter(x -> planIdList.contains(x.getPlanId())).collect(Collectors.toList());
        }
        page.setRows(voList);
        String entrys = Constant.EMPTY_LIST;
        // 记录查询日志
        CoreEvent tempObject = new CoreEvent();
        paramsUtil.logNonInsert(x5980BO.getLogEventId(),
                x5980BO.getLogActivityId(), tempObject, tempObject, entrys,
                x5980BO.getOperatorId());
        return page;
    }
}