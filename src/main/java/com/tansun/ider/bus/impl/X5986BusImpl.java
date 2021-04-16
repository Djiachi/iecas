package com.tansun.ider.bus.impl;

import com.tansun.ider.bus.X5986Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.enums.SubAccountIdentify;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5980BO;
import com.tansun.ider.model.vo.X5986VO;
import com.tansun.ider.service.QueryAccountService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2020/04/10
 * @updater:
 * @description: 资产证券化查询-明细查询 初始版本 后已废弃
 * BSS.IQ.01.0345
 */
@Service
public class X5986BusImpl implements X5986Bus {

    @Autowired
    private CoreAccountDao coreAccountDao;
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
    @Resource
    private QueryAccountService queryAccountService;
    private static Logger logger = LoggerFactory.getLogger(X5986BusImpl.class);

    @Override
    public Object busExecute(X5980BO x5980BO) throws Exception {
        PageBean<X5986VO> page = new PageBean<>();

        List<X5986VO> voList = new ArrayList<>();
        // 身份证号
        String idNumber = x5980BO.getIdNumber();
        //证件类型
        String idType = x5980BO.getIdType();
        String planId = x5980BO.getPlanId();
        if (StringUtils.isEmpty(planId)) {
            return page;
        }

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
        coreAccountSqlBuilder.andSubAccIdentifyNotEqualTo(SubAccountIdentify.P.getValue());
        if (StringUtils.isNotEmpty(productObjectCode)) {
            coreAccountSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
        }
        coreAccountSqlBuilder.andAbsPlanIdEqualTo(planId);

        int totalCount = coreAccountDao.countBySqlBuilder(coreAccountSqlBuilder);
        page.setTotalCount(totalCount);

        if (null != x5980BO.getPageSize() && null != x5980BO.getIndexNo()) {
            coreAccountSqlBuilder.orderByAccountId(false);
            coreAccountSqlBuilder.setPageSize(x5980BO.getPageSize());
            coreAccountSqlBuilder.setIndexNo(x5980BO.getIndexNo());
            page.setPageSize(x5980BO.getPageSize());
            page.setIndexNo(x5980BO.getIndexNo());
        }
        if (totalCount > 0) {
            List<CoreAccount> listCoreAccount = coreAccountDao.selectListBySqlBuilder(coreAccountSqlBuilder);

            for (CoreAccount coreAccount : listCoreAccount) {
                X5986VO vo = new X5986VO();
                CachedBeanCopy.copyProperties(coreAccount, vo);
                BigDecimal currentTotalBalance = queryAccountService.queryCurrBalance(vo.getCustomerNo(), vo.getAccountId(), vo.getCurrencyCode(), vo.getAccountOrganForm(), vo.getCycleModeFlag(), null);
                vo.setCurrentTotalBalance(currentTotalBalance);
                voList.add(vo);
            }

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