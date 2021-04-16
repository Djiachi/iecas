package com.tansun.ider.bus.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5780Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.ClrProcessingMcDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.ClrProcessingMc;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.ClrProcessingMcSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5775BO;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * MC拒付查询
 * 
 * @ClassName X5780BusImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年2月20日 下午5:09:37
 * @version 1.0.0
 */
@Service
public class X5780BusImpl implements X5780Bus {
	
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private ClrProcessingMcDao clrProcessingMcDao;
	@Autowired
    private QueryCustomerService queryCustomerService;
	@Override
	public Object busExecute(X5775BO x5775bo) throws Exception {
		String idNumber = x5775bo.getIdNumber();
		String idType = x5775bo.getIdType();
		String externalIdentificationNo = x5775bo.getExternalIdentificationNo();
		String customerNo = "";
		@SuppressWarnings("unused")
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
		
		PageBean<ClrProcessingMc> page = new PageBean<>();
		
		ClrProcessingMcSqlBuilder clrProcessingMcSqlBuilder = new ClrProcessingMcSqlBuilder();
		clrProcessingMcSqlBuilder.andCustomerNoEqualTo(customerNo);
		ClrProcessingMc clrProcessingMc = clrProcessingMcDao.selectBySqlBuilder(clrProcessingMcSqlBuilder);
		int total = clrProcessingMcDao.countBySqlBuilder(clrProcessingMcSqlBuilder);
		page.setTotalCount(total);
		if (null != x5775bo.getPageSize() && null != x5775bo.getIndexNo()) {
			clrProcessingMcSqlBuilder.setPageSize(x5775bo.getPageSize());
			clrProcessingMcSqlBuilder.setIndexNo(x5775bo.getIndexNo());
			page.setPageSize(x5775bo.getPageSize());
			page.setIndexNo(x5775bo.getIndexNo());
		}
		String entrys =Constant.EMPTY_LIST;
		if (total > 0) {
			List<ClrProcessingMc> list = clrProcessingMcDao.selectListBySqlBuilder(clrProcessingMcSqlBuilder);
			page.setRows(list);
			
			entrys = clrProcessingMc.getId();
			
		}
		//记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5775bo.getCoreEventActivityRel().getEventNo(), x5775bo.getCoreEventActivityRel().getActivityNo(),
				tempObject, tempObject, entrys, x5775bo.getOperatorId());
		return page;
	}
}