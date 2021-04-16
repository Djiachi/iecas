package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5255Bus;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.issue.CoreCustomerEffectiveCodeDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerEffectiveCode;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerEffectiveCodeSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5255BO;
import com.tansun.ider.model.vo.X5255VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 封锁码历史查询
 * @author: admin
 */
@Service
public class X5255BusImpl implements X5255Bus {

	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private CoreCustomerEffectiveCodeDao coreCustomerEffectiveCodeDao;
	@Autowired
	private HttpQueryService httpQueryService;
	
	
	@Override
	public Object busExecute(X5255BO x5255bo) throws Exception {
		// 客户号、管控层级、起止日期
		// 从客户封锁码历史信息表中查询客户、账户、产品、媒介的封锁码维护记录详情；
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5255bo, eventCommAreaNonFinance);
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		//证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		// 开始日期
		String startDate = eventCommAreaNonFinance.getStartDate();
		// 结束日期
		String endDate = eventCommAreaNonFinance.getEndDate();
		String sceneTriggerObjectCode = eventCommAreaNonFinance.getSceneTriggerObject();
		String customerNo = null;
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreCustomer coreCustomer = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		String operationMode ="";
		if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			 customerNo = coreCustomer.getCustomerNo();
			 operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			 operationMode = coreMediaBasicInfo.getOperationMode();
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
        
		//判断是否输入
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		}
		
		String entrys =Constant.EMPTY_LIST;
		PageBean<X5255VO> page = new PageBean<>();

		CoreCustomerEffectiveCodeSqlBuilder coreCustomerEffectiveCodeSqlBuilder = new CoreCustomerEffectiveCodeSqlBuilder();
		coreCustomerEffectiveCodeSqlBuilder.andCustomerNoEqualTo(customerNo);
		if (StringUtil.isNotBlank(sceneTriggerObjectCode)) {
			coreCustomerEffectiveCodeSqlBuilder.andSceneTriggerObjectCodeEqualTo(sceneTriggerObjectCode);
		}
		// operate_date 维护日期
		if (StringUtil.isNotBlank(startDate)) {
			coreCustomerEffectiveCodeSqlBuilder.andSettingDateGreaterThan(startDate);
		}
		if (StringUtil.isNotBlank(endDate)) {
			coreCustomerEffectiveCodeSqlBuilder.andSettingDateLessThan(endDate);
		}
		coreCustomerEffectiveCodeSqlBuilder.orderBySettingDate(false);
		coreCustomerEffectiveCodeSqlBuilder.orderBySettingTime(false);
		int totalCount = coreCustomerEffectiveCodeDao.countBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5255bo.getPageSize() && null != x5255bo.getIndexNo()) {
			coreCustomerEffectiveCodeSqlBuilder.setPageSize(x5255bo.getPageSize());
			coreCustomerEffectiveCodeSqlBuilder.setIndexNo(x5255bo.getIndexNo());
			page.setPageSize(x5255bo.getPageSize());
			page.setIndexNo(x5255bo.getIndexNo());
		}

		if (totalCount > 0) {
			List<CoreCustomerEffectiveCode>  CoreCustomerEffectiveCodeList  = coreCustomerEffectiveCodeDao
					.selectListBySqlBuilder(coreCustomerEffectiveCodeSqlBuilder);
			List<X5255VO>  x5255VOList = new ArrayList<>();
			for (CoreCustomerEffectiveCode coreCustomerEffectiveCode : CoreCustomerEffectiveCodeList) {
				X5255VO x5255VO = new X5255VO();
				CoreEvent coreEvent = httpQueryService.queryEvent(coreCustomerEffectiveCode.getEventNo());
				CachedBeanCopy.copyProperties(coreCustomerEffectiveCode, x5255VO);
				x5255VO.setEventDesc(coreEvent.getEventDesc());
				CoreEffectivenessCode coreEffectivenessCode = httpQueryService.queryEffectivenessCode(operationMode,
						coreCustomerEffectiveCode.getEffectivenessCodeType(), coreCustomerEffectiveCode.getEffectivenessCodeScene());
				x5255VO.setEffectivenessCodeDesc(coreEffectivenessCode.getEffectivenessCodeDesc());
				x5255VOList.add(x5255VO);
			}
			page.setRows(x5255VOList);
			if(null != CoreCustomerEffectiveCodeList && !CoreCustomerEffectiveCodeList.isEmpty()){
				entrys = CoreCustomerEffectiveCodeList.get(0).getId();
			}
		}
		//记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5255bo.getCoreEventActivityRel().getEventNo(), x5255bo.getCoreEventActivityRel().getActivityNo(),
				tempObject, tempObject, entrys, x5255bo.getOperatorId());
		return page;
	}

}
