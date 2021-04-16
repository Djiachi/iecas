package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5434Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreDelayActivityLogBDao;
import com.tansun.ider.dao.issue.CoreDelayActivityLogDao;
import com.tansun.ider.dao.issue.CoreDelayHistDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreDelayActivityLog;
import com.tansun.ider.dao.issue.entity.CoreDelayActivityLogB;
import com.tansun.ider.dao.issue.entity.CoreDelayHist;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreDelayActivityLogBSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreDelayActivityLogSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreDelayHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5435BO;
import com.tansun.ider.model.vo.X5434VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * 延滞冲减查询
 * @author qianyp
 */

@Service
public class X5434BusImpl implements X5434Bus {
	
	@Autowired
	private CoreDelayActivityLogDao coreDelayActivityLogDao;
	@Autowired
	private CoreDelayActivityLogBDao coreDelayActivityLogBDao;
	@Autowired
	private CoreDelayHistDao coreDelayHistDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	
	public Object busExecute(X5435BO x5435bo) throws Exception {
		String customerNo = x5435bo.getCustomerNo();
		String globalSerialNumbr = x5435bo.getGlobalSerialNumbr();
		
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		PageBean<X5434VO> page = new PageBean<>();
		int totalCount=0;
		List<X5434VO> listX5434VO = new ArrayList<X5434VO>();
		//查询活动日志
		if("A".equals(coreSystemUnit.getCurrLogFlag())){
			CoreDelayActivityLogSqlBuilder coreDelayActivityLogSqlBuilder = new CoreDelayActivityLogSqlBuilder();
			coreDelayActivityLogSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
			totalCount=coreDelayActivityLogDao.countBySqlBuilder(coreDelayActivityLogSqlBuilder);
			if (null != x5435bo.getPageSize() && null != x5435bo.getIndexNo()) {
				 coreDelayActivityLogSqlBuilder.setPageSize(x5435bo.getPageSize());
				 coreDelayActivityLogSqlBuilder.setIndexNo(x5435bo.getIndexNo());
		         page.setPageSize(x5435bo.getPageSize());
		         page.setIndexNo(x5435bo.getIndexNo());
		     }
			page.setTotalCount(totalCount);
			if(totalCount>0){
				List<CoreDelayActivityLog> list = coreDelayActivityLogDao.selectListBySqlBuilder(coreDelayActivityLogSqlBuilder);
				for(CoreDelayActivityLog coreDelayActivityLog : list){
					X5434VO x5434vo = new X5434VO();
				    CachedBeanCopy.copyProperties(coreDelayActivityLog, x5434vo);
				    listX5434VO.add(x5434vo);
				}
			}
		}else if("B".equals(coreSystemUnit.getCurrLogFlag())){
			CoreDelayActivityLogBSqlBuilder coreDelayActivityLogBSqlBuilder = new CoreDelayActivityLogBSqlBuilder();
			coreDelayActivityLogBSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
			totalCount=coreDelayActivityLogBDao.countBySqlBuilder(coreDelayActivityLogBSqlBuilder);
			if (null != x5435bo.getPageSize() && null != x5435bo.getIndexNo()) {
				coreDelayActivityLogBSqlBuilder.setPageSize(x5435bo.getPageSize());
				coreDelayActivityLogBSqlBuilder.setIndexNo(x5435bo.getIndexNo());
		        page.setPageSize(x5435bo.getPageSize());
		        page.setIndexNo(x5435bo.getIndexNo());
		    }
			page.setTotalCount(totalCount);
			if(totalCount>0){
				List<CoreDelayActivityLogB> list = coreDelayActivityLogBDao.selectListBySqlBuilder(coreDelayActivityLogBSqlBuilder);
				for(CoreDelayActivityLogB coreDelayActivityLogB : list){
					X5434VO x5434vo = new X5434VO();
				    CachedBeanCopy.copyProperties(coreDelayActivityLogB, x5434vo);
				    listX5434VO.add(x5434vo);
				}
			}
		}
		if(listX5434VO==null || listX5434VO.size()<=0){
			//查询延滞历史表
			CoreDelayHistSqlBuilder coreDelayHistSqlBuilder = new CoreDelayHistSqlBuilder();
			coreDelayHistSqlBuilder.andGlobalSerialNumbrEqualTo(globalSerialNumbr);
			totalCount=coreDelayHistDao.countBySqlBuilder(coreDelayHistSqlBuilder);
			if (null != x5435bo.getPageSize() && null != x5435bo.getIndexNo()) {
				coreDelayHistSqlBuilder.setPageSize(x5435bo.getPageSize());
				coreDelayHistSqlBuilder.setIndexNo(x5435bo.getIndexNo());
		        page.setPageSize(x5435bo.getPageSize());
		        page.setIndexNo(x5435bo.getIndexNo());
		    }
			page.setTotalCount(totalCount);
			if(totalCount>0){
				List<CoreDelayHist> list = coreDelayHistDao.selectListBySqlBuilder(coreDelayHistSqlBuilder);
				for(CoreDelayHist coreDelayHist : list){
					X5434VO x5434vo = new X5434VO();
				    CachedBeanCopy.copyProperties(coreDelayHist, x5434vo);
				    listX5434VO.add(x5434vo);
				}
			}
		}
		if(listX5434VO !=null && listX5434VO.size()>0){
			for(X5434VO x5434vo:listX5434VO){
				//查询层级代码描述
				if (StringUtil.isNotBlank(x5434vo.getDelinquencyLevel()) && StringUtil.isNotBlank(x5434vo.getLevelCode())) {
                	if(x5434vo.getDelinquencyLevel().equals("A")){
                		CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(coreCustomer.getOperationMode(),x5434vo.getLevelCode());
                		if(null != coreBusinessType){
                			x5434vo.setBusinessDesc(coreBusinessType.getBusinessDesc());
                		}
                	}else if(x5434vo.getDelinquencyLevel().equals("G")){
                		CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(coreCustomer.getOperationMode(),x5434vo.getLevelCode());
                		if(null != coreBusinessProgram){
                			x5434vo.setProgramDesc(coreBusinessProgram.getProgramDesc());
                		}
                	}else if (x5434vo.getDelinquencyLevel().equals("P")) {
                		CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(coreCustomer.getOperationMode(),x5434vo.getLevelCode());
                		if(null != coreBusinessProgram){
                			x5434vo.setProductLevelCodeDesc(coreBusinessProgram.getProgramDesc());
                		}
					}
               }
			   if (StringUtil.isNotBlank(x5434vo.getProductObjectNo())) {
                    CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreCustomer.getOperationMode(),x5434vo.getProductObjectNo());
                    if (coreProductObject != null) {
                    	x5434vo.setProductDesc(coreProductObject.getProductDesc());
                    }
               }
			   if(StringUtil.isNotBlank(x5434vo.getCurrencyCode())){
				   CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5434vo.getCurrencyCode());
			       int decimalPlaces = coreCurrency.getDecimalPosition();
			       if(x5434vo.getMinPaymentChange()!=null){
			    	   BigDecimal minPaymentChange = CurrencyConversionUtil.reduce(x5434vo.getMinPaymentChange(),decimalPlaces);
			    	   x5434vo.setMinPaymentChange(minPaymentChange);
			       }
			       if(coreCurrency.getCurrencyDesc()!=null){
			    	   x5434vo.setCurrencyDesc(coreCurrency.getCurrencyDesc());
			       }
			   }
			}
			page.setRows(listX5434VO);
		}
		return page;
	}

}
