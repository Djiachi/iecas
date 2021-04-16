package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4005Bus;
import com.tansun.ider.dao.beta.entity.CoreCorporationEntity;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4005BO;
import com.tansun.ider.model.vo.X4005VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * <p> Title: X4005BusImpl </p>
 * <p> Description: 预算单位信息查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月23日
 */
@Service
public class X4005BusImpl implements X4005Bus {

    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreCustomerAddrDao coreCustomerAddrDao;
    @Autowired
    private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
    @Autowired
    private NonFinancialLogUtil nonFinancialLogUtil;
    @Value("${spring.application.name}")
    private String gnsInfo;
    private String eventId = "OCS.IQ.01.0001";

    @Override
    public Object busExecute(X4005BO x4005bo) throws Exception {
        // 事件公共公共区
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(x4005bo, eventCommAreaNonFinance);
        //外部识别号
        String externalIdentificationNo = x4005bo.getExternalIdentificationNo();
        // 身份证号
        String idNumber = null;
        if(StringUtil.isBlank(externalIdentificationNo)){
        	idNumber = eventCommAreaNonFinance.getIdNumber();
        }else{
        	//从预算单位单位员工关系表取预算单位编码；
        	CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
        	coreBudgetOrgCustRelSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
        	CoreBudgetOrgCustRel budgetOrgCustRel = coreBudgetOrgCustRelDao.selectBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
        	if(budgetOrgCustRel==null){
        		throw new BusinessException("预算单位单位员工关系表不存在");
        	}
        	idNumber = budgetOrgCustRel.getBudgetOrgCode();
        }
        X4005VO x4005Vo = new X4005VO();
        x4005Vo.setGnsNumber(gnsInfo);
        // 根据预算单位编号查询客户基本信息
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if (coreCustomer != null) {
            CachedBeanCopy.copyProperties(coreCustomer, x4005Vo);
            // 根据客户基本信息中的customerNo查询预算单位地址信息
            CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
            coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
            List<CoreCustomerAddr> coreCustomerAddrList = coreCustomerAddrDao.selectListBySqlBuilder(coreCustomerAddrSqlBuilder);
            if (CollectionUtils.isEmpty(coreCustomerAddrList)) {
                throw new BusinessException("CUS-00000", "此预算单位的地址在系统不存在!");
            }
            x4005Vo.setCoreCoreCustomerAddrs(coreCustomerAddrList);
            // 根据客户基本信息中的customerNo查询预算单位附加信息
            CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
            coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
            CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
            if (coreBudgetOrgAddInfo == null) {
                throw new BusinessException("CUS-00000", "此预算单位的附件信息在系统不存在!");
            }
            x4005Vo.setManageLevelCode(coreBudgetOrgAddInfo.getManageLevelCode());
            int decimal = 2;
            BigDecimal allQuota = CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getOrgAllQuota(), decimal);
            BigDecimal maxQuota = CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getPersonMaxQuota(), decimal);
            x4005Vo.setOrgAllQuota(allQuota);
            x4005Vo.setPersonMaxQuota(maxQuota);
            if (eventId.equals(eventCommAreaNonFinance.getEventNo())) {
                // 记录非金融交易日志
                String organNo = coreCustomer.getInstitutionId();
                CoreOrgan coreOrgan = httpQueryService.queryOrgan(organNo);
                CoreCorporationEntity coreCorporationEntity =
                        httpQueryService.queryCoreCorporationEntity(coreOrgan.getCorporationEntityNo());
                String systemUnitNo = coreCorporationEntity.getSystemUnitNo();
                CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(systemUnitNo);
                nonFinancialLogUtil.createNonFinancialActivityLog(x4005bo.getEventNo(), x4005bo.getActivityNo(),
                    ModificationType.IQQ.getValue(), null, null, coreCustomer, coreCustomer.getId(), coreSystemUnit.getCurrLogFlag(),
                    x4005bo.getOperatorId(), coreCustomer.getCustomerNo(), coreCustomer.getCustomerNo(), null, null);
            }
        }
        else {
            throw new BusinessException("CUS-00000", "此预算单位在系统中不存在!");
        }
        return x4005Vo;
    }

}
