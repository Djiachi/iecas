package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5765Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.ClrProcessingVisaDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingVisa;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingVisaSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5760BO;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CurrencyUtils;
import com.tansun.ider.util.ParamsUtil;
/**
 * VISA拒付查询
 * 
 * @ClassName X5765BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月15日 下午5:09:37
 * @version 1.0.0
 */

@Service
public class X5765BusImpl implements X5765Bus {
	
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private ClrProcessingVisaDao clrProcessingVisaDao;
	@Autowired
	private CurrencyUtils currencyUtils;
	@Autowired
    private QueryCustomerService queryCustomerService;
	@Override
	public Object busExecute(X5760BO x5760bo) throws Exception {
		String idNumber = x5760bo.getIdNumber();
		String idType = x5760bo.getIdType();
		String externalIdentificationNo = x5760bo.getExternalIdentificationNo();
		String customerNo = "";
		String operationMode = "";
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
//		if (StringUtil.isNotEmpty(idNumber)) {
//			// 根据证件号查询客户号
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (coreCustomer == null) {
//				// 抛出异常CUS-00005客户信息查询失败
//				throw new BusinessException("CUS-00005");
//			}
//			customerNo = coreCustomer.getCustomerNo();
//			operationMode = coreCustomer.getOperationMode();
//		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
//			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
//					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//			if (coreMediaBasicInfo == null) {
//				// 抛出异常CUS-00005客户信息查询失败
//				throw new BusinessException("CUS-00014", "媒介单元基本信息");
//			}
//			customerNo = coreMediaBasicInfo.getMainCustomerNo();
//			operationMode = coreMediaBasicInfo.getOperationMode();
//		}
		
		PageBean<ClrProcessingVisa> page = new PageBean<>();
		
		ClrProcessingVisaSqlBuilder clrProcessingVisaSqlBuilder = new ClrProcessingVisaSqlBuilder();
		clrProcessingVisaSqlBuilder.andCustomerNoEqualTo(customerNo);
		ClrProcessingVisa clrProcessingVisa = clrProcessingVisaDao.selectBySqlBuilder(clrProcessingVisaSqlBuilder);
		int total = clrProcessingVisaDao.countBySqlBuilder(clrProcessingVisaSqlBuilder);
		page.setTotalCount(total);
		if (null != x5760bo.getPageSize() && null != x5760bo.getIndexNo()) {
			clrProcessingVisaSqlBuilder.setPageSize(x5760bo.getPageSize());
			clrProcessingVisaSqlBuilder.setIndexNo(x5760bo.getIndexNo());
			page.setPageSize(x5760bo.getPageSize());
			page.setIndexNo(x5760bo.getIndexNo());
		}
		String entrys =Constant.EMPTY_LIST;
		if (total > 0) {
			List<ClrProcessingVisa> list = clrProcessingVisaDao.selectListBySqlBuilder(clrProcessingVisaSqlBuilder);
			BigDecimal transAmt = currencyUtils.conversionAmount(list.get(0).getTransAmt(), list.get(0).getTransCurrCode(),
					CurrencyUtils.output);
			BigDecimal clearAmount = currencyUtils.conversionAmount(list.get(0).getClearAmount(), list.get(0).getClearCurrCode(),
					CurrencyUtils.output);
			list.get(0).setTransAmt(transAmt);
			list.get(0).setClearAmount(clearAmount);
			page.setRows(list);
			
			entrys = clrProcessingVisa.getId();
			
		}
		//记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5760bo.getCoreEventActivityRel().getEventNo(), x5760bo.getCoreEventActivityRel().getActivityNo(),
				tempObject, tempObject, entrys, x5760bo.getOperatorId());
		return page;
	}
}
