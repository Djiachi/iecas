package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5455Bus;
import com.tansun.ider.dao.beta.CoreEffectivenessCodeDao;
import com.tansun.ider.dao.beta.entity.CoreControlProject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.sqlbuilder.CoreEffectivenessCodeSqlBuilder;
import com.tansun.ider.dao.issue.CoreCustomerContrlEventDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlEvent;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlEventSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5265BO;
import com.tansun.ider.model.vo.X5455VO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.web.WSC;


@Service
public class X5455BusImpl implements X5455Bus{

	// 查询 000 全部列表
	private static String QUERY_FLAG_0 = "0";
	// 按照条件查询
	private static String QUERY_FLAG_1 = "1";
	@Autowired
	private CoreCustomerContrlEventDao coreCustomerContrlEventDao;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreCustomerDao CoreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
    private HttpQueryService httpQueryService;
	@Autowired
	private CoreEffectivenessCodeDao coreEffectivenessCodeDao;
	
	@Override
	public Object busExecute(X5265BO X5265BO) throws Exception {
		// 身份证号
		String idNumber = X5265BO.getIdNumber();
		String idType = X5265BO.getIdType();
		// 外部识别号
		String externalIdentificationNo = X5265BO.getExternalIdentificationNo();
		String customerNo = "";
		String operationMode = "";
		// 管控项目
		String controlProjectNo = X5265BO.getControlProjectNo();
		// 管控层级
		String contrlLevel = X5265BO.getContrlLevel();
		// 层级代码
		String levelCode = X5265BO.getLevelCode();
		String startDate = X5265BO.getStartDate();
		String endDate = X5265BO.getEndDate();
		// 管控项目
		// 查询方式 0 查询 000 列表， 1 查询根据条件查询
		String queryFlag = X5265BO.getQueryFlag();
		CoreCustomerContrlEventSqlBuilder coreCustomerContrlEventSqlBuilder = new CoreCustomerContrlEventSqlBuilder();
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
			if (null != coreMediaBasicInfo) {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
				operationMode = coreMediaBasicInfo.getOperationMode();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		}
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = CoreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				customerNo = coreCustomer.getCustomerNo();
				operationMode = coreCustomer.getOperationMode();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		}
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		} else {
			coreCustomerContrlEventSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		
		//查询开始日期，以及结束日期
		if (StringUtil.isNotBlank(startDate)) {
			coreCustomerContrlEventSqlBuilder.andContrlStartDateGreaterThanOrEqualTo(startDate);
		}
		if (StringUtil.isNotBlank(endDate)) {
			coreCustomerContrlEventSqlBuilder.andContrlEndDateLessThanOrEqualTo(endDate);
		}
		
		if (StringUtil.isNotBlank(controlProjectNo)) {
			coreCustomerContrlEventSqlBuilder.andControlProjectNoEqualTo(controlProjectNo);
		}
		if (StringUtil.isNotBlank(contrlLevel)) {
			coreCustomerContrlEventSqlBuilder.andContrlLevelEqualTo(contrlLevel);
		}
		if (StringUtil.isNotBlank(levelCode)) {
			coreCustomerContrlEventSqlBuilder.andLevelCodeEqualTo(levelCode);
		}
		//查询开始日期，以及结束日期
		if (StringUtil.isNotBlank(startDate)) {
			coreCustomerContrlEventSqlBuilder.andContrlStartDateGreaterThanOrEqualTo(startDate);
		}
		if (StringUtil.isNotBlank(endDate)) {
			coreCustomerContrlEventSqlBuilder.andContrlEndDateLessThanOrEqualTo(endDate);
		}
		int totalCount = coreCustomerContrlEventDao.countBySqlBuilder(coreCustomerContrlEventSqlBuilder);
		
		PageBean<X5455VO> page = new PageBean<X5455VO>();
		page.setTotalCount(totalCount);
		if (null != X5265BO.getPageSize() && null != X5265BO.getIndexNo()) {
			coreCustomerContrlEventSqlBuilder.setPageSize(X5265BO.getPageSize());
			coreCustomerContrlEventSqlBuilder.setIndexNo(X5265BO.getIndexNo());
			page.setPageSize(X5265BO.getPageSize());
			page.setIndexNo(X5265BO.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreCustomerContrlEvent> coreCustomerContrlEventList = coreCustomerContrlEventDao.selectListBySqlBuilder(coreCustomerContrlEventSqlBuilder);
			List<X5455VO> list = new ArrayList<X5455VO>();
			for (CoreCustomerContrlEvent coreCustomerContrlEvent : coreCustomerContrlEventList) {
				X5455VO x5455VO = new X5455VO();
				CachedBeanCopy.copyProperties(coreCustomerContrlEvent, x5455VO);
				if (coreCustomerContrlEvent.getContrlLevel().equals("M")) {
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(coreCustomerContrlEvent.getLevelCode());
					CoreMediaBasicInfo coreMediaBasicInfo1 = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
					if (null != coreMediaBasicInfo1) {
						x5455VO.setExternalIdentificationNo(coreMediaBasicInfo1.getExternalIdentificationNo());;
					}
				}
				Map<String, String> coreControlProjectMap = new HashMap<String, String>();
				coreControlProjectMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
				coreControlProjectMap.put(Constant.CONTROL_PROJECT_NO, coreCustomerContrlEvent.getControlProjectNo());
				coreControlProjectMap.put(Constant.PARAM_OPER_MODE, operationMode);
				String key = Constant.PARAMS_FLAG + operationMode + coreCustomerContrlEvent.getControlProjectNo();
				coreControlProjectMap.put(WSC.REDIS_KEY, key);
				CoreControlProject coreControlProject = betaCommonParamService
						.queryCoreControlProject(coreControlProjectMap);
				if(StringUtil.isNotBlank(x5455VO.getCurrencyCode())){
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5455VO.getCurrencyCode());
                    if(null != coreCurrency){
                        x5455VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
				x5455VO.setControlDesc(coreControlProject.getControlDesc());
				x5455VO.setControlField(coreControlProject.getControlField());
				String eventNo  = coreCustomerContrlEvent.getEventNo();
				CoreEvent coreEvent = httpQueryService.queryEvent(eventNo);
				if (null != coreEvent) {
					x5455VO.setEventDesc(coreEvent.getEventDesc());
				}
				list.add(x5455VO);
			}
			page.setRows(list);
		}
		return page;
	}
	
}
