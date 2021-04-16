package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5540Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreFeeItem;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOperationMode;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5540BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;

/**
 * 客户费用信息查询
 * @ClassName X5540BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2019年9月19日 下午2:15:22
 * @version 5.0.0
 */
@Service
public class X5540BusImpl implements X5540Bus {

    @Resource
    private CoreCustomerDao coreCustomerDao;
    @Resource
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
    @Resource
    private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
    @Autowired
    private HttpQueryService httpQueryService;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
    private QueryCustomerService queryCustomerService;

    @Override
    public Object busExecute(X5540BO x5540bo) throws Exception {
        String idNumber = x5540bo.getIdNumber();
        String idType = x5540bo.getIdType();
        String externalIdentificationNo = x5540bo.getExternalIdentificationNo();
        String customerNo = null;
        String entrys =Constant.EMPTY_LIST;
        String operationMode = "";
        PageBean<X5540BO> page = new PageBean<>();
        Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
        if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
			operationMode = coreMediaBasicInfo.getOperationMode();
		}
        
        CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
        if (StringUtil.isNotEmpty(customerNo)) {
            coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
        }
        if (StringUtil.isNotEmpty(x5540bo.getPeriodicFeeIdentifier())) {
            if(Constant.CUST_CYCLE_FEE_FLAG.equals(x5540bo.getPeriodicFeeIdentifier())){
                //查询客户服务费，根据客户号查询客户费用信息表中周期类费用标识为C，且实例化代码1=运营币种
                CoreOperationMode coreOperationMode = httpQueryService.queryOperationMode(operationMode);
                coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode1EqualTo(coreOperationMode.getAccountCurrency());
                coreCustomerWaiveFeeInfoSqlBuilder.andPeriodicFeeIdentifierEqualTo(x5540bo.getPeriodicFeeIdentifier());// 不等于N,及查询周期费用
            }else if(Constant.PROT_CYCLE_FEE_FLAG.equals(x5540bo.getPeriodicFeeIdentifier())){
                //查询产品服务费，根据客户号+产品代码查询
                String productObjectCode = x5540bo.getProductObjectCode();
                if(StringUtil.isNotBlank(productObjectCode)){
                    //产品代码不为空，查询客户费用信息表中该客户周期类费用标识为P，且实例化代码1=产品代码
                    coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode1EqualTo(productObjectCode);
                }
                coreCustomerWaiveFeeInfoSqlBuilder.andPeriodicFeeIdentifierEqualTo(x5540bo.getPeriodicFeeIdentifier());// 不等于N,及查询周期费用
            }else if(Constant.NO_CYCLE_FEE_FLAG.equals(x5540bo.getPeriodicFeeIdentifier())){
                CoreCustomerWaiveFeeInfoSqlBuilder sqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
                sqlBuilder.orPeriodicFeeIdentifierEqualTo(x5540bo.getPeriodicFeeIdentifier());
                sqlBuilder.orPeriodicFeeIdentifierIsNull();
                coreCustomerWaiveFeeInfoSqlBuilder.and(sqlBuilder);
            }else if("!N".equals(x5540bo.getPeriodicFeeIdentifier())){
                coreCustomerWaiveFeeInfoSqlBuilder.andPeriodicFeeIdentifierNotEqualTo(Constant.NO_CYCLE_FEE_FLAG);
            }
        }
        int totalCount = coreCustomerWaiveFeeInfoDao.countBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
        page.setTotalCount(totalCount);
        if (null != x5540bo.getPageSize() && null != x5540bo.getIndexNo()) {
            coreCustomerWaiveFeeInfoSqlBuilder.orderByFeeItemNo(false);
            coreCustomerWaiveFeeInfoSqlBuilder.setPageSize(x5540bo.getPageSize());
            coreCustomerWaiveFeeInfoSqlBuilder.setIndexNo(x5540bo.getIndexNo());
            page.setPageSize(x5540bo.getPageSize());
            page.setIndexNo(x5540bo.getIndexNo());
        }
        if (totalCount > 0) {
        	List<X5540BO> x5540BOList = new ArrayList<>();
            List<CoreCustomerWaiveFeeInfo> list = coreCustomerWaiveFeeInfoDao
                    .selectListBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
            for (CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo : list) {
            	X5540BO x5540BO = new X5540BO();
            	CachedBeanCopy.copyProperties(coreCustomerWaiveFeeInfo, x5540BO);
                // 金额转换及描述返回
                amountConversion(coreCustomerWaiveFeeInfo, coreCustomerWaiveFeeInfo.getCurrencyCode(),x5540BO,operationMode);
                x5540BOList.add(x5540BO);
            }
            page.setRows(x5540BOList);
			if(null != list && !list.isEmpty()){
				entrys = list.get(0).getId();
			}
			//记录查询日志
			CoreEvent tempObject = new CoreEvent();
			paramsUtil.logNonInsert(x5540bo.getCoreEventActivityRel().getEventNo(), x5540bo.getCoreEventActivityRel().getActivityNo(),
					tempObject, tempObject, entrys, x5540bo.getOperatorId());
        }
        return page;
    }

    private void amountConversion(CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo, String currencyCode,X5540BO x5540BO,String operationMode)
            throws Exception {
        CoreCurrency coreCurrency = httpQueryService.queryCurrency(currencyCode);
        int decimalPlaces = coreCurrency.getDecimalPosition();
        if(null != coreCurrency){
        	x5540BO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
        }
        if (coreCustomerWaiveFeeInfo.getAccumultWaiveAmt() != null) {
            BigDecimal accumultWaiveAmt = CurrencyConversionUtil.reduce(coreCustomerWaiveFeeInfo.getAccumultWaiveAmt(),
                    decimalPlaces);
            x5540BO.setAccumultWaiveAmt(accumultWaiveAmt);
        }
        if (coreCustomerWaiveFeeInfo.getLastWaiveAmt() != null) {
            BigDecimal lastWaiveAmt = CurrencyConversionUtil.reduce(coreCustomerWaiveFeeInfo.getLastWaiveAmt(),
                    decimalPlaces);
            x5540BO.setLastWaiveAmt(lastWaiveAmt);
        }
        if (coreCustomerWaiveFeeInfo.getAccumulatedCollectionAmount() != null) {
            BigDecimal accumulatedCollectionAmount = CurrencyConversionUtil.reduce(coreCustomerWaiveFeeInfo.getAccumulatedCollectionAmount(),
                    decimalPlaces);
            x5540BO.setAccumulatedCollectionAmount(accumulatedCollectionAmount);
        }
        if(StringUtil.isNotBlank(x5540BO.getFeeItemNo())){
        	CoreFeeItem coreFeeItem = httpQueryService.queryFeeItem(x5540BO.getFeeItemNo());
        	String instanCode1 = "";
        	String instanCode2 = "";
        	String instanCode3 = "";
        	String instanCode4 = "";
        	String instanCode5 = "";
        	if(null != coreFeeItem){
        		x5540BO.setFeeDesc(coreFeeItem.getFeeDesc());
        		instanCode1 = coreFeeItem.getInstanCode1();
        		instanCode2 = coreFeeItem.getInstanCode2();
        		instanCode3 = coreFeeItem.getInstanCode3();
        		instanCode4 = coreFeeItem.getInstanCode4();
        		instanCode5 = coreFeeItem.getInstanCode5();
        	}
        	if(StringUtil.isNotBlank(instanCode1)){
        		if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5540BO.getInstanCode1())){
        			x5540BO.setInstanDesc1(getInstanCodeDesc(operationMode,x5540BO.getInstanCode1(),instanCode1));
        		}
        	}
        	if(StringUtil.isNotBlank(instanCode2)){
        		if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5540BO.getInstanCode2())){
        			x5540BO.setInstanDesc2(getInstanCodeDesc(operationMode,x5540BO.getInstanCode2(),instanCode2));
        		}
        	}
        	if(StringUtil.isNotBlank(instanCode3)){
        		if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5540BO.getInstanCode3())){
        			x5540BO.setInstanDesc3(getInstanCodeDesc(operationMode,x5540BO.getInstanCode3(),instanCode3));
        		}
        	}
        	if(StringUtil.isNotBlank(instanCode4)){
        		if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5540BO.getInstanCode4())){
        			x5540BO.setInstanDesc4(getInstanCodeDesc(operationMode,x5540BO.getInstanCode4(),instanCode4));
        		}
        	}
        	if(StringUtil.isNotBlank(instanCode5)){
        		if(StringUtil.isNotBlank(operationMode) && StringUtil.isNotBlank(x5540BO.getInstanCode5())){
        			x5540BO.setInstanDesc5(getInstanCodeDesc(operationMode,x5540BO.getInstanCode5(),instanCode5));
        		}
        	}
        }
        
    }
    
    /**
     * 获取实例化维度描述
     * 
     * @param 
     * @return
     * @throws Exception
     */
	private String getInstanCodeDesc(String operationMode,String instanCode,String instanCodeC) throws Exception {
		String instanDesc = "";
		if("MODT".equals(instanCodeC)){
			CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode, instanCode);
			if (null != coreBusinessType) {
				instanDesc = coreBusinessType.getBusinessDesc();
			}
		}else if("MODP".equals(instanCodeC)){
			CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, instanCode);
			if (null != coreProductObject) {
				instanDesc = coreProductObject.getProductDesc();
			}
		}else if("MODM".equals(instanCodeC)){
			CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(operationMode, instanCode);
			if (null != coreMediaObject) {
				instanDesc = coreMediaObject.getMediaObjectDesc();
			}
		}else if("CURR".equals(instanCodeC)){
			CoreCurrency coreCurrency = httpQueryService.queryCurrency(instanCode);
			if(null != coreCurrency){
				instanDesc = coreCurrency.getCurrencyDesc();
			}
		}else if("MODG".equals(instanCodeC)){
			CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode, instanCode);
			if (null != coreBusinessProgram) {
				instanDesc = coreBusinessProgram.getProgramDesc();
			}
		}
		return instanDesc;
	}

}
