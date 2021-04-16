package com.tansun.ider.bus.impl;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5119Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountBalanceCheckDao;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceCheck;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceCheckSqlBuilder;
import com.tansun.ider.dao.nonfinance.mapper.AccountBalanceCheckMapper;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5115BO;
import com.tansun.ider.model.vo.X5119VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.util.CachedBeanCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version:1.0
 * @Description: 账户余额平衡查询
 * @author: gaozhennan
 */
@Service
public class X5119BusImpl implements X5119Bus {
    private static Logger logger = LoggerFactory.getLogger(X5119BusImpl.class);

    @Autowired
    private QueryCustomerService queryCustomerService;
    @Autowired
    private CoreAccountBalanceCheckDao coreAccountBalanceCheckDao;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private AccountBalanceCheckMapper accountBalanceCheckMapper;
    /**
     * 来源授权
     **/
    private static String AUTH = "A";
    /**
     * 来源发卡
     **/
    private static String CARD = "C";

    @Override

    public Object busExecute(X5115BO x5115bo) throws Exception {
        PageBean<X5119VO> page = new PageBean<>();
        logger.info("pageSize:{},IndexNo:{},indexNos:{}",x5115bo.getPageSize(),x5115bo.getIndexNo(),x5115bo.getIdNumber());
        // 身份证号
        Object object = null;
        if(!StringUtil.isBlank(x5115bo.getIdNumber())&& !StringUtil.isBlank(x5115bo.getIdType())){
            String idNumber =  x5115bo.getIdNumber();
            String idType = x5115bo.getIdType();
            object = queryCustomerService.queryCustomer(idType, idNumber, null);
        }
        if(!StringUtil.isBlank(x5115bo.getExternalIdentificationNo())){
            String externalIdentificationNo = x5115bo.getExternalIdentificationNo();
            object = queryCustomerService.queryCustomer(null, null, externalIdentificationNo);
        }


        String customerNo = null;
        String operationMode = null;
        CoreMediaBasicInfo coreMediaBasicInfo = null;
        String productObjectCode = null;

//        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
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
        List<X5119VO> listX5119VO = new ArrayList<>();
        CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder = new CoreAccountBalanceCheckSqlBuilder();
        if(!StringUtil.isBlank(customerNo)){
            coreAccountBalanceCheckSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        int count = coreAccountBalanceCheckDao.countBySqlBuilder(coreAccountBalanceCheckSqlBuilder);
        page.setTotalCount(count);

        if (count > 0) {
            List<CoreAccountBalanceCheck> coreAccountBalanceCheckList = null;
            if (null != x5115bo.getPageSize() && null != x5115bo.getIndexNo()) {
                coreAccountBalanceCheckSqlBuilder.setIndexNo(x5115bo.getIndexNo());
                coreAccountBalanceCheckSqlBuilder.setPageSize(x5115bo.getPageSize());
                page.setPageSize(x5115bo.getPageSize());
                page.setIndexNo(x5115bo.getIndexNo());
                coreAccountBalanceCheckList = coreAccountBalanceCheckDao.selectListBySqlBuilder(coreAccountBalanceCheckSqlBuilder);
            }else{
                coreAccountBalanceCheckList = accountBalanceCheckMapper.selectBySqlBuilder(coreAccountBalanceCheckSqlBuilder);

            }

            if(!CollectionUtils.isEmpty(coreAccountBalanceCheckList)){
                for (CoreAccountBalanceCheck coreAccountBalanceCheck : coreAccountBalanceCheckList) {
                    X5119VO x5119VO = new X5119VO();
                    CachedBeanCopy.copyProperties(coreAccountBalanceCheck, x5119VO);

                    // 查询业务项目描述
                    if(StringUtil.isBlank(operationMode)){
                        operationMode= x5115bo.getOperationMode();
                    }
                    CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode,
                            coreAccountBalanceCheck.getBusinessTypeCode());
                    x5119VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
//                x5119VO.setBusinessDesc(coreAccountBalanceCheck.getBusinessProgramNo());
                    CoreProductObject coreProductObject = httpQueryService
                            .queryProductObject(operationMode, coreAccountBalanceCheck.getProductObjectCode());
                    if (coreProductObject != null) {
                        if(logger.isDebugEnabled()){
                            logger.debug(coreProductObject.toString());
                        }
                        x5119VO.setProductDesc(coreProductObject.getProductDesc());
                    }
                    CoreBusinessProgram coreBusinessProgram = httpQueryService
                            .queryBusinessProgram(operationMode, coreAccountBalanceCheck.getBusinessProgramNo());
                    if (coreBusinessProgram != null) {
                        x5119VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
                    }
                    listX5119VO.add(x5119VO);
                }
            }


            page.setRows(listX5119VO);
            logger.info("totle:{}",page.getTotalCount());
        }
        return page;
    }
}

