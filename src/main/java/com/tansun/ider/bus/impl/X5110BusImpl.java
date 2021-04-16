package com.tansun.ider.bus.impl;

import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5110Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5110BO;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 客户地址资料查询
 * @author: admin
 */
@Service
public class X5110BusImpl implements X5110Bus {

	@Autowired
	private CoreCustomerAddrDao coreCustomerAddrDao;
//	@Autowired
//	private CoreCustomerDao coreCustomerDao;
//	@Autowired
//	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private ParamsUtil paramsUtil;

	@Override
	public Object busExecute(X5110BO x5110bo) throws Exception {
		SpringUtil.getBean(ValidatorUtil.class).validate(x5110bo);
		// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5110bo, eventCommAreaNonFinance);
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		String idType = x5110bo.getIdType();
		String idNumber = x5110bo.getIdNumber();
		String customerNo  = "";
		PageBean<CoreCustomerAddr> page = new PageBean<>();
		CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
		CoreCustomer coreCustomer = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(customerNo);
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
				eventCommAreaNonFinance.setCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
				coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(customerNo);
			}
		}

//		if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
//			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//			coreMediaBasicInfoSqlBuilder
//					.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
//					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//			if (null != coreMediaBasicInfo) {
//				customerNo = coreMediaBasicInfo.getMainCustomerNo();
//				eventCommAreaNonFinance.setCustomerNo(coreMediaBasicInfo.getMainCustomerNo());
//			} else {
//				throw new BusinessException("CUS-00014", "媒介基本");
//			}
//		}
//		CoreCustomerSqlBuilder coreCustomerSqlBuilderStr = new CoreCustomerSqlBuilder();
		// if (eventCommAreaNonFinance.getCustomerNo() != null) {
		// coreCustomerSqlBuilderStr.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
		// }
//		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
//			coreCustomer  = queryCustomerService.queryCoreCustomer(idType, idNumber);
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
//			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null != coreCustomer) {
//				customerNo = coreCustomer.getCustomerNo();
//			} else {
//				throw new BusinessException("CUS-00014", "客户基本");
//			}
//		}
		
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		}
		
		page.setObj(coreCustomer);

		if (eventCommAreaNonFinance.getMobilePhone() != null) {
			coreCustomerAddrSqlBuilder.andContactMobilePhoneEqualTo(eventCommAreaNonFinance.getMobilePhone());
		}
		if (eventCommAreaNonFinance.getCustomerNo() != null) {
			coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
		}

		int totalCount = coreCustomerAddrDao.countBySqlBuilder(coreCustomerAddrSqlBuilder);
		page.setTotalCount(totalCount);

		if (null != x5110bo.getPageSize() && null != x5110bo.getIndexNo()) {
			coreCustomerAddrSqlBuilder.setPageSize(x5110bo.getPageSize());
			coreCustomerAddrSqlBuilder.setIndexNo(x5110bo.getIndexNo());
			page.setPageSize(x5110bo.getPageSize());
			page.setIndexNo(x5110bo.getIndexNo());
		}
		String entrys = Constant.EMPTY_LIST;
		if (totalCount > 0) {
			List<CoreCustomerAddr> liscCoreCustomerAddr = coreCustomerAddrDao
					.selectListBySqlBuilder(coreCustomerAddrSqlBuilder);
			page.setRows(liscCoreCustomerAddr);
			if (null != liscCoreCustomerAddr && !liscCoreCustomerAddr.isEmpty()) {
				entrys = liscCoreCustomerAddr.get(0).getId();
			}
		}
		// 记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5110bo.getCoreEventActivityRel().getEventNo(),
				x5110bo.getCoreEventActivityRel().getActivityNo(), tempObject, tempObject, entrys,
				x5110bo.getOperatorId());
		return page;
	}

}
