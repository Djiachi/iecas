package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4025Bus;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.impl.CoreBudgetOrgCustRelDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.MapBean;
import com.tansun.ider.model.bo.X4025BO;
import com.tansun.ider.model.vo.X4005VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.HttpQueryServiceByGns;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> Title: X4025BusImpl </p>
 * <p> Description: 预算单位与员工客户关系建立</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月11日
 */
@Service
public class X4025BusImpl implements X4025Bus {
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDaoImpl;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreBudgetOrgCustRelDaoImpl coreBudgetOrgCustRelDaoImpl;
    @Value("${spring.application.name}")
    private String gnsInfo;
    @Autowired
    private HttpQueryServiceByGns httpQueryServiceByGns;
    @Value("${global.target.service.url.nofn}")
    private String nofnUrl;

    @Override
    public Object busExecute(X4025BO x4025bo) throws Exception {
        // 判断输入的各字段是否为空
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x4025bo, eventCommAreaNonFinance);
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
        eventCommAreaNonFinance.setOperationMode(coreCustomer.getOperationMode());
        // 查询产品对象对应的业务项目代码
        List<CoreProductBusinessScope> queryProductBusinessScopes =
                httpQueryService.queryProductBusinessScope(x4025bo.getProductObjectCode(), x4025bo.getOperationMode());
        if (CollectionUtils.isNotEmpty(queryProductBusinessScopes)) {
            // 查询业务项目对应的构件元件值,如果为预算单位业务,则预算单位编码必须有.
            EventCommArea eventCommArea = new EventCommArea();
            eventCommArea.setEcommOperMode(queryProductBusinessScopes.get(0).getOperationMode());// 运营模式
            eventCommArea.setEcommBusinessProgramCode(queryProductBusinessScopes.get(0).getBusinessProgramNo());// 业务项目代码
            CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
            Map<String, String> baseLoanRateMap = artService.getElementByArtifact(Constant.ARTIFACT_NO_501, eventCommArea);
            MapBean baseLoanRateMode = handleMap(baseLoanRateMap);
            if (Constant.BUSINESS_BY_BUDGETUNIT.equals(baseLoanRateMode.getKey())) {
                if (x4025bo.getBudgetOrgCode() != null) {
                    // 查询预算单位所在的片区编号，判断预算单位和客户的片区编号是否相同
                    X4005VO x4005Vo = new X4005VO();
                    String queryGnsCode = httpQueryServiceByGns.queryGnsCode(nofnUrl, x4025bo.getBudgetOrgCode());
                    if (StringUtil.isNotBlank(queryGnsCode)) {
                        x4005Vo = JSON.parseObject(queryGnsCode, X4005VO.class, Feature.DisableCircularReferenceDetect);
                        String budgetGnsCode = x4005Vo.getGnsNumber();
                        // 如果预算单位和客户在一个片区里则直接保存。
                        if (budgetGnsCode.equals(gnsInfo)) {
                            // 建立客户与预算单位关联关系
                            CoreBudgetOrgCustRel coreBudgetOrgCustRel = new CoreBudgetOrgCustRel();
                            coreBudgetOrgCustRel.setId(RandomUtil.getUUID());
                            coreBudgetOrgCustRel.setCustomerNo(coreCustomer.getCustomerNo());
                            coreBudgetOrgCustRel.setExternalIdentificationNo(eventCommAreaNonFinance.getExternalIdentificationNo());// 外部识别号
                            coreBudgetOrgCustRel.setIdType(coreCustomer.getIdType());
                            coreBudgetOrgCustRel.setIdNumber(coreCustomer.getIdNumber());
                            String regEx = "[^0-9]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(gnsInfo);
                            coreBudgetOrgCustRel.setCustomerArea(m.replaceAll("").trim());// 持卡人信息所在片区
                            coreBudgetOrgCustRel.setBudgetOrgCode(x4025bo.getBudgetOrgCode());
                            coreBudgetOrgCustRel.setVersion(1);
                            int insert = coreBudgetOrgCustRelDaoImpl.insert(coreBudgetOrgCustRel);
                            if (insert != 1) {
                                throw new BusinessException("COR-11006");
                            }
                        }
                        // 如果预算单位和客户不在一个片区中则，保存关联关系信息时必须在预算单位所在的片区保存
                        else {
                            httpQueryServiceByGns.insertCoreBudgetOrgCustRelByGns(gnsInfo, nofnUrl, x4025bo.getBudgetOrgCode(),
                                coreCustomer.getCustomerNo(), coreCustomer.getIdType(), coreCustomer.getIdNumber(),
                                eventCommAreaNonFinance.getExternalIdentificationNo());
                        }

                    }
                }
                else {
                    throw new BusinessException("COR-11005");
                }
            }
        }
        return eventCommAreaNonFinance;
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

}
