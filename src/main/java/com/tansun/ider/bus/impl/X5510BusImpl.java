package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5510Bus;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5510BO;
import com.tansun.ider.model.vo.X5510VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * @version:1.0
 * @Description: 客户已有封锁码查询
 * @author: admin
 */
@Service
public class X5510BusImpl implements X5510Bus {

    @Autowired
    private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
    @Autowired
    private QueryCustomerService queryCustomerService;
    @Resource
    private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
    @Autowired
    private HttpQueryService httpQueryService;  

    @Override
    public Object busExecute(X5510BO x5510bo) throws Exception {

        String externalIdentificationNo = x5510bo.getExternalIdentificationNo();
        String idNumber = x5510bo.getIdNumber();
        String idType = x5510bo.getIdType();
        // 查询方式 0 查询 000 列表， 1 查询根据条件查询
     	String queryFlag = x5510bo.getQueryFlag();
     	String effectivenessCodeType = x5510bo.getEffectivenessCodeType();
     	String effectivenessCodeScene = x5510bo.getEffectivenessCodeScene();
        // 查询出客户客户号
        String customerNo = null;
        String operationMode = null;
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if(object instanceof CoreCustomer){
            CoreCustomer coreCustomer = (CoreCustomer)object;
            customerNo = coreCustomer.getCustomerNo();
            operationMode = coreCustomer.getOperationMode();
        }else if(object instanceof CoreMediaBasicInfo){
             coreMediaBasicInfo = (CoreMediaBasicInfo)object;
            customerNo = coreMediaBasicInfo.getMainCustomerNo();
            operationMode = coreMediaBasicInfo.getOperationMode();
        }

        PageBean<X5510VO> page = new PageBean<>();
        CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
        if (StringUtil.isNotBlank(queryFlag) && queryFlag.equals("1")) {
        	coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.NO.getValue());
		}else {
			coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
		}
        coreCustomerEffectiveCodeSqlBuilder.andStateEqualTo(YesOrNo.YES.getValue());
        coreCustomerEffectiveCodeSqlBuilder.orderBySettingDate(false);
        coreCustomerEffectiveCodeSqlBuilder.orderBySettingTime(false);
        coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
        if (StringUtil.isNotBlank(x5510bo.getQueryControl()) && YesOrNo.YES.getValue().equals(x5510bo.getQueryControl())) {
        	coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeSceneEqualTo(effectivenessCodeScene);
        	coreCustomerEffectiveCodeSqlBuilder.andEffectivenessCodeTypeEqualTo(effectivenessCodeType);
		}
       
        if (null != x5510bo.getPageSize() && null != x5510bo.getIndexNo()) {
        	coreCustomerEffectiveCodeSqlBuilder.setPageSize(x5510bo.getPageSize());
        	coreCustomerEffectiveCodeSqlBuilder.setIndexNo(x5510bo.getIndexNo());
            page.setPageSize(x5510bo.getPageSize());
            page.setIndexNo(x5510bo.getIndexNo());
        }
        
        int totalCount = coreCustomerEffectiveCodeDao.countBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
        
        page.setTotalCount(totalCount);
        //查询列表
        if (totalCount > 0) {
            List<CoreCustomerEffectiveCode>  coreCustomerEffectiveCodeList = coreCustomerEffectiveCodeDao.selectListBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
            List<X5510VO> listX5510VO = new ArrayList<X5510VO>();
            if (null!= coreCustomerEffectiveCodeList && !coreCustomerEffectiveCodeList.isEmpty()) {
                for (CoreCustomerEffectiveCode coreCustomerEffectiveCode : coreCustomerEffectiveCodeList) {
                    X5510VO x5510VO = new X5510VO();
                    CachedBeanCopy.copyProperties(coreCustomerEffectiveCode, x5510VO);
                    CoreEvent coreEvent = httpQueryService.queryEvent(coreCustomerEffectiveCode.getEventNo());
                    CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(operationMode, coreCustomerEffectiveCode.getEffectivenessCodeType(),
                    		coreCustomerEffectiveCode.getEffectivenessCodeScene());
                    x5510VO.setEffectivenessCodeDesc(coreEffectivenessCode.getEffectivenessCodeDesc());
                    x5510VO.setEventDesc(coreEvent.getEventDesc());
                    x5510VO.setOperationMode(operationMode);
                    if(StringUtil.isNotBlank(x5510VO.getCurrencyCode())){
                    	CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5510VO.getCurrencyCode());
                    	if(null != coreCurrency){
                    		x5510VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    	}
                    }
                    listX5510VO.add(x5510VO);
                }
                page.setRows(listX5510VO);
            }
        }
        return page;
    }

}
