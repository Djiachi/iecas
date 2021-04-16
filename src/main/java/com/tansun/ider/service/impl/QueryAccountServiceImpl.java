package com.tansun.ider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.CoreInstallmentTransAcctDao;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.commun.ResponseVO;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.EVENT;
import com.tansun.ider.model.vo.X5982VO;
import com.tansun.ider.service.AccountingService;
import com.tansun.ider.service.QueryAccountService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.BigDecimalUtil;
import com.tansun.ider.util.CurrencyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2019/11/21
 * @updater:
 * @description: 查询账户信息公共方法
 */
@Service
public class QueryAccountServiceImpl implements QueryAccountService {

    @Resource
    private CoreInstallmentTransAcctDao coreInstallmentTransAcctDao;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private CoreBalanceUnitDao coreBalanceUnitDao;
    @Value("${global.target.service.url.auth}")
    private String authUrl;
    @Autowired
    private CurrencyUtils currencyUtils;

    /**
     * 查询分期交易账户信息表
     *
     * @param accountId    账户号
     * @param currencyCode 币种
     * @return
     * @throws BusinessException
     */
    @Override
    public CoreInstallmentTransAcct queryCoreInstallmentTransAcct(String accountId, String currencyCode)
            throws BusinessException {
        CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilder = new CoreInstallmentTransAcctSqlBuilder();
        coreInstallmentTransAcctSqlBuilder.andAccountIdEqualTo(accountId);
        if (StringUtils.isNotEmpty(currencyCode)) {
            coreInstallmentTransAcctSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        }
        List<CoreInstallmentTransAcct> coreInstallmentTransAccts = coreInstallmentTransAcctDao
                .selectListBySqlBuilder(coreInstallmentTransAcctSqlBuilder);
        if (null == coreInstallmentTransAccts || coreInstallmentTransAccts.size() == 0) {
            throw new BusinessException("COR-10001");
        }
        return coreInstallmentTransAccts.get(0);
    }

    @Override
    public BigDecimal queryCurrBalanceUnit(String accountId, String currencyCode)
            throws Exception {

        BigDecimal totalBalance = BigDecimal.ZERO;
        // 循环账户 余额单元 之和
        CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
        coreBalanceUnitSqlBuilder.andAccountIdEqualTo(accountId);
        coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
        List<CoreBalanceUnit> coreBalanceUnits = coreBalanceUnitDao
                .selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
        if (null != coreBalanceUnits) {
            totalBalance = coreBalanceUnits.stream().map(CoreBalanceUnit::getBalance).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
        }
        totalBalance = currencyUtils.conversionAmount(totalBalance, currencyCode,
                CurrencyUtils.output);

        return totalBalance;
    }

    /**
     * 查询实时余额
     *
     * @param currencyCode 币种
     * @return 实时余额
     * @throws Exception
     */
    @Override
    public BigDecimal queryCurrBalance(String customerNo, String accountId, String currencyCode, String accountOrganForm,
                                       String cycleModeFlag, BigDecimal remainPrincipalAmount)
            throws Exception {

        X5982VO x5982VO = new X5982VO();
        x5982VO.setAccountId(accountId);
        x5982VO.setCustomerNo(customerNo);
        x5982VO.setCurrencyCode(currencyCode);
        BigDecimal totalBalance = BigDecimal.ZERO;
        if (StringUtils.equals(accountOrganForm, Constant.ACCOUNT_ORGAN_FORM_S)
                && StringUtils.equals(cycleModeFlag, YesOrNo.YES.getValue())) {
            x5982VO.setAuthDataSynFlag("1");
            String params = JSON.toJSONString(x5982VO, SerializerFeature.DisableCircularReferenceDetect);
            String triggerEventNo = EVENT.AUS_IQ_01_0004;
            RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<String> entity = new HttpEntity<String>(params, headers);
            String response = restTemplate.postForObject(authUrl + triggerEventNo, entity, String.class);
            ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class,
                    Feature.DisableCircularReferenceDetect);
            if (responseVO.getReturnData() != null) {
                JSONObject jsonObj = JSON.parseObject(responseVO.getReturnData().toString());
                Object rows = jsonObj.get("rows");
                if (rows != null) {
                    List<X5982VO> list = JSON.parseArray(rows.toString(), X5982VO.class);
                    if (list != null && list.size() > 0) {
                        X5982VO obj = list.get(0);
                        if (obj != null) {
                            x5982VO.setTotalBalance(obj.getTotalBalance());
                        }
                    }
                }
            }
            if (null == remainPrincipalAmount) {
                // 分期账户：实时余额 + 未抛分期本金
                CoreInstallmentTransAcct coreInstallmentTransAcct = queryCoreInstallmentTransAcct(
                        accountId, currencyCode);
                if (null != coreInstallmentTransAcct) {
                    BigDecimal amount = accountingService.getInstallmentTransAmount(coreInstallmentTransAcct);
                    totalBalance = amount.add(BigDecimalUtil.nullToZero(x5982VO.getTotalBalance()));
                }
            } else {
                totalBalance = remainPrincipalAmount.add(BigDecimalUtil.nullToZero(x5982VO.getTotalBalance()));
            }

        } else {
            // 循环账户 余额单元 之和
            CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
            coreBalanceUnitSqlBuilder.andAccountIdEqualTo(x5982VO.getAccountId());
            coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(currencyCode);
            List<CoreBalanceUnit> coreBalanceUnits = coreBalanceUnitDao
                    .selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
            if (null != coreBalanceUnits) {
                totalBalance = coreBalanceUnits.stream().map(CoreBalanceUnit::getBalance).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
            }
            totalBalance = currencyUtils.conversionAmount(totalBalance, currencyCode,
                    CurrencyUtils.output);
        }
        return totalBalance;
    }
}
